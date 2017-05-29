package Politizer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.*;


// По http приходит колбэк по новому событию
// Лексичиский анализатор и реализация модели решают как отреагировать на сообщение
// Через конекшн процессор и реализацию вк апи отсылается ответ что делать

// Когда приходит вызов колбэка нужно например прокоменнтить

/*
 * Класс отвечает за взаимодействие с соединениями 
 */
public class ConnectionProcessor implements Runnable
{
	private Socket _clientSocket;
	private InputStream _inputStream;
	private OutputStream _outputStream;
	//Логгер
	private static Logger _logger = Logger.getLogger(ConnectionProcessor.class.getName());
	private boolean _active = true;
	
	public ConnectionProcessor(Socket s) throws Throwable
	{
		this._clientSocket = s;
		this._inputStream = s.getInputStream();
		this._outputStream = s.getOutputStream();
	}
	
	@Override
	public void run()  
	{
		try
		{
			_active = true;
 			HttpRequest request = readInput();
			System.out.println(request.getMessage());
			// Здесь нужно решить что делать с сообщением, для этого нужно запустить 
			// парсер
 			writeResponse("ok");
		} catch (Throwable t)
		{
			_logger.log(Level.SEVERE, "Connection processing failed: " + t.getMessage(), t);
		}
		finally
		{
			try
			{
				_inputStream.close();
				_outputStream.close();
				_clientSocket.close();
			} catch(Throwable t)
			{
				_logger.log(
						Level.SEVERE, 
						"Client socket close failed: " + t.getMessage(),
						t
				);				
			}
			_active = false;
		}
	}
	
	public boolean IsActive()
	{ return this._active; }
	public Socket GetSocket()
	{ return this._clientSocket; }
	
	public void writeResponse(String response)
	{
		String result = "HTTP/1.1 200 OK\r\n" +
						"Server: Test 1.01\r\n" +
						"Content-Type: text/html\r\n" +
						"Content-Length: " + response.length() + "\r\n" +
						"Connection: close\r\n\r\n";
		try
		{
			_outputStream.write((result + response).getBytes());
			_outputStream.write("\r\n".getBytes());
			_outputStream.flush();
		} catch(Throwable t)
		{}
		_logger.info("Response was sended");
	}
	
	public HttpRequest readInput() throws Throwable
	{
		String s = "";
		HttpRequest httpRequest = new HttpRequest();
		BufferedReader reader = new BufferedReader(new InputStreamReader(_inputStream));
		// Читаем request line
		s = reader.readLine();
		httpRequest.setRequestLine(s);
		// Читаем параметры
		while((s = reader.readLine().trim()).length() > 0)
		{
			httpRequest.addHeaderParameter(s);
		}
 		try
		{
 			int contentLength = Integer.parseInt(
 					httpRequest.getParameter("Content-Length")
 			);
 			char []cbuf = new char[contentLength + 1];
 			reader.read(cbuf, 0, contentLength);
 			s = String.copyValueOf(cbuf);
  			httpRequest.setMessage(s);
		} catch (Exception e) {
			_logger.info("Request parsing has been failed: " + e.getMessage());
		}
  		return httpRequest;
	}

	public void close() {
			try{
				this._inputStream.close();
				this._outputStream.close();
				this._clientSocket.close();
			} catch(Exception e) {
			
		}
	}
}	