package minisu.ipdip.sse;

import java.util.function.Consumer;

public interface SubscriptionService
{
	void subscribe( Consumer<Event> consumer, String topic );

	void unsubscribe( Consumer<Event> consumer, String topic );
}
