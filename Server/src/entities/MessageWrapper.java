package entities;

import java.util.Observable;

public class MessageWrapper extends Observable 
{
	private String message = "";
	
	private static MessageWrapper instance;
	
	private MessageWrapper()
	{
		//handle reflection
	}
	
	public synchronized static MessageWrapper getInstance()
	{
		if(instance == null)
		{
			instance = new MessageWrapper();
		}
		return instance;
	}
	
	public synchronized String getMessage()
	{
		return message;
	}
	
	public synchronized void setMessage(String message)
	{
		this.message = message;
		measurementsChanged(message);
	}
	
	private synchronized void measurementsChanged(String message)
	{
		setChanged();
		notifyObservers(message);
	}

	
	
	
}
