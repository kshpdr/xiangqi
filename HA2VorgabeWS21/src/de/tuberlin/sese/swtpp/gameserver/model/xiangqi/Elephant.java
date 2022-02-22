package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.io.Serializable;
import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class Elephant implements Figur,Serializable {
	
	Position position;

	public Elephant(Position position) {
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
		 * Elephant can move two steps diagonal
		 * (can not cross river between row 4 and 5)
		 * (can not jump):
		 */
		
	
		int[][] movesArray = {{row+2,col+2},{row-2,col-2},{row+2,col-2},{row-2,col+2}};
		
		// iterates over possible positions in movesArray:
		for(int i = 0; i < 4; i++) {
			
			// checks whether position is on board:
			if(new Position(movesArray[i][0],movesArray[i][1]).onBoard()) {
				
				// checks whether river is being crossed:
				if((row <= 4 && movesArray[i][0] <= 4) || (row > 4 && movesArray[i][0] > 4)) {
					
					possibleMoves.addAll(checkMove(board, player, movesArray, i));
				}
			}
		}
		
		return possibleMoves;
	}
	
	
	
	// --> checks whether Elephant tries to jump, myGeneral is not threatened or in-check:
	public ArrayList<Move> checkMove(Board board, Player player, int[][] movesArray, int i) {
		
		// current row/column:
		int row = position.getRow();
		int col = position.getColumn();
				
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		int[][] checkArray = {{row+1,col+1},{row-1,col-1},{row+1,col-1},{row-1,col+1}};
		
		// new position:
		Position newPos = new Position(movesArray[i][0],movesArray[i][1]);
		// move to new position:
		Move newMove = new Move(position.moveString(newPos), board.getBoardState(), player);
				
		// Elephant can't jump:
		if(board.getBoardMatrix()[checkArray[i][0]][checkArray[i][1]] == '0') {
	
			// checks whether position is free or occupied by other player: 
			if(board.getBoardMatrix()[movesArray[i][0]][movesArray[i][1]] == '0') {
				possibleMoves.add(newMove);
			}
			else if(position.otherPlayerOnTargetField(board, newPos)) {
				possibleMoves.add(newMove);	
			}
			
			return possibleMoves;
		}
		
		return possibleMoves;
	}
}
