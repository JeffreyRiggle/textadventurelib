package textadventurelib.triggers;

import ilusr.logrunner.LogRunner;
import textadventurelib.core.TriggerParameters;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TextTrigger implements ITrigger {

	private String expression;
	
	/**
	 * 
	 * @param triggerCondition The text trigger (regex format).
	 */
	public TextTrigger(String triggerCondition) {
		expression = triggerCondition;
	}

	@Override
	public <T> void condition(T value) {
		expression = (String)value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T condition() {
		return (T)expression;
	}

	@Override
	public boolean shouldFire(TriggerParameters data) {
		String input = data.message();
		LogRunner.logger().info(String.format("Seeing if %s Matches %s", data.message(), expression));
		return input.matches(expression);
	}
}