package minisu.ipdip;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import minisu.ipdip.storage.InMemoryStorage;
import minisu.ipdip.websockets.BroadcasterServlet;
import minisu.ipdip.websockets.BroadcastingCentral;

import javax.servlet.ServletRegistration;

public class IpDipApplication extends Application<IpDipConfig>
{
	@Override
	public void initialize( Bootstrap<IpDipConfig> bootstrap )
	{
		bootstrap.addBundle( new ViewBundle() );
	}

	@Override
	public void run( IpDipConfig ipDipConfig, Environment environment ) throws Exception
	{
		BroadcastingCentral broadcastingCentral = new BroadcastingCentral();
		environment.jersey().register( new RandomResource( new InMemoryStorage(), broadcastingCentral ) );
		ServletRegistration.Dynamic websocket = environment.servlets().addServlet( "websocket", new BroadcasterServlet( broadcastingCentral ) );
		websocket.addMapping( "/websocket/*" );
	}

	public static void main(String... args) throws Exception
	{
		new IpDipApplication().run( args );
	}
}
