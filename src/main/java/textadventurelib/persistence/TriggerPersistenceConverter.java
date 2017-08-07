package textadventurelib.persistence;

import ilusr.persistencelib.configuration.ConfigurationProperty;
import ilusr.persistencelib.configuration.XmlConfigurationObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TriggerPersistenceConverter {

	/**
	 * 
	 * @param config The object to convert.
	 * @return The converted trigger.
	 */
	public TriggerPersistenceObject convert(XmlConfigurationObject config) {
		TriggerPersistenceObject retVal = null;
		String property = getType(config);
		
		if (property == null) {
			return retVal;
		}
		
		try {
			switch (property) {
				case "Text":
					TextTriggerPersistenceObject textTrigger = new TextTriggerPersistenceObject();
					textTrigger.convertFromPersistence(config);
					return textTrigger;
				case "Player":
					PlayerTriggerPersistenceObject playerTrigger = new PlayerTriggerPersistenceObject();
					playerTrigger.convertFromPersistence(config);
					return playerTrigger;
				case "MultiPart":
					MultiPartTriggerPersistenceObject multiTrigger = new MultiPartTriggerPersistenceObject();
					multiTrigger.convertFromPersistence(config);
					return multiTrigger;
				case "Script":
					ScriptedTriggerPersistenceObject scriptedTrigger = new ScriptedTriggerPersistenceObject();
					scriptedTrigger.convertFromPersistence(config);
					return scriptedTrigger;
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
		for (ConfigurationProperty prop : config.configurationProperties()) {
			if (!prop.name().equalsIgnoreCase("type")) continue;
			
			return prop.value();
		}
		
		return null;
	}
}
