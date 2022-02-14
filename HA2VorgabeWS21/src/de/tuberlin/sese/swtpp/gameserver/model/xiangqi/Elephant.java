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
		
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		/*
		 * Elephant can move two steps diagonal
		 * (can not cross river between row 4 and 5):
		 */
		
		int[][] posArray = {{row+2,col+2},{row-2,col-2},{row+2,col-2},{row-2,col+2}};
		
		// iterates over possible positions in posArray:
		for(int i = 0; i < 4; i++) {
			
			// checks whether position is on board:
			if(new Position(posArray[i][0],posArray[i][1]).onBoard()) {
				
				// new position:
				Position newPos = new Position(posArray[i][0],posArray[i][1]);
				
				// checks whether river is being crossed:
				if((row <= 4 && posArray[i][0] <= 4) || (row > 4 && posArray[i][0] > 4)) {
					
					// checks whether field is free or occupied by other player:
					if(board.getBoardMatrix()[posArray[i][0]][posArray[i][1]] == '0') {
						possibleMoves.add(new Move(position.moveString(newPos), board.boardState, player));
					}
					else if(position.isRed(board) && !newPos.isRed(board)) {
						possibleMoves.add(new Move(position.moveString(newPos), board.boardState, player));	
					}
					else if(!position.isRed(board) && newPos.isRed(board)) {
						possibleMoves.add(new Move(position.moveString(newPos), board.boardState, player));	
					}	
				}		
			}
		}
		
		return possibleMoves;
	}

}
