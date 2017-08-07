package textadventurelib.persistence.player;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import playerlib.attributes.IAttribute;
import playerlib.characteristics.ICharacteristic;
import playerlib.equipment.IBodyPart;
import playerlib.player.IPlayer;
import playerlib.player.Player;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayerPersistenceObject extends XmlConfigurationObject{

	private final static String PLAYER_NAME = "Player";
	private final static String NAME_NAME = "Name";
	private final static String ATTRIBUTES_NAME = "Attributes";
	private final static String CHARACTERISTICS_NAME = "Characteristics";
	private final static String BODY_PARTS_NAME = "BodyParts";
	
	private List<AttributePersistenceObject> attributes;
	private List<CharacteristicPersistenceObject> characteristics;
	private List<BodyPartPersistenceObject> bodyParts;
	private XmlConfigurationObject playersName;
	private InventoryPersistenceObject inventory;
	private EquipmentPersistenceObject equipment;
	
	/**
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public PlayerPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(PLAYER_NAME);
		
		attributes = new ArrayList<AttributePersistenceObject>();
		characteristics = new ArrayList<CharacteristicPersistenceObject>();
		bodyParts = new ArrayList<BodyPartPersistenceObject>();
		playersName = new XmlConfigurationObject(NAME_NAME);
		inventory = new InventoryPersistenceObject();
		equipment = new EquipmentPersistenceObject();
	}
	
	/**
	 * 
	 * @param player The player to copy from.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public PlayerPersistenceObject(IPlayer player) throws TransformerConfigurationException, ParserConfigurationException {
		this();
		createFromPlayer(player);
	}
	
	/**
	 * 
	 * @param name The new name of the player.
	 */
	public void playerName(String name) {
		playersName.value(name);
	}
	
	/**
	 * 
	 * @return The name of the player.
	 */
	public String playerName() {
		return playersName.value();
	}
	
	/**
	 * 
	 * @param attribute The attribute to add to the player.
	 */
	public void addAttribute(AttributePersistenceObject attribute) {
		attributes.add(attribute);
	}
	
	/**
	 * 
	 * @param attribute The attribute to remove from the player.
	 */
	public void removeAttribute(AttributePersistenceObject attribute) {
		attributes.remove(attribute);
	}
	
	/**
	 * 
	 * @return The players attributes.
	 */
	public List<AttributePersistenceObject> attributes() {
		return attributes;
	}
	
	/**
	 * Removes all attributes from this player.
	 */
	public void clearAttributes() {
		attributes.clear();
	}
	
	/**
	 * 
	 * @param characteristic The characteristic to add to this player.
	 */
	public void addCharacteristic(CharacteristicPersistenceObject characteristic) {
		characteristics.add(characteristic);
	}
	
	/**
	 * 
	 * @param characteristic The characteristic to remove from this player.
	 */
	public void removeCharacteristic(CharacteristicPersistenceObject characteristic) {
		characteristics.remove(characteristic);
	}
	
	/**
	 * Removes all characteristics from this player.
	 */
	public void clearCharacteristics() {
		characteristics.clear();
	}
	
	/**
	 * 
	 * @return The players characteristics.
	 */
	public List<CharacteristicPersistenceObject> characteristics() {
		return characteristics;
	}
	
	/**
	 * 
	 * @param bodyPart The body part to add to this player.
	 */
	public void addBodyPart(BodyPartPersistenceObject bodyPart) {
		bodyParts.add(bodyPart);
	}
	
	/**
	 * 
	 * @param bodyPart The body part to remove from this player.
	 */
	public void removeBodyPart(BodyPartPersistenceObject bodyPart) {
		bodyParts.remove(bodyPart);
	}
	
	/**
	 * 
	 * @return This players body parts.
	 */
	public List<BodyPartPersistenceObject> bodyParts() {
		return bodyParts;
	}
	
	/**
	 * Removes all body parts from this player.
	 */
	public void clearBodyParts() {
		bodyParts.clear();
	}
	
	/**
	 * 
	 * @param inv The inventory to associate with this player.
	 */
	public void inventory(InventoryPersistenceObject inv) {
		inventory = inv;
	}
	
	/**
	 * 
	 * @return This players inventory.
	 */
	public InventoryPersistenceObject inventory() {
		return inventory;
	}
	
	/**
	 * 
	 * @param equip The equipment to associate with this player.
	 */
	public void equipment(EquipmentPersistenceObject equip) {
		equipment = equip;
	}
	
	/**
	 * 
	 * @return This players equipment.
	 */
	public EquipmentPersistenceObject equipment() {
		return equipment;
	}
	
	/**
	 * Prepares this object to be persisted.
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public void prepareXml() throws TransformerConfigurationException, ParserConfigurationException {
		super.clearChildren();
		super.clearConfigurationProperties();
		
		super.addChild(playersName);
		
		XmlConfigurationObject attrs = new XmlConfigurationObject(ATTRIBUTES_NAME);
		
		for (AttributePersistenceObject att : attributes) {
			att.prepareXml();
			attrs.addChild(att);
		}
		super.addChild(attrs);
		
		XmlConfigurationObject charactersistics = new XmlConfigurationObject(CHARACTERISTICS_NAME);
		
		for (CharacteristicPersistenceObject chr : characteristics) {
			chr.prepareXml();
			charactersistics.addChild(chr);
		}
		super.addChild(charactersistics);
		
		XmlConfigurationObject bparts = new XmlConfigurationObject(BODY_PARTS_NAME);
		
		for (BodyPartPersistenceObject body : bodyParts) {
			body.prepareXml();
			bparts.addChild(body);
		}
		super.addChild(bparts);
		
		inventory.prepareXml();
		super.addChild(inventory);
		
		equipment.prepareXml();
		super.addChild(equipment);
	}
	
	/**
	 * 
	 * @return The converted player.
	 */
	public IPlayer convertToPlayer() {
		IPlayer player = new Player(playerName());
		
		for (AttributePersistenceObject att : attributes()) {
			player.addAttribute(att.convertToAttribute());
		}
		
		for (CharacteristicPersistenceObject character : characteristics()) {
			player.addCharacteristic(character.convertToCharacteristic());
		}
		
		for (BodyPartPersistenceObject bodyPart : bodyParts()) {
			player.addBodyPart(bodyPart.convertToBodyPart());
		}
		
		player.inventory(inventory().convertToInventory());
		player.equipment(equipment().convertToEquipment());
		
		return player;
	}
	
	/**
	 * 
	 * @param player The player to copy from.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public void createFromPlayer(IPlayer player) throws TransformerConfigurationException, ParserConfigurationException {
		playerName(player.name());
		
		for (ICharacteristic character : player.characteristics()) {
			addCharacteristic(new CharacteristicPersistenceObject(character));
		}
		
		for (IBodyPart bodyPart : player.bodyParts()) {
			addBodyPart(new BodyPartPersistenceObject(bodyPart));
		}
		
		for (IAttribute attribute : player.attributes()) {
			addAttribute(new AttributePersistenceObject(attribute));
		}
		
		inventory(new InventoryPersistenceObject(player.inventory()));
		equipment(new EquipmentPersistenceObject(player));
	}
	
	/**
	 * 
	 * @param obj The object to convert.
	 */
	public void convertFromPersistence(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			try {
				XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
				switch (cChild.name()) {
					case NAME_NAME:
						playerName(cChild.<String>value());
						break;
					case ATTRIBUTES_NAME:
						convertAttributes(cChild);
						break;
					case CHARACTERISTICS_NAME:
						convertCharacteristics(cChild);
						break;
					case BODY_PARTS_NAME:
						convertBodyParts(cChild);
						break;
					case "Inventory":
						InventoryPersistenceObject inv = new InventoryPersistenceObject();
						inv.convertFromPersistence(cChild);
						inventory(inv);
						break;
					case "Equipment":
						EquipmentPersistenceObject equip = new EquipmentPersistenceObject();
						equip.convertFromPersistence(cChild);
						equipment(equip);
						break;
					default:
						//TODO: Should something be done here.
						break;
				}
			} catch (Exception e) {
				//TODO: Should something be done here?
			}
		}
	}
	
	private void convertAttributes(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			try {
				XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
				AttributePersistenceObject att = new AttributePersistenceObject();
				att.convertFromPersistence(cChild);
				addAttribute(att);
			} catch (Exception e) {
				//TODO: should something be done here?
			}
		}
	}
	
	private void convertCharacteristics(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			try {
				XmlConfigurationObject cChild = (XmlConfigurationObject)child;
				
				CharacteristicPersistenceObject character = new CharacteristicPersistenceObject();
				character.convertFromPersistence(cChild);
				addCharacteristic(character);
			} catch (Exception e) {
				//TODO: should something be done here?
			}
		}
	}
	
	private void convertBodyParts(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			try {
				XmlConfigurationObject cChild = (XmlConfigurationObject)child;
				
				BodyPartPersistenceObject bodyPart = new BodyPartPersistenceObject();
				bodyPart.convertFromPersistence(cChild);
				addBodyPart(bodyPart);
			} catch (Exception e) {
				//TODO: should something be done here?
			}
		}
	}
}
