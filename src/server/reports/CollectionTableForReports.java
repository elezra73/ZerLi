package server.reports;

import java.io.Serializable;
import java.util.ArrayList;

import common.Complaint;
import common.CustomerOrder;
import common.Product;
import common.Store;
import common.Survey;

public class CollectionTableForReports implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8169309637834475404L;
	private ArrayList<Store> mStors=new   ArrayList<Store>();
	private ArrayList<CustomerOrder> mOrders=new ArrayList<CustomerOrder>();
	private ArrayList<OrderProduct> mOrderProducts=new ArrayList<OrderProduct>();
	private ArrayList<Product> mProducts=new ArrayList<Product>();
	private ArrayList<Complaint> mComplaint=new ArrayList<Complaint>();
	private ArrayList<Survey> mSurvey=new ArrayList<Survey>();
	
	public CollectionTableForReports() {
		
	}
	
	public CollectionTableForReports(ArrayList<Store> stors, ArrayList<CustomerOrder> orders,
			ArrayList<OrderProduct> orderProducts, ArrayList<Product> products, ArrayList<Complaint> complaint,
			ArrayList<Survey> survey) {
		super();
		mStors = stors;
		mOrders = orders;
		mOrderProducts = orderProducts;
		mProducts = products;
		mComplaint = complaint;
		mSurvey = survey;
	}
	public ArrayList<Store> getStors() {
		return mStors;
	}
	public void setStors(ArrayList<Store> stors) {
		mStors = stors;
	}
	public ArrayList<OrderProduct> getOrderProducts() {
		return mOrderProducts;
	}
	public void setOrderProducts(ArrayList<OrderProduct> orderProducts) {
		mOrderProducts = orderProducts;
	}
	public ArrayList<CustomerOrder> getOrders() {
		return mOrders;
	}
	public void setOrders(ArrayList<CustomerOrder> orders) {
		mOrders = orders;
	}
	public ArrayList<Product> getProducts() {
		return mProducts;
	}
	public void setProducts(ArrayList<Product> products) {
		mProducts = products;
	}
	public ArrayList<Complaint> getComplaint() {
		return mComplaint;
	}
	public void setComplaint(ArrayList<Complaint> complaint) {
		mComplaint = complaint;
	}
	public ArrayList<Survey> getSurvey() {
		return mSurvey;
	}
	public void setSurvey(ArrayList<Survey> survey) {
		mSurvey = survey;
	}
	
	
	

}
