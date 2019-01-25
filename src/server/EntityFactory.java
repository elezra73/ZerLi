//EntityFactory

package server;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import common.ClientToServerMessage;
import common.Complaint;
import common.CustomerOrder;
import common.Product;
import common.SendableFile;
import common.Store;
import common.Survey;
import common.ZerliDate;
import common.reports.Report;
import common.reports.ReportComplaint;
import common.reports.ReportDetailInOrderReport;
import common.reports.ReportIncome;
import common.reports.ReportOrder;
import common.reports.ReportsStatisfaction;
import common.users.*;
import server.reports.OrderProduct;

/**
 * Create object entity by string type name.
 *
 */
public class EntityFactory {

	public static Object getEntity(ClientToServerMessage message, ResultSet rs) throws Exception {
		String entityName = message.getObjectType();
		ArrayList<Object> params = message.getParams();
		try {
			switch(entityName.toLowerCase().trim()) {
				case "deleted":
					return "Entry successfully deleted";
	
				case "customer":
					if (rs.next() == false)
						return "not registered";
					if (rs.getBoolean(11) == false) 
						return "Your account has been frozen";
					return new Customer(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getBoolean(4), rs.getString(5), rs.getString(6), 
						rs.getDouble(7),rs.getString(8),rs.getString(9), new ZerliDate(rs.getString(10)));
				
				case "customers":			// for system manager
					if (rs.next() == false)
						return "not registered";
					ArrayList<Customer> customers = new ArrayList<Customer>();
					do {
						if (rs.getBoolean(11) == true) {
							customers.add(new Customer(rs.getString(1), rs.getString(2), rs.getString(3),
									rs.getBoolean(4), rs.getString(5), rs.getString(6), 
									rs.getDouble(7),rs.getString(8),rs.getString(9), new ZerliDate(rs.getString(10))));
						}
					}while (rs.next());
					return customers;
				
				case "worker":
					if (rs.next() == false || rs.getBoolean(5) == false) 	// account inactive
						return "Worker account is not activated";
					return new Worker(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4));
				
				case "users":	// while users table
					if (rs.next() == false) {	
						return "users table empty";
					}
					LinkedHashMap<String,String> users = new LinkedHashMap<String,String>();
					do {
						users.put(rs.getString(1), rs.getString(3));
					} while(rs.next());
					return users;
		
				case "user":
					if (rs.next() == false) {	
						return "Account doesn't exist or has been frozen.";
					}
					return new UserLoginInfo(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4));
			
				case "userlogininfo":
					if (rs.next() == false || rs.getBoolean(4) == false) {	// account inactive
						return "Account doesn't exist or has been frozen.";
					}
					if (!rs.getString(2).equals(params.get(1))) {
						System.out.println("server password:"+ rs.getString(2));
						System.out.println("we send password:"+ params.get(1));
						return "Passwords mismatch.";
					}
					return new UserLoginInfo(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4));
				
				case "stores":
					if (rs.next() == false) {	// if exist inactive
						String err = "No stores was found in the system!";
						System.out.println(err);
						return err;
					}
					ArrayList<Store> stores = new ArrayList<Store>();
					do {
						stores.add(new Store(rs.getString(1),rs.getString(2)));
					}while (rs.next());
					return stores;
						
				case "storsbyid":	
					if (rs.next() == false) {	// if exist inactive
						String err = "No stores was found in the system!";
						System.out.println(err);
						return err;
					}
					ArrayList<String> storesById = new ArrayList<String>();
					do {
						storesById.add(rs.getString(1));
					}while (rs.next());
					return storesById;
				
				case "customersnamesbystoreid":
					if (rs.next() == false) {	// if exist inactive
						String err = "No stores was found in the system!";
						System.out.println(err);
						return err;
					}
					ArrayList<String> customertsNameByStoreID = new ArrayList<String>();
					do {
						customertsNameByStoreID.add("ID: " + rs.getString(1) + ", Name: " +rs.getString(2));
					}while (rs.next());
					return customertsNameByStoreID;
					
