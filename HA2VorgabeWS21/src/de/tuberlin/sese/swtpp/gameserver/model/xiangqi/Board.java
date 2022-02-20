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
		
		// iterate over a stateString:
		for (int i = 0; i < state.length(); i++){
		    
			char c = state.charAt(i);   
		    
			// end of row:
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
		
		// iterates over board:
		for (int i = 0; i < boardMatrix.length; i++) {
			for (int j = 0; j < boardMatrix[0].length; j++) {
				
				// adds red figures:
				switch (boardMatrix[i][j]) {
					case 'G': redFigures.add(new General(new Position(i, j))); break;
					case 'A': redFigures.add(new Advisor(new Position(i, j))); break;
					case 'E': redFigures.add(new Elephant(new Position(i, j))); break;
					case 'H': redFigures.add(new Horse(new Position(i, j))); break;
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
		
		// iterates over board:
		for (int i = 0; i < boardMatrix.length; i++) {
			for (int j = 0; j < boardMatrix[0].length; j++) {
				
				// adds black figures:
				switch (boardMatrix[i][j]) {
					case 'g': blackFigures.add(new General(new Position(i, j))); break;
					case 'a': blackFigures.add(new Advisor(new Position(i, j))); break;
					case 'e': blackFigures.add(new Elephant(new Position(i, j))); break;
					case 'h': blackFigures.add(new Horse(new Position(i, j))); break;
					case 'r': blackFigures.add(new Rook(new Position(i, j))); break;
					case 'c': blackFigures.add(new Cannon(new Position(i, j))); break;
					case 's': blackFigures.add(new Soldier(new Position(i, j))); break;
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
	
public String boardMatrixToBoardString() {
		
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


	
	public static void main(String[] args) {
		System.out.print(Arrays.deepToString(boardFromState("rhea1a1h1/4g4/1c3r3/7cs/s1s1C4/9/S1S3SCS/R8/4A4/1HE1GAEHR")));
	}

	
	
	public void deleteFigure(Position position) {
		
		boolean isRed = position.isRed(this);
		
		// iterates over all figures:
		for(Figur f : this.figures) {
			
			if(f.getPosition().getRow() == position.getRow() && f.getPosition().getColumn() == position.getColumn()) {
				
				// deletes figure in list of all figures:
				this.figures.remove(f);
				// deletes figure in boardMatrix:
				this.boardMatrix[position.getRow()][position.getColumn()] = '0';
				
				// deletes figure in redFigures or blackFigures:
				if(isRed) {
					this.redFigures.remove(f);
				}
				else {
					this.blackFigures.remove(f);
				}
			}
		}	
	}
}
