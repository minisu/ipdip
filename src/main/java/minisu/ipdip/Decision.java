package minisu.ipdip;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import minisu.ipdip.random.ElementPicker;
import minisu.ipdip.random.RandomElementPicker;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Decision
{
	@JsonProperty
	private final String id;

	@JsonProperty
	private final String name;

	@JsonProperty
	private final List<String> alternatives;

	private final Supplier<String> decider;

	@JsonCreator
	public Decision(@JsonProperty("name") String name, @JsonProperty("alternatives") List<String> alternatives)
	{
		this( name, alternatives, new RandomElementPicker() );
	}

	Decision(String name, List<String> alternatives, ElementPicker picker)
	{
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.alternatives = alternatives;
		this.decider = Suppliers.memoize( () -> picker.pick( alternatives ) );
	}

	public String getId()
	{
		return id;
	}

	@Override
	public boolean equals( Object other )
	{
		if( other instanceof Decision )
		{
			Decision otherDecision = ( Decision )other;
			return id == otherDecision.id;
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( id );
	}

	public String decide()
	{
		return decider.get();
	}
}
