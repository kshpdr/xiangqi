package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.ArrayList;
import java.util.Arrays;

import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class Board {
	private char[][] boardMatrix;
	private String boardState;
	
	private General blackGeneral;
	private General redGeneral;
	
	ArrayList<Figur> figures = new ArrayList<>();
	
	public Board(String state) {
		char[][] boardMatrix = boardFromState(state);
		this.boardMatrix = boardMatrix;
		this.boardState = state;
		this.figures = getFiguresFromBoard(boardMatrix);
		this.blackGeneral = getBlackGeneral();
		this.redGeneral = getRedGeneral();
	}
	
	public static char[][] boardFromState(String state) {
		char[][] board = new char[10][9];
 		int row = 0;
		int column = 0;
		
		// iterate over a state row-wise
		for (int i = 0; i < state.length(); i++){
		    char c = state.charAt(i);   
		    // if delimiter then go to then next row
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
	
	public General getRedGeneral() {
		for (Figur figure : figures) {
			if (boardMatrix[figure.getPosition().getRow()][figure.getPosition().getColumn()] == 'G') {
				return (General) figure;
			}
		}
		return null;
	}
	
	public General getBlackGeneral() {
		for (Figur figure : figures) {
			if (boardMatrix[figure.getPosition().getRow()][figure.getPosition().getColumn()] == 'g') {
				return (General) figure;
			}
		}
		return null;
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
		if (Character.isLowerCase(boardMatrix[position.getRow()][position.getColumn()])) {
			return blackGeneral;
		}
		else {
			return redGeneral;
		}
	};
	
	public ArrayList<Figur> getFigures(){
		return figures;
	}
	
	public ArrayList<Figur> getFiguresFromBoard(char[][] boardMatrix){
		ArrayList<Figur> figures = new ArrayList<>();
		// go through board and add corresponding figures
		for (int i = 0; i < boardMatrix.length; i++) {
			for (int j = 0; j < boardMatrix[0].length; j++) {
				switch (boardMatrix[i][j]) {
					case 'g': case 'G': figures.add(new General(new Position(i, j))); break;
					//case 'a': case 'A': figures.add(new Advisor(new Position(i, j)));
					//case 'e': case 'E': figures.add(new Elephant(new Position(i, j)));
					//case 'h': case 'H': figures.add(new Horse(new Position(i, j)));
					case 'r': case 'R': figures.add(new Rook(new Position(i, j))); break;
					case 'c': case 'C': figures.add(new Cannon(new Position(i, j))); break;
					case 's': case 'S': figures.add(new Soldier(new Position(i, j))); break;
				}
			}
		}
		
		return figures;
	}

	
	public static void main(String[] args) {
		System.out.print(Arrays.deepToString(boardFromState("rhea1a1h1/4g4/1c3r3/7cs/s1s1C4/9/S1S3SCS/R8/4A4/1HE1GAEHR")));
	}
}
