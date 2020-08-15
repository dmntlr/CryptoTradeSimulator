package de.danielmantler.model;

public class EndgameMessage {
	
	boolean isWinner;
	public Type MESSAGE_TYPE = Type.ENDGAME_MESSAGE;
	
	public EndgameMessage() {
		
	}
	
	public EndgameMessage(boolean isWinner) {
		super();
		this.isWinner = isWinner;
	}
	
	public boolean isWinner() {
		return isWinner;
	}
	public void setWinner(boolean isWinner) {
		this.isWinner = isWinner;
	}

	
	
	
}
