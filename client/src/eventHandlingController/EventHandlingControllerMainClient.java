package eventHandlingController;

import java.net.Socket;

import controller.NetworkControllerClient;
import entities.User;
import state.State;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
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

public class EventHandlingControllerMainClient
{
	private static EventHandlingControllerMainClient instance;
	public EventHandlingControllerMainClient()
	{
		//dont handle reflection here - the FXML loader uses reflection to load data into this object
		//atleast the documentation mentions smt. about reflection.
	}
	
	public static EventHandlingControllerMainClient getInstance()
	{
		if(instance == null)
		{
			instance = new EventHandlingControllerMainClient();
		}
		return instance;
	}
	//fxid :"userColumn"
	@FXML
	private TableColumn<User, User> userColumn;
	
	//fxid :"onlineColumn"
	@FXML
	private TableColumn<User, User> onlineColumn;
	
	@FXML
	private TextArea writeChatArea;
	
	@FXML
	private AnchorPane mainAnchorPane;
	
	@FXML
	private Button sendButton;
	
	@FXML
	private TextArea collectiveTextArea;
	
	@FXML
	private ScrollPane textAreaPane;

	@FXML
	private ScrollPane writeChatPane;
	
	//fxid :"userTable"
	@FXML
	private TableView<User> userTable;
	
	
	
	@FXML
	private Label collectiveLabel;
	
	@FXML
	private Label chatLabel;
	
	@FXML
	private MenuItem connectItem;
	
	@FXML
	private MenuItem exitItem;
	
	@FXML
	private GridPane backgroundPane;
	
	private final String ZERO = "";
	private NetworkControllerClient networkController = NetworkControllerClient.getInstance();
	
	private ObservableList<User> userList;

	private Socket connectionSocket;
	private State state  = State.getInstance();
	private final String SEPERATOR = System.getProperty("line.separator");  
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize()
	{
		instance = this;
		ableToType(true);
		
		sendButton.setOnAction((event) -> 
		{
			try 
			{
				String message = writeChatArea.getText();
				if(message != null)
				{
					networkController.sendMessage(writeChatArea.getText());
				}
				else
				{
					networkController.sendMessage("the message was null/or possibly the field that it came from");
				}
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
			writeChatArea.setText(ZERO);
		});	
		 userColumn.setCellValueFactory(new PropertyValueFactory<User, User>("User Name"));
			
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
								//since the boolean is Boolean, it could possibly be null...
								//or as renderers so often do, continue their weird behavior despite no
								//objects.
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
		//åbenbart gør den her metode forskellen. men hvad den lige gør er jeg ikke helt sikker på :)
		//men det er tydeligvis en anden slags cell factory der bliver brugt. så de må komplimentere hinanden.
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
		
		connectItem.setOnAction((event) ->
		{
			try 
			{
				connect();
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
			
		}); 
		
	}
	
	public void updateUserList() 
	{
		Platform.runLater(new Runnable() 
		{
            @Override public void run() 
            {
            	userList = FXCollections.observableArrayList(state.getUserList());
            	userTable.setItems(userList);
            }
        });	
	}	
	
	public ObservableList<User> getData() {
		return userList;
	}
	
	public TableView getUserTable() {
		return userTable;
	}

	public void setUserTable(TableView userTable) {
		this.userTable = userTable;
	}

	private void connect() throws Exception
	{
		networkController.connectToServer();
	}

	private void ableToType(boolean bool) 
	{
		writeChatArea.setEditable(bool);
	}

	public void showMessage(String message) 
	{
		Platform.runLater(new Runnable() 
		{
            @Override public void run() 
            {
            	collectiveTextArea.appendText(SEPERATOR + message);
            }
        });	
	}
}
