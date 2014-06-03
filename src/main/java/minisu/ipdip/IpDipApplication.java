package minisu.ipdip;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class IpDipApplication extends Application<IpDipConfig>
{
	@Override
	public void initialize( Bootstrap<IpDipConfig> ipDipConfigBootstrap )
	{

	}

	@Override
	public void run( IpDipConfig ipDipConfig, Environment environment ) throws Exception
	{
		environment.jersey().register( new RandomResource( new InMemoryStorage() ) );
	}

	public static void main(String... args) throws Exception
	{
		new IpDipApplication().run( args );
	}
}
