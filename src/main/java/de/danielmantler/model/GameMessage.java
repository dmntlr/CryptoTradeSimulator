package de.danielmantler.model;

public class GameMessage {
	
	public Type MESSAGE_TYPE = Type.GAME_MESSAGE;
	private String cryptoId;
	private double cryptoPrice;
	private double balance;
	
	public GameMessage(String cryptoId, double cryptoPrice) {
		super();
		this.cryptoId = cryptoId;
		this.cryptoPrice = cryptoPrice;
	}

	public String getCryptoId() {
		return cryptoId;
	}

	public void setCryptoId(String cryptoId) {
		this.cryptoId = cryptoId;
	}

	public double getCryptoPrice() {
		return cryptoPrice;
	}

	public void setCryptoPrice(double cryptoPrice) {
		this.cryptoPrice = cryptoPrice;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public double getBalance() {
		return this.balance;
	}
}
