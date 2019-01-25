package client.mainmenu;

import java.net.URL;
import java.util.ResourceBundle;

import client.WindowFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Network manager main menu controller.
 * 
 */
public class NetworkManagerMainMenu extends NetworkWorkerMainMenu {


	/**
	 * View account info
	 */
	@FXML
	@Override
	protected void viewInfoClick(ActionEvent event) {
		super.viewInfoClick(event);
		WindowFactory.mStage.setTitle("Network Manager info");
	}
	
	/**
	 * parse message from server
	 */
	@Override
	public void handleMessageFromServer(Object message) {
		super.handleMessageFromServer(message);
	}
	
	/**
	 * get network manager data from the server
	 */
	@Override
	public void fetchDataFromServer() {
		super.fetchDataFromServer();
		WindowFactory.mStage.setTitle("Network Manager main menu");
	}

	/**
	 * Initialize NetworkManagerMainMenu
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		mMenuFxmlPath = "/client/mainmenu/network_manager_main_menu.fxml";	
	}
	
	/**
	 * View ZerLi's quarter reports
	 * 
	 * @param event button clicked
	 */
    @FXML
    private void viewReports(ActionEvent event) {
    	WindowFactory.show("/client/reports/report_selector.fxml", mUser);
    }

}	// NetworkManagerMainMenu
