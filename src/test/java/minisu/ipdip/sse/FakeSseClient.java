package minisu.ipdip.sse;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.servlets.EventSource;

public class FakeSseClient {

    private final FakeSseEmitter emitter = new FakeSseEmitter();
    private final EventSource eventSource;

    public FakeSseClient(String query, SubscriptionServlet servlet) throws IOException {
        HttpServletRequest request = createRequestWithQuery(query);
        eventSource = servlet.newEventSource(request);
        eventSource.onOpen(emitter);
    }

    private static HttpServletRequest createRequestWithQuery(String query) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(anyString())).thenReturn(query);
        return request;
    }

    public Set<Event> receivedPushes() {
        return emitter.emittedStrings;
    }

    public boolean receivedExactly(Event... expectedEvents) {
        return receivedPushes().containsAll(Arrays.asList(expectedEvents))
                && receivedPushes().size() == expectedEvents.length;
    }
}
