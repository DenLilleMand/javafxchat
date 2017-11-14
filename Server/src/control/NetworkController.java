package control;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.concurrent.Task;
import entities.ArrayListWrapper;
import entities.MessageWrapper;
import entities.UserThread;
import eventHandlingController.State;

public class NetworkController
{
	private static NetworkController instance;
	private ObjectOutputStream objectOutput;
	private ObjectInputStream objectInput;
	private ServerSocket serverSocketForChat;
	private ServerSocket serverSocketForUsers;
	private Socket socket;
	private final String ZERO = "";
	private String message = "";
	private State state = State.getInstance();
	private int id = 0;
	private MessageWrapper messageWrapper = MessageWrapper.getInstance();
	private ArrayListWrapper clientWrapper = ArrayListWrapper.getInstance();
	
	private NetworkController()
	{
		//handle reflection
	}
	
	public static NetworkController getInstance()
	{
		if(instance == null)
		{
			instance = new NetworkController();
		}
		return instance;
	}
	
	private void closeCrap() throws Exception
	{
		objectInput.close();
		objectOutput.close();
		socket.close();
	}
	
	public void startRunning() throws Exception
	{
		serverSocketForChat = new ServerSocket(6789, 100);//100 = hvor mange der venter p� min port
			try
			{
				Task<Void> task = new Task<Void>() 
				{
					@Override
					public void run() 
					{
						try 
						{
								while(true) 
								{
									UserThread t = new UserThread(new Thread(new ServerThread(serverSocketForChat.accept(),clientWrapper,messageWrapper)),id++);
					        		t.getThread().start();        		
					        	}
						} catch (Exception e) 
						{
							e.printStackTrace();
						}
					}
					@Override
					protected Void call() throws Exception 
					{
						return null;
					}
				};
				new Thread(task).start();
			}
			catch(Exception  eofException)
			{
				eofException.printStackTrace();
			}finally
			{
			}	
	}
}
