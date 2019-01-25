package client.user;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import client.ConnectionController;
import client.WindowFactory;
import common.ClientToServerMessage;
import common.EQueryOption;
import common.users.Customer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

/**
 * Class handles worker information changes
 * used by system manager.
 *
 */
public final class CustomerChangerController extends AInfoChanger  {
	@FXML
	private TextField mAddressTextField;
	@FXML
	private ComboBox<String> mPaymentMethodComboBox;
	@FXML
	private TextField mCreditCardTextField;
    @FXML
    private Label mAccountBalanceLabel;
    @FXML
    private TextField mSocialNumberTextField;
	private ChangeListener<String> mCreditChangeListener;
	private final static int CREDIT_LENGTH = 16;
    @FXML
    private Text mTitleLabel;
    @FXML
    private HBox mCreditCardBox;
    private static boolean mNotRegistered = true;
    private ArrayList<Customer> mAllCustomerData;
    
    /**
     * Get objects from previous fxml view
     */
	@Override
	public void setDetails(Object... objects) {
		super.setDetails(objects);
		if (mChangingWorker.getWorkerType().equalsIgnoreCase("StoreManager")) {
			WindowFactory.mStage.setTitle("Customer account activator");
			mTitleLabel.setText("Customer account activator");
			mSubmitButton.setText("Activate Account");
			mStoreSelector.getItems().add(mChangingWorker.getStoreID());
			mStoreSelector.setValue(mChangingWorker.getStoreID());
		}
		else 
			mCreditCardBox.setVisible(false);	// system manager doesnt need to see credit
		
		//  query to get customer data
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(mViewedUserID);
		String returnType = "customers";
		if (mChangingWorker.getWorkerType().equalsIgnoreCase("StoreManager")) {
			params.add(mChangingWorker.getStoreID());
			returnType = "customer";
		}
		else params.add(null);
		ClientToServerMessage clientToServerMessage = new ClientToServerMessage(
				EQueryOption.GET_CUSTOMER_INFO, params, returnType);
		mProgressBox.setVisible(true);
		mSubmitButton.setDisable(true);
		mUserVBox.setVisible(true);
		(new ConnectionController(this)).sendToServer(clientToServerMessage);	
	}
	
