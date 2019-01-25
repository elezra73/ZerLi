package server;

import java.util.Timer;

import server.tasks.ComplaintCheckTask;
import server.tasks.SubscriptionCheckTask;
import server.tasks.ReportTask;

public class ZerliTimer {
	private  Timer complaintTimer;
	private  Timer reportTimer;
	private  Timer subsciptiontTimer;
	private static ComplaintCheckTask complaintTask  = new ComplaintCheckTask();
	private static SubscriptionCheckTask monthlySubscriptionCheckTask  = new SubscriptionCheckTask();
	private static ReportTask reportTask  = new ReportTask();
	public static int HOUR_IN_SEC = 60*60*1000;
	public static int DAY_IN_SEC = HOUR_IN_SEC*24*1000;
	
	/**
	 * 
	 * @param complaintTime: value of the interval of the Complaint Check Task 
	 * @param reportTime: value of the interval of the Report Generate Task 
	 * @param subsciptiontTimer: value of the interval of the subscription Check Task 
	 */
	public ZerliTimer(long complaintTime, long reportTime, long subsciptiontTimer)
	{
		this.complaintTimer = new Timer(true);
		this.complaintTimer.schedule(complaintTask,0,complaintTime);
		this.subsciptiontTimer = new Timer(true);
		this.subsciptiontTimer.schedule(monthlySubscriptionCheckTask, 0,subsciptiontTimer);
		this.reportTimer = new Timer(true);
		reportTimer.schedule(reportTask, 0,reportTime);
		
	}
	
	
	
	
	
	
	
	

}
