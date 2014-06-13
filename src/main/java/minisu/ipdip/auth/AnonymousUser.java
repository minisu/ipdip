package minisu.ipdip.auth;

import java.util.UUID;

public class AnonymousUser
{
	public static User create()
	{
		return new User( "Anonymous-user-" + UUID.randomUUID(), UUID.randomUUID() );
	}
}
