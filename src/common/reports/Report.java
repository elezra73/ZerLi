package common.reports;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Report implements Serializable  {

	private static final long serialVersionUID = 6966808654016888771L;
	protected String mReportsID;
	protected String mStoreID;
	protected String mReportType;
	protected String mQuarter;
	protected String mYear;
	protected String mMonth1;
	protected String mMonth2;
	protected String mMonth3;
	
	
	protected static Set<String> mYearsSet = new HashSet<String>();


	/*
	 * Constructor
	 */
	public Report() {
		
	}
	public Report(String reportsID, String storeID, String reportType, String quarter, String year) {
		mReportsID = reportsID;
		mStoreID = storeID;
		mReportType = reportType;
		mQuarter = quarter;
		setMonthByQuarter(quarter);
		mYear = year;
		mYearsSet.add(year);
	}
	
	public static Set<String> getYearsSet() {
		return mYearsSet;
	}
	
	
	/*
	 * A method that gets and sets the quarter months respectively for the quarter.
	 */
	public void setMonthByQuarter(String mQuartely) {
		if(mQuartely!=null) {
			if(mQuartely.equals("1")) {
				setMonths("1","2","3");
			}
			
			if(mQuartely.equals("2")) {
				setMonths("4","5","6");
			}
			if(mQuartely.equals("3")) {
				setMonths("7","8","9");
			}
			if(mQuartely.equals("4")) {
				setMonths("10","11","12");
			}
		}
	}
	/*
	 * set the months 
	 */
	public void setMonths(String mMonth1,String mMonth2,String mMonth3) {
		setMonth1(mMonth1);
		setMonth2(mMonth2);
		setMonth3(mMonth3);
		
	}
	public boolean equalQuarter(Report report) {
		if(this.mQuarter.equals(mQuarter)) {
			return true;
		}
		else {
		return false;
		}
	}
	
	
	
	public String getReportsID() {
		return mReportsID;
	}
	public void setReportsID(String reportsID) {
		mReportsID = reportsID;
	}
	public String getStoreID() {
		return mStoreID;
	}
	public void setStoreID(String storeID) {
		mStoreID = storeID;
	}
	public String getReportType() {
		return mReportType;
	}
	public void setReportType(String reportType) {
		mReportType = reportType;
	}
	public String getQuarter() {
		return mQuarter;
	}
	public void setQuartely(String quartely) {
		mQuarter = quartely;
		setMonthByQuarter(quartely);
	}
	public String getYear() {
		return mYear;
	}
	public void setYear(String year) {
		mYear = year;
	}
	public String getMonth1() {
		return mMonth1;
	}
	public void setMonth1(String month1) {
		mMonth1 = month1;
	}
	public String getMonth2() {
		return mMonth2;
	}
	public void setMonth2(String month2) {
		mMonth2 = month2;
	}
	public String getMonth3() {
		return mMonth3;
	}
	public void setMonth3(String month3) {
		mMonth3 = month3;
	}
	
	

	
	

}