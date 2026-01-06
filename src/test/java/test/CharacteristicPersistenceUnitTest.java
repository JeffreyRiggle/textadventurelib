import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import playerlib.characteristics.Characteristic;
import playerlib.characteristics.ICharacteristic;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;

public class CharacteristicPersistenceUnitTest {

	private XmlConfigurationManager _manager;
	private final String _characteristicUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/characteristicUnitTest.xml";
	private final String _characteristicUnitTest2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/characteristicUnitTest2.xml";
	private final String _expectedCharacteristicText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><NamedObject type=\"Characteristic\"><Name ValueType=\"string\">Hair</Name><Description ValueType=\"string\">Dead skin cells</Description><Value ValueType=\"string\">Blue</Value></NamedObject>";
	
	@Test
	public void testCreate() {
		try {
			CharacteristicPersistenceObject persist = new CharacteristicPersistenceObject();
			
			assertNotNull(persist);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testCharacteristicCtor() {
		try {
			ICharacteristic character = new Characteristic("Hair Color", "Brown");
			character.description("The color of a players hair");
			
			CharacteristicPersistenceObject persist = new CharacteristicPersistenceObject(character);
			
			assertEquals("Hair Color", persist.objectName());
			assertEquals("Brown", persist.objectValue());
			assertEquals("The color of a players hair", persist.description());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testCreateFromCharacteristic() {
		try {
			ICharacteristic character = new Characteristic("Hair Color", "Brown");
			character.description("The color of a players hair");
			
			CharacteristicPersistenceObject persist = new CharacteristicPersistenceObject();
			persist.createFromCharacteristic(character);
			
			assertEquals("Hair Color", persist.objectName());
			assertEquals("Brown", persist.objectValue());
			assertEquals("The color of a players hair", persist.description());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetName() {
		try {
			CharacteristicPersistenceObject persist = new CharacteristicPersistenceObject();
			
			persist.objectName("Hair");
			assertEquals("Hair", persist.objectName());
			
			persist.objectName("Scar");
			assertEquals("Scar", persist.objectName());
			
			persist.objectName("Nail");
			assertEquals("Nail", persist.objectName());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetValue() {
		try {
			CharacteristicPersistenceObject persist = new CharacteristicPersistenceObject();
			
			persist.objectValue("Brown");
			assertEquals("Brown", persist.objectValue());
			
			persist.objectValue("Long");
			assertEquals("Long", persist.objectValue());
			
			persist.objectValue("cracked");
			assertEquals("cracked", persist.objectValue());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testSetDescription() {
		try {
			CharacteristicPersistenceObject persist = new CharacteristicPersistenceObject();
			
			persist.description("Dead skin cells");
			assertEquals("Dead skin cells", persist.description());
			
			persist.description("A permanent marking");
			assertEquals("A permanent marking", persist.description());
			
			persist.description("Bone on the hand");
			assertEquals("Bone on the hand", persist.description());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			_manager = new XmlConfigurationManager(_characteristicUnitTest);
			CharacteristicPersistenceObject persist = new CharacteristicPersistenceObject();
			
			persist.objectName("Hair");
			persist.objectValue("Blue");
			persist.description("Dead skin cells");
			
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			String fileContents = fileContents(_characteristicUnitTest);
			assertEquals(_expectedCharacteristicText, fileContents);
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testCharacteristicConversion() {
		try {
			CharacteristicPersistenceObject persist = new CharacteristicPersistenceObject();
			
			persist.objectName("Hair");
			persist.objectValue("Blue");
			persist.description("Dead skin cells");
			
			ICharacteristic characteristic = persist.convertToCharacteristic();
			
			assertEquals("Hair", characteristic.name());
			assertEquals("Blue", characteristic.value());
			assertEquals("Dead skin cells", characteristic.description());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertFromXml() {
		try {
			_manager = new XmlConfigurationManager(_characteristicUnitTest2);
			CharacteristicPersistenceObject persist = new CharacteristicPersistenceObject();
			
			persist.objectName("Hair");
			persist.objectValue("Blue");
			persist.description("Dead skin cells");
			
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(_characteristicUnitTest2);
			manager2.load();
			
			CharacteristicPersistenceObject persist2 = new CharacteristicPersistenceObject();
			persist2.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals("Hair", persist2.objectName());
			assertEquals("Blue", persist2.objectValue());
			assertEquals("Dead skin cells", persist2.description());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	private String fileContents(String fileLocation) throws IOException {
		File file = new File(fileLocation);
		return getFileContent(file);
	}
	
	private String getFileContent(File file) throws IOException {
		String output = new String();
		FileInputStream inputStream = new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line = new String();
		
		while((line = reader.readLine()) != null) {
			output += line;
		}
		
		reader.close();
		return output;
	}
}
