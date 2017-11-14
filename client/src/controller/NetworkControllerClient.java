package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import entities.User;
import eventHandlingController.EventHandlingControllerMainClient;
import state.State;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import mediator.GUIEventMediator;

public class NetworkControllerClient 
{
	private static NetworkControllerClient instance;
	private String serverIP = "127.0.0.1";
	private Socket connectionSocket;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	private String message = "";
	private GUIEventMediator eventMediator;
	private final String clientString = "CLIENT - ";
	private final String ZERO = "";
	private State state = State.getInstance();
	private EventHandlingControllerMainClient ehcm;


	private NetworkControllerClient()
	{
		
	}
	
	public static NetworkControllerClient getInstance()
	{
		if(instance == null)
		{
			instance = new NetworkControllerClient();
		}
		return instance;
	}
	

	private void setupStreams() throws Exception
	{	
		objectInputStream = new ObjectInputStream(connectionSocket.getInputStream());
		objectOutputStream = new ObjectOutputStream(connectionSocket.getOutputStream());
	}
	
	
	public void whileChatting() throws Exception
	{
		Task<Void> task = new Task<Void>() 
		{
			@Override
			public void run() 
			{
				try 
				{
					ehcm = EventHandlingControllerMainClient.getInstance();
					objectOutputStream.writeObject(state.getUser());
					//får en arrayliste ned, som vi så skal bruge til at lave vores
					//observable liste og give den videre til vores table.
					while(true)
					{	
						handleObject(objectInputStream.readObject());
					}
				} catch (Exception e) 
				{
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

	public void connectToServer() throws Exception
	{
		connectionSocket = null;
		try 
		{
			connectionSocket = new Socket(serverIP, 6789);
			eventMediator = GUIEventMediator.getInstance();
			setupStreams();
			whileChatting();
		} catch (IOException ex) 
		{
			System.err.println(ex);
		} finally 
		{
		}
	}
	
	private void handleObject(Object inputObject)
	{
		if(inputObject instanceof String)
		{
			Platform.runLater(new Runnable() 
			{
	             @Override public void run() 
	             {
	            	 eventMediator.updateCollectiveChat((String)inputObject);
	             }
	         });
		}
		else if(inputObject instanceof User)
		{
			User user = (User)inputObject;
			if(user.isOnline())
			{
				System.out.println("SOME1 Went Offline and aparently we're adding lol");
				ehcm.getData().add(user);
			}
			else if(!user.isOnline())
			{
				ehcm.getData().remove(user);
			}
		}
		else if(inputObject instanceof ArrayList<?>)
		{
			ArrayList<User> tempList = (ArrayList<User>) inputObject;
			for(User u : tempList)
			{
				ehcm.getData().add(u);
			}
		}
	}

	public void sendMessage(String message) throws Exception
	{
		objectOutputStream.writeObject(state.getUser().getUserName()+": "+message);
		objectOutputStream.flush();
	}
	
	public void disconnect() throws Exception
	{
		objectOutputStream.close();
		objectInputStream.close();
	}
	
}
