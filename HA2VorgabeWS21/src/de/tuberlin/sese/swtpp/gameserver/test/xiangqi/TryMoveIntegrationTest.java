package de.tuberlin.sese.swtpp.gameserver.test.xiangqi;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.tuberlin.sese.swtpp.gameserver.control.GameController;
import de.tuberlin.sese.swtpp.gameserver.model.Game;
import de.tuberlin.sese.swtpp.gameserver.model.Player;
import de.tuberlin.sese.swtpp.gameserver.model.User;

public class TryMoveIntegrationTest {


	User user1 = new User("Alice", "alice");
	User user2 = new User("Bob", "bob");
	
	Player redPlayer = null;
	Player blackPlayer = null;
	Game game = null;
	GameController controller;
	
	@Before
	public void setUp() throws Exception {
		controller = GameController.getInstance();
		controller.clear();
		
		int gameID = controller.startGame(user1, "", "xiangqi");
		
		game =  controller.getGame(gameID);
		redPlayer = game.getPlayer(user1);

	}
	
	public void startGame() {
		controller.joinGame(user2, "xiangqi");		
		blackPlayer = game.getPlayer(user2);
	}
	
	public void startGame(String initialBoard, boolean redNext) {
		startGame();
		
		game.setBoard(initialBoard);
		game.setNextPlayer(redNext? redPlayer:blackPlayer);
	}
	
	public void assertMove(String move, boolean red, boolean expectedResult) {
		if (red)
			assertEquals(expectedResult, game.tryMove(move, redPlayer));
		else 
			assertEquals(expectedResult,game.tryMove(move, blackPlayer));
	}
	
	public void assertGameState(String expectedBoard, boolean redNext, boolean finished, boolean redWon) {
		assertEquals(expectedBoard,game.getBoard());
		assertEquals(finished, game.isFinished());

		if (!game.isFinished()) {
			assertEquals(redNext, game.getNextPlayer() == redPlayer);
		} else {
			assertEquals(redWon, redPlayer.isWinner());
			assertEquals(!redWon, blackPlayer.isWinner());
		}
	}
	

	/*******************************************
	 * !!!!!!!!! To be implemented !!!!!!!!!!!!
	 *******************************************/
	
