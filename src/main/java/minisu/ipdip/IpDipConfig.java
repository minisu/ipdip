package minisu.ipdip;

import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import io.dropwizard.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class IpDipConfig extends Configuration
{
	@JsonProperty
	private OAuthCfgClass oauth = new OAuthCfgClass();

	public Properties getOAuthCfgProperties() {
		Properties properties = new Properties();
		properties.put(oauth.getPrefix() + ".consumer_key", oauth.getKey());
		properties.put(oauth.getPrefix() + ".consumer_secret", oauth.getSecret());
		return properties;
	}

	@JsonProperty private String oauthSuccessUrl = "";
	public String getOAuthSuccessUrl() {
		return oauthSuccessUrl;
	}

	public static class OAuthCfgClass {
		@JsonProperty private String prefix;
		@JsonProperty private String key;
		@JsonProperty private String secret;

		public String getPrefix() {
			return prefix;
		}
		public String getKey() {
			return key;
		}
		public String getSecret() {
			return secret;
		}
	}

    JedisPool jedisPool() {
        try {
            URI redisUri = new URI(firstNonNull(System.getenv("REDISCLOUD_URL"), "redis://rediscloud:YG02t4nwi9e3Wwjm@pub-redis-14037.us-east-1-3.1.ec2.garantiadata.com:14037"));
            JedisPool pool = new JedisPool(new JedisPoolConfig(),
                    redisUri.getHost(),
                    redisUri.getPort(),
                    Protocol.DEFAULT_TIMEOUT,
                    redisUri.getUserInfo().split(":",2)[1]);
            return pool;
        } catch (URISyntaxException e) {
            throw new AssertionError(e);
        }
    }
}
