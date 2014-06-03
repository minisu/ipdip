package minisu.ipdip;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class Decision
{
	@JsonProperty
	List<String> alternatives = ImmutableList.of("Alt1", "Alt2");
}
