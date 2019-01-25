package client.reports;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import client.ConnectionController;
import client.IController;
import client.WindowFactory;
import common.ClientToServerMessage;
import common.EQueryOption;
import common.IClient;
import common.Store;
import common.reports.Report;
import common.reports.ReportComplaint;
import common.reports.ReportDetailInOrderReport;
import common.reports.ReportIncome;
import common.reports.ReportOrder;
import common.reports.ReportsStatisfaction;
import common.users.Worker;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class ReportsController  implements Initializable, IClient, IController {
	private static final String INCOME_REPORT = "Income Report";
	private static final String COMPLAINTS_REPORT = "Complaints Report";
	private static final String ORDERS_REPORT = "Orders Report";
	private static final String SATI_REPORT = "Satisfaction Report";
	@FXML
	private HBox mStoreBox;
	@FXML
	private VBox mProgressBox;
    private static Worker mWorker;
    private static Class<? extends Report> mCurrentlyViewedReportType;
    private static String mCurrentlyViewedReportID;
    private static int numberOfViewedReports = 0;
    @FXML
    private TableView<Report> mTableReports;
    @FXML
    private TableColumn<Report, String> mColumnsYear;
    @FXML
    private TableColumn<Report, String> mColumnsQuarter;
    @FXML
    private TableColumn<Report, String> mColumnsStoreID;
    @FXML
    private TableColumn<Report, String> mColumnType;
    @FXML
    private TextField mTxtFieldYear;
    @FXML
    private ComboBox<String> mReportTypeSelector, mQuarterSelector, mStoreSelector;
    private ObservableList<Report>  mObservableBackupReportsList ;
	List<String> mStorslist = new ArrayList<String>();
	//private InitReports init;
	public ArrayList<Report> Reports=new ArrayList<Report>();
	ObservableList<Report> mObserReports;
	public static double mPropertyX=0;
	private static boolean rightSide = false;
	
    public ReportsController() {
        System.out.println("first");
    }
    
    /**
     * Can the manager open a report?
     * @param requestedReport request report
     * @return true if does, false otherwise.
     */
    private boolean canViewReport(Report requestedReport) {
    	if (numberOfViewedReports == 0)
    		return true;
    	if (numberOfViewedReports == 1 && mWorker.getWorkerType().equalsIgnoreCase("NetworkManager")
    			&& requestedReport.getClass().equals(mCurrentlyViewedReportType)
    			&& !mCurrentlyViewedReportID.equalsIgnoreCase(requestedReport.getReportsID()))
    		return true;	// allow two reports comparing of the same type only for network manager.  	
    	return false;
    }
    

    
    /**
     * Mated 
     * @param mReports report
     */
    public void  init(ArrayList<Report> mReports) { 
		 linkAllOrdersReportsWithTheirProductsQuantityRows(mReports);
		 ArrayList<Report> reportAfterFilterByWorkerType = filterAllReportsByWorker(mReports);
		 mObserReports = FXCollections.observableArrayList(reportAfterFilterByWorkerType);
		 filDataTotTable();
    }
    /**
     * link All Orders Reports With Their Products Quantity Rows
     * @param mReports report
     */
	private void linkAllOrdersReportsWithTheirProductsQuantityRows(ArrayList<Report> mReports) {
		ArrayList<ReportDetailInOrderReport> alllist=new ArrayList<ReportDetailInOrderReport>();
		 for (Report report : mReports) {
			 if(report instanceof ReportOrder) {
				  alllist=((ReportOrder)report).getDetailsInReport();
				 break;
			 }
		 }
		  for(Report report : mReports)
		  {
			
			  	if(report instanceof ReportOrder) {
			  		ArrayList<ReportDetailInOrderReport> DetailInOrderOfPhase=new ArrayList<ReportDetailInOrderReport>();
			  			for (ReportDetailInOrderReport reportDetailInOrderReport : alllist) {
			  					if(reportDetailInOrderReport.getReportOrderID().equals(report.getReportsID())){
			  						DetailInOrderOfPhase.add(reportDetailInOrderReport);
							 
			  					}
						 ((ReportOrder) report).setProductAndQuantity(DetailInOrderOfPhase);
						
					}
				  
			  }
			  
		  }
	}
    /**
     * We take all reports  and  filters according to worker type
     * @param mReports reports
     * @return report after filters
     */
	private ArrayList<Report> filterAllReportsByWorker(ArrayList<Report> mReports) {
		ArrayList<Report> reportAfterFilterByWorkerType=new ArrayList<Report>();
		if(mWorker.getWorkerType().equals("StoreManager")) {
			for (Report report : mReports) {
				if((report.getStoreID().equals(mWorker.getStoreID()))) {
					reportAfterFilterByWorkerType.add(report);
			
				}
			} 
		}
		else {
			reportAfterFilterByWorkerType=mReports ;
			}
		return reportAfterFilterByWorkerType;
	}

	
	/**
	 * Listener for report list
	 */
    private void initListeners() {
    	mTableReports.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                	Report mLocalreport= new Report();
                	mLocalreport=((Report) mTableReports.getSelectionModel().getSelectedItem());
                	if(mLocalreport instanceof ReportsStatisfaction) {
                		if (numberOfViewedReports == 0)
                			mCurrentlyViewedReportType = ReportsStatisfaction.class;
                        showReport(mLocalreport);
                	}
                	if(mLocalreport instanceof ReportIncome) {
                		if (numberOfViewedReports == 0)
                			mCurrentlyViewedReportType = ReportIncome.class;
                		showReport(mLocalreport);
                	}
                	if(mLocalreport instanceof ReportOrder) {
                		if (numberOfViewedReports == 0)
                			mCurrentlyViewedReportType = ReportOrder.class;
                		showReport(mLocalreport);
                	}                	
                	if(mLocalreport instanceof ReportComplaint) {
                		if (numberOfViewedReports == 0)
                			mCurrentlyViewedReportType = ReportComplaint.class;
                		showReport(mLocalreport);
                	}
                	if (numberOfViewedReports == 1)	// after opening a single report
                		mCurrentlyViewedReportID = mLocalreport.getReportsID();
                }
            }	// Handle()
        });	// setOnMouseClicked()
    }	// initListeners()
    
    
    /**
     * after select the report from table we go to show him 
     * @param report Report
     */
    private void showReport(Report report) {
    	if(!canViewReport(report))
    		return;
    	try {		
    		FXMLLoader fxmlLoader = new FXMLLoader();
    		fxmlLoader.setLocation(getClass().getResource("report_view.fxml"));
        	Pane root = fxmlLoader.load(); 
        	root.setId("pane");
            ReportViewController reportViewController = fxmlLoader.getController();
            reportViewController.setReport(report);
    		 Stage editDialogStage = new Stage();
    		 editDialogStage.setOnHidden(new EventHandler<WindowEvent>() {		
 				@Override
 				public void handle(WindowEvent event) {
 					numberOfViewedReports--;
					WindowFactory.mStage.show();
 				}
 			});
    		if(report instanceof ReportsStatisfaction) { editDialogStage.setTitle("Satisfaction Report");}
    		if(report instanceof ReportComplaint) { editDialogStage.setTitle("Complaint Report");}
    		if(report instanceof ReportOrder) { editDialogStage.setTitle("Order Report");}
    		if(report instanceof ReportIncome) { editDialogStage.setTitle("Income Report");}		 
            editDialogStage.setResizable(false);
            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(WindowFactory.class.getResource("/style.css").toExternalForm());
            editDialogStage.setScene(scene);
     		numberOfViewedReports++;
     		if (mWorker.getWorkerType().equalsIgnoreCase("NetworkManager")) {
         		if (numberOfViewedReports == 1) {
         			WindowFactory.setLeft(editDialogStage);
         			rightSide = false;	
         		}
         		else if (numberOfViewedReports == 2) {
         			if (rightSide) {
         				WindowFactory.setLeft(editDialogStage);
         				rightSide = false;	
         			}
         			else {
         				WindowFactory.setRight(editDialogStage);
         				rightSide = true;
         			}
         		}
     		}
     		if (mWorker.getWorkerType().equalsIgnoreCase("NetworkManager")) {
     			if (numberOfViewedReports == 2)
     				WindowFactory.mStage.hide();
     		}
     		else
     			WindowFactory.mStage.hide();
         editDialogStage.show(); // 
    	 } catch (IOException e) {
             e.printStackTrace();
         }
    	
    }	// showReport()
 
    
