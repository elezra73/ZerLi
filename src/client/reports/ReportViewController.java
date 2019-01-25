package client.reports;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.reports.Report;
import common.reports.ReportComplaint;
import common.reports.ReportDetailInOrderReport;
import common.reports.ReportIncome;
import common.reports.ReportOrder;
import common.reports.ReportsStatisfaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class ReportViewController implements Initializable {
	
	@FXML
    private Label mLabelAvgQuestion4ForMonth1;
    @FXML
    private Label mLabelAvgQuestion1ForMonth2;
    @FXML
    private Label mLabelAvgQuestion1ForMonth1;
    @FXML
    private Label mLblQuartely;
    @FXML
    private Label mLabelAvgQuestion1ForMonth3;
    @FXML
    private Label mLabelAvgQuestion4ForMonth3;
    @FXML
    private Label mLabelAvgQuestion4ForMonth2;
    @FXML
    private Label mLabelQ1;
    @FXML
    private Label mLblStoreName;
    @FXML
    private Label mLabelAvgQuestion5ForMonth1;
    @FXML
    private Label mLabelAvgQuestion5ForMonth2;
    @FXML
    private Label mLblCodeReport;
    @FXML
    private Label mLabelQ6;
    @FXML
    private Label mLabelAvgQuestion5ForMonth3;
    @FXML
    private Label mLabelQ2;
    @FXML
    private Label mLabelAvgQuestion2ForMonth1;
    @FXML
    private Label mLabelQ3;
    @FXML
    private Label mLabelQ4;
    @FXML
    private Label mLabelAvgQuestion2ForMonth2;
    @FXML
    private Label mLabelQ5;
    @FXML
    private Label mLabelAvgQuestion2ForMonth3;
    @FXML
    private Label mLabelMonth3;
    @FXML
    private Label mLabelMonth2;
    @FXML
    private Label mLabelMonth1;
    @FXML
    private AnchorPane mGridPane;
    @FXML
    private Label mLebelQuestions;
    @FXML
    private Label mLebelAvgQuestion6ForMonth1;
    @FXML
    private Label mLabelAvgQuestion3ForMonth2;
    @FXML
    private Label mLabelAvgQuestion3ForMonth1;
    @FXML
    private Label mLblYear;
    @FXML
    private Label mLabelAvgQuestion3ForMonth3;
 
    @FXML
    private Label mLabelAvgQuestion6ForMonth2;
    @FXML
    private Label mLabelAvgQuestion6ForMonth3;
    @FXML
    private BarChart<String,Number > mBarChartStatisfaction;
    @FXML
    private BarChart<String,Number > mBarChartIncome;
    @FXML
    private BarChart<String,Number> mBarChartComplaint;
    @FXML
    private Label mLabelMonth1OfIncomeReport;
    @FXML
    private Label mLabelIncome1;
    @FXML
    private Label mLabelIncome3;
    @FXML
    private Label mLabelIncome2;
    @FXML
    private Label mLabelTotal;
    @FXML
    private Label mLabelMonth3OfIncomeReport;
    @FXML
    private Label mReportTitl;
    @FXML
    private Label mLabelMonth2OfIncomeReport;
    @FXML
    private GridPane mGridPaneIncome;
    @FXML
    private GridPane mGridPaneStatisfaction;
 
    @FXML
    private TableView<ReportDetailInOrderReport> mTableViewProductQuantity;
    @FXML
    private TableColumn<ReportDetailInOrderReport, String> mTableColumnProductName;
    @FXML
    private TableColumn<ReportDetailInOrderReport, String> mTableColumnQuantity;
    
    private ObservableList <ReportDetailInOrderReport>  ObservableDetailInOrderForOrderReportList ;
    private ArrayList<ReportDetailInOrderReport> detailInOrder=new ArrayList<ReportDetailInOrderReport>();
    
    @FXML
    private void backToReportSelector(ActionEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide();
    }
    
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    /**
     * this method get from report controller  and show him
     * @param report
     */
	public void setReport(Report report) {
		if(report==null) {
			return ;
		}
		this.mLblCodeReport.setText(report.getReportsID());
		this.mLblStoreName.setText(report.getStoreID());
		this.mLblQuartely.setText(report.getQuarter());
		this.mLblYear.setText(report.getYear());
		
		if(report instanceof ReportsStatisfaction) {
			mGridPaneStatisfaction.setVisible(true);
			mBarChartStatisfaction.setVisible(true);
			mGridPaneIncome.setVisible(false);
			mBarChartIncome.setVisible(false);
			mTableViewProductQuantity.setVisible(false);
			mReportTitl.setText("Statisfaction Report");
			ReportsStatisfaction reportsStatisfaction=(ReportsStatisfaction)report;
			setTableSatisfactionReport(reportsStatisfaction);
			setStatisfactionHistogramChart(reportsStatisfaction);
		}
		if(report instanceof ReportIncome) {
			mGridPaneStatisfaction.setVisible(false);
			mBarChartStatisfaction.setVisible(false);
			mGridPaneIncome.setVisible(true);
			mBarChartIncome.setVisible(true);
			mTableViewProductQuantity.setVisible(false);
			
			mReportTitl.setText("Income Report");
			ReportIncome reportIncome =(ReportIncome)report;
			this.mLabelMonth1OfIncomeReport.setText(reportIncome.getMonth1());
			this.mLabelMonth2OfIncomeReport.setText(reportIncome.getMonth2());
			this.mLabelMonth3OfIncomeReport.setText(reportIncome.getMonth3());
			this.mLabelIncome1.setText(reportIncome.getIncomeAmount1());
			this.mLabelIncome2.setText(reportIncome.getIncomeAmount2());
			this.mLabelIncome3.setText(reportIncome.getIncomeAmount3());
			double Total=Double.parseDouble(reportIncome.getIncomeAmount1())+Double.parseDouble(reportIncome.getIncomeAmount2())
					+Double.parseDouble(reportIncome.getIncomeAmount3());
			this.mLabelTotal.setText(Total+"");
			
			
			
			
			mBarChartStatisfaction.setVisible(false);
			
	        // mBarChartStatisfaction =new BarChart<String,Number>(xAxis,yAxis);
	         mBarChartIncome.setTitle(" Income Summary");
	         mBarChartIncome.getXAxis().setLabel("months");
	         mBarChartIncome.getYAxis().setLabel("income");
	         
	         XYChart.Series series1 = new XYChart.Series();
	         
	              
	         series1.getData().add(new XYChart.Data("Month "+reportIncome.getMonth1(),Double.parseDouble(reportIncome.getIncomeAmount1())));
	         
	           
	         XYChart.Series series2 = new XYChart.Series();
	         
	         series2.getData().add(new XYChart.Data("Month "+reportIncome.getMonth2(),Double.parseDouble(reportIncome.getIncomeAmount2())));
	        
	         
	         XYChart.Series series3 = new XYChart.Series();
	         
	         series3.getData().add(new XYChart.Data("Month "+reportIncome.getMonth3(),Double.parseDouble(reportIncome.getIncomeAmount3())));
	         
	         mBarChartIncome.getData().addAll(series1,series2,series3);
			
		}
		
		if(report instanceof ReportOrder) {
			mGridPaneStatisfaction.setVisible(false);
			mBarChartStatisfaction.setVisible(false);
			mGridPaneIncome.setVisible(false);
			mBarChartIncome.setVisible(false);
			mTableViewProductQuantity.setVisible(true);
			ReportOrder orderReport=(ReportOrder) report;
			//this.mTableViewProductQuantity.getSelectionModel().cellSelectionEnabledProperty(orderReport.getDetailsInReport());
			detailInOrder=orderReport.getProductAndQuantity();
			
			ObservableDetailInOrderForOrderReportList=FXCollections.observableArrayList();
			ObservableDetailInOrderForOrderReportList.addAll(detailInOrder);
			mReportTitl.setText("Order Report");
			
			this.mTableColumnProductName.setCellValueFactory(new PropertyValueFactory<ReportDetailInOrderReport,String>("TypeProduct"));
			this.mTableColumnQuantity.setCellValueFactory(new PropertyValueFactory<ReportDetailInOrderReport,String>("Quantity"));
			this.mTableViewProductQuantity.setItems(ObservableDetailInOrderForOrderReportList);
		}
		
		if(report instanceof ReportComplaint) {
			mTableViewProductQuantity.setVisible(false);
			mBarChartComplaint.setVisible(true);
			mGridPaneStatisfaction.setVisible(false);
			mBarChartStatisfaction.setVisible(false);
			mGridPaneIncome.setVisible(false);
			mBarChartIncome.setVisible(false);
			ReportComplaint reportComplaint=(ReportComplaint)report;
			
			mBarChartComplaint.setTitle("Complaint Summary");
            
			mBarChartComplaint.getXAxis().setLabel("quarter");
			mBarChartComplaint.getYAxis().setLabel("quantity");
			mReportTitl.setText("Complaint Report");
            
            
            ArrayList<XYChart.Series> series=new ArrayList<XYChart.Series>();
            
          
            	 XYChart.Series series1 = new XYChart.Series();
            	 XYChart.Series series2 = new XYChart.Series();
            	 XYChart.Series series3 = new XYChart.Series();
            	 
            		 series1.setName(reportComplaint.getMonth1());
            		 series1.getData().add(new XYChart.Data(reportComplaint.getMonth1(),Integer.parseInt( reportComplaint.getQuantityComplaint1())));
            		 
            		
            		 series2.setName(reportComplaint.getMonth2());
            		 series2.getData().add(new XYChart.Data(reportComplaint.getMonth2(), Integer.parseInt(reportComplaint.getQuantityComplaint2())));
            		 series.add(series2);
            		
            		 series3.setName(reportComplaint.getMonth3());
            		 series3.getData().add(new XYChart.Data(reportComplaint.getMonth3(),Integer.parseInt( reportComplaint.getQuantityComplaint3())));
     			
            	 
            	 mBarChartComplaint.getData().addAll(series1,series2,series3);
               
			
		}
		
		
	}
/*
 * 
 * 
 */
/**
 * show Satisfaction in grid
 * @param reportsStatisfaction report satisfaction
 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setStatisfactionHistogramChart(ReportsStatisfaction reportsStatisfaction) {
		CategoryAxis xAxis = new CategoryAxis();
         NumberAxis yAxis = new NumberAxis();
        // mBarChartStatisfaction =new BarChart<String,Number>(xAxis,yAxis);
         mBarChartStatisfaction.setTitle(" Statisfaction Summary");
         xAxis.setLabel("months");       
         yAxis.setLabel("Quantity");
         XYChart.Series series1 = new XYChart.Series();
         
         series1.setName("Q1");       
         series1.getData().add(new XYChart.Data("Month "+reportsStatisfaction.getMonth1(),Double.parseDouble(reportsStatisfaction.getAvgQuestion1ForMonth1())));
         series1.getData().add(new XYChart.Data("Month "+reportsStatisfaction.getMonth2(),Double.parseDouble(reportsStatisfaction.getAvgQuestion1ForMonth2())));
         series1.getData().add(new XYChart.Data("Month "+reportsStatisfaction.getMonth3(),Double.parseDouble(reportsStatisfaction.getAvgQuestion1ForMonth3())));
           
         XYChart.Series series2 = new XYChart.Series();
         series2.setName("Q2");
         series2.getData().add(new XYChart.Data("Month "+reportsStatisfaction.getMonth1(),Double.parseDouble(reportsStatisfaction.getAvgQuestion2ForMonth1())));
         series2.getData().add(new XYChart.Data("Month "+reportsStatisfaction.getMonth2(),Double.parseDouble(reportsStatisfaction.getAvgQuestion2ForMonth2())));
         series2.getData().add(new XYChart.Data("Month "+reportsStatisfaction.getMonth3(),Double.parseDouble(reportsStatisfaction.getAvgQuestion2ForMonth3())));
         
         XYChart.Series series3 = new XYChart.Series();
         series3.setName("Q3");
         series3.getData().add(new XYChart.Data("Month "+reportsStatisfaction.getMonth1(),Double.parseDouble(reportsStatisfaction.getAvgQuestion3ForMonth1())));
         series3.getData().add(new XYChart.Data("Month "+reportsStatisfaction.getMonth2(), Double.parseDouble(reportsStatisfaction.getAvgQuestion3ForMonth2())));
         series3.getData().add(new XYChart.Data("Month "+reportsStatisfaction.getMonth3(), Double.parseDouble(reportsStatisfaction.getAvgQuestion3ForMonth3())));
         
         XYChart.Series series4 = new XYChart.Series();
         series4.setName("Q4");
         series4.getData().add(new XYChart.Data("Month "+reportsStatisfaction.getMonth1(), Double.parseDouble(reportsStatisfaction.getAvgQuestion4ForMonth1())));
         series4.getData().add(new XYChart.Data("Month "+reportsStatisfaction.getMonth2(), Double.parseDouble(reportsStatisfaction.getAvgQuestion4ForMonth2())));
         series4.getData().add(new XYChart.Data("Month "+reportsStatisfaction.getMonth3(), Double.parseDouble(reportsStatisfaction.getAvgQuestion4ForMonth3())));
         
         XYChart.Series series5 = new XYChart.Series();
         series5.setName("Q5");
         series5.getData().add(new XYChart.Data("Month "+reportsStatisfaction.getMonth1(), Double.parseDouble(reportsStatisfaction.getAvgQuestion5ForMonth1())));
         series5.getData().add(new XYChart.Data("Month "+reportsStatisfaction.getMonth2(), Double.parseDouble(reportsStatisfaction.getAvgQuestion5ForMonth2())));
         series5.getData().add(new XYChart.Data("Month "+reportsStatisfaction.getMonth3(), Double.parseDouble(reportsStatisfaction.getAvgQuestion5ForMonth3())));
         
         XYChart.Series series6 = new XYChart.Series();
         series6.setName("Q6");
         series6.getData().add(new XYChart.Data("Month "+reportsStatisfaction.getMonth1(),Double.parseDouble(reportsStatisfaction.getAvgQuestion6ForMonth1())));
         series6.getData().add(new XYChart.Data("Month "+reportsStatisfaction.getMonth2(), Double.parseDouble(reportsStatisfaction.getAvgQuestion6ForMonth2())));
         series6.getData().add(new XYChart.Data("Month "+reportsStatisfaction.getMonth3(), Double.parseDouble(reportsStatisfaction.getAvgQuestion6ForMonth3())));
         mBarChartStatisfaction.getData().addAll(series1,series2,series3,series4,series5,series6);
	}
/**
 * show in table data of satisfaction report
 * @param reportsStatisfaction report satisfaction
 */
	private void setTableSatisfactionReport(ReportsStatisfaction reportsStatisfaction) {
		this.mLabelQ1.setText("Stores are conveniently located.");
		this.mLabelQ2.setText("Store hours are appropriate for my shopping needs.");
		this.mLabelQ3.setText("Store atmosphere and decor are appealing.");
		this.mLabelQ4.setText("A good selection of products present.");
		this.mLabelQ5.setText("Merchandise sold is of the highest quality.");
		this.mLabelQ6.setText("I am very satisfied with the price I paid for what I bought.");
		
		
		this.mLabelAvgQuestion1ForMonth1.setText(reportsStatisfaction.getAvgQuestion1ForMonth1());
		this.mLabelAvgQuestion1ForMonth2.setText(reportsStatisfaction.getAvgQuestion1ForMonth2());
		this.mLabelAvgQuestion1ForMonth3.setText(reportsStatisfaction.getAvgQuestion1ForMonth3());
		
		this.mLabelAvgQuestion2ForMonth1.setText(reportsStatisfaction.getAvgQuestion2ForMonth1());
		this.mLabelAvgQuestion2ForMonth2.setText(reportsStatisfaction.getAvgQuestion2ForMonth2());
		this.mLabelAvgQuestion2ForMonth3.setText(reportsStatisfaction.getAvgQuestion2ForMonth3());
		
		this.mLabelAvgQuestion3ForMonth1.setText(reportsStatisfaction.getAvgQuestion3ForMonth1());
		this.mLabelAvgQuestion3ForMonth2.setText(reportsStatisfaction.getAvgQuestion3ForMonth2());
		this.mLabelAvgQuestion3ForMonth3.setText(reportsStatisfaction.getAvgQuestion3ForMonth3());
		
		this.mLabelAvgQuestion4ForMonth1.setText(reportsStatisfaction.getAvgQuestion4ForMonth1());
		this.mLabelAvgQuestion4ForMonth2.setText(reportsStatisfaction.getAvgQuestion4ForMonth2());
		this.mLabelAvgQuestion4ForMonth3.setText(reportsStatisfaction.getAvgQuestion4ForMonth3());
		
		this.mLabelAvgQuestion5ForMonth1.setText(reportsStatisfaction.getAvgQuestion5ForMonth1());
		this.mLabelAvgQuestion5ForMonth2.setText(reportsStatisfaction.getAvgQuestion5ForMonth2());
		this.mLabelAvgQuestion5ForMonth3.setText(reportsStatisfaction.getAvgQuestion5ForMonth3());
		
		this.mLebelAvgQuestion6ForMonth1.setText(reportsStatisfaction.getAvgQuestion6ForMonth1());
		this.mLabelAvgQuestion6ForMonth2.setText(reportsStatisfaction.getAvgQuestion6ForMonth2());
		this.mLabelAvgQuestion6ForMonth3.setText(reportsStatisfaction.getAvgQuestion6ForMonth3());
		this.mLabelMonth1.setText("Month "+ reportsStatisfaction.getMonth1());
		this.mLabelMonth2.setText("Month "+ reportsStatisfaction.getMonth2());
		this.mLabelMonth3.setText("Month "+ reportsStatisfaction.getMonth3());
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
	
		mBarChartComplaint.setVisible(false);
		
		
	}

}
