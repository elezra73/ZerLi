package client.customerServiceWorker;

import java.text.ParseException;
import java.util.ArrayList;
import client.ConnectionController;
import client.IController;
import client.WindowFactory;
import client.user.UserController;
import common.ClientToServerMessage;
import common.EQueryOption;
import common.IClient;
import common.ZerliDate;
import common.users.AUser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CustomerServiceOpenCompliant implements IController,IClient{

@FXML
private ComboBox<String> mstoreCombo;
@FXML
private ComboBox<String> mcustomerCombo;
@FXML
private TextArea mComplaintDescription;
private AUser user;
private ArrayList<String>storeId;
private ArrayList<String>customerID;
private boolean combo_store_flag = false;
private ConnectionController mComplaintController;
private boolean queryFlag=false;
ZerliDate date = new ZerliDate();

/**
 * on button save click, send complaint query request to server
 * @param event button Click
 */
	@FXML
	private void SaveClick(ActionEvent event) {
		if(mcustomerCombo.getValue()==null||mstoreCombo.getValue()==null||mComplaintDescription.getText().isEmpty()==true)
		{
			WindowFactory.showErrorDialog("Warning", "Details Missing!", "Please add all the deatils");
			return;
		}
		ArrayList<Object> paramSet = new ArrayList<Object>();
		paramSet.add(null);
		String id = mcustomerCombo.getValue().substring(4, mcustomerCombo.getValue().indexOf(','));
		paramSet.add(id);
		paramSet.add(user.getUserID());
		paramSet.add(mstoreCombo.getValue());
		paramSet.add(date.getFullDate());
		ZerliDate exp = new ZerliDate(date);
		try {
			exp.addDays(1);
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}
		paramSet.add(exp.getFullDate());
		paramSet.add(mComplaintDescription.getText());
		paramSet.add("1");
		System.out.println("size ="+paramSet.size());
	    ClientToServerMessage clientToServerMessage = new ClientToServerMessage(
	    		EQueryOption.NEW_COMPLAINT, paramSet, null);
	    mComplaintController = new ConnectionController(this);
		 mComplaintController.sendToServer(clientToServerMessage);
	}
	/**
	 * quit windows
	 * @param event close button click
	 */
	@FXML
    private void BackClick(ActionEvent event) {
		
		WindowFactory.show("/client/mainmenu/customer_service_main_menu.fxml" , user.getUserID());
	    }
	/**
	 * insert to combobox all the customers in a specific store
	 */
	
	@FXML
    void getCustomerIDbyStoreID() {
		 mcustomerCombo.getItems().clear();
		 ArrayList<Object> newParam = new ArrayList<Object>();
		 newParam.add(mstoreCombo.getValue());
		 ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.GET_CUSTOMERS_NAME_BY_STOREID,newParam,"customersnamesbystoreid");
		 mComplaintController = new ConnectionController(this);
	     mComplaintController.sendToServer(msg);    	// the msg that we sending to the server
    }
	/**
	 * get the message sent from server
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void handleMessageFromServer(Object message) 
	{
		if(message instanceof ArrayList<?>)
		{
			if(this.queryFlag == false)
			{
				this.queryFlag = true;
				this.storeId = (ArrayList<String>)message;
				Platform.runLater(()-> getStoreId());
				return;
			}
		}
		
		if(message instanceof ArrayList<?>)
		{
			this.customerID = (ArrayList<String>)message;
			Platform.runLater(()-> getCustomerId());
			
		}
		if (message instanceof String && message.equals("no results found")) 
				Platform.runLater(()-> WindowFactory.showInfoDialog("complaint succeeded"," complaint added to DB", null,"/client/mainmenu/customer_service_main_menu.fxml", UserController.getConnectedUserID()));
	}

	/**
	 * get the user's info
	 */

	@Override
	public void setDetails(Object... objects)
	{
		this.user = (AUser) objects[0];
		
		getStoresFromServer();
	}
	/**
	 *  get all stores from the server
	 */

	public void getStoresFromServer()
	{
       
       	ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.GET_ALL_STORES,null,"storsbyid");	
       	mComplaintController = new ConnectionController(this);	// must do to send msg to server - client (oscf service)
       	mComplaintController.sendToServer(msg);    	// the msg that we sending to the server
		
	}
	
	/**
	 * insert to combobox all the stores in the database
	 */
	public void getStoreId()
	{
		if(this.combo_store_flag == false)
 		{
			this.combo_store_flag = true;
			mstoreCombo.setValue(" ");
			mstoreCombo.getItems().clear();
			for(int i=0;i<this.storeId.size();i++)
				mstoreCombo.getItems().add(this.storeId.get(i));
 		}
		return;
	}
	/**
	 * insert to combobox all the customers of specific store from the database
	 */
	
	public void getCustomerId()
	{
			mcustomerCombo.getItems().clear();
			for(int i=0;i<this.customerID.size();i++)
				mcustomerCombo.getItems().add(this.customerID.get(i));
	}

}
