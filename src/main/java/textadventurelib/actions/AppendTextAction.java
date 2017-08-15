package textadventurelib.actions;

import java.util.ArrayList;
import java.util.List;

import ilusr.logrunner.LogRunner;
import textadventurelib.core.ExecutionParameters;
import textadventurelib.core.IMessageListener;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class AppendTextAction implements IAction{

	private String appendText;
	private List<IMessageListener> listeners;
	
	/**
	 * 
	 * @param text The text to append when this action is ran.
	 */
	public AppendTextAction(String text) {
		LogRunner.logger().fine(String.format("Creating Append Text Action with text: %s", text));
		appendText = text;
		listeners = new ArrayList<IMessageListener>();
	}
	
	/**
	 * 
	 * @param listener The listener to add.
	 */
	public void addListener(IMessageListener listener) {
		LogRunner.logger().finest("Adding Listener to append text action");
		listeners.add(listener);
	}
	
	/**
	 * 
	 * @param listener The listener to remove.
	 */
	public void removeListener(IMessageListener listener) {
		LogRunner.logger().finest("Removing Listener from append text action");
		listeners.remove(listener);
	}
	
	@Override
	public void execute(ExecutionParameters params) {
		LogRunner.logger().finest(String.format("Executing Append Text Action with text: %s", appendText));
		//Copy the data, to avoid modification exceptions.
		List<IMessageListener> listeners = new ArrayList<IMessageListener>(this.listeners);
		
		for (IMessageListener listener : listeners) {
			listener.sendMessageNoProcessing(appendText);
		}
	}
}
