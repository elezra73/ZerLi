package common.reports;

import java.io.Serializable;

public class ReportDetailInOrderReport implements Serializable {
	
	private static final long serialVersionUID = 3425078435245269746L;
	private	String mDetailID;
	private String mTypeProduct;
	private String mQuantity;
	private String mReportOrderID;
	/**
	 * Empty constructor
	 */
	public ReportDetailInOrderReport() {
	
	}
	/**
	 * Constructor
	 * @param detailID is identify of row
	 * @param typeProduct product type
	 * @param quantity quantity
	 * @param reportOrderID is foreign key in reportOrder class
	 */
	public ReportDetailInOrderReport(String detailID, String typeProduct, String quantity, String reportOrderID)
	{
		
		mDetailID = detailID;
		mTypeProduct = typeProduct;
		mQuantity = quantity;
		mReportOrderID = reportOrderID;
		
	}

	public String getDetailID() {
		return mDetailID;
	}
	public void setDetailID(String detailID) {
		mDetailID = detailID;
	}
	public String getTypeProduct() {
		return mTypeProduct;
	}
	public void setTypeProduct(String typeProduct) {
		mTypeProduct = typeProduct;
	}
	public String getQuantity() {
		return mQuantity;
	}
	public void setQuantity(String quantity) {
		mQuantity = quantity;
	}
	public String getReportOrderID() {
		return mReportOrderID;
	}
	public void setReportOrderID(String reportOrderID) {
		mReportOrderID = reportOrderID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "ReportDetailInOrderReport [mDetailID=" + mDetailID + ", mTypeProduct=" + mTypeProduct + ", mQuantity="
				+ mQuantity + ", mReportOrderID=" + mReportOrderID + "]\n";
	}

	

}