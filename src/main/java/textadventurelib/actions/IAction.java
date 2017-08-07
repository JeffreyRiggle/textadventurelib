package textadventurelib.actions;

import textadventurelib.core.ExecutionParameters;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface IAction {
	/**
	 * Execute this action.
	 */
	void execute(ExecutionParameters params);
}