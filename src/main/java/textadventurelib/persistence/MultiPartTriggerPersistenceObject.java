package textadventurelib.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.ConfigurationProperty;
import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.triggers.ITrigger;
import textadventurelib.triggers.MultiPartTrigger;

/**
 * 
 * @author Jeff Riggle
 *
 * Currently creates an xml object in the following format.
 * 
 * <pre>
 * {@code
 * <Trigger type="MultiPart">
 * 	<Parameters>
 * 		<Triggers>		
 * 			<Trigger type="Text">
 * 				<Parameters>
 * 						<Text>Text to trigger<Text>
 * 						<CaseSensitive>true/false<CaseSensitive>
 * 						<MatchType>Exact/Pre/Post/Contains<MatchType>
 * 				</Parameters>
 * 			</Trigger>
 * 			<Trigger type="Text">
 * 				<Parameters>
 * 						<Text>Text to trigger<Text>
 * 						<CaseSensitive>true/false<CaseSensitive>
 * 						<MatchType>Exact/Pre/Post/Contains<MatchType>
 * 				</Parameters>
 * 			</Trigger>
 * 		</Triggers>
 * 	</Parameters>
 * </Trigger>
 * }
 * </pre>
 */
public class MultiPartTriggerPersistenceObject extends TriggerPersistenceObject {
	
	private static final String MULTI_PART_TYPE = "MultiPart";
	
	private XmlConfigurationObject triggers;
	private final String TRIGGERS_NAME = "Triggers";
	
	/**
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public MultiPartTriggerPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(MULTI_PART_TYPE);
		
		triggers = new XmlConfigurationObject(TRIGGERS_NAME);
		triggers.value(new ArrayList<TriggerPersistenceObject>());
	}
	
	/**
	 * 
	 * @param triggers The new List of triggers for this multipart trigger.
	 */
	public void triggers(List<TriggerPersistenceObject> triggers) {
		super.removeParameter(this.triggers);
		this.triggers.value(triggers);
		super.addParameter(this.triggers);
	}
	
	/**
	 * 
	 * @param trigger The trigger to add to the list of triggers for this multipart trigger.
	 */
	public void addTrigger(TriggerPersistenceObject trigger) {
		super.removeParameter(triggers);
		List<TriggerPersistenceObject> params = triggers.value();
		params.add(trigger);
		triggers.value(params);
		super.addParameter(triggers);
	}
	
	/**
	 * 
	 * @param trigger The trigger to remove from this list of triggers for this multipart trigger.
	 */
	public void removeTrigger(TriggerPersistenceObject trigger) {
		super.removeParameter(triggers);
		List<TriggerPersistenceObject> params = triggers.value();
		params.remove(trigger);
		triggers.value(params);
		super.addParameter(triggers);
	}
	
	/**
	 * Clears all of the triggers on this multipart trigger.
	 */
	public void clearTriggers() {
		super.removeParameter(triggers);
		triggers.value(new ArrayList<TriggerPersistenceObject>());
		super.addParameter(triggers);
	}
	
	/**
	 * 
	 * @return The triggers associated with this multipart trigger.
	 */
	public List<TriggerPersistenceObject> triggers() {
		return triggers.value();
	}
	
	@Override
	public void prepareXml() throws TransformerConfigurationException, ParserConfigurationException {
		super.clearParameters();
		for (TriggerPersistenceObject persist : triggers()) {
			persist.prepareXml();
			super.addParameter(persist);
		}
		
		super.prepareXml();
	}
	
	//TODO: Document
	@Override
	public ITrigger convertToTrigger() {
		List<ITrigger> triggers = new ArrayList<ITrigger>();
		
		for (TriggerPersistenceObject persist : triggers()) {
			triggers.add(persist.convertToTrigger());
		}
		
		return new MultiPartTrigger(triggers);
	}
	
	public void convertFromPersistence(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			if (cChild.name().equalsIgnoreCase("Parameters")) {
				convert(cChild);
			}
		}
	}
	
	private void convert(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			if (!cChild.name().equalsIgnoreCase("Trigger")) continue;
			
			convertTrigger(cChild);
		}
	}
	
	private void convertTrigger(XmlConfigurationObject obj) {
		String type = null;
		for (ConfigurationProperty prop : obj.configurationProperties()) {
			if (!prop.name().equalsIgnoreCase("type")) continue;
			
			type = prop.value();
			break;
		}
		
		if (type == null) return;
		
		try {
			if (type.equalsIgnoreCase("Text")) {
				TextTriggerPersistenceObject trigger = new TextTriggerPersistenceObject();
				trigger.convertFromPersistence(obj);
				addTrigger(trigger);
			}
			
			if (type.equalsIgnoreCase("Player")) {
				PlayerTriggerPersistenceObject trigger = new PlayerTriggerPersistenceObject();
				trigger.convertFromPersistence(obj);
				addTrigger(trigger);
			}
			
			if (type.equalsIgnoreCase(MULTI_PART_TYPE)) {
				MultiPartTriggerPersistenceObject trigger = new MultiPartTriggerPersistenceObject();
				trigger.convertFromPersistence(obj);
				addTrigger(trigger);
			}
		} catch (Exception e) {
			//TODO What to do here.
		}
	}
}
