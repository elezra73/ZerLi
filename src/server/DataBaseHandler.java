package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.mysql.jdbc.Statement;

/**
 * This class responsible to connect and execute queries from the SQL DATABASE.
 * 
 * @author Roma
 *
 */
public class DataBaseHandler {
	
	private final static String SETTINGS_PATH = System.getProperty("user.dir") + 
			File.separator + "ServerFiles" + File.separator + "databaseSettings.zerli";
	
	// Default settings
	private final static String SQL_HOST = "jdbc:mysql://localhost/zerli"; 
	private final static String SQL_USERNAME = "root";
	private final static String SQL_PASSWORD = "Braude";
	//
	private static Connection mSqlConnection;
	
	// Settings to be read from databaseSettings.zerli
	private static String mSqlHost;
	private static String mSqlUsername;
	private static String mSqlPassword;
	  
	/**
	 * The constructor loads SQL settings from the SETTINGS_PATH file.
	 */
	public DataBaseHandler() {
		// Try to read settings
		ArrayList<String> lines = new ArrayList<String>();
		String line;
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(SETTINGS_PATH));
	        while ((line = bufferedReader.readLine()) != null)	// read lines until end of file
	      	lines.add(line);				// add non null lines to the ArrayList.
			if (bufferedReader != null)
		        bufferedReader.close();
			mSqlHost = lines.get(0);
			mSqlUsername = lines.get(1);
			mSqlPassword = lines.get(2);
		}
		catch (Exception e) {			// Something went wrong. Use default values.
			mSqlHost = SQL_HOST;
			mSqlUsername = SQL_USERNAME;
			mSqlPassword = SQL_PASSWORD;
		}	
		if (mSqlConnection == null)
			initializeSqlConnection();
	}
	
	/**
	 * Initialize an SQL connection.
	 */
	private static void initializeSqlConnection() {
		System.out.println("\nSQL Server registered data: ");
        System.out.println("\tSQL host:\t\t" + mSqlHost);
        System.out.println("\tSQL username:\t\t" + mSqlUsername);
        System.out.println("\tSQL password:\t\t" + mSqlPassword);
        System.out.println();
   		try {
   			Class.forName("com.mysql.jdbc.Driver").newInstance();
   			mSqlConnection = DriverManager.getConnection(mSqlHost, mSqlUsername, mSqlPassword);
   		}
   		catch (Exception e) {
   			System.err.println(e);
   			System.err.println("Can't connect to SQL database. Server shutting down..");
   			System.exit(1);
   		}
	}
	
	/**
	 * Execute a single query.
	 * 
	 * @param query query to execute
	 * @return ResultSet if exist. If not, return null.
	 */
	public ResultSet execute(String query) {
		ResultSet resultSet = null;	
		if (mSqlConnection == null)
			initializeSqlConnection();
		try {
	        Statement stmt = (Statement) mSqlConnection.createStatement();
	        boolean isResultSet = stmt.execute(query);
	        if (isResultSet) 
	        	resultSet = stmt.getResultSet();
	        return resultSet;
		}
		catch (SQLException e) {		// Something went wrong.
			System.err.println(e.toString());
			return resultSet;
		}
	}
	
	/**
	 * Execute several queries.
	 * 
	 * @param queries queries to execute
	 * @return ResultSet if exist. If not, return null.
	 */
	public ResultSet[] execute(String[] queries) {
		final int SIZE = queries.length;
		ResultSet[] resultSet = new ResultSet[SIZE];
        for (int i=0; i<SIZE; ++i) {
        	resultSet[i] = execute(queries[i]);
        }           
        return resultSet;
	}
	
	/**
	 * Execute several queries.
	 * @param queries queries to execute
	 * @return ResultSet if exist. If not, return null.
	 */
	public ResultSet[] execute(ArrayList<String> queries) {
		final int SIZE = queries.size();
		ResultSet[] resultSet = new ResultSet[SIZE];
        for (int i=0; i<SIZE; ++i) {
        	resultSet[i] = execute(queries.get(i));
        }           
        return resultSet;
	}
	
	
}
