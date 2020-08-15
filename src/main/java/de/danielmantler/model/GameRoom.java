package de.danielmantler.model;

import de.danielmantler.service.GameService;

public class GameRoom {

	private User[] users;
	private String crypto;
	private transient GameService gameService;

	public GameRoom(String crypto) {
		super();
		this.users = new User[2];
		this.crypto = crypto;
	}

	public User[] getUsers() {
		return users;
	}

	public void addUser(User user) throws Exception {
		for (int i = 0; i < users.length; i++) {
			if (users[i] == null) {
				users[i] = user;
				return;
			}
		}

		throw new Exception("Room is already full!");

	}

	public boolean containsUser(User user) {
		for (int i = 0; i < users.length; i++) {
			if (users[i] != null && users[i].getSession().getId().equals(user.getSession().getId())) {
				return true;
			}
		}
		return false;
	}

	public User getUser(String username) {
		for (int i = 0; i < users.length; i++) {
			if (users[i] != null && users[i].getUsername().equals(username)) {
				return users[i];
			}
		}
		return null;
	}

	public boolean kickPlayer(User toBeKickedUser) {
		for (int i = 0; i < users.length; i++) {
			if (users[i] == toBeKickedUser) {
				users[i] = null;
				return true;
			}
		}
		return false;
	}

	public int getUserCount() {
		int userCount = 0;
		for (User user : users) {
			if (user != null) {
				userCount++;
			}
		}

		return userCount;
	}

	public String getCrypto() {
		return this.crypto;
	}



	public GameService getGameService() {
		return gameService;
	}

	public void setGameService(GameService gameService) {
		this.gameService = gameService;
	}
	
	

}
