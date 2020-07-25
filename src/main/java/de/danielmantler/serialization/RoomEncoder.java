package de.danielmantler.serialization;

import java.util.List;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import de.danielmantler.model.GameRoom;


public class RoomEncoder implements Encoder.Text<List<GameRoom>> {

	Jsonb jsonb = JsonbBuilder.create();
	
    @Override
    public String encode(List<GameRoom> message) throws EncodeException {
        String json = jsonb.toJson(message);
    	return json;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Custom initialization logic
    }

    @Override
    public void destroy() {
        // Close resources
    }
}
