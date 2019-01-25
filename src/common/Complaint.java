 package common;


import java.io.Serializable;
import java.text.ParseException;

public class Complaint implements Serializable{

		private static final long serialVersionUID = -1133128363482085515L;
		private String mComplaintID;
		private String mCustomerID;
		private String mWorkerID;
		private String mStoreID;
		private ZerliDate mOpenDate;
		private ZerliDate mExpirDate;
		private String mDescription;
		private String mIsActive;
	
	
		private int quorter;
		private int mounth;
		private int year;
	
		
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
		public int getYear() {
			return year;
		}
		public void setYear(int year) {
			this.year = year;
		}

		
		
	
		public Complaint(String ComplaintID, String CustomerID, String WorkerID, String ComplaintStoreID, String openDate, String expirationDate,String ComplaintDescription) throws ParseException {
			mCustomerID = CustomerID;
			mComplaintID = ComplaintID;
			mWorkerID = WorkerID;
			mStoreID = ComplaintStoreID;
			mOpenDate=new ZerliDate(openDate);
			mExpirDate=new ZerliDate(expirationDate);
			mDescription = ComplaintDescription;
			mIsActive = "1";
		}
		//eden constructor
		public Complaint(String ComplaintID, String CustomerID, String WorkerID, String ComplaintStoreID, String ComplaintDate,String mExpirtionDate, String ComplaintDescription,String IsActive) {
			mCustomerID = CustomerID;
			mComplaintID = ComplaintID;
			mWorkerID = WorkerID;
			mStoreID = ComplaintStoreID;
			mDescription = ComplaintDescription;
			mIsActive =IsActive ;
			try {
				mOpenDate=new ZerliDate(ComplaintDate);
				mExpirDate = new ZerliDate(mExpirtionDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	


	
		public String getComplaintID() {
			return mComplaintID;
		}
		public void setComplaintID(String complaintID) {
			mComplaintID = complaintID;
		}
		public String getCustomerID() {
			return mCustomerID;
		}
		public void setCustomerID(String customerID) {
			mCustomerID = customerID;
		}
		public String getWorkerID() {
			return mWorkerID;
		}
		public void setWorkerID(String workerID) {
			mWorkerID = workerID;
		}
		public String getStoreID() {
			return mStoreID;
		}
		public void setStoreID(String storeID) {
			mStoreID = storeID;
		}
		public ZerliDate getOpenDate() {
			return mOpenDate;
		}
		public void setOpenDate(ZerliDate openDate) {
			mOpenDate = openDate;
		}
		public ZerliDate getExpirDate() {
			return mExpirDate;
		}
		public void setExpirDate(ZerliDate expirDate) {
			mExpirDate = expirDate;
		}
		public String getDescription() {
			return mDescription;
		}
		public void setDescription(String description) {
			mDescription = description;
		}
		public String getIsActive() {
			return mIsActive;
		}
		public void setIsActive(String isActive) {
			mIsActive = isActive;
		}
		@Override
		public String toString() {
			return mComplaintID+": "+mCustomerID+", "+mWorkerID+" , "+mOpenDate.toString()+", "+mExpirDate.toString()+" , "+mStoreID+" ,"+mDescription+".";
		}


	}
