package server.tasks;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.TimerTask;

import common.ClientToServerMessage;
import common.EQueryOption;
import common.ZerliDate;
import common.users.Customer;
import server.DataBaseHandler;
import server.QueryFactory;

public class SubscriptionCheckTask extends TimerTask {
	private static DataBaseHandler dbHandler = new DataBaseHandler();
	private static final String monthlySubscription= "MonthlySubscription";
	private static final String yearlySubscription= "YearlySubscription";
	

	@Override
	public void run() {
		checkSubciption(monthlySubscription,30);
		checkSubciption(yearlySubscription,365);
		System.err.println("SubscriptionCheckTask done!");	// for red color
		
	}
	/**
	 * 
	 * @param subscription : the kind of subscription 
	 * @param endTime : the end time of conclusion
	 * this function get from all the customer with the given subscription type and set their payable status to 0. 
	 */
	private void checkSubciption(String subscription,long endTime)
	{
		ZerliDate now = new ZerliDate();
		ArrayList<Object>paramsForCancelSubscription = new ArrayList<Object>();
		ArrayList<Customer> customers = new ArrayList<Customer>();
		ClientToServerMessage message ;
		String queryForGetTheCustomers;
		ArrayList<String> queriesForCancelSubscription;
		
		try {
			// check subscription start
			paramsForCancelSubscription.add(subscription);
			message = new ClientToServerMessage(EQueryOption.GET_CUSTOMERS_BY_SUBSCRIPTION, paramsForCancelSubscription, null);
			queryForGetTheCustomers= QueryFactory.getQuery(message);
			ResultSet rs = dbHandler.execute(queryForGetTheCustomers);
			paramsForCancelSubscription.clear();
			if(rs!=null && rs.next()!=false)
			{
				queriesForCancelSubscription =new ArrayList<String>();
				do {
					customers.add(new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4), 
							rs.getString(5), rs.getString(6), rs.getDouble(7), rs.getString(8), rs.getString(9), new ZerliDate(rs.getString(10))));
					if(now.getDifferenceInDays(customers.get(customers.size()-1).getAccountOpenDate())>endTime)
					{
						paramsForCancelSubscription.add(customers.get(customers.size()-1).getUserID());
						paramsForCancelSubscription.add(customers.get(customers.size()-1).getStoreID());
						message = new ClientToServerMessage(EQueryOption.DISABLE_PAYABLE_ACCOUNT, paramsForCancelSubscription, null);
						queriesForCancelSubscription.add(QueryFactory.getQuery(message)) ;
						paramsForCancelSubscription.clear();
					}
					
				} while (rs.next());
				if(queriesForCancelSubscription.size()>0)
					dbHandler.execute(queriesForCancelSubscription);
			}
			
			//check subscription end
			
			
			
		} catch (Exception e) {
			System.err.println("SubscriptionCheckTask: "+e);
		}

	}

}


