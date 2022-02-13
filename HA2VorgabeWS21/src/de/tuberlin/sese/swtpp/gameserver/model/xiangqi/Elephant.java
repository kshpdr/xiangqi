package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class Elephant implements Figur {

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
		
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		/*
		 * Elephant can move two steps diagonal
		 * (can not cross river between row 4 and 5):
		 */
		
		int[][] posArray = {{row+2,col+2},{row-2,col-2},{row+2,col-2},{row-2,col+2}};
		
		for(int i = 0; i < 4; i++) {
			// checks if position is on board:
			if(posArray[i][0] >= 0 && posArray[i][0] <= 10 && posArray[i][1] >= 0 && posArray[i][1] <= 9) {
				// checks if river is being crossed:
				if((row <= 4 && posArray[i][0] <= 4) || (row > 4 && posArray[i][0] > 4)) {
					
					// checks if field is free or occupied by other player:
					if(board.boardMatrix[posArray[i][0]][posArray[i][1]] == '0') {
						moveString = moveString + stringCol.charAt(posArray[i][1]) + stringRow.charAt(posArray[i][0]);
						possibleMoves.add(new Move(moveString, board.boardState, player));
					}
					else if(position.isRed(board) && !(new Position(posArray[i][0],posArray[i][1]).isRed(board))) {
						moveString = moveString + stringCol.charAt(posArray[i][1]) + stringRow.charAt(posArray[i][0]);
						possibleMoves.add(new Move(moveString, board.boardState, player));	
					}
					else if(!position.isRed(board) && (new Position(posArray[i][0],posArray[i][1]).isRed(board))) {
						moveString = moveString + stringCol.charAt(posArray[i][1]) + stringRow.charAt(posArray[i][0]);
						possibleMoves.add(new Move(moveString, board.boardState, player));	
					}	
				}		
			}
		}
		
		return possibleMoves;
	}

}
