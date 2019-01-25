package client.mainmenu;

import java.net.URL;
import java.util.ResourceBundle;
import client.ConnectionController;
import client.IController;
import client.WindowFactory;
import client.user.UserController;
import common.IClient;
import common.users.AUser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Main Menu controller
 *
 */
public abstract class AMainMenu implements IClient, Initializable, IController{
    @FXML
    protected VBox mButtonsGroup;		// Menu buttons group
    @FXML
    protected VBox mProgressBox;		// the progress idicator
    @FXML
    protected Label mHelloLabel;		// hello label
	protected String mUserID;			// the connected user id
	protected AUser mUser;				// the connected AUser
	protected ConnectionController mConnectionController;		// a connection controller
	protected String mMenuFxmlPath;		// the main menu's fxml path
	
	/**
	 * Open account info view.
	 * @param event button clicked
	 */
	@FXML
	protected void viewInfoClick(ActionEvent event) {
		String fxmlPath = "/client/user/user_info_view.fxml";
		Object[] objects = {mUser, mMenuFxmlPath};
		WindowFactory.show(fxmlPath, objects);			
	}
	

	/**
	 * Exit the application
	 * @param event button clicked
	 */
    @FXML
    protected void exitApplication(ActionEvent event) {
    	UserController.sendLogOutRequest(this);
		Platform.exit();
	    System.exit(0);
    }
	
    /**
     * Log out back to log in view.
     * @param event button clicked
     */
    @FXML
    protected void logOut(ActionEvent event) {
    	String fxmlPath = "/client/user/user_login_view.fxml";
       	UserController.sendLogOutRequest(this);
    	WindowFactory.show(fxmlPath);
 
    }



	/**
	 * Set account info inside the view.
	 */
	protected abstract void fetchDataFromServer();



	/**
	 * get objects from previous fxml IController
	 */
	@Override
	public void setDetails(Object... objects) {
		try {
			mUserID = (String)objects[0];
			fetchDataFromServer();	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize view
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		mButtonsGroup.setDisable(true);
		mHelloLabel.setVisible(false);
	}


	/**
	 * Handle the message from server
	 */
	@Override
	public void handleMessageFromServer(Object message) {
		System.out.println(">Answer from server:\n"+message);
		Platform.runLater(() -> mProgressBox.setVisible(false));
		if (message instanceof AUser) {
			mButtonsGroup.setDisable(false);
			mHelloLabel.setVisible(true);
			mUser = (AUser)message;
			Platform.runLater(() -> mHelloLabel.setText("Hello "+mUser.getName()+","));
		}
		
	}
	
	
}	// AMainMenu
