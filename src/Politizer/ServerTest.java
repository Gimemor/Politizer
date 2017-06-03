package Politizer;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.*;

 /*
  * Класс реализует точку входа на сервер 
  */
public class ServerTest
{
	private ServerSocket serverSocket;
	private ArrayList<ConnectionProcessor> connectionList;
	private boolean bRunning  = true;
	private final int portNumber = 80;
 	//Логгер
	private static Logger _logger = Logger.getLogger(ServerTest.class.getName());
	
	/*
	 * Вход а приложение
	 */
	public static void main(String[] args) throws Throwable
	{
		ServerTest p = new ServerTest();
		p.Exec();
		p.Close();
 	}
	
	/*
	 * Конструктор
	 */
	public ServerTest()
	{
		connectionList = new ArrayList<ConnectionProcessor>();
		_logger.info("Server Initialization...");;
		try
		{
			serverSocket = new ServerSocket(portNumber);
		} catch(IOException e)
		{
  			e.printStackTrace();
 			_logger.log(
 					Level.SEVERE, 
 					"Unable to retrieve server socket: " + e.getMessage(),
 					e
 			);
		}
	}

	public void Close()
	{
		_logger.info("Server shutdown");
		try {
			serverSocket.close();
		} catch (IOException e) {
 			e.printStackTrace();
 			_logger.log(
 					Level.SEVERE, 
 					"Unable to close server socket: " + e.getMessage(),
 					e
 			);
		}
		for(int i = 0; i < connectionList.size(); i++)
		{
			connectionList.get(i).close();
		}
	}

	public void Exec() throws Throwable
	{
  		Socket s = null;
  		while(bRunning)
  		{
 			try {
				s = serverSocket.accept();
			} catch (IOException e) {
	 			e.printStackTrace();
	 			_logger.log(
	 					Level.SEVERE, 
	 					"Unable to retrieve socket: " + e.getMessage(),
	 					e
	 			);
			}
			_logger.info("Connection accepted");
 			ConnectionProcessor connection = new ConnectionProcessor(s);
			connectionList.add(connection);
			try
			{
 				new Thread(connection).run();
 			}
			catch(Exception e)
			{
				_logger.log(
	 					Level.SEVERE, 
	 					"Process execution error: " + e.getMessage(),
	 					e
	 			);				
			}
   		}
	}
	
	public void Finalize()
	{
		this.Close();		
	}
}

