package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.ArrayList;


import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class Advisor implements Figur {
	private Position position;
	
	public Advisor(Position pos) {
		this.position = pos;
	}
	
	public ArrayList<Move> getPossibleMoves(Position position, Board board, Player player){
		ArrayList<Move> moves = new ArrayList<>();
		int reihe = 0;
		int spalte = 3;
		if(position.isRed(board)) {
			reihe = 7;
		}
		for(int row = reihe; row <= reihe + 2; row++) {
			for(int col = spalte; col <= spalte + 2; col++) {
				String match = "[gaehrcs]";
				if(position.isRed(board)) {
					match = "[GAEHRCS]";
				}
				Position goal = new Position(row, col);
				String moveString = Position.positionToString(position) + '-' + Position.positionToString(goal);
				Move move = new Move(moveString, board.boardState, player);
				if((Math.abs(position.getRow() - row) == 1) && (Math.abs(position.getColumn() - col) == 1) && !String.valueOf(board.boardMatrix[row][col]).matches(match) && !board.isTodesblick(move)) {
					moves.add(move);
				}
			}
		}
		return moves;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
}
