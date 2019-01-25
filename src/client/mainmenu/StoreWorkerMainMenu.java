package client.mainmenu;

import java.net.URL;
import java.util.ResourceBundle;

import client.IController;
import client.WindowFactory;
import common.IClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * StoreWorker main menu controller.
 *
 */
public class StoreWorkerMainMenu extends WorkerMainMenu implements IController , IClient 
{

	public static String fxmlPath = "/client/mainmenu/store_manager_main_menu.fxml";
	private static String temp;
	
	/**
	 * View account info
	 */
	@FXML
	@Override
	protected void viewInfoClick(ActionEvent event) {
		super.viewInfoClick(event);
		WindowFactory.mStage.setTitle("Store Worker info");
	}
	
	/**
	 * Parse message from server
	 */
	@Override
	public void handleMessageFromServer(Object message) 
	{
		super.handleMessageFromServer(message);
		if (message instanceof String) {
			final String doesntExist = "Product doesn't exist";
			if (((String)message).equals(doesntExist)) {
				Platform.runLater(() -> WindowFactory.showErrorDialog(doesntExist, null, doesntExist));
				return;
			}
			if (((String)message).equals("updated")) {
				final String updated = "Product sale successfully updated";
				Platform.runLater(() -> WindowFactory.showInfoDialog(updated, null, updated));
				return;
			}
			if (((String)message).equals("no results found")) {
				final String deleted = "Product "+temp+" sale successfully deleted";
				Platform.runLater(() -> WindowFactory.showInfoDialog(deleted, null, deleted));
				return;
			}
		}
	}

	/**
	 * get store worker data from server
	 */
	@Override
	public void fetchDataFromServer() {
		super.fetchDataFromServer();
	}

	/**
	 * initialize store worker main menu
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		mMenuFxmlPath = "/client/mainmenu/store_worker_main_menu.fxml";
		WindowFactory.mStage.setTitle("Store Worker main menu");
	}
	
	/**
	 * open survey to view
	 * 
	 * @param event button clicked
	 */
	@FXML
	private void surveyPressed(ActionEvent event) {
		mProgressBox.setVisible(true);
		WindowFactory.show("/client/survey/survey_view.fxml" , mUser);
	}
	
	
}	// StoreWorkerMainMenu
