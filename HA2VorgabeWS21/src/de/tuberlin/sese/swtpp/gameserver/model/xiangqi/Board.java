package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.*;

import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class Board {
	private char[][] boardMatrix;
	String boardState;
	
	private General blackGeneral;
	private General redGeneral;
	
	ArrayList<Figur> figures = new ArrayList<>();
	ArrayList<Figur> blackFigures = new ArrayList<>();
	ArrayList<Figur> redFigures = new ArrayList<>();


	
	public Board(String state) {
		char[][] boardMatrix = boardFromState(state);
		this.setBoardMatrix(boardMatrix);
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
		return getBoardMatrix();
	}
	
	public static void main(String[] args) {
		System.out.print(Arrays.deepToString(boardFromState("rhea1a1h1/4g4/1c3r3/7cs/s1s1C4/9/S1S3SCS/R8/4A4/1HE1GAEHR")));
	}

	public char[][] getBoardMatrix() {
		return boardMatrix;
	}

	public void setBoardMatrix(char[][] boardMatrix) {
		this.boardMatrix = boardMatrix;
	}
	
	public General getFriendGeneral(Position position) {
		if (Character.isLowerCase(boardMatrix[position.getRow()][position.getColumn()])) {
			return blackGeneral;
		}
		else {
			return redGeneral;
		}
	}
	
	public String boardMatrixToboardString() {
		
		String newString = "";
		
		// iterates over boardMatrix:
		for(int i = 0; i < 10; i++) {
			
			int count = 0;
			
			for(int j = 0; j < 9; j++) {
				
				// counts empty positions:
				if(this.boardMatrix[i][j] == '0') {
					count++;
				}
				
				// appends counted empty positions and playing-piece char:
				else {
					if(count != 0) {
						newString = newString + (char) count + this.boardMatrix[i][j];
						count = 0;
					}
					else {
						newString = newString + this.boardMatrix[i][j];
					}
				}
				
				// end of row:
				if(j == 8 && count != 0) {
					// appends counted empty positions:
					newString = newString + (char) count;
				}
				if(j == 8 && 0 < i && i < 9) {
					// appends '/' between rows:
					newString = newString + '/';		
				}	
			}
		}
		
		return newString;
	}
	
	public Figur getFigurFromBoard(Position position) {
		return null;
	}
	
	public General getBlackGeneral() {
		return this.blackGeneral;
	}
	public General getRedGeneral() {
		return this.redGeneral;
	}

}
