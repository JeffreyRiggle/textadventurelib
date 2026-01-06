import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.core.Condition;
import textadventurelib.core.ModificationObject;
import textadventurelib.core.PlayerConditionParameters;
import textadventurelib.persistence.PlayerTriggerPersistenceObject;
import textadventurelib.triggers.PlayerConditionTrigger;

public class PlayerTriggerPersistenceUnitTest {

	private XmlConfigurationManager _manager;
	
	private final String _playerTriggerUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/playerTriggerUnitTest.xml";
	private final String _playerTriggerUnitTest2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/playerTriggerUnitTest2.xml";
	
	private final String _expectedPlayerTriggerText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Trigger type=\"Player\"><Parameters><ComparisionData ValueType=\"int\">13</ComparisionData><Condition ValueType=\"object\">GreaterThan</Condition><ModificationObject ValueType=\"object\">Attribute</ModificationObject><ID ValueType=\"string\">age|,</ID><DataMember ValueType=\"string\">value</DataMember><PlayerName ValueType=\"string\">Deemo</PlayerName></Parameters></Trigger>";

	@Test
	public void testCreate() {
		try {
			PlayerTriggerPersistenceObject persist = new PlayerTriggerPersistenceObject();
			
			assertNotNull(persist);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testSetModificationObject() {
		try {
			PlayerTriggerPersistenceObject persist = new PlayerTriggerPersistenceObject();
			
			persist.modificationObject(ModificationObject.Attribute);
			assertEquals(ModificationObject.Attribute, persist.modificationObject());
			
			persist.modificationObject(ModificationObject.BodyPart);
			assertEquals(ModificationObject.BodyPart, persist.modificationObject());
			
			persist.modificationObject(ModificationObject.Characteristic);
			assertEquals(ModificationObject.Characteristic, persist.modificationObject());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetId() {
		try {
			PlayerTriggerPersistenceObject persist = new PlayerTriggerPersistenceObject();
			
			persist.id(new String[]{"HP"});
			assertEquals("HP", persist.id()[0]);
			
			persist.id(new String[]{"MP"});
			assertEquals("MP", persist.id()[0]);
			
			persist.id(new String[]{"Class"});
			assertEquals("Class", persist.id()[0]);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetCondtion() {
		try {
			PlayerTriggerPersistenceObject persist = new PlayerTriggerPersistenceObject();
			
			persist.condition(Condition.EqualTo);
			assertEquals(Condition.EqualTo, persist.condition());
			
			persist.condition(Condition.GreaterThan);
			assertEquals(Condition.GreaterThan, persist.condition());
			
			persist.condition(Condition.LessThan);
			assertEquals(Condition.LessThan, persist.condition());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetDataMember() {
		try {
			PlayerTriggerPersistenceObject persist = new PlayerTriggerPersistenceObject();
			
			persist.dataMember("value");
			assertEquals("value", persist.dataMember());
			
			persist.dataMember("name");
			assertEquals("name", persist.dataMember());
			
			persist.dataMember("potion|value");
			assertEquals("potion|value", persist.dataMember());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetComparisionData() {
		try {
			PlayerTriggerPersistenceObject persist = new PlayerTriggerPersistenceObject();
			
			persist.comparisonData(13);
			assertEquals(13, persist.<Object>comparisonData());
			
			persist.comparisonData("Wizard");
			assertEquals("Wizard", persist.comparisonData());
			
			persist.comparisonData(true);
			assertEquals(true, persist.comparisonData());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetPlayerName() {
		try {
			PlayerTriggerPersistenceObject persist = new PlayerTriggerPersistenceObject();
			
			persist.playerName("Deemo");
			assertEquals("Deemo", persist.playerName());
			
			persist.playerName("Miku");
			assertEquals("Miku", persist.playerName());
			
			persist.playerName("John von John");
			assertEquals("John von John", persist.playerName());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			_manager = new XmlConfigurationManager(_playerTriggerUnitTest);
			PlayerTriggerPersistenceObject persist = new PlayerTriggerPersistenceObject();
			
			persist.comparisonData(13);
			persist.condition(Condition.GreaterThan);
			persist.modificationObject(ModificationObject.Attribute);
			persist.id(new String[] {"age"});
			persist.dataMember("value");
			persist.playerName("Deemo");
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			String fileContents = fileContents(_playerTriggerUnitTest);
			assertEquals(_expectedPlayerTriggerText, fileContents);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertToTrigger() {
		try {
			PlayerTriggerPersistenceObject persist = new PlayerTriggerPersistenceObject();
			
			persist.comparisonData(13);
			persist.condition(Condition.GreaterThan);
			persist.modificationObject(ModificationObject.Attribute);
			persist.dataMember("value");
			persist.playerName("Deemo");
			
			PlayerConditionTrigger trigger = (PlayerConditionTrigger)persist.convertToTrigger();
			PlayerConditionParameters params = trigger.condition();
			assertEquals(13, params.<Object>comparisonData());
			assertEquals(Condition.GreaterThan, params.condition());
			assertEquals(ModificationObject.Attribute, params.modificationObject());
			assertEquals("value", params.dataMember());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertFromPersistence() {
		try {
			_manager = new XmlConfigurationManager(_playerTriggerUnitTest2);
			PlayerTriggerPersistenceObject persist = new PlayerTriggerPersistenceObject();
			
			persist.comparisonData(13);
			persist.condition(Condition.GreaterThan);
			persist.modificationObject(ModificationObject.Attribute);
			persist.id(new String[] {"age"});
			persist.dataMember("value");
			persist.playerName("Deemo");
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(_playerTriggerUnitTest2);
			manager2.load();
			
			PlayerTriggerPersistenceObject persist2 = new PlayerTriggerPersistenceObject();
			persist2.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			assertEquals(13, persist2.<Object>comparisonData());
			assertEquals(Condition.GreaterThan, persist2.condition());
			assertEquals(ModificationObject.Attribute, persist2.modificationObject());
			assertEquals("age", persist2.id()[0]);
			assertEquals("value", persist2.dataMember());
			assertEquals("Deemo", persist2.playerName());
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
