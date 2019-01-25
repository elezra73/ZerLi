package client.mainmenu;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import client.ConnectionController;
import client.WindowFactory;
import common.ClientToServerMessage;
import common.EQueryOption;
import common.users.AUser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * System Manager Main Menu controller.
 * 
 * System Manager can freeze/unfreeze accounts from this menu.
 * System Manager can change user's permissions from this menu.
 *
 */
public class SystemManagerMainMenu extends WorkerMainMenu {
	public static String fxmlPath = "/client/mainmenu/system_manager_main_menu.fxml";	
	private static LinkedHashMap<String, String> mAllUsers;
	
	/**
	 * parse message from server.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void handleMessageFromServer(Object message) {
		super.handleMessageFromServer(message);
		if (message instanceof AUser) {	// 1st query
			ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.GET_ALL_USERS, null, "users");
			Platform.runLater(() -> mProgressBox.setVisible(true));
			(new ConnectionController(this)).sendToServer(msg);
			return;
		}
		if (message instanceof LinkedHashMap<?, ?>) {
			mAllUsers = (LinkedHashMap<String, String>) message;
			return;
		}
	}		// handleMessageFromServer

	/**
	 * Save this user's data.
	 */
	@Override
	public void fetchDataFromServer() {
		super.fetchDataFromServer();
		WindowFactory.mStage.setTitle("System Manager main menu");
	}

	/**
	 * Initialize System Manager main menu 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		mMenuFxmlPath = "/client/mainmenu/system_manager_main_menu.fxml";	
	}

	/**
	 * View account info
	 */
	@FXML
	@Override
	protected void viewInfoClick(ActionEvent event) {
		super.viewInfoClick(event);
		WindowFactory.mStage.setTitle("System Manager info");
	}

	/**
	 * Change a user's permissions. (Account type).
	 * @param event button clicked.
	 */
    @FXML
    private void changeWorkerInfo(ActionEvent event) {
    	String[] users = new String[mAllUsers.size()];
    	int i = 0;
    	for (Entry<String, String> entry : mAllUsers.entrySet()) {
    		if (!entry.getValue().equalsIgnoreCase("customer"))
				users[i] = entry.getKey();
			else users[i] = null;
    		i++;
    	}
    	String workerID = WindowFactory.showComboBoxDialog(users, "Select a worker","Select a worker");
    	if (workerID == null) return;			
    	WindowFactory.show("/client/user/worker_info_changer.fxml", workerID , fxmlPath,
    			mUser, mAllUsers.get(workerID));
    }

    /**
     * open customer details changer
     * @param event button clicked
     */
    @FXML
    private void changeCustomerInfo(ActionEvent event) {
    	String[] users = new String[mAllUsers.size()];
    	int i = 0;
    	for (Entry<String, String> entry : mAllUsers.entrySet()) {
    		if (entry.getValue().equalsIgnoreCase("customer"))
				users[i] = entry.getKey();
			else users[i] = null;
    		i++;
    	}
    	String customerID = WindowFactory.showComboBoxDialog(users,"Select a customer", "Select a customer");
    	if (customerID == null) return;		
    	WindowFactory.show("/client/user/customer_info_changer.fxml", customerID, fxmlPath, mUser);
    }

}	// SystemManagerMainMenu
