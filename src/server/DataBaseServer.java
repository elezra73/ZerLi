// DataBaseServer

package server;


import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import common.ClientToServerMessage;
import common.Complaint;
import common.CustomerOrder;
import common.EQueryOption;
import common.Product;
import common.SendableFile;
import common.Store;
import common.Survey;
import common.reports.Report;
import common.reports.ReportDetailInOrderReport;
import common.reports.ReportOrder;
import common.users.UserLoginInfo;
import ocsf.server.*;
import server.reports.CollectionTableForReports;
import server.reports.OrderProduct;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 * 
 * @version 06 December 2017
 */
public class DataBaseServer extends AbstractServer  {
	
	public final static String IMAGES_DIR = System.getProperty("user.dir") + File.separator + 
			"ServerFiles" + File.separator + "Images" + File.separator;
	private final static int DEFAULT_PORT = 5555;
	public static int mListeningPort;
	private DataBaseHandler dbHandler;		// The SQL DataBase Handler.
	private TreeSet<String> mLoggedInUsers;		// Maintain a set of logged in users to the server.
	@SuppressWarnings("unused")
	private ZerliTimer mZerliTimer;		// Server's timers.
  
	/**
	 * 
	 * @param port listening port
	 */
	public DataBaseServer(int port) {
		super(port);
		mLoggedInUsers = new TreeSet<String>();
		dbHandler = new DataBaseHandler();
		File generateImageDir = new File(IMAGES_DIR);
		if (!generateImageDir.exists())
			generateImageDir.mkdirs();
		mZerliTimer = new ZerliTimer(ZerliTimer.HOUR_IN_SEC, ZerliTimer.DAY_IN_SEC, ZerliTimer.DAY_IN_SEC);
	}

