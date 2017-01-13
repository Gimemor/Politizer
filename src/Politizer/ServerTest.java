package Politizer;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

 /*
  * Класс реализует точку входа на сервер 
  */
public class ServerTest
{
	private ServerSocket serverSocket;
	private ArrayList<ConnectionProcessor> connectionList;
	private boolean bRunning  = true;
	private final int portNumber = 85;
	/*
	 * Вход а приложение
	 */
	public static void main(String[] args) throws Throwable
	{
		ServerTest p = new ServerTest();
		p.exec();
		p.close();
 	}
	
	/*
	 * Конструтор
	 */
	public ServerTest()
	{
		connectionList = new ArrayList<ConnectionProcessor>();
		try
		{
			serverSocket = new ServerSocket(portNumber);
		} catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
	}

	public void close() {
		try {
			serverSocket.close();
		} catch (IOException e) {
 			e.printStackTrace();
		}
		for(int i = 0; i < connectionList.size(); i++)
		{
			connectionList.get(i).close();
		}
	}

	public void exec() throws Throwable
	{
  		Socket s = null;
  		while(bRunning)
  		{
			try {
				s = serverSocket.accept();
			} catch (IOException e) {
	 			e.printStackTrace();
			}
			ConnectionProcessor connection = new ConnectionProcessor(s);
			connectionList.add(connection);
			new Thread(connection).start();
  		}
	}
	
	public void Finalize()
	{
		this.close();		
	}
}

