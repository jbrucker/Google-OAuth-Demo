package ku.ske.googleauth;

import java.util.Map;

/**
 * A shamelessly thin wrapper for a string-to-string map
 * of Google's access code attributes.
 * @author jim
 *
 */
public class AccessToken {
	private final Map<String,String> map;
	// names of Google's keys in map (from Json response object)
	public static final String ACCESS_TOKEN = "access_token";
	public static final String TOKEN_TYPE = "token_type";
	public static final String EXPIRES = "expires_in";
	public static final String REFRESH_TOKEN = "refresh_token";
	
	public AccessToken( Map<String,String> map ) { this.map = map; }

	/**
	 * Get value of an attribute of the access credentials.
	 * @param key one of the JSON attribute names returned by Google's access token response
	 * @return String value of the key
	 */
	public String get(String key) {
		return map.get(key);
	}
}
