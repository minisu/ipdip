package minisu.ipdip;

import minisu.ipdip.auth.AnonymousUser;
import minisu.ipdip.model.Decision;
import minisu.ipdip.storage.InMemoryStorage;
import minisu.ipdip.sse.BroadcastingCentral;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.net.URI;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RandomResourceTest
{
	public static final String DEFAULT_IP = "0.0.0.0";
	private RandomResource resource;
	private Decision inputDecision;

	@Before
	public void setup()
	{
		resource = new RandomResource( new InMemoryStorage(), mock( BroadcastingCentral.class ) );
		inputDecision = DummyDecision.create();
	}

	@Test
	public void createAndFetch() throws IOException {
		URI decisionLocation = createDecision();

		assertThat( getDecision( decisionLocation ) ).isEqualTo( inputDecision );
	}

	@Test
	public void createDecideAndFetch() throws IOException {
		URI decisionLocation = createDecision();

		assertThat( !getDecision( decisionLocation ).getDecidedAlternative().isPresent() );

		resource.decide( decisionLocation.toString() );

		String decidedAlternative = getDecision( decisionLocation ).getDecidedAlternative().get();
		assertThat( inputDecision.getAlternatives().contains( decidedAlternative ) );
	}

	@Test
	public void shouldPersistVisitors() throws IOException {
		URI decisionLocation = createDecision();

		getDecisionAs( decisionLocation, "Chrome" );
		Decision decision = getDecisionAs( decisionLocation, "Firefox" );

		//assertThat( decision.getSeenBy() ).contains( DEFAULT_IP + " Chrome", DEFAULT_IP + " Firefox" );
	}

	@Test
	public void shouldNotPersistVisitorsAfterDecided() throws IOException {
		URI decisionLocation = createDecision();

		resource.decide( decisionLocation.toString() );

		getDecisionAs( decisionLocation, "Chrome" );
		Decision decision = getDecisionAs( decisionLocation, "Firefox" );

		assertThat( decision.getSeenBy() ).isEmpty();
	}

	private Decision getDecision( URI decisionLocation ) throws IOException {
		return getDecisionAs( decisionLocation, "Lynx" );
	}

	private Decision getDecisionAs( URI decisionLocation, String userAgent ) throws IOException {
		HttpServletRequest request = mock( HttpServletRequest.class );
		when( request.getRemoteHost() ).thenReturn( DEFAULT_IP );
		when( request.getHeader( anyString() ) ).thenReturn( userAgent );
		return resource.getDecision( request, decisionLocation.toString() ).get().getDecision();
	}

	private URI createDecision() throws IOException {
		Response response = resource.createDecision( inputDecision );
		return ( URI )response.getMetadata().getFirst( "Location" );
	}
}