	/**
	 * Parse message from server
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void handleMessageFromServer(Object message) {
		super.handleMessageFromServer(message);
		Platform.runLater(() -> mNameTextField.setText(mViewedUserID));
		if (message instanceof String) {
			if (message.toString().equalsIgnoreCase("not registered")) {
				mNotRegistered = true;
				mUserVBox.setVisible(true);
				if (mChangingWorker.getWorkerType().equalsIgnoreCase("StoreManager")) 
					mSubmitButton.setDisable(false);
			}
			return;
		}	
		
		if (message instanceof ArrayList<?> && ((ArrayList<?>) message).get(0) instanceof Customer) {
			mAllCustomerData = (ArrayList<Customer>) message;
			mStoreSelector.setDisable(false);
			for (Customer cus : mAllCustomerData) {
				mStoreSelector.getItems().add(cus.getStoreID());
			}
		}
		if (message instanceof Customer) {		
			mNotRegistered = false;
			mViewedUser = (Customer) message;
			Platform.runLater(() -> parseCustomer((Customer) message));
			mSubmitButton.setDisable(false);
			mStoreSelector.getItems().add(mViewedUser.getStoreID());
		}
	
	}	// handleMessageFromServer

	
	/**
	 * Initialization function
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.initialize(arg0, arg1);
		WindowFactory.mStage.setTitle("Customer info changer");
		// Add all worker types and set value listener in order to know
		// Whether to specify store id or not.
		mPaymentMethodComboBox.getItems().addAll("Cash", "CreditCard", "MonthlySubscription",
				 "YearlySubscription");

		/**
		 * Combine both listeners to make a credit-card field..
		 * ignore non-digits characters and add '-' after each 4 digits.
		 */
		mCreditCardTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent key) {		
				if (key.getCode().equals(KeyCode.BACK_SPACE))	
					return;
				// do nothing if non-digit character key has been pressed.
				if (!key.getCode().isDigitKey()) 
					key.consume();
			}		
		});
		
		mCreditChangeListener = new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
					String newValue) {
	
				if (newValue.isEmpty())
					return;
				newValue = newValue.replace("-", "");	
				if (newValue.length() > CREDIT_LENGTH ||	// if credit card length exeeded CREDIT_LENGTH, do not change..
						!Character.isDigit(newValue.charAt(newValue.length()-1))) {
					mCreditCardTextField.textProperty().removeListener(mCreditChangeListener);	// bypass listener
					mCreditCardTextField.setText(oldValue);
					mCreditCardTextField.textProperty().addListener(mCreditChangeListener);		
					return;
				}
				  
				// add '-' after each 4 digits..
				String tempString = "";
				for (int i=0; i<newValue.length(); ++i) {
					if (i>1 && i%4 == 0) 
						tempString += "-" + newValue.charAt(i);
					else tempString += newValue.charAt(i);
				}
				final String toSet = tempString;
				mCreditCardTextField.textProperty().removeListener(mCreditChangeListener);	// bypass listener
				Platform.runLater(() -> {	// takes care of "IllegalArgumentException: The start must be <= the end"
					mCreditCardTextField.setText(toSet);
					mCreditCardTextField.positionCaret(toSet.length());
				});
				mCreditCardTextField.textProperty().addListener(mCreditChangeListener);		
			}	// changed
		};		// Credit card ChangeListener
		mCreditCardTextField.textProperty().addListener(mCreditChangeListener);
	}	// initialize
    
	/**
	 * On Submit Button click.
	 * Send changed data to DataBaseServer.
	 * 
	 * @param event button clicked.
	 */
    @FXML
    public void submit(ActionEvent event) {
    	String creditCardNumber = mCreditCardTextField.getText().replace("-", "").replace(" ", ""); 
    	// validity checks..
    	if (isEmptyTextBox(mNameTextField, "Name"))
    		return;
    	if (mChangingWorker.getWorkerType().equalsIgnoreCase("StoreManager")) {
    		if (isEmptyTextBox(mSocialNumberTextField, "Social Number"))
    			return;
        	if (creditCardNumber.length() != CREDIT_LENGTH || Pattern.matches("^[\\d]+", creditCardNumber) == false) {
        		WindowFactory.showErrorDialog("Invalid Credit Card Number", null, "Invalid Credit Card Number.");
        		return;
        	}
        	
    	}
    	if (mPaymentMethodComboBox.getValue() == null || mPaymentMethodComboBox.getValue().isEmpty()) {
    		WindowFactory.showErrorDialog("Please choose payment method", null, "Please choose payment method.");
    		return;
    	}
    	ArrayList<Object> params = new ArrayList<Object>();
    	params.add(mViewedUserID);
    	params.add(mNameTextField.getText());
    	if (mChangingWorker.getWorkerType().equalsIgnoreCase("StoreManager"))
    		params.add(mChangingWorker.getStoreID());
    	else params.add( mStoreSelector.getValue());
    	params.add(mAddressTextField.getText());
    	params.add(mPaymentMethodComboBox.getValue());
    	params.add(creditCardNumber);
    	params.add(mSocialNumberTextField.getText());
    	if (mChangingWorker.getWorkerType().equalsIgnoreCase("StoreManager"))
    		params.add(true);
    	else params.add(false);
    	ClientToServerMessage msg;
    	if (mNotRegistered)
    		msg = new ClientToServerMessage(EQueryOption.ADD_NEW_CUSTOMER, params, "updated");
    	else msg = new ClientToServerMessage(EQueryOption.UPDATE_CUSTOMER_INFO, params, "updated");
    	mNotRegistered = false;
     	mProgressBox.setVisible(true);
    	(new ConnectionController(this)).sendToServer(msg);
    }
    
    /**
     * On store select listener
     * @param event store selected
     */
    @FXML
    private void selectStore(ActionEvent event) {
    	if (mAllCustomerData == null) return;
    	mSubmitButton.setDisable(false);
    	for (Customer cus : mAllCustomerData) {
    		if (cus.getStoreID().equals(mStoreSelector.getValue())){
    			mViewedUser = cus;
    			parseCustomer(cus);
    			mNotRegistered = false;
    			return;
    		}
    	}
    	mNotRegistered = true;
    }
    
    /**
     * populate customer info to the view
     * @param cus customer info
     */
    private void parseCustomer(Customer cus) {
    	mNameTextField.setText(cus.getName());
		mStoreSelector.setValue(cus.getStoreID());
		mAddressTextField.setText(cus.getAddress());
		mPaymentMethodComboBox.setValue(cus.getPaymentMethod());
		Double balance = (cus.getAccountBalance());
		mAccountBalanceLabel.setText(""+balance);
		if (balance < 0 )
			mAccountBalanceLabel.setTextFill(Color.RED);
		else mAccountBalanceLabel.setTextFill(Color.GREEN);
		mSocialNumberTextField.setText(cus.getSocialNumber());
		mCreditCardTextField.setText(cus.getCreditCard());
    }
    
	/**
	 * on back button click.
	 * @param event button clicked.
	 */
    @FXML
    @Override
    protected void backButtonBlicked(ActionEvent event) {
    	mAllCustomerData = null;
    	super.backButtonBlicked(event);
    }
    
}	// CustomerChangerController
