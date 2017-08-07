package textadventurelib.persistence.player;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import playerlib.attributes.Attribute;
import playerlib.attributes.IAttribute;


/**
 * 
 * @author Jeff Riggle
 *
 *	Creates Xml for the attribute object.
 */
public class AttributePersistenceObject extends NamedPersistenceObject{
	
	private final static String ATTRIBUTE_NAME = "Attribute";
	
	/**
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public AttributePersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(ATTRIBUTE_NAME);
	}
	
	/**
	 * 
	 * @param attribute The attribute to create from.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public AttributePersistenceObject(IAttribute attribute) throws TransformerConfigurationException, ParserConfigurationException {
		super(ATTRIBUTE_NAME);
		createFromAttribute(attribute);
	}

	/**
	 * 
	 * @return The converted value.
	 */
	public IAttribute convertToAttribute() {
		IAttribute attribute = new Attribute(super.objectName(), super.objectValue());
		attribute.description(super.description());
		return attribute;
	}
	
	/**
	 * 
	 * @param attribute The attribute to copy into this persistence object.
	 */
	public void createFromAttribute(IAttribute attribute) {
		super.objectName(attribute.name());
		super.description(attribute.description());
		super.objectValue(attribute.value());
	}
}
