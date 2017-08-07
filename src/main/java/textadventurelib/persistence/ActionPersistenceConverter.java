package textadventurelib.persistence;

import ilusr.persistencelib.configuration.ConfigurationProperty;
import ilusr.persistencelib.configuration.XmlConfigurationObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ActionPersistenceConverter {

	/**
	 * 
	 * @param config The config to convert.
	 * @return The converted action.
	 */
	public ActionPersistenceObject convert(XmlConfigurationObject config) {
		ActionPersistenceObject retVal = null;
		String prop = getType(config);
		
		if (prop == null) {
			return retVal;
		}
		
		try {
			switch (prop) {
				case "AppendText":
					AppendTextActionPersistence aAction = new AppendTextActionPersistence();
					aAction.convertFromPersistence(config);
					return aAction;
				case "Completion":
					CompletionActionPersistence cAction = new CompletionActionPersistence();
					cAction.persistFromPersistence(config);
					return cAction;
				case "Execute":
					ExecutionActionPersistence eAction = new ExecutionActionPersistence();
					eAction.convertFromPersistence(config);
					return eAction;
				case "ModifyPlayer":
					ModifyPlayerActionPersistence mAction = new ModifyPlayerActionPersistence();
					mAction.convertFromPersistence(config);
					return mAction;
				case "Script":
					ScriptedActionPersistenceObject sAction = new ScriptedActionPersistenceObject();
					sAction.convertFromPersistence(config);
					return sAction;
				case "Finish":
					FinishActionPersistenceObject fAction = new FinishActionPersistenceObject();
					return fAction;
				case "Save":
					SaveActionPersistenceObject svAction = new SaveActionPersistenceObject();
					svAction.convertFromPersistence(config);
					return svAction;
				default:
					//TODO: What to do here.
					break;
			}
		} catch (Exception e) {
			//TODO: what to do here.
		}
		return retVal;
	}
	
	private String getType(XmlConfigurationObject config) {
		for (ConfigurationProperty conf : config.configurationProperties()) {
			if (!conf.name().equalsIgnoreCase("type")) continue;
			
			return conf.value();
		}
		
		return null;
	}
}
