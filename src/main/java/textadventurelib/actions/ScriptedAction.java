package textadventurelib.actions;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import ilusr.logrunner.LogRunner;
import textadventurelib.core.ExecutionParameters;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ScriptedAction implements IAction {

	private final String ENGINE_NAME = "JavaScript";
	private final ScriptEngine engine;
	
	private String script;
	private boolean scriptLoaded;
	
	/**
	 * Creates a scripted action.
	 */
	public ScriptedAction() {
		this(new String());
	}
	
	/**
	 * 
	 * @param script Script to run in action.
	 */
	public ScriptedAction(String script) {
		this.script = script;
		
		ScriptEngineManager manager = new ScriptEngineManager();
		engine = manager.getEngineByName(ENGINE_NAME);
		
		loadScript();
	}
	
	/**
	 * 
	 * @return The script to run on execution.
	 */
	public String getScript() {
		return script;
	}
	
	/**
	 * 
	 * @param script The new script to run on execution.
	 */
	public void setScript(String script) {
		this.script = script;
		loadScript();
	}
	
	private void loadScript() {
		if (script == null || script.isEmpty()) {
			scriptLoaded = false;
			return;
		}
		
		try {
			engine.eval(script);
			scriptLoaded = true;
		} catch (ScriptException e) {
			scriptLoaded = false;
			LogRunner.logger().severe(e);
		}
	}
	
	@Override
	public void execute(ExecutionParameters params) {
		if (!scriptLoaded) {
			LogRunner.logger().warning("No Script has been loaded returning.");
			return;
		}
		
		Invocable invoke = (Invocable)engine;
		
		try {
			invoke.invokeFunction("execute", params);
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}

}
