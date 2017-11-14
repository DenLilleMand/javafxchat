package eventHandlingController;

import java.net.Socket;

import control.NetworkController;
import entities.User;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import loaders.LoadingPictures;

public class EventHandlingControllerMain
{	
	//fxid :"userColumn"
	@FXML
	private TableColumn<User, User> userColumn;
		
	//fxid :"onlineColumn"
	@FXML
	private TableColumn<User, User> onlineColumn;
		
	//fx:id="mainAnchorPane"
	@FXML
	private AnchorPane mainAnchorPane;
	
	//collectiveTextArea
	@FXML
	private TextArea collectiveTextArea;
	
	// fx:id="scrollPane"
	@FXML
	private ScrollPane scrollPane;
	
	//fx:id="userLabel"
	@FXML
	private Label userLabel;
	
	
	// fx:id = "userTable"
	@FXML
	private TableView<User> userTable;
	
	//fx:id="startButton" 
	@FXML
	private Button startButton;
	
	// fx:id="collectiveLabel
	@FXML
	private Label collectiveLabel;
	
	//fx:id="backgroundPane"
	@FXML
	private GridPane backgroundPane;
	
	private ObservableList<User> data;
	
	
	public ObservableList<User> getData() 
	{
		return data;
	}

	private final String ZERO = "";
	private NetworkController networkController = NetworkController.getInstance();
	private LoadingPictures loadingPictures = LoadingPictures.getInstance();
	private Socket connectionSocket;
	private State state = State.getInstance();
	private final String SEPERATOR = System.getProperty("line.separator");  
	
	private static EventHandlingControllerMain instance;
	public EventHandlingControllerMain()
	{
		//dont handle reflection here - the FXML loader uses reflection to load data into this object
		//atleast the documentation mentions smt. about reflection.
	}
	
	public static EventHandlingControllerMain getInstance()
	{
		if(instance == null)
		{
			instance = new EventHandlingControllerMain();
		}
		return instance;
	}
	
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize()
	{
		instance = this;
		//http://docs.oracle.com/javafx/2/ui_controls/table-view.htm 
		//evt. søg på hvordan man tilføjer flere forskellige typer til det samme table.
		//SelectionModel and FocusModel er vigtige her tror jeg.
		 
		
		
		
		 // i et seriøst stykke kode ville man nok lave den der CallBack klasse som en component
		 //som bare extender CallBack, sådan at vores initialize() metode ikke bliver spammet
		 //med table relaterede ting.
		onlineColumn.setCellValueFactory(new Callback<CellDataFeatures<User, User>, ObservableValue<User>>() 
		{
			@Override
			public ObservableValue<User> call(CellDataFeatures<User, User> features) 
			{
				return new ReadOnlyObjectWrapper<User>(features.getValue());
			}
		});
		
		onlineColumn.setCellFactory(new Callback<TableColumn<User, User>, TableCell<User, User>>() 
		{
			@Override
			public TableCell<User, User> call(TableColumn<User, User> onlineColumn) 
			{
				return new TableCell<User, User>() 
				{
					final ImageView labelGraphic = new ImageView();
					final Label label = new Label();
					{
						label.setGraphic(labelGraphic);
						label.setMinWidth(130);
					}

					@Override
					public void updateItem(final User user, boolean empty) 
					{
						super.updateItem(user, empty);
						if (user != null) 
						{
							if(user.isOnline())
							{
								label.setText(ZERO);
								labelGraphic.setImage(LoadingPictures.getCheckedImage());
							}
							else if(!user.isOnline())
							{
								label.setText(ZERO);
								labelGraphic.setImage(LoadingPictures.getUncheckedImage());
							}		
						}
						else
						{
							labelGraphic.setImage(null);
							label.setText(null);
						}
						setGraphic(label);
					} 
				};
			}
		});
		userColumn.setCellValueFactory(new Callback<CellDataFeatures<User, User>, ObservableValue<User>>() 
		{
					@Override
					public ObservableValue<User> call(CellDataFeatures<User, User> features) 
					{
						return new ReadOnlyObjectWrapper<User>(features.getValue());
					}
		});
				
		 userColumn.setCellFactory(new Callback<TableColumn<User, User>, TableCell<User, User>>() 
		{
					@Override
					public TableCell<User, User> call(TableColumn<User, User> userColumn) 
					{
						return new TableCell<User, User>() 
						{
							final ImageView labelGraphic = new ImageView();
							final Label label = new Label();
							{
								label.setGraphic(labelGraphic);
								label.setMinWidth(50);
							}

							@Override
							public void updateItem(final User user, boolean empty) 
							{
								super.updateItem(user, empty);
								if (user != null) 
								{
									label.setText(user.getUserName());
								}
								else
								{
									labelGraphic.setImage(null);
									label.setText(null);
								}
								setGraphic(label);
							} 
						};
					}
				});
		
		updateUserList();
		
		startButton.setOnAction((event) ->
		{
			try 
			{
				networkController.startRunning();
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		});
	}
	//ikke helt sikker på hvordan den her metode skal se ud, hvis det er clients der skal kunne tale med hinanden
	public void showMessage(String string) 
	{
		Platform.runLater(new Runnable() 
		{
            @Override public void run() 
            {
            	collectiveTextArea.appendText(SEPERATOR + string);
            }
        });	
	}

	public void updateUserList() 
	{
		Platform.runLater(new Runnable() 
		{
            @Override public void run() 
            {
            	data = FXCollections.observableArrayList(state.userList);
            	userTable.setItems(data);
            }
        });	
	}	
}
