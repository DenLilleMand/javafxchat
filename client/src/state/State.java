package state;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import entities.User;

//kunne overveje at ligge state klasse ind sammen med eventHandlingController(s) - sådan at man tager højde
//for package scope.
public class State 
{
	protected static State state;
	protected User user;
	private ArrayList<User> userList = new ArrayList<User>();
	
	public ArrayList<User> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<User> userList) {
		this.userList = userList;
	}

	private State()
	{
		
	}
	
	public static State getInstance()
	{
		if(state == null)
		{
			state = new State();
		}
		return state;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
