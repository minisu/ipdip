package minisu.ipdip.sse;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.servlets.EventSource;
import org.eclipse.jetty.servlets.EventSourceServlet;

@SuppressWarnings("serial")
public class SubscriptionServlet extends EventSourceServlet {

    private final SubscriptionService subscriptionService;

    public SubscriptionServlet(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Override
    public EventSource newEventSource(HttpServletRequest request) {
        String channel = request.getParameter( "channel" );
        SseEventSource eventSource = new SseEventSource();

        subscriptionService.subscribe( eventSource, channel );

        return eventSource;
    }
}
