package minisu.ipdip.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import org.brickred.socialauth.util.AccessGrant;

import java.io.*;
import java.util.UUID;

/**
 * <p>
 * Simple representation of a User to provide the following to Resources:<br>
 * <ul>
 * <li>Storage of user state</li>
 * </ul>
 * </p>
 */
public class User {

	/**
	 * <p>
	 * Unique identifier for this entity
	 * </p>
	 */
	private String id;

	/**
	 * <p>
	 * A username (optional for anonymity reasons)
	 * </p>
	 */
	private String userName;

	/**
	 * <p>
	 * A user password (not plaintext and optional for anonymity reasons)
	 * </p>
	 */
	@JsonProperty
	protected String passwordDigest = null;

	/**
	 * <p>
	 * The OpenID discovery information used in phase 1 of authenticating
	 * against an OpenID server
	 * </p>
	 * <p>
	 * Once the OpenID identifier is in place, this can be safely deleted
	 * </p>
	 */
	@JsonProperty
	private DiscoveryInformationMemento openIDDiscoveryInformationMemento;

	/**
	 * An OpenID identifier that is held across sessions
	 */
	@JsonProperty
	private String openIDIdentifier;

	/**
	 * OAuth grant info in JSON format so we don't have to keep the SocialAuth
	 * Manager alive
	 */
	@JsonProperty
	private byte oauthGrantInfo[];

	/**
	 * A shared secret between the cluster and the user's browser that is
	 * revoked when the session ends
	 */
	@JsonProperty
	private UUID sessionToken;

	@JsonCreator
	public User(@JsonProperty("id") String id,
					@JsonProperty("sessionToken") UUID sessionToken) {

		this.id = id;
		this.sessionToken = sessionToken;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return The user name to authenticate with the client
	 */
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return The digested password to provide authentication between the user
	 *         and the client
	 */
	public String getPasswordDigest() {
		return passwordDigest;
	}

	/**
	 * <h3>Note that it is expected that Jasypt or similar is used prior to
	 * storage</h3>
	 *
	 * @param passwordDigest
	 *            The password digest
	 */
	public void setPasswordDigest(String passwordDigest) {
		this.passwordDigest = passwordDigest;
	}

	/**
	 *
	 * @return The OpenID discovery information (phase 1 of authentication)
	 */
	public DiscoveryInformationMemento getOpenIDDiscoveryInformationMemento() {
		return openIDDiscoveryInformationMemento;
	}

	public void setOpenIDDiscoveryInformationMemento(
			DiscoveryInformationMemento openIDDiscoveryInformationMemento) {
		this.openIDDiscoveryInformationMemento = openIDDiscoveryInformationMemento;
	}

	/**
	 * @return The OpenID identifier
	 */
	public String getOpenIDIdentifier() {
		return openIDIdentifier;
	}

	public void setOpenIDIdentifier(String openIDIdentifier) {
		this.openIDIdentifier = openIDIdentifier;
	}

	/**
	 * @return The session key
	 */
	public UUID getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(UUID sessionToken) {
		this.sessionToken = sessionToken;
	}

	public void setOAuthInfo(AccessGrant oathJSON) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(oathJSON);
		oauthGrantInfo = baos.toByteArray();
	}

	public AccessGrant getOAuthInfo() throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream bios = new ByteArrayInputStream(oauthGrantInfo);
		ObjectInputStream ois = new ObjectInputStream(bios);
		AccessGrant grant = (AccessGrant) ois.readObject();
		return grant;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper( this )
				.add("userName", userName)
				.add("openIDIdentifier", openIDIdentifier)
				.add("sessionToken", sessionToken)
				.toString();
	}

}