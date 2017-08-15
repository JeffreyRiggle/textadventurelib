package textadventurelib.triggers;

import java.util.List;

import ilusr.logrunner.LogRunner;
import textadventurelib.core.TriggerParameters;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class MultiPartTrigger implements ITrigger{

	private List<ITrigger> triggers;
	
	/**
	 * 
	 * @param triggers A List of @see ITrigger to base this multi trigger off of.
	 */
	public MultiPartTrigger(List<ITrigger> triggers) {
		this.triggers = triggers;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> void condition(T value) {
		if (value instanceof List) {
			triggers = (List<ITrigger>)value;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T condition() {
		return (T)triggers;
	}

	@Override
	public boolean shouldFire(TriggerParameters data) {
		boolean retVal = true;
		
		if (triggers.size() == 0) {
			LogRunner.logger().warning("Returning false since there are no triggers.");
			retVal = false;
		}
		
		for (ITrigger trigger : triggers) {
			if (!trigger.shouldFire(data)) {
				LogRunner.logger().finest("Trigger condition did not pass not triggering.");
				retVal = false;
				break;
			}
		}
		
		return retVal;
	}
}
