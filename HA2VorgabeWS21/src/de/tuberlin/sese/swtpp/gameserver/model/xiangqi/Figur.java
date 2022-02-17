package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public interface Figur {
	
	public ArrayList<Move> getPossibleMoves(Position position, Board board, Player player);
	/* changed function parameters, deleted position because in each figure we already have position */
	public Position getPosition();

}
