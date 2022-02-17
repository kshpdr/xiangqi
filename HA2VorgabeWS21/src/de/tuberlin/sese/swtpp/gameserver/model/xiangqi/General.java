package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class General implements Figur {
	//attributes
	private Position position;
	
	public General(Position pos) {			//braucht man ueberhaupt?
		super();
		this.position = pos;
	}
	
	public void setPosition(Position pos) {
		this.position = pos;
	}
	public Position getPosition() {
		return position;
	}
	public ArrayList<Move> getPossibleMoves(Position position,  Board board, Player player){
		ArrayList<Move> moves = new ArrayList<>();
		int reihe = 0;
		int column = 3;
		if(position.isRed(board)) {
			reihe = 7;
		}
		for(int row = reihe; row <= reihe +2; row++) {
			for(int col = column; col <= column + 2; col++) {
				if(row == position.getRow() && col == position.getColumn()) {		// check if we are trying to move to actual position of figure
					continue;
				}
				if(reihe > 2 && String.valueOf(board.getBoardMatrix()[row][col]).matches("[GAEHRCS]")) {continue;}	//means we are playing red, else black
				else if(String.valueOf(board.getBoardMatrix()[row][col]).matches("[gaehrcs]")) { continue;}			// keine diagonale Züge  no todesblick, then free to go
				
				Position possibleGoal = new Position(row, col);
				String moveString = Position.positionToString(position) + '-' + Position.positionToString(possibleGoal);
				Move move = new Move(moveString, board.boardState, player);
				if (!((Math.abs(position.getRow() - row) == 1) && (Math.abs(position.getColumn() - col) == 1)) && !board.isTodesblick(move) && ((Math.abs(position.getRow() - row)<2) && (Math.abs(position.getColumn() - col)< 2))) {
					moves.add(move);
				}
			}
		}
		return moves;
	}
	
}
