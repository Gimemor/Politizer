package Politizer;
import org.json.simple.*;
import org.json.simple.parser.*;

 
public class JsonParser {
	
	public static JSONObject Parse(String input)
	{
		System.out.println(input);
		JSONParser parser = new JSONParser();
		JSONObject object;
		try {
			object = (JSONObject) parser.parse(input.trim());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}
		return object;
	}
	
	
	
	
}
