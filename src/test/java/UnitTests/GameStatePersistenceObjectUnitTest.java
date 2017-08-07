package UnitTests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.gamestates.TextAdventureGameState;
import textadventurelib.persistence.AppendTextActionPersistence;
import textadventurelib.persistence.CompletionActionPersistence;
import textadventurelib.persistence.CompletionTimerPersistenceObject;
import textadventurelib.persistence.ExecutionActionPersistence;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.LayoutInfoPersistenceObject;
import textadventurelib.persistence.LayoutType;
import textadventurelib.persistence.MatchType;
import textadventurelib.persistence.ModifyPlayerActionPersistence;
import textadventurelib.persistence.OptionPersistenceObject;
import textadventurelib.persistence.PlayerTriggerPersistenceObject;
import textadventurelib.persistence.TextTriggerPersistenceObject;

public class GameStatePersistenceObjectUnitTest {

	private final String _gameStateUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/gameStateUnitTest.xml";
	private final String _gameStateUnitTest2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/gameStateUnitTest2.xml";
	
	private final String _expectedGameStateText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GameState><StateId ValueType=\"string\">MainState</StateId><LayoutInfo><LayoutID ValueType=\"string\"/><LayoutType ValueType=\"object\">TextAndContentWithTextInput</LayoutType><LayoutContent ValueType=\"string\">C:/Something.jpg</LayoutContent></LayoutInfo><TextLog ValueType=\"string\">This is a simple text log</TextLog><Options><Option><Triggers><Trigger type=\"Text\"><Parameters><Text ValueType=\"string\">next</Text><CaseSensitive ValueType=\"bool\">false</CaseSensitive><MatchType ValueType=\"object\">Contains</MatchType></Parameters></Trigger></Triggers><Action type=\"Completion\"><Parameters><CompletionData ValueType=\"string\">State2</CompletionData></Parameters></Action></Option><Option><Triggers><Trigger type=\"Text\"><Parameters><Text ValueType=\"string\">fire</Text><CaseSensitive ValueType=\"bool\">false</CaseSensitive><MatchType ValueType=\"object\">Prefix</MatchType></Parameters></Trigger></Triggers><Action type=\"Execute\"><Parameters><Executable ValueType=\"string\">FireFox.exe</Executable></Parameters></Action></Option></Options><Timers><Timer type=\"Completion\"><Duration ValueType=\"object\">5000</Duration><CompletionData ValueType=\"string\">State3</CompletionData></Timer></Timers></GameState>";
	
	private final String _stateId1 = "MainState";
	private final String _stateId2 = "MenuState";
	private final String _stateId3 = "FakeState";
	
	private final String _textLog1 = "This is a simple text log";
	private final String _textLog2 = "This is another text log";
	private final String _textLog3 = "Last text log";
	
