package client.order;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import client.ConnectionController;
import client.IController;
import client.WindowFactory;
import common.ClientToServerMessage;
import common.CustomerOrder;
import common.EQueryOption;
import common.IClient;
import common.ZerliDate;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;

/**
 * Class responsible for canceling a customer order
 * and refunding the customer.
 * 
 *
 */
public class OrderCancellationController implements IClient, IController, Initializable {

	@FXML
	private ComboBox<CustomerOrder> mOrderSelector;
	@FXML
	private Button mBlessingButton, mCancelButton;
	@FXML
	private VBox mProgressBox, mOrderDetailBox;
	@FXML
	private Label mRNameLabel, mPaidLabel, mDateLabel, mTimeLabel, mDeliveryDateLabel, mDeliveryTimeLabel;
	@FXML
	private Label mStoreIDLabel, mRefundLabel, mDeliveryLabel, mNoOrdersLabel, mAddressLabel, mDescLabel;
	@FXML
	private HBox mOrderTimeBox, mOrderDateBox, mDeliveryBox, mAddressBox,mRNameBox;
	private String mCustomerID;
	private static String mConfirmation;
	
	public static enum Refund {
		DELIVERED,	// Order delivered. No refund.
		FULL,		// Full refund. 
		HALF,		// Half refund.
		NONE		// no refund.
	};
	
