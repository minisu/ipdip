package minisu.ipdip.auth;

import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import io.dropwizard.auth.Auth;
import io.dropwizard.auth.Authenticator;

/**
 * <p>Authentication provider to provide the following to Jersey:</p>
 * <ul>
 * <li>Bridge between Dropwizard and Jersey for HMAC authentication</li>
 * </ul>
 *
 */
public class AuthProvider implements InjectableProvider<Auth, Parameter> {

    private final Authenticator<String, User> authenticator;
    private final String realm;

    /**
     * Creates a new {@link AuthProvider} with the given Authenticator and realm.
     *
     * @param authenticator the authenticator which will take the String and
     *                      convert them into instances of {@code T}
     * @param realm         the name of the authentication realm
     */
    public AuthProvider(Authenticator<String, User> authenticator, String realm) {
        this.authenticator = authenticator;
        this.realm = realm;
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Override
    public Injectable<?> getInjectable(ComponentContext ic,
                                       Auth a,
                                       Parameter c) {
        return new OpenIDRestrictedToInjectable(authenticator, realm);
    }
}