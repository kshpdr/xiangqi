package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public interface Figur {
	
	public ArrayList<Move> getPossibleMoves(Board board, Player player);
	
	public Position getPosition();
	
	public void setPosition(Position position);
}
