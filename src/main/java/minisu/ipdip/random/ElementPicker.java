package minisu.ipdip.random;

import java.util.List;

public interface ElementPicker
{
	<T> T pick( List<T> list );
}
