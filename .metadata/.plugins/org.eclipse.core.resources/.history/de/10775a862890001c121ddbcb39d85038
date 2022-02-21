package de.tuberlin.sese.swtpp.gameserver.test.xiangqi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;
import de.tuberlin.sese.swtpp.gameserver.model.User;
import de.tuberlin.sese.swtpp.gameserver.model.xiangqi.*;

public class FiguresTests {
	Player redPlayer = new Player(new User("Denis", "5"), new XiangqiGame());
	Player blackPlayer = new Player(new User("Daniil", "6"), new XiangqiGame());

	Board board1 = null;
	Board board2 = null;

	@Before
	public void setUp() throws Exception {
		board1 = new Board("rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR"); // start position
		board2 = new Board("r2a1aeCr/9/e1h2g1c1/s1s5s/2S4s1/S8/7sS/1C4H2/4A4/1RE1GAE1R");
	}

	@Test
	public void rookMoves() {
		Rook blackRook = new Rook(new Position(0, 8));
		ArrayList<Move> blackRookMovesMethod = blackRook.getPossibleMoves(new Position(0, 0), board1, blackPlayer);

		ArrayList<Move> blackRookMovesReal = new ArrayList<>();
		blackRookMovesReal.add(new Move("a9-a8", board1.getBoardState(), blackPlayer));
		blackRookMovesReal.add(new Move("a9-a7", board1.getBoardState(), blackPlayer));

		assertEquals(blackRookMovesMethod, blackRookMovesReal);
	}
}
