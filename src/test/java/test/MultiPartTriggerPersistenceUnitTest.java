import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import playerlib.attributes.Attribute;
import playerlib.player.IPlayer;
import playerlib.player.Player;
import textadventurelib.core.Condition;
import textadventurelib.core.ModificationObject;
import textadventurelib.core.TriggerParameters;
import textadventurelib.persistence.MatchType;
import textadventurelib.persistence.MultiPartTriggerPersistenceObject;
import textadventurelib.persistence.PlayerTriggerPersistenceObject;
import textadventurelib.persistence.TextTriggerPersistenceObject;
import textadventurelib.persistence.TriggerPersistenceObject;
import textadventurelib.triggers.MultiPartTrigger;

public class MultiPartTriggerPersistenceUnitTest {

	private XmlConfigurationManager _manager;
	private final String _multiTriggerUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/multiTriggerUnitTest.xml";
	private final String _multiTriggerUnitTest2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/multiTriggerUnitTest2.xml";
	
	private final String _expectedMultiTriggerText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Trigger type=\"MultiPart\"><Parameters><Trigger type=\"Text\"><Parameters><Text ValueType=\"string\">Test</Text><CaseSensitive ValueType=\"bool\">false</CaseSensitive><MatchType ValueType=\"object\">Contains</MatchType></Parameters></Trigger><Trigger type=\"Text\"><Parameters><Text ValueType=\"string\">Test</Text><CaseSensitive ValueType=\"bool\">true</CaseSensitive><MatchType ValueType=\"object\">Exact</MatchType></Parameters></Trigger><Trigger type=\"Player\"><Parameters><ComparisionData ValueType=\"int\">13</ComparisionData><Condition ValueType=\"object\">GreaterThan</Condition><ModificationObject ValueType=\"object\">Attribute</ModificationObject><DataMember ValueType=\"string\">value</DataMember></Parameters></Trigger></Parameters></Trigger>";

