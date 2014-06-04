package minisu.ipdip.elementpicker;

import java.util.List;

public interface ElementPicker
{
	<T> T pick( List<T> list );
}
