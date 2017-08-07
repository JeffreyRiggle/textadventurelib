package textadventurelib.layout;

import java.awt.Container;

import ilusr.core.interfaces.ISuspend;
import textadventurelib.core.IMessageListener;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface TextAdventureLayout extends ISuspend {
	/**
	 * 
	 * @return The container for the layout.
	 */
	Container container();
	/**
	 * 
	 * @param listener The message listener to apply to the layout.
	 */
	void messageListener(IMessageListener listener);
	/**
	 * 
	 * @param text The new text log.
	 */
	void textLog(String text);
	/**
	 * 
	 * @return The text log for the layout.
	 */
	String textLog();
}
