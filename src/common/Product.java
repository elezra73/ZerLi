// Product

package common;

import java.io.Serializable;
import java.util.LinkedHashMap;


public class Product implements Serializable, IZerliEntity {
	
	private static final long serialVersionUID = -1133128363482085515L;
	private String mProductID;
	private String mProductName;
	private String mProductType;
	private Double mProductPrice;
	private String mImageName;
	private String mDominantColor;
	private SendableFile picture;	

	public Product(String productID, String productName, String productType , Double productPrice , String productImage , String dominantcolor) 
	{
		mProductID = productID;
		mProductName = productName;
		mProductType = productType;
		mProductPrice = productPrice;
		mImageName= productImage;
		mDominantColor = dominantcolor;

	}
	
	public Product() {}

	/**
	 * @param productID the productID to set
	 */
	public void setProductID(String productID) {
		mProductID = productID;
	}

	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		mProductName = productName;
	}

	/**
	 * @param imageName the imageName to set
	 */
	public void setImageName(String imageName) {
		mImageName = imageName;
	}

	/**
	 * @param dominantColor the dominantColor to set
	 */
	public void setDominantColor(String dominantColor) {
		mDominantColor = dominantColor;
	}

	@Override
	public String toString() {
		return mProductID+": "+mProductName+", "+mProductType+".";
	}
	public String printToComboBox()
	{
		return "ID:"+this.mProductID+","+this.mProductName+","+mProductType+","+mProductPrice;
	}
	
	public boolean equalByID(String id)
	{
		return this.mProductID.contentEquals(id);
	}


	/**
	 * @return the picture
	 */
	public SendableFile getPicture() {
		return picture;
	}


	/**
	 * @param picture the picture to set
	 */
	public void setPicture(SendableFile picture) {
		this.picture = picture;
	}


	/**
	 * @return the productID
	 */
	public String getProductID() {
		return mProductID;
	}


	/**
	 * @return the productName
	 */
	public String getProductName() {
		return mProductName;
	}


	/**
	 * @return the productType
	 */
	public String getProductType() {
		return mProductType;
	}

	/**
	 * @param type the type to set
	 */
	public void setProductType(String type) {
		mProductType = type;
	}

	/**
	 * @param price the price to set
	 */
	public void setProductPrice(Double price) {
		mProductPrice = price;
	}

	/**
	 * @return the productPrice
	 */
	public Double getProductPrice() {
		return mProductPrice;
	}

	/**
	 * @return the imageName
	 */
	public String getImageName() {
		return mImageName;
	}


	/**
	 * @return the dominantColor
	 */
	public String getDominantColor() {
		return mDominantColor;
	}
	
	/**
	 * a way to represent data about the product
	 * @return hashmap of the product
	 */
	public LinkedHashMap<String, String> getInfo() {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("\nProduct ID: \t\t\t", mProductID);
		map.put("\nProduct Name: \t\t", mProductName);
		map.put("\nProduct Type: \t\t\t", mProductType);
		map.put("\nProduct basic price \t\t", mProductPrice.toString());
		map.put("\nDominant color: \t\t", mDominantColor);
		if (mImageName != null) 
			map.put("\nProduct image name: \t", mImageName);
		return map;
	}

}
