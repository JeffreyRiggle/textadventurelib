package textadventurelib.persistence.player;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.ConfigurationProperty;
import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class NamedPersistenceObject extends XmlConfigurationObject{

	private static final String NAMED_OBJECT_NAME = "NamedObject";
	private static final String TYPE_NAME = "type";
	private static final String NAME_NAME = "Name";
	private static final String DESCRIPTION_NAME = "Description";
	private static final String VALUE_NAME = "Value";
	
	private ConfigurationProperty type;
	private XmlConfigurationObject name;
	private XmlConfigurationObject description;
	private XmlConfigurationObject value;
	
	/**
	 * 
	 * @param type The type of persistence object.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public NamedPersistenceObject(String type) throws TransformerConfigurationException, ParserConfigurationException {
		super(NAMED_OBJECT_NAME);
		
		this.type = new ConfigurationProperty(TYPE_NAME, type);
		this.name = new XmlConfigurationObject(NAME_NAME);
		this.description = new XmlConfigurationObject(DESCRIPTION_NAME);
		this.value = new XmlConfigurationObject(VALUE_NAME);
	}
	
	/**
	 * 
	 * @return The type of persistence object.
	 */
	public String type() {
		return type.value();
	}
	
	/**
	 * 
	 * @param name The new name for the persistence object.
	 */
	public void objectName(String name) {
		this.name.value(name);
	}
	
	/**
	 * 
	 * @return The name of the persistence object.
	 */
	public String objectName() {
		return name.value();
	}
	
	/**
	 * 
	 * @param description The new description of the persistence object.
	 */
	public void description(String description) {
		this.description.value(description);
	}
	
	/**
	 * 
	 * @return The description of the persistence object.
	 */
	public String description() {
		return description.value();
	}
	
	/**
	 * 
	 * @param value The new value for this object.
	 */
	public <T> void objectValue(T value) {
		this.value.value(value);
	}
	
	/**
	 * 
	 * @return The value for this object.
	 */
	@SuppressWarnings("unchecked")
	public <T> T objectValue() {
		return (T)value.value();
	}
	
	/**
	 * 
	 * @return The type of the value.
	 */
	public String valueType() {
		return value.getValueType();
	}
	
	/**
	 * Prepares this object to be persisted.
	 */
	public void prepareXml() {
		super.clearChildren();
		super.clearConfigurationProperties();
		
		super.addConfigurationProperty(type);
		super.addChild(name);
		super.addChild(description);
		super.addChild(value);
	}
	
	/**
	 * 
	 * @param persist The object to convert.
	 */
	public void convertFromPersistence(XmlConfigurationObject persist) {
		for (PersistXml obj : persist.children()) {
			XmlConfigurationObject cobj = ((XmlConfigurationObject)obj);
			
			switch (cobj.name()) {
				case NAME_NAME:
					objectName(cobj.<String>value());
					break;
				case DESCRIPTION_NAME:
					description(cobj.<String>value());
					break;
				case VALUE_NAME:
					objectValue(cobj.value());
					break;
				default:
					//TODO: Should something be done in this case?
					break;
			}
		}
	}
	
	@Override
	public String toString() {
		return String.format("%s: %s", name.value(), value.value());
	}
}
