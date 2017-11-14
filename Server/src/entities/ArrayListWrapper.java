package entities;

import java.util.ArrayList;
import java.util.Observable;
/**
 * Jeg ved ikke helt om det her er en ordentlig måde at gøre det på, men jeg tror hvertfald der er 
 * nogle problemer med at intet er synkroniseret.
 * @author DenLilleMand
 *
 */
public class ArrayListWrapper extends Observable
{
	private ArrayList<User> userList = new ArrayList<User>();
	private static ArrayListWrapper instance; 
	
	private ArrayListWrapper()
	{
		//handle reflection
	}
	
	public static ArrayListWrapper getInstance()
	{
		if(instance == null)
		{
			instance = new ArrayListWrapper();
		}
		return instance;
	}

	
	public void removeUser(User user)
	{
		user.setOnline(false);
		userList.remove(user);
		measurementsChanged(user);
	}
	
	public ArrayList<User> getUserList()
	{
		return userList;
	}
	
	private void measurementsChanged(User user)
	{
		setChanged();
		notifyObservers(user);
	}

	public void addUser(User user) 
	{
		user.setOnline(true); 
		userList.add(user);
		measurementsChanged(user);	
		System.out.println("Sending " + user.isOnline());
	}
}
