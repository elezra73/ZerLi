package common.reports;
public class ReportsStatisfaction extends Report   {
	
	private static final long serialVersionUID = 1505046580056726564L;
	//private String SurveyId; //for futuristic
	//private String SurveyName;//for futuristic

	private String AvgQuestion1ForMonth1;//this is Ave of question number 1 in survey  of First Month's quarter
	private String AvgQuestion1ForMonth2;//this is Ave of question number 1 in survey  of Second Month's quarter
	private String AvgQuestion1ForMonth3;//this is Ave of question number 1 in survey  of third Month's quarter
	
	private String AvgQuestion2ForMonth1;//this is Ave of question number 2 in survey  of third Month's quarter
	private String AvgQuestion2ForMonth2;//this is Ave of question number 2 in survey  of third Month's quarter
	private String AvgQuestion2ForMonth3;//this is Ave of question number 2 in survey  of third Month's quarter
	
	private String AvgQuestion3ForMonth1;//this is Ave of question number 3 in survey  of third Month's quarter
	private String AvgQuestion3ForMonth2;//this is Ave of question number 3 in survey  of third Month's quarter
	private String AvgQuestion3ForMonth3;//this is Ave of question number 3 in survey  of third Month's quarter
	
	private String AvgQuestion4ForMonth1;
	private String AvgQuestion4ForMonth2;
	private String AvgQuestion4ForMonth3;
	
	private String AvgQuestion5ForMonth1;
	private String AvgQuestion5ForMonth2;
	private String AvgQuestion5ForMonth3;
	
	private String AvgQuestion6ForMonth1;
	private String AvgQuestion6ForMonth2;
	private String AvgQuestion6ForMonth3;
	
	/**
	 * Empty Contractor of satisfaction report
	 */
	public ReportsStatisfaction() {
	}
/**
 * Contractor of satisfaction report
 * @param reportsID report
 * @param quarter quarter
 * @param avgQuestion1ForMonth1	Q1Month1
 * @param avgQuestion1ForMonth2 Q1Month2
 * @param avgQuestion1ForMonth3 Q1Month3
 * @param avgQuestion2ForMonth1 Q1Month2
 * @param avgQuestion2ForMonth2 Q2Month2
 * @param avgQuestion2ForMonth3 Q3Month3
 * @param avgQuestion3ForMonth1 Q1Month3
 * @param avgQuestion3ForMonth2 Q2Month3
 * @param avgQuestion3ForMonth3 Q3Month3
 * @param avgQuestion4ForMonth1 Q1Month4
 * @param avgQuestion4ForMonth2 Q2Month4
 * @param avgQuestion4ForMonth3 Q3Month4
 * @param avgQuestion5ForMonth1 Q1Month5
 * @param avgQuestion5ForMonth2 Q2Month5
 * @param avgQuestion5ForMonth3 Q3Month5
 * @param avgQuestion6ForMonth1 Q1Month6
 * @param avgQuestion6ForMonth2 Q2Month6
 * @param avgQuestion6ForMonth3 Q3Month6
 * @param year year
 * @param storeID store id
 * @param reportType report type
 */
	public ReportsStatisfaction(String reportsID,String avgQuestion1ForMonth1, String avgQuestion1ForMonth2,
			String avgQuestion1ForMonth3, String avgQuestion2ForMonth1, String avgQuestion2ForMonth2,
			String avgQuestion2ForMonth3, String avgQuestion3ForMonth1, String avgQuestion3ForMonth2,
			String avgQuestion3ForMonth3, String avgQuestion4ForMonth1, String avgQuestion4ForMonth2,
			String avgQuestion4ForMonth3, String avgQuestion5ForMonth1, String avgQuestion5ForMonth2,
			String avgQuestion5ForMonth3, String avgQuestion6ForMonth1, String avgQuestion6ForMonth2,
			String avgQuestion6ForMonth3,String year, String quarter, String storeID ,String reportType) {
		super(reportsID, storeID, reportType, quarter, year);
		AvgQuestion1ForMonth1 = avgQuestion1ForMonth1;
		AvgQuestion1ForMonth2 = avgQuestion1ForMonth2;
		AvgQuestion1ForMonth3 = avgQuestion1ForMonth3;
		AvgQuestion2ForMonth1 = avgQuestion2ForMonth1;
		AvgQuestion2ForMonth2 = avgQuestion2ForMonth2;
		AvgQuestion2ForMonth3 = avgQuestion2ForMonth3;
		AvgQuestion3ForMonth1 = avgQuestion3ForMonth1;
		AvgQuestion3ForMonth2 = avgQuestion3ForMonth2;
		AvgQuestion3ForMonth3 = avgQuestion3ForMonth3;
		AvgQuestion4ForMonth1 = avgQuestion4ForMonth1;
		AvgQuestion4ForMonth2 = avgQuestion4ForMonth2;
		AvgQuestion4ForMonth3 = avgQuestion4ForMonth3;
		AvgQuestion5ForMonth1 = avgQuestion5ForMonth1;
		AvgQuestion5ForMonth2 = avgQuestion5ForMonth2;
		AvgQuestion5ForMonth3 = avgQuestion5ForMonth3;
		AvgQuestion6ForMonth1 = avgQuestion6ForMonth1;
		AvgQuestion6ForMonth2 = avgQuestion6ForMonth2;
		AvgQuestion6ForMonth3 = avgQuestion6ForMonth3;
		
		
	}
	

