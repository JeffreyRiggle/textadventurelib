package textadventurelib.persistence;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.actions.CompletionAction;
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
 * <Action type="Completion">
 *  	<Parameters>
 *  		<CompletionData>Data for completion</CompletionData>
 *  	</Parameters>
 *  </Action>
 * }
 * </pre>
 */
public class CompletionActionPersistence extends ActionPersistenceObject{

	private static final String COMPLETION_TYPE = "Completion";
	private static final String COMPLETION_DATA_NAME = "CompletionData";
	private XmlConfigurationObject completionData;
	
	/**
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public CompletionActionPersistence() throws TransformerConfigurationException, ParserConfigurationException {
		super(COMPLETION_TYPE);
		completionData = new XmlConfigurationObject(COMPLETION_DATA_NAME);
		super.addParameter(completionData);
	}
	
	/**
	 * 
	 * @param data The new completion Data to associate with this action.
	 */
	public <T> void completionData(T data) {
		super.removeParameter(completionData);
		completionData.value(data);
		super.addParameter(completionData);
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @return The completion data associated with this action.
	 */
	public <T> T completionData() {
		return (T)completionData.value();
	}
	
	/**
	 * Prepares the xml to be saved.
	 */
	public void prepareXml() throws TransformerConfigurationException, ParserConfigurationException {
		super.prepareXml();
	}
	
	@Override
	public IAction convertToAction() {
		CompletionAction action = new CompletionAction();
		action.data(completionData());
		return action;
	}
	
	public void persistFromPersistence(XmlConfigurationObject obj) {
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
			
			switch(cChild.name()) {
				case COMPLETION_DATA_NAME:
					completionData(cChild.value());
					break;
				default:
					break;
			}
		}
	}
}
