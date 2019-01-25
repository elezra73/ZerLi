package common;

import java.io.Serializable;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import common.users.Customer;

public class CustomerOrder implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -358485044656858603L;
	private Customer mCustomer;
	private Map<Product, Integer> mOrderedProducts;
	private HashMap<String,Integer> mCart;
	private String mShipmentMethod;
	private String mShipmentAddress;
	private String mRecieverName;
	private String mFinalAmount;
	private ZerliDate mOrderDate;
	private String mBless;
	private String mOrderID;
	private String mCustomerID;
	private String mStoreID;
	private Double mOrderPrice;
	private ZerliDate mDeliveryDate;
	private String mPhone;
	private String mIsActive;
	private int mQuorter;
	private int mMounth;
	private int mYear;
	


	public CustomerOrder(Customer customer, Map<Product, Integer> orderedProducts, String finalAmout,String bless,ZerliDate deliveryDate,String address,String name,String phone) {
		super();
		this.mCustomer = customer;
		this.mOrderedProducts = orderedProducts;
		this.mFinalAmount = finalAmout;
		this.mOrderDate=  new ZerliDate();
		this.mBless= new String(bless);
		this.mDeliveryDate=deliveryDate;
		this.mStoreID=customer.getStoreID();
		this.mShipmentAddress=address;
		this.mRecieverName=name;
		this.setPhone(phone);
		
	}

	public CustomerOrder(Customer customer, Map<Product, Integer> orderedProducts, String shipmentMethod,
			String shipmentAddress, String recieverName, String finalAmout) {
		super();
		this.mCustomer = customer;
		this.mOrderedProducts = orderedProducts;
		this.mShipmentMethod = shipmentMethod;
		this.mShipmentAddress = shipmentAddress;
		this.mRecieverName = recieverName;
		this.mFinalAmount = finalAmout;
	}

	/// this constructor used by order cancellation query.
	public CustomerOrder(String orderID, String customerID, String storeID,
			ZerliDate orderDate, String shipmentMethod, String shipmentAddress,  String recieverName, 
			Double price, String blessing, ZerliDate deliveryDate) {
		
		mOrderID = orderID;
		mCustomerID = customerID;
		mStoreID = storeID;
		mOrderDate = orderDate;
		mShipmentMethod = shipmentMethod;
		mShipmentAddress = shipmentAddress;
		mRecieverName = recieverName;
		mOrderPrice = price;
		mBless = blessing;
		mDeliveryDate = deliveryDate;
	}
	
	/// this constructor used by order cancellation query.
	public CustomerOrder(String orderID, String customerID, String storeID,
			ZerliDate orderDate, String shipmentMethod, String shipmentAddress,  String recieverName, 
			Double price, String blessing, ZerliDate deliveryDate,String IsActive) {
		
		mOrderID = orderID;
		mCustomerID = customerID;
		mStoreID = storeID;
		mOrderDate = orderDate;
		mShipmentMethod = shipmentMethod;
		mShipmentAddress = shipmentAddress;
		mRecieverName = recieverName;
		mOrderPrice = price;
		mBless = blessing;
		mDeliveryDate = deliveryDate;
		mIsActive=IsActive;
	}
	/// this constructor used by creation report test.
	public CustomerOrder(String orderID,String customerID,String storeID,String orderDate,String shipmentMethod,
			String shipmentAddress,  String recieverName,String finalAmount,String blessing, String deliveryDate,String IsActive) {
		try {
			mOrderID = orderID;
			mCustomerID = customerID;
			mStoreID = storeID;
			mOrderDate =new ZerliDate( orderDate);
			mShipmentMethod = shipmentMethod;
			mShipmentAddress = shipmentAddress;
			mRecieverName = recieverName;
			mFinalAmount =finalAmount ;
			mBless = blessing;
			mDeliveryDate =new ZerliDate( deliveryDate);
			mIsActive=IsActive;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} ;
		
		
	}

	public CustomerOrder() {
		// TODO Auto-generated constructor stub
	}

	public Customer getCustomer() {
		return mCustomer;
	}

	
	/**
	 * 
	 * @return the delivery date
	 */
	public ZerliDate getDeliveryDate() {
		return mDeliveryDate;
	}
	
	/**
	 * @return the orderPrice
	 */
	public Double getOrderPrice() {
		return mOrderPrice;
	}
	
	/**
	 * @return the storeID
	 */
	public String getStoreID() {
		return mStoreID;
	}
	
	/**
	 * @return the orderID
	 */
	public String getOrderID() {
		return mOrderID;
	}

	/**
	 * @return the customerID
	 */
	public String getCustomerID() {
		return mCustomerID;
	}
	
	public void setCustomer(Customer customer) {
		this.mCustomer = customer;
	}

	public Map<Product, Integer>  getcatalogProducts() {
		return mOrderedProducts;
	}

	public void setcatalogProducts(Map<Product, Integer>  orderedProducts) {
		this.mOrderedProducts = orderedProducts;
	}

	public String getShipmentMethod() {
		return mShipmentMethod;
	}

	public void setShipmentMethod(String shipmentMethod) {
		this.mShipmentMethod = shipmentMethod;
	}

	public String getShipmentAddress() {
		return mShipmentAddress;
	}

	public void setShipmentAddress(String shipmentAddress) {
		this.mShipmentAddress = shipmentAddress;
	}

	public String getRecieverName() {
		return mRecieverName;
	}

	public void setRecieverName(String recieverName) {
		this.mRecieverName = recieverName;
	}

	public String getFinalAmout() {
		return mFinalAmount;
	}

	public void setFinalAmout(String finalAmout) {
		this.mFinalAmount = finalAmout;
	}


	public HashMap<String,Integer> getCart() {
		return mCart;
	}


	public void setCart(HashMap<String,Integer> cart) {
		this.mCart = cart;
	}


	public ZerliDate getOrderDate() {
		return mOrderDate;
	}


	public void setoDate(ZerliDate oDate) {
		this.mOrderDate = oDate;
	}

	public Map<Product, Integer> getOrderedProducts() {
		return mOrderedProducts;
	}

	public String getBless() {
		return mBless;
	}

	public void setBless(String bless) {
		mBless = bless;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return mOrderID;			// needed for combo box
	}

	public String getPhone() {
		return mPhone;
	}

	public void setPhone(String phone) {
		mPhone = phone;
	}
	public int getQuorter() {
		return mQuorter;
	}

	public void setQuorter(int quorter) {
		mQuorter = quorter;
	}

	public int getMounth() {
		return mMounth;
	}

	public void setMounth(int mounth) {
		mMounth = mounth;
	}

	public int getYear() {
		return mYear;
	}

	public void setYear(int year) {
		mYear = year;
	}

	public String getFinalAmount() {
		return mFinalAmount;
	}

	public void setFinalAmount(String finalAmount) {
		mFinalAmount = finalAmount;
	}

	public String getIsActive() {
		return mIsActive;
	}

	public void setIsActive(String isActive) {
		mIsActive = isActive;
	}

	public void setOrderedProducts(Map<Product, Integer> orderedProducts) {
		mOrderedProducts = orderedProducts;
	}

	public void setOrderID(String orderID) {
		mOrderID = orderID;
	}

	public void setStoreID(String storeID) {
		mStoreID = storeID;
	}
	
	

}
