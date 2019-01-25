package client.user;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import client.ConnectionController;
import client.WindowFactory;
import common.ClientToServerMessage;
import common.EQueryOption;
import common.Store;
import common.users.Worker;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;


/**
 * Class handles worker information changes
 * used by system manager.
 *
 */
public final class WorkerChangerController extends AInfoChanger  {
	private String mViewedUserType; 

    @FXML
    private ComboBox<String> mPermissionComboBox;

    /**
     * Parse message from server
     */
	@Override
	public void handleMessageFromServer(Object message) {
		super.handleMessageFromServer(message);
		if (message instanceof String) {	
			if (!mUserTypeLabel.getText().equals(mViewedUserType))
				Platform.runLater(() -> mUserTypeLabel.setText(mViewedUserType));
			return;		// already taken care in super.
		}
	
		// if message is worker, populate the related fields.
		if (message instanceof Worker) {		// 2nd query returns here
			mViewedUser = (Worker) message;
			mUserVBox.setVisible(true);
			Platform.runLater(() -> mNameTextField.setText(mViewedUser.getName()));
			if (mViewedUser.getStoreID() == null || mViewedUser.getStoreID().isEmpty())
				Platform.runLater(() ->  mStoreSelector.setDisable(true));
			else
				Platform.runLater(() ->  mStoreSelector.setValue(mViewedUser.getStoreID()));
			mSubmitButton.setDisable(false);
			
			
	    	// 3rd query to get all stores
			ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.GET_ALL_STORES, null, "stores");
			mProgressBox.setVisible(true);
			(new ConnectionController(this)).sendToServer(msg);	
			return;
		}
		
		// arraylist of stores
		if (message instanceof ArrayList<?> && 
				((ArrayList<?>)message).get(0) instanceof Store) {		// 1st query returns here. Populate stores data.
			@SuppressWarnings("unchecked")
			ArrayList<Store> stores = (ArrayList<Store>)message;
			for (Store store : stores) {
				mStoreSelector.getItems().add(store.getStoreID());
			}
			return;
		}	
	}

	/**
	 * Initialization function
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.initialize(arg0, arg1);
		WindowFactory.mStage.setTitle("Worker info changer");

		// Add all worker types and set value listener in order to know
		// whether to specify store id or not.
		 mPermissionComboBox.getItems().addAll("StoreWorker","NetworkWorker",
				 "CustomerService","StoreManager","NetworkManager","SystemManager");
		 mPermissionComboBox.valueProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> arg0, String oldType, String newType) {
					if (mViewedUser == null)
						return;
					if (newType.equalsIgnoreCase("StoreWorker") || newType.equalsIgnoreCase("StoreManager")) {
						 mStoreSelector.setDisable(false);
						 mStoreSelector.setValue(mViewedUser.getStoreID());
					}
					else {
						 mStoreSelector.setValue(null);
						 mStoreSelector.setDisable(true);
					}			
				}    
		    });
	}
    
	/**
	 * On Submit Button click.
	 * Send changed data to DataBaseServer.
	 * 
	 * @param event button clicked.
	 */
    @FXML
    public void submit(ActionEvent event) {
    	String wantedType = mPermissionComboBox.getValue();
    	// validity checks.
    	if (mUserTypeLabel.getText().equalsIgnoreCase(wantedType) && mNameTextField.getText().equals(mViewedUser.getName())
    			&& mStoreSelector.getValue() == (mViewedUser.getStoreID())) {
    		WindowFactory.showErrorDialog("Nothing changed", null, "Nothing changed");
    		return;
    	}
    	if (isEmptyTextBox(mNameTextField, "Name"))
    		return;
    	if ((wantedType.equalsIgnoreCase("StoreWorker") ||(wantedType.equalsIgnoreCase("StoreManager"))
    			&& mStoreSelector.getValue() == null)) {
    		WindowFactory.showErrorDialog("Must select a store", null, "Must select a store!");
    		return;
    	}
    		
    	ArrayList<Object> params = new ArrayList<Object>();
    	params.add(mUserIDLabel.getText());
    	params.add(wantedType);
    	params.add(mNameTextField.getText());
    	params.add(mStoreSelector.getValue());
    	ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.CHANGE_WORKER_PERMISSION, params, null);
    	mProgressBox.setVisible(true);
    	(new ConnectionController(this)).sendToServer(msg);
		mViewedUserType = wantedType;
    }

    /**
     * get details from previous fxml controller
     */
    @Override
    public void setDetails(Object... objects) {
    	super.setDetails(objects);
		mUserIDLabel.setText(mViewedUserID);
		mViewedUserType =  (String) objects[3];
		mUserTypeLabel.setText(mViewedUserType);
		mPermissionComboBox.setValue(mViewedUserType);
		if (mChangingWorker.getUserID().equals(mViewedUserID)) {
			mFreezeButton.setDisable(true);
			mUnfreezeButton.setDisable(true);
			mStoreSelector.setDisable(true);
			mStoreSelector.setValue(mChangingWorker.getStoreID());
			mPermissionComboBox.setDisable(true);
			mPermissionComboBox.setValue(mChangingWorker.getWorkerType());
			mProgressBox.setVisible(false);
			return;
		}
		
		// query to fetch worker data.
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(mViewedUserID);
		ClientToServerMessage clientToServerMessage = new ClientToServerMessage(
				EQueryOption.GET_WORKER_INFO, params, "worker");
		mProgressBox.setVisible(true);
		(new ConnectionController(this)).sendToServer(clientToServerMessage);
    }
    
}	// WorkerChangerController

