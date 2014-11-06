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
	static final String CLIENT_ID = "502508066147-th58js7tkabu0h9b8h38q3tk7hi9tlvj.apps.googleusercontent.com";
	static final String CLIENT_SECRET = "k4qy81wR8KWkdA0B5DwMdJIN";
	// The API(s) you want to access. If more than one, separate by spaces.
	public static final String SCOPE = "https://www.googleapis.com/auth/tasks";
	
	public static final Scanner console = new Scanner(System.in);
	
	/**
	 * Perform authorization using Google OAuth.
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		SimpleOAuth oauth = new SimpleOAuth();

		oauth.setCredentials(CLIENT_ID, CLIENT_SECRET);
		
		// Request authorization token, which requires user consent
		System.out.println("Requesting auth token from Google");
		String authCode = null;
		if ( oauth.authorize( SCOPE ) ) {
			System.out.print("Please copy and paste the authorization code here: ");
			authCode = console.nextLine().trim();
		}
		
		
		System.out.print("Press ENTER to exchange auth code for access token...");
		console.nextLine();
		
		// Use the authcode to get an access token and refresh token
		System.out.println("Exchanging auth code for access token");
		AccessToken token = oauth.getAccessToken( authCode );
		
		// Use the access token to authorize an API call to a Google service
		
	}
}
