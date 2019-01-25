package server.tasks;

import java.text.ParseException;
import java.util.TimerTask;

import common.ZerliDate;
import server.reports.CreateReports;

public class ReportTask extends TimerTask {

	private static int nextQuarter; 
	private static CreateReports cReports = new CreateReports();
	public ReportTask()
	{
		try {
			nextQuarter  = new ZerliDate().getQuarter()+1;
			if (nextQuarter == 5)
				nextQuarter = 1;
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * this function make reports every starting quater
	 */
	@Override
	public void run() {
		ZerliDate now = new ZerliDate();
		try {
			 if(nextQuarter== now.getQuarter())
			{
				cReports.generateReports(nextQuarter, now.getYearInInteger());			
				nextQuarter= now.getQuarter()+1;
				if (nextQuarter == 5)
					nextQuarter = 1;
				System.err.println("Report Task done");
			}
			 
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
