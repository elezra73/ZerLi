// QueryFactory

package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import common.ClientToServerMessage;
import common.CustomerOrder;
import common.EQueryOption;
import common.Product;
import common.ZerliDate;
import common.reports.ReportComplaint;
import common.reports.ReportDetailInOrderReport;
import common.reports.ReportIncome;
import common.reports.ReportOrder;
import common.reports.ReportsStatisfaction;
import common.users.Customer;

/**
 * Class: QueryFactory
 * Store all server queries related to our database.
 *
 */
public class QueryFactory {
	
//////////////////////// SQL Tables Related Info //////////////////////////////////////
	
	private final static String SCHEME_NAME = "zerli"+"."; // scheme name
	private final static String IS_ACTIVE = "IsActive";
	
	/// Table names 
	private final static String PRODUCTS_TABLE = SCHEME_NAME+"products";
	private final static String USERS_TABLE = SCHEME_NAME+"users";
	private final static String CUSTOMERS_TABLE = SCHEME_NAME+"customers";
	private final static String WORKERS_TABLE = SCHEME_NAME+"workers";
	private final static String STORE_TABLE = SCHEME_NAME + "store";
	private final static String PRODUCT_SALE_TABLE = SCHEME_NAME + "productSale";
	private final static String COMPLAINT_REPORTS_TABLE=SCHEME_NAME+"reportComplaint";
	private final static String INCOME_REPORTS_TABLE=SCHEME_NAME+"reportIncome";
	private final static String SATISFACTION_REPORTS_TABLE=SCHEME_NAME+"reportSatisfaction";
	private final static String ORDER_REPORTS_TABLE=SCHEME_NAME+"reportOrder";
	private final static String DETAIL_IN_ORDER_REPORTS_TABLE=SCHEME_NAME+"reportDetailInOrder";
	private final static String COMPLAINTS_TABLE = SCHEME_NAME+"complaints";
	private final static String CUSTOMER_ORDER_TABLE = SCHEME_NAME+"customerOrder";
	private final static String PRODUCT_IN_ORDER_TABLE = SCHEME_NAME+"productInOrder";
	private final static String SURVEY_ANSWERS_TABLE = SCHEME_NAME+"surveyAnswer";
	
	/// Keys
	private final static String USERS_ID = "UserID";
	private final static String STORE_ID = "StoreID";
	private final static String CUSTOMER_ID = "CustomerID";
	private final static String PRODUCT_ID = "ProductID";
	private final static String WORKER_ID = "WorkerID";
	private final static String COMPLAINT_ID = "ComplaintID";
	private final static String SURVEY_INCREMENTID = "IncrementID";
	private final static String SURVEY_SURVEYID = "SurveyID";
	private final static String ORDER_ID = "OrderID";	
	
	/// Products table related
	private final static String PRODUCTS_NAME = "ProductName";
	private final static String PRODUCTS_TYPE = "ProductType";
	private final static String PRODUCTS_PRICE = "ProductPrice";
	private final static String PRODUCTS_IMAGE = "ProductImage";
	private final static String PRODUCT_COLOR = "DominantColor";

	/// Users table related
	private final static String USERS_ACCOUNT_TYPE = "AccountType";

	/// Customer table related
	private final static String CUSTOMERS_NAME = "CustomerName";
	private final static String CUSTOMERS_IS_PAYABLE = "IsPayable";
	private final static String CUSTOMERS_ADDRESS = "Address";
	private final static String CUSTOMERS_PAYMENT_METHOD = "PaymentMethod";
	private final static String CUSTOMERS_BALANCE = "Balance";
	private final static String CUSTOMERS_CREDIT_CARD = "CreditCard";
	private final static String CUSTOMERS_SOCIAL_NUMBER = "SocialNumber";
	
	/// Worker table related
	private final static String WORKERS_NAME = "WorkerName";
	private final static String WORKERS_TYPE = "WorkerType";
	
	/// productSale table related
	private final static String SALE_PERCENT = "SalePercent";

	/// survey table related 
	private final static String SURVEY_CONCLUSION = "Conclusion";
		
////////////////////// END OF TABLE RELATED INFO ///////////////////
	
	
	/**
	 * HashMap contains EQueryOption as key
	 * and number of parameters required as value.
	 */
	private static HashMap<EQueryOption, Integer> enumParamNum = initMap();
	
