package application;
	
import loaders.LoadingPictures;
import mediator.GUIMediator;
import mediator.Go;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application 
{
	private GUIMediator mediator = GUIMediator.getInstance();
	private LoadingPictures loadingPictures = LoadingPictures.getInstance();
	
	@Override
	public void start(Stage primaryStage)
	{
		mediator.setStage(primaryStage);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent event) 
		    {
		        try 
		        {
		        	Platform.exit();
					stop();
				} catch (Exception e) 
		        {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
		mediator.handle(Go.MAIN);
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
}
