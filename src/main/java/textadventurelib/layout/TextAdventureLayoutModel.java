package textadventurelib.layout;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import textadventurelib.core.IMessageListener;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TextAdventureLayoutModel {

	private final String initialText;
	
	private SimpleStringProperty textLog;
	private IMessageListener messageListener;
	private SimpleStringProperty resource;
	private ObservableList<String> inputs;
	
	/**
	 * 
	 * @param initialText The initial text for the layout.
	 */
	public TextAdventureLayoutModel(String initialText) {
		this.initialText = initialText;
		
		textLog = new SimpleStringProperty(initialText);
		resource = new SimpleStringProperty();
		inputs = FXCollections.observableArrayList();
	}
	
	/**
	 * Resets the text log.
	 */
	public void reset() {
		textLog.set(initialText);
	}
	
	/**
	 * 
	 * @return The text log to display.
	 */
	public SimpleStringProperty textLog() {
		return textLog;
	}
	
	/**
	 * 
	 * @param message Sends a message to the game state.
	 */
	public void sendMessage(String message) {
		textLog.set(textLog.get() + "\n" + message);
		
		if (messageListener != null) {
			messageListener.sendMessage(message);
		}
	}
	
	/**
	 * 
	 * @param messageListener The new message listener.
	 */
	public void setMessageListener(IMessageListener messageListener) {
		this.messageListener = messageListener;
	}
	
	/**
	 * 
	 * @return The resource to display in the view.
	 */
	public SimpleStringProperty resource() {
		return resource;
	}
	
	/**
	 * 
	 * @return The list of possible inputs for the view.
	 */
	public ObservableList<String> inputs() {
		return inputs;
	}
}
