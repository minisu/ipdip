package minisu.ipdip;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import minisu.ipdip.model.Decision;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.fest.assertions.api.Assertions.assertThat;

public class DecisionTest
{
	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

	@Test
	public void shouldDeserialize() throws Exception
	{
		Decision decision = MAPPER.readValue( fixture( "decision.json" ), Decision.class);

		assertThat( decision.getId() ).isNotEmpty();
	}

	@Test
	public void decisionsShouldBeFinal() throws Exception
	{
		Decision decision = DummyDecision.create();
		assertThat( decision.decide() ).isEqualTo( decision.decide() );
	}
}