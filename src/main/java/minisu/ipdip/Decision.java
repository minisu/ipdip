package minisu.ipdip;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class Decision
{
	@JsonProperty
	String id = "abc";

	@JsonProperty
	String name = "My decision";

	@JsonProperty
	List<String> alternatives = ImmutableList.of("Alt1", "Alt2");

	public String getId()
	{
		return id;
	}
}
