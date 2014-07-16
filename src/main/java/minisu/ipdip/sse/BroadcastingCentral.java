package minisu.ipdip.sse;

import java.util.function.Consumer;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class BroadcastingCentral implements SubscriptionService, Broadcaster
{
	private final Multimap<String, Consumer<Event>> sessions = HashMultimap.create();

	@Override
    public void subscribe( Consumer<Event> socket, String topic )
	{
		sessions.put( topic, socket );
	}

	@Override
    public void unsubscribe( Consumer<Event> socket, String topic )
	{
		sessions.remove( topic, socket );
	}

	@Override
    public void broadcast( String topic, Event message )
	{
		sessions.get( topic )
				.forEach( socket -> socket.accept(message) );
	}
}
