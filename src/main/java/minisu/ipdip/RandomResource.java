package minisu.ipdip;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import minisu.ipdip.model.Decision;
import minisu.ipdip.storage.DecisionStorage;
import minisu.ipdip.views.DecisionView;

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
	public Optional<DecisionView> getDecision( @PathParam( "id" )String id )
	{
		return Optional.of( new Decision( "Should we party?", ImmutableList.of( "Yes", "No" ) ) ).transform( DecisionView::new );
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