	@Test
	public void testCreate() {
		try {
			MultiPartTriggerPersistenceObject persist = new MultiPartTriggerPersistenceObject();
			
			assertNotNull(persist);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetTriggers() {
		try {
			MultiPartTriggerPersistenceObject persist = new MultiPartTriggerPersistenceObject();
			
			List<TriggerPersistenceObject> triggers = new ArrayList<TriggerPersistenceObject>();
			TextTriggerPersistenceObject trigger1 = new TextTriggerPersistenceObject();
			trigger1.text("Something");
			PlayerTriggerPersistenceObject trigger2 = new PlayerTriggerPersistenceObject();
			trigger2.comparisonData("Somethingelse");
			triggers.add(trigger1);
			triggers.add(trigger2);
			
			persist.triggers(triggers);
			
			assertNotNull(persist.triggers());
			assertEquals(2, persist.triggers().size());
			assertEquals(trigger1, persist.triggers().get(0));
			assertEquals(trigger2, persist.triggers().get(1));
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testAddTrigger() {
		try {
			MultiPartTriggerPersistenceObject persist = new MultiPartTriggerPersistenceObject();
			
			TextTriggerPersistenceObject trigger1 = new TextTriggerPersistenceObject();
			trigger1.text("Something");
			PlayerTriggerPersistenceObject trigger2 = new PlayerTriggerPersistenceObject();
			trigger2.comparisonData("Somethingelse");
			TextTriggerPersistenceObject trigger3 = new TextTriggerPersistenceObject();
			trigger3.text("Something er something");
			
			persist.addTrigger(trigger1);
			assertEquals(1, persist.triggers().size());
			assertEquals(trigger1, persist.triggers().get(0));
			
			persist.addTrigger(trigger2);
			assertEquals(2, persist.triggers().size());
			assertEquals(trigger2, persist.triggers().get(1));
			
			persist.addTrigger(trigger3);
			assertEquals(3, persist.triggers().size());
			assertEquals(trigger3, persist.triggers().get(2));
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testRemoveTrigger() {
		try {
			MultiPartTriggerPersistenceObject persist = new MultiPartTriggerPersistenceObject();
			
			List<TriggerPersistenceObject> triggers = new ArrayList<TriggerPersistenceObject>();
			TextTriggerPersistenceObject trigger1 = new TextTriggerPersistenceObject();
			trigger1.text("Something");
			PlayerTriggerPersistenceObject trigger2 = new PlayerTriggerPersistenceObject();
			trigger2.comparisonData("Somethingelse");
			triggers.add(trigger1);
			triggers.add(trigger2);
			
			persist.triggers(triggers);
			
			assertNotNull(persist.triggers());
			assertEquals(2, persist.triggers().size());
			
			persist.removeTrigger(trigger1);
			assertEquals(1, persist.triggers().size());
			
			persist.removeTrigger(trigger2);
			assertEquals(0, persist.triggers().size());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testClearTriggers() {
		try {
			MultiPartTriggerPersistenceObject persist = new MultiPartTriggerPersistenceObject();
			
			List<TriggerPersistenceObject> triggers = new ArrayList<TriggerPersistenceObject>();
			TextTriggerPersistenceObject trigger1 = new TextTriggerPersistenceObject();
			trigger1.text("Something");
			PlayerTriggerPersistenceObject trigger2 = new PlayerTriggerPersistenceObject();
			trigger2.comparisonData("Somethingelse");
			triggers.add(trigger1);
			triggers.add(trigger2);
			
			persist.triggers(triggers);
			
			assertNotNull(persist.triggers());
			assertEquals(2, persist.triggers().size());
			
			persist.clearTriggers();
			
			assertNotNull(persist.triggers());
			assertEquals(0, persist.triggers().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			_manager = new XmlConfigurationManager(_multiTriggerUnitTest);
			MultiPartTriggerPersistenceObject persistence = new MultiPartTriggerPersistenceObject();
			
			TextTriggerPersistenceObject trigger1 = new TextTriggerPersistenceObject();
			trigger1.text("Test");
			trigger1.caseSensitive(false);
			trigger1.matchType(MatchType.Contains);
			
			TextTriggerPersistenceObject trigger2 = new TextTriggerPersistenceObject();
			trigger2.text("Test");
			trigger2.caseSensitive(true);
			trigger2.matchType(MatchType.Exact);
			
			PlayerTriggerPersistenceObject trigger3 = new PlayerTriggerPersistenceObject();
			trigger3.comparisonData(13);
			trigger3.condition(Condition.GreaterThan);
			trigger3.modificationObject(ModificationObject.Attribute);
			trigger3.dataMember("value");
			
			persistence.addTrigger(trigger1);
			persistence.addTrigger(trigger2);
			persistence.addTrigger(trigger3);
			
			persistence.prepareXml();
			_manager.addConfigurationObject(persistence);
			_manager.save();
			
			String fileContents = fileContents(_multiTriggerUnitTest);
			assertEquals(_expectedMultiTriggerText, fileContents);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertToTrigger() {
		try {
			MultiPartTriggerPersistenceObject persistence = new MultiPartTriggerPersistenceObject();
			
			TextTriggerPersistenceObject trigger1 = new TextTriggerPersistenceObject();
			trigger1.text("Test");
			trigger1.caseSensitive(false);
			trigger1.matchType(MatchType.Contains);
			
			PlayerTriggerPersistenceObject trigger3 = new PlayerTriggerPersistenceObject();
			trigger3.comparisonData(13);
			trigger3.condition(Condition.GreaterThan);
			trigger3.modificationObject(ModificationObject.Attribute);
			trigger3.dataMember("value");
			trigger3.id(new String[]{"age"});
			trigger3.playerName("Player");
			
			persistence.addTrigger(trigger1);
			persistence.addTrigger(trigger3);
			
			MultiPartTrigger triggerResult = (MultiPartTrigger)persistence.convertToTrigger();
			IPlayer player = new Player("Player");
			player.addAttribute(new Attribute("age", 14));
			List<IPlayer> players = new ArrayList<IPlayer>();
			players.add(player);
			
			TriggerParameters passingParams = new TriggerParameters("test", players);
			assertTrue(triggerResult.shouldFire(passingParams));
			
			TriggerParameters failingParams = new TriggerParameters("bad text", players);
			assertFalse(triggerResult.shouldFire(failingParams));
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertFromPersistence() {
		try {
			_manager = new XmlConfigurationManager(_multiTriggerUnitTest2);
			MultiPartTriggerPersistenceObject persistence = new MultiPartTriggerPersistenceObject();
			
			TextTriggerPersistenceObject trigger1 = new TextTriggerPersistenceObject();
			trigger1.text("Test");
			trigger1.caseSensitive(false);
			trigger1.matchType(MatchType.Contains);
			
			TextTriggerPersistenceObject trigger2 = new TextTriggerPersistenceObject();
			trigger2.text("Test");
			trigger2.caseSensitive(true);
			trigger2.matchType(MatchType.Exact);
			
			PlayerTriggerPersistenceObject trigger3 = new PlayerTriggerPersistenceObject();
			trigger3.comparisonData(13);
			trigger3.condition(Condition.GreaterThan);
			trigger3.modificationObject(ModificationObject.Attribute);
			trigger3.dataMember("value");
			
			persistence.addTrigger(trigger1);
			persistence.addTrigger(trigger2);
			persistence.addTrigger(trigger3);
			
			persistence.prepareXml();
			_manager.addConfigurationObject(persistence);
			_manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(_multiTriggerUnitTest2);
			manager2.load();
			
			MultiPartTriggerPersistenceObject result = new MultiPartTriggerPersistenceObject();
			result.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals(3, result.triggers().size());
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
