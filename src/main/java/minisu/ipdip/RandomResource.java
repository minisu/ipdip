package minisu.ipdip;

import com.google.common.base.Optional;

import javax.ws.rs.*;


import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path( "decisions/" )
@Produces( APPLICATION_JSON )
public class RandomResource
{
	private final DecisionStorage storage;

	public RandomResource( DecisionStorage storage )
	{
		this.storage = storage;
	}

	@GET
	@Path( "{id}" )
	public Optional<Decision> abc( @PathParam( "id" )String id )
	{
		return storage.get( id );
	}

	@POST
	public String abc2( Decision decision )
	{
		throw new UnsupportedOperationException(  );
	}
}
