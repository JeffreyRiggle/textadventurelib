package textadventurelib.triggers;

import textadventurelib.core.TriggerParameters;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface ITrigger {
	/**
	 * 
	 * @param value The new condition for this trigger.
	 */
	<T> void condition(T value);
	/**
	 * 
	 * @return The trigger condition.
	 */
	<T> T condition();
	/**
	 * 
	 * @param data The data to compare against
	 * @return If the trigger should fire.
	 */
	boolean shouldFire(TriggerParameters data);
}