package minisu.ipdip.sse;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.servlets.EventSource;

public class FakeSseClient {

    private final FakeSseEmitter emitter = new FakeSseEmitter();
    private final EventSource eventSource;

    public FakeSseClient(String channel, SubscriptionServlet servlet) throws IOException {
        HttpServletRequest request = createRequestForChannel(channel);
        eventSource = servlet.newEventSource(request);
        eventSource.onOpen(emitter);
    }

    private static HttpServletRequest createRequestForChannel(String query) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(anyString())).thenReturn(query);
        return request;
    }

    public Set<Event> receivedPushes() {
        return emitter.emittedStrings;
    }
}
