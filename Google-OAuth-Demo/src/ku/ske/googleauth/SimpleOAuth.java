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
 * Console based demonstration of Google OAuth usage.
 * This code constructs the OAuth requests using Apache HttpClient,
 * so you can see what query parameters, headers, etc. are required.
 * 
 * This code is designed for an "Installed Application" (Google's name) auth flow.
 * After the user grants access to his data you have to copy/paste the auth code
 * into this app!
 * 
 * To run this you need:
 * a) a Google Client ID and Client Secret
 * b) a Google API that you want to access. This is the SCOPE.
 * 
 * @author jim
 */

public class SimpleOAuth {
	// Log messages
	private static final Logger logger = Logger.getLogger( "ConsoleDemo" );
	
	private static final Scanner console = new Scanner(System.in);
	
	// Your Google Client ID (from Google Developer's Console)
	private String clientId = null;
	// Your Google Client Secret
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
	 */
	public String authorize(String authUrl, String redirectUri, String scope) {
		if (clientId == null || clientSecret == null)
			throw new RuntimeException("Must set client credentials before requesting auth token");
		try {
			URIBuilder uribuilder = new URIBuilder(authUrl);
			URI uri = uribuilder.addParameter("response_type", "code")
				.addParameter("client_id", clientId)
				.addParameter("redirect_uri", redirectUri)
				.addParameter("scope", scope)
				.build();
			System.out.println( "Please grant access to your resources and then copy/paste the code here.");
			
			if ( openBrowser(uri.toString()) ) {
				System.out.println( "Started browser for URI " + uri.toString() );
				// wait a moment for Desktop message to print
				try { Thread.sleep(2000); } catch(InterruptedException ex) { /* ignore it */ }
			}
			else {
				System.out.println("Couldn't start browser. Please copy/paste this URL into your browser:");
				System.out.println( uri.toString() );
			}
			System.out.print("Please paste the auth code here: ");
			String authcode = console.nextLine().trim();
			return authcode;
			
		} catch( URISyntaxException usex ) {
			System.out.println("URI Syntax error: " + usex.getMessage() );
		} 
		return null;
	}
	
	/**
	 * Exchange authentication code for an access token and refresh token.
	 * @param accessUrl  Google's URL for getting access token
	 * @param authCode   authorization code obtained by user (resource owner)
	 * @param redirectUri  your redirect URI
	 * @return  Google's response, which will be a JSON object containing access token (if successful)
	 */
	public String getAccessToken(String accessUrl, String authCode, String redirectUri) {
		if (clientId == null || clientSecret == null)
			throw new RuntimeException("Must set client credentials before requesting access token");
		try {
			// create a POST request containing all the parameters Google wants.
			// See Google's OAuth documentation. There are some optional params not shown here.
			CloseableHttpClient httpclient =  HttpClients.createDefault();
			HttpPost post = new HttpPost( accessUrl );
			// Google uses form-encoded parameters.
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add( new BasicNameValuePair("code", authCode) );
			formparams.add( new BasicNameValuePair("client_id", clientId) );
			formparams.add( new BasicNameValuePair("client_secret", clientSecret) );
			formparams.add( new BasicNameValuePair("redirect_uri", redirectUri) );
			formparams.add( new BasicNameValuePair("grant_type", "authorization_code") );
			post.setEntity( new UrlEncodedFormEntity(formparams) );
			
// Another way: Request.Post( accessUrl ).bodyForm( formparams );

			CloseableHttpResponse response = httpclient.execute(post);
			int status = response.getStatusLine().getStatusCode();
			
			logger.info("Response code from access token request: "+200 );
			
			HttpEntity body = response.getEntity();
			String responseData = EntityUtils.toString(body);
			logger.info("getAccess response: " + responseData );
			return responseData;
			
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
	 * @param string containing access response as a JSON object
	 * @return Map of key-values in the access response. Return empty map if can't parse param.
	 */
	public Map<String,String> parseAccessResponse(String accessResponse) {
		// Parse JSON object to a map of key-value pairs.
		try {
			ObjectMapper mapper = new ObjectMapper();
			// define the type of object that ObjectMapper should instantiate.
			TypeReference<Map<String,String>> typeref = new TypeReference< Map<String,String> >() {};
			Map<String,String> accessmap =  mapper.readValue(accessResponse, typeref);
			return accessmap;
			
		} catch (IOException e) {
			System.out.println("Couldn't parse Google access response:");
			System.out.println(accessResponse);
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
