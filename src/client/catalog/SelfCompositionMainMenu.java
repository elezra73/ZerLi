package client.catalog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import client.ConnectionController;
import client.IController;
import client.WindowFactory;
import common.ClientToServerMessage;
import common.EQueryOption;
import common.IClient;
import common.Product;
import common.users.Customer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SelfCompositionMainMenu implements IClient, IController {

	@FXML
	private Label pName;
	@FXML
	private Label pPrice;
	@FXML
	private Label pType;

	@FXML
	private ImageView resultImg;
	@FXML
	private TextField fromBtn;
	@FXML
	private TextField toBtn;
	
    @FXML
    private ComboBox<String> kind_combo;

    @FXML
    private ComboBox<String> color_combo;
    
    
    @FXML 
    private Button searchBtn;
    
    @FXML 
    private Button backBtn;
    private static boolean searchFlag = false;
	private ArrayList<Product> self_composition_products = new ArrayList<Product>();
	private ArrayList<Product> products_after_filters = new ArrayList<Product>();
	private Product chosenProduct;
	private ConnectionController mConnectiontController;
	private Product productAfterFilters = new Product();
	private boolean kind_flag=false;
	private boolean color_flag=false;
	private Customer mCustomer;
	private Double fromValue = -1.0;
	private Double toValue = -1.0;
	
	public SelfCompositionMainMenu() {}
	

	/**
     * get catalog of selfProducts from the DB.
     */
	
	private void getSelfProductFromDB()
	{
      	
       	ArrayList<Object> params = new ArrayList<Object>();
       	ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.GET_CATALOG,params,"catalog");		//get all the products (network catalog)
      	mConnectiontController = new ConnectionController(this);	// must do to send msg to server - client (oscf service)
		mConnectiontController.sendToServer(msg);    	// the msg that we sending to the server
		
	}

	/**
	 * get all the products that on sale from specific store
	 * @param ProductStoreID get storeID
	 */
	
 	private void getPercent(String ProductStoreID)		
   	{
   	ArrayList<Object> params = new ArrayList<Object>();
   	params.add(ProductStoreID);		
   	ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.GET_PRODUCT_PERCENT_BY_STOREID,params,"percent_price_product_by_storeid");		
   	mConnectiontController = new ConnectionController(this);	
	mConnectiontController.sendToServer(msg);    
   	}
	
 	
 	/**
 	 * handle comboBox : set all the product kind from DB to combo box
 	 */
	
 	public void kind_combo()
 	{
 		if(this.kind_flag == false)
 		{
 			kind_combo.setValue(" ");
 		HashMap<String,Integer> ProductTypes = new HashMap<String,Integer>();
 		for(int i=0;i<self_composition_products.size();i++)
 			ProductTypes.put(self_composition_products.get(i).getProductType(), 1);
 		for (String key : ProductTypes.keySet()) 
 			kind_combo.getItems().add(key);
 		this.kind_flag = true;
 		}
 	}
 

 	/**
 	 * handle comboBox :
 	 * set all the product colors from DB to combo box
 	 */
 	
 	public void dominant_color()
 	{
 		if(this.color_flag == false)
 		{
 			color_combo.getItems().clear();
	 		HashMap<String,Integer> colors = new HashMap<String,Integer>();
	 		for(int i=0;i<self_composition_products.size();i++)
	 			colors.put(self_composition_products.get(i).getDominantColor(), 1);
	 		for (String key : colors.keySet()) 
	 			color_combo.getItems().add(key);
	 		this.color_flag = true;
 		}
 	}
	
 	/**
 	 * handle search : find match between the user input to the current products in the store
 	 * if input is valid and found match, create ArrayList of Products and send it to next FXML 
 	 * in order to display the result.
 	 * 
 	 */
 	
 	public void searchPressed()
 	{
 		boolean color_ignore = true;
 		if((kind_combo.getValue() != " " && kind_combo.getValue() != null))
 		{
 			productAfterFilters.setProductType(kind_combo.getValue());		
 			try {															
 	 			fromValue = Double.parseDouble(fromBtn.getText());
 	 			toValue = Double.parseDouble(toBtn.getText());
 	 		}
 	 		catch(Exception e)
 	 		{
 	 			WindowFactory.showErrorDialog( "error", "error",  "please enter numbers only") ;
 	 			return;
 	 		}
 			
 			if((fromValue > toValue) || (fromValue < 0 ) || (toValue < 0))
 			{
 				WindowFactory.showErrorDialog( "error", "error",  "please change price range") ;
 				return;
 			}
 			if(this.color_flag != false && color_combo.getValue() != " ")
 			{
 				productAfterFilters.setDominantColor(color_combo.getValue());
 				color_ignore = false;
 			}
 			
 			for(int i=0;i<self_composition_products.size();i++)
 			{
 				if(self_composition_products.get(i).getProductType().equals(productAfterFilters.getProductType()))		// the same kind of product
 				{
 					Double ProductPrice = self_composition_products.get(i).getProductPrice();
 					if(ProductPrice >= fromValue && ProductPrice <= toValue)		//
 					{
 						if (color_ignore == true)
 						{
 							products_after_filters.add(self_composition_products.get(i));
 						}
 						else if(self_composition_products.get(i).getDominantColor().equals(color_combo.getValue()))
 								products_after_filters.add(self_composition_products.get(i));
 					}
 				}				
 					
 					
 			}
 			
 			if(products_after_filters.size() == 0)
 			{
 				WindowFactory.showErrorDialog( "Msg", "We couldn't find any match in our system",  "please try again") ;
 				return;
 			}
 			SelfCompositionMainMenu.searchFlag = true;
 			WindowFactory.show("/client/catalog/result_after_search.fxml" , this.mCustomer ,products_after_filters );
 		}
 		else
 		{
 			WindowFactory.showErrorDialog( "error", "error",  "you didnt press yet") ;
 		}
 	}
	
 	 /**
     * redirect to the previous window
     */
 	
 	public void backPressed()
 	{
 		WindowFactory.show("/client/mainmenu/customer_main_menu.fxml" , this.mCustomer.getUserID());
 	}
 	
 	 /**
     * redirect to the previous window
     */
 	
 	public void backFromResultSearchPressed()
 	{
 		SelfCompositionMainMenu.searchFlag = false;
 		WindowFactory.show("/client/catalog/self_composition.fxml" , this.mCustomer);
 	}
 	
 	 /**
     * display the result to 
     */
 	
 	
 	/**
 	 * find the best choise for the user after filters
 	 * display the result
 	 */
 	
 	public void handleResultAfterSearch()			
 	{
 		Random rand = new Random();
 		Image img;
 		boolean addFlag = false;
 		int choosen_index;
 		do {
 		choosen_index = rand.nextInt(products_after_filters.size());		//size is the maximum 
		if(this.self_composition_products.contains(products_after_filters.get(choosen_index)) == false)		//adding rand suggestion item
		{
			this.self_composition_products.add(products_after_filters.get(choosen_index));		//add suggestion to tmp array
			addFlag = true;
			if(this.self_composition_products.size() == this.products_after_filters.size())	// all the suggestions already displayed
				this.self_composition_products.clear();											// initial in order to get new suggetion item
		}
 		}while(addFlag == false);
		if(this.products_after_filters.get(choosen_index).getImageName() != null)
		{
			img = products_after_filters.get(choosen_index).getPicture().getImage();
			resultImg.setImage(img);
		}
		pName.setText("Product Name: " + this.products_after_filters.get(choosen_index).getProductName());
		String formattedData = String.format("%.02f", this.products_after_filters.get(choosen_index).getProductPrice());
		pPrice.setText("Product Price: " + formattedData);
		pType.setText("Product Type: " + this.products_after_filters.get(choosen_index).getProductType());
		
		this.chosenProduct = this.products_after_filters.get(choosen_index);			// current suggestion product
 	}

 	/**
 	 * redirect to the next window and send the chosen items by the user
 	 */
 	
 	public void continuePress()
 	{
 			Map<Product, Integer> fo = new HashMap<Product, Integer>();
 			fo.put(this.chosenProduct, 1);
 			searchFlag = false;
 			WindowFactory.show("/client/order/order_details.fxml",this.mCustomer, fo,this.chosenProduct.getProductPrice() , "/client/catalog/self_composition.fxml");
 	}
 	
 	/**
 	 * setDetails - handle the result from previous controller
 	 */
 	
	@SuppressWarnings("unchecked")
	@Override
	public void setDetails(Object... objects) 
	{
		this.mCustomer = (Customer)objects[0];
		if(searchFlag == false)
		{
			WindowFactory.mStage.setTitle("Self Composition");
			getSelfProductFromDB();
			getPercent(this.mCustomer.getStoreID());
		}
		else
		{
			WindowFactory.mStage.setTitle("Self Composition Selection");
			this.products_after_filters = (ArrayList<Product>)objects[1];
			this.self_composition_products.clear();				//clear the arraylist to handle diffrent suggestion		
			handleResultAfterSearch();
		}
		

	}


