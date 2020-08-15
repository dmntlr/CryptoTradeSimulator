package de.danielmantler.service;


import de.danielmantler.model.GameRoom;
import de.danielmantler.model.TokenMessage;
import de.danielmantler.model.User;
import de.danielmantler.security.GenerateToken;

public class GameService implements Runnable {

	private GameRoom room;
	private static int TIME_TO_NEW_MESSAGE = 2 * 60 * 1000;

	public GameService(GameRoom room) {
		this.room = room;
	}

	public GameRoom getRoom() {
		return room;
	}

	public void setRoom(GameRoom room) {
		this.room = room;
	}

	public void broadcastToGameRoom() {
		
		User[] users = room.getUsers();
		for (int i = 0; i < users.length; i++) {
			String token = GenerateToken.generateToken(
					room.getCrypto(),users[i].getUsername(),users[i].getRoomID(), users[i].getBalance(),CryptocurrencyService.getCurrentPrice(room.getCrypto()),TIME_TO_NEW_MESSAGE); 
			TokenMessage message = new TokenMessage(token);
			try {
				users[i].getSession().getBasicRemote().sendObject(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isTransacted() {
		for (User user : room.getUsers()) {
			if (user.isTransacted() == false) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				broadcastToGameRoom();	
				Thread.sleep(TIME_TO_NEW_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
