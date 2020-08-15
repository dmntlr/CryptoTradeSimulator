package de.danielmantler.serialization;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import de.danielmantler.model.EndgameMessage;


public class EndgameMessageEncoder implements Encoder.Text<EndgameMessage> {
	
	Jsonb jsonb = JsonbBuilder.create();

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(EndpointConfig arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String encode(EndgameMessage message) throws EncodeException {
        String json = jsonb.toJson(message); 
        return json;
	}

}
