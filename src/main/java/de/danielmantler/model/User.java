package de.danielmantler.model;

import javax.websocket.Session;

public class User {
	
	final static Double STARTING_BALANCE = 100.00;
	private String username;
	private transient Session session;
	private Integer roomID;
	private Double balance;
	private Double amount;
	private boolean isTransacted;
	
	public User() {
		this.balance = STARTING_BALANCE;
		this.amount = 0.0;
	}

	public User(String username) {
		super();
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Integer getRoomID() {
		return roomID;
	}

	public void setRoomID(Integer roomID) {
		this.roomID = roomID;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public boolean isTransacted() {
		return isTransacted;
	}

	public void setTransacted(boolean isTransacted) {
		this.isTransacted = isTransacted;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}



	
	
}
