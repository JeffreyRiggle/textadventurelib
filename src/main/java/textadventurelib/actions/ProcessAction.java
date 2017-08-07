package textadventurelib.actions;

import java.io.IOException;
import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import textadventurelib.core.ExecutionParameters;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ProcessAction implements IAction {

	private String[] _data;
	private String _process;
	private Runtime _runTime;
	private boolean _block;
	
	/**
	 * 
	 * @param process The process to run.
	 */
	public ProcessAction(String process) {
		this(process, new String[0]);
	}
	
	/**
	 * 
	 * @param process The process to run.
	 * @param data The args to run with this process.
	 */
	public ProcessAction(String process, String[] data) {
		this(process, data, Runtime.getRuntime());
	}
	
	/**
	 * 
	 * @param process The process to run.
	 * @param data The args to run with this process.
	 * @param runner The Runtime for this environment.
	 */
	public ProcessAction(String process, String[] data, Runtime runner) {
		this(process, data, runner, false);
	}
	
	/**
	 * 
	 * @param process The process to run
	 * @param data The args to run with this process.
	 * @param runner The Runtime for this environment.
	 * @param blocking If the process should block the executing thread.
	 */
	public ProcessAction (String process, String[] data, Runtime runner, boolean blocking) {
		_process = process;
		_data = data;
		_runTime = runner;
		_block = blocking;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T data() {
		return (T)_data;
	}

	public <T> void data(T value) {
		_data = (String[])value;
	}

	/**
	 * 
	 * @return The blocking state.
	 */
	public boolean blocking() {
		return _block;
	}
	
	/**
	 * 
	 * @param value If the process should block the running thread.
	 */
	public void blocking(boolean value) {
		_block = value;
	}
	
	@Override
	public void execute(ExecutionParameters params) {
		Process proc;
		try {
			LogRunner.logger().log(Level.INFO, String.format("Attempting to launch: %s.", _process));
			String[] ex = generateProcessString(_process, _data);
			proc = _runTime.exec(ex);
			LogRunner.logger().log(Level.INFO, "Process launched.");
			if (!_block) return;
			LogRunner.logger().log(Level.INFO, "Process is blocking waiting on process to close.");
			proc.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private String[] generateProcessString(String process, String[] commands) {
		String[] retVal = new String[commands.length + 1];
		retVal[0] = process;

		for (int i = 0; i < commands.length; i++) {
			retVal[i+1] = commands[i];
		}
		
		return retVal;
	}
}