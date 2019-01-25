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
import common.users.AUser;
import common.users.Worker;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * A class to define common behavior for customer and worker
 * info changer controller class.
 * 
 */
public abstract class AInfoChanger implements IController, IClient, Initializable {
	protected Worker mChangingWorker;	// the worker who's changing the details..
	protected String previousFXMLpath;	// previous fxml view
	protected String mViewedUserID;		// the viewed user ID
	protected AUser mViewedUser;		// the viewed user object
	
	@FXML
	protected VBox mUserVBox;			
    @FXML
    protected Button mSubmitButton, mUnfreezeButton, mFreezeButton;
    @FXML
    protected VBox mProgressBox;
    @FXML
    protected Label mUserIDLabel;
    @FXML
	protected ComboBox<String> mStoreSelector;
    @FXML
    protected TextField mNameTextField;
    @FXML
    protected Label mUserTypeLabel;

	/**
	 * Initialization function
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		mUserIDLabel.setText("");
		if (mUserTypeLabel != null)
			mUserTypeLabel.setText("");
		mUserVBox.setVisible(false);
		mSubmitButton.setDisable(true);
	}
    
	/**
	 * on back button click.
	 * @param event button clicked.
	 */
    @FXML
    protected void backButtonBlicked(ActionEvent event) {
    	WindowFactory.show(previousFXMLpath, mChangingWorker.getUserID());
    }
    
    /**
     * get objects from previous IControlelr
     */
	@Override
	public void setDetails(Object... objects) {
		mViewedUserID = (String) objects[0];
		mUserIDLabel.setText(mViewedUserID);
		previousFXMLpath = (String) objects[1];
		mChangingWorker = (Worker)objects[2];
		if (!mChangingWorker.getWorkerType().equalsIgnoreCase("systemManager")){
			mUnfreezeButton.setVisible(false);
			mFreezeButton.setVisible(false);
		}
	}
	
	/**
	 * On submit click
	 * @param event button clicked
	 */
	@FXML
   	protected abstract void submit(ActionEvent event);
	
	/**
	 * Parse message from server
	 */
	@Override
	public void handleMessageFromServer(Object message) {
		Platform.runLater(() -> mProgressBox.setVisible(false));
		System.out.println(">Answer from server: "+ message);
		if (message instanceof String) {			// occurs when server has to update only.
			if (((String)message).equals("no results found") || ((String)message).equals("updated")) {	// update was ok
					Platform.runLater(() -> WindowFactory.showInfoDialog("user info updated",
							null, "User information successfully updated"));
					return;
			}
			if (message.equals("froze") || message.equals("unfroze")) {
				Platform.runLater(() -> WindowFactory.showInfoDialog(message.toString()+" user", null,
						"User succsefully "+message.toString()+"n."));
				return;
			}
		}
	}


	/**
	 * check if a text field is empty
	 * @param textField get data
	 * @param textFieldName get Name
	 * @return true if empty
	 */
	protected static boolean isEmptyTextBox(TextField textField, String textFieldName) {
    	if (textField.getText() == null || textField.getText().isEmpty()) {
    		WindowFactory.showErrorDialog(textFieldName+" cannot be empty", null, "please fill "+textFieldName+".");
    		return true;
    	}
		return false;
	}
	
    /**
     * Freeze / Unfreeze user account
     * 
     * @param eq freeze / unfreeze query enum
     */
    private void accountFreezing(EQueryOption eq) { 	
    	ArrayList<Object> params = new ArrayList<Object>();
    	params.add(mViewedUserID);
    	mProgressBox.setVisible(true);
    	ClientToServerMessage ctsm = new ClientToServerMessage(eq, params, null);
    	(new ConnectionController(this)).sendToServer(ctsm);
    }
	
    /**
     * Freeze a user's account.
     * @param event button clicked.
     */
    @FXML
    private void freezeAccount(ActionEvent event) {
    	accountFreezing(EQueryOption.FREEZE_USER_ACCOUNT);
    }

    /**
     * Unfreeze a user's account.
     * @param event button clicked.
     */
    @FXML
    private void unfreezeAccount(ActionEvent event) {
    	accountFreezing(EQueryOption.UNFREEZE_USER_ACCOUNT);
    }
	
}	// AInfoChanger