	/**
	 * This method handles any messages received from the client.
	 * This method is synchronized. Only after fully handling client's request,
	 * proceed to next client request.
	 * 
	 * @param msg The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	@SuppressWarnings("unchecked")
	public synchronized void handleMessageFromClient(Object msg, ConnectionToClient client) {
		System.out.println("<< Message received: " + msg + " from " + client);
	  	if (handleFileMessage(msg, client)) 
	  		return;	
	    try {    
	        if (msg instanceof ArrayList) {	// ArrayList<ClientToServerMessage>
	        	handleComplexCommands((ArrayList<ClientToServerMessage>)msg, client);
	        	return;
	        }
	        ClientToServerMessage message = (ClientToServerMessage)msg;     
	        // Handle login / logout messages.
	        if (!handleLoggedInUsers(message, client)) 
	        	return; 
	        if (severalQueriesExpectsNothing(message, client))
	        	return;
	        singleQueryStatement(message, client);
	    }
	    catch (Exception ex) {
	    	ex.printStackTrace();
	    	try {
	    		client.sendToClient(ex);
	    	} 
	    	catch (IOException e) {
	    		System.err.println("\nServer can't reply exception:\t"+e);
	    	}
	    }
	}	// handleMessageFromClient
  
	/**
	 * Handle complex commands such as several queries that require combined results.
	 * @param messages ArrayList of commands. 
	 * @param client connection to client.
	 * @throws Exception exception
	 */
	@SuppressWarnings("unchecked")
	private void handleComplexCommands(ArrayList<ClientToServerMessage> messages, 
			ConnectionToClient client) throws Exception {
		if (messages.get(0).getQueryOption() == EQueryOption.GET_All_COMPLAINT_REPORTS_INFO 
				&& messages.size() == 5){
			ResultSet rs[] = new ResultSet[5];
			String queries[] = new String[5];
			Object [] reports = new Object[5];
	      
			ArrayList<Report> allreport= new ArrayList<Report>();
			ArrayList<ReportOrder> allOrderReports=new ArrayList<ReportOrder>();
			ArrayList<ReportDetailInOrderReport> allDetailInOrderReports =new ArrayList<ReportDetailInOrderReport>();
			
			for (int i=0; i<5; ++i) {
				queries[i] = QueryFactory.getQuery(messages.get(i));
				rs[i] = dbHandler.execute(queries[i]);
				
				if (rs[i] != null) {		// ResultSet exists.
	  	      		reports[i] = EntityFactory.getEntity(messages.get(i), rs[i]);  	      		
	  	      		if(i==0)  {		// if array type is ReportComplaint 
	  	      			allreport.addAll((Collection<? extends Report>)reports[i]);
	  	      		}
	  	      		if(i==1 ) {		// if array type is ReportDetailInOrderReport
	  	      			allDetailInOrderReports.addAll((Collection< ReportDetailInOrderReport>)reports[i]);
	  	      		}
	  	      		if(i==2) {		// set detail to order reports
	  	      			allOrderReports=(ArrayList<ReportOrder>)reports[i];
	  	      			allOrderReports.get(0).setDetailsInReport(allDetailInOrderReports);
	  	      			allreport.addAll(allOrderReports);
	  	      		}
	  	      		if(i==3) {		// if array type is income reports
	  	      			allreport.addAll((Collection<? extends Report>)reports[i]);
	  	      		}
	  	      		if(i==4) {		// if array type is satisfaction reports
	  	      			allreport.addAll((Collection<? extends Report>)reports[i]);
	  	      		}
	  	      		rs[i].close();
	  	      	}		// rs[i] != null. 
			} // for
			client.sendToClient(allreport);
		} // end if messages getQueryOptuin messages.size() == 5
	  
		if (messages.get(0).getQueryOption() == EQueryOption.GET_ALL_STORES &&
			  messages.size() == 6) {
			ResultSet rs[] = new ResultSet[6];
			String queries[] = new String[6];
			Object [] Tables = new Object[6];
			CollectionTableForReports mcollectionTableForReports=new CollectionTableForReports();
			for (int i=0; i<6; ++i) {
				queries[i] = QueryFactory.getQuery(messages.get(i));
				rs[i] = dbHandler.execute(queries[i]);
				if (rs[i] != null) {
					Tables[i] = EntityFactory.getEntity(messages.get(i), rs[i]);
	    		  	if(i==0)  {
	    		  		mcollectionTableForReports.setStors( (ArrayList<Store>)(Tables[i]) );
	    		  	}
	    		  	if(i==1)  {
	    		  		mcollectionTableForReports.setOrders(( (ArrayList<CustomerOrder>)(Tables[i])));
	    		  	}
	    		  	if(i==2)  {
	    		  		mcollectionTableForReports.setOrderProducts(( (ArrayList<OrderProduct>)(Tables[i])));
	    		  	}
	    		  	if(i==3)  {
	    		  		mcollectionTableForReports.setProducts(( (ArrayList<Product>)(Tables[i])));
	    		  	}
	    		  	if(i==4)  {
	    		  		mcollectionTableForReports.setComplaint((( (ArrayList<Complaint>)(Tables[i]))));
	    		  	}
	    		  	if(i==5)  {
	    		  		mcollectionTableForReports.setSurvey(((( (ArrayList<Survey>)(Tables[i])))));
	    		  	}
	    		  	rs[i].close();
				}//end if isResultSet
			} // end for messages.size() == 6
			client.sendToClient(mcollectionTableForReports); 
		} //end for messages.size() == 6
	} // handleComplexCommands

