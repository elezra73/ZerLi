package client.order;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import client.ConnectionController;
import client.IController;
import client.WindowFactory;
import common.ClientToServerMessage;
import common.CustomerOrder;
import common.EQueryOption;
import common.IClient;
import common.Product;
import common.ZerliDate;
import common.users.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class OrderController implements IController , IClient{
	
	private ConnectionController mConnectionController;		// if we used IClient
	private CustomerOrder mCustomerOrder;
	private Customer mCustomer;
	private Double mFinalAmount;
	private Map<Product, Integer> mOrderedProducts;
	private final double shipmenttCost = 20;
	private final int max_open_hour=24;
	private final int min_open_hour=8;	
	private  String mBackPath;
	private ZerliDate deliveryDate;
	private boolean mDeliveryPreferenceChoosed=false,mWarned=false,mShipmentselected=false;
	private ZerliDate now;
	private boolean mSubmited=false; //to prevent bug of ordering twice the same order
	private String mSelectedTime;
	
	@FXML
	private Label lblShipmentCost;
	@FXML
	private TextField txtAddress,txtName,txtPhone,txtFinalPrice,txtShipmentCost;
	@FXML
	private RadioButton rbSelfPickup,rbShipment;
	@FXML
	private TextArea txtBlessing;
	@FXML
	private Button btnSubmit,btnBack;
	@FXML
	private ComboBox<String> cmbTImes;
	@FXML
	private DatePicker dpDates;


	
	/**
	 * set the final price in addition to shipment price and make shipment info field visible
	 */
	@FXML
	private void rdButtonShipmentSelected()
	{
		rbSelfPickup.setSelected(false);
		rbShipment.setSelected(true);
		txtAddress.setText("");
		txtName.setText("");
		txtPhone.setText("");
		txtAddress.setVisible(true);
		txtName.setVisible(true); 
		txtPhone.setVisible(true);
		String formattedData = String.format("%.02f", mFinalAmount+shipmenttCost);
		txtFinalPrice.setText(formattedData);
		mDeliveryPreferenceChoosed=true;
		mShipmentselected=true;
		lblShipmentCost.setVisible(true);
		txtShipmentCost.setVisible(true);
		String formattedData2 = String.format("%.02f", shipmenttCost);
		txtShipmentCost.setText(formattedData2);
		
	}
	/**
	 * set the final price without addition to shipment price and make shipment info field invisible
	 */
	@FXML
	private void rdButtonSelfPickupClicked()
	{
		rbShipment.setSelected(false);
		rbSelfPickup.setSelected(true);
		txtAddress.setText("Self-PickUp");
		txtName.setText("Self-PickUp");
		txtPhone.setText("Self-PickUp");
		txtAddress.setVisible(false);
		txtName.setVisible(false);
		txtPhone.setVisible(false);
		String formattedData = String.format("%.02f", mFinalAmount);
		txtFinalPrice.setText(formattedData);
		mDeliveryPreferenceChoosed=true;
		mShipmentselected=false;
		lblShipmentCost.setVisible(false);
		txtShipmentCost.setVisible(false);
		String formattedData2 = String.format("%.02f", shipmenttCost);
		txtShipmentCost.setText(formattedData2);
	}
	/**
	 * create a CustomerOrder instance with actual selected product and the final ,and send it to DB
	 * @throws ParseException ParseException
	 */
	@FXML
	private void btnSubmitClicked() throws ParseException
	{
		if(mSubmited==true)
			return;
		String shipmentPreferenceSelected;
		if(checkDateAndValidate()==false)
			return;
		if(mDeliveryPreferenceChoosed==false)
		{
			WindowFactory.showErrorDialog("Information missing", "", "You must choose shipment preference");
			return;
		}
		if(mWarned==false)
			checkSelectedTime();
			
		ArrayList<Object> params;
		if(mShipmentselected==true && checkFields()==false)
		{
			 WindowFactory.showErrorDialog("Information missing", "Missing Field", "Please fill all field");
			 return;
		}
		if(mShipmentselected==true)
		{
			mFinalAmount+=shipmenttCost;
			shipmentPreferenceSelected= "Delivery";
		}
		else
			shipmentPreferenceSelected= "Self-PickUp";
		mCustomerOrder = new CustomerOrder(mCustomer, mOrderedProducts, mFinalAmount.toString(),txtBlessing.getText(),deliveryDate,txtAddress.getText(),txtName.getText(),txtPhone.getText());
		mCustomerOrder.setShipmentMethod(shipmentPreferenceSelected);
		params= new ArrayList<Object>();
		params.add(mCustomerOrder);
		mConnectionController = new ConnectionController(this);
		ClientToServerMessage clientToServerMessage = new ClientToServerMessage(EQueryOption.ADD_CUSTOMER_ORDER, params, "new order");
		mConnectionController.sendToServer(clientToServerMessage);
		mSubmited=true;
	}
	/**
	 * check the field and return true if all the field are not empty ,else return false
	 * @return true if all the fields are not null
	 */
	private boolean checkFields()
	{
		if(txtAddress.getText().length()==0 ||txtName.getText().length()==0 ||
				 txtPhone.getText().length()==0)
		 {
			 return false;
		 }
		return true;
	}
	/**
	 * go back to the previous window
	 */
	@FXML
	private void btnBackClicked()
	{
		WindowFactory.show(mBackPath, mCustomer);
	}
	/**
	 * check the selected date of delivery , pop up window appear if it is not a future  date and return false ,else only return true.
	 * @throws ParseException ParseException
	 * @return true if date is valid
	 */
	@FXML
	private boolean checkDateAndValidate() throws ParseException
	{
		String content = dpDates.getValue().toString();
		String year =content.substring(0, 4);
		String month =content.substring(5, 7);
		String day =content.substring(8, 10);
		deliveryDate= new ZerliDate(day+"/"+month+"/"+year+" "+mSelectedTime);
		if(deliveryDate.compareDateWithoutTime(now)<0)
		{
			WindowFactory.showErrorDialog("Invalid Date", "", "you need to choose a future date!");
			return false;
		}
		//insertTimesToComboBox();
		return true;
	}

	
	/**
	 * check the time selected : pop up appear if the time is less than 3 hour from actual time
	 * @throws ParseException ParseException
	 */
	public void checkSelectedTime() throws ParseException
	{
		deliveryDate = new ZerliDate(deliveryDate.getDate()+" "+mSelectedTime);
		if((deliveryDate.getDifferenceInHours(now)<=3))
			WindowFactory.showInfoDialog("Info", "short time", "Because of choosing a near hour ,notice that you may recieve your item after the time selected");
		mWarned=true;
	}
	/**
	 * insert the delivery hour times for choose.
	 * @throws ParseException ParseException
	 */
	private void insertTimesToComboBox() throws ParseException {
		int hour,min;
		
		ObservableList<String> toInsert = FXCollections.observableArrayList();
		String temp;
		cmbTImes.getItems().clear();
		if(deliveryDate.getDate().equals(now.getDate()))
		{
			hour= now.getHourInInTeger();
			min= now.getMinuteInInteger();
			
		}
		else
		{
			hour = min_open_hour;
			min=14;
		}
		while(++min%15!=0);
		if(min==0||min%60==0)hour++;
		
		for( ;hour <max_open_hour;hour++)
		{
			if(hour>Integer.parseInt(now.getTime().substring(0, 2))||min>45)
					min =0;
			for(;min<60;min+=15)
			{
				if(hour<=9)
					temp="0"+hour;
				else
					temp=""+hour;
				if(min<=9)
					temp+=":0"+min;
				else
					temp+=":"+min;
				
				toInsert.add(temp);
			}
		}
		cmbTImes.getItems().addAll(toInsert);
		cmbTImes.setValue(cmbTImes.getItems().get(0));
			
		
	}
	/**
	 * set the selected time for delivery
	 */
	@FXML
	private void getSelectedTime()
	{
		mSelectedTime= cmbTImes.getValue();
		
	}
	/**
	 * get the info a the customer to proceed a customer order
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setDetails(Object... objects) 
	{
		WindowFactory.mStage.setTitle("Order Details");
		mCustomer = (Customer)objects[0];
		mOrderedProducts =(Map<Product, Integer>)objects[1];
		mFinalAmount= (Double)objects[2];
		mBackPath=(String)objects[3];
		initialize();
		
	}
	/**
	 * get the message sent from server
	 */
	@Override
	public void handleMessageFromServer(Object message) {
		
		if (message instanceof String) {
			if (((String)message).equals("uploaded"))
			{
				Platform.runLater(() -> WindowFactory.showInfoDialog("Order Info",
						null, "Succed! You will redirected to main menu"));
			}
		}
		if(message instanceof String && message.equals("Ordered!!"))
			Platform.runLater(() ->WindowFactory.showInfoDialog("Order Confirmed", "Thank you for shopping with us", "Hope to see you again","/client/mainmenu/customer_main_menu.fxml", mCustomer.getUserID()));
	}
	
	/**
	 * initialize all the field and make the phone text field to be only numeric text
	 */
	private void initialize()
	{
		dpDates.setValue(LocalDate.now());
		deliveryDate= new ZerliDate();
		now = new ZerliDate();
		String formattedData = String.format("%.02f", mFinalAmount);
		txtFinalPrice.setText(formattedData);
		try {
			insertTimesToComboBox();
			mSelectedTime= cmbTImes.getValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		txtPhone.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		            txtPhone.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		    }
		});
		
	}
}
