package common.reports;

public class ReportIncome extends Report {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3957027236834390587L;
	private String IncomeAmount1;
	private String IncomeAmount2;
	private String IncomeAmount3;
	/**
	 * Constructor
	 * @param reportsID reportsID
	 * @param storeID	storeID
	 * @param incomeAmount1 is amount of first month of quarter	
	 * @param incomeAmount2 is amount of second month of quarter
	 * @param incomeAmount3 is amount of third month of quarter
	 * @param year year
	 * @param quartely	quarter
	 * @param reportType	report type
	 */
	public ReportIncome(String reportsID, String storeID
			, String incomeAmount1, String incomeAmount2, String incomeAmount3,String year, String quartely,String reportType) {
		super(reportsID, storeID, reportType, quartely, year);
		IncomeAmount1 = incomeAmount1;
		IncomeAmount2 = incomeAmount2;
		IncomeAmount3 = incomeAmount3;
	}
	/**
	 * Empty constructor
	 */
	public ReportIncome() {
		// TODO Auto-generated constructor stub
	}
	public String getIncomeAmount1() {
		return IncomeAmount1;
	}
	public void setIncomeAmount1(String incomeAmount1) {
		IncomeAmount1 = incomeAmount1;
	}
	public String getIncomeAmount2() {
		return IncomeAmount2;
	}
	public void setIncomeAmount2(String incomeAmount2) {
		IncomeAmount2 = incomeAmount2;
	}
	public String getIncomeAmount3() {
		return IncomeAmount3;
	}
	public void setIncomeAmount3(String incomeAmount3) {
		IncomeAmount3 = incomeAmount3;
	}

	@Override
	public String toString() {
		return "ReportIncome [IncomeAmount1=" + IncomeAmount1 + ", IncomeAmount2=" + IncomeAmount2 + ", IncomeAmount3="
				+ IncomeAmount3 + ", mReportsID=" + mReportsID + ", mStoreID=" + mStoreID + ", mReportType="
				+ mReportType + ", mQuartely=" + mQuarter + ", mYearReport=" + mYear
				+ ", mMonth1=" + mMonth1 + ", mMonth2=" + mMonth2 + ", mMonth3=" + mMonth3 + "]\n";
	}
	
	

	

}