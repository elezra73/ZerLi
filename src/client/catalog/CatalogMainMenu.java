package client.catalog;
/**
 * Sample Skeleton for 'catalog_main_menu.fxml' Controller Class
 */

import java.util.ArrayList;
import java.util.HashMap;
import client.ConnectionController;
import client.IController;
import client.WindowFactory;
import common.ClientToServerMessage;
import common.EQueryOption;
import common.IClient;
import common.Product;
import common.users.Customer;
import common.users.AUser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class CatalogMainMenu implements IClient, IController {

	HashMap<Product, Integer> cart;		//key - product id, value - num of product
	private ConnectionController mConnectiontController;		// connection handler.
	private ArrayList<Product> catalog;
	HashMap<String , Boolean> productOnSale;							// get from DB the products from specific catalog that on sale
    private boolean listP = false;
    private Product selectedProduct;
    private Double total_price;
	private AUser mUser;
	
    @FXML
    private VBox mProgressBox;
	@FXML
	private Label typeLabel;
	@FXML
	private Label price;
	@FXML
	private Button checkOutBtn;
	@FXML
	private Label FirstTitle,saleLabel;
    @FXML
    private ImageView viewProduct;
    
    @FXML
    private TextField totalTextField;
    
    @FXML
    private ListView<String> listCart;

    @FXML
    private ListView<String> listProduct;

    @FXML
    private Button removeBtn;

    @FXML
    private Button backBtn;

    @FXML
    private Button addBtn;


    public CatalogMainMenu ()
    {
    	this.total_price = 0.0;
       	cart = new HashMap<Product, Integer>();
    	catalog = new ArrayList<Product>();
    	productOnSale = new HashMap<>();
    }
   


	/**
     * get  basic catalog from the DB.
     */

       
       	private void getCatalogForCustomerFromDB()		
       	{
       	ArrayList<Object> params = new ArrayList<Object>();
       	ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.GET_CATALOG,params,"catalog");		
      	mConnectiontController = new ConnectionController(this);
      	mProgressBox.setVisible(true);
		mConnectiontController.sendToServer(msg);    	
       		
       }
   
       	/**
       	 * send customer store id to server in order to get products on sale for specific store - getPercent 
       	 * @param customerStoreID store id
       	 */
       	private void getPercent(String customerStoreID)		
       	{
       	ArrayList<Object> params = new ArrayList<Object>();
       	params.add(customerStoreID);				
       	ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.GET_PRODUCT_PERCENT_BY_STOREID,params,"percent_price_product_by_storeid");		
       	mConnectiontController = new ConnectionController(this);
       	mProgressBox.setVisible(true);
		mConnectiontController.sendToServer(msg);    	
       		
       }
    

    /**
     * redirect to the previous window
     */

    public void backPressed()
    {
     	WindowFactory.show("/client/mainmenu/customer_main_menu.fxml", mUser.getUserID());
    }

    /**
     * initial catalog list view and display all the product to the screen
     */
    
    
	public void displayProduct()		
	{
		if(listP == false)
		{
			for(Product pr : catalog)
				listProduct.getItems().add(pr.getProductName());
				listProduct.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		}
		listP = true;
	}

	  /**
     * handle when user select item from the catalog
     */
	
	public void selectedFromCatalog()			
	{
		viewProduct.setImage(null);
		String ctlg = listProduct.getSelectionModel().getSelectedItem();
		if(ctlg != null)	{
			selectedProduct = catalog.get(listProduct.getSelectionModel().getSelectedIndex());
			Image img;
			if(selectedProduct.getPicture() != null)
			{
				img = selectedProduct.getPicture().getImage();
				viewProduct.setImage(img);
			}
			saleLabel.setVisible(productOnSale.get(selectedProduct.getProductID()));	
			System.out.println(selectedProduct.getProductPrice());		// need to present the
			String formattedData = String.format("%.02f", selectedProduct.getProductPrice());
			price.setText(formattedData + " ils");
			typeLabel.setText("Type: " + selectedProduct.getProductType());
			return;
		}
	
	}

	  /**
     * handle when user want to add chosen item from the catalog 
     */
	
	public void addToCart()
	{
		
		int index,CartListindex;
		if(selectedProduct == null)
		{
			WindowFactory.showErrorDialog("error", "error", "please choose product first");
			return;
		}
		if(cart.containsKey(selectedProduct) == false)		//if the cart already have this product
		{
			cart.put(selectedProduct,1);
			listCart.getItems().add(selectedProduct.getProductName());		// adding the product to cart
		}
		else		//need to fix adding numbers of same product. and also remove
		{		//need to find the correct index in listCart , then it will work
			//listCart.getSelectionModel().getSelectedItem()
			for(int i=0; ; i++)
			{
				String tmp = listCart.getItems().get(i);
				CartListindex = tmp.indexOf('X');
				if(CartListindex != -1)
					tmp = tmp.substring(0, CartListindex - 1);
				if (selectedProduct.getProductName().equals(tmp) == true)
				{
					index = i;
					break;
				}
			}
			
			cart.put(selectedProduct, cart.get(selectedProduct) + 1);
		    listCart.getItems().remove(index);
		    listCart.getItems().add(index, selectedProduct.getProductName() + " X"+ cart.get(selectedProduct));	
		}
		System.out.println("current cart size is  = " + cart.size());
		SetTotalPrice(selectedProduct ,"inc");
	
	}
	
	  /**
     * handle when user want to remove chosen item from the cart 
     */
	
	public void removeFromCart()		
	{
		int index , CartListindex;
	//	String productName =" ";
		if(cart.isEmpty() == true )
		{
			WindowFactory.showErrorDialog("error", "error", "Your cart is empty");
			return;
		}		
		Product LocalselectedProduct = null;
		String crt = listCart.getSelectionModel().getSelectedItem();
		if(crt != null)
		{
			CartListindex = crt.indexOf('X');
			if(CartListindex != -1)
				crt = crt.substring(0, CartListindex - 1);
			index = 0;
			for(int i=0;i<catalog.size();i++)
			{
				if(catalog.get(i).getProductName().equals(crt))
				{
					//productName = catalog.get(i).mProductName;
					index = i;
					break;
				}
			}
			LocalselectedProduct = catalog.get(index);
			System.out.println(LocalselectedProduct);		// need to present the
		}
		else
		{
			WindowFactory.showErrorDialog("error", "error", "Yout must choose item from cart in order to remove");
			return;
		}
		

	    if(cart.get(LocalselectedProduct) == 1)
		{
			cart.remove(LocalselectedProduct);
			listCart.getItems().remove(listCart.getSelectionModel().getSelectedIndex());
		}
	    
		else
		{
			int tmpIndex;
			for(int i=0; ; i++)
			{
				String t = listCart.getItems().get(i);
				tmpIndex = t.indexOf('X');
				if(tmpIndex == -1)
					continue;
				t = t.substring(0, tmpIndex - 1);		// there's a problem here. Must find it.
				if (LocalselectedProduct.getProductName().equals(t) == true)
				{
					CartListindex = i;
					break;
				}
			}
			cart.put(LocalselectedProduct, cart.get(LocalselectedProduct) -1);
			listCart.getItems().remove(CartListindex);
			if(cart.get(LocalselectedProduct) == 1)
				listCart.getItems().add(CartListindex, LocalselectedProduct.getProductName());
			else
				listCart.getItems().add(CartListindex, LocalselectedProduct.getProductName() + " X"+ cart.get(LocalselectedProduct));
				
	    	//listCart.getItems().remove(index);
	}
			System.out.println("current cart size is  = " + cart.size());
			SetTotalPrice(LocalselectedProduct , "dec");
		
	}

	/**
	 * calculate total price and display it to the user
	 * @param product selected by user
	 * @param operation -&gt; add to the cart or remove from the cart
	 */
	
	public void SetTotalPrice(Product product , String operation)
	{
		if(operation.equals("inc"))
			total_price = total_price  + product.getProductPrice();
		if(operation.equals("dec"))
			total_price = total_price  - product.getProductPrice();
		String formattedData = String.format("%.02f", total_price);
		totalTextField.setText(formattedData);
	}
	
	/**
	 * handle checkout press.
	 */
	
	public void checkOutPress()
	{
		if(this.total_price == 0)
		{
			WindowFactory.showInfoDialog( "Warning", "Your cart is empty",  "you must choose product first") ;
			return;
		}
		WindowFactory.show("/client/order/order_details.fxml",this.mUser, cart,total_price , "/client/catalog/ctalogView.fxml");
	}


	
	/**
	 *  handleMessageFromServer - handle the result from server. expected 2 different results
	 */
	
	
	@Override
	public void handleMessageFromServer(Object message)  
	{
		System.out.println(">Answer from server: "+message);
		Platform.runLater(() -> mProgressBox.setVisible(false));
		if (message instanceof String)
		{
			Platform.runLater(()-> WindowFactory.showErrorDialog("cannt get data from DB", "try again",(String)message));
			return;
		}
		
		if(message instanceof ArrayList<?>)			//get all the products to display the catalog
		{
			@SuppressWarnings("unchecked")
			ArrayList<Product> resultFromServer = (ArrayList<Product>)message;		//casting from message to arraylist<product>
			for(int i=0;i<resultFromServer.size();i++)
				catalog.add(resultFromServer.get(i));
			return;
		}

		if(message instanceof HashMap)				// get all the products on sale for specific catalog
		{
			double original_price , afterDiscount;
			@SuppressWarnings("unchecked")
			HashMap<String,Double> resultFromServer = (HashMap<String,Double>)message;		//casting from message to arraylist<product>
			for(int i=0;i<catalog.size();i++)
			{
				if(resultFromServer.containsKey(catalog.get(i).getProductID()))
				{
					
					productOnSale.put(catalog.get(i).getProductID(),true);
					Double percent = resultFromServer.get(catalog.get(i).getProductID());			//the percent for specific product
					original_price = catalog.get(i).getProductPrice();
					afterDiscount = original_price - (original_price *percent / 100 ) ;			//calculate discount
					catalog.get(i).setProductPrice(afterDiscount);				//save the discount for the product
				}
				else 
					productOnSale.put(catalog.get(i).getProductID(),false);
			}
			Platform.runLater(()-> displayProduct());
		}

	
	}

/**
 * setDetails - handle the result from previous controller
 */
	
	@Override
	public void setDetails(Object... objects) 
	{
		if(objects.length == 1)					//case 1 - display catalog
		{
			this.mUser = (AUser)objects[0];				// receive customer 
			getCatalogForCustomerFromDB();				// get basic catalog from DB
			getPercent(this.mUser.getStoreID());			// get products on sale - specific store
			if(((Customer)mUser).isPayable() == false ||((Customer)mUser).getStoreID().equals("general"))			// only watching -> cannot make order
			{
				WindowFactory.showInfoDialog( "Warning", "Cannot make order",  "you must have Payable account to make order") ;
				addBtn.setDisable(true);
				removeBtn.setDisable(true);
				checkOutBtn.setDisable(true);	
			}
		}
		WindowFactory.mStage.setTitle("Catalog");
		WindowFactory.mStage.setResizable(false);
	}


}
