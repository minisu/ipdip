package minisu.ipdip;

import com.google.common.base.Optional;
import io.dropwizard.auth.Auth;
import io.dropwizard.views.View;
import minisu.ipdip.auth.AnonymousUser;
import minisu.ipdip.auth.User;
import minisu.ipdip.model.Decision;
import minisu.ipdip.sse.Event;
import minisu.ipdip.storage.DecisionStorage;
import minisu.ipdip.views.DecisionView;
import minisu.ipdip.sse.Broadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.MediaType.TEXT_HTML;

@Path( "/" )
@Produces( TEXT_HTML)
@Consumes( MediaType.APPLICATION_FORM_URLENCODED )
public class RandomResource
{
	static Logger log = LoggerFactory.getLogger( RandomResource.class );

	private final DecisionStorage storage;
	private final Broadcaster broadcaster;

	public RandomResource( DecisionStorage storage, Broadcaster broadcaster )
	{
		this.storage = storage;
		this.broadcaster = broadcaster;
	}

	@GET
	public View index()
	{
		return new View("index.mustache") {};
	}

	@GET
	@Path( "decisions/{id}" )
	public Optional<DecisionView> getDecision( @Context HttpServletRequest request, @PathParam( "id" )String id ) throws IOException {
        return storage.get(id).transform(DecisionView::new);
	}

	@POST
    @Path( "decisions" )
	@Consumes( MediaType.APPLICATION_JSON )
	public Response createDecision( Decision decision ) throws IOException {
		storage.store( decision );
		return Response.created( URI.create( "decisions/" + decision.getId() ) ).entity( decision ).build();
	}

	@POST
    @Path( "decisions" )
	public Response createDecisionFromForm( MultivaluedMap<String, String> formParams ) throws IOException {
		String name = formParams.getFirst( "name" );
		List<String> alternatives = formParams.get( "alternative" );

		Decision decision = new Decision( name, alternatives );
		storage.store( decision );
		return Response
				.seeOther( URI.create( "decisions/" + decision.getId() ) )
				.entity( new DecisionView( decision ) )
				.build();
	}

	@POST
	@Path( "decisions/{id}/decide" )
	public Response decide( @PathParam( "id" )String id ) throws IOException {
		Decision decision = storage.get( id ).get();
		decision.decide();
        broadcaster.broadcast( id, Event.decisionMade(decision.getDecidedAlternative().get()) );
		return Response
				.ok( URI.create( decision.getId() ) )
				.entity( new DecisionView( decision ) ).build();
	}
}
