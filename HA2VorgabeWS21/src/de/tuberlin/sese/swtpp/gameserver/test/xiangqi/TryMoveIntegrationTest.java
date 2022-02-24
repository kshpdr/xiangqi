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
	// isCheckTest: --> check the isCheck move for black general and move of red horse
	public void isCheckTest10() {
		startGame("rhea1ae2/4g4/1c5H1/s1s1scs1s/9/9/S1S1S1S1S/1C3r1h1/4G2C1/RHEA1AE1R",false);
	    assertMove("e8-f8",false,false);
	    assertGameState("rhea1ae2/4g4/1c5H1/s1s1scs1s/9/9/S1S1S1S1S/1C3r1h1/4G2C1/RHEA1AE1R",false,false,false);
	}
	
	@Test
	// isCheckTest: --> check the isCheck move for black general and check of soldier
	public void isCheckTest9() {
		startGame("rhea1ae2/4g4/1c3Sc2/s1s1s1s1s/9/9/S1S1S3S/1C3r1h1/4G2C1/RHEA1AEHR",false);
	    assertMove("e8-f8",false,false);
	    assertGameState("rhea1ae2/4g4/1c3Sc2/s1s1s1s1s/9/9/S1S1S3S/1C3r1h1/4G2C1/RHEA1AEHR",false,false,false);
	}
	
	@Test
	// isCheckTest: --> check the isCheck move for red general and check of rook
	public void isCheckTest8() {
		startGame("rheag1e2/4a4/1c4c2/s1s1s1s1s/6S2/9/S1S1S3S/1C3r1h1/4G2C1/RHEA1AEHR",true);
	    assertMove("e1-f1",true,false);
	    assertGameState("rheag1e2/4a4/1c4c2/s1s1s1s1s/6S2/9/S1S1S3S/1C3r1h1/4G2C1/RHEA1AEHR",true,false,false);
	}
	
	@Test
	// isCheckTest: --> check the isCheck move for red general and check of soldier
	public void isCheckTest7() {
		startGame("rheag1e1r/4a4/1c4c2/s1s3s1s/6S2/9/S1S1S3S/1C3s1h1/4G2C1/RHEA1AEHR",true);
	    assertMove("e1-f1",true,false);
	    assertGameState("rheag1e1r/4a4/1c4c2/s1s3s1s/6S2/9/S1S1S3S/1C3s1h1/4G2C1/RHEA1AEHR",true,false,false);
	}
	
	@Test
	// isCheckTest: --> check the isCheck move for red general and check of horse
	public void isCheckTest6() {
		startGame("rheag1e1r/4a4/1c4c2/s1s1s1s1s/6S2/9/S1S1S3S/1C5h1/4G2C1/RHEA1AEHR",true);
	    assertMove("e1-f1",true,false);
	    assertGameState("rheag1e1r/4a4/1c4c2/s1s1s1s1s/6S2/9/S1S1S3S/1C5h1/4G2C1/RHEA1AEHR",true,false,false);
	}
	
	
	@Test
	// isCheckTest: --> check the isCheck move for black horse
	public void isCheckTest5() {
		startGame("rheag1e1r/4a4/1c4c2/s1s1s1s1s/6S2/6h2/S1S1S3S/1C7/5G1C1/RHEA1AEHR",false);
	    assertMove("g4-h2",false,true);
	    assertGameState("rheag1e1r/4a4/1c4c2/s1s1s1s1s/6S2/9/S1S1S3S/1C5h1/5G1C1/RHEA1AEHR",true,false,false);
	}
	
	@Test
	// isCheckTest: --> check the isCheck move for black soldier
	public void isCheckTest4() {
		startGame("rheag1ehr/4a4/1c4c2/s1s1s3s/9/9/S1S1S1S1S/1C3AsC1/5G3/RHEA2EHR",false);
	    assertMove("g2-g1",false,true);
	    assertGameState("rheag1ehr/4a4/1c4c2/s1s1s3s/9/9/S1S1S1S1S/1C3A1C1/5Gs2/RHEA2EHR",true,false,false);
	}
	@Test
	// isCheckTest: --> check the isCheck move for black cannon
	public void isCheckTest3() {
		startGame("rheag1ehr/4a4/1c4c2/s1s1s1s1s/9/9/S1S1S1S1S/1C3A1C1/5G3/RHEA2EHR",false);
	    assertMove("g7-f7",false,true);
	    assertGameState("rheag1ehr/4a4/1c3c3/s1s1s1s1s/9/9/S1S1S1S1S/1C3A1C1/5G3/RHEA2EHR",true,false,false);
	}
	
	
	@Test
	// isCheckTest: --> check the isCheck move for black rook
	public void isCheckTest2() {
		startGame("rheag1eh1/4a4/1c4rc1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/5G3/RHEA1AEHR",false);
	    assertMove("g7-f7",false,true);
	    assertGameState("rheag1eh1/4a4/1c3r1c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/5G3/RHEA1AEHR",true,false,false);
	}
	
	@Test
	// isCheckTest: --> check the isCheck move for red rook
	public void isCheckTest1() {
		startGame("rhea1gehr/4a4/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C4RC1/9/RHEAGAEH1",true);
	    assertMove("g2-f2",true,true);
	    assertGameState("rhea1gehr/4a4/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C3R1C1/9/RHEAGAEH1",false,false,false);
	}
	
	@Test
	// TodesblickTest --> simple check if the move of black advisor with no danger of threatening will proceed
	public void TodesblickTest5() {
		startGame("rheag1ehr/9/1c3a1c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/5G3/RHEA1AEHR",false);
	    assertMove("f7-e8",false,true);
	    assertGameState("rheag1ehr/4a4/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/5G3/RHEA1AEHR",true,false,false);
	}
	
	@Test
	// TodesblickTest --> simple check if the move of black advisor that cause threatening will validate
	public void TodesblickTest4() {
		startGame("rhea1gehr/9/1c3a1c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/5G3/RHEA1AEHR",false);
	    assertMove("f7-e8",true,false);
	    assertGameState("rhea1gehr/9/1c3a1c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/5G3/RHEA1AEHR",false,false,false);
	}
	@Test
	// TodesblickTest --> simple check if the move of black general that cause threatening 
	public void TodesblickTest3() {
		startGame("rhea1aehr/4g4/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/5G3/RHEA1AEHR",false);
	    assertMove("e8-f8",true,false);
	    assertGameState("rhea1aehr/4g4/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/5G3/RHEA1AEHR",false,false,false);
	}
	@Test
	// isThreatenedTest --> simple check of move of Red Advisor which causes threatening of general
	public void TodesblickTest2() {
	    startGame("rhea1aehr/5g3/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C3A1C1/5G3/RHEA2EHR",true);
	    assertMove("f2-e1",true,false);
	    assertGameState("rhea1aehr/5g3/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C3A1C1/5G3/RHEA2EHR",true,false,false);
	}
	
	@Test
	// isThreatenedTest --> simple check of move of Red which causes threatening of general
	public void TodesblickTest1() {
	    startGame("rhea1aehr/5g3/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/4G4/RHEA1AEHR",true);
	    assertMove("e1-f1",true,false);
	    assertGameState("rhea1aehr/5g3/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/4G4/RHEA1AEHR",true,false,false);
	}
	
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
		startGame("rheagaehr/9/9/9/4s4/9/9/1Cs4C1/9/RHEAGAEHR",true);
		assertMove("b0-c2",true,true);
		assertGameState("rheagaehr/9/9/9/4s4/9/9/1CH4C1/9/R1EAGAEHR",false,false,false);
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

	
	@Test
	// cheatCheck: --> checks whether player tries to make a move after game is finished:
	public void cheatCheckTest6() {
		startGame("3ege3/9/3Rs4/9/9/9/9/9/9/4G4",true);
		assertMove("d7-e7",true,true);
		assertMove("d9-b7",false,false);
		assertGameState("3ege3/9/4R4/9/9/9/9/9/9/4G4",false,true,true);
	}
	
	@Test
	// cheatCheck: --> random valid move with blackPlayer (to cover branches in XiangqiGame):
	public void validMoveWithBlackPlayer() {
		startGame("3ege3/9/3Rs4/9/9/9/9/9/9/4G4",false);
		assertMove("d9-b7",false,true);
		assertGameState("4ge3/9/1e1Rs4/9/9/9/9/9/9/4G4",true,false,false);
	}
	
	@Test
	// cheatCheck: --> random valid move with blackPlayer (to cover branches in XiangqiGame):
	public void isWonByPattCheck() {
		startGame("4g4/9/6H2/2R6/4S4/9/9/9/9/4G4",true);
		assertMove("c6-d6",true,true);
		assertGameState("4g4/9/6H2/3R5/4S4/9/9/9/9/4G4",false,true,true);
	}
	
	@Test
	// Cannon: --> checks correct attack at the black figure :
	public void CannonTest1() {
		startGame("r2ag1eh1/1C2a3r/1c2e2c1/s3s4/2s3s1s/4S4/S1S1H1S1S/7C1/1R2A4/1HEA1GE1R",true);
		assertMove("h2-h9",true,true);
		assertGameState("r2ag1eC1/1C2a3r/1c2e2c1/s3s4/2s3s1s/4S4/S1S1H1S1S/9/1R2A4/1HEA1GE1R",false,false,false);
	}
	@Test
	// Cannon: --> checks correct attack at the black figure :
	public void CannonTest2() {
		startGame("r2ag1eh1/1C2a3r/1c2e2c1/s3s4/2s3s1s/4S4/S1S1H1S1S/7C1/1R2A4/1HEA1GE1R",true);
		assertMove("b8-i8",true,true);
		assertGameState("r2ag1eh1/4a3C/1c2e2c1/s3s4/2s3s1s/4S4/S1S1H1S1S/7C1/1R2A4/1HEA1GE1R",false,false,false);
	}
	@Test
	// Cannon: --> checks correct move of black cannon without attacking:
	public void CannonTest3() {
		startGame("r2ag1eh1/4a3C/1c2e2c1/s3s4/2s3s1s/4S4/S1S1H1S1S/7C1/1R2A4/1HEA1GE1R",false);
		assertMove("h7-h3",false,true);
		assertGameState("r2ag1eh1/4a3C/1c2e4/s3s4/2s3s1s/4S4/S1S1H1ScS/7C1/1R2A4/1HEA1GE1R",true,false,false);
	}
	@Test
	// Cannon: --> checks incorrect move of red cannon because otherwise isThreatened:
	public void CannonTest4() {
		startGame("r1ea1ge2/5C3/4S3h/s8/2s3s1s/9/S1S3ScS/2H4C1/1R2A3R/1HEA1GE2",true);
		assertMove("f8-g8",true, false);
		assertGameState("r1ea1ge2/5C3/4S3h/s8/2s3s1s/9/S1S3ScS/2H4C1/1R2A3R/1HEA1GE2",true,false,false);
	}
	@Test
	// Cannon: --> checks correct move of red cannon without ruining todesBlick:
	public void CannonTest5() {
		startGame("r1ea1ge2/5C3/4S3h/s8/2s3s1s/9/S1S3ScS/2H4C1/1R2A3R/1HEA1GE2",true);
		assertMove("f8-f7",true, true);
		assertGameState("r1ea1ge2/9/4SC2h/s8/2s3s1s/9/S1S3ScS/2H4C1/1R2A3R/1HEA1GE2",false,false,false);
	}
	@Test
	// Cannon: --> checks correct attack backwards:
	public void CannonTest6() {
		startGame("r1ea1ge2/6h2/4SC3/s8/2s3s1s/9/S1S3ScS/2H4C1/1R2A4/1HEA1GER1",false);
		assertMove("h3-h0",false, true);
		assertGameState("r1ea1ge2/6h2/4SC3/s8/2s3s1s/9/S1S3S1S/2H4C1/1R2A4/1HEA1GEc1",true,false,false);
	}
	@Test
	// Cannon: --> checks correct attack left:
	public void CannonTest7() {
		startGame("r1ea1ge2/6h2/4SC3/s8/2s3s1s/9/S1S3S1S/2H2E1C1/1R2A4/1HEA1G1c1",false);
		assertMove("h0-d0",false, true);
		assertGameState("r1ea1ge2/6h2/4SC3/s8/2s3s1s/9/S1S3S1S/2H2E1C1/1R2A4/1HEc1G3",true,false,false);
	}
	@Test
	// Cannon: --> checks correct attack right:
	public void CannonTest8() {
		startGame("r1ea1ge2/6h2/4SC3/s8/2s3s1s/9/S1S3S1S/2H2E3/1R2A4/1HEc1G1C1",false);
		assertMove("d0-h0",false, true);
		assertGameState("r1ea1ge2/6h2/4SC3/s8/2s3s1s/9/S1S3S1S/2H2E3/1R2A4/1HE2G1c1",true,false,false);
	}
	@Test
	// Cannon: --> checks general in check from left cannon:
	public void CannonTest9() {
		startGame("2Ca2e2/5g3/4S3h/9/s1s5s/7s1/S1S2S1c1/2H4EC/1R6r/1HEA1G3",false);
		assertMove("f8-f9",false, false);
		assertGameState("2Ca2e2/5g3/4S3h/9/s1s5s/7s1/S1S2S1c1/2H4EC/1R6r/1HEA1G3",false,false,false);
	}
	@Test
	// Cannon: --> checks general in check from forward cannon:
	public void CannonTest10() {
		startGame("3a1ge2/9/5S2h/9/s1s5s/7s1/S1S2S1c1/4H2EC/1R6r/1HEACG3",false);
		assertMove("f9-e9",false, false);
		assertGameState("3a1ge2/9/5S2h/9/s1s5s/7s1/S1S2S1c1/4H2EC/1R6r/1HEACG3",false,false,false);
	}
	
	@Test
	// Rook: --> checks incorrect move cause of friendly figure:
	public void RookTest1() {
		startGame("r1ea1ge2/5C3/4S3h/s8/2s5s/7s1/S1S3Sc1/2H5C/1R2A3R/1HEA1GE2",true);
		assertMove("i1-i5",true, false);
		assertGameState("r1ea1ge2/5C3/4S3h/s8/2s5s/7s1/S1S3Sc1/2H5C/1R2A3R/1HEA1GE2",true,false,false);
	}
	@Test
	// Rook: --> checks incorrect move cause of friendly figure:
	public void RookTest2() {
		startGame("r1ea1ge2/5C3/4S3h/s8/2s5s/7s1/S1S3Sc1/2H5C/1R2A3R/1HEA1GE2",false);
		assertMove("a9-a3",false, false);
		assertGameState("r1ea1ge2/5C3/4S3h/s8/2s5s/7s1/S1S3Sc1/2H5C/1R2A3R/1HEA1GE2",false,false,false);
	}
	@Test
	// Rook: --> checks general in check from rook:
	public void RookTest3() {
		startGame("2ea1ge2/5C3/4S3h/9/s1s5s/7s1/S1S3Sc1/2H4EC/1R6r/1HEA1G3",false);
		assertMove("i1-i0",false, true);
		assertGameState("2ea1ge2/5C3/4S3h/9/s1s5s/7s1/S1S3Sc1/2H4EC/1R7/1HEA1G2r",true,false,false);
	}
	@Test
	// Rook: --> checks general in check from left rook:
	public void RookTest4() {
		startGame("5ge2/1R2a4/5S2h/9/s1s5s/7s1/S1S2S1c1/4H2EC/8r/1HEACG3",true);
		assertMove("b8-b9",true, true);
		assertGameState("1R3ge2/4a4/5S2h/9/s1s5s/7s1/S1S2S1c1/4H2EC/8r/1HEACG3",false,false,false);
	}
	
	@Test
	// Rook: --> checks incorrect move cause of friendly figure:
	public void SoldierTest1() {
		startGame("r1ea1ge2/5C3/4S3h/s8/2s5s/7s1/S1S3Sc1/2H5C/1R2A3R/1HEA1GE2",false);
		assertMove("a6-a5",false, true);
		assertGameState("r1ea1ge2/5C3/4S3h/9/s1s5s/7s1/S1S3Sc1/2H5C/1R2A3R/1HEA1GE2",true,false,false);
	}

}
