package minisu.ipdip.views;

import io.dropwizard.views.View;
import minisu.ipdip.model.Decision;

public class DecisionView extends View
{
	private final Decision decision;

	public DecisionView( Decision decision )
	{
		super( "decision.ftl" );
		this.decision = decision;
	}

	public String getDecidedAlternative()
	{
		return decision.getDecidedAlternative().orNull();
	}

	public Decision getDecision()
	{
		return decision;
	}
}
