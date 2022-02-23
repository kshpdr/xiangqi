package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.io.Serializable;
import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class Soldier implements Figur,Serializable {
	Position position;
	
	public Soldier(Position position) {
		this.position = position;
	}
	
	public Position getPosition() {
		return position;
	}
	
	@Override
	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public ArrayList<Move> getPossibleMoves(Board board, Player player) {
		
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		possibleMoves.addAll(moveLeft(position, board, player));
		possibleMoves.addAll(moveRight(position, board, player));
		possibleMoves.addAll(moveForward(position, board, player));
		
		return possibleMoves;
		
	}
	
	public boolean isRiverCrossed (Position position, Board board) {
		
		char[][] boardMatrix = board.getBoardMatrix();
		
		// if black:
		if (Character.isLowerCase(boardMatrix[position.getRow()][position.getColumn()])){
			if (position.getRow() >= 5) {
				return true;
			}
			else {
				return false;
			}
		}
		// if red:
		else {
			if (position.getRow() <= 4) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	public ArrayList<Move> moveForward(Position position, Board board, Player player) {
				
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		// if red:
		int lastRow = 0;
		int nextStep = -1;
		String figurePattern = "[GAEHRCS]";
		
		// if black:
		if (!position.isRed(board)) {
			lastRow = 9;
			nextStep = 1;
			figurePattern = "[gaehrcs]";
		}
		
		// checks if last row was reached:
		if (position.getRow() != lastRow) {
			
			// new position:
			Position targetForward = new Position(position.getRow() + nextStep, position.getColumn());
			// new move:
			Move moveForward = new Move(position.moveString(targetForward), board.getBoardState(), player);
			
			// checks whether target position is on board:
			if(targetForward.onBoard()) {
				
				// checks if friendly figure stays on the forward target position
				if (!Character.toString(board.getBoardMatrix()[targetForward.getRow()][targetForward.getColumn()]).matches(figurePattern)) {
					possibleMoves.add(moveForward);
				}
			}
			
		}
		return possibleMoves;
	};
	
	public ArrayList<Move> moveLeft(Position position, Board board, Player player){
		
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		// if red:
		String figurePattern = "[GAEHRCS]";
		
		// if black:
		if (!position.isRed(board)) {
			figurePattern = "[gaehrcs]";
		}
		
		if (isRiverCrossed(position, board)) {
			
			Position targetLeft = new Position(position.getRow(), position.getColumn() - 1);
			Move moveLeft = new Move(position.moveString(targetLeft), board.getBoardState(), player);
			
			// checks whether target position on board:
			if(targetLeft.onBoard()) {
				
				// checks whether friendly figure stays on the left target position:
				if (!Character.toString(board.getBoardMatrix()[targetLeft.getRow()][targetLeft.getColumn()]).matches(figurePattern)) {
					possibleMoves.add(moveLeft);
				}
			}	
		}
		return possibleMoves;
	}
	
	public ArrayList<Move> moveRight(Position position, Board board, Player player){
		
		String figurePattern = "[GAEHRCS]";
		
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		// if black plays, then change the variable
		if (!position.isRed(board)) {
			figurePattern = "[gaehrcs]";
		}
		
		if (isRiverCrossed(position, board)) {
			
			Position targetRight = new Position(position.getRow(), position.getColumn() + 1);
			Move moveRight = new Move(position.moveString(targetRight), board.getBoardState(), player);
			
			// checks if target position is on board:
			if(targetRight.onBoard()) {
				
				// checks if friendly figure stays on the left target position
				if (!Character.toString(board.getBoardMatrix()[targetRight.getRow()][targetRight.getColumn()]).matches(figurePattern)) {
					possibleMoves.add(moveRight);
				}
			}
			
		}
		return possibleMoves;
	}
}
