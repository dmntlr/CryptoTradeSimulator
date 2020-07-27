package de.danielmantler;

import java.security.Principal;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.websocket.server.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/transaction")
@RequestScoped
public class TransactionResource {

	@Inject
	JsonWebToken jwt;
	
	
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({"gamer"})
    public String transaction(@Context SecurityContext ctx) {
    	Principal caller = ctx.getUserPrincipal();
        String name = caller == null ? "anonymous" : caller.getName();
    	boolean hasJWT = jwt.getClaimNames() != null;
        String helloReply = String.format("hello + %s, isSecure: %s, authScheme: %s, hasJWT: %s",
        		name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJWT);
    	//Does User exist?
    	//Does User exits in Room?
    	//Enough Money?
        return helloReply;
    }
}