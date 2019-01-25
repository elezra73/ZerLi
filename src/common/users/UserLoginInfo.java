package common.users;

import java.io.Serializable;

/**
 * Class to hold user login information.
 *
 */
public class UserLoginInfo implements Serializable {

	private static final long serialVersionUID = -595670036148975568L;
	private String mUserID;			// Username
	private String mPassword; 		// password 
	private String mAccountType;	// account type
	private boolean mIsActive;		// is account active?
	
	public UserLoginInfo(String userID, String password, String accountType,
			boolean isActive) {
		mUserID = userID;
		mPassword = password;
		mAccountType = accountType;
		mIsActive = isActive;
	}

	/**
	 * @return the userID
	 */
	public String getUserID() {
		return mUserID;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return mPassword;
	}

	/**
	 * @return the accountType
	 */
	public String getAccountType() {
		return mAccountType;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return mIsActive;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserLoginInfo [mUserID=" + mUserID + ", mPassword=" + mPassword + ", mAccountType=" + mAccountType
		        + ", mIsActive=" + mIsActive + "]";
	}
}
