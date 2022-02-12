package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

public class Advisor extends Figure {
	public static boolean isMoveOk(String move) {
		String[] moveArray = move.split("-");		//Ascii for 'a' = 97
		//get a numeric position of start and goal position
		int startC = moveArray[0].charAt(0) - 97;			//char of start
		int startI = moveArray[0].charAt(1);				//int of start
		
		int goalC = moveArray[1].charAt(0) - 97;			//char of start
		int goalI = moveArray[1].charAt(1);					//int of start
		
		
		return false;
	}
}
