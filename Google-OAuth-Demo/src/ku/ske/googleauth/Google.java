package ku.ske.googleauth;
/**
 * Define OAuth constants for Google's OAuth.
 * @author jim
 *
 */
public class Google {
	// Endpoint for authenticating user and granting access. (This is same for all Google APIs.)
	public static final String AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
	// Endpoint for Limited Input devices flow to get a user code (auth request code)
	public static final String AUTH_CODE_URL = "https://accounts.google.com/o/oauth2/device/code";
	// Endpoint for exchanging auth code for access token. (This is the same for all Google APIs.)
	public static final String ACCESS_URL = "https://accounts.google.com/o/oauth2/token";
	
	// This is the redirect URI for installed applications
	public static final String INSTALLED_APP_REDIRECT_URI =  "urn:ietf:wg:oauth:2.0:oob";
	
}
