package minisu.ipdip.websockets;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class BroadcastingCentral implements SubscriptionService, Broadcaster
{
	private final Multimap<String, PublishingSocket> sessions = HashMultimap.create();

	public void subscribe( PublishingSocket socket, String topic )
	{
		sessions.put( topic, socket );
	}

	public void unsubscribe( PublishingSocket socket, String topic )
	{
		sessions.remove( topic, socket );
	}

	public void broadcast( String topic, String message )
	{
		sessions.get( topic )
				.forEach( socket -> socket.send( message ) );
	}
}
