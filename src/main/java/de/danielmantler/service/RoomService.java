package de.danielmantler.service;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.Session;

import de.danielmantler.model.GameRoom;
import de.danielmantler.model.User;

public class RoomService implements Runnable {
    
	final static int INITIAL_ROOM_COUNT = 5;
	
	
    private static RoomService instance;
    public static List<GameRoom> rooms;
    private RoomService() {}
    
    public static String add(User user) throws Exception {
    	GameRoom gameRoom = rooms.get(user.getRoomID());
    	if(gameRoom.containsUser(user)) {
    		System.out.println("Room Already full!");
    	} else {
        	gameRoom.addUser(user);
        	if(gameRoom.getUserCount()
        			== gameRoom.getUsers().length) {
        		GameService gameService = new GameService(gameRoom);
        		new Thread(gameService).start();
        	}
    	}
    	return "Connected user + " + user.getUsername() + " to room " + user.getRoomID();
    }
    
    public static void initialize() {
        if (instance == null) {
            instance = new RoomService();
            rooms = new ArrayList<>();
            try {
                for(int i = 0; i < INITIAL_ROOM_COUNT; i++) {
                	rooms.add(new GameRoom(CryptocurrencyService.CRYPTO_IDS[i]));
                }
            } catch(Exception e) {
            	e.printStackTrace();
            }
            //Create 5 empty rooms
            new Thread(instance).start();
        }
    }

    public static boolean findRoomAndKick(Session session) {
    	//TODO -> Get room directly be session
    		for(GameRoom room : rooms) {
    			for(User user : room.getUsers()) {
    				if(user.getSession() == session) {
    					room.kickPlayer(user);
    					return true;
    				}
    			}
    		}
    		return false;
    }
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
