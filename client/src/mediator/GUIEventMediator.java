package mediator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import state.State;
import entities.User;
import eventHandlingController.*;

public class GUIEventMediator
{
	private static GUIEventMediator instance;
	private EventHandlingControllerMainClient ehcMain;
	private EventHandlingControllerLogin ehcLogin;
	private State state = State.getInstance();
	private ObservableList<User> observableUserList;
	
	private GUIEventMediator()
	{
		ehcMain = EventHandlingControllerMainClient.getInstance();
		ehcLogin = EventHandlingControllerLogin.getInstance();
	}
	
	public static GUIEventMediator getInstance()
	{
		if(instance == null)
		{
			instance = new GUIEventMediator();
		}
		return instance;
	}
	
	//ehcMain:
	public void updateCollectiveChat(String message)
	{
		ehcMain.showMessage(message);
	}
	
	public void updateUserList()
	{
		observableUserList = FXCollections.observableArrayList(state.getUserList());
		ehcMain.getUserTable().setItems(observableUserList);
	}
	
	//ehcLogin:
	
	
	
	
	
	
	
}
