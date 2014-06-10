package minisu.ipdip.websockets;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class BroadcasterServlet extends WebSocketServlet
{
	private final SubscriptionService subscriptionService;

	public BroadcasterServlet( SubscriptionService subscriptionService )
	{
		this.subscriptionService = subscriptionService;
	}

	@Override
	public void configure(WebSocketServletFactory factory)
	{
		factory.setCreator( ( req, resp ) -> new PublishingSocket( subscriptionService ) );
	}
}