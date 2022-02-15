package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class General implements Figur {
	//attributes
	private Position position;
	
	public General() {			//braucht man ueberhaupt?
		super();
		
	}
	
	/*public static boolean isMoveOk(String move, char[][] board) {
		String[] moveArray = move.split("-");		//Ascii for 'a' = 97
		//get a numeric position of start and goal position
		int startC = moveArray[0].charAt(0) - 97;			//char of start
		int startI = moveArray[0].charAt(1);				//int of start
		
		int goalC = moveArray[1].charAt(0) - 97;			//char of start
		int goalI = moveArray[1].charAt(1);					//int of start
		
		if( goalC < 3 || goalC > 5 || goalI > 2) {
			return false;
		}
		else if(goalC - startC > 1 || goalI - startI > 1) {				//Schritt nicht groesser 1
			return false;
		}
		else if(goalC - startC != 0 && goalI- startI != 0) {			//Schritt diagonal
			return false;
		}
		else {															//todesblick
			for(int i = goalI; i < board[0].length; i++) {
				if((board[i][goalC] != 'g' || board[i][goalC] != 'G') && board[i][goalC] != '0') { //es gibt Figur davor
					return true;
				}
				if(board[i][goalC] == 'g' || board[i][goalC] == 'G' ) {								//todesblick
					return false;
				}
			}
		}
		return false;
	}
	public static char[][] move(String move, char[][] board){
		String[] moveArray = move.split("-");		//Ascii for 'a' = 97
		//get a numeric position of start and goal position
		int startC = moveArray[0].charAt(0) - 97;			//char of start
		int startI = moveArray[0].charAt(1);				//int of start
		
		int goalC = moveArray[1].charAt(0) - 97;			//char of start
		int goalI = moveArray[1].charAt(1);					//int of start
		
		if(!isMoveOk(move, board)) {
			return board;
		}
		else {
			if(board[startI][startC] == 'G') {
				board[goalI][goalC] = 'G';
			}
			else board[goalI][goalC] = 'g';
			board[startI][startC] = '0';
		}
		return board;
	}*/
	
	public boolean isTodesblick(Position position, char[][]board) {
		int col = position.getColumn();
		int row = position.getRow();
		if(row > 2) {							//black
			for(int i = row; i >= 0; i--) {
				if(board[i][col] != 'G' && board[i][col] != '0') { 	//we meet some other figure on way down
					return false;
				}
				if(board[i][col] == 'G') {
					return true;
				}
			}
		}
		else {									//red
			for(int i = row; i <= 9; i++) {		
				if(board[i][col] != 'g' && board[i][col] != '0') { 	//we meet some other figure on way up
					return false;
				}
				if(board[i][col] == 'g') {
					return true;
				}
			}
		}
		
		return false;
	}
	public void setPosition(Position pos) {
		this.position = pos;
	}
	public Position getPosition() {
		return position;
	}
	public ArrayList<Move> getPossibleMoves(Position position, Board board, Player player){
		ArrayList<Move> moves = new ArrayList<>();
		int reihe = 0;
		int column = 3;
		if(!position.isRed(board)) {
			reihe = 7;
		}
		for(int row = reihe; row <= reihe +2; row++) {
			for(int col = column; col <= column + 2; col++) {
				if(row == position.getRow() && col == position.getColumn()) {
					continue;
				}
				if(reihe > 2) {			//means we are playing black, else red
					if(String.valueOf(board.getBoardMatrix()[row][col]).matches("[gaehrcs]")) {
						continue;
					}
				}
				else if(String.valueOf(board.getBoardMatrix()[row][col]).matches("[GAEHRCS]")) {
					continue;
				}
				else if (!isTodesblick(position, board.getBoardMatrix())) {
					Position possibleGoal = new Position(row, col);
					String moveString = Position.positionToString(position);
					moveString += '-' + Position.positionToString(possibleGoal);
					Move move = new Move(moveString, board.boardState, player);
					moves.add(move);
				}
			}
		}
		return moves;
	}
}
