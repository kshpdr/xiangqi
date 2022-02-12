package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

public class Figure {
	//attributes
	private String position;
	private Boolean inGame;
	
	//constructor
	public Figure(String position) {
		this.position = position;
		this.inGame = true;
	}
	public Figure() {
		this.position = null;
		this.inGame = true;
	}
	
	//functions
	public void figDefeated() {
		this.inGame = false;
	}
	public String getPosition(){
		return this.position;
	}
	public void setPosition(String pos) {
		this.position = pos;
	}
	public Boolean figureInGame() {
		return inGame;
	}
	
}
