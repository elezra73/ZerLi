package common;

import java.io.Serializable;
import java.util.ArrayList;

import common.EQueryOption;

/**
 * Message to be sent from client to server.
 * @author Roma
 */
public class ClientToServerMessage implements Serializable {
	
	private static final long serialVersionUID = 3645831604535073047L;
	private EQueryOption mQueryOption;
	/**
	 * @param queryOption the queryOption to set
	 */
	public void setQueryOption(EQueryOption queryOption) {
		mQueryOption = queryOption;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(ArrayList<Object> params) {
		mParams = params;
	}

	/**
	 * @param objectType the objectType to set
	 */
	public void setObjectType(String objectType) {
		mObjectType = objectType;
	}


	private ArrayList<Object> mParams; 
	private String mObjectType;
	
	public ClientToServerMessage(EQueryOption queryOption, ArrayList<Object> params,
			String objectType) {
		mQueryOption = queryOption;
		mParams = params;
		mObjectType = objectType;
	}

	/**
	 * @return the mQueryOption
	 */
	public EQueryOption getQueryOption() {
		return mQueryOption;
	}

	/**
	 * @return the mParams
	 */
	public ArrayList<Object> getParams() {
		return mParams;
	}

	/**
	 * @return the mObjectToReturn
	 */
	public String getObjectType() {
		return mObjectType;
	}


	@Override
	public String toString() {
		return "ClientToServerMessage [mQueryOption=" + mQueryOption + ", mParams=" + mParams + ", mObjectType="
		        + mObjectType + "]";
	}
	
	
	
}
