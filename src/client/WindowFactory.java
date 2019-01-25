package client;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Optional;

import common.Product;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * New fxml windows / dialogs factory.
 * 
 */
public class WindowFactory {
	public static Stage mStage = null;

	/**
	 * Initialize the stage
	 * @param stage stage 
	 */
	public static void initializeStage(Stage stage) {
		if (stage == null)
			return;
		mStage = stage;
		stage.setTitle("Zer-Li by G8");
		stage.setResizable(false);
		URL url = WindowFactory.class.getResource("/logo.jpg");
		stage.getIcons().add(new Image(url.toString()));
		setCenter(mStage);
	}
	
	/**
	 * Open a new fxml window. The fxmlPath's controller MUST implement IController!
	 * Can pass objects to the next fxml controller.
	 * 
	 * @param fxmlPath new window's fxml path.
	 * @param objects parameters to pass.
	 */
	public static void show(String fxmlPath, Object...objects) {
		FXMLLoader loader = new FXMLLoader();
		try {
			loader.setLocation(Main.class.getResource(fxmlPath));
			Pane root = loader.load();
			IController controller = loader.getController();
			if (objects != null)
				controller.setDetails(objects);	// pass objects.
			root.setId("pane");
			Scene scene = new Scene(root);	
			scene.getStylesheets().addAll(WindowFactory.class.getResource("/style.css").toExternalForm());
			mStage.setScene(scene);
			setCenter(mStage);
			mStage.show();			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * set screen to center
	 * @param stage stage
	 */
	public static void setCenter(Stage stage) {
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
		stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
	}
	
	/**
	 * set screen to center
	 * @param stage stage
	 */
	public static void setRight(Stage stage) {
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		stage.setX(primScreenBounds.getWidth()/2);
		stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
	}
	
	/**
	 * set screen to center
	 * @param stage stage
	 */
	public static void setLeft(Stage stage) {
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		stage.setX(0);
		stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
	}
	
	/**
	 * Shows a new ERROR Dialog, can pass null parameters.
	 * 
	 * @param title Window's title.
	 * @param header the header.
	 * @param content the content
	 */
	public static void showErrorDialog(String title, String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.getDialogPane().getStylesheets().add("/style.css");
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}
	
	/**
	 * Shows an Information dialog. When user pressed OK, opens a new fxml view.
	 * the fxmlPath's controller must implements IController!
	 * 
	 * @param title the title
	 * @param header the header
	 * @param content the content
	 * @param fxmlpath the new fxml path
	 * @param userID the logged in User.
	 */
	public static void showInfoDialog(String title,String header,String content,String fxmlpath,String userID )
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.getDialogPane().getStylesheets().add("/style.css");
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
		WindowFactory.show(fxmlpath, userID);
	}
	
	/**
	 * Single input dialog.
	 * 
	 * @param title window's title
	 * @param header the header
	 * @param content the content, MAKE IT SHORT!
	 * @return the user's input
	 */
	public static String showInputDialog(String title, String header, String content) {
		String[] results = showInputDialog(title, header, content, null);
		if (results == null)
			return null;
		else return results[0];
	}
	
	
	/**
	 * Several input fields dialog. given number of string as inputNames, 
	 * show matching input fields.
	 * 
	 * @param title the dialog's title
	 * @param header the dialog's header
	 * @param inputNames the input fields name
	 * @return array of strings which are the user's input
	 */
	public static String[] showInputDialog(String title, String header, String... inputNames) {
		final int size = inputNames.length;
		if (size == 0)
			return null;		
		Dialog<String[]> dialog = new Dialog<>();
		dialog.getDialogPane().getStylesheets().add("/style.css");
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);		
		TextField textFields[] = new TextField[size];		
		for (int i=0; i<size; ++i) {
			if (inputNames[i] != null) {
				inputNames[i] = inputNames[i].replace(":", "").trim();
				grid.add(new Label(inputNames[i]+" :"), 0, i);
				textFields[i] = new TextField();
				textFields[i].setPromptText(inputNames[i]);
				grid.add(textFields[i], 1, i);
			}
		}
		dialog.getDialogPane().setContent(grid);
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == ButtonType.APPLY) {
		    String[] results = new String[size];
		       for (int i=0; i<size; ++i) {
		    	   if (textFields[i] != null) {
			    	   String temp = textFields[i].getText();
			    	   results[i] = temp;
			    	   if (temp.isEmpty()) {
			    		   showErrorDialog("Empty Field", null, inputNames[i]);
			    		   return null;
			    	   }
		    	   }
		       }
		       return results;
		    }
		    return null;
		});	
		Optional<String[]> results = dialog.showAndWait();
		if (results.isPresent()) 
			return results.get(); 
		else return null;
	}
	
	/**
	 * Just an informative pop up window.
	 * @param title the dialog title
	 * @param header the header
	 * @param content the content
	 */
	public static void showInfoDialog(String title, String header, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.getDialogPane().getStylesheets().add("/style.css");
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	
	/**
	 * Show a pop-up dialog containing a text area. 
	 * The content string is the massive content..
	 * 
	 * @param title the title
	 * @param header the header
	 * @param content the expandable content
	 */
	public static void showFieldDialog(String title, String header, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.getDialogPane().getStylesheets().add("/style.css");
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(""+header);	
		TextArea textArea = new TextArea(content);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		alert.getDialogPane().setExpandableContent(textArea);
		alert.showAndWait();
	}
	

	
	/**
	 * Given array of products, show combobox with products.
	 * On each product selection, populate the data.
	 * @param products products.
	 * @return chosen product
	 */
	public static Product showComboBoxDialog(Product[] products) {
		Dialog<Product> dialog = new Dialog<Product>();
		dialog.getDialogPane().getStylesheets().add("/style.css");
		dialog.setTitle("Product Selector");
		dialog.setHeaderText("Product Selector");
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);		
		ComboBox<Product> comboBox = new ComboBox<Product>();		
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == ButtonType.OK) {
		    return comboBox.getValue();
		    }
		    else return null;
		});	
		for (Product p : products)
			comboBox.getItems().add(p);
		VBox wrapper = new VBox();
		wrapper.getChildren().add(comboBox);
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		VBox vbox = new VBox();
		comboBox.setPromptText("Choose a product");
		final TextArea txtArea = new TextArea();
		txtArea.setEditable(false);
		txtArea.setFocusTraversable(false);
		txtArea.setMaxWidth(250);
		txtArea.setMaxHeight(180);
		vbox.getChildren().add(txtArea);
		final ImageView imgView = new ImageView();
		imgView.setFitWidth(250);
		imgView.setFitHeight(180);
		hbox.getChildren().addAll(vbox, imgView);
		wrapper.getChildren().add(hbox);
		dialog.getDialogPane().setContent(wrapper);
		comboBox.valueProperty().addListener(new ChangeListener<Product>() {
			@Override
			public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
				LinkedHashMap<String,String> data = newValue.getInfo();
				String content = "";
				for (Entry<String, String> entry : data.entrySet()) {
					content += entry.getKey() + entry.getValue();
				}
				if (newValue.getPicture() != null) {
					imgView.setImage(newValue.getPicture().getImage());
				}
				txtArea.setText(content);
			}
		});
		Optional<Product> results = dialog.showAndWait();
		if (results.isPresent()) 
			return results.get(); 
		else return null;
	}
	
	/**
	 * shows a user id selector
	 * @param users users id
	 * @param title title of pop up
	 * @param prompt header of pop up
	 * @return selected user id
	 */
	public static String showComboBoxDialog(String[] users, String title, String prompt) {
		Dialog<String> dialog = new Dialog<String>();
		dialog.getDialogPane().getStylesheets().add("/style.css");
		dialog.setTitle(title);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);		
		ComboBox<String> comboBox = new ComboBox<String>();	
		comboBox.setPromptText(prompt);
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == ButtonType.OK) {
		    return comboBox.getValue();
		    }
		    else return null;
		});	
		for (String u : users) {
			if (u != null)
				comboBox.getItems().add(u);
		}
		dialog.getDialogPane().setContent(comboBox);
		Optional<String> results = dialog.showAndWait();
		if (results.isPresent()) 
			return results.get(); 
		else return null;
	}
	
	/**
	 * Creates and show a dialog containing a textArea.
	 * The user can fill in the textArea and receive the input as string.
	 * 
	 * @param title the pop up's title
	 * @param header the pop's up header
	 * @return user's input from the text area
	 */
	public static String showInputField(String title, String header) {	
		Dialog<String> dialog = new Dialog<String>();
		dialog.getDialogPane().getStylesheets().add("/style.css");
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);		
		TextArea textArea = new TextArea();		
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == ButtonType.APPLY) {
		    return textArea.getText();
		    }
		    else return null;
		});	
		dialog.getDialogPane().setContent(textArea);
		Optional<String> results = dialog.showAndWait();
		if (results.isPresent()) 
			return results.get(); 
		else return null;
	}
	
	/**
	 * Show Exception details in a Field Dialog.
	 * @param e the exception to show
	 */
	public static void showException(Exception e) {
		StringWriter stackTrace = new StringWriter();
		e.printStackTrace(new PrintWriter(stackTrace));
		showFieldDialog(e.toString(), e.toString(), stackTrace.toString());
	}
	
	/**
	 * Shows a yes/no dialog.
	 * 
	 * @param title the title
	 * @param header the header 
	 * @param content the content
	 * @return true if yes pressed / false if no pressed
	 */
	public static boolean showYesNoDialog(String title, String header, String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.getDialogPane().getStylesheets().add("/style.css");
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.YES){
		   return true;
		} 
		return false;
	}
	
}	// WindowFactory