	/**
	 * Static initialization of enumParameterNumber
	 * @return initialized HashMap
	 */
	private static HashMap<EQueryOption, Integer> initMap()
    {
		HashMap<EQueryOption, Integer> map = new HashMap<EQueryOption,Integer>();
		// Whole Tables
		map.put(EQueryOption.GET_ALL_USERS,0);
	    map.put(EQueryOption.GET_All_CUSTOMERS, 0);
        map.put(EQueryOption.GET_ALL_STORES, 0);
        map.put(EQueryOption.GET_CATALOG,0);
        map.put(EQueryOption.GET_SELF_COMPOSITION ,0);
        map.put(EQueryOption.GET_All_COMPLAINT_REPORTS_INFO,0);
        map.put(EQueryOption.GET_All_DETAIL_IN_ORDER_REPORTS_INFO,0);
        map.put(EQueryOption.GET_All_INCOME_REPORTS_INFO,0);
        map.put(EQueryOption.GET_All_ORDER_REPORTS_INFO,0);
        map.put(EQueryOption.GET_All_SATISFACTION_REPORTS_INFO,0);
        map.put(EQueryOption.GET_All_CUSTOMER_ORDER,0);
        map.put(EQueryOption.GET_All_PRODUCT_IN_ORDER_TABLE,0);
        map.put(EQueryOption.GET_All_PRODUCTS,0);
        map.put(EQueryOption.GET_All_COMPLAINTS,0);
        map.put(EQueryOption.GET_ALL_ACTIVE_COMPLAINTS,0);
        map.put(EQueryOption.GET_All_SURVEY_ANSWER,0);
        
        
		// users table related queries
		map.put(EQueryOption.LOGIN_REQUEST, 2); // Expected Params: username, password.
		
		//	
		// workers table related queries
        map.put(EQueryOption.GET_WORKER_INFO,1);
        map.put(EQueryOption.CHANGE_WORKER_PERMISSION, 4);	// expected param: user's id, new permission, name, storeid.
        //
        // Customer table related queries
    
        map.put(EQueryOption.GET_CUSTOMER_INFO,2);
        map.put(EQueryOption.UPDATE_CUSTOMER_INFO, 8);
        map.put(EQueryOption.ADD_NEW_CUSTOMER, 8);
        //
        
        // System Manager actions related
        map.put(EQueryOption.FREEZE_USER_ACCOUNT, 1);		// expected param: user's id.
        map.put(EQueryOption.UNFREEZE_USER_ACCOUNT, 1);		// expected param: user's id.
        map.put(EQueryOption.VIEW_USER_PERMISSION, 1);		// expected param: user's id.
        map.put(EQueryOption.DISABLE_PAYABLE_ACCOUNT,2);//expected customerID,StoreID
        //
        // Store table related

        map.put(EQueryOption.GET_CUSTOMERS_BY_SUBSCRIPTION, 1);
        map.put(EQueryOption.GET_CUSTOMERS_NAME_BY_STOREID, 1); 
        map.put(EQueryOption.GET_STORES_ID_BY_CUS_ID,1);	// get all the stores of specific customer
        //
        
		// products table related queries
        map.put(EQueryOption.GET_PRODUCT_INFO, 1);		// expected Params: ID
        map.put(EQueryOption.UPDATE_PRODUCT_INFO, 6);	// expected Params: oldID, newID, newName, newType..
        map.put(EQueryOption.ADD_NEW_PRODUCT,5);
        map.put(EQueryOption.DELETE_PRODUCT,2);
        //
    	
    	map.put(EQueryOption.ADD_COMPLAINT,1);
        map.put(EQueryOption.SURVEY_ADD_PROFESSIONAL_REVIEW,4);
        map.put(EQueryOption.GET_SURVEY_FROM_SPECIFIC_STORE, 1);
        ///
        
        // catalog related 
        map.put(EQueryOption.DELETE_PRODUCT_SALE_PERCENT, 2);
        map.put(EQueryOption.SET_PRODUCT_SALE_PERCENT, 3);
        map.put(EQueryOption.GET_PRODUCT_PERCENT_BY_STOREID , 1);

        map.put(EQueryOption.NEW_COMPLAINT,8); //expected all data about complaint
        map.put(EQueryOption.GET_COMPLAINTS_BY_WORKER_ID, 1); //expected workerID

        map.put(EQueryOption.REWARD_ACCOUNT, 3);//expected CustomerID,  Reward and StoreID.
        map.put(EQueryOption.UPDATE_COMPLAINT, 1);//expected ComplaintID
        
        
        //survey query
        map.put(EQueryOption.ADD_FILLED_SURVEY,11);
        
        ///order
        map.put(EQueryOption.GET_CUSTOMER_ACTIVE_ORDERS, 1);	// expected customer ID.
        map.put(EQueryOption.CANCEL_CUSTOMER_ORDER, 4);		// expected orderID, customerID, storeID, refund amount.
        //
  
        return map;
    }
	
	

