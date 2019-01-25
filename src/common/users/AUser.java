package common.users;

import java.io.Serializable;
import common.IZerliEntity;

/**
 * Abstract class of user.
 *
 */
public abstract class AUser implements Serializable, IZerliEntity {

	private static final long serialVersionUID = 4144780034554852615L;
	protected String mUserID;
	protected String mName;		// not a username but a user's name!
	protected String mStoreID;	// can be null
	
	/**
	 * @return the userID
	 */
	public String getUserID() {
		return mUserID;
	}
	/**
	 * @return the storeID
	 */
	public String getStoreID() {
		return mStoreID;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return mName;
	}
	
	/**
	 * Constructor
	 * @param userID user id
	 * @param name user name
	 * @param storeID store id
	 */
	public AUser(String userID, String name, String storeID) {
		mUserID = userID.trim();
		mName = name.trim();
		if (storeID != null)
			mStoreID = storeID.trim();
		else mStoreID = storeID;
	}
	
}
