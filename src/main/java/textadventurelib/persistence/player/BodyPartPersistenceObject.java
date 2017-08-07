package textadventurelib.persistence.player;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import playerlib.characteristics.ICharacteristic;
import playerlib.equipment.BodyPart;
import playerlib.equipment.IBodyPart;


/**
 * 
 * @author Jeff Riggle
 *
 */
public class BodyPartPersistenceObject extends XmlConfigurationObject{
	
	private final static String BODY_PART = "BodyPart";
	private final static String NAME_NAME = "Name";
	private final static String DESCRIPTION_NAME = "Description";
	private final static String CHARACTERISTIC_NAME = "Characteristics";
	
	private XmlConfigurationObject name;
	private XmlConfigurationObject description;
	private XmlConfigurationObject characteristics;
	
	/**
	 * Ctor.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public BodyPartPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(BODY_PART);
		
		name = new XmlConfigurationObject(NAME_NAME);
		description = new XmlConfigurationObject(DESCRIPTION_NAME);
		characteristics = new XmlConfigurationObject(CHARACTERISTIC_NAME);
	}

	public BodyPartPersistenceObject(IBodyPart bodyPart) throws TransformerConfigurationException, ParserConfigurationException {
		this();
		createFromBodyPart(bodyPart);
	}
	
	/**
	 * 
	 * @param name The new name for the body part.
	 */
	public void objectName(String name) {
		this.name.value(name);
	}
	
	/**
	 * 
	 * @return The name associated with the body part.
	 */
	public String objectName() {
		return name.value();
	}
	
	/**
	 * 
	 * @param description The new description for the body part.
	 */
	public void description(String description) {
		this.description.value(description);
	}
	
	/**
	 * 
	 * @return The description for the body part.
	 */
	public String description() {
		return description.value();
	}
	
	/**
	 * 
	 * @param characteristic The @see CharacteristicPersistenceObject to add to this body part.
	 */
	public void addCharacteristic(CharacteristicPersistenceObject characteristic) {
		characteristics.addChild(characteristic);
	}
	
	/**
	 * 
	 * @param characteristic The @see CharacteristicPersistenceObject to remove from this body part.
	 */
	public void removeCharacteristic(CharacteristicPersistenceObject characteristic) {
		characteristics.removeChild(characteristic);
	}
	
	/**
	 * Removes all @see CharacteristicPersistenceObject 's from this body part.
	 */
	public void clearCharacteristics() {
		characteristics.clearChildren();
	}
	
	/**
	 * 
	 * @return A List of @see CharacteristicPersistenceObject 's associated with this body part.
	 */
	public List<CharacteristicPersistenceObject> characteristics() {
		List<CharacteristicPersistenceObject> retVal = new ArrayList<CharacteristicPersistenceObject>();
		
		for (PersistXml characteristic : characteristics.children()) {
			retVal.add((CharacteristicPersistenceObject)characteristic);
		}
		return retVal;
	}
	
	/**
	 * Prepares this xml to be saved. This must be called before saving the xml!
	 */
	public void prepareXml() {
		super.clearConfigurationProperties();
		super.clearChildren();
		
		super.addChild(name);
		super.addChild(description);
		
		for (PersistXml characteristic : characteristics.children()) {
			((CharacteristicPersistenceObject)characteristic).prepareXml();
		}
		
		super.addChild(characteristics);
	}
	
	/**
	 * 
	 * @return The converted body part object.
	 */
	public IBodyPart convertToBodyPart() {
		IBodyPart bodyPart = new BodyPart(objectName());
		bodyPart.description(description());
		
		for (PersistXml characteristic : characteristics.children()) {
			CharacteristicPersistenceObject character = ((CharacteristicPersistenceObject)characteristic);
			
			bodyPart.addCharacteristic(character.convertToCharacteristic());
		}
		
		return bodyPart;
	}
	
	/**
	 * 
	 * @param bodyPart The body part to copy from.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public void createFromBodyPart(IBodyPart bodyPart) throws TransformerConfigurationException, ParserConfigurationException {
		objectName(bodyPart.name());
		description(bodyPart.description());
		
		for (ICharacteristic character : bodyPart.getCharacteristics()) {
			addCharacteristic(new CharacteristicPersistenceObject(character));
		}
	}
	
	/**
	 * 
	 * @param obj The persistence object to convert from.
	 */
	public void convertFromPersistence(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = ((XmlConfigurationObject)child);
			
			switch (cChild.name()) {
				case NAME_NAME:
					objectName(cChild.<String>value());
					break;
				case DESCRIPTION_NAME:
					description(cChild.<String>value());
					break;
				case CHARACTERISTIC_NAME:
					persistCharacteristics(cChild);
					break;
				default:
					//TODO: what to do here.
					break;
			}
		}
	}
	
	private void persistCharacteristics(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			try {
				XmlConfigurationObject cChild = ((XmlConfigurationObject)child);
			
				CharacteristicPersistenceObject characteristic = new CharacteristicPersistenceObject();
				characteristic.convertFromPersistence(cChild);
				addCharacteristic(characteristic);
			} catch (Exception e) {
				//TODO: What to do here.
			}
		}
	}
	
	@Override
	public String toString() {
		return String.format("%s: %s", objectName(), description());
	}
}
