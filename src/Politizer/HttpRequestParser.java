package Politizer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONObject;


//
// This class responses for an output data determination
//
public class HttpRequestParser 
{
	 private static final Map<String, IRequestProcessor> callbackMap;
	 private  static String _key = "a19a4372";
	 static
	 {
		callbackMap = new HashMap<String, IRequestProcessor>();
		callbackMap.put("confirmation", new IRequestProcessor() {
			public boolean Process(ConnectionProcessor connection, JSONObject request)
			{
				connection.writeResponse(_key);
				return true;
			}
		});
		
		callbackMap.put("wall_post_new", new IRequestProcessor() {
			public boolean Process(ConnectionProcessor connection, JSONObject request)
			{
				System.out.println(request.toJSONString());
				@SuppressWarnings("unchecked")
				Map<String, Object> post = (Map<String, Object>) request.get("object");
				ActionModule.DeleteWallPost(
						connection,
						(Long)post.get("owner_id"),
						(Long)post.get("id"));	 
				
 				return true;
			}
		});
		
	 }
	
	public static void Parse(ConnectionProcessor connection, HttpRequest request)
	{
			JSONObject input = JsonParser.Parse(request.getMessage());
			System.out.println(request.getMessage());
			String type = (String)input.get("type");
			try
			{
					callbackMap.get(type).Process(connection, input);
			} catch(NullPointerException ex)
			{
					return;
			}
			return;
	}
	
	public static void Process(ConnectionProcessor connection, HttpRequest request)
	{
			String resource = request.getRequestLine().split(" ")[1];
			System.out.println(resource);
			//TODO: Сравнение должно быть еще и по длине?
			if(resource.equalsIgnoreCase("/auth"))
			{
 				connection.writeResponse("ok");
 				System.out.println("auth workflow");
 				try {
 					//-88709722
					AuthModule.ObtainAccessToken("5698981", "yzD1fNzyIc6LHPuKALxI", "wall, manage");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(resource.equalsIgnoreCase("/authCallback"))
			{
				System.out.println("authCallback");
				System.out.println(request.getFullRequest());
				connection.writeResponse("ok");
			}
			else if(resource.equalsIgnoreCase("/test"))
			{
				System.out.println("test");
				System.out.println(request.getFullRequest());
				connection.writeResponse("ok");
			}
			else if(resource.equalsIgnoreCase("/"))
			{
				Parse(connection, request);		
				connection.writeResponse("ok");
			}
			else 
			return;
	}
	
	
	
	// we need to list all the criteria to check in the message
 	// so: 
 	// 1) filthy language
 	// 2) 
 	//
 			
}
