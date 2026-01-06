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
import textadventurelib.options.IOption;
import textadventurelib.persistence.AppendTextActionPersistence;
import textadventurelib.persistence.CompletionActionPersistence;
import textadventurelib.persistence.MatchType;
import textadventurelib.persistence.OptionPersistenceObject;
import textadventurelib.persistence.PlayerTriggerPersistenceObject;
import textadventurelib.persistence.TextTriggerPersistenceObject;

public class ObjectPersistenceUnitTest {

	private XmlConfigurationManager _manager;
	private final String _optionPersistenceUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/optionUnitTest.xml";
	private final String _optionPersistenceUnitTest2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/optionUnitTest2.xml";
	
	private final String _expectedOptionPersistence = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Option><Triggers><Trigger type=\"Text\"><Parameters><Text ValueType=\"string\">Test</Text><CaseSensitive ValueType=\"bool\">false</CaseSensitive><MatchType ValueType=\"object\">Contains</MatchType></Parameters></Trigger><Trigger type=\"Text\"><Parameters><Text ValueType=\"string\">Something</Text><CaseSensitive ValueType=\"bool\">true</CaseSensitive><MatchType ValueType=\"object\">Exact</MatchType></Parameters></Trigger><Trigger type=\"Player\"><Parameters><Condition ValueType=\"object\">GreaterThan</Condition><ComparisionData ValueType=\"int\">13</ComparisionData><DataMember ValueType=\"string\">value</DataMember><ID ValueType=\"string\">age|,</ID><ModificationObject ValueType=\"object\">Attribute</ModificationObject></Parameters></Trigger></Triggers><Action type=\"AppendText\"><Parameters><AppendText ValueType=\"string\">Append Text</AppendText></Parameters></Action></Option>";
	