	/**
	 * The main function of this class. Given a ClientToServer message
	 * returns the suitable query.
	 * @param message message from client
	 * @return suitable query
	 * @throws Exception exception
	 */
	public static String getQuery(ClientToServerMessage message) throws Exception{
		EQueryOption queryOption = message.getQueryOption();
		ArrayList<Object> objectParams = message.getParams();	
		ArrayList<String> params = null;
		if (objectParams == null && enumParamNum.get(queryOption) != 0) {
			System.out.println("INVALID PARAMS: #of params should be "+enumParamNum.get(queryOption));
			return "";
		}
		else if(objectParams != null) {
			if (objectParams.size() != enumParamNum.get(queryOption)) {
				System.out.println("INVALID PARAMS: #of params should be "+enumParamNum.get(queryOption));
				return "";
				// TODO: add invalid parameters number error or Exception 
			}
			params = new ArrayList<String>();
			for (Object param : objectParams) {
				if (param == null)
					params.add(null);
				else if(param instanceof String)
					params.add("'"+param+"'");
				else params.add(param.toString());
			}	
		}

		switch(queryOption) {
		
		/// get whole tables queries 
		case GET_ALL_USERS:
			return getWholeTable(USERS_TABLE);
		case GET_All_CUSTOMERS:
			return getWholeTable(CUSTOMERS_TABLE);
		case GET_ALL_STORES:
			return getWholeTable(STORE_TABLE);
		case GET_All_CUSTOMER_ORDER:
			return getWholeTable(CUSTOMER_ORDER_TABLE);
		case GET_All_PRODUCT_IN_ORDER_TABLE:
			return getWholeTable(PRODUCT_IN_ORDER_TABLE);
		case GET_All_PRODUCTS:
			return getWholeTable(PRODUCTS_TABLE);
		case GET_All_COMPLAINTS:
			return getWholeTable(COMPLAINTS_TABLE);
		case GET_ALL_ACTIVE_COMPLAINTS:
			return getActiveWholeTable(COMPLAINTS_TABLE);
		case GET_All_SURVEY_ANSWER:
			return getWholeTable(SURVEY_ANSWERS_TABLE);	
		case GET_All_COMPLAINT_REPORTS_INFO:
			return getWholeTable(COMPLAINT_REPORTS_TABLE);		
		case GET_All_DETAIL_IN_ORDER_REPORTS_INFO:
			return getWholeTable(DETAIL_IN_ORDER_REPORTS_TABLE);
		case GET_All_INCOME_REPORTS_INFO:;
			return getWholeTable(INCOME_REPORTS_TABLE);
		case GET_All_ORDER_REPORTS_INFO:
			return getWholeTable(ORDER_REPORTS_TABLE);
		case GET_All_SATISFACTION_REPORTS_INFO:
			return getWholeTable(SATISFACTION_REPORTS_TABLE);
	
		/// Get a single entry in a table queries
		case VIEW_USER_PERMISSION:	// same data required as login request.
		case LOGIN_REQUEST:
			return  getEntry(USERS_TABLE, USERS_ID, params.get(0));
		case GET_WORKER_INFO:
			return  getEntry(WORKERS_TABLE, WORKER_ID, params.get(0));
		case GET_PRODUCT_INFO:
			return getEntry(PRODUCTS_TABLE, PRODUCT_ID, params.get(0));
		case GET_STORES_ID_BY_CUS_ID:
			return getEntry(CUSTOMERS_TABLE, CUSTOMER_ID, params.get(0));		
		case GET_CUSTOMERS_NAME_BY_STOREID:
			return getEntry(CUSTOMERS_TABLE, STORE_ID, params.get(0));		
		case GET_CUSTOMERS_BY_SUBSCRIPTION:
			return getEntry(CUSTOMERS_TABLE, CUSTOMERS_PAYMENT_METHOD, params.get(0));

		/// Get a single ACTIVE entry in table queries 
		case GET_CUSTOMER_ACTIVE_ORDERS:
			return getActiveEntry(CUSTOMER_ORDER_TABLE, CUSTOMER_ID, params.get(0));
	
		/// Delete a single entry in a table queries 
		case DELETE_PRODUCT:
			return deleteEntry(PRODUCTS_TABLE, PRODUCT_ID,params.get(0));
	
		case FREEZE_USER_ACCOUNT:
			return freezeUserAccount(params.get(0), true);
		case UNFREEZE_USER_ACCOUNT:
			return freezeUserAccount(params.get(0), false);		
		case CHANGE_WORKER_PERMISSION:
			return changeWorkerPermission(params.get(0),params.get(1),params.get(2),params.get(3));
		case GET_CUSTOMER_INFO:
			return  getCustomerInfo(params.get(0), params.get(1));
		case UPDATE_CUSTOMER_INFO:
			return changeCustomerInfo(params.get(0), params.get(1), params.get(2), params.get(3),
					params.get(4), params.get(5), params.get(6), params.get(7));
		case ADD_NEW_CUSTOMER:
			return addNewCustomer(params.get(0), params.get(1), params.get(2), params.get(3),
					params.get(4), params.get(5), params.get(6), params.get(7));
		case DISABLE_PAYABLE_ACCOUNT:
			return disablePaybleAccount(params.get(0), params.get(1));
			
		case UPDATE_PRODUCT_INFO:
			return updateProductInfo(params.get(0),params.get(1),params.get(2),params.get(3),params.get(4),params.get(5)) ;					
		case ADD_NEW_PRODUCT:
			return AddproductToCatalog(params.get(0), params.get(1), params.get(2), params.get(3), params.get(4));
						
		
		
		case SURVEY_ADD_PROFESSIONAL_REVIEW:
			return addProfessionalReview(params.get(0) , params.get(1) , params.get(2) , params.get(3));
		case GET_SURVEY_FROM_SPECIFIC_STORE:
			return getSurveyBySpecificStore(params.get(0));
		/// insert new complaint to the complaint table	
		case NEW_COMPLAINT:
			return createNewComplaint(params.get(0),params.get(1),params.get(2), params.get(3),
								params.get(4),params.get(5), params.get(6), params.get(7));
								
		// catalog related
		case DELETE_PRODUCT_SALE_PERCENT:
			return deleteProductSale(params.get(0), params.get(1));
		case SET_PRODUCT_SALE_PERCENT:
			return setProductSale(params.get(0), params.get(1), params.get(2));
		case GET_CATALOG:					// get network catalog
			return getNetworkCatalog();
						
		case GET_PRODUCT_PERCENT_BY_STOREID:
			return getCatalogFromSpecificStore(params.get(0));
		
		case GET_SELF_COMPOSITION:
			return getSelfComposition();
		///get all specific Worker's complaints 
		case GET_COMPLAINTS_BY_WORKER_ID:
			return getactiveComplaintsbyWorkerID(params.get(0));
		
	//add reward to account
		case REWARD_ACCOUNT:
			return rewardAccount(params.get(0),params.get(1),  params.get(2));
			//change isActive to false
		case UPDATE_COMPLAINT:
			return update_active(params.get(0));
 					
		//survey
		case ADD_FILLED_SURVEY:
			return addNewSurvey(params.get(0), params.get(1), params.get(2), params.get(3), params.get(4), params.get(5), params.get(6), params.get(7), params.get(8),params.get(9),params.get(10));
			

		case CANCEL_CUSTOMER_ORDER:
			return getCancelOrderQueries(params.get(0), params.get(1), params.get(2), params.get(3));
	
			default:
				return null;	
		}
	}		
	




