package client.user;

import java.util.Map.Entry;

import client.IController;
import client.WindowFactory;
import common.users.Customer;
import common.users.AUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This class handles the user info view.
 * provides all the user's information about himself.
 *
 */
public class UserInfoController implements IController {
	private String mUserID;
    @FXML
    private VBox mLeftVBox;
    @FXML
    private VBox mRightVBox;
    private String mPreviousMenuFxmlPath;

    /**
     * Populate VBOX with the user's info
     * @param user user instance
     */
    private void populateData(AUser user) {
    	mLeftVBox.setAlignment(Pos.BASELINE_LEFT);
    	mRightVBox.setAlignment(Pos.BASELINE_LEFT);
    	Font dataFont = new Font("Arial", 18);
    	Label blackLabel;	// left sided label
    	Label blueLabel;	// right sided label
    	
		for (Entry<String, String> entry : user.getInfo().entrySet()) {
			blackLabel = new Label(entry.getKey());
			blueLabel = new Label(entry.getValue());
			blackLabel.setFont(dataFont);
			blackLabel.setStyle("-fx-underline: true");
			blueLabel.setFont(dataFont);
			blueLabel.setTextFill(Color.DARKBLUE);
    		mLeftVBox.getChildren().add(blackLabel);
    		mRightVBox.getChildren().add(blueLabel);
		}
    	if (user instanceof Customer) 
    		WindowFactory.mStage.setTitle("Customer Info");
    } // populateData

    /**
     * on back button click
     * @param event button clicked
     */
    @FXML
    private void backButtonBlicked(ActionEvent event) {
    	if (mPreviousMenuFxmlPath == null)
    		return;
    	WindowFactory.show(mPreviousMenuFxmlPath, mUserID);
    }

    /**
     * Set user info details
     * objects[0] = User
     * objects[1] = previous menu fxml path
     * @param objects objects to path
     */
	@Override
	public void setDetails(Object... objects) {
		try {
			mUserID = ((AUser)objects[0]).getUserID();
			mPreviousMenuFxmlPath = (String)objects[1];
			populateData((AUser)objects[0]);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}	// UserInfoController
