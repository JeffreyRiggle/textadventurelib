package textadventurelib.persistence;

import java.util.Base64;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.triggers.ITrigger;
import textadventurelib.triggers.ScriptedTrigger;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ScriptedTriggerPersistenceObject extends TriggerPersistenceObject {

	private static final String SCRIPT_TYPE = "Script";
	private static final String SCRIPT = "Script";
	
	private XmlConfigurationObject script;
	private boolean encoded;
	
	/**
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public ScriptedTriggerPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(SCRIPT_TYPE);
		
		script = new XmlConfigurationObject(SCRIPT);
	}

	/**
	 * 
	 * @param script The new JavaScript to use to trigger.
	 */
	public void setScript(String script) {
		setScriptImpl(script, false);
	}
	
	private void setScriptImpl(String script, boolean encode) {
		if (encode && encoded) {
			return;
		}
		
		String val;
		if (encode) {
			val = Base64.getEncoder().encodeToString(script.getBytes());
			encoded = true;
		} else {
			val = script;
			encoded = false;
		}
		
		super.removeParameter(this.script);
		this.script.value(val);
		super.addParameter(this.script);
	}
	
	/**
	 * 
	 * @return The script to trigger off of.
	 */
	public String getScript() {
		return script.value();
	}
	
	@Override
	public void prepareXml() throws TransformerConfigurationException, ParserConfigurationException {
		setScriptImpl(getScript(), true);
		super.prepareXml();
	}
	
	@Override
	public ITrigger convertToTrigger() {
		ScriptedTrigger trigger = new ScriptedTrigger(this.script.value());
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
				case SCRIPT:
					byte[] bytes = Base64.getDecoder().decode(cChild.<String>value());
					setScript(new String(bytes));
					break;
				default:
					break;
			}
		}
	}

}
