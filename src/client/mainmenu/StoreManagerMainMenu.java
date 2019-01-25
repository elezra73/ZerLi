package client.mainmenu;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.TreeSet;

import client.ConnectionController;
import client.WindowFactory;
import common.ClientToServerMessage;
import common.EQueryOption;
import common.Product;
import common.users.AUser;
import common.users.Customer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Store manager main menu controller.
 *
 */
public class StoreManagerMainMenu extends WorkerMainMenu {
	public static String fxmlPath = "/client/mainmenu/store_manager_main_menu.fxml";
	private static Product mLastUsedProduct;
	private static Product[] mStoreProducts;
	private static Customer[] mCustomers;
	
	/**
	 * View account info
	 */
	@FXML
	@Override
	protected void viewInfoClick(ActionEvent event) {
		super.viewInfoClick(event);
		WindowFactory.mStage.setTitle("Store Manager info");
	}
	
	/**
	 * Parse message from server
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void handleMessageFromServer(Object message) {
		super.handleMessageFromServer(message);
		if (message instanceof AUser) {
			// 1st query returned here, ask 2nd query to get all stores.
			ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.GET_All_PRODUCTS, null, "catalog");
			mConnectionController = new ConnectionController(this);
			Platform.runLater(() -> mProgressBox.setVisible(true));
			mConnectionController.sendToServer(msg);
			return;
		}
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
				final String deleted = "Product "+mLastUsedProduct.getProductName()+" sale successfully deleted";
				Platform.runLater(() -> WindowFactory.showInfoDialog(deleted, null, deleted));
				return;
			}
		}
		
		if (message instanceof ArrayList<?>) {
			try {
				if (((ArrayList<?>)message).get(0) instanceof Product) {
					ArrayList<Product> products = (ArrayList<Product>) message;	//mStoreProducts
					mStoreProducts = new Product[products.size()];
					for (int i=0; i<products.size(); ++i)
						mStoreProducts[i] = products.get(i);
					ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.GET_All_CUSTOMERS, null, "customers");
					Platform.runLater(() -> mProgressBox.setVisible(true));
					(new ConnectionController(this)).sendToServer(msg);
					return;
				}
				if (((ArrayList<?>)message).get(0) instanceof Customer) {
					ArrayList<Customer> customers = (ArrayList<Customer>) message;	//mStoreProducts
					mCustomers = new Customer[customers.size()];
					for (int i=0; i<customers.size(); ++i)
						mCustomers[i] = customers.get(i);
					return;
				}
			}
			catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}
	
	
	/**
	 * Get store manager data from the server
	 */
	@Override
	public void fetchDataFromServer() {
		super.fetchDataFromServer();
		WindowFactory.mStage.setTitle("Store Manager main menu");
	}

	/**
	 * initialize StoreManagerMainMenu
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		mMenuFxmlPath = "/client/mainmenu/store_manager_main_menu.fxml";	
	}


	/**
	 * Promote a product in the catalog by adding a sale.
	 * @param event button clicked.
	 */
    @FXML
    private void promoteProduct(ActionEvent event) {    
    	Product p = WindowFactory.showComboBoxDialog(mStoreProducts);
    	if (p == null)
    		return;
    	String perStr = WindowFactory.showInputDialog("Product promotion", null, 
    			"Insert product "+p.getProductName()+" sale %: ");
    	if (perStr == null)
    		return;
    	double percent;
    	try {
    		percent = Double.parseDouble(perStr);
    		if (percent < 1 || percent > 99) {
    			WindowFactory.showErrorDialog("Invalid percentage", null, "Invalid percentage!");
    			return;
    		}
    	}
    	catch (NumberFormatException e) {
    		WindowFactory.showErrorDialog("Invalid percentage", null, "Invalid percentage!");
    		return;
    	}
    	catch(Exception e) {
    		return;
    	}
    	ArrayList<Object> params = new ArrayList<Object>();
    	params.add(mUser.getStoreID());
    	params.add(p.getProductID());
    	params.add(percent);
    	System.out.println(params);
    	ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.SET_PRODUCT_SALE_PERCENT, 
    			params, null);
    	(new ConnectionController(this)).sendToServer(msg);
    }

    /**
     * Cancel a product's promotion.
     * @param event button clicked.
     */
    @FXML
    private void cancelProductPromotion(ActionEvent event) {
    	Product p = WindowFactory.showComboBoxDialog(mStoreProducts);
    	if (p == null)
    		return;
    	ArrayList<Object> params = new ArrayList<Object>();
    	params.add(p.getProductID());
    	params.add(mUser.getStoreID());
    	ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.DELETE_PRODUCT_SALE_PERCENT, 
    			params, null);
    	mLastUsedProduct = p;
    	(new ConnectionController(this)).sendToServer(msg);
    }

    /**
     * View store's quarter reports.
     * @param event button clicked.
     */
    @FXML
    private void viewStoreReports(ActionEvent event) {
    	WindowFactory.show("/client/reports/report_selector.fxml", mUser);
    }

    /**
     * Activate a customer account (set IsPayable = true)
     * 
     * @param event button clicked
     */
    @FXML
    void activateCustomerAccount(ActionEvent event) {
    	TreeSet<String> set = new TreeSet<String>();
    	for (Customer cusId : mCustomers) {
    		set.add(cusId.getUserID());
    	}
    	String[] cusIDs = new String[set.size()];
    	int i=0;
    	for (String str : set) {
    		cusIDs[i] = str;
    		i++;
    	}
    	String customerID = WindowFactory.showComboBoxDialog(cusIDs,"Select a customer", "Select a customer");
    	if (customerID == null) return;		
    	WindowFactory.show("/client/user/customer_info_changer.fxml", customerID, fxmlPath, mUser);
    }

}	// StoreManagerMainMenu
