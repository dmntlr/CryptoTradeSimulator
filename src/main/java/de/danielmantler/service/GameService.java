package de.danielmantler.service;


import de.danielmantler.model.GameMessage;
import de.danielmantler.model.GameRoom;
import de.danielmantler.model.User;
import de.danielmantler.security.GenerateToken;

public class GameService implements Runnable {

	private GameRoom room;

	public GameService(GameRoom room) {
		this.room = room;
	}

	public GameRoom getRoom() {
		return room;
	}

	public void setRoom(GameRoom room) {
		this.room = room;
	}

	public void broadcastToGameRoom(GameMessage message) {
		
		User[] users = room.getUsers();
		for (int i = 0; i < users.length; i++) {
			String token = GenerateToken.generateToken(users[i].getUsername(),users[i].getRoomID(), users[i].getBalance()); 
			message.setToken(token);
			message.setBalance(users[i].getBalance());
			try {
				users[i].getSession().getBasicRemote().sendObject(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		GameMessage initialMessage = new GameMessage(getRoom().getCrypto(),
				CryptocurrencyService.getCurrentPrice(getRoom().getCrypto()));
		broadcastToGameRoom(initialMessage);
		while (true) {
			try {
				Thread.sleep(60 * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