				case "allcomplaintreports":
					if ( rs.next()== false ) {	// if exist inactive
						System.out.println("'reportsComplaint' table  doesn't exist or the number of row chang in database go to EntityFactory class and chang case :allcomplaintreports");
						return "complaint reports doesn't exist.";
					}
	
					ArrayList<Report>  mComplaintsReports =new ArrayList<Report>();
					do { 
						Report mComplaintReport = new ReportComplaint(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),"Complaints Report");
						mComplaintsReports.add(mComplaintReport);
					} while((rs.next()));
					return mComplaintsReports;
					
				case "alldetailinorederreports":
					if (rs.next() == false  ) {	// if exist inactive
						System.out.println("'reportDetailInOrder' table  doesn't exist or the number of row chang in database go to EntityFactory class and chang case :alldetailinorederreports");
						return "'reportDetailInOrder' table  doesn't exist or the number of row chang in database go to EntityFactory class and chang case :alldetailinorederreports";
					}
					ArrayList<ReportDetailInOrderReport>  mDetailInOrders =new ArrayList<ReportDetailInOrderReport>();
					do { 
						ReportDetailInOrderReport mDetailInOrder = new ReportDetailInOrderReport(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4) );
						mDetailInOrders.add(mDetailInOrder);
					} while((rs.next()));
					return mDetailInOrders;

				case "allorederreports":
					if (rs.next() == false ) {	// if exist inactive
						System.out.println("'reportOrder' table doesn't exist or the number of row chang in database go to EntityFactory class and chang case :allorederreports");
						return "'reportOrder' table doesn't exist or the number of row chang in database go to EntityFactory class and chang case :allorederreports";
					}
					ArrayList<Report>  mOrderReports = new ArrayList<Report>();
					do { 
						Report mOrderReport = new ReportOrder(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),"Orders Report");
						mOrderReports.add(mOrderReport);
					} while((rs.next()));
					return mOrderReports;
				
				case "allincomereports":
					if (rs.next() == false ) {	// if exist inactive
						System.out.println("'reportIncome' table doesn't exist or the number of row chang in database go to EntityFactory class and chang case :'allincomereports'");
						return "'reportIncome' table doesn't exist or the number of row chang in database go to EntityFactory class and chang case :'allincomereports'";
					}
					ArrayList<Report>  mIncomeReports =new ArrayList<Report>();
					do { 
						Report mIncomeReport = new ReportIncome(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),"Income Report");
						mIncomeReports.add(mIncomeReport);
					} while((rs.next()));
					return mIncomeReports;
				
				case "allsatisfactionports":
						if ( rs.next() == false ) {	// if exist inactive
						System.out.println("'reportSatisfaction' table doesn't exist or the number of row chang in database go to EntityFactory class and chang case :'allsatisfactionports'");
						return "'reportSatisfaction' table doesn't exist or the number of row chang in database go to EntityFactory class and chang case :'allsatisfactionports'";
					}
		
					ArrayList<Report>  mStatisfactionReports =new ArrayList<Report>();
					do { 
						ReportsStatisfaction mStatisfactionReport = new ReportsStatisfaction(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),
								rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),
								rs.getString(12),rs.getString(13),rs.getString(14),rs.getString(15),rs.getString(16),rs.getString(17),rs.getString(18),
								rs.getString(19),rs.getString(20),rs.getString(21),rs.getString(22),
								"Satisfaction Report");
						mStatisfactionReports.add(mStatisfactionReport);
					} while((rs.next()));
					return mStatisfactionReports;
					
				case "allsurveyanswer":
					if (!rs.next())
						return "none";
					ArrayList<Survey> allSurveyAnswer = new ArrayList<Survey>();
					do {
						
						allSurveyAnswer.add(new Survey(rs.getString(1),rs.getString(2), rs.getString(3), rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),
								rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13))); 
					}while(rs.next());
					return allSurveyAnswer;
				
				case "surveyanswerbyspecificstore":
					if (! rs.next())
						return "none";
					ArrayList<Survey> SurveyAnswersBySpecificStore = new ArrayList<Survey>();
					do {
						
						SurveyAnswersBySpecificStore.add(new Survey(rs.getString(1),rs.getString(2), rs.getString(3), rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),
								rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13))); 
					}while(rs.next());
					return SurveyAnswersBySpecificStore;
				
				case "allorders":
					if (!rs.next())
						return "none";
					ArrayList<CustomerOrder> allOrders = new ArrayList<CustomerOrder>();
					do {
						
						allOrders.add(new CustomerOrder( rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),
								rs.getString(6), rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11)));
					}while(rs.next());
					return allOrders;
			
				case "allproductinordertable":
					if (!rs.next())
						return "none";
					ArrayList<OrderProduct> allProductOrder = new ArrayList<OrderProduct>();
					do {
						allProductOrder.add(new OrderProduct(rs.getString(1), rs.getString(2),rs.getString(3),rs.getString(4))); 
					}while(rs.next());
					return allProductOrder;
			
				case "allproducts":
					if (!rs.next())
						return "none";
					ArrayList<Product> allproducts =new ArrayList<Product>();
					do {
						allproducts.add(new Product(rs.getString(1),rs.getString(2),rs.getString(3),rs.getDouble(4), rs.getString(5), rs.getString(6)));
					}while(rs.next());   //if exist
				return allproducts;
				
			case "allcomplaints":
				if (!rs.next())
					return "none";
				ArrayList<Complaint> allComplaint =new ArrayList<Complaint>();
				do {
					allComplaint.add(new Complaint(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4), rs.getString(5), rs.getString(6),rs.getString(7),rs.getString(8) ));
				}while(rs.next());   //if exist
				return allComplaint;

				case "catalog":
					ArrayList<Product> catalog =new ArrayList<Product>();
					while(rs.next()) {  //if exist
						Product product = new Product(rs.getString(1),rs.getString(2),rs.getString(3),rs.getDouble(4), rs.getString(5) ,rs.getString(6));
						if(product.getImageName() != null)
							product.setPicture(new SendableFile(DataBaseServer.IMAGES_DIR + product.getImageName()));
						catalog.add(product);
					}
					return catalog;
	
				case "complaints":
					if ( rs.next() == false || rs.getBoolean(8) == false) {	// if exist inactive
						return "complaint doesn't exist.";
					}
		
					ArrayList<Complaint>  complaints =new ArrayList<Complaint>();
					do { 
						Complaint mComplaintCustomerID = new Complaint(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8));
						complaints.add(mComplaintCustomerID);
					} while((rs.next()));
					return complaints;
	
				case "percent_price_product_by_storeid":
					HashMap<String ,Double > percent_price_product_by_storeID = new HashMap<String ,Double>();
					if(rs.isFirst()) 
					{  //if exist
						percent_price_product_by_storeID.put(rs.getString(1) , rs.getDouble(2));
					}
					while(rs.next())
					{
						
						percent_price_product_by_storeID.put(rs.getString(1) , rs.getDouble(2));
					}
					return percent_price_product_by_storeID;
			
				case "self_composition":
					ArrayList<Product> self_composition =new ArrayList<Product>();
				while(rs.next()) {  //if exist
					self_composition.add(new Product(rs.getString(1),rs.getString(2),rs.getString(3),rs.getDouble(4), rs.getString(5), rs.getString(6)));
				}
				return self_composition;
				
				case "active_orders":
					if (!rs.next())
						return "none";
					ArrayList<CustomerOrder> orders = new ArrayList<CustomerOrder>();
					do {
						ZerliDate oDate = new ZerliDate(rs.getString(4));
						ZerliDate dDate = new ZerliDate(rs.getString(10));
						orders.add(new CustomerOrder(rs.getString(1), rs.getString(2), rs.getString(3), 
								oDate , rs.getString(5), rs.getString(6), rs.getString(7), rs.getDouble(8)
								,rs.getString(9),dDate));
					}while(rs.next());
					return orders;
			
				case "new order":
					return "Order Succeed!";

			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
}
