package client.mainmenu;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ConnectionController;
import client.WindowFactory;
import common.ClientToServerMessage;
import common.Complaint;
import common.EQueryOption;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Customer service main menu controller.
 * 
 */
public class CustomerServiceMainMenu extends WorkerMainMenu {
	
	/**
	 * View account info
	 */
	@FXML
	@Override
	protected void viewInfoClick(ActionEvent event) {
		super.viewInfoClick(event);
		WindowFactory.mStage.setTitle("Customer Service Worker info");
	}

	/**
	 * Submit a new customer's complaint.
	 * @param event button clicked.
	 */
    @FXML
    private void submitComplaint(ActionEvent event) {
    	WindowFactory.show("/client/customerServiceWorker/customer_service_open_complaint.fxml" , mUser );
    }

    /**
     * View a customer existing complaint.
     * Can compensate customer if needed.
     * @param event button clicked.
     */
    @FXML
    private void viewCustomerComplaints(ActionEvent event) {
    	ConnectionController mConnectiontController = new ConnectionController(this);
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(mUser.getUserID());
		ClientToServerMessage clientToServerMessage = new ClientToServerMessage(EQueryOption.GET_COMPLAINTS_BY_WORKER_ID, params, "complaints");
		mConnectiontController.sendToServer(clientToServerMessage);
    }


    /**
     * View an existing customer's survey.
     * Can attach professional review to the survey.
     * @param event button clicked.
     */
    @FXML
    private void viewCustomerSurveys(ActionEvent event)
    {
    	WindowFactory.show("/client/survey/survey_professional_review.fxml" , mUser );
    }
    
	/**
	 * Parse message from the server
	 */
	@Override
	public void handleMessageFromServer(Object message) {
		super.handleMessageFromServer(message);
		if (message instanceof ArrayList<?> && ((ArrayList<?>) message).get(0) instanceof Complaint) {
			Platform.runLater(() -> WindowFactory.show("/client/customerServiceWorker/complaints_view.fxml", message));
			}
		if (message instanceof String && message.equals("complaint doesn't exist.")) {
			Platform.runLater(() -> WindowFactory.showInfoDialog("No complaints found", null, 
					"You didn't submit any complaints"));
		}
	}

	/**
	 * Get connected worker data from server.
	 */
	@Override
	public void fetchDataFromServer() {
		super.fetchDataFromServer();
		WindowFactory.mStage.setTitle("Customer Service main menu");

	}

	/**
	 * Initialize the customer service main menu
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		mMenuFxmlPath = "/client/mainmenu/customer_service_main_menu.fxml";	
	}

}	// CustomerServiceMainMenu
