package textadventurelib.persistence;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import textadventurelib.triggers.ITrigger;

/**
 * 
 * @author Jeff Riggle
 *
 */
public abstract class TriggerPersistenceObject extends ParameterizedPersistenceObject {
	private final static String TRIGGER_NAME = "Trigger";
	
	/**
	 * 
	 * @param type The type of Trigger (ex: Player, Text).
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public TriggerPersistenceObject(String type) throws TransformerConfigurationException, ParserConfigurationException {
		super(TRIGGER_NAME);
		super.type(type);
	}
	
	public String type() {
		return super.type();
	}
	
	public abstract ITrigger convertToTrigger();
}