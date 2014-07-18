package minisu.ipdip.sse;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import minisu.ipdip.DummyDecision;
import minisu.ipdip.RandomResource;
import minisu.ipdip.auth.AnonymousUser;
import minisu.ipdip.model.Decision;
import minisu.ipdip.storage.InMemoryStorage;

import java.io.IOException;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SubscriptionServletTest {

    public static final String DEFAULT_IP = "0.0.0.0";

    private RandomResource resource;
    private SubscriptionServlet servlet;

    @Before
    public void setup() {
        BroadcastingCentral broadcastingCentral = new BroadcastingCentral();
        resource = new RandomResource(new InMemoryStorage(), broadcastingCentral);
        servlet = new SubscriptionServlet(broadcastingCentral);
    }

    @Test
    public void decisionsShouldBeEmitted() throws Exception {

        URI decisionLocation = createDecision();
        Decision decision = getDecision(decisionLocation);

        FakeSseClient client = createFakeClient(decision.getId());

        resource.decide(decision.getId());

        assertThat(client.receivedPushes()).containsExactly(Event.of("decisionMade", "Yes"));
    }

    @Ignore
    @Test
    public void newVisitorsShouldBeEmitted() throws Exception {

        URI decisionLocation = createDecision();
        Decision decision = getDecision(decisionLocation);

        FakeSseClient client = createFakeClient(decision.getId());
        getDecisionAs(decisionLocation, "Chrome");

        assertThat(client.receivedPushes()).containsExactly(Event.of("newVisitor", "0.0.0.0 Chrome"));
    }

    @Ignore
    @Test
    public void newVisitorsShouldNotBeEmittedAfterDecisionHasBeenMade() throws Exception {

        URI decisionLocation = createDecision();
        Decision decision = getDecision(decisionLocation);

        FakeSseClient client = createFakeClient(decision.getId());
        resource.decide(decision.getId());

        getDecisionAs(decisionLocation, "Chrome");

        assertThat(client.receivedPushes()).isEmpty();
    }

    private URI createDecision() throws IOException {
        Response response = resource.createDecision(DummyDecision.create());
        return ( URI )response.getMetadata().getFirst( "Location" );
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

    private FakeSseClient createFakeClient(String decisionId) throws IOException {
        return new FakeSseClient(decisionId, servlet);
    }
}
