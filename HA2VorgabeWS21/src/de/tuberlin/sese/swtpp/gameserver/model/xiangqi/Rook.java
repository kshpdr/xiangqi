package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class Rook implements Figur {
	
	Position position;

	public Rook(Position position) {
		this.position = position;
	}

	
	// right:
	public ArrayList<Move> moveR(int row, int col, Board board, Player player, General myGeneral) {
		
		ArrayList<Move> possibleMoves = new ArrayList<Move>(); 
		
		for(int i = col + 1; i < board.getBoardMatrix()[0].length; i++) {
			// new Position:
			Position newPos = new Position(row,i);
			
			if(board.getBoardMatrix()[row][i] == '0' && !myGeneral.isThreatened(position.moveString(newPos))) {
				possibleMoves.add(new Move(position.moveString(newPos), board.boardState, player));
			}
			else if(position.otherPlayerOnTargetField(board, newPos) && !myGeneral.isThreatened(position.moveString(newPos))) {
				possibleMoves.add(new Move(position.moveString(newPos), board.boardState, player));
				break;
			}
			else {
				break;
			}
		}
		
		return possibleMoves;
	}
		
	// left:
	public ArrayList<Move> moveL(int row, int col, Board board, Player player, General myGeneral) {
		
		ArrayList<Move> possibleMoves = new ArrayList<Move>(); 
		
		for(int i = col - 1; i >= 0; i--) {
			// new Position:
			Position newPos = new Position(row,i);
			
			if(board.getBoardMatrix()[row][i] == '0' && !myGeneral.isThreatened(position.moveString(newPos))) {	
				possibleMoves.add(new Move(position.moveString(new Position(row,i)), board.boardState, player));
			}
			else if(position.otherPlayerOnTargetField(board, newPos) && !myGeneral.isThreatened(position.moveString(newPos))) {
				possibleMoves.add(new Move(position.moveString(new Position(row,i)), board.boardState, player));
				break;
			}
			else {
				break;
			}
		}
		
		return possibleMoves;
		
	}
	
	// up:
	public ArrayList<Move> moveU(int row, int col, Board board, Player player, General myGeneral) {
		
		ArrayList<Move> possibleMoves = new ArrayList<Move>(); 
		
		for(int i = row - 1; i >= 0; i--) {
			// new Position:
			Position newPos = new Position(i,col);
						
			if(board.getBoardMatrix()[i][col] == '0' && !myGeneral.isThreatened(position.moveString(newPos))) {
				possibleMoves.add(new Move(position.moveString(newPos), board.boardState, player));
			}
			else if(position.otherPlayerOnTargetField(board, newPos) && !myGeneral.isThreatened(position.moveString(newPos))) {
				possibleMoves.add(new Move(position.moveString(newPos), board.boardState, player));
				break;
			}
			else {
				break;
			}		
		}	
		
		return possibleMoves;
	}
	
	// down:
	public ArrayList<Move> moveD(int row, int col, Board board, Player player, General myGeneral) {
		
		ArrayList<Move> possibleMoves = new ArrayList<Move>(); 

		for(int i = row + 1; i < board.getBoardMatrix().length; i++) {
			// new Position:
			Position newPos = new Position(i,col);
						
			if(board.getBoardMatrix()[i][col] == '0' && !myGeneral.isThreatened(position.moveString(newPos))) {		
				possibleMoves.add(new Move(position.moveString(newPos), board.boardState, player));
			}
			else if(position.otherPlayerOnTargetField(board, newPos) && !myGeneral.isThreatened(position.moveString(newPos))) {
				possibleMoves.add(new Move(position.moveString(newPos), board.boardState, player));
				break;
			}
			else {
				break;
			}	
		}	
		
		return possibleMoves;
	}
		
	
	
	@Override  
	public ArrayList<Move> getPossibleMoves(Position position, Board board, Player player) {
		
		// current row/column:
		int row = position.getRow();
		int col = position.getColumn();
		
		ArrayList<Move> possibleMoves = new ArrayList<Move>(); 
		
		General myGeneral = board.getFriendGeneral(position);
		
		/*
		 * rook can do multiple steps up/down or left/right 
		 * (but can not jump over other figures):
		 */
		
		possibleMoves.addAll(moveU(row,col,board,player,myGeneral));
		possibleMoves.addAll(moveD(row,col,board,player,myGeneral));
		possibleMoves.addAll(moveL(row,col,board,player,myGeneral));
		possibleMoves.addAll(moveR(row,col,board,player,myGeneral));

		return possibleMoves;
	}
}
