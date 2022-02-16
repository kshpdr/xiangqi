package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.Arrays;

import de.tuberlin.sese.swtpp.gameserver.model.Move;

public class Board {
	char[][] boardMatrix;
	String boardState;
	
	private General blackGeneral;
	private General redGeneral;

	
	public Board(String state) {
		char[][] boardMatrix = boardFromState(state);
		this.boardMatrix = boardMatrix;
		this.boardState = state;
	}
	
	public static char[][] boardFromState(String state) {
		char[][] board = new char[10][9];
 		int row = 0;
		int column = 0;
		
		// iterate over a state
		for (int i = 0; i < state.length(); i++){
		    char c = state.charAt(i);   
		    if (c == '/') {
		    	row++;
		    	column = 0;
		    }
		    else if (String.valueOf(c).matches("[0-9]")) {
		    	for (int j = 0; j < Integer.parseInt(String.valueOf(c)); j++) {
		    		board[row][column] = '0';
		    		column++;
		    	}
		    }
		    else if (String.valueOf(c).matches("[gaehrcsGAEHRCS]")) {
		    	board[row][column] = c;
		    	column++;
		    }
		}	
		return board;
	}
	
	public String getBoardState() {
		return boardState;
	}
	
	public void setBoardState(String state) {
		this.boardState = state;
	}
	
	public char[][] getCurrentBoard(){
		return boardMatrix;
	}
	public char[][] getBoardMatrix(){
		return boardMatrix;
	}
	public General getRedGeneral() {
		return redGeneral;
	}
	
	public General getBlackGeneral() {
		return blackGeneral;
	}
	
	public General getFriendGeneral(Position position) {
		if (Character.isLowerCase(boardMatrix[position.getRow()][position.getColumn()])) {
			return blackGeneral;
		}
		else {
			return redGeneral;
		}
	};
	
	public General getEnemyGeneral(Position position) {
		if (!Character.isLowerCase(boardMatrix[position.getRow()][position.getColumn()])) {
			return blackGeneral;
		}
		else {
			return redGeneral;
		}
	};
	public boolean isTodesblick(Board board, Move move) {
		char [][] boardMatrix = board.boardMatrix;
		char[][] boardBuf = boardMatrix.clone();			//copy of board
		//get a numeric position of start and goal position
		int startC = move.getMove().charAt(0) - 97;			//char of start
		int startI = 10 - move.getMove().charAt(1);				//int of start		10 - ... is because of matrix indices in java
		int goalC = move.getMove().charAt(3) - 97;			//char of start
		int goalI = 10 - move.getMove().charAt(4);					//int of start
		//make move
		boardBuf[goalI][goalC] = boardBuf[startI][startC];
		boardBuf[startI][startC] = '0';
		
		if(blackGeneral.getPosition().getColumn() == redGeneral.getPosition().getColumn()) { 	// if generals are on the same column, start check for threatening
			for( int i = blackGeneral.getPosition().getRow(); i < 10; i++) {					//go through each row on column of blackGeneral and check for other figures
				if(!String.valueOf(boardBuf[i][blackGeneral.getPosition().getColumn()]).matches("[G0]")) {	//if we find some other figure on the way, return false
					return false;
				}
				else return true;																			// we find enemy general
			}
		}
		return false;
	}
	public static void main(String[] args) {
		System.out.print(Arrays.deepToString(boardFromState("rhea1a1h1/4g4/1c3r3/7cs/s1s1C4/9/S1S3SCS/R8/4A4/1HE1GAEHR")));
	}
}
