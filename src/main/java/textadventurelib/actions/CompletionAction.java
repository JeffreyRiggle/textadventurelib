package textadventurelib.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import textadventurelib.core.ExecutionParameters;
import textadventurelib.core.ICompletionListener;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class CompletionAction implements IAction{

	private Object data;
	private List<ICompletionListener> listeners;
	
	/**
	 * Base Constructor
	 */
	public CompletionAction() {
		this(null);
	}
	
	/**
	 * 
	 * @param data The completion data to be raised.
	 */
	public <T>CompletionAction(T data) {
		this.data = data;
		this.listeners = new ArrayList<ICompletionListener>();
	}
	
	/**
	 * 
	 * @param listener The listener to add.
	 */
	public void addListener(ICompletionListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * 
	 * @param listener The listener to remove.
	 */
	public void removeListener(ICompletionListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Clears all listeners.
	 */
	public void clearListeners() {
		listeners.clear();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T data() {
		return (T)data;
	}

	public <T> void data(T value) {
		data = value;
	}

	@Override
	public void execute(ExecutionParameters params) {
		LogRunner.logger().log(Level.INFO, String.format("Executing Completion Action with text: %s", data));
		
		//Copy the data, to avoid modification exceptions.
		List<ICompletionListener> listeners = new ArrayList<ICompletionListener>(this.listeners);
		
		for (ICompletionListener listener : listeners) {
			listener.completed(data);
		}
	}
}