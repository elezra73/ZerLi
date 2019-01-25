package client.user;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ConnectionController;
import client.IController;
import client.WindowFactory;
import common.ClientToServerMessage;
import common.EQueryOption;
import common.IClient;
import common.users.UserLoginInfo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * System user login screen controller.
 * Validates username and password and redirects by user type
 * to suitable Main Menu.
 *
 */
public class UserController implements IClient, IController, Initializable {
    @FXML
    private VBox mProgressBox;
    @FXML
    private TextField mUsernameTextField;
    @FXML
    private TextField mPasswordTextField;
    @FXML
    private Label mConnectionStatusLabel;
    private ConnectionController mConnectionController;
	private static String mConnectedUserID = null;
    ///
    
    /**
     * exit the application
     * @param event button clicked
     */
    @FXML
    void exitApplication(ActionEvent event) {
		 Platform.exit();
	     System.exit(0);
    }

	
	/**
	 * Tries to connect to server.
	 * @param event button clicked
	 */
	@FXML
	public void createNewConnection(ActionEvent event) {
		if (event == null)
			mConnectionController = new ConnectionController(this);
		else
			mConnectionController = new ConnectionController(this, mConnectionStatusLabel);
		if (!mConnectionController.isConnected())
			mProgressBox.setVisible(false);
	}

	/**
	 * Open settings.
	 * @param event button clicked
	 */
	@FXML
	private void onSettingsClick(ActionEvent event) {
		WindowFactory.show("/client/settings/settings_view.fxml");
	}


	/**
	 * Login button pressed. validate login info.
	 * @param event login button pressed.
	 */
    @FXML
    void submitLogin(ActionEvent event) {
    	String username = mUsernameTextField.getText();
    	String password = mPasswordTextField.getText();
    	if (username.isEmpty() || password.isEmpty()) {
    		WindowFactory.showErrorDialog("Invalid username or password", 
    				"Cannot be empty", "Please check your info");
    		return;
    	}
    	mProgressBox.setVisible(true);
    	createNewConnection(null);
    	ArrayList<Object> params = new ArrayList<Object>();
    	params.add(username);
    	params.add(password);
    	ClientToServerMessage clientToServerMessage = new ClientToServerMessage(EQueryOption.LOGIN_REQUEST,
    			params,"UserLoginInfo");
    	mConnectedUserID = username;
    	mConnectionController.sendToServer(clientToServerMessage);
    }
    
    /**
     * parse message from the server
     */
	@Override
	public void handleMessageFromServer(Object message) {
		System.out.println(">Answer from server: "+message);
		mProgressBox.setVisible(false);
		if (message instanceof String) {
			Platform.runLater(() -> WindowFactory.showErrorDialog("Login error", "Couldn't login to the system", 
					(String)message));
			return;
		}
		
		String mainMenuPath;
		UserLoginInfo loginInfo = (UserLoginInfo)message;
		switch (loginInfo.getAccountType().toLowerCase().trim()) {
			case "storemanager":
				mainMenuPath = "/client/mainmenu/store_manager_main_menu.fxml";
				break;
			case "storeworker":
				mainMenuPath = "/client/mainmenu/store_worker_main_menu.fxml";
				break;
			case "systemmanager":
				mainMenuPath = "/client/mainmenu/system_manager_main_menu.fxml";
				break;
			case "networkmanager":
				mainMenuPath = "/client/mainmenu/network_manager_main_menu.fxml";
				break;
			case "networkworker":
				mainMenuPath = "/client/mainmenu/network_worker_main_menu.fxml";
				break;
			case "customerservice":
			case "customerserviceworker":
				mainMenuPath = "/client/mainmenu/customer_service_main_menu.fxml";
				break;
			case "customer":
				mainMenuPath = "/client/mainmenu/customer_main_menu.fxml";
				break;
			default:
			//TODO: add error
				System.out.println("Account type doesnt exist: "+loginInfo.getAccountType());
				return;
		}
		// Use Lambda expression to update stage with some user main menu.
		Platform.runLater(() -> WindowFactory.show(mainMenuPath, mConnectedUserID));
	}
	
	/**
	 * initialize the view
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		WindowFactory.mStage.setResizable(false);
		WindowFactory.mStage.setTitle("Zer-Li Login");
		mProgressBox.setVisible(false);
	}

	
	/**
	 * Send a logout request..
	 * @param client the client
	 */
	public static void sendLogOutRequest(IClient client) {
		if (mConnectedUserID == null || mConnectedUserID.isEmpty())		// ignore logout request.
			return;
		ArrayList<Object> params = new ArrayList<Object>();
    	params.add(mConnectedUserID);
    	ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.LOGOUT_REQUEST, params, null);
    	try {
    		new ConnectionController(client).sendToServer(msg);
    		mConnectedUserID = "";
		} catch (Exception e) {
			System.err.println(e);
			System.err.println("Failed telling the server user logged out");
		}
	}

	/**
	 * @return the current logged in user's ID.
	 */
	public static String getConnectedUserID() {
		return mConnectedUserID;
	}

	@Override
	public void setDetails(Object... objects) {}	// not used

}
