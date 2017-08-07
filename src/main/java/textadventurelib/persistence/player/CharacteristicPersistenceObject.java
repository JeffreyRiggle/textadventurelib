package textadventurelib.persistence.player;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import playerlib.characteristics.Characteristic;
import playerlib.characteristics.ICharacteristic;


/**
 * 
 * @author Jeff Riggle
 *
 *	Creates xml for player characteristic data.
 */
public class CharacteristicPersistenceObject extends NamedPersistenceObject{
	private final static String CHARACTERISTIC = "Characteristic";
	
	/**
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public CharacteristicPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(CHARACTERISTIC);
	}
	
	/**
	 * 
	 * @param character The characteristic to create from.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public CharacteristicPersistenceObject(ICharacteristic character) throws TransformerConfigurationException, ParserConfigurationException {
		super(CHARACTERISTIC);
		createFromCharacteristic(character);
	}
	
	/**
	 * 
	 * @param character The characteristic to copy into this persistence object.
	 */
	public void createFromCharacteristic(ICharacteristic character) {
		super.objectName(character.name());
		super.description(character.description());
		super.objectValue(character.value());
	}
	
	/**
	 * 
	 * @return The converted characteristic.
	 */
	public ICharacteristic convertToCharacteristic() {
		ICharacteristic characteristic = new Characteristic(objectName(), objectValue());
		characteristic.description(description());
		return characteristic;
	}
}
