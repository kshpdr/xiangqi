package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.io.Serializable;

import de.tuberlin.sese.swtpp.gameserver.model.Move;

public class Position implements Serializable{
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
		int row = 9 - (positionString.charAt(1) - '0');
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
		char CharOfFigur = board.getBoardMatrix()[this.row ][this.column];	
		if ("GAEHRCS".indexOf(CharOfFigur) != -1) return true;
		return false;
	}
	
	public boolean onBoard() {
		
		if(this.row >= 0 && this.row < 10 && this.column >= 0 && this.column < 9) {
			return true;
		}
		return false;
	}
	
	public boolean otherPlayerOnTargetField(Board board, Position newPosition) {
		
		if((this.isRed(board) && !newPosition.isRed(board)) || (!this.isRed(board) && newPosition.isRed(board))) {
			return true;
		}
		return false;
	}
	
	public String moveString(Position newPosition) {
		
		return positionToString(this) + "-" + positionToString(newPosition); 
	}
	
	public static void main(String[] args) {
		System.out.print(stringToPosition("e0").getRow());
		//System.out.print(stringToPosition("e9").getColumn());
	}

}