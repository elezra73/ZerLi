package client;

/**
 * Interface for defining how to pass data into the next fxml view.
 * Must be implemented in order to use WindowFactory services.
 *
 */
public interface IController {

	/**
	 * This method is invoked when IController asks to show other fxml view.
	 * The objects passed via the window factory can be accessed from this function. 
	 * 	
	 * @param objects that are passed from previous IController.
	 */
	public abstract void setDetails(Object... objects);
	  
}
