package ku.ske.googleauth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test parsing JSON to Map using Jackson
 * @author jim
 *
 */
public class JacksonTest {

	public static void main(String[] args) {
		String json = "{ \"a\": \"apple\", \"b\": \"banana\", \"n\": 123 }";
		
		ObjectMapper mapper = new ObjectMapper();
		// TypeReference describes the datatype the objectmapper should unmarshal into
		// Similar to Jersey's GenericType
		TypeReference<Map<String,Object>> typeref = new TypeReference< Map<String,Object> >() {};
		Map<String,Object> values = null;
		try {
			values = mapper.readValue(json, typeref);
			for(String key : values.keySet()) System.out.printf("%s = %s\n", key, values.get(key));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
