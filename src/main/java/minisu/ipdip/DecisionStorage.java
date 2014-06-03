package minisu.ipdip;


import com.google.common.base.Optional;

public interface DecisionStorage
{
	void store( Decision decision );

	Optional<Decision> get( String id );
}
