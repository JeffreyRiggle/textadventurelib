package textadventurelib.persistence;

import ilusr.persistencelib.configuration.ConfigurationProperty;
import ilusr.persistencelib.configuration.XmlConfigurationObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TimerPersistenceConverter {

	/**
	 * 
	 * @param config The object to convert.
	 * @return
	 */
	public TimerPersistenceObject convert(XmlConfigurationObject config) {
		TimerPersistenceObject retVal = null;
		XmlConfigurationObject cChild = (XmlConfigurationObject)config;
		
		String type = getType(cChild);
		
		if (type == null) {
			return retVal;
		}
		
		try {
			if (type.equalsIgnoreCase("Completion")) {
				CompletionTimerPersistenceObject timer = new CompletionTimerPersistenceObject();
				timer.convertFromPersistence(cChild);
				return timer;
			}
		} catch (Exception e) {
			//TODO What to do here.
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