	/**
	 * 
	 * generate queires to execute when cancelling an order.
	 * @param orderID order id to cancel
	 * @param customerID customr id
	 * @param storeID store id 
	 * @param refund amount to refund
	 * @return query
	 */
	private static String getCancelOrderQueries(String orderID, String customerID, String storeID, String refund) {
		return "UPDATE " + CUSTOMERS_TABLE + " SET "+ CUSTOMERS_BALANCE + " = " + CUSTOMERS_BALANCE +" + "+ refund +
				" WHERE  "+ CUSTOMER_ID + " = " + customerID + " AND " + STORE_ID + " = " + storeID +  ";\n" +
				"UPDATE " + CUSTOMER_ORDER_TABLE + " SET " + IS_ACTIVE + " = 0 WHERE " + ORDER_ID + " = " + orderID
				+ ";\n";
	}


	/////////////////////////// Generic Queries //////////////////////////////
	
	/**
	* Get a whole table data from SQL
	* @param tableName table name
	* @return whole sql table data
	*/
	private static String getWholeTable(String tableName) {
		return "SELECT * FROM " + tableName + ";";
	}
	
	/**
	* Get a whole table data from SQL (only active entries)
	* @param tableName table name
	* @return whole sql table data
	*/
	private static String getActiveWholeTable(String tableName) {
		return "SELECT * FROM " + tableName + " WHERE " +
				IS_ACTIVE + " = 1;";
	}
	
