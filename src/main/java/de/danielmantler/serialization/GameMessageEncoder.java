package de.danielmantler.serialization;


import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.config.PropertyVisibilityStrategy;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import de.danielmantler.model.TokenMessage;

public class GameMessageEncoder implements Encoder.Text<TokenMessage> {
	
	Jsonb jsonb = JsonbBuilder.create();

    @Override
    public String encode(TokenMessage message) throws EncodeException {
    	//Add Static class fields to serialziation
        String json = jsonb.toJson(message); 
        return json;
    }

	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}