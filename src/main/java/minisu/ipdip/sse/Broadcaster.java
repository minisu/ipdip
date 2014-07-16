package minisu.ipdip.sse;

public interface Broadcaster
{
	public void broadcast( String topic, Event message );
}
