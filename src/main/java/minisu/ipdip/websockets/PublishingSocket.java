package minisu.ipdip.websockets;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;

@WebSocket
public class PublishingSocket
{
	private final SubscriptionService subscriptionService;
	private Session session;
	private String topic;

	public PublishingSocket( SubscriptionService subscriptionService )
	{
		this.subscriptionService = subscriptionService;
	}

	@OnWebSocketMessage
	public void onMessage( Session session, String message ) throws IOException
	{
		this.session = session;
		this.topic = message;

		subscriptionService.subscribe( this, message );
		session.getRemote().sendString( "Client " + session.getRemoteAddress() + " subscribed to id " + message );
	}

	@OnWebSocketClose
	public void onClose(int statusCode, String reason)
	{
		subscriptionService.unsubscribe( this, topic );
	}

	void send( String message )
	{
		session.getRemote().sendStringByFuture( message );
	}
}