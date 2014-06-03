package minisu.ipdip;

import com.google.common.base.Optional;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


import java.net.URI;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path( "decisions/" )
@Produces( APPLICATION_JSON )
@Consumes( APPLICATION_JSON )
public class RandomResource
{
	private final DecisionStorage storage;

	public RandomResource( DecisionStorage storage )
	{
		this.storage = storage;
	}

	@GET
	@Path( "{id}" )
	public Optional<Decision> getDecision( @PathParam( "id" )String id )
	{
		return storage.get( id );
	}

	@POST
	public Response createDecision( Decision decision )
	{
		storage.store( decision );
		return Response.created( URI.create( decision.getId() ) ).entity( decision ).build();
	}

	@PUT
	@Path( "{id}/decide" )
	public Response decide( @PathParam( "id" )String id )
	{
		Decision decision = storage.get( id ).get();
		decision.decide();
		return Response.created( URI.create( decision.getId() ) ).entity( decision ).build();
	}
}
