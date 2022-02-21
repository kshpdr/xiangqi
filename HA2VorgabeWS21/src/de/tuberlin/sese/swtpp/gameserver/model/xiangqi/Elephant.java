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
		
		int[][] checkArray = {{row+1,col+1},{row-1,col-1},{row+1,col-1},{row-1,col+1}};
		int[][] movesArray = {{row+2,col+2},{row-2,col-2},{row+2,col-2},{row-2,col+2}};
		
		// iterates over possible positions in movesArray:
		for(int i = 0; i < 4; i++) {
			
			// new position:
			Position newPos = new Position(movesArray[i][0],movesArray[i][1]);
			// move to new position:
			Move newMove = new Move(position.moveString(newPos), board.getBoardState(), player);
			
			// checks whether position is on board:
			if(newPos.onBoard()) {
				
				// checks whether river is being crossed:
				if((row <= 4 && movesArray[i][0] <= 4) || (row > 4 && movesArray[i][0] > 4)) {
					// checks whether Elephant tries to jump, myGeneral is not threatened or in-check, position is free or occupied by other player: 
					possibleMoves.addAll(checkMove(board, newMove, newPos, player, i, checkArray, movesArray));
				}
			}
		}
		
		return possibleMoves;
	}
	
	
	
	// --> checks whether Elephant tries to jump, myGeneral is not threatened or in-check:
	public ArrayList<Move> checkMove(Board board, Move newMove, Position newPos, Player player, int i, int[][] checkArray, int[][] movesArray) {
		
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		General myGeneral = board.getFriendGeneral(position);
		
		
		// Elephant can't jump:
		if(board.getBoardMatrix()[checkArray[i][0]][checkArray[i][1]] == '0') {
	
			// checks whether myGeneral is not threatened or in-check:
			if(!myGeneral.isThreatened(board, newMove) && !myGeneral.isCheck(board, newMove, player)) {
				
				possibleMoves.addAll(checkOccupiedOrFree(board, movesArray, newPos, newMove, i));
			}
		}
		
		return possibleMoves;
	}
	
	
	
	// --> checks whether position is free or occupied by other player: 
	public ArrayList<Move> checkOccupiedOrFree(Board board, int[][] movesArray, Position newPos, Move newMove, int i) {
		
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		if(board.getBoardMatrix()[movesArray[i][0]][movesArray[i][1]] == '0') {
			possibleMoves.add(newMove);
		}
		else if(position.otherPlayerOnTargetField(board, newPos)) {
			possibleMoves.add(newMove);	
		}
		
		return possibleMoves;
	}

}