	@Test
	public void testCreate() {
		try {
			GameStatePersistenceObject persist = new GameStatePersistenceObject(_stateId1);
			
			assertNotNull(persist);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testSetStateId() {
		try {
			GameStatePersistenceObject persist = new GameStatePersistenceObject(_stateId1);
			
			assertEquals(_stateId1, persist.stateId());
			
			persist.stateId(_stateId2);
			assertEquals(_stateId2, persist.stateId());
			
			persist.stateId(_stateId3);
			assertEquals(_stateId3, persist.stateId());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetTextLog() {
		try {
			GameStatePersistenceObject persist = new GameStatePersistenceObject(_stateId1);
			
			persist.textLog(_textLog1);
			assertEquals(_textLog1, persist.textLog());
			
			persist.textLog(_textLog2);
			assertEquals(_textLog2, persist.textLog());
			
			persist.textLog(_textLog3);
			assertEquals(_textLog3, persist.textLog());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetLayout() {
		try {
			GameStatePersistenceObject persist = new GameStatePersistenceObject(_stateId1);
			
			LayoutInfoPersistenceObject layout1 = new LayoutInfoPersistenceObject();
			layout1.setLayoutType(LayoutType.TextAndContentWithTextInput);
			LayoutInfoPersistenceObject layout2 = new LayoutInfoPersistenceObject();
			layout2.setLayoutType(LayoutType.ContentOnly);
			layout2.setLayoutContent("C:/SomeFile.png");
			LayoutInfoPersistenceObject layout3 = new LayoutInfoPersistenceObject();
			layout3.setLayoutType(LayoutType.TextAndContentWithButtonInput);
			
			persist.layout(layout1);
			assertEquals(layout1, persist.layout());
			
			persist.layout(layout2);
			assertEquals(layout2, persist.layout());
			
			persist.layout(layout3);
			assertEquals(layout3, persist.layout());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testAddOption() {
		try {
			GameStatePersistenceObject persist = new GameStatePersistenceObject(_stateId1);
			
			OptionPersistenceObject option1 = new OptionPersistenceObject();
			option1.action(new CompletionActionPersistence());
			option1.addTrigger(new TextTriggerPersistenceObject());
			OptionPersistenceObject option2 = new OptionPersistenceObject();
			option2.action(new AppendTextActionPersistence());
			option2.addTrigger(new TextTriggerPersistenceObject());
			OptionPersistenceObject option3 = new OptionPersistenceObject();
			option3.action(new ModifyPlayerActionPersistence());
			option3.addTrigger(new PlayerTriggerPersistenceObject());
			
			persist.addOption(option1);
			assertEquals(1, persist.options().size());
			assertEquals(option1, persist.options().get(0));
			
			persist.addOption(option2);
			assertEquals(2, persist.options().size());
			assertEquals(option2, persist.options().get(1));
			
			persist.addOption(option3);
			assertEquals(3, persist.options().size());
			assertEquals(option3, persist.options().get(2));
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testRemoveOption() {
		try {
			GameStatePersistenceObject persist = new GameStatePersistenceObject(_stateId1);
			
			OptionPersistenceObject option1 = new OptionPersistenceObject();
			option1.action(new CompletionActionPersistence());
			option1.addTrigger(new TextTriggerPersistenceObject());
			OptionPersistenceObject option2 = new OptionPersistenceObject();
			option2.action(new AppendTextActionPersistence());
			option2.addTrigger(new TextTriggerPersistenceObject());
			OptionPersistenceObject option3 = new OptionPersistenceObject();
			option3.action(new ModifyPlayerActionPersistence());
			option3.addTrigger(new PlayerTriggerPersistenceObject());
			
			persist.addOption(option1);
			persist.addOption(option2);
			persist.addOption(option3);
			
			assertEquals(3, persist.options().size());
			
			persist.removeOption(option1);
			assertEquals(2, persist.options().size());
			
			persist.removeOption(option2);
			assertEquals(1, persist.options().size());
			
			persist.removeOption(option3);
			assertEquals(0, persist.options().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testClearOptions() {
		try {
			GameStatePersistenceObject persist = new GameStatePersistenceObject(_stateId1);
			
			OptionPersistenceObject option1 = new OptionPersistenceObject();
			option1.action(new CompletionActionPersistence());
			option1.addTrigger(new TextTriggerPersistenceObject());
			OptionPersistenceObject option2 = new OptionPersistenceObject();
			option2.action(new AppendTextActionPersistence());
			option2.addTrigger(new TextTriggerPersistenceObject());
			OptionPersistenceObject option3 = new OptionPersistenceObject();
			option3.action(new ModifyPlayerActionPersistence());
			option3.addTrigger(new PlayerTriggerPersistenceObject());
			
			persist.addOption(option1);
			persist.addOption(option2);
			persist.addOption(option3);
			
			assertEquals(3, persist.options().size());
			
			persist.clearOptions();
			assertEquals(0, persist.options().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testAddTimer() {
		try {
			GameStatePersistenceObject persist = new GameStatePersistenceObject(_stateId1);
			
			CompletionTimerPersistenceObject timer1 = new CompletionTimerPersistenceObject();
			timer1.duration(5000);
			timer1.completionData("a");
			CompletionTimerPersistenceObject timer2 = new CompletionTimerPersistenceObject();
			timer2.duration(25000);
			timer2.completionData("b");
			CompletionTimerPersistenceObject timer3 = new CompletionTimerPersistenceObject();
			timer3.duration(15000);
			timer3.completionData("c");
			
			persist.addTimer(timer1);
			assertEquals(1, persist.timers().size());
			assertEquals(timer1, persist.timers().get(0));
			
			persist.addTimer(timer2);
			assertEquals(2, persist.timers().size());
			assertEquals(timer2, persist.timers().get(1));
			
			persist.addTimer(timer3);
			assertEquals(3, persist.timers().size());
			assertEquals(timer3, persist.timers().get(2));
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testRemoveTimer() {
		try {
			GameStatePersistenceObject persist = new GameStatePersistenceObject(_stateId1);
			
			CompletionTimerPersistenceObject timer1 = new CompletionTimerPersistenceObject();
			timer1.duration(5000);
			timer1.completionData("a");
			CompletionTimerPersistenceObject timer2 = new CompletionTimerPersistenceObject();
			timer2.duration(25000);
			timer2.completionData("b");
			CompletionTimerPersistenceObject timer3 = new CompletionTimerPersistenceObject();
			timer3.duration(15000);
			timer3.completionData("c");
			
			persist.addTimer(timer1);
			persist.addTimer(timer2);
			persist.addTimer(timer3);
			assertEquals(3, persist.timers().size());
			
			persist.removeTimer(timer1);
			assertEquals(2, persist.timers().size());
			
			persist.removeTimer(timer2);
			assertEquals(1, persist.timers().size());
			
			persist.removeTimer(timer3);
			assertEquals(0, persist.timers().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testClearTimer() {
		try {
			GameStatePersistenceObject persist = new GameStatePersistenceObject(_stateId1);
			
			CompletionTimerPersistenceObject timer1 = new CompletionTimerPersistenceObject();
			timer1.duration(5000);
			timer1.completionData("a");
			CompletionTimerPersistenceObject timer2 = new CompletionTimerPersistenceObject();
			timer2.duration(25000);
			timer2.completionData("b");
			CompletionTimerPersistenceObject timer3 = new CompletionTimerPersistenceObject();
			timer3.duration(15000);
			timer3.completionData("c");
			
			persist.addTimer(timer1);
			persist.addTimer(timer2);
			persist.addTimer(timer3);
			assertEquals(3, persist.timers().size());
			
			persist.clearTimers();
			assertEquals(0, persist.timers().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			XmlConfigurationManager manager = new XmlConfigurationManager(_gameStateUnitTest);
			GameStatePersistenceObject persist = new GameStatePersistenceObject(_stateId1);
			
			persist.textLog(_textLog1);
			
			LayoutInfoPersistenceObject layout = new LayoutInfoPersistenceObject();
			layout.setLayoutType(LayoutType.TextAndContentWithTextInput);
			layout.setLayoutContent("C:/Something.jpg");
			persist.layout(layout);
			
			OptionPersistenceObject option1 = new OptionPersistenceObject();
			TextTriggerPersistenceObject trigger1 = new TextTriggerPersistenceObject();
			trigger1.text("next");
			trigger1.caseSensitive(false);
			trigger1.matchType(MatchType.Contains);
			CompletionActionPersistence action1 = new CompletionActionPersistence();
			action1.completionData("State2");
			option1.addTrigger(trigger1);
			option1.action(action1);

			OptionPersistenceObject option2 = new OptionPersistenceObject();
			TextTriggerPersistenceObject trigger2 = new TextTriggerPersistenceObject();
			trigger2.text("fire");
			trigger2.caseSensitive(false);
			trigger2.matchType(MatchType.Prefix);
			ExecutionActionPersistence action2 = new ExecutionActionPersistence();
			action2.executable("FireFox.exe");
			option2.addTrigger(trigger2);
			option2.action(action2);
			
			CompletionTimerPersistenceObject timer1 = new CompletionTimerPersistenceObject();
			timer1.completionData("State3");
			timer1.duration(5000);
			
			persist.addOption(option1);
			persist.addOption(option2);
			persist.addTimer(timer1);
			
			persist.prepareXml();
			manager.addConfigurationObject(persist);
			manager.save();
			
			String fileContents = fileContents(_gameStateUnitTest);
			assertEquals(_expectedGameStateText, fileContents);
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertToGameState() {
		try {
			GameStatePersistenceObject persist = new GameStatePersistenceObject(_stateId1);
			
			persist.textLog(_textLog1);
			
			LayoutInfoPersistenceObject layout = new LayoutInfoPersistenceObject();
			layout.setLayoutType(LayoutType.TextAndContentWithTextInput);
			layout.setLayoutContent("C:/Program Files (x86)/nilrem/UnitTests/TAL/Something.png");
			persist.layout(layout);
			
			OptionPersistenceObject option1 = new OptionPersistenceObject();
			TextTriggerPersistenceObject trigger1 = new TextTriggerPersistenceObject();
			trigger1.text("next");
			trigger1.caseSensitive(false);
			trigger1.matchType(MatchType.Contains);
			CompletionActionPersistence action1 = new CompletionActionPersistence();
			action1.completionData("State2");
			option1.addTrigger(trigger1);
			option1.action(action1);

			OptionPersistenceObject option2 = new OptionPersistenceObject();
			TextTriggerPersistenceObject trigger2 = new TextTriggerPersistenceObject();
			trigger2.text("fire");
			trigger2.caseSensitive(false);
			trigger2.matchType(MatchType.Prefix);
			ExecutionActionPersistence action2 = new ExecutionActionPersistence();
			action2.executable("FireFox.exe");
			action2.blocking(false);
			option2.addTrigger(trigger2);
			option2.action(action2);
			
			CompletionTimerPersistenceObject timer1 = new CompletionTimerPersistenceObject();
			timer1.completionData("State3");
			timer1.duration(5000);
			
			persist.addOption(option1);
			persist.addOption(option2);
			persist.addTimer(timer1);
			
			TextAdventureGameState gameState = (TextAdventureGameState)persist.convertToGameState();
			assertNotNull(gameState);
			assertEquals(_textLog1, gameState.textLog()); 
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertFromPersistence() {
		try {
			XmlConfigurationManager manager = new XmlConfigurationManager(_gameStateUnitTest2);
			GameStatePersistenceObject persist = new GameStatePersistenceObject(_stateId1);
			
			persist.textLog(_textLog1);
			
			LayoutInfoPersistenceObject layout = new LayoutInfoPersistenceObject();
			layout.setLayoutType(LayoutType.TextAndContentWithTextInput);
			layout.setLayoutContent("C:/Program Files (x86)/nilrem/UnitTests/TAL/Something.png");
			persist.layout(layout);
			
			OptionPersistenceObject option1 = new OptionPersistenceObject();
			TextTriggerPersistenceObject trigger1 = new TextTriggerPersistenceObject();
			trigger1.text("next");
			trigger1.caseSensitive(false);
			trigger1.matchType(MatchType.Contains);
			CompletionActionPersistence action1 = new CompletionActionPersistence();
			action1.completionData("State2");
			option1.addTrigger(trigger1);
			option1.action(action1);

			OptionPersistenceObject option2 = new OptionPersistenceObject();
			TextTriggerPersistenceObject trigger2 = new TextTriggerPersistenceObject();
			trigger2.text("fire");
			trigger2.caseSensitive(false);
			trigger2.matchType(MatchType.Prefix);
			ExecutionActionPersistence action2 = new ExecutionActionPersistence();
			action2.executable("FireFox.exe");
			option2.addTrigger(trigger2);
			option2.action(action2);
			
			CompletionTimerPersistenceObject timer1 = new CompletionTimerPersistenceObject();
			timer1.completionData("State3");
			timer1.duration(5000);
			
			persist.addOption(option1);
			persist.addOption(option2);
			persist.addTimer(timer1);
			
			persist.prepareXml();
			manager.addConfigurationObject(persist);
			manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(_gameStateUnitTest2);
			manager2.load();
			
			GameStatePersistenceObject persist2 = new GameStatePersistenceObject("");
			persist2.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals(_textLog1, persist2.textLog());
			assertEquals(LayoutType.TextAndContentWithTextInput, persist2.layout().getLayoutType());
			assertEquals("C:/Program Files (x86)/nilrem/UnitTests/TAL/Something.png", persist2.layout().getLayoutContent());
			assertEquals(2, persist2.options().size());
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
