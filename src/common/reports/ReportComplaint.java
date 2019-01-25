package common.reports;





public class ReportComplaint extends Report {

	private static final long serialVersionUID = -6512917681733475477L;
	private String mQuantityComplaint1;
	private String mQuantityComplaint2;
	private String mQuantityComplaint3;
	
/**
 * Constructor of Complaint Report
 * @param reportsID report ID
 * @param storeID store ID
 * @param quantityComplaint1 is quantity complaint of first Month's  quarter
 * @param quantityComplaint2 is quantity complaint of second Month's  quarter
 * @param quantityComplaint3 is quantity complaint of third Month's  quarter
 * @param year is of report
 * @param quartely quarterly
 * @param reportType	report type
 */
	public ReportComplaint(String reportsID, String storeID ,String quantityComplaint1, String quantityComplaint2, String quantityComplaint3, String year, String quartely,
			String reportType) {
		super(reportsID, storeID, reportType, quartely, year);
		mQuantityComplaint1 = quantityComplaint1;
		mQuantityComplaint2 = quantityComplaint2;
		mQuantityComplaint3 = quantityComplaint3;
	}
	
	/**
	 * Empty constructor
	 */
	public ReportComplaint() {
		// TODO Auto-generated constructor stub
	}


	
	public String getQuantityComplaint1() {
		return mQuantityComplaint1;
	}
	public void setQuantityComplaint1(String quantityComplaint1) {
		mQuantityComplaint1 = quantityComplaint1;
	}
	public String getQuantityComplaint2() {
		return mQuantityComplaint2;
	}
	public void setQuantityComplaint2(String quantityComplaint2) {
		mQuantityComplaint2 = quantityComplaint2;
	}
	public String getQuantityComplaint3() {
		return mQuantityComplaint3;
	}
	public void setQuantityComplaint3(String quantityComplaint3) {
		mQuantityComplaint3 = quantityComplaint3;
	}
	@Override
	public String toString() {
		return "ReportComplaint [mQuantityComplaint1=" + mQuantityComplaint1 + ", mQuantityComplaint2="
				+ mQuantityComplaint2 + ", mQuantityComplaint3=" + mQuantityComplaint3 + ", mReportsID=" + mReportsID
				+ ", mStoreID=" + mStoreID + ", mReportType=" + mReportType + ", mQuartely=" + mQuarter
				+ ", mYearReport=" + mYear + ", mMonth1=" + mMonth1 + ", mMonth2="
				+ mMonth2 + ", mMonth3=" + mMonth3 + "]\n";
	}
		

}