/**
 * handleMessageFromServer - handle the result from server. expected 2 different results
 */
	
	@Override
	public void handleMessageFromServer(Object message) 
	{
		System.out.println(">Answer from server: "+message);
		
		if (message instanceof String)
		{
			Platform.runLater(()-> WindowFactory.showErrorDialog("cannt get data from DB", "try again",(String)message));
			return;
		}
		
		if(message instanceof ArrayList<?>)			//get all the products to display the self_composition_products
		{
			@SuppressWarnings("unchecked")
			ArrayList<Product> resultFromServer1 = (ArrayList<Product>)message;		//casting from message to arraylist<product>
			for(int i=0;i<resultFromServer1.size();i++)
			{
				self_composition_products.add(resultFromServer1.get(i));
			//	self_composition_products.get(i).mProductType = self_composition_products.get(i).mProductType.substring(5);		//remove 'self_' from each productType
			}
			//products_after_filters = self_composition_products;
		}

		if(message instanceof HashMap)				// get all the products on sale for specific self_composition_products
		{
			double original_price , afterDiscount;
			@SuppressWarnings("unchecked")
			HashMap<String,Double> resultFromServer2 = (HashMap<String,Double>)message;		//casting from message to arraylist<product>
			for(int i=0;i<self_composition_products.size();i++)
			{
				if(resultFromServer2.containsKey(self_composition_products.get(i).getProductID()))
				{
					Double percent = resultFromServer2.get(self_composition_products.get(i).getProductID());			//the percent for specific product
					original_price = self_composition_products.get(i).getProductPrice();
					afterDiscount = original_price - (original_price *percent / 100 ) ;			//calculate discount
					self_composition_products.get(i).setProductPrice(afterDiscount);				//save the discount for the product
				}	
			}	
		}

	}
}

