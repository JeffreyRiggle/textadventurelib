package textadventurelib.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.options.IOption;
import textadventurelib.options.Option;
import textadventurelib.triggers.ITrigger;

/**
 * 
 * @author Jeff Riggle
 *
 *	Creates Xml in the following format
 *	
 *	<pre>
 *	{@code
 *	<Option>
 *		<Triggers>
 *			<Trigger type="Text">
 *				<Text>Match String</Text>
 *			</Trigger>
 *		</Triggers>
 *		<Action type="Exe">
 *			<Executable>notepad.exe</Executable>
 *		</Action>
 *	<Option>
 *	}
 *	</pre>
 */
public class OptionPersistenceObject extends XmlConfigurationObject{

	private static final String OPTION_NAME = "Option";
	private static final String TRIGGER_NAME = "Triggers";
	private static final String ACTION_NAME = "Action";
	private final TriggerPersistenceConverter triggerConverter;
	private final ActionPersistenceConverter actionConverter;
	
	private XmlConfigurationObject triggers;
	private ActionPersistenceObject action;
	
	/**
	 * Ctor.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public OptionPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(OPTION_NAME);
		
		triggers = new XmlConfigurationObject(TRIGGER_NAME);
		triggerConverter = new TriggerPersistenceConverter();
		actionConverter = new ActionPersistenceConverter();
	}
	
	/**
	 * 
	 * @param trigger A @see TriggerPersistenceObject to add to this options triggers collection.
	 */
	public void addTrigger(TriggerPersistenceObject trigger) {
		triggers.addChild(trigger);
	}
	
	/**
	 * 
	 * @param trigger A @see TriggerPersistenceObject to remove from this options triggers collection.
	 */
	public void removeTrigger(TriggerPersistenceObject trigger) {
		triggers.removeChild(trigger);
	}
	
	/**
	 * Removes all @see TriggerPersistenceObject from this options triggers collection.
	 */
	public void clearTriggers() {
		triggers.clearChildren();
	}
	
	/**
	 * 
	 * @return A List of all of the @see TriggerPersistenceObject associated with this option.
	 */
	public List<TriggerPersistenceObject> triggers() {
		List<TriggerPersistenceObject> retVal = new ArrayList<TriggerPersistenceObject>();
		
		for (PersistXml obj : triggers.children()) {
			retVal.add((TriggerPersistenceObject)obj);
		}
		
		return retVal;
	}
	
	/**
	 * 
	 * @param action The @see ActionPersistenceObject to associated with this option.
	 */
	public void action(ActionPersistenceObject action) {
		this.action = action;
	}
	
	/**
	 * 
	 * @return The @see ActionPersistenceObject associated with this option.
	 */
	public ActionPersistenceObject action () {
		if (action == null) {
			return null;
		}
		
		return (ActionPersistenceObject)action;
	}
	
	/**
	 * Prepares the xml for saving. This must be called before save the xml to a file.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public void prepareXml() throws TransformerConfigurationException, ParserConfigurationException {
		super.clearChildren();
		super.configurationProperties();
		
		for (PersistXml obj : triggers.children()) {
			((TriggerPersistenceObject)obj).prepareXml();
		}
		
		((ActionPersistenceObject)action).prepareXml();
		
		super.addChild(triggers);
		super.addChild(action);
	}
	
	/**
	 * Converts this persistence object to a @see IOption
	 * @return A @see IOption that reperesents this peristence object.
	 */
	public IOption convertToOption() {
		List<ITrigger> triggers = new ArrayList<ITrigger>();
		
		for (TriggerPersistenceObject trigger : triggers()) {
			triggers.add(trigger.convertToTrigger());
		}
		
		IOption option = new Option(triggers, action().convertToAction());
		return option;
	}
	
	public void convertFromPersistence(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			switch (cChild.name()) {
				case TRIGGER_NAME:
					convertTriggers(cChild);
					break;
				case ACTION_NAME:
					convertAction(cChild);
					break;
				default:
					//TODO: What to do here.
					break;
			}
		}
	}
	
	private void convertTriggers(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			TriggerPersistenceObject trigger = triggerConverter.convert(cChild);
			if (trigger != null) {
				addTrigger(trigger);
			}
		}
	}
	
	private void convertAction(XmlConfigurationObject obj) {
		ActionPersistenceObject action = actionConverter.convert(obj);
		if (action != null) {
			action(action);
		}
	}
}
