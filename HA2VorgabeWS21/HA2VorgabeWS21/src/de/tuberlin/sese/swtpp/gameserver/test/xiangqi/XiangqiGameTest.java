package de.tuberlin.sese.swtpp.gameserver.test.xiangqi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.tuberlin.sese.swtpp.gameserver.control.GameController;
import de.tuberlin.sese.swtpp.gameserver.model.Player;
import de.tuberlin.sese.swtpp.gameserver.model.User;
import de.tuberlin.sese.swtpp.gameserver.model.xiangqi.*;

public final class XiangqiGameTest {
	
	final User user1 = new User("Alice", "alice");
	final User user2 = new User("Bob", "bob");
	final User user3 = new User("Eve", "eve");
	
	Player redPlayer = null;
	Player blackPlayer = null;
	XiangqiGame game = null;
	GameController controller;
	
	@Before
	public void setUp() throws Exception {
		controller = GameController.getInstance();
		controller.clear();
		
		int gameID = controller.startGame(user1, "", "xiangqi");
		
		game = (XiangqiGame) controller.getGame(gameID);
		redPlayer = game.getPlayer(user1);
	}
	
	public void startGame() {
		controller.joinGame(user2, "xiangqi");
		blackPlayer = game.getPlayer(user2);
	}

	
	@Test
	public void testWaitingGame() {
		assertEquals("Wait", game.getStatus());
		assertEquals("", game.gameInfo());
	}
	
	@Test
	public void testGameStarted() {
		assertEquals(game.getGameID(), controller.joinGame(user2, "xiangqi"));
		assertFalse(game.addPlayer(new Player(user3, game))); // no third player
		assertEquals("Started", game.getStatus());
		assertEquals("", game.gameInfo());
		assertTrue(game.isRedNext());
		assertFalse(game.didRedDraw());
		assertFalse(game.didBlackDraw());
		assertFalse(game.redGaveUp());
		assertFalse(game.blackGaveUp());
	}

	@Test
	public void testSetNextPlayer() {
		startGame();
		
		game.setNextPlayer(blackPlayer);
		
		assertFalse(game.isRedNext());
	}

	
	@Test
	public void testCallDrawBoth() {	
		// call draw before start
		assertFalse(game.callDraw(redPlayer));

		startGame();
		
		controller.callDraw(user1, game.getGameID());
		assertTrue(game.didRedDraw());
		assertFalse(game.didBlackDraw());
		assertEquals("red called draw", game.gameInfo());
		
		controller.callDraw(user2, game.getGameID());
		assertTrue(game.didBlackDraw());

		assertEquals("Draw", game.getStatus());
		assertEquals("draw game", game.gameInfo());
		
		// call draw after finish
		assertFalse(game.callDraw(redPlayer));
	}
	
	@Test
	public void testCallDrawBlack() {
		startGame();
		
		controller.callDraw(user2, game.getGameID());
		assertFalse(game.didRedDraw());
		assertTrue(game.didBlackDraw());
		assertEquals("black called draw", game.gameInfo());
	}

	@Test
	public void testGiveUpRed() {
		// try before start 
		assertFalse(game.giveUp(redPlayer));
		assertFalse(game.giveUp(blackPlayer));
		
		startGame();
		
		controller.giveUp(user1, game.getGameID());
		
		assertEquals("Surrendered", game.getStatus());
		assertEquals("red gave up", game.gameInfo());
		
		// try after finish
		assertFalse(game.giveUp(redPlayer));
		assertFalse(game.giveUp(blackPlayer));

	}
	
	@Test
	public void testGiveUpBlack() {
		startGame();
		
		controller.giveUp(user2, game.getGameID());
		
		assertEquals("Surrendered", game.getStatus());
		assertEquals("black gave up", game.gameInfo());
	}

	@Test
	public void testGetMinPlayers() {
		assertEquals(2, game.getMinPlayers());
	}
	
	@Test
	public void testGetMaxPlayers() {
		assertEquals(2, game.getMaxPlayers());
	}
	
	@Test
	public void testNextPlayerString() {
		startGame();
		
		assertEquals("r", game.nextPlayerString());
		
		game.setNextPlayer(blackPlayer);
		
		assertEquals("b", game.nextPlayerString());
	}

	@Test
	public void testFinish() {
		startGame();
		
		assertTrue(game.regularGameEnd(redPlayer));
		assertEquals("Finished", game.getStatus());
		assertEquals("red won", game.gameInfo());
		
		// test after finish
		assertFalse(game.regularGameEnd(redPlayer));
	}
	
	@Test
	public void testFinishBlack() {
		startGame();
		
		assertTrue(game.regularGameEnd(blackPlayer));
		assertEquals("Finished", game.getStatus());
		assertEquals("black won", game.gameInfo());
	}

	@Test
	public void testError() {
		assertFalse(game.isError());
		game.setError(true);
		assertTrue(game.isError());
		assertEquals("Error", game.getStatus());
		game.setError(false);
		assertFalse(game.isError());
	}
}
