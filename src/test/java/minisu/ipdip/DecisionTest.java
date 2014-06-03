package minisu.ipdip;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.fest.assertions.api.Assertions.assertThat;

public class DecisionTest
{
	@Test
	public void shouldDeserialize() throws Exception
	{
		ObjectMapper mapper = new ObjectMapper();
		Decision decision = mapper.readValue( fixture( "decision.json" ), Decision.class);

		assertThat( decision.getId() ).isNotEmpty();
	}
}