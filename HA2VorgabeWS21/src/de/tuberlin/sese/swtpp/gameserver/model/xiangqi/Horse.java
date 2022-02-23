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
		
		/*
		 * Horse moves like knight in chess (one step up/down/left/right and then one step diagonal):
		 */
		
		// horse can't jump:
		int[][] checkArray = {{row+1,col},{row,col+1},{row-1,col},{row,col-1}};

		// iterates over checkArray:
		for(int i = 0; i < 4; i++) {
			
			// checks whether position is on board:
			if(new Position(checkArray[i][0], checkArray[i][1]).onBoard()) {
				
				// checks whether horse tries to jump:
				if(board.getBoardMatrix()[checkArray[i][0]][checkArray[i][1]] == '0') {
					
					possibleMoves.addAll(moveCheckHorse(board, player, i));
				}
			}
		}
		
		return possibleMoves;
	}
	
	// extends getPossibleMoves:
	public ArrayList<Move> moveCheckHorse(Board board, Player player, int i) {
		
		// current row/column:
		int row = position.getRow();
		int col = position.getColumn();
		
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
	
		// possible moves:
		int[][] movesArray = {{row+2,col-1},{row+2,col+1},{row+1,col+2},{row-1,col+2},{row-2,col+1},{row-2,col-1},{row-1,col-2},{row+1,col-2}};
		
		// checks possible moves for each direction:		
		for(int j = 0; j < 2; j++) {
			
			// new position:
			Position newPos = new Position(movesArray[2*i+j][0],movesArray[2*i+j][1]);
			// new move:
			Move newMove = new Move(position.moveString(newPos), board.getBoardState(), player);
			
			// checks whether position is on board:
			if(newPos.onBoard()) {
				
				// checks whether field is free or occupied by other player:
				if(board.getBoardMatrix()[movesArray[2*i+j][0]][movesArray[2*i+j][1]] == '0') {
					possibleMoves.add(newMove);
				}
				else if(position.otherPlayerOnTargetField(board, newPos)) {
					possibleMoves.add(newMove);	
				}	
			}
		}
		
		return possibleMoves;
		
	}
	
}
