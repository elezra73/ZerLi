package server.tasks;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.TimerTask;
import common.ClientToServerMessage;
import common.Complaint;
import common.EQueryOption;
import common.ZerliDate;
import server.DataBaseHandler;
import server.QueryFactory;

public class ComplaintCheckTask extends TimerTask {
	private static DataBaseHandler dbHandler = new DataBaseHandler();
	private static final double reward =50;
	private static final double maxTimeToCheck= 24;
	
	/**
	 * this function get all the Active  complaint and give a reward if the complaint not traited more than 24 hour from the open date .
	 */
	@Override
	public void run() {  
		
		ZerliDate now = new ZerliDate();
		ArrayList<Object>paramsForReward = new ArrayList<Object>();
		ArrayList<Object>paramsForCancelComplaint = new ArrayList<Object>();
		ClientToServerMessage message ;
		String getAllActiveComplaintQuery;
		ArrayList<String> RewardsQueries = new ArrayList<String>();
		ArrayList<String> cancelComplaintsQueries = new ArrayList<String>();
		try {
			message = new ClientToServerMessage(EQueryOption.GET_ALL_ACTIVE_COMPLAINTS, paramsForReward, null);
			getAllActiveComplaintQuery = QueryFactory.getQuery(message);
			ResultSet rs = dbHandler.execute(getAllActiveComplaintQuery);
			if(rs!=null && rs.next()!=false)
			{
				ArrayList<Complaint> complaints = new ArrayList<Complaint>();
				do {
					complaints.add(new Complaint(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));			
				} while (rs.next());
				///////expected CustomerID,  Reward and StoreID.
				for (Complaint complaint : complaints) {
					if(now.getDifferenceInHours(complaint.getOpenDate())>=maxTimeToCheck)
					{
						paramsForReward = new ArrayList<Object>();
						paramsForReward.add(complaint.getCustomerID());
						paramsForReward.add(reward);
						paramsForReward.add(complaint.getStoreID());
						message= new ClientToServerMessage(EQueryOption.REWARD_ACCOUNT, paramsForReward, null);
						RewardsQueries.add(QueryFactory.getQuery(message));
						
						paramsForCancelComplaint = new ArrayList<Object>();
						paramsForCancelComplaint.add(complaint.getComplaintID());
						message = new ClientToServerMessage(EQueryOption.UPDATE_COMPLAINT, paramsForCancelComplaint, null);
						cancelComplaintsQueries.add(QueryFactory.getQuery(message));
					}
				}
				if(RewardsQueries.size()>0 && cancelComplaintsQueries.size()>0)
				{
					dbHandler.execute(RewardsQueries);
					dbHandler.execute(cancelComplaintsQueries);
					
				}
				System.err.println("ComplaintCheckTask done!"); // for red color
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block'
			System.err.println("ComplaintCheckTask: "+e.toString());
		}
	}



}
