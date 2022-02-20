package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.io.Serializable;
import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class General implements Figur,Serializable {
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
	public ArrayList<Move> getPossibleMoves( Board board, Player player){
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
				Move move = new Move(moveString, board.getBoardState(), player);
				if (!((Math.abs(position.getRow() - row) == 1) && (Math.abs(position.getColumn() - col) == 1)) && !isThreatened(board, move) && !isCheck(board, move, player) && ((Math.abs(position.getRow() - row)<2) && (Math.abs(position.getColumn() - col)< 2))) {
					moves.add(move);
				}
			}
		}
		return moves;
	}
	public General checkColour(Board board, General general) {
		General that;
		if(board.getBlackGeneralFromBoard() == general) {
			that = board.getRedGeneralFromBoard();	
		}
		else {
			that = board.getBlackGeneralFromBoard();
		}
		return that;
	}
	
	public boolean isThreatened(Board board, Move move) {
		
		// copies board:
		Board boardBuf = new Board(board.getBoardState());
		
		General that = checkColour(board, this);
		
		char match;
		if(board.getBlackGeneralFromBoard() == this) {	
			match = 'G';
		}
		else {
			match = 'g';
		}
		
		Position start = Position.stringToPosition(move.getMove().split("-")[0]);	
		Position goal = Position.stringToPosition(move.getMove().split("-")[1]);
		
		boardBuf.getBoardMatrix()[goal.getRow()][goal.getColumn()] = boardBuf.getBoardMatrix()[start.getRow()][start.getColumn()]; 
		boardBuf.getBoardMatrix()[start.getRow()][start.getColumn()] = '0';
		
		// checks whether generals in same column:
		if(this.getPosition().getColumn() == that.getPosition().getColumn()) {
			
			for( int i = this.getPosition().getRow(); i < 10; i++) {					
				if(!String.valueOf(boardBuf.getBoardMatrix()[i][this.getPosition().getColumn()]).matches("[" + match+ "0]")) {	
					return false;
				}
				if(boardBuf.getBoardMatrix()[i][this.getPosition().getColumn()] == match) {
					return true;
				}																			// we find enemy general
			}
		}
		return false;
	}
	
	
	public boolean isCheck(Board board, Move move, Player player) {
		
		// copies board:
		Board boardBuf = new Board(board.getBoardState());
		
		ArrayList<Figur> figsToCheck;
		
		Position start = Position.stringToPosition(move.getMove().split("-")[0]);	
		Position goal = Position.stringToPosition(move.getMove().split("-")[1]);
		
		// checks whether targetPosition is occupied:
		if(boardBuf.getBoardMatrix()[goal.getRow()][goal.getColumn()] != '0') {
			// deletes playing-piece on targetPosition from list of figures:
			boardBuf.deleteFigure(new Position(goal.getRow(), goal.getColumn()));	
		}
		
		if(this.getPosition().getRow() < 3) {				
			figsToCheck = board.getRedFigures();
		}
		else {												
			figsToCheck = board.getBlackFigures();
		}
		
		boardBuf.getBoardMatrix()[goal.getRow()][goal.getColumn()] = boardBuf.getBoardMatrix()[start.getRow()][start.getColumn()]; 
		boardBuf.getBoardMatrix()[start.getRow()][start.getColumn()] = '0';
		
		// iterates over enemyFigures:
		for(Figur afigure : figsToCheck) {
			ArrayList<Move> possibleMoves = afigure.getPossibleMoves(board, player);
			
			// iterates over possible Moves:
			for(Move amove : possibleMoves) {	
				
				// checks whether general on targetPosition:
				if((amove.getMove().split("-")[1]).equals(Position.positionToString(afigure.getPosition()))) {	
					return true;
				}
			}
		}
		
		return false;
	}
}
