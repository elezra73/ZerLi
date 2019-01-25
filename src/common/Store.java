package common;

import java.io.Serializable;

public class Store implements Serializable{

	private static final long serialVersionUID = 544935150166078474L;
	private String mStoreID;
	private String mStoreCity;
	/**
	 * @param storeID get the storeID
	 * @param storeCity get the StoreCity
	 */
	public Store(String storeID, String storeCity) {
		mStoreID = storeID;
		mStoreCity = storeCity;
	}
	/**
	 * @return the storeID
	 */
	public String getStoreID() {
		return mStoreID;
	}

	/**
	 * @return the storeCity
	 */
	public String getStoreCity() {
		return mStoreCity;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return mStoreID;
	}
	

}
