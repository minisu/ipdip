package minisu.ipdip.sse;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import minisu.ipdip.DummyDecision;
import minisu.ipdip.RandomResource;
import minisu.ipdip.auth.AnonymousUser;
import minisu.ipdip.model.Decision;
import minisu.ipdip.storage.InMemoryStorage;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.junit.Test;

public class SubscriptionServletTest {

    public static final String DEFAULT_IP = "0.0.0.0";

    private RandomResource resource;

    @Test
    public void abc() throws Exception {
        BroadcastingCentral broadcastingCentral = new BroadcastingCentral();
        resource = new RandomResource(new InMemoryStorage(), broadcastingCentral);
        SubscriptionServlet servlet = new SubscriptionServlet(broadcastingCentral);

        Response response = resource.createDecision(DummyDecision.create());
        URI decisionLocation = ( URI )response.getMetadata().getFirst( "Location" );
        Decision decision = getDecision(decisionLocation);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(anyString())).thenReturn(decision.getId());

        FakeSseClient client = new FakeSseClient(decision.getId(), servlet);

        resource.decide(decision.getId());

        System.out.println(client.receivedPushes());
        assertTrue(client.receivedExactly(Event.of("decisionMade", "Yes")));
    }

    private Decision getDecision( URI decisionLocation )
    {
        return getDecisionAs( decisionLocation, "Lynx" );
    }

    private Decision getDecisionAs( URI decisionLocation, String userAgent )
    {
        HttpServletRequest request = mock( HttpServletRequest.class );
        when( request.getRemoteHost() ).thenReturn( DEFAULT_IP );
        when( request.getHeader( anyString() ) ).thenReturn( userAgent );
        return resource.getDecision( request, AnonymousUser.create(), decisionLocation.toString() ).get().getDecision();
    }
}
