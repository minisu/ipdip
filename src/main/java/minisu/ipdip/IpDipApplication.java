package minisu.ipdip;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import minisu.ipdip.storage.InMemoryStorage;
import minisu.ipdip.websockets.MyWebSocketServlet;

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
		environment.jersey().register( new RandomResource( new InMemoryStorage() ) );
		ServletRegistration.Dynamic websocket = environment.servlets().addServlet( "websocket", new MyWebSocketServlet() );
		websocket.addMapping( "/websocket/*" );
	}

	public static void main(String... args) throws Exception
	{
		new IpDipApplication().run( args );
	}
}
