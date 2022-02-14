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
		
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		/*
		 * Horse moves like knight in chess (one step up/down/left/right and then one step diagonal):
		 */
		
		int[][] checkArray = {{row+1,col},{row,col+1},{row-1,col},{row,col-1}};
		int[][] movesArray = {{row+2,col-1},{row+2,col+1},
							  {row+1,col+2},{row-1,col+2},
							  {row-2,col+1},{row-2,col-1},
							  {row-1,col-2},{row+1,col-2}};

		// iterates over checkArray:
		for(int i = 0; i < 4; i++) {
			
			// checks whether position is on board:
			if(new Position(checkArray[i][0], checkArray[i][1]).onBoard()) {
				
				// checks whether position is free (horse can't jump):
				if(board.getBoardMatrix()[checkArray[i][0]][checkArray[i][1]] == '0') {
					
					// checks possible moves for each direction:
					int[][] moves = {movesArray[2*i], movesArray[2*i+1]};
							
					for(int j = 0; j < 2; j++) {
						
						// new position:
						Position newPos = new Position(moves[j][0],moves[j][1]);
						
						// checks whether position is on board:
						if(newPos.onBoard()) {
						
							// checks whether field is free or occupied by other player:
							if(board.getBoardMatrix()[moves[j][0]][moves[j][1]] == '0') {
								possibleMoves.add(new Move(position.moveString(newPos), board.boardState, player));
								// position.moveString(new Position(moves[i][0], moves[i][1]))
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
			}
		}
		
		return possibleMoves;
	}

}
