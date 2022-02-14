package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import de.tuberlin.sese.swtpp.gameserver.model.Move;

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
	
	public static void main(String[] args) {
		System.out.print(positionToString(new Position(9, 0)));
	}
	
	public boolean isRed(Board board) {
		char Fig = board.getBoardMatrix()[this.getRow()][this.getColumn()];	
		if ("GAEHRCS".indexOf(Fig) != -1) return true;
		return false;
	}
	
	public String moveString(Position newPosition) {
		
		// returns oldColum oldRow - newColumn newRow
		String stringRow = "9876543210";
		String stringCol = "abcdefghi";
		
		int x1 = this.getRow();
		int y1 = this.getColumn();
		
		int x2 = newPosition.getRow();
		int y2 = newPosition.getColumn();
		
		return stringCol.charAt(y1) + stringRow.charAt(x1) + "-" + stringCol.charAt(y2) + stringRow.charAt(x2);
	}
	

}