	/**
	* Get entry
	 * @param table table name
	 * @param keyName column name
	 * @param keyValue value
	 * @return query
	*/
	private static String getEntry(String table, String keyName, String keyValue) {
		if (table == null || keyName == null || keyValue == null)
			return "";
		return "SELECT * FROM " + table + " WHERE " + keyName + " = " + keyValue + ";";
	}
	
	/**
	 * get active entry
	 * @param table table name
	 * @param keyName column name
	 * @param keyValue value
	 * @return query
	 */
	private static String getActiveEntry(String table, String keyName, String keyValue) {
		if (table == null || keyName == null || keyValue == null)
			return "";
		return "SELECT * FROM " + table + " WHERE " + keyName + " = " + keyValue 
				+ " AND " + IS_ACTIVE + " = 1;";
	}
	
	/**
	 * delete an entry
	 * @param table table name
	 * @param keyName column name
	 * @param keyValue value
	 * @return query
	 */
	private static String deleteEntry(String table, String keyName, String keyValue) {
		if (table == null || keyName == null || keyValue == null)
			return "";
		return "DELETE FROM " + table + " WHERE " + keyName + " = " + keyValue + ";";
	}

	//////////////////////////////////////////////////////////////////////////





	/**
	 * Get customer info
	 * @param customerID customerid
	 * @param storeID storeid
	 * @return query
	 */
	private static String getCustomerInfo(String customerID, String storeID) {
		String stringToReturn = "SELECT * FROM " + CUSTOMERS_TABLE + " WHERE " + CUSTOMER_ID +
				" = " + customerID;
		if (storeID == null)
			return stringToReturn + ";";
		else return stringToReturn + " AND " + STORE_ID + " = " + storeID+";";
	}

	/**
	 * 
	 * @param id id
	 * @param name name
	 * @param storeID storeid
	 * @param address address
	 * @param paymentMethod payment method
	 * @param creditCard credit card
	 * @param socialNumber social nu,ber 
	 * @param isPayable is payable
	 * @return query
	 */
	private static String changeCustomerInfo(String id, String name, String storeID, String address,
			String paymentMethod, String creditCard, String socialNumber, String isPayable) {
		return "UPDATE "+ CUSTOMERS_TABLE + " SET " + CUSTOMERS_NAME + " = " + name + ", " +
			 CUSTOMERS_ADDRESS + " = " + address + 
			", " + CUSTOMERS_PAYMENT_METHOD + " = " + paymentMethod + ", " + CUSTOMERS_CREDIT_CARD + 
			" = " + creditCard + ", " + CUSTOMERS_SOCIAL_NUMBER + " = " + socialNumber + ", "+
			CUSTOMERS_IS_PAYABLE + " = " +  isPayable + 
			" WHERE " + CUSTOMER_ID + " = " + id + " AND " + STORE_ID + " = " + storeID + ";";
	}

	private static String addNewCustomer(String id, String name, String storeID, String address,
			String paymentMethod, String creditCard, String socialNumber, String isPayable) {
		ZerliDate now = new ZerliDate();
		return "INSERT INTO "+ CUSTOMERS_TABLE + " VALUES( " + id + ", "+  name + ", " + storeID + 
				", true, " + address + ", " + paymentMethod + ",0 , " + creditCard + ", " + 
				socialNumber + ", '" + now.getFullDate() + "', true);";
	}

	
	private static String changeWorkerPermission(String userID, String type, String name, String storeID) {
		return "UPDATE " + USERS_TABLE + " SET " + USERS_ACCOUNT_TYPE + " = " +
				type + " WHERE " + USERS_ID + " = " + userID + ";\n" + 
				"UPDATE " + WORKERS_TABLE + " SET " + WORKERS_TYPE + " = " + type +
				", " + WORKERS_NAME + " = " + name + ", " + STORE_ID + 
				" = " + storeID + " WHERE " + WORKER_ID + " = " + userID + ";\n";
				
	}


