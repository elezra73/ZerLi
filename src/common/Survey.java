package common;

import java.io.Serializable;
import java.text.ParseException;

public class Survey implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2797129909604624437L;
	private String mIncremendID;
	private String mSurveyID ;
	private String CustomerID;
	private String mStoreID;
	private ZerliDate mAnswerDate;
	private int mA1;
	private int mA2;
	private int mA3;
	private int mA4;
	private int mA5;
	private int mA6;
	private String mIsActive;
	private String mConclusuon;
	private int quorter;
	private int mounth;
	private int year;
/**
 * @param incrementid increment id for DB
 * @param surveyID	survey id
 * @param customerID	customer id
 * @param storeID	store id
 * @param answerDate	answer date of survey
 * @param a1 is customer answer between 1-10
 * @param a2 is customer answer between 1-10
 * @param a3 is customer answer between 1-10
 * @param a4 is customer answer between 1-10
 * @param a5 is customer answer between 1-10
 * @param a6 is customer answer between 1-10
 * @param isActive	if is active
 * @param conclusuon	conclusion
 * Constructor survey
 */
	public Survey(String incrementid , String surveyID, String customerID, String storeID, String answerDate, String a1, String a2,
			String a3, String a4, String a5, String a6, String isActive, String conclusuon) {
		super();
		mIncremendID = incrementid;
		mSurveyID = surveyID;
		CustomerID = customerID;
		mStoreID = storeID;
		try {
			this.mAnswerDate=new ZerliDate (answerDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mA1 = Integer.parseInt(a1);
		mA2 = Integer.parseInt(a2);
		mA3 = Integer.parseInt(a3);
		mA4 = Integer.parseInt(a4);
		mA5 = Integer.parseInt(a5);
		mA6 = Integer.parseInt(a6);
		mIsActive = isActive;
		mConclusuon = conclusuon;
	}

	public Survey() {
		// TODO Auto-generated constructor stub
	}
	public String getIncrementID() {
		return mIncremendID;
	}
	
	public String getSurveyID() {
		return mSurveyID;
	}
	public void setSurveyID(String surveyID) {
		mSurveyID = surveyID;
	}
	public String getCustomerID() {
		return CustomerID;
	}
	public void setCustomerID(String customerID) {
		CustomerID = customerID;
	}
	public String getStoreID() {
		return mStoreID;
	}
	public void setStoreID(String storeID) {
		mStoreID = storeID;
	}
	public ZerliDate getAnswerDate() {
		return mAnswerDate;
	}
	public void setAnswerDate(ZerliDate answerDate) {
		mAnswerDate = answerDate;
	}

	public int getA1() {
		return mA1;
	}
	public void setA1(int a1) {
		mA1 = a1;
	}
	public int getA2() {
		return mA2;
	}
	public void setA2(int a2) {
		mA2 = a2;
	}
	public int getA3() {
		return mA3;
	}
	public void setA3(int a3) {
		mA3 = a3;
	}
	public int getA4() {
		return mA4;
	}
	public void setA4(int a4) {
		mA4 = a4;
	}
	public int getA5() {
		return mA5;
	}
	public void setA5(int a5) {
		mA5 = a5;
	}
	public int getA6() {
		return mA6;
	}
	public void setA6(int a6) {
		mA6 = a6;
	}
	public String getIsActive() {
		return mIsActive;
	}
	public void setIsActive(String isActive) {
		mIsActive = isActive;
	}
	public String getConclusuon() {
		return mConclusuon;
	}
	public void setConclusuon(String conclusuon) {
		mConclusuon = conclusuon;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getQuorter() {
		return quorter;
	}
	public void setQuorter(int quorter) {
		this.quorter = quorter;
	}
	public int getMounth() {
		return mounth;
	}
	public void setMounth(int mounth) {
		this.mounth = mounth;
	}
	
	@Override
public String toString() {
	return "Survey [mSurveyID=" + mSurveyID + ", CustomerID=" + CustomerID + ", mStoreID=" + mStoreID + ", mAnswerDate="
			+ mAnswerDate + ", mA1=" + mA1 + ", mA2=" + mA2 + ", mA3=" + mA3 + ", mA4=" + mA4 + ", mA5=" + mA5
			+ ", mA6=" + mA6 + ", mIsActive=" + mIsActive + ", mConclusuon=" + mConclusuon + ", quorter=" + quorter
			+ ", mounth=" + mounth + ", year=" + year + "]";
}
}
