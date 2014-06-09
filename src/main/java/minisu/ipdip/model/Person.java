package minisu.ipdip.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;

public class Person
{
	@JsonProperty
	private String id; // Hash of user agent and IP address

	@JsonProperty
	private Optional<String> name;
}
