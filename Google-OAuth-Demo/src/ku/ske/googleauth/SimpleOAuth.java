package ku.ske.googleauth;
/*
 * JARs need to comple and run:
 * Apache HTTP Client 4 (several JARs)
 * jackson-core-2.4.x.jar
 * jackson-databind-2.4.x.jar
 * jackson-annotations-2.4.x.jar
 */
import java.awt.Desktop;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * This code provides OAuth authorization for Google's "Installed Application" 
 * authorization flow.  This is for a desktop app that is not able to receive
 * a redirect from Google.
 * 
 * To use this code, create a new instance and then...
 * 1. call setCredentials to input
 * your CLIENT_ID and CLIENT_SECRET.  Those credentials must be for an app
 * you registered at Google's API Console as "Installed Application" of type "Other".
 * 
 * 2. call authorize(scope) when you want the user to authorize your access
 * to a scope.(Google's name) auth flow.
 * After the user grants access to his data you have to copy/paste the auth code
 * into this app!
 * 
 * 
 * This code constructs the OAuth requests using Apache HttpClient,
 * so you can see what query parameters, headers, etc. are required.
 * 
 *
 * To run this you need:
 * a) a Google Client ID and Client Secret
 * b) a Google API that you want to access. This is the SCOPE.
 * 
 * @author jim
 */

public class SimpleOAuth {
	private static final int HTTP_STATUS_OK = 200;

	// Log messages
	private static final Logger logger = Logger.getLogger( "OAuthDemo" );
	
	// The application's Google Client ID (from Google Developer's Console)
	private String clientId = null;
	// The application's Google Client Secret
	private String clientSecret = null;
	
	/**
	 * The the Client ID and Client Secret.
	 * You must call this method before requesting an authorization code or access code.
	 * @param clientId your Google Client ID
	 * @param clientSecret your Google Client Secret
	 */
	 public void setCredentials(String client_Id, String client_Secret) {
		 clientId = client_Id;
		 clientSecret = client_Secret;
	 }

	/**
	 * Ask the user to authorize access to the scope.
	 * This will open a browser window connected to Google's authentication server.
	 * If users grants access to his resources, google will display the Auth Token in browser.
	 * User must copy and paste the authorization token here.
	 * The Google authorization URI is:
	   http://accounts.google.com/o/oauth2/auth?
                  response_type=code    (for installed applications)
                 &client_id=yourClientId
                 &redirect_uri=(one of the URI from Google API console)
                 &scope=(name of resources you want access to, separate by space if more than one)
	 
	 * @param scope is the scope of Google resources you want to access. If more than one,
	 * separate the scope URLs by space.
	 */
	public boolean authorize( String scope ) {
		if (clientId == null || clientSecret == null)
			throw new RuntimeException("Must set client credentials before requesting auth token");
		try {
			URIBuilder uribuilder = new URIBuilder(Google.AUTH_URL);
			URI uri = uribuilder.addParameter("response_type", "code")
				.addParameter("client_id", clientId)
				.addParameter("redirect_uri", Google.INSTALLED_APP_REDIRECT_URI)
				.addParameter("scope", scope)
				.build();
			
			if ( openBrowser(uri.toString()) ) {
				logger.info( "Started browser for URI " + uri.toString() );
				// wait a moment for Desktop message to print
				try { Thread.sleep(2000); } catch(InterruptedException ex) { /* ignore it */ }
			}
			else {
				System.out.println("Couldn't start browser. Please copy/paste this URL into your browser:");
				System.out.println( uri.toString() );
			}
			return true; // it probably succeeded :-)
			
		} catch( URISyntaxException usex ) {
			System.out.println("URI Syntax error: " + usex.getMessage() );
		} 
		return false;
	}
	
	/**
	 * Exchange authentication code for an access token and refresh token.
	 * First call authorize() and get the authCode from the user (somehow, like copy/paste
	 * into your app window).
	 * @param authCode   authorization code obtained by the user (resource owner).
	 * @return  Google's response, which will be a JSON object containing access token (if successful)
	 *         or return null if unsuccessful.
	 */
	public AccessToken getAccessToken(String authCode) {
		if (clientId == null || clientSecret == null)
			throw new RuntimeException("Must set client credentials before requesting access token");
		try {
			// create a POST request containing all the parameters Google wants.
			// See Google's OAuth documentation. There are some optional params not shown here.
			CloseableHttpClient httpclient =  HttpClients.createDefault();
			HttpPost post = new HttpPost( Google.ACCESS_URL );
			// Google uses form-encoded parameters.
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add( new BasicNameValuePair("code", authCode) );
			formparams.add( new BasicNameValuePair("client_id", clientId) );
			formparams.add( new BasicNameValuePair("client_secret", clientSecret) );
			formparams.add( new BasicNameValuePair("redirect_uri", Google.INSTALLED_APP_REDIRECT_URI) );
			formparams.add( new BasicNameValuePair("grant_type", "authorization_code") );
			post.setEntity( new UrlEncodedFormEntity(formparams) );
			
// Another way: Request.Post( accessUrl ).bodyForm( formparams );

			CloseableHttpResponse response = httpclient.execute(post);
			int status = response.getStatusLine().getStatusCode();
			HttpEntity body = response.getEntity();
			String responseData = EntityUtils.toString(body);
			
			logger.info("Response code from access token request: "+status );
			if (status != HTTP_STATUS_OK) {
				logger.warning("Error response from access request: "+responseData);
				return null;
			}
			
			// parse response and return access token + refresh token
			Map<String,String> map = parseJson(responseData);
			AccessToken token = new AccessToken( map );
			
			return token;
			
			
		}  catch (UnsupportedEncodingException e) {
			System.out.println("Programmer error");
			logger.severe( e.getMessage() );
		} catch (ClientProtocolException e) {
			System.out.println("Protocol error");
			logger.severe( e.getMessage() );
		} catch (IOException e) {
			System.out.println("I/O Exception. Are you connected to Internet?");
			logger.severe( e.getMessage() );
		} 
		return null;
	}
	
	/**
	 * Parse the access response in JSON format.
	 * @param jsonString contains key-value pairs in JSON format
	 * @return Map of key-values in the key-value pairs in JSON string data
	 */
	public Map<String,String> parseJson(String jsonString) {
		// Parse JSON object to a map of key-value pairs.
		try {
			ObjectMapper mapper = new ObjectMapper();
			// define the type of object that ObjectMapper should instantiate.
			TypeReference<Map<String,String>> typeref = new TypeReference< Map<String,String> >() {};
			Map<String,String> accessmap =  mapper.readValue(jsonString, typeref);
			return accessmap;
			
		} catch (IOException e) {
			System.out.println("Couldn't parse Google access response:");
			System.out.println(jsonString);
		}
		return new HashMap<String,String>();
	}
	
	/**
	 * Launch browser window and navigate to a URL.
	 * @param url the URL to browse
	 * @return true if apparently successful.
	 */
	public static boolean openBrowser(String url) {
		if (! Desktop.isDesktopSupported()) return false;
		try {
			Desktop.getDesktop().browse( new URI(url) );
		} catch ( Exception ex ) {
			logger.severe("Coudn't open browser window");
			System.out.println("Oops. "+ex.getMessage() );
			return false;
		}
		return true;
	}
}
