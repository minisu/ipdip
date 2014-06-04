package minisu.ipdip.storage;


import com.google.common.base.Optional;
import minisu.ipdip.model.Decision;

public interface DecisionStorage
{
	void store( Decision decision );

	Optional<Decision> get( String id );
}
