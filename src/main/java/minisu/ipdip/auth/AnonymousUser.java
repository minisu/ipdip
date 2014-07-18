package minisu.ipdip.auth;

import java.util.UUID;

public class AnonymousUser
{
	public static User create()
	{
		return new User( "Anonymous-user-" + UUID.randomUUID(), "http://www.austadpro.com/blog/wp-content/uploads/2011/07/anonymous-user-gravatar.png", true );
	}
}
