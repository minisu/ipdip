package minisu.ipdip;

import com.google.common.collect.ImmutableList;
import minisu.ipdip.elementpicker.CyclingElementPicker;
import minisu.ipdip.model.Decision;

public class DummyDecision
{
	public static Decision create()
	{
		return new Decision( "Should we party?", ImmutableList.of( "Yes", "No" ), new CyclingElementPicker() );
	}
}
