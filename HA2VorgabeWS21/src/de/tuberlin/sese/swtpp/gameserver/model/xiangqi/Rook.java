package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.ArrayList;
import java.util.Collections;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class Rook implements Figur {
	
	Position position;
	
	public Rook(Position position) {
		this.position = position;
	}
	
	//TODO: check if positions after figure possible
	@Override
	public ArrayList<Move> getPossibleMoves(Board board, Player player) {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		possibleMoves.addAll(rightMoves(position, board, player));
		possibleMoves.addAll(leftMoves(position, board, player));
		possibleMoves.addAll(forwardMoves(position, board, player));
		possibleMoves.addAll(backwardMoves(position, board, player));
		
		return possibleMoves;
	}
	
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
		// right horizontal
		for (int i = position.getColumn() + 1; i < board.getBoardMatrix()[0].length; i++) {
			Position target = new Position(position.getRow(), i);
			String move = createMoveFromPositions(position, target);
			
			// check whether other friendly figures on the way: if so, break.
			if ((position.isRed(board) && Character.toString(board.getBoardMatrix()[position.getRow()][i]).matches("[GAEHRCS]")) || (!position.isRed(board) && Character.toString(board.getBoardMatrix()[position.getRow()][i]).matches("[gaehrcs]"))){
				break; // stop after first friendly figure found on the way
			}		
			
			// check whether general is threatened
			if (!friendGeneral.isThreatened(board, new Move(move, board.getBoardState(), player)) && !friendGeneral.isCheck(board, new Move(move, board.getBoardState(), player), player)) {
			//if (true) {
				possibleMoves.add(new Move(move, board.getBoardState(), player));
			}
		}
		return possibleMoves;
	}
	
	public ArrayList<Move> leftMoves(Position position, Board board, Player player) {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		General friendGeneral = board.getFriendGeneral(position);
		for (int i = position.getColumn() - 1; i >= 0; i--) {
			Position target = new Position(position.getRow(), i);
			String move = createMoveFromPositions(position, target);
			
			// check whether other friendly figures on the way: if so, break.
			if ((position.isRed(board) && Character.toString(board.getBoardMatrix()[position.getRow()][i]).matches("[GAEHRCS]")) || (!position.isRed(board) && Character.toString(board.getBoardMatrix()[position.getRow()][i]).matches("[aehrcs]"))){
				break; // stop after first friendly figure found on the way
			}			
			
			// check whether general is threatened
			if (!friendGeneral.isThreatened(board, new Move(move, board.getBoardState(), player)) && !friendGeneral.isCheck(board, new Move(move, board.getBoardState(), player), player)) {
			//if (true) {
				possibleMoves.add(new Move(move, board.getBoardState(), player));
			}
		}
		return possibleMoves;
	}
	
	public ArrayList<Move> forwardMoves(Position position, Board board, Player player) {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		General friendGeneral = board.getFriendGeneral(position);
		for (int i = position.getRow() - 1; i >= 0; i--) {
			Position target = new Position(i, position.getColumn());
			String move = createMoveFromPositions(position, target);
			
			// check whether other friendly figures on the way: if so, break.
			if ((position.isRed(board) && Character.toString(board.getBoardMatrix()[i][position.getColumn()]).matches("[GAEHRCS]")) || (!position.isRed(board) && Character.toString(board.getBoardMatrix()[i][position.getColumn()]).matches("[gaehrcs]"))){
				break; // stop after first friendly figure found on the way
			}			
			
			// check whether general is threatened
			if (!friendGeneral.isThreatened(board, new Move(move, board.getBoardState(), player)) && !friendGeneral.isCheck(board, new Move(move, board.getBoardState(), player), player)) {
			//if (true) {
				possibleMoves.add(new Move(move, board.getBoardState(), player));
			}
		}
		return possibleMoves;
	}
	
	public ArrayList<Move> backwardMoves(Position position, Board board, Player player) {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		General friendGeneral = board.getFriendGeneral(position);
		for (int i = position.getRow() + 1; i < board.getBoardMatrix().length; i++) {
			Position target = new Position(i, position.getColumn());
			String move = createMoveFromPositions(position, target);
			
			// check whether other friendly figures on the way: if so, break.
			if ((position.isRed(board) && Character.toString(board.getBoardMatrix()[i][position.getColumn()]).matches("[GAEHRCS]")) || (!position.isRed(board) && Character.toString(board.getBoardMatrix()[i][position.getColumn()]).matches("[gaehrcs]"))){
				break; // stop after first friendly figure found on the way
			}			
			
			// check whether general is threatened
			if (!friendGeneral.isThreatened(board, new Move(move, board.getBoardState(), player)) && !friendGeneral.isCheck(board, new Move(move, board.getBoardState(), player), player)) {
			//if (true) {
				possibleMoves.add(new Move(move, board.getBoardState(), player));
			}
		}
		return possibleMoves;
	}

	public Position getPosition() {
		return position;
	}
}