	@Test
	public void exampleTest() {
	    startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true);
	    assertMove("e3-e4",true,true);
	    assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/4S4/S1S3S1S/1C5C1/9/RHEAGAEHR",false,false,false);
	}

	//TODO: implement test cases of same kind as example here
	
	@Test
	// cheatCheck: --> checks whether startPosition and targetPosition of moveSting differ:
	public void cheatCheckTest1() {
	    startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true);
	    assertMove("e3-e3",true,false);
	    assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true,false,false);
	}
	@Test
	// cheatCheck: --> checks whether redPlayer tries to move playing-piece of blackPlayer:
	public void cheatCheckTest2() {
		startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true);
		assertMove("a9-a8",true,false);
		assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true,false,false);
	}
	@Test
	// cheatCheck: --> checks whether blackPlayer tries to move playing-piece of redPlayer:
	public void cheatCheckTest3() {
		startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",false);
		assertMove("a0-a1",false,false);
		assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",false,false,false);
	}
	@Test
	// cheatCheck: --> checks whether it's player's turn:
	public void cheatCheckTest4() {
		startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true);
		assertMove("a0-a1",false,false);
		assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true,false,false);
	}
	@Test
	// cheatCheck: --> checks whether start Position of moveString is empty:
	public void cheatCheckTest5() {
		startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true);
		assertMove("a1-c1",true,false);
		assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true,false,false);
	}




	@Test
	// moveStringFormatCheck: --> checks format of moveString:
	public void formatCheckTest1() {
	    startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true);
	    assertMove("33-e3",true,false);
	    assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true,false,false);
	}
	@Test
	// moveStringFormatCheck: --> checks format of moveString:
	public void formatCheckTest2() {
	    startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true);
	    assertMove("ee-e3",true,false);
	    assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true,false,false);
	}
	@Test
	// moveStringFormatCheck: --> checks format of moveString:
	public void formatCheckTest3() {
	    startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true);
	    assertMove("e3-33",true,false);
	    assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true,false,false);
	}
	@Test
	// moveStringFormatCheck: --> checks format of moveString:
	public void formatCheckTest4() {
	    startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true);
	    assertMove("e3-ee",true,false);
	    assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true,false,false);
	}
	@Test
	// moveStringFormatCheck: --> checks format of moveString:
	public void formatCheckTest5() {
		startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true);
		assertMove("e3ke3",true,false);
		assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true,false,false);
	}
	@Test
	// moveStringFormatCheck: --> checks length of moveString:
	public void formatCheckTest6() {
		startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true);
		assertMove("a3-e",true,false);
		assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true,false,false);
	}



	@Test
	// Horse: --> checks valid move to a free position:
	public void HorseTest1() {
		startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true);
		assertMove("b0-c2",true,true);
		assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1CH4C1/9/R1EAGAEHR",false,false,false);
	}
	@Test
	// Horse: --> checks valid move and hits other player's playing-piece:
	public void HorseTest2() {
		startGame("rheagaehr/9/9/9/9/9/9/1Cs4C1/9/RHEAGAEHR",true);
		assertMove("b0-c2",true,true);
		assertGameState("rheagaehr/9/9/9/9/9/9/1CH4C1/9/R1EAGAEHR",false,false,false);
	}
	@Test
	// Horse: --> checks invalid move to a free position:
	public void HorseTest3() {
		startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true);
		assertMove("b0-c1",true,false);
		assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true,false,false);
	}
	@Test
	// Horse: --> checks invalid move, tries to hit own playing-piece:
	public void HorseTest4() {
		startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true);
		assertMove("b0-b1",true,false);
		assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true,false,false);
	}
	@Test
	// Horse: --> checks invalid move, (horse can't jump):
	public void HorseTest5() {
		startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true);
		assertMove("b0-d1",true,false);
		assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true,false,false);
	}




	@Test
	// Elephant: --> checks valid move to a free position:
	public void ElephantTest1() {
		startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true);
		assertMove("c0-e2",true,true);
		assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C2E2C1/9/RH1AGAEHR",false,false,false);
	}
	@Test
	// Elephant: --> checks valid move and hits other player's playing-piece:
	public void ElephantTest2() {
		startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C2r2C1/4R4/RHEAGAEH1",true);
		assertMove("c0-e2",true,true);
		assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C2E2C1/4R4/RH1AGAEH1",false,false,false);
	}
	@Test
	// Elephant: --> checks invalid move to a free position:
	public void ElephantTest3() {
		startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true);
		assertMove("c0-c1",true,false);
		assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR",true,false,false);
	}
	@Test
	// Elephant: --> checks invalid move, tries to hit own playing-piece:
	public void ElephantTest4() {
		startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C2R2C1/9/RHEAGAEH1",true);
		assertMove("c0-e2",true,false);
		assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C2R2C1/9/RHEAGAEH1",true,false,false);
	}
	@Test
	// Elephant: --> checks invalid move, (elephant can't jump):
	public void ElephantTest5() {
		startGame("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/3R5/RHEAGAEH1",true);
		assertMove("c0-e2",true,false);
		assertGameState("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/3R5/RHEAGAEH1",true,false,false);
	}
	@Test
	// Elephant: --> checks invalid move, redElephant tries to cross river:
	public void ElephantTest6() {
		startGame("rheagaehr/9/1c5c1/s1s3s1s/9/6E2/S1S1S1S1S/1C5C1/9/RH1AGAEHR",true);
		assertMove("g4-e6",true,false);
		assertGameState("rheagaehr/9/1c5c1/s1s3s1s/9/6E2/S1S1S1S1S/1C5C1/9/RH1AGAEHR",true,false,false);
	}
	@Test
	// Elephant: --> checks invalid move, blackElephant tries to cross river:
	public void ElephantTest7() {
		startGame("rheaga1hr/9/1c5c1/s1s1s1s1s/2e6/9/S1S3S1S/1C5C1/9/RHEAGAEHR",false);
		assertMove("c5-e3",false,false);
		assertGameState("rheaga1hr/9/1c5c1/s1s1s1s1s/2e6/9/S1S3S1S/1C5C1/9/RHEAGAEHR",false,false,false);
	}


}
