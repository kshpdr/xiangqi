package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.io.Serializable;
import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class Cannon implements Figur,Serializable {
	
	Position position;
	
	public Cannon (Position position) {
		this.position = position;
	}
	
	public Position getPosition() {
		return position;
	}
	
	@Override
	public void setPosition(Position position) {
		this.position = position;
	}

	
	// TODO: add check on todesBlick after all checks of figures
	@Override
	public ArrayList<Move> getPossibleMoves(Board board, Player player) {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		possibleMoves.addAll(rightMoves(position, board, player));
		possibleMoves.addAll(leftMoves(position, board, player));
		possibleMoves.addAll(forwardMoves(position, board, player));
		possibleMoves.addAll(backwardMoves(position, board, player));
		
		return possibleMoves;
	}
	
	
	
	
	public String createMoveFromPositions(Position currentPosition, Position targetPosition) {
		String move = Position.positionToString(currentPosition);
		move += '-';
		move += Position.positionToString(targetPosition);
		return move;
	}
	
	
	public boolean posCheckRight(Position position, Board board, int i) {
		if ((position.isRed(board) && Character.toString(board.getBoardMatrix()[position.getRow()][i]).matches("[gaehrcs]")) || (!position.isRed(board) && Character.toString(board.getBoardMatrix()[position.getRow()][i]).matches("[GAEHRCS]"))) {
			return true;
		}
		return false;
	}
	
	public ArrayList<Move> rightMoves(Position position, Board board, Player player) {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		General friendGeneral = board.getFriendGeneral(position);
		boolean figureBefore = false;
		for (int i = position.getColumn() + 1; i < board.getBoardMatrix()[0].length; i++) {

			String move = createMoveFromPositions(position, new Position(position.getRow(), i));
			// check whether first figure on the way was found
			if (figureBefore) {
				// if so, then check, whether an enemy figure appear afterwards
				if (posCheckRight(position, board, i)){
					if (!friendGeneral.isThreatened(board, new Move(move, board.getBoardState(), player)) && !board.getFriendGeneral(position).isCheck(board, new Move(move, board.getBoardState(), player))) {
						possibleMoves.add(new Move(move, board.getBoardState(), player));
						break;	
					}
				}
				else {
					continue;
				}
			}
			// when first figure on the way found
			else if (board.getBoardMatrix()[position.getRow()][i] != '0') {
				figureBefore = true;
				continue;
			}
			// check whether general is threatened
			if (!friendGeneral.isThreatened(board, new Move(move, board.getBoardState(), player)) && !board.getFriendGeneral(position).isCheck(board, new Move(move, board.getBoardState(), player))) {
			//if (true) {
				possibleMoves.add(new Move(move, board.getBoardState(), player));
			}
		}
		return possibleMoves;
	}
	
	public boolean posCheckLeft(Position position, Board board, int i) {
		if((position.isRed(board) && Character.toString(board.getBoardMatrix()[position.getRow()][i]).matches("[gaehrcs]")) || (!position.isRed(board) && Character.toString(board.getBoardMatrix()[position.getRow()][i]).matches("[GAEHRCS]"))) {
			return true;
		}
		return false;
	}
	
	public ArrayList<Move> leftMoves(Position position, Board board, Player player) {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		General friendGeneral = board.getFriendGeneral(position);
		boolean figureBefore = false;
		// left horizontal
		for (int i = position.getColumn() - 1; i >= 0; i--) {
			String move = createMoveFromPositions(position, new Position(position.getRow(), i));
			
			// check whether first figure on the way was found
			if (figureBefore) {
				// if so, then check, whether an enemy figure appear afterwards
				if (posCheckLeft(position, board, i)){
					if (!friendGeneral.isThreatened(board, new Move(move, board.getBoardState(), player)) && !board.getFriendGeneral(position).isCheck(board, new Move(move, board.getBoardState(), player))) {
						possibleMoves.add(new Move(move, board.getBoardState(), player));
						break;
					}
				}
				else {
					continue;
				}
			}
			// when first figure on the way found
			else if (board.getBoardMatrix()[position.getRow()][i] != '0') {
				figureBefore = true;
				continue;
			}	
			
			// check whether general is threatened
			if (!friendGeneral.isThreatened(board, new Move(move, board.getBoardState(), player)) && !board.getFriendGeneral(position).isCheck(board, new Move(move, board.getBoardState(), player))) {
			//if (true) {
				possibleMoves.add(new Move(move, board.getBoardState(), player));
			}
		}
		return possibleMoves;
	}
	
	public boolean posCheckForward(Position position, Board board, int i) {
		if ((position.isRed(board) && Character.toString(board.getBoardMatrix()[i][position.getColumn()]).matches("[gaehrcs]")) || (!position.isRed(board) && Character.toString(board.getBoardMatrix()[i][position.getColumn()]).matches("[GAEHRCS]"))) {
			return true;
		}
		return false;
	}
	
	public ArrayList<Move> forwardMoves(Position position, Board board, Player player) {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		General friendGeneral = board.getFriendGeneral(position);
		boolean figureBefore = false;
		// forwards vertical
		for (int i = position.getRow() - 1; i >= 0; i--) {
			String move = createMoveFromPositions(position, new Position(i, position.getColumn()));
			
			// check whether first figure on the way was found
			if (figureBefore) {
				// if so, then check, whether an enemy figure appear afterwards
				if (posCheckForward(position, board, i)){
					if (!friendGeneral.isThreatened(board, new Move(move, board.getBoardState(), player)) && !friendGeneral.isCheck(board, new Move(move, board.getBoardState(), player))) {
						possibleMoves.add(new Move(move, board.getBoardState(), player));
						break;
					}
				}
				else {
					continue;
				}
			}
			// when first figure on the way found
			else if (board.getBoardMatrix()[i][position.getColumn()] != '0') {
				figureBefore = true;
				continue;
			}		
			
			// check whether general is threatened
			if (!friendGeneral.isThreatened(board, new Move(move, board.getBoardState(), player)) && !friendGeneral.isCheck(board, new Move(move, board.getBoardState(), player))) {
			//if (true) {
				possibleMoves.add(new Move(move, board.getBoardState(), player));
			}
		}
		return possibleMoves;
	}
	
	public boolean posCheckBackward(Position position, Board board, int i) {
		if((position.isRed(board) && Character.toString(board.getBoardMatrix()[i][position.getColumn()]).matches("[gaehrcs]")) || (!position.isRed(board) && Character.toString(board.getBoardMatrix()[i][position.getColumn()]).matches("[GAEHRCS]"))) {
			return true;
		}
		return false;
	}
	
	public ArrayList<Move> backwardMoves(Position position, Board board, Player player) {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		General friendGeneral = board.getFriendGeneral(position);
		boolean figureBefore = false;
		for (int i = position.getRow() + 1; i < board.getBoardMatrix().length; i++) {
			String move = createMoveFromPositions(position, new Position(i, position.getColumn()));
			
			// check whether first figure on the way was found
			if (figureBefore) {
				// if so, then check, whether an enemy figure appear afterwards
				if (posCheckBackward(position, board, i)){
					if (!friendGeneral.isThreatened(board, new Move(move, board.getBoardState(), player)) && !friendGeneral.isCheck(board, new Move(move, board.getBoardState(), player))) {
						possibleMoves.add(new Move(move, board.getBoardState(), player));
						break;	
					}
				}
				else {
					continue;
				}
			}
			// when first figure on the way found
			else if (board.getBoardMatrix()[i][position.getColumn()] != '0') {
				figureBefore = true;
				continue;
			}				
			
			// check whether general is threatened
			if (!friendGeneral.isThreatened(board, new Move(move, board.getBoardState(), player)) && !friendGeneral.isCheck(board, new Move(move, board.getBoardState(), player))) {
			//if (true) {
				possibleMoves.add(new Move(move, board.getBoardState(), player));
			}
		}
		return possibleMoves;
	}
		
}