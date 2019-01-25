package client.customerServiceWorker;

import java.util.ArrayList;
import client.ConnectionController;
import client.IController;
import client.WindowFactory;
import client.user.UserController;
import common.ClientToServerMessage;
import common.Complaint;
import common.EQueryOption;
import common.IClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ComplaintsViewController implements IController, IClient{
	@FXML
    private TableView<Complaint> mComplaintsList;
	 
	@FXML
    private TableColumn<Complaint, String> mComplaintCol;

    @FXML
    private TableColumn<Complaint, String> mDateCol;

    @FXML
    private TableColumn<Complaint, String> mCustomerCol;

    @FXML
    private TableColumn<Complaint, String> mStoreCol;

    @FXML
    private TableColumn<Complaint, String> mWorkerCol;

    @FXML
    private TableColumn<Complaint, String> mDescriptionCol;

	private ConnectionController mConnectiontController;
	private ArrayList<Complaint> mComplaints;
	private ObservableList<Complaint> mObservableComplaint;

	/**
	 * insert details of complaints by specific customerService to the tableView
	 */
	@SuppressWarnings("unchecked")
	@Override
	
	public void setDetails(Object... objects) {
		if (objects.length == 0 || objects[0] instanceof String) {
			return;
		}
		mComplaints = (ArrayList<Complaint>)objects[0];
		mObservableComplaint=FXCollections.observableArrayList(mComplaints);
		mComplaintsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		mComplaintCol.setCellValueFactory(new PropertyValueFactory<Complaint,String>("ComplaintID"));
		mCustomerCol.setCellValueFactory(new PropertyValueFactory<Complaint,String>("CustomerID"));
		mWorkerCol.setCellValueFactory(new PropertyValueFactory<Complaint,String>("WorkerID"));
		mStoreCol.setCellValueFactory(new PropertyValueFactory<Complaint,String>("StoreID"));
		mDateCol.setCellValueFactory(new PropertyValueFactory<Complaint,String>("OpenDate"));
		mDescriptionCol.setCellValueFactory(new PropertyValueFactory<Complaint,String>("Description"));
		mComplaintsList.setItems(mObservableComplaint);
	}
	/**
	 * we select the complaint we want to reward
	 * @return the chosen complaint ID 
	 */
	@FXML
	private String selectedComplaint()
	{
		Complaint mObserComplaint=(Complaint)mComplaintsList.getSelectionModel().getSelectedItem();
		return mObserComplaint.getComplaintID();
	}
	/**
	 *  selected complaint from the table
	 * @return return the store ID
	 */
	private String getSelecedStoreID() 
	{
		Complaint mObserComplaint=(Complaint)mComplaintsList.getSelectionModel().getSelectedItem();
		return mObserComplaint.getStoreID();
	}
/**
 *selected complaint from the table
 * @return the customer ID
 */
	private String getSelecedCustomerID() 
	{
		Complaint mObserComplaint=(Complaint)mComplaintsList.getSelectionModel().getSelectedItem();
		return mObserComplaint.getCustomerID();
	}
	/**
	 * reward customer in a specific store that complained
	 * @param event reward button pressed
	 */
	@FXML
	private void onRewardAccountClick(ActionEvent event){
		if(mComplaintsList.getSelectionModel().getSelectedItem()==null)
		{
			WindowFactory.showErrorDialog("Warning", null, "Please choose complaint!");
			return;
		}
		String reward = WindowFactory.showInputDialog("Reward account", null, "Enter amount");
		if (reward==null)
		{
			return;
		}
		String selectedComplaint = selectedComplaint();
		String selectedCustomer = getSelecedCustomerID();
		String SelectedStore =getSelecedStoreID();
		mConnectiontController = new ConnectionController(this);
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(selectedCustomer);
		params.add(Double.parseDouble(reward));
		params.add(SelectedStore);
		params.add(selectedComplaint);
		
		ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.UPDATE_COMPLAINT_AND_REWARD_ACCOUNT, params, null);
	
		mConnectiontController.sendToServer(msg);
		
	
	}
	/**
	 * quit windows
	 * @param event close button click
	 */
	@FXML
    private void BackClick(ActionEvent event) {
		WindowFactory.show("/client/mainmenu/customer_service_main_menu.fxml", 
				UserController.getConnectedUserID());
	    }
	
	/**
	 * get the message sent from server
	 */
	@Override
	public void handleMessageFromServer(Object message) {
		if (message instanceof String) {
			if (message.toString().equals("updated")) {
				Platform.runLater(() -> WindowFactory.showInfoDialog("reward succeeded"," reward succeeded", null,"/client/mainmenu/customer_service_main_menu.fxml", UserController.getConnectedUserID()));
					
				}
		}
		
		
	}
	

}
