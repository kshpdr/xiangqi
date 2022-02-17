package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.ArrayList;
import java.util.Arrays;

import de.tuberlin.sese.swtpp.gameserver.model.Game;
import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;
import de.tuberlin.sese.swtpp.gameserver.model.User;

public class Board {
	char[][] boardMatrix;
	String boardState;
	
	private General blackGeneral;
	private General redGeneral;
	ArrayList<Figur> figures = new ArrayList<>();
	
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
	public boolean isTodesblick(Move move) {
		char[][] boardBuf = boardMatrix.clone();			//copy of board
		//get a numeric position of start and goal position
		Position start = Position.stringToPosition(move.getMove().split("-")[0]);
		Position goal = Position.stringToPosition(move.getMove().split("-")[1]);
		//make move
		boardBuf[goal.getRow()][goal.getColumn()] = boardBuf[start.getRow()][start.getColumn()];
		boardBuf[start.getRow()][start.getColumn()] = '0';
		
		if(blackGeneral.getPosition().getColumn() == redGeneral.getPosition().getColumn()) { 	// if generals are on the same column, start check for threatening
			for( int i = blackGeneral.getPosition().getRow(); i < 10; i++) {					//go through each row on column of blackGeneral and check for other figures
				if(!String.valueOf(boardBuf[i][blackGeneral.getPosition().getColumn()]).matches("[G0]")) {	//if we find some other figure on the way, return false
					return false;
				}
				if(boardBuf[i][blackGeneral.getPosition().getColumn()] == 'G') {
					return true;
				}																			// we find enemy general
				
			}
		}
		return false;
	}
	public boolean isCheck(Player player) {
		for(Figur afigure : figures) {
			ArrayList<Move> possibleMoves = afigure.getPossibleMoves(afigure.getPosition(), this, player);
			for(Move amove : possibleMoves) {													//iterate through all possible moves of figure and 
				if(amove.getMove().split("-")[1] == Position.positionToString(afigure.getPosition())) {		//check if the goal position equals position of general, return true if so
					return true;
				}
			}
		}
		return false;
	}
	public ArrayList<Figur> getFiguresFromBoard(char[][] boardMatrix){
		ArrayList<Figur> figures = new ArrayList<>();
		// go through board and add corresponding figures
		for (int i = 0; i < boardMatrix.length; i++) {
			for (int j = 0; j < boardMatrix[0].length; j++) {
				switch (boardMatrix[i][j]) {
					case 'g': case 'G': figures.add(new General(new Position(i, j)));
					case 'a': case 'A': figures.add(new Advisor(new Position(i, j)));
					//case 'e': case 'E': figures.add(new Elephant(new Position(i, j)));
					//case 'h': case 'H': figures.add(new Horse(new Position(i, j)));
					case 'r': case 'R': figures.add(new Rook(new Position(i, j)));
					case 'c': case 'C': figures.add(new Cannon(new Position(i, j)));
					
				}
			}
		}
		
		return figures;
	}

	public static void main(String[] args) {
		
		General redG = new General(new Position(9,4));
		General blackG = new General(new Position(0,4));
		Advisor ar1 = new Advisor(new Position(8,4));
		Advisor ar2 = new Advisor(new Position(9,5));
		
		Player a = new Player(new User("a", "1"), new XiangqiGame());
		Player b = new Player(new User("b", "2"), new XiangqiGame());
		Board board = new Board("rhea1aehr/4g4/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/4A4/RHE1GAEHR");
		board.blackGeneral = blackG;
		board.redGeneral = redG;
		ArrayList<Move> redGMoves = redG.getPossibleMoves(redG.getPosition(), board, a);
		for(Move move : redGMoves) {
			System.out.println("moves for red general: " + move.getMove());
		}
		ArrayList<Move> redA1Moves = ar1.getPossibleMoves(ar1.getPosition(), board, a);
		
		for(Move move : redA1Moves) {
			System.out.println("moves for red advisor left: " + move.getMove());
		}
	}
}
