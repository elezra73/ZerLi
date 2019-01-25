package common.users;

import java.util.LinkedHashMap;

import common.ZerliDate;

/**
 * Customer class
 *
 */
public class Customer extends AUser {
	private static final long serialVersionUID = 6813468080503280728L;
	private boolean mIsPayable;		// not null
	private String mAddress;		// can be null
	private String mPaymentMethod;	// can be null
	private String mCreditCard;		// can be null
	private double mAccountBalance;	// not null
	private String mSocialNumber;	// can be null
	private ZerliDate mOpenAccountDate;	// not null

	/**
	 * @param userID user id
	 * @param name customer's  name
	 * @param storeID customer's store id  
	 * @param isPayable	is customer's account payable
	 * @param address customer's  address
	 * @param paymentMethod customer's payment method
	 * @param creditCard customer's  credit card
	 * @param accountBalance customer's  account balance
	 * @param socialNumber customer's Teudat Zehut
	 * @param openDate dte of open
	 */
	public Customer(String userID, String name, String storeID, boolean isPayable, String address, String paymentMethod,
			double accountBalance ,String creditCard, String socialNumber, ZerliDate openDate) {
		super(userID, name, storeID);
		mIsPayable = isPayable;
		mAddress = address.trim();
		mPaymentMethod = paymentMethod.replace(" ", "");
		mCreditCard = creditCard.replace(" ", "").replace("-", "");
		mAccountBalance = accountBalance;
		mSocialNumber = socialNumber.replace(" ", "");
		mOpenAccountDate = openDate;
	}

	/**
	 * For general catalog browsing
	 * @param userID user id
	 * @param storeID store id
	 */
	public Customer(String userID, String storeID) {
		super(userID, "", storeID);
		mIsPayable = false;
	}
	public void setSelectedStoreID(String storeID) {
		mStoreID = storeID;
	}
	
	/**
	 * @return the socialNumber
	 */
	public String getSocialNumber() {
		return mSocialNumber;
	}
	
	/**
	 * @return the account's open date
	 */
	public ZerliDate getAccountOpenDate() {
		return mOpenAccountDate;
	}
	
	/**
	 * @return the isPayable
	 */
	public boolean isPayable() {
		return mIsPayable;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return mAddress;
	}
	/**
	 * @return the paymentMethod
	 */
	public String getPaymentMethod() {
		return mPaymentMethod;
	}

	/**
	 * @return the creditCard
	 */
	public String getCreditCard() {
		return mCreditCard;
	}
	/**
	 * @return the AccountBalance
	 */
	public Double getAccountBalance() {
		return mAccountBalance;
	}
	

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Customer [mIsPayable=" + mIsPayable + ", mAddress=" + mAddress + ", mPaymentMethod=" + mPaymentMethod
				+ ", mCreditCard=" + mCreditCard + ", mAccountBalance=" + mAccountBalance + ", mSocialNumber="
				+ mSocialNumber + ", mUserID=" + mUserID + ", mName=" + mName + ", mStoreID=" + mStoreID + "]";
	}

	/**
	 * return customer's info represented in linked hash map
	 */
	@Override
	public LinkedHashMap<String, String> getInfo() {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("CustomerID:",mUserID);
		map.put("Name:",mName);
		map.put("Social Number:",mSocialNumber);
		map.put("StoreID:", mStoreID);
		if (mIsPayable)
			map.put("Payable Account. Activated on: ", mOpenAccountDate.getFullDate());
		map.put("Address:" , mAddress);
		map.put("Payment method:", mPaymentMethod);
		map.put("Credit card number:",mCreditCard);
		map.put("Balance:", ""+mAccountBalance);
		return map;
	}
	
}
