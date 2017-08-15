package textadventurelib.options;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ilusr.logrunner.LogRunner;
import textadventurelib.actions.ModifyPlayerAction;
import textadventurelib.core.ModificationType;
import textadventurelib.core.TriggerParameters;
import textadventurelib.triggers.ITrigger;

//TODO: Make this more general. Really this should be something like extra data option.
/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayerModificationOption extends Option{

	private Pattern pattern;
	private ModifyPlayerAction modPlayerAction;
	
	/**
	 * 
	 * @param trigger The trigger for this option.
	 * @param action The action for this option.
	 * @param extraDataMatch The extra data matching regex.
	 */
	public PlayerModificationOption(ITrigger trigger, ModifyPlayerAction action, String extraDataMatch) {
		super(trigger, action);
		modPlayerAction = action;
		pattern = Pattern.compile(extraDataMatch);
	}
	
	/**
	 * 
	 * @param triggers The triggers for this option.
	 * @param action The action for this option.
	 * @param extraDataMatch The extra data matching regex.
	 */
	public PlayerModificationOption(List<ITrigger> triggers, ModifyPlayerAction action, String extraDataMatch) {
		super(triggers, action);
		modPlayerAction = action;
		pattern = Pattern.compile(extraDataMatch);	
	}
	
	@Override
	public boolean shouldTrigger(TriggerParameters data) {
		System.out.println("Seeing if " + data.message() + " Should trigger");
		boolean execute = super.shouldTrigger(data);
		
		LogRunner.logger().info("Should execute " + execute);
		
		if (execute && modPlayerAction.data().modificationType() == ModificationType.Change) {
			String dataString = data.message();
			LogRunner.logger().info("Running matcher against " + pattern.pattern());
			Matcher matcher = pattern.matcher(dataString);
			if (matcher.find()) {
				LogRunner.logger().info("Setting data to " + matcher.group(1));
				modPlayerAction.data().args().data(matcher.group(1));
			}
		}
		
		return execute;
	}
}
