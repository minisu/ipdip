package minisu.ipdip.sse;

import java.util.function.Consumer;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class BroadcastingCentral implements SubscriptionService, Broadcaster
{
	private final Multimap<String, Consumer<String>> sessions = HashMultimap.create();

	@Override
    public void subscribe( Consumer<String> socket, String topic )
	{
		sessions.put( topic, socket );
	}

	@Override
    public void unsubscribe( Consumer<String> socket, String topic )
	{
		sessions.remove( topic, socket );
	}

	@Override
    public void broadcast( String topic, String message )
	{
		sessions.get( topic )
				.forEach( socket -> socket.accept(message) );
	}
}
