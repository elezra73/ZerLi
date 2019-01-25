package client.mainmenu;

import java.util.ArrayList;

import client.ConnectionController;
import client.WindowFactory;
import client.user.UserController;
import common.ClientToServerMessage;
import common.EQueryOption;
import javafx.application.Platform;

/**
 * Any worker main menu controller.
 * This class implements common logic to all worker main menus.
 * 
 *
 */
public abstract class WorkerMainMenu extends AMainMenu {

	/**
	 * Get connected user's information from the server.
	 */
	@Override
	public void fetchDataFromServer() {
		mConnectionController = new ConnectionController(this); 	
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(mUserID);
		ClientToServerMessage clientToServerMessage = new ClientToServerMessage(
				EQueryOption.GET_WORKER_INFO, params, "worker");
		mProgressBox.setVisible(true);
		mConnectionController.sendToServer(clientToServerMessage);	
	}
	
	@Override
	public void handleMessageFromServer(Object message) {
		super.handleMessageFromServer(message);
		if (message instanceof String && message.equals("Worker account is not activated")) {
			Platform.runLater(() -> WindowFactory.showErrorDialog("Login error", null, message.toString()));
			UserController.sendLogOutRequest(null);
			Platform.runLater(() -> WindowFactory.show("/client/user/user_login_view.fxml"));
			return;
		}
	}

}	// WorkerMainMenu
