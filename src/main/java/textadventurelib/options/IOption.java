package textadventurelib.options;

import java.util.List;

import textadventurelib.actions.IAction;
import textadventurelib.core.TriggerParameters;
import textadventurelib.triggers.ITrigger;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface IOption {
	/**
	 * 
	 * @return The triggers associated with this option.
	 */
	List<ITrigger> triggers();
	/**
	 * 
	 * @param value The new value of the trigger.
	 */
	void triggers(List<ITrigger> value);
	/**
	 * 
	 * @param data The data to test against the triggers.
	 * @return If this option should trigger.
	 */
	boolean shouldTrigger(TriggerParameters data);
	/**
	 * 
	 * @return The action associated with this option.
	 */
	IAction action();
	/**
	 * 
	 * @param value The new value for this action.
	 */
	void action(IAction value);
}