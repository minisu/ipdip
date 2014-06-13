package minisu.ipdip.auth;

import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class OAuthAuthenticator implements Authenticator<String, User>
{
	static Logger log = LoggerFactory.getLogger( OAuthAuthenticator.class );

	@Override
	public Optional<User> authenticate( String sessionToken ) throws AuthenticationException
	{
		log.info( "Authentication attempt with credentials: " + sessionToken );

		// Get the User referred to by the API key
		Optional<User> user = UserCache
				.INSTANCE
				.getBySessionToken( UUID.fromString( sessionToken ) );
		if (!user.isPresent()) {
			log.info("Anonymous user returned");
			return Optional.of( AnonymousUser.create() );
		}

		log.info( "Returned " + user );
		return user;
	}
}
