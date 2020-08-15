package de.danielmantler.service;

import java.util.ArrayList;

import de.danielmantler.model.CryptoPrices;
import de.danielmantler.model.GameRoom;
import de.danielmantler.model.TokenMessage;
import de.danielmantler.model.User;
import de.danielmantler.security.GenerateToken;

public class GameService implements Runnable {

	private GameRoom room;

	private static int TIME_TO_NEW_MESSAGE = 2 * 60 * 1000;
	private static final int GAME_DAYS = 5;
	int priceListIndex = 0;
	private CryptoPrices cryptoPrices;

	public GameService(GameRoom room) {
		this.room = room;
		this.cryptoPrices = CryptocurrencyService.getCurrentPrice(room.getCrypto(), GAME_DAYS);
	}

	public GameRoom getRoom() {
		return room;
	}

	public void setRoom(GameRoom room) {
		this.room = room;
	}

	public void broadcastToGameRoom() {

		User[] users = room.getUsers();

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
	}

	public boolean isTransacted() {
		for(User user : room.getUsers()) {
			if (user.isTransacted() == false) {
				return false;
			}
		}
		return true;
	}

	public void resetTransacted() {
		for(User user: room.getUsers()) {
			user.setTransacted(false);
		}
	}
	
	public void finishGame() {
		//TODO-> Implement
	}
	
	public Double getCurrentPrice() {
		ArrayList<ArrayList<Number>> pricesList = cryptoPrices.getPrices();
		Double price = pricesList.get(priceListIndex).get(1).doubleValue();
		priceListIndex += (pricesList.size() / GAME_DAYS) - 1;
		if(priceListIndex > pricesList.size()) {
			finishGame();
		}
		return price;
	}

	@Override
	public void run() {
		broadcastToGameRoom();
		while (true) {
			try {
				Thread.sleep(TIME_TO_NEW_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
