package de.danielmantler.resources;

import java.security.Principal;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonNumber;
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
    public Double transaction(@Context SecurityContext ctx, @PathParam Double amount) {
    	Principal principal = ctx.getUserPrincipal();
    	System.out.println(principal.getName() + "Called transaction!");
    	JsonNumber roomNumber = jwt.getClaim("room");
    	GameRoom gameRoom = RoomService.rooms.get(roomNumber.intValue());
    	User user = null;
    	if(gameRoom != null) {
    		user = gameRoom.getUser(principal.getName());
    	} else {
    		return null;
    	}
  
    	if(user != null) {
    		System.out.println("User " + user.getUsername() + " asked for a transaction!");
    		//Calculate Price
    		JsonNumber price = jwt.getClaim("price");
    		Double cost = amount * price.doubleValue();
    		
    		//Check if Balance is correct
    		JsonNumber balance = jwt.getClaim("balance");
    		
    		if(balance.doubleValue() == user.getBalance() && (user.getBalance() - cost) >= 0) {
    			user.setBalance(user.getBalance() - cost);	
    			user.setTransacted(true);
    		}
    		
    		if(gameRoom.getGameService().isTransacted() == true) {
    			gameRoom.getGameService().broadcastToGameRoom();
    		}
    		
    	} else {
        	return null;
    	}
    	
    	return user.getBalance();
    }
}