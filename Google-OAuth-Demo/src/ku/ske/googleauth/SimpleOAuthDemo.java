package ku.ske.googleauth;

import java.util.Map;
import java.util.Scanner;

/**
 * Authorize access to a resource using SimpleOAuth.
 * @author jim
 *
 */
public class SimpleOAuthDemo {
	/**  Your Google Client ID and Secret. 
	 *  For a real app you would separate these from your code.
	 */
	static final String CLIENT_ID = "";
	static final String CLIENT_SECRET = "";
	// The API(s) you want to access. If more than one, separate by spaces.
	public static final String TASKS_SCOPE = "https://www.googleapis.com/auth/tasks";
	
	public static final Scanner console = new Scanner(System.in);
	
	/**
	 * Perform authorization using Google OAuth.
	 * @param args
	 */
	public static void main(String[] args) {
		// This is the redirect URI for installed applications
		final String REDIRECT_URI =  "urn:ietf:wg:oauth:2.0:oob";
		
		SimpleOAuth oauth = new SimpleOAuth();

		oauth.setCredentials(CLIENT_ID, CLIENT_SECRET);
		
		// Request authorization token, which requires user consent
		System.out.println("Requesting auth token from Google");
		String authcode = oauth.authorize( Google.AUTH_URL, REDIRECT_URI, TASKS_SCOPE );
		System.out.println("Auth code: "+authcode);
		
		System.out.print("Press ENTER to exchange auth code for access token...");
		console.nextLine();
		
		// Use the authcode to get an access token and refresh token
		System.out.println("Exchanging auth code for access token");
		String accessResponse = oauth.getAccessToken(Google.ACCESS_URL, authcode, REDIRECT_URI);
		
		// Parse the response (JSON)
		Map<String, String> accessmap = oauth.parseAccessResponse( accessResponse );
		// and display what we got
		for(String key: accessmap.keySet()) System.out.printf("%s = %s\n", key, accessmap.get(key));
	}
}
