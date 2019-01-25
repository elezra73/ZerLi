// EqueryOption

package common;

/**
 * Have to update initMap() and getQuery() in QueryFactory
 * when updating EQueryOption and vice versa.
 *
 */
public enum EQueryOption {
	

	// users table related enums
	LOGIN_REQUEST,
	LOGOUT_REQUEST,
	GET_ALL_USERS,
	//
	
	// customers table related enums
	GET_CUSTOMER_INFO,
	UPDATE_CUSTOMER_INFO,
	ADD_NEW_CUSTOMER,
	//
		
	// workers table related enums
	GET_WORKER_INFO,
	CHANGE_WORKER_PERMISSION,
	//
	
	// System Manager Queries
	FREEZE_USER_ACCOUNT,
	UNFREEZE_USER_ACCOUNT,
	VIEW_USER_PERMISSION,
	DISABLE_PAYABLE_ACCOUNT,
	
	//
	
	//	store table related
	GET_ALL_STORES,
	//
	
	//
	GET_STORES_ID_BY_CUS_ID,			// get all the stores that customer
	//
	GET_All_CUSTOMERS,
	GET_CUSTOMERS_NAME_BY_STOREID,
	GET_CUSTOMERS_BY_SUBSCRIPTION,

	
	// Products table related enums
	GET_PRODUCT_INFO,
	UPDATE_PRODUCT_INFO,
	ADD_NEW_PRODUCT,
	DELETE_PRODUCT,
	//
		
	///Customer Order Query 
	ADD_CUSTOMER_ORDER,
	GET_CUSTOMER_ACTIVE_ORDERS,
	CANCEL_CUSTOMER_ORDER,

	ADD_COMPLAINT,
	
	
	//Survey Query
	ADD_FILLED_SURVEY,
	SURVEY_ADD_PROFESSIONAL_REVIEW,	
	GET_SURVEY_FROM_SPECIFIC_STORE,
		
	// catalog table related
	GET_CATALOG,		// get main catalog ,
	GET_PRODUCT_PERCENT_BY_STOREID,		// get the percent for all product for final price,
	SET_PRODUCT_SALE_PERCENT,			// set a specific product sale %
	DELETE_PRODUCT_SALE_PERCENT,	
	
	GET_SELF_COMPOSITION,				// get the products for self composition
	
	//complaint reports related enum
	GET_All_COMPLAINT_REPORTS_INFO,
	//detail in order table related enum
	GET_All_DETAIL_IN_ORDER_REPORTS_INFO,
	//income reports table 
	GET_All_INCOME_REPORTS_INFO,
	//order reports table
	GET_All_ORDER_REPORTS_INFO,
	//satisfaction reports table
	GET_All_SATISFACTION_REPORTS_INFO,
	GET_All_CUSTOMER_ORDER,
	GET_All_PRODUCT_IN_ORDER_TABLE,
	GET_All_PRODUCTS,
	GET_All_COMPLAINTS,
	GET_ALL_ACTIVE_COMPLAINTS,
	GET_All_SURVEY_ANSWER,
	//CHECK_CUSTOMER_EXIST,
	//create new complaint
	NEW_COMPLAINT,
	//get list of complaints
	GET_COMPLAINTS_BY_CUSTOMER_ID,
	GET_COMPLAINTS_BY_WORKER_ID,
	GET_COMPLAINT_BY_COMPLAINT_ID,
	//add reward to account
	REWARD_ACCOUNT,
	//change isactive to false
	UPDATE_COMPLAINT,
	
	UPDATE_COMPLAINT_AND_REWARD_ACCOUNT
}