	public String getAvgQuestion1ForMonth1() {
		return AvgQuestion1ForMonth1;
	}
	public void setAvgQuestion1ForMonth1(String avgQuestion1ForMonth1) {
		AvgQuestion1ForMonth1 = avgQuestion1ForMonth1;
	}

	public String getAvgQuestion1ForMonth2() {
		return AvgQuestion1ForMonth2;
	}

	public void setAvgQuestion1ForMonth2(String avgQuestion1ForMonth2) {
		AvgQuestion1ForMonth2 = avgQuestion1ForMonth2;
	}

	public String getAvgQuestion1ForMonth3() {
		return AvgQuestion1ForMonth3;
	}

	public void setAvgQuestion1ForMonth3(String avgQuestion1ForMonth3) {
		AvgQuestion1ForMonth3 = avgQuestion1ForMonth3;
	}

	public String getAvgQuestion2ForMonth1() {
		return AvgQuestion2ForMonth1;
	}

	public void setAvgQuestion2ForMonth1(String avgQuestion2ForMonth1) {
		AvgQuestion2ForMonth1 = avgQuestion2ForMonth1;
	}

	public String getAvgQuestion2ForMonth2() {
		return AvgQuestion2ForMonth2;
	}

	public void setAvgQuestion2ForMonth2(String avgQuestion2ForMonth2) {
		AvgQuestion2ForMonth2 = avgQuestion2ForMonth2;
	}

	public String getAvgQuestion2ForMonth3() {
		return AvgQuestion2ForMonth3;
	}

	public void setAvgQuestion2ForMonth3(String avgQuestion2ForMonth3) {
		AvgQuestion2ForMonth3 = avgQuestion2ForMonth3;
	}

	public String getAvgQuestion3ForMonth1() {
		return AvgQuestion3ForMonth1;
	}

	public void setAvgQuestion3ForMonth1(String avgQuestion3ForMonth1) {
		AvgQuestion3ForMonth1 = avgQuestion3ForMonth1;
	}

	public String getAvgQuestion3ForMonth2() {
		return AvgQuestion3ForMonth2;
	}

	public void setAvgQuestion3ForMonth2(String avgQuestion3ForMonth2) {
		AvgQuestion3ForMonth2 = avgQuestion3ForMonth2;
	}

	public String getAvgQuestion3ForMonth3() {
		return AvgQuestion3ForMonth3;
	}

	public void setAvgQuestion3ForMonth3(String avgQuestion3ForMonth3) {
		AvgQuestion3ForMonth3 = avgQuestion3ForMonth3;
	}


	public String getAvgQuestion4ForMonth1() {
		return AvgQuestion4ForMonth1;
	}

	public void setAvgQuestion4ForMonth1(String avgQuestion4ForMonth1) {
		AvgQuestion4ForMonth1 = avgQuestion4ForMonth1;
	}

	public String getAvgQuestion4ForMonth2() {
		return AvgQuestion4ForMonth2;
	}

	public void setAvgQuestion4ForMonth2(String avgQuestion4ForMonth2) {
		AvgQuestion4ForMonth2 = avgQuestion4ForMonth2;
	}

	public String getAvgQuestion4ForMonth3() {
		return AvgQuestion4ForMonth3;
	}

	public void setAvgQuestion4ForMonth3(String avgQuestion4ForMonth3) {
		AvgQuestion4ForMonth3 = avgQuestion4ForMonth3;
	}


	public String getAvgQuestion5ForMonth1() {
		return AvgQuestion5ForMonth1;
	}

	public void setAvgQuestion5ForMonth1(String avgQuestion5ForMonth1) {
		AvgQuestion5ForMonth1 = avgQuestion5ForMonth1;
	}

	public String getAvgQuestion5ForMonth2() {
		return AvgQuestion5ForMonth2;
	}

	public void setAvgQuestion5ForMonth2(String avgQuestion5ForMonth2) {
		AvgQuestion5ForMonth2 = avgQuestion5ForMonth2;
	}

	public String getAvgQuestion5ForMonth3() {
		return AvgQuestion5ForMonth3;
	}

	public void setAvgQuestion5ForMonth3(String avgQuestion5ForMonth3) {
		AvgQuestion5ForMonth3 = avgQuestion5ForMonth3;
	}

	public String getAvgQuestion6ForMonth1() {
		return AvgQuestion6ForMonth1;
	}

	public void setAvgQuestion6ForMonth1(String avgQuestion6ForMonth1) {
		AvgQuestion6ForMonth1 = avgQuestion6ForMonth1;
	}

	public String getAvgQuestion6ForMonth2() {
		return AvgQuestion6ForMonth2;
	}

	public void setAvgQuestion6ForMonth2(String avgQuestion6ForMonth2) {
		AvgQuestion6ForMonth2 = avgQuestion6ForMonth2;
	}

	public String getAvgQuestion6ForMonth3() {
		return AvgQuestion6ForMonth3;
	}

	public void setAvgQuestion6ForMonth3(String avgQuestion6ForMonth3) {
		AvgQuestion6ForMonth3 = avgQuestion6ForMonth3;
	}


	

	

}