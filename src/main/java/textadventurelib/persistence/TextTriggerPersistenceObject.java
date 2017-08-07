package textadventurelib.persistence;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.triggers.ITrigger;
import textadventurelib.triggers.TextTrigger;

/**
 * 
 * @author Jeff Riggle
 * 
 * Currently creates an xml object in the following format.
 * 
 * <pre>
 * {@code
 * <Trigger type="Text>
 * 		<Parameters>
 * 				<Text>Text to trigger<Text>
 * 				<CaseSensitive>true/false<CaseSensitive>
 * 				<MatchType>Exact/Pre/Post/Contains<MatchType>
 * 		</Parameters>
 * </Trigger>
 * }
 * </pre>
 * 
 */
public class TextTriggerPersistenceObject extends TriggerPersistenceObject{

	private static final String TEXT_TYPE = "Text";
	private static final String TEXT_NAME = "Text";
	private static final String CASE_NAME = "CaseSensitive";
	private static final String MATCH_NAME = "MatchType";
	
	private XmlConfigurationObject text;
	private XmlConfigurationObject caseSensitive;
	private XmlConfigurationObject matchType;
	
	/**
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public TextTriggerPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(TEXT_TYPE);
		
		text = new XmlConfigurationObject(TEXT_NAME, new String());
		caseSensitive = new XmlConfigurationObject(CASE_NAME, false);
		matchType = new XmlConfigurationObject(MATCH_NAME, MatchType.Exact);
	}
	
	/**
	 * 
	 * @param text The new Text to trigger with.
	 */
	public void text(String text) {
		super.removeParameter(this.text);
		this.text.value(text);
		super.addParameter(this.text);
	}
	
	/**
	 * 
	 * @return The text to trigger off of.
	 */
	public String text() {
		return text.value();
	}
	
	/**
	 * 
	 * @param ignoreCase If the trigger should be case sensitive or not.
	 */
	public void caseSensitive(boolean ignoreCase) {
		super.removeParameter(caseSensitive);
		caseSensitive.value(ignoreCase);
		super.addParameter(caseSensitive);
	}
	
	/**
	 * 
	 * @return If the trigger is case sensitive.
	 */
	public boolean caseSensitive() {
		return caseSensitive.value();
	}
	
	/**
	 * 
	 * @param type The new @see MatchType for this trigger.
	 */
	public void matchType(MatchType type) {
		super.removeParameter(matchType);
		matchType.value(type);
		super.addParameter(matchType);
	}
	
	/**
	 * 
	 * @return The @see MatchType assoicated with this trigger.
	 */
	public MatchType matchType() {
		return matchType.value();
	}
	
	@Override
	public void prepareXml() throws TransformerConfigurationException, ParserConfigurationException {
		super.prepareXml();
	}
	
	@Override
	public ITrigger convertToTrigger() {
		TextTrigger trigger = new TextTrigger(toRegEx());
		return trigger;
	}
	
	public void convertFromPersistence(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			switch (cChild.name()) {
				case "Parameters":
					convert(cChild);
					break;
				default:
					break;
			}
		}
	}
	
	private void convert(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			switch (cChild.name()) {
				case TEXT_NAME:
					text(cChild.<String>value());
					break;
				case MATCH_NAME:
					matchType(convertToMatchType(cChild.<String>value()));
					break;
				case CASE_NAME:
					caseSensitive(cChild.<Boolean>value());
					break;
				default:
					break;
			}
		}
	}
	
	private MatchType convertToMatchType(String type) {
		switch (type) {
			case "Contains":
				return MatchType.Contains;
			case "Exact":
				return MatchType.Exact;
			case "Prefix":
				return MatchType.Prefix;
			case "Postfix":
				return MatchType.Postfix;
			case "NotContains":
				return MatchType.NotContains;
			default:
				return MatchType.Exact;
		}
	}
	
	/**
	 * Takes this persistence object and turns it into a java regex string.
	 * 
	 * Example:
	 * Text = test, CaseSensitive = false, MatchType = Contains 
	 * would return (?i).*test.*
	 * 
	 * @return The regex formatted String.
	 */
	public String toRegEx() {
		String retVal = new String();
		
		if (caseSensitive.value() != null && !caseSensitive()) {
			retVal += "(?i)";
		}
		
		switch (matchType()) {
			case Exact:
				retVal += text();
				break;
			case Prefix:
				retVal += text() + ".*";
				break;
			case Postfix:
				retVal += ".*" + text();
				break;
			case Contains:
				retVal += ".*" + text() + ".*";
				break;
			case NotContains:
				if (caseSensitive.value() != null && !caseSensitive()) {
					retVal = "^(?!" + text() + "$).*";
				} else {
					retVal = "^(" + text() + "$).*";
				}
				break;
		}
		
		return retVal;
	}
}
