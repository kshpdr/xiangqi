package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class Soldier implements Figur {
	Position position;
	
	public Soldier(Position position) {
		this.position = position;
	}

	@Override
	public ArrayList<Move> getPossibleMoves(Position position, Board board, Player player) {
		// TODO Auto-generated method stub
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		char[][] boardMatrix = board.getCurrentBoard();
		General friendGeneral = board.getFriendGeneral(position);
		General enemyGeneral = board.getEnemyGeneral(position);
		boolean redNext;
		boolean isRiverCrossed = isRiverCrossed(position, board);
		
		
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
		
		if (redNext) {
			// check if river crossed
			if (isRiverCrossed) {
				Position targetLeft = new Position(position.getRow(), position.getColumn() - 1);
				Position targetRight = new Position(position.getRow(), position.getColumn() + 1);
				String moveLeft = createMoveFromPositions(position, targetLeft);
				String moveRight = createMoveFromPositions(position, targetRight);
				
				// check if friendly figure stays on the left target position
				if (!Character.toString(boardMatrix[targetLeft.getRow()][targetLeft.getColumn()]).matches("[GAEHRCS]")) {
					possibleMoves.add(new Move(moveLeft, board.getBoardState(), player));
				}
				
				// check if friendly figure stays on the right target position
				if (!Character.toString(boardMatrix[targetRight.getRow()][targetRight.getColumn()]).matches("[GAEHRCS]")) {
					possibleMoves.add(new Move(moveRight, board.getBoardState(), player));
				}

				// check if last row was reached
				if (position.getRow() != 0) {
					Position targetForward = new Position(position.getRow() - 1, position.getColumn());
					String moveForward = createMoveFromPositions(position, targetForward);
					
					// check if friendly figure stays on the forward target position
					if (!Character.toString(boardMatrix[targetForward.getRow()][targetForward.getColumn()]).matches("[GAEHRCS]")) {
						possibleMoves.add(new Move(moveForward, board.getBoardState(), player));
					}
				}
				else {
					Position targetForward = new Position(position.getRow() - 1, position.getColumn());
					String moveForward = createMoveFromPositions(position, targetForward);
					if (boardMatrix[position.getRow()-1][position.getColumn()] == '0') {
						possibleMoves.add(new Move(moveForward, board.getBoardState(), player));
					}	
				}
			}
		}
		else {
			// check if river crossed
			if (isRiverCrossed) {
				Position targetLeft = new Position(position.getRow(), position.getColumn() - 1);
				Position targetRight = new Position(position.getRow(), position.getColumn() + 1);
				String moveLeft = createMoveFromPositions(position, targetLeft);
				String moveRight = createMoveFromPositions(position, targetRight);
				
				// check if friendly figure stays on the left target position
				if (!Character.toString(boardMatrix[targetLeft.getRow()][targetLeft.getColumn()]).matches("[gaehrcs]")) {
					possibleMoves.add(new Move(moveLeft, board.getBoardState(), player));
				}
				
				// check if friendly figure stays on the right target position
				if (!Character.toString(boardMatrix[targetRight.getRow()][targetRight.getColumn()]).matches("[gaehrcs]")) {
					possibleMoves.add(new Move(moveRight, board.getBoardState(), player));
				}

				// check if last row was reached
				if (position.getRow() != 0) {
					Position targetForward = new Position(position.getRow() + 1, position.getColumn());
					String moveForward = createMoveFromPositions(position, targetForward);
					
					// check if friendly figure stays on the forward target position
					if (!Character.toString(boardMatrix[targetForward.getRow()][targetForward.getColumn()]).matches("[gaehrcs]")) {
						possibleMoves.add(new Move(moveForward, board.getBoardState(), player));
					}
				}
				else {
					Position targetForward = new Position(position.getRow() + 1, position.getColumn());
					String moveForward = createMoveFromPositions(position, targetForward);
					if (boardMatrix[position.getRow()-1][position.getColumn()] == '0') {
						possibleMoves.add(new Move(moveForward, board.getBoardState(), player));
					}	
				}
			}
		}
		
		return possibleMoves;
		
	}
	
	public boolean isRiverCrossed (Position position, Board board) {
		char[][] boardMatrix = board.getCurrentBoard();
		if (Character.isLowerCase(boardMatrix[position.getRow()][position.getColumn()])){
			if (position.getRow() >= 5) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			if (position.getRow() <= 4) {
				return true;
			}
			else {
				return false;
			}
		}
	}

	public String createMoveFromPositions(Position currentPosition, Position targetPosition) {
		String move = Position.positionToString(currentPosition);
		move += '-';
		move += Position.positionToString(targetPosition);
		return move;
	}
}
