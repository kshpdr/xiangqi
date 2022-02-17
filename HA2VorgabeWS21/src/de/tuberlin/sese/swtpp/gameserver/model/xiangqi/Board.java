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
	ArrayList<Figur> blackFigures = new ArrayList<>();
	ArrayList<Figur> redFigures = new ArrayList<>();
	
	public Board(String state) {
		char[][] boardMatrix = boardFromState(state);
		this.boardMatrix = boardMatrix;
		this.boardState = state;
		this.figures = getFiguresFromBoard(boardMatrix);
		this.blackFigures = getBlackFiguresFromBoard(boardMatrix);
		this.redFigures = getRedFiguresFromBoard(boardMatrix);
		this.blackGeneral = getBlackGeneralFromBoard();
		this.redGeneral = getRedGeneralFromBoard();
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
	
	
	// ** GETTERS AND SETTERS ** 
	public String getBoardState() {
		return boardState;
	}
	
	public void setBoardState(String state) {
		this.boardState = state;
	}
	
	public char[][] getBoardMatrix(){
		return boardMatrix;
	}
	
	public ArrayList<Figur> getFigures(){
		return figures;
	}
	
	public ArrayList<Figur> getRedFigures(){
		return redFigures;
	} 
	
	public ArrayList<Figur> getBlackFigures(){
		return blackFigures;
	} 
	
	// ** USEFUL STUFF **
	
	public ArrayList<Figur> getFiguresFromBoard(char[][] boardMatrix){
		ArrayList<Figur> figures = new ArrayList<>();
		figures.addAll(getRedFiguresFromBoard(boardMatrix));
		figures.addAll(getBlackFiguresFromBoard(boardMatrix));	
		return figures;
	}
	
	public ArrayList<Figur> getRedFiguresFromBoard(char[][] boardMatrix){
		ArrayList<Figur> redFigures = new ArrayList<>();
		// go through board and add corresponding figures
		for (int i = 0; i < boardMatrix.length; i++) {
			for (int j = 0; j < boardMatrix[0].length; j++) {
				switch (boardMatrix[i][j]) {
					case 'G': redFigures.add(new General(new Position(i, j))); break;
					//case 'A': redFigures.add(new Advisor(new Position(i, j)));
					//case 'E': redFigures.add(new Elephant(new Position(i, j)));
					//case 'H': redFigures.add(new Horse(new Position(i, j)));
					case 'R': redFigures.add(new Rook(new Position(i, j))); break;
					case 'C': redFigures.add(new Cannon(new Position(i, j))); break;
					case 'S': redFigures.add(new Soldier(new Position(i, j))); break;
				}
			}
		}
		return redFigures;
	}
	
	public ArrayList<Figur> getBlackFiguresFromBoard(char[][] boardMatrix){
		ArrayList<Figur> blackFigures = new ArrayList<>();
		// go through board and add corresponding figures
		for (int i = 0; i < boardMatrix.length; i++) {
			for (int j = 0; j < boardMatrix[0].length; j++) {
				switch (boardMatrix[i][j]) {
					case 'G': blackFigures.add(new General(new Position(i, j))); break;
					//case 'A': blackFigures.add(new Advisor(new Position(i, j)));
					//case 'E': blackFigures.add(new Elephant(new Position(i, j)));
					//case 'H': blackFigures.add(new Horse(new Position(i, j)));
					case 'R': blackFigures.add(new Rook(new Position(i, j))); break;
					case 'C': blackFigures.add(new Cannon(new Position(i, j))); break;
					case 'S': blackFigures.add(new Soldier(new Position(i, j))); break;
				}
			}
		}
		return blackFigures;
	}
	
	public General getRedGeneralFromBoard() {
		for (Figur figure : figures) {
			if (boardMatrix[figure.getPosition().getRow()][figure.getPosition().getColumn()] == 'G') {
				return (General) figure;
			}
		}
		return null;
	}
	
	public General getBlackGeneralFromBoard() {
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
	
	public Figur getFigurFromBoard(Position position) {
		for (Figur figure : figures) {
			if (figure.getPosition().getRow() == position.getRow() && figure.getPosition().getColumn() == position.getColumn()) {
				return figure;
			}
		}
		return null;
	}

	
	public static void main(String[] args) {
		System.out.print(Arrays.deepToString(boardFromState("rhea1a1h1/4g4/1c3r3/7cs/s1s1C4/9/S1S3SCS/R8/4A4/1HE1GAEHR")));
	}
}
