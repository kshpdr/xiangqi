package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class Rook implements Figur {
	
	Position position;
	
	public Rook(Position position) {
		this.position = position;
	}

	@Override
	public ArrayList<Move> getPossibleMoves(Position position, Board board, Player player) {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		char[][] boardMatrix = board.getCurrentBoard();
		General friendGeneral = board.getFriendGeneral(position);
		General enemyGeneral = board.getEnemyGeneral(position);
		boolean redNext;
		
		
		// specify friend and enemy generals
		if (Character.isLowerCase(boardMatrix[position.getRow()][position.getColumn()])) {
			redNext = false;
			friendGeneral = board.getBlackGeneral();
			enemyGeneral = board.getRedGeneral();
		}
		else {
			redNext = true;
			enemyGeneral = board.getBlackGeneral();
			friendGeneral = board.getRedGeneral();	
		}
		
		// right horizontal
		for (int i = position.getColumn() + 1; i < boardMatrix[0].length; i++) {
			Position target = new Position(position.getRow(), i);
			String move = createMoveFromPositions(position, target);
			
			// check whether other friendly figures on the way: if so, break.
			if ((redNext && Character.toString(boardMatrix[position.getRow()][i]).matches("[GAEHRCS]")) || (!redNext && Character.toString(boardMatrix[position.getRow()][i]).matches("[gaehrcs]"))){
				break; // stop after first friendly figure found on the way
			}		
			
			// check whether general is threatened
			//if (!friendGeneral.isThreatened(move)) {
			if (true) {
				possibleMoves.add(new Move(move, board.getBoardState(), player));
			}
		}
		
		// left horizontal
		for (int i = position.getColumn() - 1; i >= 0; i--) {
			Position target = new Position(position.getRow(), i);
			String move = createMoveFromPositions(position, target);
			
			// check whether other friendly figures on the way: if so, break.
			if ((redNext && Character.toString(boardMatrix[position.getRow()][i]).matches("[GAEHRCS]")) || (!redNext && Character.toString(boardMatrix[position.getRow()][i]).matches("[aehrcs]"))){
				break; // stop after first friendly figure found on the way
			}			
			
			// check whether general is threatened
			//if (!friendGeneral.isThreatened(move)) {
			if (true) {
				possibleMoves.add(new Move(move, board.getBoardState(), player));
			}
		}
		
		// backwards vertical
		for (int i = position.getRow() + 1; i < boardMatrix.length; i++) {
			Position target = new Position(i, position.getColumn());
			String move = createMoveFromPositions(position, target);
			
			// check whether other friendly figures on the way: if so, break.
			if ((redNext && Character.toString(boardMatrix[i][position.getColumn()]).matches("[GAEHRCS]")) || (!redNext && Character.toString(boardMatrix[i][position.getColumn()]).matches("[gaehrcs]"))){
				break; // stop after first friendly figure found on the way
			}			
			
			// check whether general is threatened
			//if (!friendGeneral.isThreatened(move)) {
			if (true) {
				possibleMoves.add(new Move(move, board.getBoardState(), player));
			}
		}
		
		// forwards vertical
		for (int i = position.getRow() - 1; i >= 0; i--) {
			Position target = new Position(i, position.getColumn());
			String move = createMoveFromPositions(position, target);
			
			// check whether other friendly figures on the way: if so, break.
			if ((redNext && Character.toString(boardMatrix[i][position.getColumn()]).matches("[GAEHRCS]")) || (!redNext && Character.toString(boardMatrix[i][position.getColumn()]).matches("[gaehrcs]"))){
				break; // stop after first friendly figure found on the way
			}			
			
			// check whether general is threatened
			//if (!friendGeneral.isThreatened(move)) {
			if (true) {
				possibleMoves.add(new Move(move, board.getBoardState(), player));
			}
		}
		
		return possibleMoves;
	}
	
	public String createMoveFromPositions(Position currentPosition, Position targetPosition) {
		String move = Position.positionToString(currentPosition);
		move += '-';
		move += Position.positionToString(targetPosition);
		return move;
	}
	
	public static void main(String args[]) {
	}
	
}
