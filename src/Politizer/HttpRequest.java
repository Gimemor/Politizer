package Politizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/*
 * Класс представляет собой сущность HTTP-запроса
 *  
 */
public class HttpRequest {
	private String _requestLine;
	private HashMap<String, String> _headerParams;
	private String _body;
	
	public HttpRequest()
	{
		_requestLine = "";
		_body = "";
		_headerParams = new HashMap<String, String>();
		_headerParams.clear();
 	}
	
	public void parseMessage(String message) throws IOException
	{
		BufferedReader br = new BufferedReader(new StringReader(message));
		String s;
		try
		{
			setRequestLine(br.readLine());
			while((s = br.readLine().trim()) != "")
			{
				addHeaderParameter(s);
			}
			while((s = br.readLine()) != null)
			{
				_body = _body + s;
			}
			
		} catch(Exception e)
		{
			throw e;
		}
		
	}

	public void setRequestLine(String s)
	{
		_requestLine = s;
	}
	
	public void addHeaderParameter(String s) 
	{
		String[] param = s.split(":", 2);
		try
		{
			_headerParams.put(param[0].trim(), param[1].trim());
		} catch(Exception e)
		{
			System.out.println(s);
		}
	}
	
	public String getParameter(String s)
	{
		return _headerParams.get(s);
	}
	
	public String getMessage()
	{
		String result = _requestLine;
		return result;
	}

	public void setMessage(String s) {
		_body = s;
		
	}
	
}
