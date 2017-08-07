package textadventurelib.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.ConfigurationProperty;
import ilusr.persistencelib.configuration.XmlConfigurationObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
abstract class ParameterizedPersistenceObject extends XmlConfigurationObject{

	private String TYPE_NAME = "type";
	private static final String PARAMTER_NAME = "Parameters";
	private List<XmlConfigurationObject> parameters;
	private ConfigurationProperty type;
	
	/**
	 * 
	 * @param persistenceObject The name of the object.
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public ParameterizedPersistenceObject(String persistenceObject) throws TransformerConfigurationException, ParserConfigurationException {
		super(persistenceObject);
		type = new ConfigurationProperty(TYPE_NAME, "");
		parameters = new ArrayList<XmlConfigurationObject>();
	}
	
	/**
	 * 
	 * @param type The new type of action.
	 */
	protected void type(String type) {
		this.type.value(type);
	}
	
	/**
	 * 
	 * @return The current actions type.
	 */
	protected String type() {
		return type.value();
	}
	
	/**
	 * 
	 * @param parameter A list of @see XmlConfigurationObject to add under the parameters section.
	 */
	protected void addParameter(XmlConfigurationObject parameter) {
		parameters.add(parameter);
	}
	
	/**
	 * 
	 * @param parameter A list of @see XmlConfigurationObject to remove from the parameters section.
	 */
	protected void removeParameter(XmlConfigurationObject parameter) {
		parameters.remove(parameter);
	}
	
	/**
	 * Removes all parameters from the parameters section.
	 */
	protected void clearParameters() {
		parameters.clear();
	}
	
	/**
	 * 
	 * @return A list of @see XmlConfigurationObject representing the parameters section of the xml.
	 */
	protected List<XmlConfigurationObject> parameters() {
		return parameters;
	}
	
	/**
	 * This must be called before saving the xml otherwise this object will not be
	 * saved correctly to the xml file.
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public void prepareXml() throws TransformerConfigurationException, ParserConfigurationException {
		this.clearChildren();
		this.clearConfigurationProperties();
		
		this.addConfigurationProperty(type);
		this.addChild(parameterObject());
	}
	
	private XmlConfigurationObject parameterObject() throws TransformerConfigurationException, ParserConfigurationException {
		XmlConfigurationObject retVal = new XmlConfigurationObject(PARAMTER_NAME);
		
		for (XmlConfigurationObject obj : parameters) {
			retVal.addChild(obj);
		}
		
		return retVal;
	}
}
