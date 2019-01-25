package client.settings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ConnectionController.ConnectionDetails;
import client.IController;
import client.WindowFactory;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

/**
 * Class responsible for settings view behavior.
 * Must call setConnected() when creating view.
 *
 */
public class SettingsController implements Initializable, IController {
	public static final String SETTINGS_FOLDER_PATH = System.getProperty("user.home") +File.separator + 
			"ZerLiSettings"+File.separator;
    @FXML
    private TextField mHostTextField;
    @FXML
    private TextField mPortTextField;
    @FXML
    private Label mSettingsSavedLabel;

    /**
     * Go back to user login view.
     * @param event button clicked
     */
    @FXML
    private void goBackFromSettingsClick(ActionEvent event) {
    	String fxmlPath = "/client/user/user_login_view.fxml";
    	WindowFactory.show(fxmlPath);
    }

    /**
     * Save user's settings
     * @param event button clicked
     */
    @FXML
    private void saveSettingsClick(ActionEvent event) {
    	try {
    		String host = mHostTextField.getText();
    		if (host.isEmpty()) {
    			host = mHostTextField.getPromptText();
    		}		
    		String portText = mPortTextField.getText();
    		int port;
    		if (portText.isEmpty())
    			port = Integer.parseInt(mPortTextField.getPromptText());	
    		else port = Integer.parseInt(mPortTextField.getText());	
    		if (ConnectionDetails.writeConnectionDetails(host, port)) {
            	System.out.println("Settings saved");		
            	mSettingsSavedLabel.setVisible(true);	
            	// hide settings saved label after 3 seconds.
            	PauseTransition visiblePause = new PauseTransition(
            	        Duration.seconds(1)
            	);
            	visiblePause.setOnFinished(
            	        event2 -> mSettingsSavedLabel.setVisible(false)
            	);
            	visiblePause.play();
    		}
    	}
    	catch(NumberFormatException e) {
    		WindowFactory.showErrorDialog("Invalid port number", 
    				"Invalid port number", "Please check your port number.");
    	}
    	catch(Exception ex) {
    		System.err.println("Saving settings error: "+ex);
    	}
    }

    /**
     * Initialize Settings view
     */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
    	if (ConnectionDetails.readConnectionDetails()) {
    		mHostTextField.setPromptText(ConnectionDetails.getHost());
    		mPortTextField.setPromptText(Integer.toString(ConnectionDetails.getPort()));
    	}
    	else {
    		mHostTextField.setPromptText(ConnectionDetails.DEFAULT_HOST);
    		mPortTextField.setPromptText(Integer.toString(ConnectionDetails.DEFAULT_PORT));
    	}
    	mHostTextField.setFocusTraversable(false);
    	mPortTextField.setFocusTraversable(false);
    	
		WindowFactory.mStage.setTitle("Connection settings");
		WindowFactory.mStage.setResizable(false);
		mSettingsSavedLabel.requestFocus();
	}
	
	
	/**
	 * Not used here.
	 */
	@Override
	public void setDetails(Object... objects) {}	// not used

	
	/**
	 * Create Folder in user home directory
	 * which have the user's settings
	 * such as connection settings.
	 * The files are also used by the app's inner logics.
	 */
	public static void initSettingFiles() {
		new File(SETTINGS_FOLDER_PATH).mkdirs();	// create the settings folder.
	 }
	

	/**
	 * Write data to fileName inside Zerli settings folder.
	 * @param fileName filename
	 * @param dataToWrite the data to write
	 * @throws Exception Exception
	 */
	public static void writeDetails(String fileName, String dataToWrite) throws Exception{
    	String filePath = generateFilePath(fileName);
		File file = new File(filePath);
		if(!file.exists())
			file.createNewFile();	   
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));
    	bufferedWriter.write(dataToWrite);
    	if (bufferedWriter != null)
			bufferedWriter.close();
	}
	
	
	  /**
	   * Read data from a fileName within settings folder.
	   * @param fileName file name
	   * @throws Exception Exception
	   * @return data string.
	   */
	  public static ArrayList<String> readDetails(String fileName) throws Exception {
		  String filePath = generateFilePath(fileName);
		  ArrayList<String> lines = new ArrayList<String>();
		  String line;	  
		  BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
          while ((line = bufferedReader.readLine()) != null)	// read lines until end of file
        	  lines.add(line);				// add non null lines to the ArrayList.
		  if (bufferedReader != null)
			  bufferedReader.close();
		  return lines;
	  }
	  

	/**
	 * generate full file path.
	 * @param fileName file name
	 * @return full file path.
	 */
	private static String generateFilePath(String fileName) {
    	if (!fileName.endsWith(".zerli"))
    		fileName += ".zerli";
    	return SETTINGS_FOLDER_PATH + fileName;
	}

}	// SettingsController
