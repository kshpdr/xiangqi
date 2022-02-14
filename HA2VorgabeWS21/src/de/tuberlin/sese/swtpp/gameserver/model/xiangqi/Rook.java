package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class Rook implements Figur {

	@Override
	public ArrayList<Move> getPossibleMoves(Position position, Board board, Player player) {
		
		// current row/column:
		int row = position.getRow();
		int col = position.getColumn();
		
		ArrayList<Move> possibleMoves = new ArrayList<Move>(); 
		
		/*
		 * rook can do multiple steps up/down or left/right 
		 * (but can not jump over other figures):
		 */
		
		// right:
		for(int i = col + 1; i < board.getBoardMatrix()[0].length; i++) {
			if(board.getBoardMatrix()[row][i] == '0') {
				possibleMoves.add(new Move(position.moveString(new Position(row,i)), board.boardState, player));
			}
			else if(position.isRed(board) && !(new Position(row,i).isRed(board))) {
				possibleMoves.add(new Move(position.moveString(new Position(row,i)), board.boardState, player));
				break;
			}
			else if(!position.isRed(board) && (new Position(row,i).isRed(board))) {
				possibleMoves.add(new Move(position.moveString(new Position(row,i)), board.boardState, player));
				break;
			}
			else {
				break;
			}
		}
		
		// left:
		for(int i = col - 1; i >= 0; i--) {
			if(board.getBoardMatrix()[row][i] == '0') {
				possibleMoves.add(new Move(position.moveString(new Position(row,i)), board.boardState, player));
			}
			else if(position.isRed(board) && !(new Position(row,i).isRed(board))) {
				possibleMoves.add(new Move(position.moveString(new Position(row,i)), board.boardState, player));
				break;
			}
			else if(!position.isRed(board) && (new Position(row,i).isRed(board))) {
				possibleMoves.add(new Move(position.moveString(new Position(row,i)), board.boardState, player));
				break;
			}
			else {
				break;
			}
		}
		
		// up:
		for(int i = row - 1; i >= 0; i--) {
			if(board.getBoardMatrix()[i][col] == '0') {
				possibleMoves.add(new Move(position.moveString(new Position(i,col)), board.boardState, player));
			}
			else if(position.isRed(board) && !(new Position(i,col).isRed(board))) {
				possibleMoves.add(new Move(position.moveString(new Position(i,col)), board.boardState, player));
				break;
			}
			else if(!position.isRed(board) && (new Position(i,col).isRed(board))) {
				possibleMoves.add(new Move(position.moveString(new Position(i,col)), board.boardState, player));
				break;
			}
			else {
				break;
			}
		}
		
		// down:
		for(int i = row + 1; i < board.getBoardMatrix().length; i++) {
			if(board.getBoardMatrix()[i][col] == '0') {
				possibleMoves.add(new Move(position.moveString(new Position(i,col)), board.boardState, player));
			}
			else if(position.isRed(board) && !(new Position(i,col).isRed(board))) {
				possibleMoves.add(new Move(position.moveString(new Position(i,col)), board.boardState, player));
				break;
			}
			else if(!position.isRed(board) && (new Position(i,col).isRed(board))) {
				possibleMoves.add(new Move(position.moveString(new Position(i,col)), board.boardState, player));
				break;
			}
			else {
				break;
			}
		}
	
		return possibleMoves;
	}

}
