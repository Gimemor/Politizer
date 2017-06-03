package Politizer;

import org.json.simple.JSONObject;

 
public interface IRequestProcessor
{
	public boolean Process(ConnectionProcessor connection, JSONObject request);
}
