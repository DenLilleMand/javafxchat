package application;
	
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.NetworkControllerClient;
import mediator.Go;
import mediator.GUIMediatorClient;
import state.State;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainClient extends Application 
{
	private GUIMediatorClient guiMediator = GUIMediatorClient.getInstance();
	private State state = State.getInstance();
	private NetworkControllerClient networkControllerClient = NetworkControllerClient.getInstance();
	
	@Override
	public void start(Stage primaryStage) 
	{
		guiMediator.setStage(primaryStage);
		/**
		 * herpderp - virker til at man kun skal sætte den her på primary Stage. 
		 * fordi vi bliver ved med at genbruge det samme Stage Object, anyway 
		 * det vil selF ændrer sig når man begynder at bruge JavaFX's version af 
		 */
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() 
		{
		    @Override
		    public void handle(WindowEvent event) 
		    {
		        try 
		        {
		        	networkControllerClient.disconnect();
		        	Platform.exit();
					stop();
				} catch (Exception e) 
		        {
					e.printStackTrace();
				}
		    }
		});
		try 
		{
			guiMediator.handle(Go.LOGIN);
		} catch(Exception e) 
		{
			 Logger.getLogger(MainClient.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
}
