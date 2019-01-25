package common.reports;


import java.util.ArrayList;

public class ReportOrder extends Report {
	
	private   ArrayList<ReportDetailInOrderReport> mDetailsInReport=new ArrayList<ReportDetailInOrderReport>();//this is all Quantity of products purchased
	private  ArrayList<ReportDetailInOrderReport> mProductAndQuantity=new ArrayList<ReportDetailInOrderReport>();//Segmented according to report
	private static final long serialVersionUID = -6256326715525918352L;
	/**
	 * Constructor
	 * @param reportsID	report ID
	 * @param storeID	store iD
	 * @param year	year
	 * @param quartely quarter
	 * @param reportType report type
	 */
	public ReportOrder(String reportsID, String storeID,String year ,String quartely, String reportType) {
		super(reportsID, storeID, reportType, quartely, year);
		// TODO Auto-generated constructor stub
	}
	/**
	 * Empty constructor
	 */
	public ReportOrder() {
		
	}

	
	
	
	
	public ArrayList<ReportDetailInOrderReport> getDetailsInReport() {
		return mDetailsInReport;
	}
	public void setDetailsInReport(ArrayList<ReportDetailInOrderReport> detailsInReport) {
		mDetailsInReport = detailsInReport;
	}


	public ArrayList<ReportDetailInOrderReport> getProductAndQuantity() {
		return mProductAndQuantity;
	}


	public void setProductAndQuantity(ArrayList<ReportDetailInOrderReport> productAndQuantity) {
		mProductAndQuantity = productAndQuantity;
	}

	
	
	@Override
	public String toString() {
		return "ReportOrder "  + ", mReportsID=" + mReportsID + ", mStoreID="
				+ mStoreID + ", mReportType=" + mReportType + ", mQuartely=" + mQuarter + ", mYearReport="
				+ mYear + ", mMonth1=" + mMonth1 + ", mMonth2=" + mMonth2
				+ ", mMonth3=" + mMonth3  + "]\n [mProductAndQuantity="+mProductAndQuantity;
	}

	


	
	
	
	

}