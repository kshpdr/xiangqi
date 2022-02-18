package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

public class Position {
	private int row;
	private int column;
	
	public Position(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public void setColumn(int column) {
		this.column = column;
	}
	
	public static Position stringToPosition(String positionString) {
		int row = positionString.charAt(1) - '0';
		int column = positionString.charAt(0) - 97;
		return new Position(row, column);
	}
	
	public static String positionToString(Position position) {
		String positionString = "";
		positionString += (char)(position.getColumn() + 97);
		positionString += 9 - position.getRow();
		return positionString;
	}
	
	public boolean isRed(Board board) {
		char fig = board.getCurrentBoard()[this.row][this.column];	
		if ("GAEHRCS".indexOf(fig) != -1) return true;
		return false;
	}
	
	public boolean onBoard() {
		if(this.row >= 0 && this.row < 10 && this.column >= 0 && this.column < 9) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.print(positionToString(new Position(9, 0)));
	}

}