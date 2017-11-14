package eventHandlingController;



import entities.User;
import state.State;
import mediator.Go;
import mediator.GUIMediatorClient;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;


public class EventHandlingControllerLogin
{
	private static EventHandlingControllerLogin instance;
	public EventHandlingControllerLogin()
	{
		//dont handle reflection here - the FXML loader uses reflection to load data into this object
		//atleast the documentation mentions smt. about reflection.
	}
	
	public static EventHandlingControllerLogin getInstance()
	{
		if(instance == null)
		{
			instance = new EventHandlingControllerLogin();
		}
		return instance;
	}
	
	
	@FXML
	private AnchorPane mainPane;
	
	
	@FXML
	private TextField userTextField;
	
	private GUIMediatorClient mediator;
	private State state = State.getInstance();
	private int id = 0;
	
	@FXML
	private void initialize()
	{
		instance = this;
		mediator = GUIMediatorClient.getInstance();
		userTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
               if(ke.getCode() == KeyCode.ENTER)
               {
            	   state.setUser(new User(userTextField.getText(),true, id++));
            	   mediator.handle(Go.MAIN);
               }
            }
        });
	}
	
	
	
}