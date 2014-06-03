package minisu.ipdip;


import com.google.common.base.Optional;

public class InMemoryStorage implements DecisionStorage
{
	@Override
	public void store( Decision decision )
	{

	}

	@Override
	public Optional<Decision> get( String id )
	{
		return Optional.of( new Decision() );
	}
}
