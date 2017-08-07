package textadventurelib.persistence.player;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.XmlConfigurationObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class EquipPersistenceObject extends XmlConfigurationObject{

	private final static String EQUIP_NAME = "Equiptable";
	
	private ItemPersistenceObject item;
	private BodyPartPersistenceObject bodyPart;
	
	/**
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public EquipPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(EQUIP_NAME);
		
		item = new ItemPersistenceObject();
		bodyPart = new BodyPartPersistenceObject();
	}
	
	/**
	 * 
	 * @param item The item to equip.
	 */
	public void item(ItemPersistenceObject item) {
		this.item = item;
	}
	
	/**
	 * 
	 * @return The equipped item.
	 */
	public ItemPersistenceObject item() {
		return item;
	}
	
	/**
	 * 
	 * @param bodyPart The new body part to equip to.
	 */
	public void bodyPart(BodyPartPersistenceObject bodyPart) {
		this.bodyPart = bodyPart;
	}
	
	/**
	 * 
	 * @return The body part to equip to.
	 */
	public BodyPartPersistenceObject bodyPart() {
		return bodyPart;
	}
	
	/**
	 * Prepares this object to be persisted.
	 */
	public void prepareXml() {
		super.clearChildren();
		super.clearConfigurationProperties();
		
		item.prepareXml();
		super.addChild(item);
		
		bodyPart.prepareXml();
		super.addChild(bodyPart);
	}
	
	@Override
	public String toString() {
		return String.format("Body Part: %s, Item: %s", bodyPart.objectName(), item.itemName());
	}
}
