package minisu.ipdip;

import org.junit.Test;

import javax.ws.rs.core.Response;
import java.net.URI;

public class RandomResourceTest
{
	@Test
	public void createAndFetch()
	{
		RandomResource resource = new RandomResource( new InMemoryStorage() );

		Response response = resource.createDecision( new Decision() );
		URI decisionLocation = ( URI )response.getMetadata().getFirst( "Location" );

		Decision decision = resource.getDecision( decisionLocation.toString() ).get();
	}
}