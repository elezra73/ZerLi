package client.survey;

import java.util.ArrayList;

import client.ConnectionController;
import client.IController;
import client.WindowFactory;
import common.ClientToServerMessage;
import common.EQueryOption;
import common.IClient;
import common.Survey;
import common.users.AUser;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SurveyProfessionalController implements IController, IClient
{
	   @FXML
	    private TableView<Survey> surveyTable;

	    @FXML
	    private TableColumn<Survey,String> colStoreID;

	    @FXML
	    private TableColumn<Survey,String> colCustomerID;

	    @FXML
	    private TableColumn<Survey,Integer> colmA1;

	    @FXML
	    private TableColumn<Survey,Integer> colQ2;

	    @FXML
	    private TableColumn<Survey,Integer> colQ3;

	    @FXML
	    private TableColumn<Survey,Integer> colQ4;

	    @FXML
	    private TableColumn<Survey,Integer> colQ5;

	    @FXML
	    private TableColumn<Survey,Integer> colQ6;

	    @FXML
	    private TableColumn<Survey,String> colConclusion;

	    @FXML
	    private ComboBox<String> storeCMB;

	    @FXML
	    private Button backBTN;

	    @FXML
	    private Button ReviewBTN;
	    private ConnectionController mComplaintController;
	    private ObservableList<Survey> ObservableSurvey;
	    private Survey selectedByUser;
	    private AUser user;
	    ArrayList<Survey> Surveyis;
	    
	    /**
	     * get from DB the surveys of user chosen
	     */
	    @FXML
	    public void getStoreFromCMB()
	    {
	    	for ( int i = 0; i<surveyTable.getItems().size(); i++) 
	    		surveyTable.getItems().clear();
	    	String store = storeCMB.getSelectionModel().getSelectedItem();
	    	if(store.equals("All stores") == true)
	    		{
	    	 	ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.GET_All_SURVEY_ANSWER,null,"allsurveyanswer");	
		       	mComplaintController = new ConnectionController(this);	// must do to send msg to server - client (oscf service)
		       	mComplaintController.sendToServer(msg);    	// the msg that we sending to the server
	    		}
	    	else
	    	{
	    		ArrayList<Object> params = new ArrayList<Object>();
	    		params.add(store);
	    	 	ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.GET_SURVEY_FROM_SPECIFIC_STORE,params,"surveyanswerbyspecificstore");	
		       	mComplaintController = new ConnectionController(this);	// must do to send msg to server - client (oscf service)
		       	mComplaintController.sendToServer(msg);    	// the msg that we sending to the server
	    	}
	    	
	    }
	    
	    /**
	     * function that receive all the stores from DB
	     */
	    public void getStoresFromServer()
		{
	       
	       	ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.GET_ALL_STORES,null,"storsbyid");	
	       	mComplaintController = new ConnectionController(this);	// must do to send msg to server - client (oscf service)
	       	mComplaintController.sendToServer(msg);    	// the msg that we sending to the server
			
		}
	
	    /**
	     * insert all the stores from DB to the combo box
	     * @param allStores ArrayList of all the stores
	     */
	    public void setStoreToCMB(ArrayList<String> allStores)			//insert Stores to DB
		{
	    		
				for(int i=0;i<allStores.size();i++)
					storeCMB.getItems().add(allStores.get(i));
		}
	    
	    /**
	     * set all the survies that return from DB by user chosen : specific store / all the stores
	     * @param Surveyis ArrayList of Survey
	     */
	    
	    public void setTableValuesFromServer(ArrayList<Survey> Surveyis)
	    {
	    	for ( int i = 0; i<surveyTable.getItems().size(); i++) 
	    		surveyTable.getItems().clear();
	    	ObservableSurvey = FXCollections.observableArrayList(Surveyis);
			surveyTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			colStoreID.setCellValueFactory(new PropertyValueFactory<Survey,String>("StoreID"));
			colCustomerID.setCellValueFactory(new PropertyValueFactory<Survey,String>("CustomerID"));
			colmA1.setCellValueFactory(new PropertyValueFactory<Survey,Integer>("A1"));
			colQ2.setCellValueFactory(new PropertyValueFactory<Survey,Integer>("A2"));
			colQ3.setCellValueFactory(new PropertyValueFactory<Survey,Integer>("A3"));
			colQ4.setCellValueFactory(new PropertyValueFactory<Survey,Integer>("A4"));
			colQ5.setCellValueFactory(new PropertyValueFactory<Survey,Integer>("A5"));
			colQ6.setCellValueFactory(new PropertyValueFactory<Survey,Integer>("A6"));
			colConclusion.setCellValueFactory(new PropertyValueFactory<Survey,String>("Conclusuon"));
			surveyTable.setItems(ObservableSurvey);
	    }
	
	    /**
	     * handle when user select item from the table view
	     */
	    public void selectedFromTable()
	    {
	    	this.selectedByUser = surveyTable.getSelectionModel().getSelectedItem();
	    	ReviewBTN.setDisable(false);
	    	System.out.println(selectedByUser.getCustomerID());
	    }
	    
	    
	    /**
	     * The Expert can add his review by pressing Add Button. this func handle it
	     */
	    public void AddReviewPressed()
	    {
	    	String review = WindowFactory.showInputField("Professional Conclusion","enter your expert opinion" );
	    	if(review == null)
	    		return;
	    	this.Surveyis.get(this.Surveyis.indexOf(selectedByUser)).setConclusuon(review);
	    //	selectedByUser.setConclusuon(review);
	    	setTableValuesFromServer(this.Surveyis);
	    	System.out.println(review);
	    	ArrayList<Object> params = new ArrayList<Object>();
	    	params.add(selectedByUser.getIncrementID());
	    	params.add(selectedByUser.getSurveyID());
	    	params.add(selectedByUser.getCustomerID());
	    	params.add(review);
	     	ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.SURVEY_ADD_PROFESSIONAL_REVIEW ,params,"storsbyid");	
	       	mComplaintController = new ConnectionController(this);	// must do to send msg to server - client (oscf service)
	       	mComplaintController.sendToServer(msg);    	// the msg that we sending to the server
	       	ReviewBTN.setDisable(true);
	    }
	    
	    
	    /**
		 * redirect to the previous window
		 * 
		 */
	    public void backPressed()
	    {
	    	WindowFactory.show("/client/mainmenu/customer_service_main_menu.fxml" , user.getUserID());
	    }
		/**
		 * @param message
		 *  handleMessageFromServer - handle the result from server. expected 2 different results
		 */ 
	    @SuppressWarnings("unchecked")
	@Override
	public void handleMessageFromServer(Object message) 
	{
		if(message instanceof ArrayList<?> && ((ArrayList<?>) message).get(0) instanceof String)		//recive from DB all the Stores
			{
		    	ArrayList<String> allStores;
				allStores = (ArrayList<String>)message;
				Platform.runLater(() ->setStoreToCMB(allStores));
			}
		
		if(message instanceof ArrayList<?> && ((ArrayList<?>)message).get(0) instanceof Survey)
		{
			this.Surveyis = new ArrayList<Survey>();
			for(int i=0;i<((ArrayList<Survey>)message).size();i++)
				if(((ArrayList<Survey>)message).get(i).getIsActive().equals("1"))
				{
					this.Surveyis.add(((ArrayList<Survey>)message).get(i));
				}
			Platform.runLater(() -> setTableValuesFromServer(this.Surveyis));
				
		}
	}

		
	    
	    
	    
	    /**
	 	 * setDetails - handle the result from previous controller
	 	 */
	@Override
	public void setDetails(Object... objects)
	{
		this.user = (AUser) objects[0];
		storeCMB.getItems().add("All stores");
		getStoresFromServer();
		
		
	}



	
}	
