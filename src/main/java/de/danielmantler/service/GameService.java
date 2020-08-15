package de.danielmantler.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

import de.danielmantler.model.CryptoPrices;
import de.danielmantler.model.EndgameMessage;
import de.danielmantler.model.GameRoom;
import de.danielmantler.model.TokenMessage;
import de.danielmantler.model.User;
import de.danielmantler.security.GenerateToken;

public class GameService implements Runnable {

	private GameRoom room;
	volatile boolean stop;
	private static int TIME_TO_NEW_MESSAGE = 2 * 60 * 1000;
	private static final int GAME_DAYS = 5;
	int priceListIndex = 0;
	private CryptoPrices cryptoPrices;
	private User[] users;

	public GameService(GameRoom room) {
		this.room = room;
		this.cryptoPrices = CryptocurrencyService.getCurrentPrice(room.getCrypto(), GAME_DAYS);
		this.users = room.getUsers();
	}

	public GameRoom getRoom() {
		return room;
	}

	public void setRoom(GameRoom room) {
		this.room = room;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public void broadcastGameUpdate() {
		Double price = getCurrentPrice();

		for (int i = 0; i < users.length; i++) {
			String token = GenerateToken.generateToken(room.getCrypto(), users[i].getUsername(), users[i].getRoomID(),
					users[i].getBalance(), price, users[i].getAmount());
			TokenMessage message = new TokenMessage(token);
			try {
				users[i].getSession().getBasicRemote().sendObject(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		resetTransacted();
		checkGameStatus();
	}

	public boolean isTransacted() {
		for (User user : room.getUsers()) {
			if (user.isTransacted() == false) {
				return false;
			}
		}
		return true;
	}

	// TODO-> Should return a ranking in the future and use stream for sending the message directly.
	public void broadcastWinning() {
		User winner = Arrays.stream(users).max(Comparator.comparingDouble(User::getBalance))
				.orElseThrow(NoSuchElementException::new);

		for (User user : users) {
			try {
				if (user == winner) {
					user.getSession().getBasicRemote().sendObject(new EndgameMessage(true));
				} else {
					user.getSession().getBasicRemote().sendObject(new EndgameMessage(false));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void resetTransacted() {
		for (User user : room.getUsers()) {
			user.setTransacted(false);
		}
	}

	public void checkGameStatus() {
		if (priceListIndex > cryptoPrices.getPrices().size()) {
			RoomService.finishGame(this);
		}
	}

	public Double getCurrentPrice() {
		ArrayList<ArrayList<Number>> pricesList = cryptoPrices.getPrices();
		Double price = pricesList.get(priceListIndex).get(1).doubleValue();
		priceListIndex += (pricesList.size() / GAME_DAYS) - 1;
		return price;
	}

	@Override
	public void run() {
		broadcastGameUpdate();
		while (!stop) {
			try {
				Thread.sleep(TIME_TO_NEW_MESSAGE);
				broadcastGameUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
