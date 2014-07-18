package minisu.ipdip;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import minisu.ipdip.auth.PublicOAuthResource;
import minisu.ipdip.sse.BroadcastingCentral;
import minisu.ipdip.sse.SubscriptionServlet;
import minisu.ipdip.storage.DecisionStorage;
import minisu.ipdip.storage.InMemoryStorage;
import minisu.ipdip.storage.RedisStorage;

import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class IpDipApplication extends Application<IpDipConfig>
{
	@Override
	public void initialize( Bootstrap<IpDipConfig> bootstrap )
	{
		bootstrap.addBundle( new ViewBundle() );
	}

	@Override
	public void run( IpDipConfig config, Environment environment ) throws Exception
	{
		BroadcastingCentral broadcastingCentral = new BroadcastingCentral();
        DecisionStorage storage = new RedisStorage(config.jedisPool());
        environment.jersey().register( new RandomResource(storage, broadcastingCentral ) );

        environment.getApplicationContext().addServlet(
                new ServletHolder(new SubscriptionServlet(broadcastingCentral)), "/subscribe"
        );

		environment.jersey().register( new PublicOAuthResource( config, storage, broadcastingCentral ) );
		environment.servlets().setSessionHandler( new SessionHandler() );
	}

	public static void main(String... args) throws Exception
	{
		new IpDipApplication().run( args );
	}
}
