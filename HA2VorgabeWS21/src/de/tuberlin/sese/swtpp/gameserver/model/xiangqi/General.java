package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.io.Serializable;
import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class General implements Figur,Serializable {
	private static final long serialVersionUID = 5424778147226994452L;
	
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
	
	
//	public boolean isCheck(Board board, Move move, Player player) {
//		
//		// copies board:
//		Board boardBuf = new Board(board.getBoardState());
//		
//		ArrayList<Figur> figsToCheck;
//		
//		Position start = Position.stringToPosition(move.getMove().split("-")[0]);	
//		Position goal = Position.stringToPosition(move.getMove().split("-")[1]);
//		
//		// checks whether targetPosition is occupied:
//		if(boardBuf.getBoardMatrix()[goal.getRow()][goal.getColumn()] != '0') {
//			// deletes playing-piece on targetPosition from list of figures:
//			boardBuf.deleteFigure(new Position(goal.getRow(), goal.getColumn()));	
//		}
//		
//		if(this.getPosition().getRow() < 3) {				
//			figsToCheck = board.getRedFigures();
//		}
//		else {												
//			figsToCheck = board.getBlackFigures();
//		}
//		
//		boardBuf.getBoardMatrix()[goal.getRow()][goal.getColumn()] = boardBuf.getBoardMatrix()[start.getRow()][start.getColumn()]; 
//		boardBuf.getBoardMatrix()[start.getRow()][start.getColumn()] = '0';
//		
//		// iterates over enemyFigures:
//		for(Figur afigure : figsToCheck) {
//			ArrayList<Move> possibleMoves = afigure.getPossibleMoves(board, player);
//			
//			// iterates over possible Moves:
//			for(Move amove : possibleMoves) {	
//				
//				// checks whether general on targetPosition:
//				if((amove.getMove().split("-")[1]).equals(Position.positionToString(afigure.getPosition()))) {	
//					return true;
//				}
//			}
//		}
//		
//		return false;
//	}
	
	public boolean isCheck(Board board, Move move, Player player) {
		// copies board:
		Board boardBuf = new Board(board.getBoardState());
		boolean isRed = position.isRed(board);
		
		Position start = Position.stringToPosition(move.getMove().split("-")[0]);	
		Position goal = Position.stringToPosition(move.getMove().split("-")[1]);
		
		boardBuf.getBoardMatrix()[goal.getRow()][goal.getColumn()] = boardBuf.getBoardMatrix()[start.getRow()][start.getColumn()]; 
		boardBuf.getBoardMatrix()[start.getRow()][start.getColumn()] = '0';

		// ROOK & CANNON
		if (isCheckRook(boardBuf.getBoardMatrix(), isRed) || isCheckCannon(boardBuf.getBoardMatrix(), isRed) || isCheckHorse(boardBuf.getBoardMatrix(), isRed) || isCheckSoldier(boardBuf.getBoardMatrix(), isRed)) {
			return true;
		}
		
		return false;
	}
	
	public boolean isCheckRook(char[][] boardMatrix, boolean isRed) {
		char enemyRook;
		if (isRed) {
			enemyRook = 'r';
		}
		else {
			enemyRook = 'R';
		}
		
		// ROOK
		// right horizontally
		for (int i = position.getColumn() + 1; i < boardMatrix.length; i++) {
			if (boardMatrix[position.getRow()][i] == '0') {
				continue;
			}
			else if (boardMatrix[position.getRow()][i] != enemyRook) {
				break;
			}
			else if (boardMatrix[position.getRow()][i] == enemyRook) {
				return true;
			}
		}
		
		// left horizontally
		for (int i = position.getColumn() - 1; i >= 0; i--) {
			if (boardMatrix[position.getRow()][i] == '0') {
				continue;
			}
			else if (boardMatrix[position.getRow()][i] != enemyRook) {
				break;
			}
			else if (boardMatrix[position.getRow()][i] == enemyRook) {
				return true;
			}
		}
		
		// vertically backward
		for (int i = position.getRow() + 1; i < boardMatrix[0].length; i++) {
			if (boardMatrix[i][position.getColumn()] == '0') {
				continue;
			}
			else if (boardMatrix[i][position.getColumn()] != enemyRook) {
				break;
			}
			else if (boardMatrix[i][position.getColumn()] == enemyRook) {
				return true;
			}
		}
		
		// vertically forward
		for (int i = position.getRow() - 1; i >= 0; i--) {
			if (boardMatrix[i][position.getColumn()] == '0') {
				continue;
			}
			else if (boardMatrix[i][position.getColumn()] != enemyRook) {
				break;
			}
			else if (boardMatrix[i][position.getColumn()] == enemyRook) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean cannonRight(char[][] boardMatrix, boolean isRed, char enemyCannon) {
		boolean figurBefore = false;
		for (int i = position.getRow() + 1; i < boardMatrix.length; i++) {
			if (boardMatrix[i][position.getColumn()] == '0') {
				continue;
			}
			else if (boardMatrix[i][position.getColumn()] != '0') {
				// if first figure on the way found go further
				if (!figurBefore) {
					figurBefore = true;
					continue;
				}
				else {
					// if next figure after that cannon, then check
					if (boardMatrix[i][position.getColumn()] == enemyCannon) {
						return true;
					}
					else {
						break;
					}
				}
			}
		}
		return false;
	}
	public boolean cannonLeft(char[][] boardMatrix, boolean isRed, char enemyCannon) {
		boolean figurBefore = false;
		for (int i = position.getRow() - 1; i >= 0; i--) {
			if (boardMatrix[i][position.getColumn()] == '0') {
				continue;
			}
			else if (boardMatrix[i][position.getColumn()] != '0') {
				// if first figure on the way found go further
				if (!figurBefore) {
					figurBefore = true;
					continue;
				}
				else {
					// if next figure after that cannon, then check
					if (boardMatrix[i][position.getColumn()] == enemyCannon) {
						return true;
					}
					else {
						break;
					}
				}
			}
		}
		return false;
	}
	public boolean cannonBack(char[][] boardMatrix, boolean isRed, char enemyCannon) {
		boolean figurBefore = false;
		for (int i = position.getColumn() + 1; i < boardMatrix[0].length; i++) {
			if (boardMatrix[position.getRow()][i] == '0') {
				continue;
			}
			else if (boardMatrix[position.getRow()][i] != '0') {
				// if first figure on the way found go further
				if (!figurBefore) {
					figurBefore = true;
					continue;
				}
				else {
					// if next figure after that cannon, then check
					if (boardMatrix[position.getRow()][i] == enemyCannon) {
						return true;
					}
					else {
						break;
					}
				}
			}
		}
		return false;
	}
	public boolean cannonForward(char[][] boardMatrix, boolean isRed, char enemyCannon) {
		boolean figurBefore = false;
		for (int i = position.getColumn() - 1; i >= 0; i--) {
			if (boardMatrix[position.getRow()][i] == '0') {
				continue;
			}
			else if (boardMatrix[position.getRow()][i] != '0') {
				// if first figure on the way found go further
				if (!figurBefore) {
					figurBefore = true;
					continue;
				}
				else {
					// if next figure after that cannon, then check
					if (boardMatrix[position.getRow()][i] == enemyCannon) {
						return true;
					}
					else {
						break;
					}
				}
			}
		}
		return false;
	}
	
	public boolean isCheckCannon(char[][] boardMatrix, boolean isRed) {
		char enemyCannon;
		if (isRed) {
			enemyCannon = 'c';
		}
		else {
			enemyCannon = 'C';
		}
		
		// right horizontally
		/*boolean figurBefore = false;
		for (int i = position.getRow() + 1; i < boardMatrix.length; i++) {
			if (boardMatrix[i][position.getColumn()] == '0') {
				continue;
			}
			else if (boardMatrix[i][position.getColumn()] != '0') {
				// if first figure on the way found go further
				if (!figurBefore) {
					figurBefore = true;
					continue;
				}
				else {
					// if next figure after that cannon, then check
					if (boardMatrix[i][position.getColumn()] == enemyCannon) {
						return true;
					}
					else {
						break;
					}
				}
			}
		}*/
		boolean right = cannonRight(boardMatrix, isRed, enemyCannon);
		
		// left horizontally
		/*figurBefore = false;
		for (int i = position.getRow() - 1; i >= 0; i--) {
			if (boardMatrix[i][position.getColumn()] == '0') {
				continue;
			}
			else if (boardMatrix[i][position.getColumn()] != '0') {
				// if first figure on the way found go further
				if (!figurBefore) {
					figurBefore = true;
					continue;
				}
				else {
					// if next figure after that cannon, then check
					if (boardMatrix[i][position.getColumn()] == enemyCannon) {
						return true;
					}
					else {
						break;
					}
				}
			}
		}*/
		boolean left = cannonLeft(boardMatrix, isRed, enemyCannon);
		
		// vertically backward
		/*figurBefore = false;
		for (int i = position.getColumn() + 1; i < boardMatrix[0].length; i++) {
			if (boardMatrix[position.getRow()][i] == '0') {
				continue;
			}
			else if (boardMatrix[position.getRow()][i] != '0') {
				// if first figure on the way found go further
				if (!figurBefore) {
					figurBefore = true;
					continue;
				}
				else {
					// if next figure after that cannon, then check
					if (boardMatrix[position.getRow()][i] == enemyCannon) {
						return true;
					}
					else {
						break;
					}
				}
			}
		}*/
		boolean back = cannonBack(boardMatrix, isRed, enemyCannon);
		
		/*figurBefore = false;
		// vertically forward
		for (int i = position.getColumn() - 1; i >= 0; i--) {
			if (boardMatrix[position.getRow()][i] == '0') {
				continue;
			}
			else if (boardMatrix[position.getRow()][i] != '0') {
				// if first figure on the way found go further
				if (!figurBefore) {
					figurBefore = true;
					continue;
				}
				else {
					// if next figure after that cannon, then check
					if (boardMatrix[position.getRow()][i] == enemyCannon) {
						return true;
					}
					else {
						break;
					}
				}
			}
		}*/
		boolean forward = cannonForward(boardMatrix, isRed, enemyCannon);
		if( right || left || back || forward ) {
			return true;
		}
		
		return false;
	}
	
	public boolean isCheckHorse(char[][] boardMatrix, boolean isRed) {
		
		char enemyHorse;
		
		if(isRed) {
			enemyHorse = 'h';
		}
		else {
			enemyHorse = 'H';
		}
		
		// current row/column of general:
		int row = position.getRow();
		int col = position.getColumn();
		
		
		/*
		 * inverted HorseMove: first one step diagonal, then one step up/down/left/right
		 * (Horse can't jump)
		 */
		
		
		int[][] diagoArray = {{row+1,col+1},{row-1,col-1},{row+1,col-1},{row-1,col+1}};
		int[][] movesArray = {{row+2,col+1},{row+1,col+2},{row-2,col-1},{row-1,col-2},{row+1,col-2},{row+2,col-1},{row-1,col+2},{row-2,col+1}};

		// iterates over diagoArray:
		for(int i = 0; i < 4; i++) {
			
			// checks whether "diagonal" position is on board:
			if(new Position(diagoArray[i][0], diagoArray[i][1]).onBoard()) {
				
				// horse can't jump:
				if(boardMatrix[diagoArray[i][0]][diagoArray[i][1]] == '0') {
					
					// iterates over possible moves for each direction:		
					for(int j = 0; j < 2; j++) {
						
						// new position:
						Position newPos = new Position(movesArray[2*i+j][0],movesArray[2*i+j][1]);
						
						// checks whether position is on board:
						if(newPos.onBoard()) {
							
							// checks whether field is occupied by enemyHorse:
							if(boardMatrix[movesArray[2*i+j][0]][movesArray[2*i+j][1]] == enemyHorse) {
								return true;
							}	
						}
					}	
				}
			}
		}	
		return false;	
	}
	
	public boolean isCheckSoldier(char[][] boardMatrix, boolean isRed) {
		char enemySoldier;
		int stepRow;
		if(isRed) {
			enemySoldier = 's';
			stepRow = -1;
		}
		else {
			enemySoldier = 'S';
			stepRow = 1;
		}
		
		char left = boardMatrix[position.getRow()][position.getColumn() - 1];
		char right = boardMatrix[position.getRow()][position.getColumn() + 1];
		char step = boardMatrix[position.getRow() + stepRow][position.getColumn()];
		if(left == enemySoldier || right == enemySoldier || step == enemySoldier) {
			return true;
		}
		return false;
	}
}
