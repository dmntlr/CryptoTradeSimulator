package de.danielmantler;

import java.security.Principal;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonNumber;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import de.danielmantler.model.GameRoom;
import de.danielmantler.model.User;
import de.danielmantler.service.RoomService;



@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class TransactionResource {

	@Inject
	JsonWebToken jwt;
	
	
    @GET
    @Path("/{amount}")
    public String transaction(@Context SecurityContext ctx, @PathParam Double amount) {
    	Principal caller = ctx.getUserPrincipal();
    	JsonNumber roomNumber = jwt.getClaim("room");
    	GameRoom gameRoom = RoomService.rooms.get(roomNumber.intValue());
    	User user = null;
    	if(gameRoom != null) {
    		user = gameRoom.getUser(caller.getName());
    	} else {
    		return "Specified GameRoom not found";
    	}
  
    	if(user != null) {
    		//Calculate Price
    		JsonNumber price = jwt.getClaim("price");
    		Double cost = amount * price.doubleValue();
    		
    		//Check if Balance is correct
    		JsonNumber balance = jwt.getClaim("balance");
    		
    		if(balance.doubleValue() == user.getBalance() &&
    				(user.getBalance() - cost) >= 0) {
    			user.setBalance(user.getBalance() - cost);
    		}
    		
    	} else {
        	return "Specified User not found";
    	}
    	return "NEW TOKEN?";
    }
}