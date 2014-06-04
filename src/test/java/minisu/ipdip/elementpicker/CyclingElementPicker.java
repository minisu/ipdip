package minisu.ipdip.elementpicker;

import java.util.List;

public class CyclingElementPicker implements ElementPicker
{
	private int invocationCounter = 0;

	@Override
	public <T> T pick( List<T> list )
	{
		return list.get( invocationCounter++ );
	}
}