	/**
	 * Execute several queries but client doesn't expect a result.
	 * @param message message from client
	 * @param client the client
	 * @return	true if enum belongs here.
	 * @throws Exception Exception
	 */
	private boolean severalQueriesExpectsNothing(ClientToServerMessage message, 
		  ConnectionToClient client) throws Exception {
		if(message.getQueryOption() == EQueryOption.DELETE_PRODUCT) {
			String query = QueryFactory.getQuery(message);
			dbHandler.execute(query);
			System.err.println(query);
			message.setQueryOption(EQueryOption.DELETE_PRODUCT_SALE_PERCENT);
			query = QueryFactory.getQuery(message);
			System.err.println(query);
			dbHandler.execute(query);
			client.sendToClient("removed");
			return true;
		}
		// Freeze / Unfreeze user account
		if (message.getQueryOption() == EQueryOption.FREEZE_USER_ACCOUNT || 
				message.getQueryOption() == EQueryOption.UNFREEZE_USER_ACCOUNT ||
				message.getQueryOption() == EQueryOption.CHANGE_WORKER_PERMISSION) {	  
			String queries[] = QueryFactory.getQuery(message).split("\\n");
			dbHandler.execute(queries);
			if (message.getQueryOption() == EQueryOption.FREEZE_USER_ACCOUNT)
				client.sendToClient("froze");
			if (message.getQueryOption() == EQueryOption.UNFREEZE_USER_ACCOUNT)
				client.sendToClient("unfroze");
			if (message.getQueryOption() == EQueryOption.CHANGE_WORKER_PERMISSION)
				client.sendToClient("updated");		  	  
			return true;
		}
	  
		// Cancel customer order
		if (message.getQueryOption() == EQueryOption.CANCEL_CUSTOMER_ORDER) {
			String queries[] = QueryFactory.getQuery(message).split("\\n");
			for (String q : queries)
				System.err.println(q);
			dbHandler.execute(queries);
			client.sendToClient("cancelled");  // ok message for the controller.
			return true;
		}
	  
		// Add a product sale %
		if (message.getQueryOption() == EQueryOption.SET_PRODUCT_SALE_PERCENT) {
			String queries[] = QueryFactory.getQuery(message).split("\\n");
			if (dbHandler.execute(queries[0]).next()) { 		// check if product exist
				// Insert or Update a sale. one of the queries will work and 
				// the other will fail and return null. which is ok. 
				dbHandler.execute(queries[1]);	
				dbHandler.execute(queries[2]);	
				client.sendToClient("updated");
				return true;
			}
			client.sendToClient("Product doesn't exist");
			return true;
		}
	
		// Complaint handling
		if (message.getQueryOption() == EQueryOption.UPDATE_COMPLAINT_AND_REWARD_ACCOUNT) {		 
			ArrayList<Object> newParam = new ArrayList<Object>();		// TODO: add storeID
			newParam.add(message.getParams().get(0)); //add customerID
			newParam.add(message.getParams().get(1)); // add reward
			newParam.add(message.getParams().get(2));
			ClientToServerMessage msg1= new ClientToServerMessage(EQueryOption.REWARD_ACCOUNT, 
					newParam, null);
			String[] queries = new String[2];
			queries[0] = QueryFactory.getQuery(msg1);
			System.out.println("got query" + queries[0]);
			ArrayList<Object> compID = new ArrayList<Object>();
			compID.add(message.getParams().get(3)); //add complaintID
			ClientToServerMessage msg2 = new ClientToServerMessage(EQueryOption.UPDATE_COMPLAINT, 
					compID, null);	
			queries[1] = QueryFactory.getQuery(msg2);
			System.out.println("got query" + queries[1]);	
			dbHandler.execute(queries);
			client.sendToClient("updated"); 
			return true;
		}
	 
		// Add a new customer order
		if(message.getQueryOption()==EQueryOption.ADD_CUSTOMER_ORDER) {
			CustomerOrder customerOrder = (CustomerOrder) message.getParams().get(0);
			String queries[] = QueryFactory.addCustomerOrder(customerOrder).split("\\n");
			dbHandler.execute(queries);
			client.sendToClient("Ordered!!");
			return true;
		}
		
		return false;		// proceed as normal
	}	// severalQueriesExpectsNothing


	/**
	 * Generate and run a single query statement
	 * @param message message from client
	 * @param client the client
	 * @throws Exception Exception
	 */
	private void singleQueryStatement(ClientToServerMessage message, 
		  ConnectionToClient client)  throws Exception {
		System.out.println("\nServer got message:\t"+message);
		Object objectToReturn = null;
		String query = QueryFactory.getQuery(message);
		System.out.println("\nServer got query:\t"+query);
	    ResultSet rs = dbHandler.execute(query);
	    if (rs != null && message.getObjectType() != null) {
	    	objectToReturn = EntityFactory.getEntity(message, rs);
    		System.out.println("Server needs to return:\n"+objectToReturn);
    		if (message.getQueryOption().equals(EQueryOption.LOGIN_REQUEST) 
    				&& objectToReturn instanceof UserLoginInfo) {
    			mLoggedInUsers.add(((UserLoginInfo)objectToReturn).getUserID().toLowerCase());
    			System.out.println();
    			System.err.println("user "+((UserLoginInfo)objectToReturn).getUserID()+" connected.");
    		}
    		client.sendToClient(objectToReturn);
    		rs.close();
    	}
	    else {
	    	client.sendToClient("no results found");
	    }
	}	// singleQueryStatement

