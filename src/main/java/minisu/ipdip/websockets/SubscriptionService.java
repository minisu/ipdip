package minisu.ipdip.websockets;

public interface SubscriptionService
{
	void subscribe( PublishingSocket socket, String topic );

	void unsubscribe( PublishingSocket socket, String topic );
}
