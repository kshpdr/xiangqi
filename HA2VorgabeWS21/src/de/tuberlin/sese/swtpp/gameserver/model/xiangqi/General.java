package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class General implements Figur {

	Position position;
	
	@Override
	public ArrayList<Move> getPossibleMoves(Position position, Board board, Player player) {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean isThreatened(String move) {
		return true;
	}
	
	public Position getPosition() {
		return position;
	}


}
