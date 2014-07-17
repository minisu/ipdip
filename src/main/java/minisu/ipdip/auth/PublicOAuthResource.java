package minisu.ipdip.auth;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import minisu.ipdip.IpDipConfig;
import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.SocialAuthConfig;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.util.SocialAuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * Resource to provide the following to application:
 * </p>
 * <ul>
 * <li>OAuth authentication handling</li>
 * </ul>
 */
@Path("/oauth")
@Produces( MediaType.TEXT_HTML)
public class PublicOAuthResource  {

	private static final Logger log = LoggerFactory.getLogger( PublicOAuthResource.class );

	private final IpDipConfig appConfig;

	public PublicOAuthResource( IpDipConfig appConfig )
	{
		this.appConfig = appConfig;
	}

	@GET
	@Timed
	@Path("/request")
	public Response requestOAuth(@Context HttpServletRequest request, @QueryParam( "decisionId" ) String decisionId) throws Exception
	{
		String redirectUri = URLEncoder.encode( "http://placeholder.com:9010/decisions/" + decisionId, "UTF-8" );

		// instantiate SocialAuth for this provider type and tuck into session
		// get the authentication URL for this provider
		SocialAuthManager manager = getSocialAuthManager();
		request.getSession().setAttribute("authManager", manager);

		URI url = new URI( manager.getAuthenticationUrl(
				"twitter", appConfig.getOAuthSuccessUrl() + "?redirect_uri=" + redirectUri ) );
		log.debug("OAuth Auth URL: {}", url);
		return Response.temporaryRedirect( url ).build();
	}

	/**
	 * Handles the OAuth server response to the earlier AuthRequest
	 *
	 * @return The OAuth identifier for this user if verification was
	 *         successful
	 */
	@GET
	@Timed
	@Path("/verify")
	public Response verifyOAuthServerResponse( @Context HttpServletRequest request,
															 @QueryParam( "redirect_uri" ) String redirectUri ) throws Exception
	{
		// this was placed in the session in the /request resource
		SocialAuthManager manager = (SocialAuthManager) request.getSession()
				.getAttribute("authManager");

		if (manager != null) {
			// call connect method of manager which returns the provider object
			Map<String, String> params = SocialAuthUtil
					.getRequestParametersMap( request );
			AuthProvider provider = manager.connect(params);

			// get profile
			Profile p = provider.getUserProfile();

			log.info("Logging in user '{}'", p);

			// at this point, we've been validated, so save off this user's info
			User tempUser = new User(p.getDisplayName(), p.getProfileImageURL(), UUID.randomUUID());
			tempUser.setOpenIDIdentifier( p.getValidatedId() );
			tempUser.setOAuthInfo(provider.getAccessGrant());

			// Search for a pre-existing User matching the temp User
			Optional<User> userOptional = UserCache.INSTANCE
					.getByOpenIDIdentifier( tempUser.getOpenIDIdentifier() );
			if (!userOptional.isPresent()) {
				// Persist the user with the generated session token
				UserCache.INSTANCE.put(tempUser.getSessionToken(),
						tempUser);
			} else {
				tempUser = userOptional.get();
			}

			return Response
					.temporaryRedirect( new URI( URLDecoder.decode( redirectUri, "UTF-8" ) ) )
					.cookie( replaceSessionTokenCookie( Optional.of( tempUser ) ) )
					.build();
		}

		// Must have failed to be here
		throw new WebApplicationException(Response.Status.UNAUTHORIZED);
	}

	/**
	 * Gets an initialized SocialAuthManager
	 * @return gets an initialized SocialAuthManager
	 */
	private SocialAuthManager getSocialAuthManager() throws Exception
	{
		SocialAuthConfig config = SocialAuthConfig.getDefault();
		config.load( appConfig.getOAuthCfgProperties() );
		SocialAuthManager manager = new SocialAuthManager();
		manager.setSocialAuthConfig(config);
		return manager;
	}

	/**
	 * @param user A user with a session token. If absent then the cookie will be removed.
	 *
	 * @return A cookie with a long term expiry date suitable for use as a session token for OpenID
	 */
	protected NewCookie replaceSessionTokenCookie(Optional<User> user) {

		if (user.isPresent()) {

			String value = user.get().getSessionToken().toString();

			log.info("Replacing session token with {}", value);

			return new NewCookie(
					"IpDip-session",
					value,   // Value
					"/",     // Path
					null,    // Domain
					null,    // Comment
					86400 * 30, // 30 days
					false);
		} else {
			// Remove the session token cookie
			log.debug("Removing session token");

			return new NewCookie(
					"IpDip-session",
					null,   // Value
					null,    // Path
					null,   // Domain
					null,   // Comment
					0,      // Expire immediately
					false);
		}
	}

}