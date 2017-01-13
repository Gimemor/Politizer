package Politizer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionProcessor implements Runnable
{
	Socket clientSocket;
	InputStream inputStream;
	OutputStream outputStream;
	public ConnectionProcessor(Socket s) throws Throwable
	{
		clientSocket = s;
		this.inputStream = s.getInputStream();
		this.outputStream = s.getOutputStream();
	}
	
	@Override
	public void run()  
	{
		try
		{
			readInputHeader();
			writeResponse("<html><body><h1>Hello there!!!</h1></body></html>");
		} catch (Throwable t)
		{}
		finally
		{
			try
			{
				clientSocket.close();
			} catch(Throwable t)
			{}
		}
	}
	public void writeResponse(String response)
	{
		String result = "HTTP/1.1 200 OK\r\n" +
						"Server: Test 1.01\r\n" +
						"Content-Type: text/html\r\n" +
						"Content-Length: " + response.length() + "\r\n" +
						"Connection: close\r\n\r\n";
		try
		{
			this.outputStream.write((result + response).getBytes());
			this.outputStream.flush();
		} catch(Throwable t)
		{}
	}
	
	public void readInputHeader() throws Throwable
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(this.inputStream));
		while(true)
		{
			String s = br.readLine();
            if(s == null || s.trim().length() == 0) 
            {
                break;
            }
		}
	}

	public void close() {
		try{
			this.inputStream.close();
			this.outputStream.close();
			this.clientSocket.close();
		} catch(Exception e) {
			
		}
	}
}	