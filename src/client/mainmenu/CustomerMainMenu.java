package client.mainmenu;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ConnectionController;
import client.WindowFactory;
import common.ClientToServerMessage;
import common.EQueryOption;
import common.Store;
import common.users.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

/**
 * Customer's main menu controller.
 * 
 */
public class CustomerMainMenu extends AMainMenu {
    @FXML
    private ComboBox<Store> mStoreSelector;		// store selector
    @FXML
    private Button mBrowseCatalogButton;		// browse catalog button
    private final static Store mGeneralStore = new Store("general", "general");	// dummy general store
    
    
	/**
	 * Self composition order.
	 * Customer can combine products and make a unique order.
	 * @param event	button clicked.
	 */
    @FXML
    private void selfOrder(ActionEvent event) {
    	WindowFactory.show("/client/catalog/self_composition.fxml" , mUser);
    }

    /**
     * Customer can browse the catalog.
     * From there, he will be able to order products from catalog.
     * @param event button clicked.
     */
    @FXML
    private void catalogBrowse(ActionEvent event) {
    	WindowFactory.show("/client/catalog/ctalogView.fxml", mUser);
    }

    /**
     * Customer may request to cancel one of his orders.
     * @param event button clicked.
     */
    @FXML
    private void cancelOrder(ActionEvent event) {
    	WindowFactory.show("/client/order/order_cancellation.fxml", mUserID);
    }
	
    /**
     * Initialize account data.
     */
	@Override
	public void fetchDataFromServer() {
		mConnectionController = new ConnectionController(this); 	
		ClientToServerMessage clientToServerMessage = new ClientToServerMessage(
				EQueryOption.GET_ALL_STORES, null, "stores");
		mConnectionController.sendToServer(clientToServerMessage);	
	}

	/**
	 * Initialize the customer menu
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		WindowFactory.mStage.setTitle("Customer main menu");
		mMenuFxmlPath = "/client/mainmenu/customer_main_menu.fxml";	
		mStoreSelector.setPromptText("Select branch");
		mStoreSelector.getItems().add(mGeneralStore);
		mStoreSelector.setDisable(true);
	}


	/**
	 * parse the message from the server
	 */
	@Override
	public void handleMessageFromServer(Object message) {
		super.handleMessageFromServer(message);
		if (message instanceof String) {
			mUser = new Customer(mUserID, mStoreSelector.getValue().getStoreID());
			if (message.toString().equalsIgnoreCase("not registered")){
				mBrowseCatalogButton.setVisible(true);
				mButtonsGroup.setVisible(false);
			}
		}
		if (message instanceof ArrayList<?> && 
				((ArrayList<?>) message).get(0) instanceof Store) {
			@SuppressWarnings("unchecked")
			ArrayList<Store> stores = (ArrayList<Store>)message;
			for (Store store : stores) {
				mStoreSelector.getItems().add(store);
			}
			mStoreSelector.setDisable(false);
		}
		
	}	// handleMessage
	
	/**
	 * Change selected store
	 * @param event button clicked
	 */
    @FXML
    private void onStoreSelected(ActionEvent event) {
    	try {
    		Store selectedStore = mStoreSelector.getValue();
    		if (selectedStore.equals(mGeneralStore)) {
    			mUser = new Customer(mUserID, "general");
    			mButtonsGroup.setVisible(false);
    			mBrowseCatalogButton.setVisible(true);
    			return;
    		}
    		mButtonsGroup.setVisible(true);
			mBrowseCatalogButton.setVisible(false);
    		ArrayList<Object> params = new ArrayList<Object>();
    		params.add(mUserID);
    		params.add(selectedStore.getStoreID());
    		ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.GET_CUSTOMER_INFO, params, "customer");
    		mProgressBox.setVisible(true);
    		(new ConnectionController(this)).sendToServer(msg);
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
}	// CustomerMainMenu
