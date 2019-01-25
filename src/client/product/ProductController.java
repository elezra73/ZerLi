package client.product;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.ImageFormatException;

import client.ConnectionController;
import client.IController;
import client.WindowFactory;
import client.user.UserController;
import common.ClientToServerMessage;
import common.EQueryOption;
import common.IClient;
import common.Product;
import common.SendableFile;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class ProductController implements IController,IClient {
	
	private ConnectionController mConnectionController;		// if we used IClient
	private ArrayList<Product> mCatalog;
	private final String addNewProductFxmlPath ="/client/product/new_product_view.fxml";
	private final String updateProductInfoFxmlPath="/client/product/update_product_view.fxml";
	private final String removeProductInfoFxmlPath="/client/product/delete_product_view.fxml";
	private final String previousWindow="/client/mainmenu/network_worker_main_menu.fxml";
	@FXML
	private VBox mProgressBox;
    @FXML
    private ImageView imgView, imgView2;
	@FXML
	private TextField txtProductID,txtProductName,txtProductType,txtProductPrice,txtProductColor,txtProductUrlImage;
	@FXML
	private Button btnPic;
	@FXML
	private ComboBox<String> cmbSelectProduct;
	private static SendableFile fileToUpload;
	private static boolean addingNew;
	
	/**
	 *  redirecting to Add product window
	 */
	@FXML
	private void btnAddProductOpenWindowClicked()
	{
		WindowFactory.show(addNewProductFxmlPath);
	}
	/**
	 *  redirecting to Update product window
	 */
	@FXML
	private void btnUpdateProductOpenWindowClicked()
	{
		WindowFactory.show(updateProductInfoFxmlPath);
			
	}
	/**
	 *  redirecting to Remove product window
	 */
	@FXML
	private void btnRemoveProductOpenWindowClicked()
	{
		WindowFactory.show(removeProductInfoFxmlPath);
	}
	/**
	 *  Update the product info in DB
	 */
	@FXML
	private void btnUpdateProductSubmit()
	{
		if (checkFields() == false)
			return;
		addingNew = false;
		mConnectionController = new ConnectionController(this);
		mProgressBox.setVisible(true);
		mConnectionController.sendToServer(fileToUpload);
	}
	/**
	 *  Remove the product info in DB
	 */
	@FXML
	private void btnRemoveProductSubmit()
	{
		ArrayList<Object> params ;
		if(txtProductID.getText().length()==0)
		{
			WindowFactory.showErrorDialog("Information missing", "Missing Field", "You must enter ID!!");
		}
		else
		{
			params = new ArrayList<Object>();
			params.add(txtProductID.getText());
			params.add(null);
			mConnectionController = new ConnectionController(this);
			ClientToServerMessage clientToServerMessage = new ClientToServerMessage(EQueryOption.DELETE_PRODUCT, params, null);
			mProgressBox.setVisible(true);
			mConnectionController.sendToServer(clientToServerMessage);
		}
	}
	/**
	 *  Add new product info in DB
	 */
	@FXML
	private void btnAddNewProductSubmit() 
	{
		if (checkFields() == false)
			return;
		addingNew = true;
		mConnectionController = new ConnectionController(this);
		mProgressBox.setVisible(true);
		mConnectionController.sendToServer(fileToUpload);
	}
	/**
	 *  check the fields and return true if all fields are not empty
	 *  @return boolean true if fields are ok
	 */
	private boolean checkFields()
	{

		if(txtProductName.getText().isEmpty() || txtProductType.getText().isEmpty() 
				|| txtProductPrice.getText().isEmpty() || txtProductColor.getText().isEmpty())
		 {
			 WindowFactory.showErrorDialog("Missing fields! can't upload!", null, "Please fill all fields");
			 return false;
		 }
		try {
			Double.parseDouble(txtProductPrice.getText());
		}
		catch(Exception e) {
			 WindowFactory.showErrorDialog("Invalid price", null, "Invalid price.");
			 return false;
		}
		if (txtProductUrlImage.getText().isEmpty()) {
			 WindowFactory.showErrorDialog("No picture, can't upload!", null, "Please choose a picture");
			 return false;
		}
		return true;
	}
	
	/**
	 *  Open a file dialog and send to the database the file selected
	 *  @throws IOException IOException
	 */
	@FXML
	private void  openFileDialog() throws IOException
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose Picture");
		File selectedFile= null;
		
		try {
			selectedFile=fileChooser.showOpenDialog(WindowFactory.mStage);
			if (selectedFile != null) {
				if (ImageIO.read(selectedFile) == null)
					throw new ImageFormatException("Not a valid image file");
				fileToUpload = new SendableFile(selectedFile);
				imgView.setImage(fileToUpload.getImage());
				txtProductUrlImage.setText(selectedFile.getName());
			}
		}
		catch (Exception e) {
			WindowFactory.showErrorDialog("Failed to open picture", null, e.getMessage());
		}
		
	}
	
	/**
	 *  return to back window
	 */
	@FXML
	private void btnBackClicked()
	{
		WindowFactory.show(previousWindow, UserController.getConnectedUserID());
	}

	
	/**
	 *  set details for use them in the current controller
	 */
	@Override
	public void setDetails(Object... objects) {
		WindowFactory.mStage.setTitle("Product Manager");
		ArrayList<Object> params = new ArrayList<Object>();
       	ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.GET_CATALOG,params,"catalog");		//get all the products (network catalog)
      	mConnectionController = new ConnectionController(this);	// must do to send msg to server - client (oscf service)
      	mProgressBox.setVisible(true);
		mConnectionController.sendToServer(msg);    	// the msg that we sending to the server
	}
	/**
	 *  Insert all the products in the comboBox
	 */
	private void insertProductsToCmb()
	{
		ObservableList<String> toInsert = FXCollections.observableArrayList();
		for (Product product : mCatalog) {
			toInsert.add(product.printToComboBox());
		}
		cmbSelectProduct.getItems().addAll(toInsert);
	}
	private Product getProductByID(String id)
	{
		for (Product product : mCatalog) {
			if(product.equalByID(id))
				return product;
		}
		return null;
	}
	/**
	 * insert the info of selected product to the text Field 
	 */
	@FXML
	private void showSelectedProduct() {
		String value=cmbSelectProduct.getValue();
		value=value.substring(value.indexOf(':')+1, value.indexOf(','));
		Product selectedProduct= getProductByID(value);
		txtProductID.setText(selectedProduct.getProductID());
		txtProductName.setText(selectedProduct.getProductName());
		txtProductType.setText(selectedProduct.getProductType());
		txtProductPrice.setText(selectedProduct.getProductPrice().toString());
		txtProductColor.setText(selectedProduct.getDominantColor());
		txtProductUrlImage.setText(selectedProduct.getImageName());
		Image img;
		if(selectedProduct.getPicture() != null)
		{
			img = selectedProduct.getPicture().getImage();
			imgView.setImage(img);
		}
	}

	/**
	 *  Handle message from database after a request
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void handleMessageFromServer(Object message) {
		System.out.println("> "+message);
		Platform.runLater(() -> mProgressBox.setVisible(false));
		if (message instanceof String) {
			if (message.equals("uploaded"))
			{
				ArrayList<Object> params  = new ArrayList<Object>();
				if (!addingNew) {
					params.add(txtProductID.getText());
					params.add(txtProductName.getText());
					params.add(txtProductType.getText());
					params.add(txtProductPrice.getText());
					params.add(txtProductUrlImage.getText());
					params.add(txtProductColor.getText());
					mConnectionController = new ConnectionController(this);
					ClientToServerMessage clientToServerMessage = new ClientToServerMessage(EQueryOption.UPDATE_PRODUCT_INFO,params, null);
					Platform.runLater(() -> mProgressBox.setVisible(true));
					mConnectionController.sendToServer(clientToServerMessage);
				}
				else {
					 params.add(txtProductName.getText());
					 params.add(txtProductType.getText());
					 params.add(txtProductPrice.getText());
					 params.add(txtProductUrlImage.getText());
					 params.add(txtProductColor.getText());
					 mConnectionController = new ConnectionController(this);
					 ClientToServerMessage clientToServerMessage = new ClientToServerMessage(EQueryOption.ADD_NEW_PRODUCT, params, null);
					 Platform.runLater(() -> mProgressBox.setVisible(true));
					 mConnectionController.sendToServer(clientToServerMessage);
				}
				return;
			}
			if (message.equals("removed") ) 	//case when remove product from DB
			{
				Platform.runLater(() ->WindowFactory.showInfoDialog("Confiramtion", "", "Remove Success" ,previousWindow ,  UserController.getConnectedUserID()));
				return;
			}
			
			if (message.equals("no results found")) {						//case when update product 
				Platform.runLater(() -> WindowFactory.showInfoDialog("Product succesfully updated", null, 
						"Product succesfully updated",previousWindow,UserController.getConnectedUserID()));
				return;
			}
		}
		if(message instanceof ArrayList<?> )
		{
			mCatalog=(ArrayList<Product>)message;
			insertProductsToCmb();
			
		}
		
	}

}	// ProductController
 