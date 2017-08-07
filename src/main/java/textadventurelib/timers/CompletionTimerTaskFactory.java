package textadventurelib.timers;

import java.util.TimerTask;
import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import textadventurelib.core.ICompletionListener;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class CompletionTimerTaskFactory implements ITimerTaskFactory{

	private Object data;
	private ICompletionListener listener;
	private CompletionTimerTask task;
	
	/**
	 * 
	 * @param data The data to complete with.
	 */
	public <T> CompletionTimerTaskFactory(T data) {
		this.data = data;
	}
	
	/**
	 * 
	 * @param listener A listener to call when the completion has occurred.
	 */
	public void listener(ICompletionListener listener) {
		this.listener = listener;
	}
	
	@Override
	public TimerTask create() {
		if (task != null) {
			task.removeListener(listener);
		}
		
		LogRunner.logger().log(Level.INFO, String.format("Creating completion timer task with data: %s", data));

		task = new CompletionTimerTask(data);
		
		if (listener != null) {
			task.addListener(listener);
		}
		
		return task;
	}

}
