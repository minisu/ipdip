package minisu.ipdip.storage;

import com.google.common.base.Optional;
import minisu.ipdip.model.Decision;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStorage implements DecisionStorage
{
	private ConcurrentHashMap<String, Decision> decisions = new ConcurrentHashMap<String, Decision>();

	@Override
	public void store( Decision decision )
	{
		decisions.put( decision.getId(), decision );
	}

	@Override
	public Optional<Decision> get( String id )
	{
		return Optional.fromNullable( decisions.get( id ) );
	}
}
