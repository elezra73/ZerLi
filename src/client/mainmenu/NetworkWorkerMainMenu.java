package client.mainmenu;

import java.net.URL;
import java.util.ResourceBundle;

import client.WindowFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * NetworkWorker main menu controller.
 * 
 */
public class NetworkWorkerMainMenu extends WorkerMainMenu {

	/**
	 * View account info
	 */
	@FXML
	@Override
	protected void viewInfoClick(ActionEvent event) {
		super.viewInfoClick(event);
		WindowFactory.mStage.setTitle("Nework Worker info");
	}
	
	/**
	 * Get NetworkWorker data from the server
	 */
	@Override
	public void fetchDataFromServer() {
		super.fetchDataFromServer();
		WindowFactory.mStage.setTitle("Network Worker main menu");
	}

	/**
	 * Initialize NetworkWorker main menu
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		mMenuFxmlPath = "/client/mainmenu/network_worker_main_menu.fxml";	
	}


	
}	// NetworkWorkerMainMenu
