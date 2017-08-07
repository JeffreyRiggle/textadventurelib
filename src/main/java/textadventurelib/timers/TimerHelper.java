package textadventurelib.timers;

import java.util.*;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TimerHelper {

	private long duration;
	private ITimerTaskFactory taskFactory;
	private Timer timer;
	private TimerTask task;
	
	/**
	 * 
	 * @param task The task to run.
	 * @param duration The time to wait to run this process.
	 */
	public TimerHelper(ITimerTaskFactory taskFactory, long duration) {
		this.duration = duration;
		this.taskFactory = taskFactory;
		this.timer = new Timer();
	}
	
	/**
	 * 
	 * @param value The new value for the duration.
	 */
	public void duration(long value) {
		duration = value;
	}
	
	/**
	 * 
	 * @return The duration of this timer.
	 */
	public long duration() {
		return duration;
	}
	
	/**
	 * 
	 * @param value The new task factory for this timer.
	 */
	public void taskFactory(ITimerTaskFactory value) {
		taskFactory = value;
	}
	
	/**
	 * 
	 * @return The task factory associated with this timer.
	 */
	public ITimerTaskFactory taskFactory() {
		return taskFactory;
	}
	
	/**
	 * Start the timer.
	 */
	public void start() {
		task = taskFactory.create();
		timer = new Timer();
		timer.schedule(task, duration);
	}
	
	/**
	 * Stop the timer.
	 */
	public void stop() {
		timer.cancel();
		task = null;
	}
}