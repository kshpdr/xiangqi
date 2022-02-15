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
		
		General myGeneral = board.getFriendGeneral(position);
		
		/*
		 * rook can do multiple steps up/down or left/right 
		 * (but can not jump over other figures):
		 */
		
		// right:
		for(int i = col + 1; i < board.getBoardMatrix()[0].length; i++) {
			// new Position:
			Position newPos = new Position(row,i);
			
			if(board.getBoardMatrix()[row][i] == '0' && !myGeneral.isThreatened(position.moveString(newPos))) {
				possibleMoves.add(new Move(position.moveString(newPos), board.boardState, player));
			}
			else if(position.isRed(board) && !newPos.isRed(board) && !myGeneral.isThreatened(position.moveString(newPos))) {
				possibleMoves.add(new Move(position.moveString(newPos), board.boardState, player));
				break;
			}
			else if(!position.isRed(board) && newPos.isRed(board) && !myGeneral.isThreatened(position.moveString(newPos))) {
				possibleMoves.add(new Move(position.moveString(newPos), board.boardState, player));
				break;
			}
			else {
				break;
			}
		}
		
		// left:
		for(int i = col - 1; i >= 0; i--) {
			// new Position:
			Position newPos = new Position(row,i);
			
			if(board.getBoardMatrix()[row][i] == '0' && !myGeneral.isThreatened(position.moveString(newPos))) {	
				possibleMoves.add(new Move(position.moveString(new Position(row,i)), board.boardState, player));
				}
			else if(position.isRed(board) && !newPos.isRed(board) && !myGeneral.isThreatened(position.moveString(newPos))) {
				possibleMoves.add(new Move(position.moveString(new Position(row,i)), board.boardState, player));
				break;
				}
			else if(!position.isRed(board) && newPos.isRed(board) && !myGeneral.isThreatened(position.moveString(newPos))) {
				possibleMoves.add(new Move(position.moveString(new Position(row,i)), board.boardState, player));
				break;
				}
			else {
				break;
				}
	
		}
		
		// up:
		for(int i = row - 1; i >= 0; i--) {
			// new Position:
			Position newPos = new Position(i,col);
						
			if(board.getBoardMatrix()[i][col] == '0' && !myGeneral.isThreatened(position.moveString(newPos))) {
				possibleMoves.add(new Move(position.moveString(newPos), board.boardState, player));
			}
			else if(position.isRed(board) && !newPos.isRed(board) && !myGeneral.isThreatened(position.moveString(newPos))) {
				possibleMoves.add(new Move(position.moveString(newPos), board.boardState, player));
				break;
			}
			else if(!position.isRed(board) && newPos.isRed(board) && !myGeneral.isThreatened(position.moveString(newPos))) {
				possibleMoves.add(new Move(position.moveString(newPos), board.boardState, player));
				break;
			}
			else {
				break;
			}
			
		}
		
		// down:
		for(int i = row + 1; i < board.getBoardMatrix().length; i++) {
			// new Position:
			Position newPos = new Position(i,col);
						
			if(board.getBoardMatrix()[i][col] == '0' && !myGeneral.isThreatened(position.moveString(newPos))) {		
				possibleMoves.add(new Move(position.moveString(newPos), board.boardState, player));
			}
			else if(position.isRed(board) && !newPos.isRed(board) && !myGeneral.isThreatened(position.moveString(newPos))) {
				possibleMoves.add(new Move(position.moveString(newPos), board.boardState, player));
				break;
			}
			else if(!position.isRed(board) && newPos.isRed(board) && !myGeneral.isThreatened(position.moveString(newPos))) {
				possibleMoves.add(new Move(position.moveString(newPos), board.boardState, player));
				break;
			}
			else {
				break;
			}	
		}
	
		return possibleMoves;
	}

}