	/**
	 * Handle file messages between server and client
	 * @param msg message from client
	 * @param client the client
	 * @return true if file message
	 */
	private boolean handleFileMessage(Object msg, ConnectionToClient client) {
		if (msg instanceof SendableFile) {
			SendableFile fileRecieved = (SendableFile)msg;
			try {
				fileRecieved.commitFile(IMAGES_DIR);
			}
			catch (IOException e) {		// failed to write to server.
				System.err.println(e+ ": failed to write to server");
				try {
					client.sendToClient(e);
					} catch (IOException e1) {
						System.err.println(e1+": failed to send error to client");
					}
			}	
			try {
				client.sendToClient("uploaded");
			} catch (IOException e) {
				System.err.println(e+": failed to send error to client");
			}
			int fileSize = fileRecieved.getSize(); 
			System.out.println("length "+ fileSize);
			return true;
		}	// msg is SendableFile
	  
		return false;
	}	// handleFileMessage

	/**
	 * a client has been connected.
	 */
	protected void clientConnected(ConnectionToClient client) {
		System.out.println("\n<<< Client "+client+" connected to the server.");
	}
	/**
	 * a client has been disconnected.
	 */
	protected void clientDisconnected(ConnectionToClient client) {
		System.out.println("\n>>> Client "+client+" disconnected from the server.");
	}
    

	/**
	 * Handle server's logged in users.
	 * @param msg message from client.
	 * @param client the client
	 * @return false if no need to continue to queryFactory.
	 * @throws Exception Exception
	 */
	private boolean handleLoggedInUsers(ClientToServerMessage msg, ConnectionToClient client) throws Exception{
		EQueryOption queryOption = msg.getQueryOption();
		if (queryOption != EQueryOption.LOGIN_REQUEST && queryOption != EQueryOption.LOGOUT_REQUEST) 
			return true;		// skip this function.
		String userID = (String) msg.getParams().get(0);  
		if (userID == null || userID.isEmpty()) {
		  // Ignore logout request..
			return false;
		}
		if (queryOption == EQueryOption.LOGIN_REQUEST) {
			if (mLoggedInUsers.contains(userID)) {	// user is already logged in
				client.sendToClient("Your user is logged in already.");
				return false;  
			}
			return true;	// proceed
		}
		if (queryOption == EQueryOption.LOGOUT_REQUEST) {
			mLoggedInUsers.remove(userID);
			System.out.println();
			System.err.println("user " + userID + " disconnected.");
		}
		return false;
	}	// handleLoggedInUsers
  
  
	/**
	 * This method overrides the one in the superclass.  Called
	 * when the server starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}
  
	/**
	 * This method overrides the one in the superclass.  Called
	 * when the server stops listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}
  
	/**
	 * This method is responsible for the creation of 
	 * the server instance (there is no UI in this phase).
	 *
	 * @param args[0] The port number to listen on.  Defaults to 5555 
	 *          if no argument is entered.
	 */
	public static void main(String[] args) {
		try {
			mListeningPort = Integer.parseInt(args[0]); //Get port from command line
		}
		catch(Throwable t) {
			mListeningPort = DEFAULT_PORT; //Set port to 5555
		}
    
		DataBaseServer sv = new DataBaseServer(mListeningPort);
    
		try {
			sv.listen(); //Start listening for connections
			System.out.println("Server IP Address: "+Inet4Address.getLocalHost().getHostAddress() );
		} 
		catch (Exception ex) 
		{
			System.err.println("ERROR - Could not listen for clients!" + ex.toString());
		}
	}	// main
	
	
} // End of DataBaseServer class

