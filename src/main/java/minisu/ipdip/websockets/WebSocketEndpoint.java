package minisu.ipdip.websockets;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;

@WebSocket
public class WebSocketEndpoint
{
	public static WebSocketEndpoint usedByEventBus( EventBus eventBus )
	{
		WebSocketEndpoint endpoint = new WebSocketEndpoint();
		eventBus.register( endpoint );
		return endpoint;
	}

	private final Multimap<String, Session> sessions = HashMultimap.create();

	@OnWebSocketMessage
	public void onMessage( Session session, String message ) throws IOException
	{
		sessions.put( message, session );
		session.getRemote().sendString( "Client " + session.getRemoteAddress() + " subscribed to id " + message );
	}

	@Subscribe
	public void broadcast( String id )
	{
		sessions.get( id ).stream()
				.map( Session::getRemote )
				.forEach( remote -> remote.sendStringByFuture( "New viewer!" ) );
	}
}