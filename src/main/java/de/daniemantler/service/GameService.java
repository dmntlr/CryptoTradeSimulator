package de.daniemantler.service;


import de.danielmantler.model.GameMessage;
import de.danielmantler.model.GameRoom;
import de.danielmantler.model.User;

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
		for (User user : room.getUsers()) {
			message.setBalance(user.getBalance());
			try {
				user.getSession().getBasicRemote().sendObject(message);
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
