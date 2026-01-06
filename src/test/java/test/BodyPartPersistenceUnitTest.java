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
import playerlib.equipment.BodyPart;
import playerlib.equipment.IBodyPart;
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;

public class BodyPartPersistenceUnitTest {

	private XmlConfigurationManager _manager;
	private final String _bodyPartUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/bodyPartUnitTest.xml";
	private final String _bodyPartUnitTest2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/bodyPartUnitTest2.xml";
	private final String _expectedBodyPartText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><BodyPart><Name ValueType=\"string\">Head</Name><Description ValueType=\"string\">An Appendage of the players tourso</Description><Characteristics><NamedObject type=\"Characteristic\"><Name ValueType=\"string\">Hair</Name><Description ValueType=\"string\">Dead skin cells</Description><Value ValueType=\"string\">Brown</Value></NamedObject></Characteristics></BodyPart>";
	
	@Test
	public void testCreate() {
		try {
			BodyPartPersistenceObject persist = new BodyPartPersistenceObject();
			
			assertNotNull(persist);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testBodyPartCtor() {
		try {
			IBodyPart bodyPart = new BodyPart("Arm");
			bodyPart.description("Part attached to tourso");
			
			ICharacteristic hasHair = new Characteristic("Has hair", true);
			ICharacteristic skinColor = new Characteristic("SkinColor", "white");
			
			bodyPart.addCharacteristic(hasHair);
			bodyPart.addCharacteristic(skinColor);
			
			BodyPartPersistenceObject persist = new BodyPartPersistenceObject(bodyPart);
			
			assertEquals("Arm", persist.objectName());
			assertEquals("Part attached to tourso", persist.description());
			assertEquals(2, persist.characteristics().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testCreateFromBodyPart() {
		try {
			IBodyPart bodyPart = new BodyPart("Arm");
			bodyPart.description("Part attached to tourso");
			
			ICharacteristic hasHair = new Characteristic("Has hair", true);
			ICharacteristic skinColor = new Characteristic("SkinColor", "white");
			
			bodyPart.addCharacteristic(hasHair);
			bodyPart.addCharacteristic(skinColor);
			
			BodyPartPersistenceObject persist = new BodyPartPersistenceObject();
			persist.createFromBodyPart(bodyPart);
			
			assertEquals("Arm", persist.objectName());
			assertEquals("Part attached to tourso", persist.description());
			assertEquals(2, persist.characteristics().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetName() {
		try {
			BodyPartPersistenceObject persist = new BodyPartPersistenceObject();
			
			persist.objectName("Arm");
			assertEquals("Arm", persist.objectName());
			
			persist.objectName("Hand");
			assertEquals("Hand", persist.objectName());
			
			persist.objectName("Head");
			assertEquals("Head", persist.objectName());
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testSetDescription() {
		try {
			BodyPartPersistenceObject persist = new BodyPartPersistenceObject();
			
			persist.description("An Appendage of the players tourso");
			assertEquals("An Appendage of the players tourso", persist.description());
			
			persist.description("An Appendage of the players arm");
			assertEquals("An Appendage of the players arm", persist.description());
			
			persist.description("An Appendage of the players tourso");
			assertEquals("An Appendage of the players tourso", persist.description());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testAddCharacteristic() {
		try {
			BodyPartPersistenceObject persist = new BodyPartPersistenceObject();
			
			CharacteristicPersistenceObject char1 = new CharacteristicPersistenceObject();
			CharacteristicPersistenceObject char2 = new CharacteristicPersistenceObject();
			CharacteristicPersistenceObject char3 = new CharacteristicPersistenceObject();
			
			assertEquals(0, persist.characteristics().size());
			
			persist.addCharacteristic(char1);
			assertEquals(1, persist.characteristics().size());
			assertEquals(char1, persist.characteristics().get(0));
			
			persist.addCharacteristic(char2);
			assertEquals(2, persist.characteristics().size());
			assertEquals(char2, persist.characteristics().get(1));
			
			persist.addCharacteristic(char3);
			assertEquals(3, persist.characteristics().size());
			assertEquals(char3, persist.characteristics().get(2));
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testRemoveCharacteristic() {
		try {
			BodyPartPersistenceObject persist = new BodyPartPersistenceObject();
			
			CharacteristicPersistenceObject char1 = new CharacteristicPersistenceObject();
			CharacteristicPersistenceObject char2 = new CharacteristicPersistenceObject();
			CharacteristicPersistenceObject char3 = new CharacteristicPersistenceObject();
			
			persist.addCharacteristic(char1);
			persist.addCharacteristic(char2);
			persist.addCharacteristic(char3);
			
			assertEquals(3, persist.characteristics().size());
			
			persist.removeCharacteristic(char1);
			assertEquals(2, persist.characteristics().size());
			
			persist.removeCharacteristic(char2);
			assertEquals(1, persist.characteristics().size());
			
			persist.removeCharacteristic(char3);
			assertEquals(0, persist.characteristics().size());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testClearCharacteristics() {
		try {
			BodyPartPersistenceObject persist = new BodyPartPersistenceObject();
			
			CharacteristicPersistenceObject char1 = new CharacteristicPersistenceObject();
			CharacteristicPersistenceObject char2 = new CharacteristicPersistenceObject();
			CharacteristicPersistenceObject char3 = new CharacteristicPersistenceObject();
			
			persist.addCharacteristic(char1);
			persist.addCharacteristic(char2);
			persist.addCharacteristic(char3);
			
			assertEquals(3, persist.characteristics().size());
			
			persist.clearCharacteristics();
			assertEquals(0, persist.characteristics().size());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			_manager = new XmlConfigurationManager(_bodyPartUnitTest);
			BodyPartPersistenceObject persist = new BodyPartPersistenceObject();
			
			persist.objectName("Head");
			persist.description("An Appendage of the players tourso");
			CharacteristicPersistenceObject char1 = new CharacteristicPersistenceObject();
			char1.objectName("Hair");
			char1.objectValue("Brown");
			char1.description("Dead skin cells");
			persist.addCharacteristic(char1);
			
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			String fileContents = fileContents(_bodyPartUnitTest);
			assertEquals(_expectedBodyPartText, fileContents);
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertToBodyPart(){
		try {
			BodyPartPersistenceObject bpersist = new BodyPartPersistenceObject();
			bpersist.objectName("Head");
			bpersist.description("Attached to the torso");
			
			CharacteristicPersistenceObject hair = new CharacteristicPersistenceObject();
			hair.objectName("Hair");
			hair.objectValue("Blue");
			hair.description("Dead skin cells");
			bpersist.addCharacteristic(hair);
			CharacteristicPersistenceObject skin = new CharacteristicPersistenceObject();
			skin.objectName("Skin");
			skin.objectValue("White");
			skin.description("Covers the body");
			bpersist.addCharacteristic(skin);
			
			IBodyPart bodyPart = bpersist.convertToBodyPart();
			assertEquals("Head", bodyPart.name());
			assertEquals("Attached to the torso", bodyPart.description());
			assertEquals(2, bodyPart.getCharacteristics().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertFromPersistence() {
		try {
			_manager = new XmlConfigurationManager(_bodyPartUnitTest2);
			
			BodyPartPersistenceObject bpersist = new BodyPartPersistenceObject();
			bpersist.objectName("Head");
			bpersist.description("Attached to the torso");
			
			CharacteristicPersistenceObject hair = new CharacteristicPersistenceObject();
			hair.objectName("Hair");
			hair.objectValue("Blue");
			hair.description("Dead skin cells");
			bpersist.addCharacteristic(hair);
			CharacteristicPersistenceObject skin = new CharacteristicPersistenceObject();
			skin.objectName("Skin");
			skin.objectValue("White");
			skin.description("Covers the body");
			bpersist.addCharacteristic(skin);
			bpersist.prepareXml();
			_manager.addConfigurationObject(bpersist);
			_manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(_bodyPartUnitTest2);
			manager2.load();
			
			BodyPartPersistenceObject persist2 = new BodyPartPersistenceObject();
			persist2.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals("Head", persist2.objectName());
			assertEquals("Attached to the torso", persist2.description());
			assertEquals(2, persist2.characteristics().size());
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
