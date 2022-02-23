package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.io.Serializable;
import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class General implements Figur,Serializable {
	private static final long serialVersionUID = 5424778147226994452L;
	
	//attributes
	private Position position;
	
	public General(Position pos) {			
		this.position = pos;
	}
	
	public void setPosition(Position pos) {
		this.position = pos;
	}
	public Position getPosition() {
		return position;
	}
	
	public boolean checkDiagonal(Position position, int row, int col) { 
		if(((Math.abs(position.getRow() - row) == 1) && (Math.abs(position.getColumn() - col) == 1))) {
			return true;
		}
		return false;
	}
	public boolean checkOneStep(Position position, int row, int col) {
		if(((Math.abs(position.getRow() - row) < 2) && (Math.abs(position.getColumn() - col) < 2))) {
			return false;
		}
		return true;
	}
	public boolean checkOneStepAndDiagonal(Position position,int row, int col ) {
		if(checkDiagonal(position, row, col) || checkOneStep(position, row, col )) {
			return true;
		}
		return false;
	}
	public boolean samePosition(int row, int col) {
		if(row == position.getRow() && col == position.getColumn()) {
			return true;
		}
		return false;
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
				if(samePosition(row, col)) {		// check if we are trying to move to actual position of figure
					continue;
				}
				if(reihe > 2 && String.valueOf(board.getBoardMatrix()[row][col]).matches("[GAEHRCS]")) {continue;}	//means we are playing red, else black
				else if(String.valueOf(board.getBoardMatrix()[row][col]).matches("[gaehrcs]")) { continue;}			// keine diagonale Züge  no todesblick, then free to go
				
				String moveString = Position.positionToString(position) + '-' + Position.positionToString(new Position(row, col));
				Move move = new Move(moveString, board.getBoardState(), player);
				if (checkOneStepAndDiagonal(position, row, col)) {
					continue;
				}
				moves.add(move);			
			}
		}
		return moves;
	}

	
	
	public boolean isThreatened(Board board, Move move) {
		
		// copies board:
		Board boardBuf = new Board(board.getBoardState());
		
		Position start = Position.stringToPosition(move.getMove().split("-")[0]);	
		Position goal = Position.stringToPosition(move.getMove().split("-")[1]);
		
		// does move:
		boardBuf.getBoardMatrix()[goal.getRow()][goal.getColumn()] = boardBuf.getBoardMatrix()[start.getRow()][start.getColumn()]; 
		boardBuf.getBoardMatrix()[start.getRow()][start.getColumn()] = '0';
		
		// updates boardState of boardBuf by initializing a new board (-> to update positions):
		boardBuf = new Board(boardBuf.boardMatrixToBoardString());
		
		General blackGeneral = boardBuf.getBlackGeneral();
		General redGeneral = boardBuf.getRedGeneral();
		
		
		// checks whether generals in same column:
		if(redGeneral.getPosition().getColumn() != blackGeneral.getPosition().getColumn()) {
			return false;	
		}
		
		// generals in same column:
		else {
			// iterates over positions between generals (from blackGeneral down to redGeneral):
			for(int i = blackGeneral.getPosition().getRow() + 1; i < redGeneral.getPosition().getRow(); i++) {					
				
				// continues to iterate if position is empty:
				if(boardBuf.getBoardMatrix()[i][blackGeneral.getPosition().getColumn()] == '0') {	
					continue;
				}
				
				// returns false, if other playing-piece on a position between blackGeneral and redGenerl:
				else if((boardBuf.getBoardMatrix()[i][this.getPosition().getColumn()] != '0') && (boardBuf.getBoardMatrix()[i][this.getPosition().getColumn()] != 'G')) {
					return false;
				}																			
			}	
		}
		
		// returns true, if for-loop iterates over all positions between blackGeneral and redGenerl without finding other playing-piece:
		return true;
	}
	

	
	public boolean isCheck(Board board, Move move) {
		
		// copies board:
		Board boardBuf = new Board(board.getBoardState());
		
		// checks if redGeneral or blackGeneral:
		boolean isRed = position.isRed(board);
		
		Position start = Position.stringToPosition(move.getMove().split("-")[0]);	
		Position goal = Position.stringToPosition(move.getMove().split("-")[1]);
		
		// executes move:
		boardBuf.getBoardMatrix()[goal.getRow()][goal.getColumn()] = boardBuf.getBoardMatrix()[start.getRow()][start.getColumn()]; 
		boardBuf.getBoardMatrix()[start.getRow()][start.getColumn()] = '0';
		
		// checks, if we try to move general:
		if((start.getRow() == this.getPosition().getRow()) && (start.getColumn() == this.getPosition().getColumn())) {
			
			// update position of red/black buffer general:
			General bufGeneral = boardBuf.getBlackGeneral();
			
			if(position.isRed(board)) {
				bufGeneral = boardBuf.getRedGeneral();
			}
			
			// updates position:
			bufGeneral.setPosition(goal);
			
			// calls isCheck-methods with bufGeneral:
			if (bufGeneral.isCheckRook(boardBuf.getBoardMatrix(), isRed) || bufGeneral.isCheckCannon(boardBuf.getBoardMatrix(), isRed) || bufGeneral.isCheckHorse(boardBuf.getBoardMatrix(), isRed) || bufGeneral.isCheckSoldier(boardBuf.getBoardMatrix(), isRed)) {
				return true;
			}
		}
		
		// calls isCheck-methods with "this" general (because we didn't move general):
		else {
			if (isCheckRook(boardBuf.getBoardMatrix(), isRed) || isCheckCannon(boardBuf.getBoardMatrix(), isRed) || isCheckHorse(boardBuf.getBoardMatrix(), isRed) || isCheckSoldier(boardBuf.getBoardMatrix(), isRed)) {
				return true;
			}
		}
	
		return false;
	}
	
	// extends isCheckRook:
	public boolean checkRookRight(char[][] boardMatrix, boolean isRed, char enemyRook) {

		for (int i = position.getColumn() + 1; i < boardMatrix[0].length; i++) {
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
		return false;
	}
	
	// extends isCheckRook:
	public boolean checkRookLeft(char[][] boardMatrix, boolean isRed, char enemyRook) {
		
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
		return false;
	}
	
	// extends isCheckRook:
	public boolean checkRookBack(char[][] boardMatrix, boolean isRed, char enemyRook) {
		
		for (int i = position.getRow() + 1; i < boardMatrix.length; i++) {
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
	
	// extends isCheckRook:
	public boolean checkRookForward(char[][] boardMatrix, boolean isRed, char enemyRook) {
		
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
	
	public boolean isCheckRook(char[][] boardMatrix, boolean isRed) {
		char enemyRook;
		if (isRed) {
			enemyRook = 'r';
		}
		else {
			enemyRook = 'R';
		}
		
		boolean right = checkRookRight(boardMatrix, isRed, enemyRook);
		boolean left = checkRookLeft(boardMatrix, isRed, enemyRook);
		boolean back = checkRookBack(boardMatrix, isRed, enemyRook);
		boolean forward = checkRookForward(boardMatrix, isRed, enemyRook);
		
		if(right || left || back || forward) {
			return true;
		}
		
		return false;
	}
	
	// extends isCheckCannon:
	public boolean cannonForward(char[][] boardMatrix, boolean isRed, char enemyCannon) {
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
	
	// extends isCheckCannon:
	public boolean cannonBack(char[][] boardMatrix, boolean isRed, char enemyCannon) {
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
	
	// extends isCheckCannon:
	public boolean cannonRight(char[][] boardMatrix, boolean isRed, char enemyCannon) {
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
	
	// extends isCheckCannon:
	public boolean cannonLeft(char[][] boardMatrix, boolean isRed, char enemyCannon) {
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
		
		
		boolean right = cannonRight(boardMatrix, isRed, enemyCannon);
		
		
		boolean left = cannonLeft(boardMatrix, isRed, enemyCannon);
		
		
		boolean back = cannonBack(boardMatrix, isRed, enemyCannon);
		
		boolean forward = cannonForward(boardMatrix, isRed, enemyCannon);
		
		if( right || left || back || forward ) {
			return true;
		}
		
		return false;
	}
	
	
	// extends isCheckHorse:
	public boolean checkDiagonalAndJump(int [][] diagoArray, int[][] movesArray, char[][] boardMatrix, int i) {
		
		// checks whether "diagonal" position is on board:
		if(new Position(diagoArray[i][0], diagoArray[i][1]).onBoard()) {
				
			// horse can't jump:
			if(boardMatrix[diagoArray[i][0]][diagoArray[i][1]] == '0') {
				return true;
			}
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
			
			// checks whether "diagonal" position is on board and horse can't jump:
			if(checkDiagonalAndJump(diagoArray, movesArray,  boardMatrix, i)) {
					
				// iterates over possible moves for each direction:		
				for(int j = 0; j < 2; j++) {
					
					// new position:
					Position newPos = new Position(movesArray[2*i+j][0],movesArray[2*i+j][1]);
					
					// checks whether position is on board:
					if(newPos.onBoard() && boardMatrix[movesArray[2*i+j][0]][movesArray[2*i+j][1]] == enemyHorse) {
						return true;	
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
