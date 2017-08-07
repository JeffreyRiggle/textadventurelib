package textadventurelib.persistence.player;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import playerlib.items.IItem;
import playerlib.items.IProperty;
import playerlib.items.Item;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ItemPersistenceObject extends XmlConfigurationObject {

	private static final String ITEM_NAME = "Item";
	private static final String NAME_NAME = "Name";
	private static final String DESCRIPTION_NAME = "Description";
	private static final String CONTENT_NAME = "Content";
	private static final String PROPERTY_NAME = "Properties";

	private XmlConfigurationObject name;
	private XmlConfigurationObject description;
	private XmlConfigurationObject content;
	private XmlConfigurationObject properties;
	
	/**
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public ItemPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(ITEM_NAME);
		
		name = new XmlConfigurationObject(NAME_NAME);
		description = new XmlConfigurationObject(DESCRIPTION_NAME);
		content = new XmlConfigurationObject(CONTENT_NAME);
		properties = new XmlConfigurationObject(PROPERTY_NAME);
	}
	
	/**
	 * 
	 * @param item The item to copy from.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public ItemPersistenceObject(IItem item) throws TransformerConfigurationException, ParserConfigurationException {
		this();
		createFromItem(item);
	}
	
	/**
	 * 
	 * @param item The persistence object to clone.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public ItemPersistenceObject(ItemPersistenceObject item) throws TransformerConfigurationException, ParserConfigurationException {
		this();
		itemName(item.itemName());
		itemDescription(item.itemDescription());
		
		for (PropertyPersistenceObject prop : item.properties()) {
			addProperty(prop);
		}
	}
	
	/**
	 * 
	 * @param name The new name for the item.
	 */
	public void itemName(String name) {
		this.name.value(name);
	}
	
	/**
	 * 
	 * @return The name of the item.
	 */
	public String itemName() {
		return name.value();
	}
	
	/**
	 * 
	 * @param description The new description of the item.
	 */
	public void itemDescription(String description) {
		this.description.value(description);
	}
	
	/**
	 * 
	 * @return The description of the item.
	 */
	public String itemDescription() {
		return description.value();
	}
	
	/**
	 * 
	 * @param content The new content for the item.
	 */
	public <T> void itemContent(T content) {
		this.content.value(content);
	}
	
	/**
	 * 
	 * @return The content for the item.
	 */
	@SuppressWarnings("unchecked")
	public <T> T itemContent() {
		return (T)content.value();
	}
	
	/**
	 * 
	 * @param property The property to add to the item.
	 */
	public void addProperty(PropertyPersistenceObject property) {
		properties.addChild(property);
	}
	
	/**
	 * 
	 * @param property The property to remove from the item.
	 */
	public void removeProperty(PropertyPersistenceObject property) {
		properties.removeChild(property);
	}
	
	/**
	 * Removes all properties from the item.
	 */
	public void clearProperties() {
		properties.clearChildren();
	}
	
	/**
	 * 
	 * @return The properties associated with this item.
	 */
	public List<PropertyPersistenceObject> properties() {
		List<PropertyPersistenceObject> retVal = new ArrayList<PropertyPersistenceObject>();
		
		for(PersistXml prop : properties.children()) {
			retVal.add((PropertyPersistenceObject)prop);
		}
		
		return retVal;
	}
	
	/**
	 * Prepares this object to be persisted.
	 */
	public void prepareXml() {
		super.clearConfigurationProperties();
		super.clearChildren();
		
		super.addChild(name);
		super.addChild(description);
		super.addChild(content);
		
		for (PersistXml prop : properties.children()) {
			((PropertyPersistenceObject)prop).prepareXml();
		}
		
		super.addChild(properties);
	}
	
	/**
	 * 
	 * @return The converted item.
	 */
	public IItem convertToItem() {
		IItem item = new Item(itemName());
		item.description(itemDescription());
		item.content(itemContent());
		
		for (PropertyPersistenceObject prop : properties()) {
			item.addProperty(prop.convertToProperty());
		}
		
		return item;
	}
	
	/**
	 * 
	 * @param item The item to copy from.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public void createFromItem(IItem item) throws TransformerConfigurationException, ParserConfigurationException {
		itemName(item.name());
		itemDescription(item.description());
	
		for (IProperty prop : item.properties()) {
			addProperty(new PropertyPersistenceObject(prop));
		}
	}
	
	/**
	 * 
	 * @param obj The object to convert.
	 */
	public void convertFromPersistence(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			switch (cChild.name()) {
				case NAME_NAME:
					itemName(cChild.<String>value());
					break;
				case DESCRIPTION_NAME:
					itemDescription(cChild.<String>value());
					break;
				case CONTENT_NAME:
					itemContent(cChild.value());
					break;
				case PROPERTY_NAME:
					convertProperties(cChild);
					break;
				default:
					//TODO: what to do here.
					break;
			}
		}
	}
	
	private void convertProperties(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			try {
				XmlConfigurationObject cChild = (XmlConfigurationObject)child;
				PropertyPersistenceObject prop = new PropertyPersistenceObject();
				prop.convertFromPersistence(cChild);
				
				addProperty(prop);
			} catch (Exception e) {
				//TODO: what to do here.
			}
		}
	}
	
	@Override
	public String toString() {
		return itemName();
	}
}