	@Test
	public void testCreate() {
		try {
			OptionPersistenceObject persist = new OptionPersistenceObject();
			
			assertNotNull(persist);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testAddTrigger() {
		try {
			OptionPersistenceObject persist = new OptionPersistenceObject();
			
			TextTriggerPersistenceObject trigger1 = new TextTriggerPersistenceObject();
			trigger1.text("Test");
			trigger1.caseSensitive(false);
			trigger1.matchType(MatchType.Contains);
			
			TextTriggerPersistenceObject trigger2 = new TextTriggerPersistenceObject();
			trigger2.text("Something");
			trigger2.caseSensitive(true);
			trigger2.matchType(MatchType.Exact);
			
			PlayerTriggerPersistenceObject trigger3 = new PlayerTriggerPersistenceObject();
			trigger3.condition(Condition.GreaterThan);
			trigger3.comparisonData(13);
			trigger3.dataMember("value");
			trigger3.id(new String[]{"age"});
			trigger3.modificationObject(ModificationObject.Attribute);
			
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
			OptionPersistenceObject persist = new OptionPersistenceObject();
			
			TextTriggerPersistenceObject trigger1 = new TextTriggerPersistenceObject();
			trigger1.text("Test");
			trigger1.caseSensitive(false);
			trigger1.matchType(MatchType.Contains);
			
			TextTriggerPersistenceObject trigger2 = new TextTriggerPersistenceObject();
			trigger2.text("Something");
			trigger2.caseSensitive(true);
			trigger2.matchType(MatchType.Exact);
			
			PlayerTriggerPersistenceObject trigger3 = new PlayerTriggerPersistenceObject();
			trigger3.condition(Condition.GreaterThan);
			trigger3.comparisonData(13);
			trigger3.dataMember("value");
			trigger3.id(new String[]{"age"});
			trigger3.modificationObject(ModificationObject.Attribute);
			
			persist.addTrigger(trigger1);
			persist.addTrigger(trigger2);
			persist.addTrigger(trigger3);

			assertEquals(3, persist.triggers().size());
			
			persist.removeTrigger(trigger1);
			assertEquals(2, persist.triggers().size());
			
			persist.removeTrigger(trigger2);
			assertEquals(1, persist.triggers().size());
			
			persist.removeTrigger(trigger3);
			assertEquals(0, persist.triggers().size());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testClearTriggers() {
		try {
			OptionPersistenceObject persist = new OptionPersistenceObject();
			
			TextTriggerPersistenceObject trigger1 = new TextTriggerPersistenceObject();
			trigger1.text("Test");
			trigger1.caseSensitive(false);
			trigger1.matchType(MatchType.Contains);
			
			TextTriggerPersistenceObject trigger2 = new TextTriggerPersistenceObject();
			trigger2.text("Something");
			trigger2.caseSensitive(true);
			trigger2.matchType(MatchType.Exact);
			
			PlayerTriggerPersistenceObject trigger3 = new PlayerTriggerPersistenceObject();
			trigger3.condition(Condition.GreaterThan);
			trigger3.comparisonData(13);
			trigger3.dataMember("value");
			trigger3.id(new String[]{"age"});
			trigger3.modificationObject(ModificationObject.Attribute);
			
			persist.addTrigger(trigger1);
			persist.addTrigger(trigger2);
			persist.addTrigger(trigger3);
			
			persist.clearTriggers();
			assertEquals(0, persist.triggers().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetAction() {
		try {
			OptionPersistenceObject persist = new OptionPersistenceObject();
			
			AppendTextActionPersistence action = new AppendTextActionPersistence();
			action.appendText("Append Text");
			persist.action(action);
			
			assertEquals(action, persist.action());
			
			CompletionActionPersistence action2 = new CompletionActionPersistence();
			action2.completionData("Next State");
			persist.action(action2);
			
			assertEquals(action2, persist.action());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			_manager = new XmlConfigurationManager(_optionPersistenceUnitTest);
			OptionPersistenceObject persist = new OptionPersistenceObject();
			
			TextTriggerPersistenceObject trigger1 = new TextTriggerPersistenceObject();
			trigger1.text("Test");
			trigger1.caseSensitive(false);
			trigger1.matchType(MatchType.Contains);
			TextTriggerPersistenceObject trigger2 = new TextTriggerPersistenceObject();
			trigger2.text("Something");
			trigger2.caseSensitive(true);
			trigger2.matchType(MatchType.Exact);
			PlayerTriggerPersistenceObject trigger3 = new PlayerTriggerPersistenceObject();
			trigger3.condition(Condition.GreaterThan);
			trigger3.comparisonData(13);
			trigger3.dataMember("value");
			trigger3.id(new String[]{"age"});
			trigger3.modificationObject(ModificationObject.Attribute);
			persist.addTrigger(trigger1);
			persist.addTrigger(trigger2);
			persist.addTrigger(trigger3);
			
			AppendTextActionPersistence action = new AppendTextActionPersistence();
			action.appendText("Append Text");
			persist.action(action);
			
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			String fileContents = fileContents(_optionPersistenceUnitTest);
			assertEquals(_expectedOptionPersistence, fileContents);
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}

	@Test
	public void testConvertToOption() {
		try {
			OptionPersistenceObject persist = new OptionPersistenceObject();
			
			TextTriggerPersistenceObject trigger1 = new TextTriggerPersistenceObject();
			trigger1.text("Test");
			trigger1.caseSensitive(false);
			trigger1.matchType(MatchType.Contains);
			TextTriggerPersistenceObject trigger2 = new TextTriggerPersistenceObject();
			trigger2.text("Something");
			trigger2.caseSensitive(true);
			trigger2.matchType(MatchType.Exact);
			PlayerTriggerPersistenceObject trigger3 = new PlayerTriggerPersistenceObject();
			trigger3.condition(Condition.GreaterThan);
			trigger3.comparisonData(13);
			trigger3.dataMember("value");
			trigger3.id(new String[]{"age"});
			trigger3.modificationObject(ModificationObject.Attribute);
			persist.addTrigger(trigger1);
			persist.addTrigger(trigger2);
			persist.addTrigger(trigger3);
			
			AppendTextActionPersistence action = new AppendTextActionPersistence();
			action.appendText("Append Text");
			persist.action(action);
			
			IOption option = persist.convertToOption();
			assertEquals(3, option.triggers().size());
			assertNotNull(option.action());
			
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertFromPersistence() {
		try {
			_manager = new XmlConfigurationManager(_optionPersistenceUnitTest2);
			OptionPersistenceObject persist = new OptionPersistenceObject();
			
			TextTriggerPersistenceObject trigger1 = new TextTriggerPersistenceObject();
			trigger1.text("Test");
			trigger1.caseSensitive(false);
			trigger1.matchType(MatchType.Contains);
			TextTriggerPersistenceObject trigger2 = new TextTriggerPersistenceObject();
			trigger2.text("Something");
			trigger2.caseSensitive(true);
			trigger2.matchType(MatchType.Exact);
			PlayerTriggerPersistenceObject trigger3 = new PlayerTriggerPersistenceObject();
			trigger3.condition(Condition.GreaterThan);
			trigger3.comparisonData(13);
			trigger3.dataMember("value");
			trigger3.id(new String[]{"age"});
			trigger3.modificationObject(ModificationObject.Attribute);
			persist.addTrigger(trigger1);
			persist.addTrigger(trigger2);
			persist.addTrigger(trigger3);
			
			AppendTextActionPersistence action = new AppendTextActionPersistence();
			action.appendText("Append Text");
			persist.action(action);
			
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(_optionPersistenceUnitTest2);
			manager2.load();
			
			OptionPersistenceObject persist2 = new OptionPersistenceObject();
			persist2.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals(3, persist2.triggers().size());
			assertNotNull(persist2.action());
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
