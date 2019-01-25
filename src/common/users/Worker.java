package common.users;

import java.util.LinkedHashMap;

/**
 * Worker class
 *
 */
public class Worker extends AUser{
	private String mWorkerType;
	private static final long serialVersionUID = -5211227756353356015L;

	public Worker(String userID, String name, String storeID, String workerType) {
		super(userID, name, storeID);
		mWorkerType = workerType.replace(" ", "");
	}

	/**
	 * work info represented by linked hash map
	 */
	@Override
	public LinkedHashMap<String, String> getInfo() {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put(mWorkerType+" ID:",mUserID);
		map.put("Name:",mName);
		if (mStoreID != null && mStoreID != "")
			map.put("StoreID:", mStoreID);
		return map;
	}


	/**
	 * @return the workerType
	 */
	public String getWorkerType() {
		return mWorkerType;
	}


	/**
	 * to string
	 */
	@Override
	public String toString() {
		return "mUserID=" + mUserID + ", mName="
				+ mName + ", Worker [mWorkerType=" + mWorkerType + ", mStoreID=" + mStoreID  + "]";
	}



}
