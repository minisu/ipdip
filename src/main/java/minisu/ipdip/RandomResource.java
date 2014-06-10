package minisu.ipdip;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import io.dropwizard.views.View;
import minisu.ipdip.model.Decision;
import minisu.ipdip.storage.DecisionStorage;
import minisu.ipdip.views.DecisionView;
import minisu.ipdip.websockets.Broadcaster;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_HTML;

@Path( "decisions/" )
@Produces( TEXT_HTML)
@Consumes( APPLICATION_JSON )
public class RandomResource
{
	private final DecisionStorage storage;
	private final Broadcaster broadcaster;

	public RandomResource( DecisionStorage storage, Broadcaster broadcaster )
	{
		this.storage = storage;
		this.broadcaster = broadcaster;
	}

	@GET
	@Path( "dummy" )
	public Optional<DecisionView> getDummyDecision( @Context HttpServletRequest request, @PathParam( "id" )String id )
	{
		String userId = request.getRemoteHost() + " " + request.getHeader( "User-Agent" );
		return Optional.of( new Decision( "Should we party?", ImmutableList.of( "Yes", "No" ) ) )
				.transform( d -> d.wasSeenBy( userId ) )
				.transform( DecisionView::new );
	}

	@GET
	public View index()
	{
		return new View("index.ftl") {};
	}

	@GET
	@Path( "{id}" )
	public Optional<DecisionView> getDecision( @Context HttpServletRequest request, @PathParam( "id" )String id )
	{
		String userId = request.getRemoteHost() + " " + request.getHeader( "User-Agent" );
		broadcaster.broadcast( id, userId );
		return storage.get( id )
				.transform( d -> d.wasSeenBy( userId ) )
				.transform( DecisionView::new );
	}

	@POST
	public Response createDecision( Decision decision )
	{
		storage.store( decision );
		return Response.created( URI.create( decision.getId() ) ).entity( decision ).build();
	}

	@POST
	@Consumes( MediaType.APPLICATION_FORM_URLENCODED )
	public Response createDecisionFromForm( MultivaluedMap<String, String> formParams )
	{
		String name = formParams.getFirst( "name" );
		List<String> alternatives = formParams.get( "alternative" );

		Decision decision = new Decision( name, alternatives );
		storage.store( decision );
		return Response
				.seeOther( URI.create( "decisions/" + decision.getId() ) )
				.entity( new DecisionView( decision ) )
				.build();
	}

	@PUT
	@Path( "{id}/decide" )
	public Response decide( @PathParam( "id" )String id )
	{
		Decision decision = storage.get( id ).get();
		decision.decide();
		return Response
				.created( URI.create( decision.getId() ) )
				.entity( decision ).build();
	}
}
