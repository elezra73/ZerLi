package client;

import client.settings.SettingsController;
import client.user.UserController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Main program entry point.
 * 
 */
public class Main extends Application {
	
	final String firstScreenView = "/client/user/user_login_view.fxml";	// The 1st fxml view.

	/**
	 * Invoke the first fxml.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		WindowFactory.initializeStage(primaryStage);
        WindowFactory.show(firstScreenView);
	}

	/**
	 * The program's main entry point.
	 * @param args main args.
	 */
	public static void main(String[] args) {
		SettingsController.initSettingFiles();
		launch(args);
	}

	 /**
	  * At any moment of the program, if stopped, send a log out request to the server.
	  * Needed because a user can't be logged in twise.
	  */
	 @Override
	 public void stop() {
		 UserController.sendLogOutRequest(null);
		 Platform.exit();
		 System.exit(0);
	 }
	

}
