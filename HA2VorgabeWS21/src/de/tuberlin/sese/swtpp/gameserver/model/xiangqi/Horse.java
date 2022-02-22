package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.io.Serializable;
import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class Horse implements Figur,Serializable {
	
	Position position;

	public Horse(Position position) {
		this.position = position;
	}
	
	@Override
	public Position getPosition() {
		return this.position;
	}
	
	@Override
	public void setPosition(Position position) {
		this.position = position;
	}


	@Override
	public ArrayList<Move> getPossibleMoves(Board board, Player player) {
		
		// current row/column:
		int row = position.getRow();
		int col = position.getColumn();
		
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		General myGeneral = board.getFriendGeneral(position);
		
		/*
		 * Horse moves like knight in chess (one step up/down/left/right and then one step diagonal):
		 */
		
		int[][] checkArray = {{row+1,col},{row,col+1},{row-1,col},{row,col-1}};
		int[][] movesArray = {{row+2,col-1},{row+2,col+1},{row+1,col+2},{row-1,col+2},{row-2,col+1},{row-2,col-1},{row-1,col-2},{row+1,col-2}};

		// iterates over checkArray:
		for(int i = 0; i < 4; i++) {
			
			// checks whether position is on board:
			if(new Position(checkArray[i][0], checkArray[i][1]).onBoard()) {
				
				// checks whether position is free (horse can't jump):
				if(board.getBoardMatrix()[checkArray[i][0]][checkArray[i][1]] == '0') {
					
					// checks possible moves for each direction:		
					for(int j = 0; j < 2; j++) {
						
						// new position:
						Position newPos = new Position(movesArray[2*i+j][0],movesArray[2*i+j][1]);
						
						// checks whether position is on board:
						if(newPos.onBoard()) {
							
							// checks whether field is free or occupied by other player (and !isThreatende and !isCheck):
							if(board.getBoardMatrix()[movesArray[2*i+j][0]][movesArray[2*i+j][1]] == '0' && !myGeneral.isThreatened(board, new Move(position.moveString(newPos), board.getBoardState(), player)) && !myGeneral.isCheck(board, new Move(position.moveString(newPos), board.getBoardState(), player), player)) {
								possibleMoves.add(new Move(position.moveString(newPos), board.getBoardState(), player));
							}
							else if(position.otherPlayerOnTargetField(board, newPos) && !myGeneral.isThreatened(board, new Move(position.moveString(newPos), board.getBoardState(), player)) && !myGeneral.isCheck(board, new Move(position.moveString(newPos), board.getBoardState(), player), player)) {
								possibleMoves.add(new Move(position.moveString(newPos), board.getBoardState(), player));	
							}	
						}
					}	
				}
			}
		}
		
		return possibleMoves;
	}
}
