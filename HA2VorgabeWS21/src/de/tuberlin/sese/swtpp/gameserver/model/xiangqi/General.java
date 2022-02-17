package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class General implements Figur {
	//attributes
	private Position position;
	
	public General(Position pos) {			//braucht man ueberhaupt?
		//super();
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
				if (!((Math.abs(position.getRow() - row) == 1) && (Math.abs(position.getColumn() - col) == 1)) && !isThreatened(board, move) && ((Math.abs(position.getRow() - row)<2) && (Math.abs(position.getColumn() - col)< 2))) {
					moves.add(move);
				}
			}
		}
		return moves;
	}
	public General checkColour(Board board, General general) {
		General that;
		if(board.getBlackGeneral() == general) {
			that = board.getRedGeneral();	
		}
		else {
			that = board.getBlackGeneral();
		}
		return that;
	}
	
	public boolean isThreatened(Board board, Move move) {
		char[][] boardBuf = board.boardMatrix.clone();			//copy of board
		General that = checkColour(board, this);
		char match;
		if(board.getBlackGeneral() == this) {	
			match = 'G';
		}
		else {
			match = 'g';
		}
		Position start = Position.stringToPosition(move.getMove().split("-")[0]);	//get a numeric position of start and goal position
		Position goal = Position.stringToPosition(move.getMove().split("-")[1]);
		boardBuf[goal.getRow()][goal.getColumn()] = boardBuf[start.getRow()][start.getColumn()]; //make move
		boardBuf[start.getRow()][start.getColumn()] = '0';
		if(this.getPosition().getColumn() == that.getPosition().getColumn()) { 	// if generals are on the same column, start check for threatening
			for( int i = this.getPosition().getRow(); i < 10; i++) {					//go through each row on column of blackGeneral and check for other figures
				if(!String.valueOf(boardBuf[i][this.getPosition().getColumn()]).matches("[" + match+ "0]")) {	//if we find some other figure on the way, return false
					return false;
				}
				if(boardBuf[i][this.getPosition().getColumn()] == match) {
					return true;
				}																			// we find enemy general
			}
		}
		return false;
	}
	public boolean isCheck(Player player, Board board) {
		ArrayList<Figur> figsToCheck;
		if(this.getPosition().getRow() < 3) {				//means we have black general
			figsToCheck = board.redFigures;
		}
		else {												//we have red general, so check black figures if they may be dangerous
			figsToCheck = board.blackFigures;
		}
		for(Figur afigure : figsToCheck) {
			ArrayList<Move> possibleMoves = afigure.getPossibleMoves(afigure.getPosition(), board, player);
			
			for(Move amove : possibleMoves) {													//iterate through all possible moves of figure and 
				if(amove.getMove().split("-")[1] == Position.positionToString(afigure.getPosition())) {		//check if the goal position equals position of general, return true if so
					return true;
				}
			}
		}
		
		
		return false;
	}
}
