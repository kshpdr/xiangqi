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
		boolean figureBefore = false;
		for (int i = position.getColumn() + 1; i < boardMatrix[0].length; i++) {
			Position target = new Position(position.getRow(), i);
			String move = createMoveFromPositions(position, target);
			
			// check whether first figure on the way was found
			if (figureBefore) {
				// if so, then check, whether an enemy figure appear afterwards
				if ((redNext && Character.toString(boardMatrix[position.getRow()][i]).matches("[gaehrcs]")) || (!redNext && Character.toString(boardMatrix[position.getRow()][i]).matches("[GAEHRCS]"))){
					possibleMoves.add(new Move(move, board.getBoardState(), player));
				}
				else {
					continue;
				}
			}
			// when first figure on the way found
			else if (boardMatrix[position.getRow()][i] != '0') {
				figureBefore = true;
				continue;
			}
			
			// check whether general is threatened
			//if (!friendGeneral.isThreatened(move)) {
			if (true) {
				possibleMoves.add(new Move(move, board.getBoardState(), player));
			}
		}
		
		figureBefore = false;
		// left horizontal
		for (int i = position.getColumn() - 1; i >= 0; i--) {
			Position target = new Position(position.getRow(), i);
			String move = createMoveFromPositions(position, target);
			
			// check whether first figure on the way was found
			if (figureBefore) {
				// if so, then check, whether an enemy figure appear afterwards
				if ((redNext && Character.toString(boardMatrix[position.getRow()][i]).matches("[gaehrcs]")) || (!redNext && Character.toString(boardMatrix[position.getRow()][i]).matches("[GAEHRCS]"))){
					possibleMoves.add(new Move(move, board.getBoardState(), player));
				}
				else {
					continue;
				}
			}
			// when first figure on the way found
			else if (boardMatrix[position.getRow()][i] != '0') {
				figureBefore = true;
				continue;
			}	
			
			// check whether general is threatened
			//if (!friendGeneral.isThreatened(move)) {
			if (true) {
				possibleMoves.add(new Move(move, board.getBoardState(), player));
			}
		}
		
		figureBefore = false;
		// backwards vertical
		for (int i = position.getRow() + 1; i < boardMatrix.length; i++) {
			Position target = new Position(i, position.getColumn());
			String move = createMoveFromPositions(position, target);
			
			// check whether first figure on the way was found
			if (figureBefore) {
				// if so, then check, whether an enemy figure appear afterwards
				if ((redNext && Character.toString(boardMatrix[i][position.getColumn()]).matches("[gaehrcs]")) || (!redNext && Character.toString(boardMatrix[i][position.getColumn()]).matches("[GAEHRCS]"))){
					possibleMoves.add(new Move(move, board.getBoardState(), player));
				}
				else {
					continue;
				}
			}
			// when first figure on the way found
			else if (boardMatrix[i][position.getColumn()] != '0') {
				figureBefore = true;
				continue;
			}				
			
			// check whether general is threatened
			//if (!friendGeneral.isThreatened(move)) {
			if (true) {
				possibleMoves.add(new Move(move, board.getBoardState(), player));
			}
		}
		
		figureBefore = false;
		// forwards vertical
		for (int i = position.getRow() - 1; i >= 0; i--) {
			Position target = new Position(i, position.getColumn());
			String move = createMoveFromPositions(position, target);
			
			// check whether first figure on the way was found
			if (figureBefore) {
				// if so, then check, whether an enemy figure appear afterwards
				if ((redNext && Character.toString(boardMatrix[i][position.getColumn()]).matches("[gaehrcs]")) || (!redNext && Character.toString(boardMatrix[i][position.getColumn()]).matches("[GAEHRCS]"))){
					possibleMoves.add(new Move(move, board.getBoardState(), player));
				}
				else {
					continue;
				}
			}
			// when first figure on the way found
			else if (boardMatrix[i][position.getColumn()] != '0') {
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
	
	public String createMoveFromPositions(Position currentPosition, Position targetPosition) {
		String move = Position.positionToString(currentPosition);
		move += '-';
		move += Position.positionToString(targetPosition);
		return move;
	}
}