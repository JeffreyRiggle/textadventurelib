import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.actions.ModifyPlayerAction;
import textadventurelib.core.ChangeType;
import textadventurelib.core.ModificationObject;
import textadventurelib.core.ModificationType;
import textadventurelib.persistence.ModifyPlayerActionPersistence;

public class ModifyPlayerPersistenceUnitTest {

	private XmlConfigurationManager _manager;
	private final String _modificationActionUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/modPlayerUnitTest.xml";
	private final String _modificationActionUnitTest2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/modPlayerUnitTest2.xml";
	
	private final String _expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Action type=\"ModifyPlayer\"><Parameters><PlayerName ValueType=\"string\">Player1</PlayerName><ChangeType ValueType=\"object\">Add</ChangeType><ModificationType ValueType=\"object\">Change</ModificationType><ID ValueType=\"string\">Age</ID><Data ValueType=\"int\">16</Data><ModificationObject ValueType=\"object\">Attribute</ModificationObject></Parameters></Action>";
	
	private final int _data1 = 15;
	private final boolean _data2 = false;
	private final String _data3 = "SomeString";
	
	private final String _id1 = "Arm";
	private final String _id2 = "Hair";
	private final String _id3 = "Class";
	
	@Test
	public void testCreate() {
		try {
			ModifyPlayerActionPersistence persistence = new ModifyPlayerActionPersistence();
			
			assertNotNull(persistence);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testSetModificationObject() {
		try {
			ModifyPlayerActionPersistence persistence = new ModifyPlayerActionPersistence();
			
			persistence.modificationObj(ModificationObject.Inventory);
			assertEquals(ModificationObject.Inventory, persistence.modificationObj());
			
			persistence.modificationObj(ModificationObject.Attribute);
			assertEquals(ModificationObject.Attribute, persistence.modificationObj());
			
			persistence.modificationObj(ModificationObject.Characteristic);
			assertEquals(ModificationObject.Characteristic, persistence.modificationObj());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetData() {
		try {
			ModifyPlayerActionPersistence persistence = new ModifyPlayerActionPersistence();
			
			persistence.data(_data1);
			assertEquals(_data1, persistence.<Object>data());
			
			persistence.data(_data2);
			assertEquals(_data2, persistence.data());
			
			persistence.data(_data3);
			assertEquals(_data3, persistence.data());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetId() {
		try {
			ModifyPlayerActionPersistence persistence = new ModifyPlayerActionPersistence();
			
			persistence.<String>id(_id1);
			assertEquals(_id1, persistence.id());

			persistence.<String>id(_id2);
			assertEquals(_id2, persistence.id());
			
			persistence.<String>id(_id3);
			assertEquals(_id3, persistence.id());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetChangeType() {
		try {
			ModifyPlayerActionPersistence persistence = new ModifyPlayerActionPersistence();
			
			persistence.changeType(ChangeType.Add);
			assertEquals(ChangeType.Add, persistence.changeType());

			persistence.changeType(ChangeType.Subtract);
			assertEquals(ChangeType.Subtract, persistence.changeType());
			
			persistence.changeType(ChangeType.Assign);
			assertEquals(ChangeType.Assign, persistence.changeType());
		} catch (Exception e) {
			fail(e.toString());
		}		
	}
	
	@Test
	public void testSetModificationType() {
		try {
			ModifyPlayerActionPersistence persistence = new ModifyPlayerActionPersistence();
			
			persistence.modificationType(ModificationType.Add);
			assertEquals(ModificationType.Add, persistence.modificationType());

			persistence.modificationType(ModificationType.Change);
			assertEquals(ModificationType.Change, persistence.modificationType());
			
			persistence.modificationType(ModificationType.Remove);
			assertEquals(ModificationType.Remove, persistence.modificationType());
		} catch (Exception e) {
			fail(e.toString());
		}		
	}
	
	@Test
	public void testPlayerName() {
		try {
			ModifyPlayerActionPersistence persistence = new ModifyPlayerActionPersistence();
			
			persistence.playerName("Player1");
			assertEquals("Player1", persistence.playerName());
			
			persistence.playerName("Deemo");
			assertEquals("Deemo", persistence.playerName());
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			_manager = new XmlConfigurationManager(_modificationActionUnitTest);
			ModifyPlayerActionPersistence persistence = new ModifyPlayerActionPersistence();
			
			persistence.playerName("Player1");
			persistence.changeType(ChangeType.Add);
			persistence.modificationType(ModificationType.Change);
			persistence.<String>id("Age");
			persistence.data(16);
			persistence.modificationObj(ModificationObject.Attribute);

			persistence.prepareXml();
			_manager.addConfigurationObject(persistence);
			_manager.save();
			
			String fileContents = fileContents(_modificationActionUnitTest);
			assertEquals(_expectedXml, fileContents);
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertToAction() {
		try {
			ModifyPlayerActionPersistence persistence = new ModifyPlayerActionPersistence();
			
			persistence.playerName("Player1");
			persistence.changeType(ChangeType.Add);
			persistence.modificationType(ModificationType.Change);
			persistence.<String>id("Age");
			persistence.data(16);
			persistence.modificationObj(ModificationObject.Attribute);

			ModifyPlayerAction action = (ModifyPlayerAction)persistence.convertToAction();
			
			assertEquals(ChangeType.Add, action.data().args().changeType());
			assertEquals(ModificationType.Change, action.data().modificationType());
			assertEquals("Age", action.data().args().identifier());
			assertEquals(16, action.data().args().<Object>data());
			assertEquals(ModificationObject.Attribute, action.data().args().modificationObject());
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertFromPersistence() {
		try {
			_manager = new XmlConfigurationManager(_modificationActionUnitTest2);
			ModifyPlayerActionPersistence persistence = new ModifyPlayerActionPersistence();
			
			persistence.playerName("Player1");
			persistence.changeType(ChangeType.Add);
			persistence.modificationType(ModificationType.Change);
			persistence.<String>id("Age");
			persistence.data(16);
			persistence.modificationObj(ModificationObject.Attribute);

			persistence.prepareXml();
			_manager.addConfigurationObject(persistence);
			_manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(_modificationActionUnitTest2);
			manager2.load();
			
			ModifyPlayerActionPersistence persist = new ModifyPlayerActionPersistence();
			persist.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals("Player1", persist.playerName());
			assertEquals(ChangeType.Add, persist.changeType());
			assertEquals(ModificationType.Change, persist.modificationType());
			assertEquals("Age", persist.id());
			assertEquals(16, persist.<Object>data());
			assertEquals(ModificationObject.Attribute, persist.modificationObj());
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
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
