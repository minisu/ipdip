package minisu.ipdip.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import minisu.ipdip.elementpicker.ElementPicker;
import minisu.ipdip.elementpicker.RandomElementPicker;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class Decision
{
	@JsonProperty
	private final String id;

	@JsonProperty
	private final String name;

	@JsonProperty
	private final List<String> alternatives;

	@JsonProperty
	private final Set<String> seenBy;

	private final Supplier<String> decider;

	@JsonProperty
	private volatile Optional<String> decidedAlternative = Optional.absent();

	@JsonCreator
	public Decision(@JsonProperty("name") String name, @JsonProperty("alternatives") List<String> alternatives)
	{
		this( name, alternatives, new RandomElementPicker() );
	}

	public Decision( String name, List<String> alternatives, ElementPicker picker )
	{
		this.id = UUID.randomUUID().toString();
		this.name = checkNotNull( name );
		this.alternatives = checkNotNull( alternatives );
		this.seenBy = new LinkedHashSet<>();
		this.decider = Suppliers.memoize( () -> picker.pick( alternatives ) );
	}

	public String decide()
	{
		String decision = decider.get();
		decidedAlternative = Optional.of( decision );
		return decision;
	}

	public Decision wasSeenBy( String user )
	{
		if( !decidedAlternative.isPresent() )
		{
			seenBy.add( user );
		}
		return this;
	}

	public String getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public List<String> getAlternatives()
	{
		return alternatives;
	}

	public Set<String> getSeenBy()
	{
		return seenBy;
	}

	public Optional<String> getDecidedAlternative()
	{
		return decidedAlternative;
	}

	@Override
	public boolean equals( Object other )
	{
		if( other instanceof Decision )
		{
			Decision otherDecision = ( Decision )other;
			return id.equals( otherDecision.id );
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode( id );
	}

	@Override
	public String toString()
	{
		return Objects.toStringHelper( this )
				.add( "id", id )
				.add( "name", name )
				.toString();
	}
}
