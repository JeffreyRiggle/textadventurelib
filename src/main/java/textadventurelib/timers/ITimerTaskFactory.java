package textadventurelib.timers;

import java.util.TimerTask;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface ITimerTaskFactory {
	/**
	 * 
	 * @return A new @see TimerTask
	 */
	TimerTask create();
}
