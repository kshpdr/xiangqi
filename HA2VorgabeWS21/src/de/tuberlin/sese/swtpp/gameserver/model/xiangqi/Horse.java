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
		
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		/*
		 * Horse moves like knight in chess (one step up/down/left/right and then one step diagonal):
		 */
		
		int[][] checkArray = {{1,0},{0,1},{-1,0},{0,-1}};
		int[][] movesArray = {{2,-1},{2,1},{1,2},{-1,2},{-2,1},{-2,-1},{-1,-2},{1,-2}};
		
		// iterates over checkArray:
		for(int i = 0; i < 4; i++) {
			// checks whether position is on board:
			if(checkArray[i][0] >= 0 && checkArray[i][0] <= 10 && checkArray[i][1] >= 0 && checkArray[i][1] <= 9) {
				// checks whether field on board is free (horse can't jump):
				if(board.getBoardMatrix()[row+checkArray[i][0]][col+checkArray[i][1]] == '0') {
					
					// checks possible moves for each direction:
					int[][] moves = {movesArray[2*i], movesArray[2*i+1]};
					for(int j = 0; j < 2; j++) {
						// checks whether position is on board:
						if(moves[i][0] >= 0 && moves[i][0] <= 10 && moves[i][1] >= 0 && moves[i][1] <= 9) {
						
							// checks whether field is free or occupied by other player:
							if(board.getBoardMatrix()[row+moves[i][0]][col+moves[i][1]] == '0') {
								moveString = moveString + stringCol.charAt(col+moves[i][1]) + stringRow.charAt(row+moves[i][0]);
								possibleMoves.add(new Move(moveString, board.boardState, player));
							}
							else if(position.isRed(board) && !(new Position(row+moves[i][0],col+moves[i][1]).isRed(board))) {
								moveString = moveString + stringCol.charAt(col+moves[i][1]) + stringRow.charAt(row+moves[i][0]);
								possibleMoves.add(new Move(moveString, board.boardState, player));	
							}
							else if(!position.isRed(board) && (new Position(row+moves[i][0],col+moves[i][1]).isRed(board))) {
								moveString = moveString + stringCol.charAt(col+moves[i][1]) + stringRow.charAt(row+moves[i][0]);
								possibleMoves.add(new Move(moveString, board.boardState, player));	
							}	
						}
					}	
				}
			}
			
		}
		
		return possibleMoves;
	}

}