	/**
	 * Cancel order.
	 * @param event button clicked.
	 */
	@FXML
	private void onCancelClick(ActionEvent event) {
		try {
			double amountToRefund = 0;
			amountToRefund = Double.parseDouble(mRefundLabel.getText().replace("$",""));		
			if (amountToRefund < 0)
				throw new NumberFormatException();
			ArrayList<Object> params = new ArrayList<Object>();
			params.add(mOrderSelector.getValue().getOrderID());
			params.add(mCustomerID);
			params.add(mStoreIDLabel.getText());
			params.add(amountToRefund);
			ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.CANCEL_CUSTOMER_ORDER, 
					params, null);
			mProgressBox.setVisible(true);
			(new ConnectionController(this)).sendToServer(msg);
		}
		catch(Exception e) {
			WindowFactory.showErrorDialog("Error", "Oops! Something went wrong..", e.toString());
		}
	}

	/**
	 * Back to customer main menu.
	 * 
	 * @param event button clicked.
	 */
	@FXML
	private void onBackClick(ActionEvent event) {
		WindowFactory.show("/client/mainmenu/customer_main_menu.fxml", mCustomerID);
	}
	    
	
	/**
	 * Show attached blessing.
	 * @param event button clicked.
	 */
	@FXML
	private void showBlessing(ActionEvent event) {
		WindowFactory.showFieldDialog("Blessing", "Blessing", mOrderSelector.getValue().getBless());
	}
	
	/**
	 * This function calculates the refund.
	 * @param deliveryDate	The delivery date.
	 * @param price		The order's full price.
	 * @return	a Refund state and refund price.
	 * @throws ParseException ParseException
	 */
	private Pair<Refund,Double> calculateRefund(ZerliDate deliveryDate, double price) throws ParseException{
		if (deliveryDate == null || price < 0)
			return null;
		ZerliDate now = new ZerliDate();
		if (now.compareTo(deliveryDate) > 0 || now.getDifferenceInMinutes(deliveryDate) > 0) 		
			return new Pair<Refund, Double>(Refund.DELIVERED, 0.0);
		
		long minDiff =  -1 * now.getDifferenceInMinutes(deliveryDate);

		if (minDiff > 180) {
			return new Pair<Refund, Double>(Refund.FULL, price);
		}
		else if (minDiff > 60) {
			return new Pair<Refund, Double>(Refund.HALF, 0.5*price);
		}
		else {
			return new Pair<Refund, Double>(Refund.NONE, 0.0);
		}
	}
	
	private void calculateOrderDifferences(ZerliDate deliveryDate) throws ParseException {
		double paidPrice = Double.parseDouble(mPaidLabel.getText());
		Pair<Refund,Double> refundState = calculateRefund(deliveryDate, paidPrice);
		
		if (refundState.getKey().equals(Refund.DELIVERED)) {				
			mDescLabel.setText("Order already delivered.");
			mRefundLabel.setText("0.00 $");
			mDescLabel.setTextFill(Color.RED);
			mRefundLabel.setVisible(false);
			mCancelButton.setDisable(true);
			return;
		}
		else {
			mDescLabel.setText("Refund ammount if cancelled:");
			mDescLabel.setTextFill(Color.BLACK);
			mRefundLabel.setVisible(true);
			mCancelButton.setDisable(false);
		}

		if (refundState.getKey().equals(Refund.FULL)) {
			String formattedData = String.format("%.02f", refundState.getValue());
			mRefundLabel.setText(formattedData+" $");
			mRefundLabel.setTextFill(Color.GREEN);
			mDescLabel.setText("100% Refund if cancelled: ");
			mConfirmation = "You received 100% Refund: " + mRefundLabel.getText();
		}
		else if (refundState.getKey().equals(Refund.HALF)) {
			String formattedData = String.format("%.02f", refundState.getValue()/2.0);
			mRefundLabel.setText(formattedData+" $");
			mRefundLabel.setTextFill(Color.ORANGE);
			mDescLabel.setText("50% Refund if cancelled: ");
			mConfirmation = "You received 50% Refund: " + mRefundLabel.getText();
		}
		else {
			mRefundLabel.setVisible(false);
			mRefundLabel.setText("0.00 $");
			mDescLabel.setText("Sorry, No refund if cancelled.");
			mDescLabel.setTextFill(Color.RED);
			mConfirmation = "Order cancelled but you didn't receive any refund.";

		}
	}
	
	@FXML
	private void populateOrderDetails(ActionEvent event) {
		clearAll();
		mOrderDetailBox.setVisible(true);
		CustomerOrder order = mOrderSelector.getValue();
		mStoreIDLabel.setText(order.getStoreID());
		mDateLabel.setText(order.getOrderDate().getDate());
		mTimeLabel.setText(order.getOrderDate().getTime());
		mDeliveryDateLabel.setText(order.getDeliveryDate().getDate());
		mDeliveryTimeLabel.setText(order.getDeliveryDate().getTime());
		if (order.getShipmentMethod() == null || order.getShipmentMethod().equals(""))
			mDeliveryLabel.setText("Missing information");  
		else
			mDeliveryLabel.setText(order.getShipmentMethod()); 
		if (order.getShipmentAddress() == null || order.getShipmentAddress().equals(""))
			mAddressLabel.setText("Missing information");
		else
			mAddressLabel.setText(order.getShipmentAddress());
		if (order.getRecieverName() == null || order.getRecieverName().equals(""))
			mRNameLabel.setText("Missing information");
		else
			mRNameLabel.setText(order.getRecieverName());
		String formattedData = String.format("%.02f", order.getOrderPrice());
		mPaidLabel.setText(formattedData);
		
		try {
			calculateOrderDifferences(order.getDeliveryDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Get customerID from previous menu.
	 */
	@Override
	public void setDetails(Object... objects) {
		mCustomerID = (String)objects[0];
		
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(mCustomerID);
		ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.GET_CUSTOMER_ACTIVE_ORDERS, 
				params, "active_orders");
		mProgressBox.setVisible(true);
		(new ConnectionController(this)).sendToServer(msg);
	}

	@Override
	public void handleMessageFromServer(Object message) {
		System.out.println(">"+message.toString());
		mProgressBox.setVisible(false);
		if (message instanceof String) {
			if (message.toString().equals("none")) {
				mNoOrdersLabel.setVisible(true);
				Platform.runLater(() -> WindowFactory.showInfoDialog("No orders found", null, 
						"You don't have any orders"));
				return;
			}
			if (message.toString().equals("cancelled")) {
				Platform.runLater(() -> WindowFactory.showInfoDialog("Order cancelled", "Order successfully cancelled",
						mConfirmation));
				Platform.runLater(() -> onBackClick(null));
				return;
			}
			Platform.runLater(() -> WindowFactory.showInfoDialog("Server message", null, message.toString()));
			
			return;
		}
		if (message instanceof ArrayList<?>) {
			@SuppressWarnings("unchecked")
			ArrayList<CustomerOrder> orders = (ArrayList<CustomerOrder>) message;	
			mOrderSelector.getItems().clear();
			for (CustomerOrder order : orders) {
				mOrderSelector.getItems().add(order);
			}
			mOrderSelector.setDisable(false);
		}
	
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		WindowFactory.mStage.setTitle("Order Cancellation");
		WindowFactory.mStage.setResizable(false);
		mCancelButton.setDisable(true);
		mOrderDetailBox.setVisible(false);
		mProgressBox.setVisible(false);
		mNoOrdersLabel.setVisible(false);
		mOrderSelector.setDisable(true);
		
	}

	private void clearAll() {
		mStoreIDLabel.setText("");
		mDateLabel.setText("");
		mTimeLabel.setText("");
		mDeliveryDateLabel.setText("");
		mDeliveryTimeLabel.setText("");
		mDeliveryLabel.setText("");
		mAddressLabel.setText("");
		mRNameLabel.setText("");
		mPaidLabel.setText("");
		mRefundLabel.setText("");
	}


}
