package de.danielmantler.serialization;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import de.danielmantler.model.Transaction;
import de.danielmantler.model.User;

public class UserDecoder implements Decoder.Text<User> {

	Jsonb jsonb = JsonbBuilder.create();
	
	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public User decode(String s) throws DecodeException {
		return jsonb.fromJson(s,User.class);
		
} 	@Override 
	public boolean willDecode(String s) {
		return (s != null);
	}

}
