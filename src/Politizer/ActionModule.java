package Politizer;

 
import org.json.simple.JSONObject;

//
// Module is performing actions over social network's entities
//
public class ActionModule {
	//private String accessKey = "9be764204d2f5ff03340b153d33ddb6466ffdf2a9e1224566169e49152c2da97fedd603c02bed571c480c";
	
	private static String MakeHttpRequest(String method, String resource, String body)
	{
		String result =  method + " " + resource + " HTTP/1.1\r\n" + 
				"Server: Test 1.01\r\n" +
				"Content-Type: application/json\r\n" +
				"Content-Length: " + body.length() + "\r\n" +
				"Connection: close\r\n\r\n" + body + "\r\n";
		return result;
	}
	
	// 
	@SuppressWarnings("unchecked")
	public static void DeleteWallPost(ConnectionProcessor connection, Long owner_id, Long post_id)
	{
		JSONObject json = new JSONObject();
		json.put("owner_id", owner_id);
		json.put("post_id", post_id);
		String body = json.toJSONString();
		String request = MakeHttpRequest("POST", "https://api.vk.com/method/wall.delete", body);
		System.out.println(request);
		connection.sendHttpRequest(request);
		HttpRequest answer = null;
		try {
			answer = connection.readInput();
			System.out.println(answer.getMessage());
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	}
	
	
}
 