	/**
	 * Freeze an account
	 * @param userID account id to freeze
	 * @param toFreeze to freeze / unfreeze
	 * @return queries 
	 */
	private static String freezeUserAccount(String userID, boolean toFreeze) {
		int boolToInt = toFreeze ? 0 : 1;
		return "UPDATE " + USERS_TABLE + " SET " + IS_ACTIVE + " = " + boolToInt
				+ " WHERE " + USERS_ID + " = " + userID + ";\n" +
				"UPDATE " + WORKERS_TABLE + " SET " + IS_ACTIVE + " = " + boolToInt
				+ " WHERE " + WORKER_ID + " = " + userID + ";\n" +
				"UPDATE " + CUSTOMERS_TABLE + " SET " + IS_ACTIVE + " = " + boolToInt
				+ " WHERE " + CUSTOMER_ID + " = " + userID + ";\n";	
	}



	private static String createNewComplaint(String complaintID, String customerID, String workerID, String storeID,
			String complaintDate, String expirationDate, String description, String isActive) {
		return "INSERT INTO "+COMPLAINTS_TABLE+" VALUES ("+complaintID+", "+customerID+", "+workerID+", "+storeID+", "+complaintDate+", "+expirationDate+", "+description+", "+isActive+" )"+";";
			} 

		
	/**
	 * Generate 3 queries to be used by server to insert a sale..
	 * @param storeID store id
	 * @param productID product
	 * @param salePercent sale percent
	 * @return query
	 */
	private static String setProductSale(String storeID, String productID, String salePercent) {
		return getEntry(PRODUCTS_TABLE, PRODUCT_ID, productID) + "\n" + // 1st query to check if product exist
				"INSERT INTO " + PRODUCT_SALE_TABLE + " VALUES("+storeID + ", " + productID + ", " + salePercent+");\n"
				+ "UPDATE " + PRODUCT_SALE_TABLE + " SET " + SALE_PERCENT + " = " + salePercent
				+ " WHERE " + STORE_ID + " = " + storeID + " AND " + PRODUCT_ID + 
				" = " + productID + ";\n";
	}

	
	/**
	 * Delete a product sale percent
	 * @param storeID store id
	 * @param productID product id
	 * @return query
	 */
	private static String deleteProductSale(String productID, String storeID) {
		String toReturn =  "DELETE FROM "+PRODUCT_SALE_TABLE + " WHERE " + PRODUCT_ID + " = " + productID;
		if (storeID != null && storeID != "null")
			toReturn +=	 " AND " + STORE_ID + " = " + storeID;
		return toReturn + ";";
		
	}

	/**
	 * 	
	 * @param pID product id
	 * @param productName name
	 * @param productType type
	 * @param pPrice price
	 * @param pUrl img name
	 * @param color color
	 * @return query
	 */
	private static String updateProductInfo(String pID, String productName, String productType,String pPrice,String pUrl,String color) {	
			return "UPDATE " + PRODUCTS_TABLE + " SET "+
					PRODUCTS_NAME +" = "+ productName +", "+
					PRODUCTS_TYPE +" = "+ productType +", "+
					PRODUCTS_PRICE +" = "+pPrice+", "+
					PRODUCTS_IMAGE +" = "+pUrl+", "+
					PRODUCT_COLOR+" = "+color+
					" WHERE  "+ PRODUCT_ID+" = "+pID+" ;";
				
		}
		private static String AddproductToCatalog(String pname,String ptype,String pprice,String pimage,String color)
		{
			return "INSERT INTO "+PRODUCTS_TABLE+ " VALUES ( NULL, "+pname+", "+ptype+", "+pprice+", "+pimage+", "+color+", 1);" ;
		}

		


		
	
		
			
			
	
		/**
		 * 
		 * @return basic catalog
		 */
		private static String getNetworkCatalog() 		//get Network Catalog (common to everyone)
		{
			return "SELECT * FROM " + PRODUCTS_TABLE + " WHERE " + IS_ACTIVE + " = " + "true" + ";";
	
		}
		
		
		
		/**
		 * get specific catalog
		 * @param StoreID store id
		 * @return query
		 */
		private static String getCatalogFromSpecificStore(String StoreID) 		// get the products that have percent in catalog
		{
			return "SELECT ProductID , SalePercent FROM " + PRODUCT_SALE_TABLE + " WHERE " + STORE_ID + " = " + StoreID + " ;" ;
		}
		
		
		/**
		 * 
		 * @return get self compse
		 */
		private static String getSelfComposition ()
		{
			return "SELECT * FROM " + PRODUCTS_TABLE + " WHERE " + IS_ACTIVE + " = " + "true AND " + PRODUCTS_TYPE + " LIKE" + "'self%'" + ";"; 
		}
		
