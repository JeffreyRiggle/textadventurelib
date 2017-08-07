package textadventurelib.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import ilusr.gamestatemanager.IFinishListener;
import ilusr.logrunner.LogRunner;
import textadventurelib.core.ExecutionParameters;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class FinishAction implements IAction {

	private List<IFinishListener> listeners;
	
	/**
	 * Base Ctor.
	 */
	public FinishAction() {
		listeners = new ArrayList<IFinishListener>();
	}
	
	/**
	 * 
	 * @param listener A @see IFinishListener to add.
	 */
	public void addFinishListener(IFinishListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * 
	 * @param listener A @see IFinishListener to remove.
	 */
	public void removeFinishListener(IFinishListener listener) {
		listeners.remove(listener);
	}
	
	@Override
	public void execute(ExecutionParameters params) {
		LogRunner.logger().log(Level.INFO, String.format("Executing Finish Action. Game will end soon."));
		List<IFinishListener> list = new ArrayList<IFinishListener>(listeners);
		for (IFinishListener listener : list) {
			listener.onFinished();
		}
	}
}
