package minisu.ipdip.sse;

import java.util.function.Consumer;

public interface SubscriptionService
{
	void subscribe( Consumer<String> consumer, String topic );

	void unsubscribe( Consumer<String> consumer, String topic );
}