		/**
		 * 
		 * @param workerID worker id
		 * @return query
		 */
		private static String getactiveComplaintsbyWorkerID(String workerID)
		{
			return "SELECT * FROM " + COMPLAINTS_TABLE + " WHERE " 
				+WORKER_ID +" = " + workerID+ " AND " +IS_ACTIVE +" = 1;";
		}	

		private static String rewardAccount(String customerID, String reward, String storeID) {	
			return "UPDATE " + CUSTOMERS_TABLE + " SET "+ CUSTOMERS_BALANCE + " = " + CUSTOMERS_BALANCE +" + "+ reward +
					" WHERE  "+ CUSTOMER_ID+" = "+ customerID + " AND " +STORE_ID +" = " +storeID+";";
		}
				
		private static String update_active(String complaintID) {	
						return "UPDATE " + COMPLAINTS_TABLE + " SET "+
								IS_ACTIVE +" = "+ 0 + " WHERE  "+ COMPLAINT_ID+" = "+ complaintID +" ;";
		}
		
		private static String addNewSurvey(String surveyID,String customerID, String storeID, String answertDate ,String Q1,String Q2,String Q3,String Q4,String Q5,String Q6,String isActive)
		{
			return "INSERT INTO "+ SURVEY_ANSWERS_TABLE + " VALUES (null, "+ surveyID +", "+customerID+",  "+storeID+",  "+answertDate+", "+Q1+", "+Q2+", "+Q3+", "+Q4+", "+Q5+", "+Q6+", "+isActive+" ,"+"null"+" );";			
		}
		
		private static String addProfessionalReview(String incrementID , String surveyid , String storeid , String conclusion)
		{
			return "UPDATE " + SURVEY_ANSWERS_TABLE + " SET " + SURVEY_CONCLUSION + " = " + conclusion + " WHERE " + SURVEY_INCREMENTID + " = " + incrementID + " and " + SURVEY_SURVEYID + " = " + surveyid + " and " + CUSTOMER_ID + " = " + storeid +";" ;
		}	 
		
		private static String getSurveyBySpecificStore(String store)
		{
			return "SELECT * FROM " + SURVEY_ANSWERS_TABLE + " WHERE " + STORE_ID + " = " + store + " and " + IS_ACTIVE + " = 1;";
		}
		
		
		
		private static String addONewOrderToTable(String customerID,String storeID,String orderDate,String shipmentMethod,String shipmentAddress,String recieverName,String finalAmount,String blessing,String deliveryDate)
		{
			return "INSERT INTO "+CUSTOMER_ORDER_TABLE+" VALUES ( NULL ,'"+customerID+"','"+storeID+"', '"+orderDate+"', '"+shipmentMethod+"', '"+shipmentAddress+"', '"+recieverName+"', "+finalAmount+", '"+
					blessing+"', '"+deliveryDate+"', 1" +" );";
		}
		
		private static String addSingleValueForProductInOrder(String orderID,String productID,String qauntity,String storeID)
		{
			return " ( "+orderID+", '"+productID+"',"+qauntity+", '"+storeID+"' )";
		}
		
		public static String addCustomerOrder(CustomerOrder customerOrder)
		{
			Customer customer = customerOrder.getCustomer();
			Map<Product, Integer> orderedProducts = customerOrder.getOrderedProducts();
			String query = "";
			query+=addONewOrderToTable(customer.getUserID(),customer.getStoreID(), customerOrder.getOrderDate().toString(), customerOrder.getShipmentMethod(), customerOrder.getShipmentAddress(), customerOrder.getRecieverName(),
					customerOrder.getFinalAmout(), customerOrder.getBless(), customerOrder.getDeliveryDate().toString());
			//query+= "\n SELECT MAX(orderID) AS ID FROM zerli.customerOrder; ";
			query +="\n INSERT INTO "+PRODUCT_IN_ORDER_TABLE+" VALUES  ";
			
			for (Entry<Product, Integer> entry : orderedProducts.entrySet())
			{
				query+= addSingleValueForProductInOrder("last_insert_id()", entry.getKey().getProductID(), entry.getValue().toString(), customer.getStoreID());
				query+=" , ";
			}
			query= query.substring(0, query.length()-2);
			query+=";";
			
			return query;
		}
		
