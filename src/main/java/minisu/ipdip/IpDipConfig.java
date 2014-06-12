package minisu.ipdip;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

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
}
