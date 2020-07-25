package de.danielmantler.model;

import javax.websocket.Session;

public class User {
	
	final static double STARTING_BALANCE = 100.00;
	private String username;
	private int score;
	private transient Session session;
	private int roomID;
	private double balance;
	
	public User() {
		this.balance = STARTING_BALANCE;	
	}

	public User(String username) {
		super();
		this.username = username;
		this.balance = STARTING_BALANCE;

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public int getRoomID() {
		return roomID;
	}

	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}
	
	public double getBalance() {
		return this.balance;
	}
	


	
	
}
