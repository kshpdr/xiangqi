package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.util.ArrayList;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

public class Elephant implements Figur {
	
	Position position;

	public Elephant(Position position) {
		this.position = position;
	}

	@Override
	public Position getPosition() {
		return this.position;
	}
	
	@Override
	public ArrayList<Move> getPossibleMoves(Position position, Board board, Player player) {

		// current row/column:
		int row = position.getRow();
		int col = position.getColumn();
		
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
		General myGeneral = board.getFriendGeneral(position);
		
		/*
		 * Elephant can move two steps diagonal
		 * (can not cross river between row 4 and 5):
		 */
		
		int[][] posArray = {{row+2,col+2},{row-2,col-2},{row+2,col-2},{row-2,col+2}};
		
		// iterates over possible positions in posArray:
		for(int i = 0; i < 4; i++) {
			
			// new position:
			Position newPos = new Position(posArray[i][0],posArray[i][1]);
			
			// checks whether position is on board:
			if(newPos.onBoard()) {
				
				// checks whether river is being crossed:
				if((row <= 4 && posArray[i][0] <= 4) || (row > 4 && posArray[i][0] > 4)) {
				
					// checks whether myGeneral is not threatened:
					if(!myGeneral.isThreatened(position.moveString(newPos))) {
						
						// checks whether position is free or occupied by other player:
						if(board.getBoardMatrix()[posArray[i][0]][posArray[i][1]] == '0') {
							possibleMoves.add(new Move(position.moveString(newPos), board.boardState, player));
						}
						else if(position.otherPlayerOnTargetField(board, newPos)) {
							possibleMoves.add(new Move(position.moveString(newPos), board.boardState, player));	
						}
					}
				}
			}
		}
		
		return possibleMoves;
	}
	
}
