package textadventurelib.persistence.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import playerlib.equipment.Equipment;
import playerlib.equipment.IBodyPart;
import playerlib.equipment.IEquipment;
import playerlib.items.IItem;
import playerlib.player.IPlayer;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class EquipmentPersistenceObject extends XmlConfigurationObject{

	private static final String EQUIPMENT_NAME = "Equipment";
	
	private List<EquipPersistenceObject> equipted;
	
	/**
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public EquipmentPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(EQUIPMENT_NAME);
		
		equipted = new ArrayList<EquipPersistenceObject>();
	}
	
	/**
	 * 
	 * @param player The player to copy from.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public EquipmentPersistenceObject(IPlayer player) throws TransformerConfigurationException, ParserConfigurationException {
		this();
		createFromPlayer(player);
	}
	
	/**
	 * 
	 * @param item The item to equip.
	 * @param bodyPart The body part to equip to.
	 */
	public void equip(ItemPersistenceObject item, BodyPartPersistenceObject bodyPart) {
		try {
			EquipPersistenceObject equiptable = new EquipPersistenceObject();
			
			equiptable.item(item);
			equiptable.bodyPart(bodyPart);
			
			equipted.add(equiptable);
		} catch (Exception e) {
			//TODO: Should anything be done here?
		}
	}
	
	/**
	 * 
	 * @param bodyPart The body part to unequip.
	 */
	public void unequip(BodyPartPersistenceObject bodyPart) {
		for (EquipPersistenceObject equip : equipted) {
			if (!equip.bodyPart().equals(bodyPart)) continue;
			
			equipted.remove(equip);
			break;
		}
	}
	
	/**
	 * 
	 * @return The equipted items and their associated body parts.
	 */
	public Map<BodyPartPersistenceObject, ItemPersistenceObject> equipment() {
		Map<BodyPartPersistenceObject, ItemPersistenceObject> retVal = new HashMap<BodyPartPersistenceObject, ItemPersistenceObject>();
		
		for (EquipPersistenceObject equip : equipted) {
			retVal.put(equip.bodyPart(), equip.item());
		}
		
		return retVal;
	}
	
	/**
	 * Prepares this object to be persisted.
	 */
	public void prepareXml() {
		super.clearChildren();
		super.clearConfigurationProperties();
		
		for (EquipPersistenceObject equip : equipted) {
			equip.prepareXml();
			super.addChild(equip);
		}
	}
	
	/**
	 * 
	 * @return The converted value.
	 */
	public IEquipment convertToEquipment() {
		IEquipment equipment = new Equipment();
		
		for (EquipPersistenceObject equip : equipted) {
			IItem equipted = equip.item().convertToItem();
			IBodyPart bodyPart = equip.bodyPart().convertToBodyPart();
			
			equipment.equip(bodyPart, equipted);
		}
		
		return equipment;
	}
	
	/**
	 * 
	 * @param player The player to copy from.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public void createFromPlayer(IPlayer player) throws TransformerConfigurationException, ParserConfigurationException {
		for (IBodyPart bodyPart : player.bodyParts()) {
			IItem equipted = player.equipment().equipted(bodyPart);
			if (equipted != null) {
				equip(new ItemPersistenceObject(equipted), new BodyPartPersistenceObject(bodyPart));
			}
		}
	}
	
	/**
	 * 
	 * @param obj The object to convert.
	 */
	public void convertFromPersistence(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			convert(cChild);
		}
	}
	
	private void convert(XmlConfigurationObject obj) {
		List<ItemPersistenceObject> items = new ArrayList<ItemPersistenceObject>();
		List<BodyPartPersistenceObject> bodyParts = new ArrayList<BodyPartPersistenceObject>();
		
		for (PersistXml child : obj.children()) {
			try {
				XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
				switch (cChild.name()) {
					case "Item":
						ItemPersistenceObject item = new ItemPersistenceObject();
						item.convertFromPersistence(cChild);
						items.add(item);
						break;
					case "BodyPart":
						BodyPartPersistenceObject bodyPart = new BodyPartPersistenceObject();
						bodyPart.convertFromPersistence(cChild);
						bodyParts.add(bodyPart);
						break;
					default:
						//TODO: what to do here.
						break;
				}
			} catch (Exception e) {
				//TODO: What to do here.
			}
		}
		
		for (int i = 0; i < items.size(); i++) {
			equip(items.get(i), bodyParts.get(i));
		}
	}
}
