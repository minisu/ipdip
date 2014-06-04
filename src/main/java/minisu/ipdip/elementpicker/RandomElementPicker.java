package minisu.ipdip.elementpicker;

import java.util.List;
import java.util.Random;

public class RandomElementPicker implements ElementPicker
{
	private Random random = new Random();

	@Override
	public <T> T pick( List<T> list )
	{
		int index = random.nextInt( list.size() );
		return list.get( index );
	}
}
