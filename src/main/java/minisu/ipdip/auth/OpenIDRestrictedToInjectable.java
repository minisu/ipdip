package minisu.ipdip.auth;

import com.google.common.base.Optional;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * <ul>
 * <li>Performs decode from HTTP request</li>
 * <li>Carries OpenID authentication data</li>
 * </ul>
 */
class OpenIDRestrictedToInjectable extends AbstractHttpContextInjectable<User> {

    private static final Logger log = LoggerFactory.getLogger(OpenIDRestrictedToInjectable.class);

    private final Authenticator<String, User> authenticator;
    private final String realm;

    /**
     * @param authenticator       The Authenticator that will compare credentials
     * @param realm               The authentication realm
     */
    OpenIDRestrictedToInjectable(
            Authenticator<String, User> authenticator,
            String realm) {
        this.authenticator = authenticator;
        this.realm = realm;
    }

    public Authenticator<String, User> getAuthenticator() {
        return authenticator;
    }

    public String getRealm() {
        return realm;
    }

    @Override
    public User getValue(HttpContext httpContext) {

        try {
            // Get the Authorization header
            final Map<String, Cookie> cookieMap = httpContext.getRequest().getCookies();
            if (!cookieMap.containsKey("IpDip-session")) {
                return AnonymousUser.create();
            }

            String sessionToken = cookieMap.get("IpDip-session").getValue();

            if (sessionToken != null) {

                final Optional<User> result = authenticator.authenticate(sessionToken);
                if (result.isPresent()) {
                    return result.get();
                }
            }
        } catch (IllegalArgumentException e) {
            log.debug("Error decoding credentials", e);
        } catch (AuthenticationException e) {
            log.warn("Error authenticating credentials", e);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

        // Must have failed to be here
        throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

}
