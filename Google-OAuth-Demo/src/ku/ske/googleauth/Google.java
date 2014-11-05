package ku.ske.googleauth;
/**
 * Define OAuth constants for Google's OAuth.
 * @author jim
 *
 */
public class Google {
	// Endpoint for authenticating user and granting access. (This is same for all Google APIs.)
	public static final String AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
	// Endpoint for exchanging auth code for access token. (This is the same for all Google APIs.)
	public static final String ACCESS_URL = "https://accounts.google.com/o/oauth2/token";
}
