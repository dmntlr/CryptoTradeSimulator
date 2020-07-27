package de.danielmantler;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import de.danielmantler.model.GameRoom;
import de.danielmantler.model.User;
import de.danielmantler.serialization.GameMessageEncoder;
import de.danielmantler.serialization.RoomEncoder;
import de.danielmantler.serialization.UserDecoder;
import de.daniemantler.service.RoomService;


@ServerEndpoint(value = "/rooms",	
				encoders = { RoomEncoder.class, GameMessageEncoder.class },
				decoders = UserDecoder.class)
public class RoomResource {
	private Session session;
	private static Set<RoomResource> endpoints 
			= new CopyOnWriteArraySet<>();;

	@OnOpen
	public void onOpen(Session session) {
		
		this.session = session;
		endpoints.add(this);
		System.out.println("Connected to Room Endpoint");

		RoomService.initialize();
		
		// Return Rooms
		broadcast(RoomService.rooms);

	}

	@OnClose
	public void onClose(Session session) {
		try {
			if(session != null) {
				endpoints.remove(this);
				RoomService.findRoomAndKick(session);
				session.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	@OnMessage
	public void onMessage(Session session, User user) throws Exception {
			user.setSession(session);
			System.out.println(RoomService.add(user));
			broadcast(RoomService.rooms);
		
	}
	
	@OnError
	public void onError(Throwable t) {
		System.out.println("onError::" + t.getMessage());
		t.printStackTrace();
	}
	
	
	private static void broadcast(List<GameRoom> rooms) {
    	System.out.println("Broadcasting!");
		endpoints.forEach(endpoint -> {
			synchronized (endpoint) {
                endpoint.session.getAsyncRemote().
				  sendObject(RoomService.rooms);

				
			}
		});
	}

}