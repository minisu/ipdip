package minisu.ipdip;

import io.dropwizard.Application;
import io.dropwizard.auth.oauth.OAuthProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import minisu.ipdip.auth.OAuthAuthenticator;
import minisu.ipdip.auth.PublicOAuthResource;
import minisu.ipdip.storage.InMemoryStorage;
import minisu.ipdip.websockets.BroadcasterServlet;
import minisu.ipdip.websockets.BroadcastingCentral;
import org.eclipse.jetty.server.session.SessionHandler;

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

		environment.jersey().register( new PublicOAuthResource( ipDipConfig ) );
		environment.servlets().setSessionHandler( new SessionHandler() );

		environment.jersey().register( new OAuthProvider<>( new OAuthAuthenticator(), "protected-resources" ) );
	}

	public static void main(String... args) throws Exception
	{
		new IpDipApplication().run( args );
	}
}
