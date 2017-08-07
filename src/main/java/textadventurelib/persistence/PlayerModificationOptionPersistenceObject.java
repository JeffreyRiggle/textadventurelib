package textadventurelib.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.ConfigurationProperty;
import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.actions.ModifyPlayerAction;
import textadventurelib.options.PlayerModificationOption;
import textadventurelib.triggers.ITrigger;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayerModificationOptionPersistenceObject extends OptionPersistenceObject{

	private final String EXTRA_DATA = "ExtraDataMatcher";
	private final String TYPE = "type";
	private final String PLAYER_MODIFICATION_OPTION = "PlayerModificationOption";
	private XmlConfigurationObject extraDataMatcher;
	private ConfigurationProperty optionType;
	
	/**
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public PlayerModificationOptionPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super();
		extraDataMatcher = new XmlConfigurationObject(EXTRA_DATA);
		optionType = new ConfigurationProperty(TYPE, PLAYER_MODIFICATION_OPTION);
	}
	
	/**
	 * 
	 * @param match The new extra data to match.
	 */
	public void extraDataMatch(String match) {
		extraDataMatcher.value(match);
	}
	
	/**
	 * 
	 * @return The extra data to match.
	 */
	public String extraDataMatch() {
		return extraDataMatcher.<String>value();
	}
	
	@Override
	public void prepareXml() throws TransformerConfigurationException, ParserConfigurationException {
		super.prepareXml();
		super.addChild(extraDataMatcher);
		super.addConfigurationProperty(optionType);
	}
	
	public void convertFromPersistence(XmlConfigurationObject obj) {
		super.convertFromPersistence(obj);
		
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			if (!cChild.name().equalsIgnoreCase(EXTRA_DATA)) continue;
			
			extraDataMatch(cChild.<String>value());
		}
	}
	
	public PlayerModificationOption convertToOption() {
		List<ITrigger> triggers = new ArrayList<ITrigger>();
		
		for (TriggerPersistenceObject trigger : triggers()) {
			triggers.add(trigger.convertToTrigger());
		}
		
		PlayerModificationOption option = new PlayerModificationOption(triggers, (ModifyPlayerAction)action().convertToAction(), extraDataMatch());
		return option;
	}
}
