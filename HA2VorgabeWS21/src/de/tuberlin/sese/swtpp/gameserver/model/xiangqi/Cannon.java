package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class Cannon implements Figur {
	
	Position position;
	
	public Cannon (Position position) {
		this.position = position;
	}

	
	// TODO: add check on todesBlick after all checks of figures
	// TODO: ob zwei oder mehr figuren dazwischen stehen dürfen
	@Override
	public ArrayList<Move> getPossibleMoves(Position position, Board board, Player player) {
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
	
	public ArrayList<Move> rightMoves(Position position, Board board, Player player) {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		General friendGeneral = board.getFriendGeneral(position);
		boolean figureBefore = false;
		for (int i = position.getColumn() + 1; i < board.getCurrentBoard()[0].length; i++) {
			Position target = new Position(position.getRow(), i);
			String move = createMoveFromPositions(position, target);
			
			// check whether first figure on the way was found
			if (figureBefore) {
				// if so, then check, whether an enemy figure appear afterwards
				if ((position.isRed(board) && Character.toString(board.getCurrentBoard()[position.getRow()][i]).matches("[gaehrcs]")) || (!position.isRed(board) && Character.toString(board.getCurrentBoard()[position.getRow()][i]).matches("[GAEHRCS]"))){
					possibleMoves.add(new Move(move, board.getBoardState(), player));
					break;
				}
				else {
					continue;
				}
			}
			// when first figure on the way found
			else if (board.getCurrentBoard()[position.getRow()][i] != '0') {
				figureBefore = true;
				continue;
			}
			// check whether general is threatened
			//if (!friendGeneral.isThreatened(move)) {
			if (true) {
				possibleMoves.add(new Move(move, board.getBoardState(), player));
			}
		}
		return possibleMoves;
	}
	
	public ArrayList<Move> leftMoves(Position position, Board board, Player player) {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		General friendGeneral = board.getFriendGeneral(position);
		boolean figureBefore = false;
		// left horizontal
		for (int i = position.getColumn() - 1; i >= 0; i--) {
			Position target = new Position(position.getRow(), i);
			String move = createMoveFromPositions(position, target);
			
			// check whether first figure on the way was found
			if (figureBefore) {
				// if so, then check, whether an enemy figure appear afterwards
				if ((position.isRed(board) && Character.toString(board.getCurrentBoard()[position.getRow()][i]).matches("[gaehrcs]")) || (!position.isRed(board) && Character.toString(board.getCurrentBoard()[position.getRow()][i]).matches("[GAEHRCS]"))){
					possibleMoves.add(new Move(move, board.getBoardState(), player));
					break;
				}
				else {
					continue;
				}
			}
			// when first figure on the way found
			else if (board.getCurrentBoard()[position.getRow()][i] != '0') {
				figureBefore = true;
				continue;
			}	
			
			// check whether general is threatened
			//if (!friendGeneral.isThreatened(move)) {
			if (true) {
				possibleMoves.add(new Move(move, board.getBoardState(), player));
			}
		}
		return possibleMoves;
	}
	
	public ArrayList<Move> forwardMoves(Position position, Board board, Player player) {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		General friendGeneral = board.getFriendGeneral(position);
		boolean figureBefore = false;
		// forwards vertical
		for (int i = position.getRow() - 1; i >= 0; i--) {
			Position target = new Position(i, position.getColumn());
			String move = createMoveFromPositions(position, target);
			
			// check whether first figure on the way was found
			if (figureBefore) {
				// if so, then check, whether an enemy figure appear afterwards
				if ((position.isRed(board) && Character.toString(board.getCurrentBoard()[i][position.getColumn()]).matches("[gaehrcs]")) || (!position.isRed(board) && Character.toString(board.getCurrentBoard()[i][position.getColumn()]).matches("[GAEHRCS]"))){
					possibleMoves.add(new Move(move, board.getBoardState(), player));
					break;
				}
				else {
					continue;
				}
			}
			// when first figure on the way found
			else if (board.getCurrentBoard()[i][position.getColumn()] != '0') {
				figureBefore = true;
				continue;
			}		
			
			// check whether general is threatened
			//if (!friendGeneral.isThreatened(move)) {
			if (true) {
				possibleMoves.add(new Move(move, board.getBoardState(), player));
			}
		}
		return possibleMoves;
	}
	
	public ArrayList<Move> backwardMoves(Position position, Board board, Player player) {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		General friendGeneral = board.getFriendGeneral(position);
		boolean figureBefore = false;
		for (int i = position.getRow() + 1; i < board.getCurrentBoard().length; i++) {
			Position target = new Position(i, position.getColumn());
			String move = createMoveFromPositions(position, target);
			
			// check whether first figure on the way was found
			if (figureBefore) {
				// if so, then check, whether an enemy figure appear afterwards
				if ((position.isRed(board) && Character.toString(board.getCurrentBoard()[i][position.getColumn()]).matches("[gaehrcs]")) || (!position.isRed(board) && Character.toString(board.getCurrentBoard()[i][position.getColumn()]).matches("[GAEHRCS]"))){
					possibleMoves.add(new Move(move, board.getBoardState(), player));
					break;
				}
				else {
					continue;
				}
			}
			// when first figure on the way found
			else if (board.getCurrentBoard()[i][position.getColumn()] != '0') {
				figureBefore = true;
				continue;
			}				
			
			// check whether general is threatened
			//if (!friendGeneral.isThreatened(move)) {
			if (true) {
				possibleMoves.add(new Move(move, board.getBoardState(), player));
			}
		}
		return possibleMoves;
	}
	public Position getPosition() {
		return position;
	}
}

