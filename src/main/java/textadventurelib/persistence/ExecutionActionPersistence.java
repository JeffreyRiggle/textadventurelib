package textadventurelib.persistence;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.actions.IAction;
import textadventurelib.actions.ProcessAction;

/**
 * 
 * @author Jeff Riggle
 *
 *  Creates an XML node for the Append Text action. The current xml should look like
 * 	this:
 * 
 * <pre>
 * {@code
 *  <Action type="Execute">
 *  	<Parameters>
 *  		<Executable>The application to run</Executable>
 *  		<Blocking>If the application should block<Blocking>
 *  	</Parameters>
 *  </Action>
 *  }
 *  </pre>
 */
public class ExecutionActionPersistence extends ActionPersistenceObject{

	private final static String EXE_TYPE = "Execute";
	private final static String EXECUTION_NAME = "Executable";
	private final static String BLOCKING_NAME = "Blocking";
	
	private XmlConfigurationObject execution;
	private XmlConfigurationObject blocking;
	
	/**
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public ExecutionActionPersistence() throws TransformerConfigurationException, ParserConfigurationException {
		super(EXE_TYPE);
		
		execution = new XmlConfigurationObject(EXECUTION_NAME, new String());
		blocking = new XmlConfigurationObject(BLOCKING_NAME, false);
	}
	
	/**
	 * 
	 * @param exe The new executable to associate with this action.
	 */
	public void executable(String exe) {
		super.removeParameter(execution);
		execution.value(exe);
		super.addParameter(execution);
	}
	
	/**
	 * 
	 * @return The executable associated with this action.
	 */
	public String executable() {
		return execution.value();
	}
	
	public void blocking(boolean block) {
		super.removeParameter(blocking);
		blocking.value(block);
		super.addParameter(blocking);
	}
	
	public boolean blocking() {
		return blocking.value();
	}
	
	@Override
	public void prepareXml() throws TransformerConfigurationException, ParserConfigurationException {
		super.prepareXml();
	}
	
	@Override
	public IAction convertToAction() {
		ProcessAction action = new ProcessAction(executable());
		action.blocking(blocking());
		return action;
	}
	
	public void convertFromPersistence(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			switch(cChild.name()) {
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
				case EXECUTION_NAME:
					executable(cChild.<String>value());
					break;
				case BLOCKING_NAME:
					blocking(cChild.<Boolean>value());
					break;
				default:
					break;
			}
		}
	}
}
