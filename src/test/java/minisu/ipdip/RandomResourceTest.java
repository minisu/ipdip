package minisu.ipdip;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.net.URI;

import static org.fest.assertions.api.Assertions.assertThat;

public class RandomResourceTest
{
	private final Decision inputDecision = new Decision( "Should we party?", ImmutableList.of( "Yes", "No" ) );

	@Test
	public void createAndFetch()
	{
		RandomResource resource = new RandomResource( new InMemoryStorage() );

		Response response = resource.createDecision( inputDecision );
		URI decisionLocation = ( URI )response.getMetadata().getFirst( "Location" );

		Decision outputDecision = resource.getDecision( decisionLocation.toString() ).get();

		assertThat( outputDecision ).isEqualTo( inputDecision );
	}
}