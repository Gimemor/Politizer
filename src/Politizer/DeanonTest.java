package Politizer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

 
// Сервер принимает запрос
// Авторизуется 
// Возвращает список друзей пользователя


public class DeanonTest {
	public String token = "b5707c51f";
	private ServerSocket serverSocket;
	private Socket client;
	private BufferedReader reader = null;
	private PrintWriter    writer = null;;
	
	public DeanonTest()  throws IOException
	{
		serverSocket = new ServerSocket(4444);
		client = serverSocket.accept();
		
		reader  = new BufferedReader(new InputStreamReader(client.getInputStream()));
		writer = new PrintWriter(client.getOutputStream(),true);
		
		String in;
		while((in = reader.readLine()) != null)
		{
			if(in.equalsIgnoreCase("exit"))
			{
				break;
			}
			writer.println(in);
		}
		reader.close();
		writer.close();
		client.close();
		serverSocket.close();
	}
}
