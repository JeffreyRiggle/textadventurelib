package textadventurelib.options;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import ilusr.logrunner.LogRunner;
import textadventurelib.actions.IAction;
import textadventurelib.core.TriggerParameters;
import textadventurelib.triggers.ITrigger;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class Option implements IOption{

	private List<ITrigger> triggers;
	private IAction action;
	
	/**
	 * 
	 * @param trigger The trigger for this option.
	 * @param action The action for this option.
	 */
	public Option(ITrigger trigger, IAction action) {
		this(new ArrayList<ITrigger>(Arrays.asList(trigger)), action);
	}

	/**
	 * 
	 * @param triggers The triggers for this option.
	 * @param action The action for this option.
	 */
	public Option(List<ITrigger> triggers, IAction action) {
		this.triggers = triggers;
		this.action = action;
	}
	
	@Override
	public List<ITrigger> triggers() {
		return triggers;
	}

	@Override
	public void triggers(List<ITrigger> value) {
		triggers = value;
	}

	@Override
	public boolean shouldTrigger(TriggerParameters data) {
		LogRunner.logger().finest(String.format("Seeing if option should trigger with message: %s", data.message()));
		boolean execute = false;
		
		for (ITrigger trigger: triggers) {
			if (trigger.shouldFire(data)) {
				execute = true;
				break;
			}
		}
		
		return execute;
	}
	
	@Override
	public IAction action() {
		return action;
	}

	@Override
	public void action(IAction value) {
		action = value;
	}
}
