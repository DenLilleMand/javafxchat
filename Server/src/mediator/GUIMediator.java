package mediator;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class GUIMediator 
{
	private Scene mainScene;
	private Stage stage;
	private static GUIMediator instance;
	
	private GUIMediator()
	{
		
	}
	
	public void setStage(Stage stage)
	{
		this.stage = stage;
	}
	
	public static GUIMediator getInstance()
	{
		if(instance == null)
		{
			instance = new GUIMediator();
		}
		return instance;
	}
	
	public void handle(Go login) 
	{
			switch(login)
			{
			case MAIN:
				try
				{
					stage.setTitle("MAIN");
					AnchorPane mainPage =  FXMLLoader.load(GUIMediator.class.getResource("/fxml/serverfxml.fxml"));
					mainScene = new Scene(mainPage);
					stage.setScene(mainScene);
					stage.show();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				break;
			}
		
	}
	
	
	
	
}
