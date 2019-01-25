
package client;

import ocsf.client.*;
import common.*;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;

import client.settings.SettingsController;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 */
public class ConnectionController extends AbstractClient {

	/**
   	* The interface type variable.  It allows the implementation of 
   	* the display method in the client.
   	*/
	private IClient clientUI;

	/**
	 * Constructs an instance of the chat client.
	 * @param clientUI The interface type variable.
   	 */
	public ConnectionController(IClient clientUI) {
		super(ConnectionDetails.DEFAULT_HOST, ConnectionDetails.DEFAULT_PORT); 
		this.clientUI = clientUI;
		if (ConnectionDetails.readConnectionDetails()) {	// get connection details from file and update.
			this.setHost(ConnectionDetails.getHost());
			this.setPort(ConnectionDetails.getPort());
		}
		try {
			openConnection();
		} catch (Exception e) {
			e.printStackTrace();
			WindowFactory.showErrorDialog("Open connection failed", "Can't connnect to DataBaseServer", e.toString());
		}
	}

	/**
	 * For testing connection
	 * @param clientUI the client
	 * @param connectionLabel	Connection label
	 */
	public ConnectionController(IClient clientUI, Label connectionLabel) {
		super(ConnectionDetails.DEFAULT_HOST, ConnectionDetails.DEFAULT_PORT); 
		this.clientUI = clientUI;
		if (ConnectionDetails.readConnectionDetails()) {	// get connection details from file and update.
			this.setHost(ConnectionDetails.getHost());
			this.setPort(ConnectionDetails.getPort());
		}
		try {
			openConnection();
			connectionLabel.setText("Connected");
			connectionLabel.setTextFill(Color.GREEN);
		} catch (Exception e) {
			e.printStackTrace();
			WindowFactory.showErrorDialog("Testing connection failed", "Can't connnect to DataBaseServer", e.toString());
			connectionLabel.setText("Disconnected");
			connectionLabel.setTextFill(Color.RED);
		}
		finally {
			try {
				this.closeConnection();
			} catch (IOException e) {
				System.err.println("Closing connection failed: "+e);
			}
		}
	}

  
	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		if (msg instanceof Exception) {
			Platform.runLater(() -> WindowFactory.showException((Exception)msg));
		}
		else
			clientUI.handleMessageFromServer(msg);
		try {
			this.closeConnection();
		} catch (IOException e) {
			System.err.println("Closing connection failed: "+e);
		}
	}

	/**
	 * Sends an object to the server
	 * 
	 * @param msg object to send
	 */
	public void sendToServer(Object msg) {
		if (msg == null) {
			WindowFactory.showErrorDialog("Cant send null to server", null, "Cant send null to server");
			return;
		}
		if (msg instanceof File) {
			msg = new SendableFile((File)msg);
		}
		if (msg instanceof SendableFile || msg instanceof ClientToServerMessage
			  || msg instanceof ArrayList<?>) {
			try {
				super.sendToServer(msg);
			} catch (IOException e) {
				WindowFactory.showErrorDialog("Sending to server failed", "Can't send to DataBaseServer", e.toString());
				e.printStackTrace();
			}
			return;
		}
		WindowFactory.showErrorDialog("Sending to server failed", null, "Sorry, you can't send a "+
				msg.getClass().getSimpleName() + "!!!");
	}
  
  
	/**
	 * This method terminates the client.
   		*/
	public void quit() {
		try {
			closeConnection();
		}
		catch(IOException e) {}
		System.exit(0);
	}
  

	/**
	 * Inner Class containing the connection details and logics for 
	 * connecting to the server.
	 *
	 */
	public static class ConnectionDetails {
		// Default server connection details.
		public final static String DEFAULT_HOST = "localhost";
		public final static int DEFAULT_PORT = 5555;

		// connection file name to read from when creating a new ConnectionController
		private final static String CONNECTION_FILENAME = "ZerLiConnectionInfo";
		private static String mHost;
		private static int mPort;
	
		
	  /**
	   * Write connection details to CONNECTION_FILENAME
	   * @param host host to write
	   * @param port port to write
	   * @return true if writing succeeded
	   */
	  public static boolean writeConnectionDetails(String host, int port) {
		  String connectionInfoToWrite = host + "\n" + port;
		  try {
			  SettingsController.writeDetails(CONNECTION_FILENAME, connectionInfoToWrite);
			  return true;
		  }
		  catch (Exception e){
			  System.err.println("Write error to file " + CONNECTION_FILENAME);
			  System.err.println(e);
			  return false;
		  }
	  }
	  
	  /**
	   * Get the host address
	   * @return the host address
	   */
	  public static String getHost() {
		  return mHost;
	  }
	  
	  /**
	   * Get the port number
	   * @return the port number
	   */
	  public static int getPort() {
		  return mPort;
	  }
	  
	  /**
	   * Read connection details from a CONNECTION_FILENAME
	   * @return true if reading succeeded
	   */
	  public static boolean readConnectionDetails() {
		  try {
			ArrayList<String> connectionData = SettingsController.readDetails(CONNECTION_FILENAME);
			mHost = connectionData.get(0);
			mPort = Integer.parseInt(connectionData.get(1));		
			return true;
		} catch (Exception e) {
			if (!(e instanceof FileNotFoundException)) {
				System.err.println("Unable to read file: " + CONNECTION_FILENAME);
		        System.err.println(e);	
			}
	        return false;
		}
	  }
	  
	  
	}	// end inner class

}	// end ConnectionController
