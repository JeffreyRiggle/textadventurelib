package textadventurelib.persistence.player;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import playerlib.items.IProperty;
import playerlib.items.Property;


/**
 * 
 * @author Jeff Riggle
 *
 *	Creates xml for a players items property.
 */
public class PropertyPersistenceObject extends NamedPersistenceObject{

	private final static String PROPERTY_NAME = "Property";
	
	/**
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public PropertyPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(PROPERTY_NAME);
	}
	
	/**
	 * 
	 * @param property The property to copy from.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public PropertyPersistenceObject(IProperty property) throws TransformerConfigurationException, ParserConfigurationException {
		super(PROPERTY_NAME);
		createFromProperty(property);
	}
	
	/**
	 * 
	 * @param property The property to copy from.
	 */
	public void createFromProperty(IProperty property) {
		super.objectName(property.name());
		super.objectValue(property.value());
		super.description(property.description());
	}
	
	/**
	 * 
	 * @return The converted property.
	 */
	public IProperty convertToProperty() {
		IProperty prop = new Property(super.objectName(), super.objectValue());
		prop.description(super.description());
		return prop;
	}
}
