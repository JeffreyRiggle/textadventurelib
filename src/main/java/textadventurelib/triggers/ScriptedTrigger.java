package textadventurelib.triggers;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import ilusr.logrunner.LogRunner;
import textadventurelib.core.TriggerParameters;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ScriptedTrigger implements ITrigger {

	private final String ENGINE_NAME = "JavaScript";
	private final ScriptEngine engine;
	
	private String script;
	private boolean scriptLoaded;
	
	/**
	 * Creates a Scripted trigger with no script.
	 */
	public ScriptedTrigger() {
		this(new String());
	}
	
	/**
	 * Creates a Script trigger with a script.
	 * 
	 * @param script The javascript content to run.
	 */
	public ScriptedTrigger(String script) {
		this.script = script;
		ScriptEngineManager manager = new ScriptEngineManager();
		engine = manager.getEngineByName(ENGINE_NAME);
		
		loadScript();
	}
	
	@Override
	public <T> void condition(T value) {
		script = (String)value;
		loadScript();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T condition() {
		return (T)script;
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
	public boolean shouldFire(TriggerParameters data) {
		if (!scriptLoaded) {
			LogRunner.logger().warning("No Script has been loaded returning false.");
			return false;
		}
		
		Invocable invoke = (Invocable)engine;
		
		try {
			return (Boolean)invoke.invokeFunction("shouldFire", data);
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
		
		return false;
	}
}
