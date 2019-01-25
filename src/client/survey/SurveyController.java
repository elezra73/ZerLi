package client.survey;

import java.util.ArrayList;
import client.ConnectionController;
import client.IController;
import client.WindowFactory;
import common.ClientToServerMessage;
import common.EQueryOption;
import common.IClient;
import common.ZerliDate;
import common.users.AUser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class SurveyController implements IClient,IController {
	
	@FXML
	private ComboBox<String> customersCombo;
    @FXML
    private TextArea txtQ3;

    @FXML
    private TextArea txtQ4;

    @FXML
    private TextArea txtQ1;

    @FXML
    private ComboBox<Integer> cbQ6;

    @FXML
    private TextArea txtQ2;

    @FXML
    private ComboBox<Integer> cbQ4;

    @FXML
    private ComboBox<Integer> cbQ5;

    @FXML
    private TextArea txtQ5;

    @FXML
    private ComboBox<Integer> cbQ2;

    @FXML
    private TextArea txtQ6;

    @FXML
    private ComboBox<Integer> cbQ3;

    @FXML
    private ComboBox<Integer> cbQ1;

    @FXML
    private Button btnSubmit;
    
    @FXML
    private Button backBTN;
    @FXML
    private AUser user;
    final static String survyid = "1234";
    private boolean initial_combo = false;
	private ConnectionController mConnectionController;


	/**
	 * get all the answers on the survey and return Survey object in order to insert to DB
	 * @param ans1 answer1
	 * @param ans2 answer2
	 * @param ans3 answer3
	 * @param ans4 answer4
	 * @param ans5 answer5
	 * @param ans6 answer6
	 * @return object survey ready send to server
	 */
	public ArrayList<Object> getSurveyController ( int ans1, int ans2, int ans3,int ans4, int ans5, int ans6)
	{
		ArrayList<Object> surveyToServer = new ArrayList<Object>();
		surveyToServer.add(survyid);
		String id = customersCombo.getValue().substring(4, customersCombo.getValue().indexOf(','));
		surveyToServer.add(id);
		surveyToServer.add(this.user.getStoreID()); 		
		ZerliDate date = new ZerliDate();
		surveyToServer.add(date.getFullDate().toString());				
		surveyToServer.add(ans1 + "");
		surveyToServer.add(ans2+ "");
		surveyToServer.add(ans3+ "");
		surveyToServer.add(ans4+ "");
		surveyToServer.add(ans5+ "");
		surveyToServer.add(ans6+ "");
		surveyToServer.add("1");
		return surveyToServer;
	}
	@FXML
	
	/**
	 * submit click - handle full survey to DB
	 */
    private void btnSubmitClicked()
    {
    		if(checkFields()==false)
    		{
    			WindowFactory.showErrorDialog("Warning", "Answer Missing!", "Please answer all the question");
    			return;
    		}
    		if(customersCombo.getValue() == null)
    		{
    			WindowFactory.showErrorDialog("Warning", "Please choose customer", "Please choose customer");
    			return;
    		}
    		ArrayList<Object> params = new ArrayList<Object>();
    		params = getSurveyController(cbQ1.getValue(),cbQ2.getValue(),cbQ3.getValue(),cbQ4.getValue(),cbQ5.getValue(),cbQ6.getValue());
    		
    		mConnectionController = new ConnectionController(this);
    		ClientToServerMessage clientToServerMessage = new ClientToServerMessage(EQueryOption.ADD_FILLED_SURVEY, params,null);//TODO:QUERY
    		mConnectionController.sendToServer(clientToServerMessage);
    }
    
    /**
     * check validate of Survey. return true if user complete answering the survey
     * @return true if all the fields are not empty
     */
	
	
    private boolean checkFields()
	{
		if(cbQ1.getValue() == null  || cbQ2.getValue() == null || cbQ3.getValue() == null || cbQ4.getValue() == null
				|| cbQ5.getValue() == null || cbQ6.getValue() == null )
		 {
			 return false;
		 }
		return true;
	}

    
    /**
	 * redirect to the previous window
	 * 
	 */
    
    @FXML
    private void backBTNPressed()
    {
    	WindowFactory.show("/client/mainmenu/store_worker_main_menu.fxml", user.getUserID());
    }
    
    
    /**
     * initial all the combo box with answers 1 to 10
     */
    @FXML
    private void setComboBoxValues()
    {
    	if(this.initial_combo == false)
    	{
    		this.initial_combo  = true;
    			for (int i = 1; i<11; i++)
    			{
    				cbQ1.getItems().add(i);
    				cbQ2.getItems().add(i);
    				cbQ3.getItems().add(i);
    				cbQ4.getItems().add(i);
    				cbQ5.getItems().add(i);
    				cbQ6.getItems().add(i);
    		}
    	}
		
    }
    
    /**
     * get from DB all the customers of specific store
     */
    
    private void getCustomersByStoreID()
    {
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(this.user.getStoreID());
		mConnectionController = new ConnectionController(this);
		ClientToServerMessage clientToServerMessage = new ClientToServerMessage(EQueryOption.GET_CUSTOMERS_NAME_BY_STOREID, params, "customersnamesbystoreid");//TODO:QUERY
		mConnectionController.sendToServer(clientToServerMessage);
    }
    
    
    /**
     * open new dialog and set submit button on if the customer want to answer the survey, and disable if not. 
     */
	
/*	@FXML
	public void verifyAnswerSurvey()
	{
		boolean Answer = WindowFactory.showYesNoDialog("Do you want to fill this survey?", null, "Do you want to fill this survey?");
		if(Answer == false)
			btnSubmit.setDisable(true);
		else
			btnSubmit.setDisable(false);
	}
	*/

    
    
	/**
 	 * setDetails - handle the result from previous controller
 	 */
 	
	@Override
	public void setDetails(Object... objects) 
	{
		this.user = (AUser) objects[0];
		getCustomersByStoreID();
	}

	/**
	 * @param message return from server
	 *  handleMessageFromServer - handle the result from server. expected 2 different results
	 */ 
	
	@Override
	public void handleMessageFromServer(Object message)
	{
		if(message instanceof ArrayList<?>)
		{
			@SuppressWarnings("unchecked")
			ArrayList<String> customers = (ArrayList<String>)message;
			customersCombo.getItems().clear();
			for(int i=0;i<customers.size();i++)
				customersCombo.getItems().add(customers.get(i));
		}
		
		if(message instanceof String && message.equals("no results found"))
			Platform.runLater(()->WindowFactory.showInfoDialog( "upload succeeded", "your survey successfuly added to DB", null,"/client/mainmenu/store_worker_main_menu.fxml",user.getUserID()));
	}
		
}

    
    
    


