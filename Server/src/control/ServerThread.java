package control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import eventHandlingController.State;
import entities.ArrayListWrapper;
import entities.MessageWrapper;
import entities.User;
import entities.UserThread;
import eventHandlingController.EventHandlingControllerMain;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class ServerThread implements Runnable, Observer    
{
	private ObjectOutputStream objectOutput;
	private ObjectInputStream objectInput;
	private static EventHandlingControllerMain ehcMain;
	private final String SPACE = " ";
	private State state = State.getInstance();
	private ArrayListWrapper clientWrapper = ArrayListWrapper.getInstance();
	private MessageWrapper messageWrapper = MessageWrapper.getInstance();
	private Socket socket;
	
	
	public ServerThread(Socket socket, Observable userObs, Observable messageObs)throws Exception
	{
		userObs.addObserver(this);
		messageObs.addObserver(this);
		setupStreams(socket);
	}

	@Override
	public void run() 
	{	
		try 
		{
			whileChatting();
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	private void whileChatting() throws Exception
	{
		Task<Void> task = new Task<Void>() 
		{
			@Override
			public void run() 
			{
				try 
				{
					ehcMain = EventHandlingControllerMain.getInstance();
					/**
					 * Den her bracket kode er ikke lige vupti-doo, vi m� nok hellere implementere noget som tjekker p�
					 * instanceof user / String og s� g�re noget med hvad end der er modtaget iforhold til det tjek.
					 * men p� den anden siden s� f�r vi vel kun user informationen i den her bracket kode 1 gang, s� 1 tjek hver gang
					 * er lidt et overkill .... m�ske burde vi tage imod userObjektet et andet sted, eller m�ske er det her okay. 
					 */
					User user = (User) objectInput.readObject(); 
	        		objectOutput.writeObject(clientWrapper.getUserList());
	        		objectOutput.flush();
	        		System.out.println("Listen n�r vi har sendt den:" + clientWrapper.getUserList());
	        		clientWrapper.addUser(user);
					ehcMain.getData().add(user);
					
					/**
					 * 
						this while(true) is suppose to be doWhile loops i think, checking on something.
						but there is other avenues of this that we'll explore first. such as
						the client sending a userObject with isOnline(false) and then we'll just remove it,
						and terminate the connection.    this will have to be done with a logoutButton + 
						shutdown hook- so we make sure that we get the UserObject, this will not work 
						if the clients internet get hanged. so in that case we would have to find an actual
						way for the server to check in the socket somehow, but all of the methods for this
						seems to have some kind of error
					*/
					Boolean b = true;
					while (b)
					{
						try
						{
							messageWrapper.setMessage((String) objectInput.readObject());
							ehcMain.showMessage(messageWrapper.getMessage());
						}
						catch(Exception e)
						{
							clientWrapper.removeUser(user);
							ehcMain.getData().remove(user);
							b = false;
						}
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
	
	private void setupStreams(Socket socket) throws Exception
	{
		objectOutput = new ObjectOutputStream(socket.getOutputStream());
		objectInput = new ObjectInputStream(socket.getInputStream());
		this.socket = socket;
	}
	/**
	 * Vi mangler stadig at implementere et message system, s� vi har brug for en ny wrapper klasse til beskeder.
	 * M� lige t�nke over hvordan det skal implementeres. 
	 */
	@Override
	public void update(Observable observer, Object objectToSend) 
	{	
		if(observer instanceof ArrayListWrapper)
		{
				try
				{
					System.out.println("Sending User stuff:" + ((User) objectToSend).isOnline());
					objectOutput.writeObject(objectToSend);
					objectOutput.flush();
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
		}
		else if(observer instanceof MessageWrapper)
		{
				System.out.println("Sending Message stuff:" + (String)objectToSend);
				try
				{
					objectOutput.writeObject(objectToSend);
					objectOutput.flush();	
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
		}
	}
}
