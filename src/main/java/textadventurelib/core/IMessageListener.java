package textadventurelib.core;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface IMessageListener {
	/**
	 * 
	 * @param message The message to send to this listener.
	 */
	void sendMessage(String message);
	//TODO: I am not sure if I like this.
	/**
	 * 
	 * @param message Sends a message to this listener that does not get processed.
	 */
	void sendMessageNoProcessing(String message);
}
