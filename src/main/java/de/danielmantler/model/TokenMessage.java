package de.danielmantler.model;

public class TokenMessage {
	
	public Type MESSAGE_TYPE = Type.GAME_MESSAGE;
	private String token;
	
	public TokenMessage() {

	}

	
	public TokenMessage(String token) {
		super();
		this.token = token;
	}


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	
}
