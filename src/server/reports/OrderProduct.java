package server.reports;

import java.io.Serializable;

public class OrderProduct implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1115566217091601062L;
	private String mOrderID;
	private String mProducID;
	private int mQuantity;
	private String mStoreID;
	
	/*
	 * Constructor
	 */
	public OrderProduct(String orderID, String producID, String quantity, String storeID) {
		mOrderID = orderID;
		mProducID = producID;
		mQuantity =Integer.parseInt(quantity) ;
		mStoreID = storeID;
	}
	public String getStoreID() {
		return mStoreID;
	}
	public void setStoreID(String storeID) {
		mStoreID = storeID;
	}
	public String getOrderID() {
		return mOrderID;
	}
	public void setOrderID(String orderID) {
		mOrderID = orderID;
	}
	public String getProducID() {
		return mProducID;
	}
	public void setProducID(String producID) {
		mProducID = producID;
	}
	public int getQuantity() {
		return mQuantity;
	}
	public void setQuantity(int quantity) {
		mQuantity = quantity;
	}
	
	
	
	

}
