package textadventurelib.persistence;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.actions.AppendTextAction;
import textadventurelib.actions.IAction;

/**
 * 
 * @author Jeff Riggle
 *
 * Creates an XML node for the Append Text action. The current xml should look like
 * this:
 * 
 * <pre>
 * {@code
 * <Action type="AppendText">
 * 	<Parameters>
 *		<AppendText>Text to append</AppendText>
 * 	</Parameters>
 * </Action>
 * }
 * </pre>
 */
public class AppendTextActionPersistence extends ActionPersistenceObject{
	
	private static final String APPEND_TEXT_TYPE = "AppendText";
	private XmlConfigurationObject appendText;
	
	/**
	 * Default Constructor.
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public AppendTextActionPersistence() throws TransformerConfigurationException, ParserConfigurationException {
		super(APPEND_TEXT_TYPE);
		appendText = new XmlConfigurationObject(APPEND_TEXT_TYPE, "");
		super.addParameter(appendText);
	}
	
	/**
	 * 
	 * @param text The text that should be appened with this action.
	 */
	public void appendText(String text) {
		super.removeParameter(appendText);
		appendText.value(text);
		super.addParameter(appendText);
	}
	
	/**
	 * 
	 * @return The String for the append text action.
	 */
	public String appendText() {
		return appendText.value();
	}
	
	@Override
	public void prepareXml() throws TransformerConfigurationException, ParserConfigurationException {
		super.prepareXml();
	}
	
	@Override
	public IAction convertToAction() {
		AppendTextAction action = new AppendTextAction(appendText());
		return action;
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
				case APPEND_TEXT_TYPE:
					appendText(cChild.<String>value());
					break;
				default:
					break;
			}
		}
	}
}