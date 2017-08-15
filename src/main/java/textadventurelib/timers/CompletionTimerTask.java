package textadventurelib.timers;

import java.util.TimerTask;

import ilusr.logrunner.LogRunner;
import textadventurelib.actions.CompletionAction;
import textadventurelib.core.ICompletionListener;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class CompletionTimerTask extends TimerTask{
	
	private CompletionAction completion;
	
	/**
	 * 
	 * @param data The completion data to be raised.
	 */
	public <T>CompletionTimerTask(T data) {
		completion = new CompletionAction(data);
	}
	
	/**
	 * 
	 * @param listener The listener to add.
	 */
	public void addListener(ICompletionListener listener) {
		completion.addListener(listener);
	}
	
	/**
	 * 
	 * @param listener The listener to remove.
	 */
	public void removeListener(ICompletionListener listener) {
		completion.removeListener(listener);
	}
	
	/**
	 * Clear all listeners.
	 */
	public void clearListeners() {
		completion.clearListeners();
	}
	
	@Override
	public void run() {
		LogRunner.logger().info("Executing completion");
		completion.execute(null);
	}
}