/**
 * view all reports in table
 */
    private void filDataTotTable() {
		//init table
    	try {
    		this.mTableReports.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    		this.mColumnsStoreID.setCellValueFactory(new PropertyValueFactory<Report,String>("StoreID"));
    		this.mColumnType.setCellValueFactory(new PropertyValueFactory<Report,String>("ReportType"));
    		this.mColumnsQuarter.setCellValueFactory(new PropertyValueFactory<Report,String>("Quarter"));
    		this.mColumnsYear.setCellValueFactory(new PropertyValueFactory<Report,String>("Year"));
        	mObservableBackupReportsList=FXCollections.observableArrayList();
        	mObservableBackupReportsList.addAll(mObserReports);
        	this.mTableReports.setItems(mObserReports);
    	}
    	catch(Exception e) {
    		System.err.println(e.toString());
    	}
    }
   
    /**
     * Reset all filters
     * @param actionEvent button clicked
     */
    @FXML
    public void resetFilters(ActionEvent actionEvent) {
    	mObserReports.clear();
    	for (Report report : mObservableBackupReportsList) 
    		mObserReports.add(report);
    	mStoreSelector.setValue(null);
		mReportTypeSelector.setValue(null);
		mQuarterSelector.setValue(null);
		mTxtFieldYear.setText(null);
    }
    
    /**
     * Apply all filters
     * @param event button clicked
     */
    @FXML
    private void filter(ActionEvent event) {
    	String storeID = mStoreSelector.getValue();
    	String type = mReportTypeSelector.getValue();
    	String quarter = mQuarterSelector.getValue();
    	String year = null;
    	if (mTxtFieldYear.getText() != null && !mTxtFieldYear.getText().isEmpty()) {
    		try {
    			Integer.parseInt(mTxtFieldYear.getText());
    			year = mTxtFieldYear.getText();
    		}
    		catch (Exception e) {
    			WindowFactory.showErrorDialog("Invalid year",null, e.getMessage());
    			year = null;
    		}
    	}
    	mObserReports.clear();
    	for (Report report : mObservableBackupReportsList) {
    		mObserReports.add(report);
		}	
    	HashSet<Report> toDelete = new HashSet<Report>();
    	for (Report report : mObserReports) {
    		if (storeID != null && !storeID.isEmpty() && !report.getStoreID().equals(storeID)) 
    			toDelete.add(report);
    		if (type != null && !type.isEmpty() && !report.getReportType().equals(type)) 
    			toDelete.add(report);
    		if (quarter != null && !quarter.isEmpty() && !report.getQuarter().equals(quarter)) 
    			toDelete.add(report);
    		if (year != null && !year.isEmpty() && !report.getYear().equals(year)) 
    			toDelete.add(report);
    	}
    	for (Report report : toDelete) {
    		mObserReports.remove(report);
    	}
    }

    /**
     * Reset store filter
     * @param event button clicked
     */
    @FXML
    private void resetStoresFilter(ActionEvent event) {
    	mStoreSelector.setValue(null);
    	filter(null);
    }
    
    /**
     * Reset type filter
     * @param event button clicked
     */
    @FXML
    private void resetTypeFilter(ActionEvent event) {
    	mReportTypeSelector.setValue(null);
    	filter(null);
    }
   
    /**
     * Reset quarter filter
     * @param event button clicked
     */
    @FXML
    private void resetQuarterFilter(ActionEvent event) {
    	mQuarterSelector.setValue(null);
    	filter(null);
    }
    
    /**
     * Reset year filter
     * @param event button clicked
     */
    @FXML
    private void resetYearFilter(ActionEvent event) {
    	mTxtFieldYear.setText(null);
    	filter(null);
    }
    
    /**
     * the method get all array list and remove duplicate in list
     * @param mList list
     * @return list without duplicate
     */
    public static ArrayList<String>  removDupFromAyrrayList(ArrayList<String> mList) {
    	
    	// add elements to al, including duplicates
    	Set<String> hs = new HashSet<>();
    	hs.addAll(mList);
    	mList.clear();
    	mList.addAll(hs);
    	return mList;
    	
    }
    

    /**
     * get from server all reports
     */
    private void GetAllReportFromServer() {
		ArrayList<ClientToServerMessage> ReportsQueryes=new ArrayList<ClientToServerMessage>();
		ClientToServerMessage clientToServerMessage1 = new ClientToServerMessage(EQueryOption.GET_All_COMPLAINT_REPORTS_INFO, null, "allComplaintReports");
		ClientToServerMessage clientToServerMessage2 = new ClientToServerMessage(EQueryOption.GET_All_DETAIL_IN_ORDER_REPORTS_INFO, null, "alldetailinorederreports");
		ClientToServerMessage clientToServerMessage3 = new ClientToServerMessage(EQueryOption.GET_All_ORDER_REPORTS_INFO, null, "allorederreports");
		ClientToServerMessage clientToServerMessage4 = new ClientToServerMessage(EQueryOption.GET_All_INCOME_REPORTS_INFO, null, "allincomereports");
		ClientToServerMessage clientToServerMessage5 = new ClientToServerMessage(EQueryOption.GET_All_SATISFACTION_REPORTS_INFO, null, "allsatisfactionports");
		ReportsQueryes.add(clientToServerMessage1);
		ReportsQueryes.add(clientToServerMessage2);
		ReportsQueryes.add(clientToServerMessage3);
		ReportsQueryes.add(clientToServerMessage4);
		ReportsQueryes.add(clientToServerMessage5);
		mProgressBox.setVisible(true);
		(new ConnectionController(this)).sendToServer(ReportsQueryes);
   
    }


	@SuppressWarnings("unchecked")
	@Override
	public  void handleMessageFromServer(Object message) {
		System.out.println("> "+message);
		Platform.runLater(() -> mProgressBox.setVisible(false));
		if(message instanceof ArrayList<?> && ((ArrayList<?>) message).isEmpty())
			return;
		if(message instanceof ArrayList<?> && ((ArrayList<?>) message).get(0) instanceof Report){
			Reports = (ArrayList<Report>)message;
			init(Reports);
			ClientToServerMessage msg = new ClientToServerMessage(EQueryOption.GET_ALL_STORES, null, "stores");
			Platform.runLater(() -> mProgressBox.setVisible(true));
			(new ConnectionController(this)).sendToServer(msg);
			return;
		}
		if(message instanceof ArrayList<?> && ((ArrayList<?>) message).get(0) instanceof Store){
			ArrayList<Store> stores = (ArrayList<Store>) message;
			for (Store store : stores) {
				if (!mStoreSelector.getItems().contains(store.getStoreID()))
					mStoreSelector.getItems().add(store.getStoreID());
			}
			return;
		}
	}



	@Override
	public void setDetails(Object... objects) {
		mWorker =  (Worker) objects[0];	// The viewing worker
		WindowFactory.mStage.setTitle("Bad worker type for reports view: "+mWorker.getWorkerType());
		if (mWorker.getWorkerType().equalsIgnoreCase("StoreManager")) {
			WindowFactory.mStage.setTitle("Store Manager view reports");
			mStoreBox.setDisable(true);
			if (mStoreSelector.getItems().contains(mWorker.getStoreID()))
				mStoreSelector.getItems().add(mWorker.getStoreID());
			mStoreSelector.setValue(mWorker.getStoreID());
		}
		if (mWorker.getWorkerType().equalsIgnoreCase("NetworkManager")) {
			WindowFactory.mStage.setTitle("Network Manager view reports");
			mStoreBox.setDisable(false);
		}
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		mObserReports=FXCollections.observableArrayList(Reports);
		mReportTypeSelector.getItems().addAll(COMPLAINTS_REPORT,INCOME_REPORT,
				ORDERS_REPORT, SATI_REPORT);
		mQuarterSelector.getItems().addAll("1","2","3","4");
		initListeners();
		GetAllReportFromServer();
	}
	
	/**
	 * back to main menu
	 * @param event button clicked
	 */
	@FXML
	private void backToMainMenu(ActionEvent event) {
		if (mWorker.getWorkerType().equalsIgnoreCase("StoreManager")) {
			WindowFactory.show("/client/mainmenu/store_manager_main_menu.fxml", mWorker.getUserID());
			return;
		}
		if (mWorker.getWorkerType().equalsIgnoreCase("NetworkManager")) {
			WindowFactory.show("/client/mainmenu/network_manager_main_menu.fxml", mWorker.getUserID());
			return;
		}
		System.err.println("worker type is " + mWorker.getWorkerType()+", but it should be store or network manager!");
	}

}	// ReportsController

