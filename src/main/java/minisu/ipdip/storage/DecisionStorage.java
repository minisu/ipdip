package minisu.ipdip.storage;


import minisu.ipdip.model.Decision;

import java.io.IOException;

import com.google.common.base.Optional;

public interface DecisionStorage
{
	void store( Decision decision ) throws IOException;

	Optional<Decision> get( String id ) throws IOException;
}
