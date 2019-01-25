package server.reports;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import common.ClientToServerMessage;
import common.Complaint;
import common.CustomerOrder;
import common.EQueryOption;
import common.Product;
import common.Store;
import common.Survey;
import common.reports.ReportComplaint;
import common.reports.ReportDetailInOrderReport;
import common.reports.ReportIncome;
import common.reports.ReportOrder;
import common.reports.ReportsStatisfaction;
import server.DataBaseHandler;
import server.EntityFactory;
import server.QueryFactory;

public class CreateReports {
	
	private DataBaseHandler dbHandler;
	CollectionTableForReports collectionTableForReports = new CollectionTableForReports();
	
	public CreateReports() {
		dbHandler = new DataBaseHandler();
	}
	
	public void generateReports(int quarter, int year) {
		try {
			initCollectionTableForReportsFromDataBase();
				createSatisfactionReport(quarter,year);
		 		createComplaintReport(quarter, year);
		 		createIncomeReports(quarter,year);
		 		creatOrderReportGroupByProductType(quarter,year);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
	}
/**
 * fun that get from database all data for creation reports 
 */
	@SuppressWarnings("unchecked")
	private void initCollectionTableForReportsFromDataBase() {
		
		//get from database stores
		ClientToServerMessage clientToServerMessage1 = new ClientToServerMessage(
				EQueryOption.GET_ALL_STORES, null, "stores");
		ArrayList<Store> storesArrayList=(ArrayList<Store>)sendToSqlClientToServerMessage(clientToServerMessage1);
		//get from database order
		ClientToServerMessage clientToServerMessage2 = new ClientToServerMessage(EQueryOption.GET_All_CUSTOMER_ORDER,
				null, "allOrders");
		ArrayList<CustomerOrder> OrderArrayList=(ArrayList<CustomerOrder>)sendToSqlClientToServerMessage(clientToServerMessage2);
		//get from database order Products
		ClientToServerMessage clientToServerMessage3 = new ClientToServerMessage(
				EQueryOption.GET_All_PRODUCT_IN_ORDER_TABLE, null, "allproductinordertable");
		ArrayList<OrderProduct> OrderProductArrayList=(ArrayList<OrderProduct>)sendToSqlClientToServerMessage(clientToServerMessage3);
		//get from database Products table 
		ClientToServerMessage clientToServerMessage4 = new ClientToServerMessage(EQueryOption.GET_All_PRODUCTS, null,
				"allproducts");
		ArrayList<Product> ProductsArrayList=(ArrayList<Product>)sendToSqlClientToServerMessage(clientToServerMessage4);
		//get from database complaints table 
		ClientToServerMessage clientToServerMessage5 = new ClientToServerMessage(EQueryOption.GET_All_COMPLAINTS, null,
				"allcomplaints");
		ArrayList<Complaint> complaintsArrayList=(ArrayList<Complaint>)sendToSqlClientToServerMessage(clientToServerMessage5);
		//get from database surveys answer
		ClientToServerMessage clientToServerMessage6 = new ClientToServerMessage(EQueryOption.GET_All_SURVEY_ANSWER,
				null, "allsurveyanswer");
		//get from database surveys
		ArrayList<Survey> SurveysArrayList=(ArrayList<Survey>)sendToSqlClientToServerMessage(clientToServerMessage6);
	//	set all table to helper object
		collectionTableForReports.setStors(storesArrayList);
		collectionTableForReports.setOrders(OrderArrayList);
		collectionTableForReports.setOrderProducts(OrderProductArrayList);
		collectionTableForReports.setProducts(ProductsArrayList);
		collectionTableForReports.setComplaint(complaintsArrayList);
		collectionTableForReports.setSurvey(SurveysArrayList);
		
		
	}
 /**
  * 
  * @param clientToServerMessage client to server message obj
  * @return entity
  */
	private Object sendToSqlClientToServerMessage(ClientToServerMessage clientToServerMessage) {

		
		Object entity = new Object();
		String[] queries = new String[1];
		try {
			queries[0] = QueryFactory.getQuery(clientToServerMessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		ResultSet [] rs = dbHandler.execute(queries);
		try {
			rs[0].next();
			System.out.println(rs[0].getString(1));
		} catch (SQLException e1) {
			System.err.println("line 259 CreateRerport"+e1.toString());
		}
		try {
			 
	//		String query = QueryFactory.addNewSatisfactionReport(Object);
		//	dbHandler.execute(query);
			entity = EntityFactory.getEntity(clientToServerMessage,rs[0]);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entity;
		
	}
/**
 * 
 * @param quary query
 */
	private void sendToSqlQuary(String quary) {
		
		try {
			dbHandler.execute(quary);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	private void sendToSqlQuaryArray(ArrayList<String> quary) {
		
		try {
			dbHandler.execute(quary);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public  void test() {
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				createSatisfactionReport(1,2018);
				
			}
		});
		t.start();
	}
/**
 * create satisfaction reports of  quarter rang
 * @param quorter quarter
 * @param year year
 */
	private void createSatisfactionReport(int quorter, int year) {
		ArrayList<Store> storeList = collectionTableForReports.getStors();
		ArrayList<Survey> generalSurvList = collectionTableForReports.getSurvey();
		ArrayList<Survey> reportSurvList =new ArrayList<Survey>();
		ArrayList<ReportsStatisfaction> repoList = new ArrayList<ReportsStatisfaction>();
		//init survey with data
		for (Survey survey : generalSurvList) {
			try {
				survey.setQuorter(survey.getAnswerDate().getQuarter());
				survey.setMounth(Integer.parseInt(survey.getAnswerDate().getMonthInString()));
				survey.setYear(Integer.parseInt(survey.getAnswerDate().getYearInString()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}
		
		


		for (Store store : storeList) {
			for (Survey survey : generalSurvList) {
				if (store.getStoreID().equals(survey.getStoreID()) 
						&& survey.getQuorter() == quorter && survey.getYear() == year) {
					Survey FilterSurvey=new Survey();
					FilterSurvey=survey;
					reportSurvList.add(FilterSurvey);
				}
			}
			
			ArrayList<Survey> l1 = new ArrayList<Survey>();
			ArrayList<Survey> l2 = new ArrayList<Survey>();
			ArrayList<Survey> l3 = new ArrayList<Survey>();

			double avgM1 = 0;
			double avgM2 = 0;
			double avgM3 = 0;
			double avgM4 = 0;
			double avgM5 = 0;
			double avgM6 = 0;
			
			AnswersHolder holder = new AnswersHolder();
			holder.year=year;
			holder.qr=quorter;
			for (Survey surv : reportSurvList) {
				int m = surv.getMounth();
				switch (quorter) {
				case 1:
					if (m == quorter+0) {
						l1.add(surv);
					} else if (m == quorter+1) {
						l2.add(surv);
					} else {
						l3.add(surv);
					}
					break;
				case 2:
					if (m == quorter+2) {
						l1.add(surv);
					} else if (m == quorter+3) {
						l2.add(surv);
					} else {
						l3.add(surv);
					}
					break;
				case 3:
					if (m == quorter+4) {
						l1.add(surv);
					} else if (m == quorter+5) {
						l2.add(surv);
					} else {
						l3.add(surv);
					}
					break;
				case 4:
					if (m == quorter+6) {
						l1.add(surv);
					} else if (m == quorter+7) {
						l2.add(surv);
					} else {
						l3.add(surv);
					}
					break;
				}
			}
			
			if(l1.size()!=0 || l2.size()!=0 ||l3.size()!=0) {
			avgM1 = 0;
			avgM2 = 0;
			avgM3 = 0;
			avgM4 = 0;
			avgM5 = 0;
			avgM6 = 0;
			
			holder.A1=0.0;
			holder.A2=0.0;
			holder.A3=0.0;
			holder.A4=0.0;
			holder.A5=0.0;
			holder.A6=0.0;
			for(Survey srv : l1) {
				holder.A1+=srv.getA1();
				holder.A2+=srv.getA2();
				holder.A3+=srv.getA3();
				holder.A4+=srv.getA4();
				holder.A5+=srv.getA5();
				holder.A6+=srv.getA6();
			}
			
		
			if(l1.size()!=0) {
			avgM1 = holder.A1 / l1.size();
			avgM2 = holder.A2 / l1.size();
			avgM3 = holder.A3 / l1.size();
			avgM4 = holder.A4 / l1.size();
			avgM5 = holder.A5 / l1.size();
			avgM6 = holder.A6 / l1.size();
			
			}
			ReportsStatisfaction satisfactionreport = new ReportsStatisfaction();
			
			String.valueOf(avgM1);
			satisfactionreport.setAvgQuestion1ForMonth1(String.format("%.03f", avgM1));
			String.valueOf(avgM2);
			satisfactionreport.setAvgQuestion2ForMonth1(String.format("%.03f", avgM2));
			String.valueOf(avgM3);
			satisfactionreport.setAvgQuestion3ForMonth1(String.format("%.03f", avgM3));
			String.valueOf(avgM4);
			satisfactionreport.setAvgQuestion4ForMonth1(String.format("%.03f", avgM4));
			String.valueOf(avgM5);
			satisfactionreport.setAvgQuestion5ForMonth1(String.format("%.03f", avgM5));
			String.valueOf(avgM6);
			satisfactionreport.setAvgQuestion6ForMonth1(String.format("%.03f", avgM6));
			
			avgM1 = 0;
			avgM2 = 0;
			avgM3 = 0;
			avgM4 = 0;
			avgM5 = 0;
			avgM6 = 0;
			
			holder.A1=0.0;
			holder.A2=0.0;
			holder.A3=0.0;
			holder.A4=0.0;
			holder.A5=0.0;
			holder.A6=0.0;
			
			
			for(Survey srv : l2) {
				holder.A1+=srv.getA1();
				holder.A2+=srv.getA2();
				holder.A3+=srv.getA3();
				holder.A4+=srv.getA4();
				holder.A5+=srv.getA5();
				holder.A6+=srv.getA6();
			}
			
			
			if(l2.size()!=0) {
			avgM1 = holder.A1 / l2.size();
			avgM2 = holder.A2 / l2.size();
			avgM3 = holder.A3 / l2.size();
			avgM4 = holder.A4 / l2.size();
			avgM5 = holder.A5 / l2.size();
			avgM6 = holder.A6 / l2.size();
			}
			
			String.valueOf(avgM1);
			satisfactionreport.setAvgQuestion1ForMonth2(String.format("%.03f", avgM1));
			String.valueOf(avgM2);
			satisfactionreport.setAvgQuestion2ForMonth2(String.format("%.03f", avgM2));
			String.valueOf(avgM3);
			satisfactionreport.setAvgQuestion3ForMonth2(String.format("%.03f", avgM3));
			String.valueOf(avgM4);
			satisfactionreport.setAvgQuestion4ForMonth2(String.format("%.03f", avgM4));
			String.valueOf(avgM5);
			satisfactionreport.setAvgQuestion5ForMonth2(String.format("%.03f", avgM5));
			String.valueOf(avgM6);
			satisfactionreport.setAvgQuestion6ForMonth2(String.format("%.03f", avgM6));
			
			avgM1 = 0;
			avgM2 = 0;
			avgM3 = 0;
			avgM4 = 0;
			avgM5 = 0;
			avgM6 = 0;
			
			holder.A1=0.0;
			holder.A2=0.0;
			holder.A3=0.0;
			holder.A4=0.0;
			holder.A5=0.0;
			holder.A6=0.0;
			
			for(Survey srv : l3) {
				holder.A1+=srv.getA1();
				holder.A2+=srv.getA2();
				holder.A3+=srv.getA3();
				holder.A4+=srv.getA4();
				holder.A5+=srv.getA5();
				holder.A6+=srv.getA6();
			}
			
			
			if(l3.size()!=0){
			avgM1 = holder.A1 / l3.size();
			avgM2 = holder.A2 / l3.size();
			avgM3 = holder.A3 / l3.size();
			avgM4 = holder.A4 / l3.size();
			avgM5 = holder.A5 / l3.size();
			avgM6 = holder.A6 / l3.size();
			}
			
			String.valueOf(avgM1);
			satisfactionreport.setAvgQuestion1ForMonth3(String.format("%.03f", avgM1));
			String.valueOf(avgM2);
			satisfactionreport.setAvgQuestion2ForMonth3(String.format("%.03f", avgM2));
			String.valueOf(avgM3);
			satisfactionreport.setAvgQuestion3ForMonth3(String.format("%.03f", avgM3));
			String.valueOf(avgM4);
			satisfactionreport.setAvgQuestion4ForMonth3(String.format("%.03f", avgM4));
			String.valueOf(avgM5);
			satisfactionreport.setAvgQuestion5ForMonth3(String.format("%.03f", avgM5));
			String.valueOf(avgM6);
			satisfactionreport.setAvgQuestion6ForMonth3(String.format("%.03f", avgM6));
			
			satisfactionreport.setYear(String.valueOf(holder.year));
			satisfactionreport.setQuartely(String.valueOf(holder.qr));
			satisfactionreport.setStoreID(store.getStoreID());
			repoList.add(satisfactionreport);
			reportSurvList.clear();
			}//end IF l1.size()!=0 || l2.size()!=0 ||l3.size()!=0
		}
		
		for (ReportsStatisfaction reportsStatisfaction : repoList) {
			String query = QueryFactory.addNewSatisfactionReport(reportsStatisfaction);
			sendToSqlQuary(query);
		}
		
		System.out.println("System create Statisfaction report ");
		
		
		
	}
/**
 * create complaint reports of  quarter rang
 * @param quorter quarter
 * @param year year
 * @throws ParseException ParseException
 */
	public void createComplaintReport(int quorter, int year) throws ParseException{
		ArrayList<Store> storeList = collectionTableForReports.getStors();
		ArrayList<Complaint> generalComlaintList = collectionTableForReports.getComplaint();
		ArrayList<Complaint> reportComplaintList =new ArrayList<Complaint>();
		
		ArrayList<ReportComplaint> repoList = new ArrayList<ReportComplaint>();

		for (Store store : storeList) {
			for (Complaint complaint : generalComlaintList) {
				if (store.getStoreID().equals(complaint.getStoreID()) 
						&& complaint.getOpenDate().getQuarter() == quorter && complaint.getOpenDate().getYearInInteger() == year) {
					reportComplaintList.add(complaint);
				}
			}
			
			List<Complaint> l1 = new ArrayList<Complaint>();
			List<Complaint> l2 = new ArrayList<Complaint>();
			List<Complaint> l3 = new ArrayList<Complaint>();
			
			for (Complaint complaint : reportComplaintList) {
				int m = complaint.getMounth();
				switch (quorter) {
				case 1:
					if (m == quorter+0) {
						l1.add(complaint);
					} else if (m == quorter+1) {
						l2.add(complaint);
					} else {
						l3.add(complaint);
					}
					break;
				case 2:
					if (m == quorter+2) {
						l1.add(complaint);
					} else if (m == quorter+3) {
						l2.add(complaint);
					} else {
						l3.add(complaint);
					}
					break;
				case 3:
					if (m == quorter+4) {
						l1.add(complaint);
					} else if (m == quorter+5) {
						l2.add(complaint);
					} else {
						l3.add(complaint);
					}
					break;
				case 4:
					if (m == quorter+6) {
						l1.add(complaint);
					} else if (m == quorter+7) {
						l2.add(complaint);
					} else {
						l3.add(complaint);
					}
					break;
				}
			}
	if(l1.size()!=0||l2.size()!=0||l3.size()!=0) {
			ReportComplaint reportComplaint= new ReportComplaint();
			reportComplaint.setStoreID(store.getStoreID());
			reportComplaint.setYear(String.valueOf(year));
			reportComplaint.setQuartely(String.valueOf(quorter));
			reportComplaint.setStoreID(store.getStoreID());
			reportComplaint.setQuantityComplaint1(String.valueOf(l1.size()));
			reportComplaint.setQuantityComplaint2(String.valueOf(l2.size()));
			reportComplaint.setQuantityComplaint3(String.valueOf(l3.size()));
			
			repoList.add(reportComplaint);
	}
			l1.clear();
			l2.clear();
			l3.clear();
			reportComplaintList.clear();
			
			
			
		}
		
		for (ReportComplaint complaintReport : repoList) {
			String query = QueryFactory.AddNewComplaintReport(complaintReport);
			sendToSqlQuary(query);
		}
		
		System.out.println("System created Complaint reports ");

	}
	/**
	 * create income reports of  quarter rang
	 * @param quorter quarter
	 * @param year year 
	 * @throws ParseException ParseException
	 */
	public void createIncomeReports(int quorter, int year) throws ParseException {
		ArrayList<Store> storeList = collectionTableForReports.getStors();
		ArrayList<CustomerOrder> generalCustomerOrderList = collectionTableForReports.getOrders();
		ArrayList<CustomerOrder> reportCustomerOrderList =new ArrayList<CustomerOrder>();
		

		ArrayList<ReportIncome> repoList = new ArrayList<ReportIncome>();
		//init customerOrders 
		for (CustomerOrder customerOrder : generalCustomerOrderList) {
			customerOrder.setQuorter(customerOrder.getOrderDate().getQuarter());
			customerOrder.setMounth(customerOrder.getOrderDate().getMonthInInteger());
			customerOrder.setYear(customerOrder.getOrderDate().getYearInInteger()); 
		}
		
		


		for (Store store : storeList) {
			for (CustomerOrder customerOrder : generalCustomerOrderList) {
				if (store.getStoreID().equals(customerOrder.getStoreID()) 
						&& customerOrder.getQuorter() == quorter && customerOrder.getYear() == year) {
					CustomerOrder FiltercustomerOrder=new CustomerOrder();
					FiltercustomerOrder=customerOrder;
					reportCustomerOrderList.add(FiltercustomerOrder);
				}
			}
			
			List<CustomerOrder> l1 = new ArrayList<CustomerOrder>();
			List<CustomerOrder> l2 = new ArrayList<CustomerOrder>();
			List<CustomerOrder> l3 = new ArrayList<CustomerOrder>();
			
			for (CustomerOrder customerOrder : reportCustomerOrderList) {
				int m = customerOrder.getMounth();
				switch (quorter) {
				case 1:
					if (m == quorter+0) {
						l1.add(customerOrder);
					} else if (m == quorter+1) {
						l2.add(customerOrder);
					} else {
						l3.add(customerOrder);
					}
					break;
				case 2:
					if (m == quorter+2) {
						l1.add(customerOrder);
					} else if (m == quorter+3) {
						l2.add(customerOrder);
					} else {
						l3.add(customerOrder);
					}
					break;
				case 3:
					if (m == quorter+4) {
						l1.add(customerOrder);
					} else if (m == quorter+5) {
						l2.add(customerOrder);
					} else {
						l3.add(customerOrder);
					}
					break;
				case 4:
					if (m == quorter+6) {
						l1.add(customerOrder);
					} else if (m == quorter+7) {
						l2.add(customerOrder);
					} else {
						l3.add(customerOrder);
					}
					break;
				}
			}
			
			Double TotalAmountFirstMonthOfQuarter=0.0;
			Double TotalAmountSecondMonthOfQuarter=0.0;
			Double TotalAmountThirdMonthOfQuarter=0.0;
			if(l1.size()!=0 ||l2.size()!=0||l3.size()!=0) {
			for(CustomerOrder customerOrder:l1) {
				TotalAmountFirstMonthOfQuarter+=Double.parseDouble(customerOrder.getFinalAmount());
			}
			
			for(CustomerOrder customerOrder:l2) {
				TotalAmountSecondMonthOfQuarter+=Double.parseDouble(customerOrder.getFinalAmount());
			}
			for(CustomerOrder customerOrder:l3) {
				TotalAmountThirdMonthOfQuarter+=Double.parseDouble(customerOrder.getFinalAmount());
			}
			
	
			ReportIncome reportIncome= new ReportIncome();
			reportIncome.setStoreID(store.getStoreID());
			reportIncome.setYear(String.valueOf(year));
			reportIncome.setQuartely(String.valueOf(quorter));
			reportIncome.setStoreID(store.getStoreID());
			
			String.valueOf(TotalAmountFirstMonthOfQuarter);
			reportIncome.setIncomeAmount1(String.format("%.03f", TotalAmountFirstMonthOfQuarter));
			String.valueOf(TotalAmountSecondMonthOfQuarter);
			reportIncome.setIncomeAmount2(String.format("%.03f", TotalAmountSecondMonthOfQuarter));
			String.valueOf(TotalAmountThirdMonthOfQuarter);
			reportIncome.setIncomeAmount3(String.format("%.03f", TotalAmountThirdMonthOfQuarter));
			
			
			
			repoList.add(reportIncome);
			}//end if l1.size()!=0 ||l2.size()!=0||l3.size()!=0
			l1.clear();
			l2.clear();
			l3.clear();
			reportCustomerOrderList.clear();
			
			
			
		}
		
		for (ReportIncome incomeReport : repoList) {
			String query = QueryFactory.addNewIncomeReport(incomeReport);
			sendToSqlQuary(query);
		}
		
		System.out.println("System created Income reports ");
		
		
		
	}
	
	/**
	 * create order reports foreach store with group by product  of  quarter rang
	 * @param quorter quarter
	 * @param year year
	 * @throws ParseException ParseException
	 */
		public void creatOrderReportGroupByProductType(int quorter, int year) throws ParseException {
			ArrayList<Store> storeList = collectionTableForReports.getStors();
			ArrayList<CustomerOrder> generalCustomerOrderList = collectionTableForReports.getOrders();
			ArrayList<OrderProduct> orderProductList=collectionTableForReports.getOrderProducts();
			ArrayList<Product> products =collectionTableForReports.getProducts();
			
			ArrayList<CustomerOrder> reportCustomerOrderList =new ArrayList<CustomerOrder>();
			

			ArrayList<ReportOrder> repoOrderList = new ArrayList<ReportOrder>();
			new ArrayList<ReportDetailInOrderReport>();
			//init customerOrders 
			for (CustomerOrder customerOrder : generalCustomerOrderList) {
				customerOrder.setQuorter(customerOrder.getOrderDate().getQuarter());
				customerOrder.setMounth(customerOrder.getOrderDate().getMonthInInteger());
				customerOrder.setYear(customerOrder.getOrderDate().getYearInInteger()); 
			}
			
			

			//Stores 
			for (Store store : storeList){
				HashMap<String,Integer> prodctQuantyHashMap = new HashMap<String,Integer>();
				//orders
				for (CustomerOrder customerOrder : generalCustomerOrderList) {
					
						
						if (store.getStoreID().equals(customerOrder.getStoreID()) 
								&& customerOrder.getQuorter() == quorter && customerOrder.getOrderDate().getYearInInteger() == year) {
							reportCustomerOrderList.add(customerOrder);
						}//end if
						
					
					
					
				}
				
				List<CustomerOrder> l1 = new ArrayList<CustomerOrder>();
				
				for (CustomerOrder customerOrder : reportCustomerOrderList) {
					customerOrder.getMounth();
					switch (quorter) {
					case 1:
							l1.add(customerOrder);
					
						break;
					case 2:
							l1.add(customerOrder);
				
						break;
					case 3:
							l1.add(customerOrder);
						break;
					case 4:
							l1.add(customerOrder);
						break;
					}
				}
				
				

				
				for (CustomerOrder customerOrder : l1) {
					
					
					for(OrderProduct  orderProduct : orderProductList) {
						
						if(customerOrder.getOrderID().equals(orderProduct.getOrderID())) {
							for(Product product: products) {
								if(product.getProductID().equals(orderProduct.getProducID())) {
									
											Integer Quantty =  prodctQuantyHashMap.get(product.getProductName());

											if (null == Quantty ) {
												// not exists 
												prodctQuantyHashMap.put(product.getProductName(), orderProduct.getQuantity());
											} else {
											// exists 
												Integer oldQuantty=prodctQuantyHashMap.get(product.getProductName());
												oldQuantty+=orderProduct.getQuantity();
												prodctQuantyHashMap.put(product.getProductName(), oldQuantty);
									    
											}//end else 
											
									
									
								}//end if
								 
							}//end For
							
						}//end if
					
					}//end for
				}//end for
														
				ReportOrder reportOrder=new ReportOrder("null",store.getStoreID() ,String.valueOf(year) ,String.valueOf(quorter), "");
				ArrayList<ReportDetailInOrderReport> reportDetailInOrderReportList=new ArrayList<ReportDetailInOrderReport>();
				for(Entry<String, Integer> prodctQuanty : prodctQuantyHashMap.entrySet()) {
					  prodctQuanty.getKey();
					    Integer Quantity = prodctQuanty.getValue();
					ReportDetailInOrderReport reportdetail =new ReportDetailInOrderReport("", prodctQuanty.getKey(),Quantity.toString(),"");
					reportDetailInOrderReportList.add(reportdetail);
				    
				}
				if(reportDetailInOrderReportList.size()>0) {
				reportOrder.setProductAndQuantity(reportDetailInOrderReportList);
				repoOrderList.add(reportOrder);
				}
				
				prodctQuantyHashMap.clear();
				
				
				
				
			}
			
			for (ReportOrder orderReport : repoOrderList) {
				
				ArrayList<String> arrylist = QueryFactory.addOrderReport(orderReport);
				sendToSqlQuaryArray(arrylist);
			}
			
			System.out.println("System created order reports ");
			
			
		}

	}


