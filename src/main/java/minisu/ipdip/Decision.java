package minisu.ipdip;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.UUID;

public class Decision
{
	@JsonProperty
	private final String id;

	@JsonProperty
	private final String name = "My decision";

	@JsonProperty
	private final List<String> alternatives = ImmutableList.of("Alt1", "Alt2");

	@JsonCreator
	public Decision()
	{
		this.id = UUID.randomUUID().toString();
	}

	public String getId()
	{
		return id;
	}
}
