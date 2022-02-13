package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class Horse implements Figur {

	@Override
	public ArrayList<Move> getPossibleMoves(Position position, Board board, Player player) {
		
		// current row/column:
		int row = position.getRow();
		int col = position.getColumn();
		
		// creates moveString:
		String stringRow = "9876543210";
		String stringCol = "abcdefghi";
		char x = stringRow.charAt(row);
		char y = stringCol.charAt(col);
		String moveString = y + x + "-";
		
		/*
		 * Horse moves like knight (chess)
		 * (but can not jump):
		 */
		
		
		return null;
	}

}