		private static String disablePaybleAccount(String customerID,String storeID)
		{
			return "UPDATE " + CUSTOMERS_TABLE + " SET " +CUSTOMERS_IS_PAYABLE + " = 0  WHERE " + CUSTOMER_ID+" = "+customerID + " AND "+STORE_ID +" = "+storeID+" ;";
		}
		public static ArrayList<String> addOrderReport(ReportOrder reportOrder) {
			ArrayList<String> queryList=new ArrayList<String>() ;
			ArrayList<ReportDetailInOrderReport> detailofReportSatisfaction=new ArrayList<ReportDetailInOrderReport>();
			detailofReportSatisfaction=reportOrder.getProductAndQuantity();
			if(detailofReportSatisfaction.size()>0) {
				
				String query="";
				query=addNewOrderReportToTable(reportOrder.getStoreID(),reportOrder.getYear(),reportOrder.getQuarter()) ;
				queryList.add(query);
				query="SET @last_id_in_ReportOrder := last_insert_id();";
				queryList.add(query);
				query ="INSERT INTO "+DETAIL_IN_ORDER_REPORTS_TABLE+" VALUES  ";
					for (ReportDetailInOrderReport reportDetailInOrderReport : detailofReportSatisfaction) {
						
							query+= addSingleValueForReportDtaillInorder(reportDetailInOrderReport.getTypeProduct(),reportDetailInOrderReport.getQuantity(),"@last_id_in_ReportOrder");
							query+=" , ";
						}
				query= query.substring(0, query.length()-2);
				query+=";";
			//	String arr[] = query.split("\\n");
				queryList.add(query);
				return queryList;
			}
			return null;
		}
		private static String addNewOrderReportToTable(String storeID,String yearReport,String quartely)
		{
			return "INSERT INTO "+ORDER_REPORTS_TABLE+" VALUES ( NULL ,'"+storeID+"','"+yearReport+"', '"+quartely+"' );";
		}
		public static String addSingleValueForReportDtaillInorder(String productName,String qauntity,String ReportOrder)
		{
			return " ( NULL, '"+productName+"','"+qauntity+"', "+ReportOrder+")";
		}
		
		public static String AddNewComplaintReport(ReportComplaint reportComplaint) {
			return "INSERT INTO "+COMPLAINT_REPORTS_TABLE+" VALUES (NULL,'"+reportComplaint.getStoreID()+"','"+reportComplaint.getQuantityComplaint1()+"','"+reportComplaint.getQuantityComplaint2() +"','"+reportComplaint.getQuantityComplaint3()+"','"+reportComplaint.getYear()+"','"+reportComplaint.getQuarter()+"');";
		} 
		public static String addNewIncomeReport(ReportIncome reportIncome) {
			return "INSERT INTO "+INCOME_REPORTS_TABLE+" VALUES (NULL,'"+reportIncome.getStoreID()+"','"+ reportIncome.getIncomeAmount1()+"','"+reportIncome.getIncomeAmount2()+"','"+reportIncome.getIncomeAmount3()+"','"+reportIncome.getYear() +"','"+reportIncome.getQuarter()+"');";
		} 
		
		public static String addNewSatisfactionReport(ReportsStatisfaction satisfactionReport) {
			return "INSERT INTO "+SATISFACTION_REPORTS_TABLE+" VALUES (NULL,'"+satisfactionReport.getAvgQuestion1ForMonth1()+"','"+satisfactionReport.getAvgQuestion1ForMonth2()+"','"+satisfactionReport.getAvgQuestion1ForMonth3()+"','"+satisfactionReport.getAvgQuestion2ForMonth1()+"','"+satisfactionReport.getAvgQuestion2ForMonth2()+"','"+satisfactionReport.getAvgQuestion2ForMonth3()+"','"
		+satisfactionReport.getAvgQuestion3ForMonth1()+"','"+satisfactionReport.getAvgQuestion3ForMonth2()+"','"+satisfactionReport.getAvgQuestion3ForMonth3()+"','"+satisfactionReport.getAvgQuestion4ForMonth1()+"','"+satisfactionReport.getAvgQuestion4ForMonth2()+"','"+satisfactionReport.getAvgQuestion4ForMonth3()+"','"+satisfactionReport.getAvgQuestion5ForMonth1()+"','"+
					satisfactionReport.getAvgQuestion5ForMonth2()+"','"+satisfactionReport.getAvgQuestion5ForMonth3()+"','"+satisfactionReport.getAvgQuestion6ForMonth1()+"','"+satisfactionReport.getAvgQuestion6ForMonth2()+"','"+satisfactionReport.getAvgQuestion6ForMonth3()+"','"+satisfactionReport.getYear()+"','"+satisfactionReport.getQuarter()+"','"
		+ satisfactionReport.getStoreID()+"')"+";";
		} 
		
		
		
		
}	/// QueryFactory

