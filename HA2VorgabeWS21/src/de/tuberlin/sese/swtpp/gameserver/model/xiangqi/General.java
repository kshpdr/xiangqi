package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

public class General extends Figure {
	//attributes
	private String colour;
	
	public General(String colour) {			//braucht man ueberhaupt?
		super();
		this.colour = colour;
	}
	
	public static boolean isMoveOk(String move, char[][] board) {
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
	}
	public boolean isTodesblick(char[][]board) {
		
		
		return false;
	}
}
