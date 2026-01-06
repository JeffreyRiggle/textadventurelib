import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.junit.Test;

import textadventurelib.persistence.DisplayType;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.LayoutInfoPersistenceObject;
import textadventurelib.persistence.LayoutType;
import textadventurelib.persistence.TextAdventurePersistenceManager;
import textadventurelib.persistence.TextAdventurePersistenceObject;
import textadventurelib.persistence.TransitionPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

public class TextAdventurePersistenceManagerUnitTest {

	private final String _saveLocation1 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/managerUnitTest1.xml";
	private final String _saveLocation2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/managerUnitTest2.xml";
	private final String _expectedText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><TextAdventure inlineLayouts=\"false\" inlinegamestate=\"false\" inlineplayers=\"false\"><Name ValueType=\"string\">Super Cool Name</Name><Transition><DisplayType ValueType=\"object\">Window</DisplayType><MediaLocation ValueType=\"string\">C:/Something.avi</MediaLocation></Transition><CurrentGameState ValueType=\"string\">Main</CurrentGameState><PlayersLocation ValueType=\"string\">C:/Program Files (x86)/nilrem/UnitTests/TAL/Players1.xml</PlayersLocation><GameStatesLocation ValueType=\"string\">C:/Program Files (x86)/nilrem/UnitTests/TAL/GameStates1.xml</GameStatesLocation><LayoutLocation/><Buffer ValueType=\"int\">0</Buffer></TextAdventure>";
	
	@Test
	public void testCreate() {
		try {
			TextAdventurePersistenceManager persistenceManager = new TextAdventurePersistenceManager(_saveLocation1);
			
			assertNotNull(persistenceManager);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testSetTextAdventure() {
		try {
			TextAdventurePersistenceManager persistenceManager = new TextAdventurePersistenceManager(_saveLocation1);
			
			TextAdventurePersistenceObject persist = createTextAdventure();
			persistenceManager.textAdventure(persist);
			
			assertEquals(persist, persistenceManager.textAdventure());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			TextAdventurePersistenceManager persistenceManager = new TextAdventurePersistenceManager(_saveLocation1);
			
			TextAdventurePersistenceObject persist = createTextAdventure();
			persistenceManager.textAdventure(persist);
			
			persistenceManager.save();
			
			String fileContents = fileContents(_saveLocation1);
			assertEquals(_expectedText, fileContents);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testLoad() {
		try {
			TextAdventurePersistenceManager persistenceManager = new TextAdventurePersistenceManager(_saveLocation2);
			
			TextAdventurePersistenceObject persist = createTextAdventure();
			persistenceManager.textAdventure(persist);
			
			persistenceManager.save();
			
			TextAdventurePersistenceManager persistenceManager2 = new TextAdventurePersistenceManager(_saveLocation2);
			persistenceManager2.load();
			
			assertEquals("Super Cool Name", persistenceManager2.textAdventure().gameName());
			assertEquals("C:/Something.avi", persistenceManager2.textAdventure().transition().mediaLocation());
			assertEquals(DisplayType.Window, persistenceManager2.textAdventure().transition().displayType());
			assertEquals("C:/Program Files (x86)/nilrem/UnitTests/TAL/GameStates1.xml", persistenceManager2.textAdventure().gameStatesLocation());
			assertEquals("C:/Program Files (x86)/nilrem/UnitTests/TAL/Players1.xml", persistenceManager2.textAdventure().playersLocation());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	private TextAdventurePersistenceObject createTextAdventure() throws TransformerConfigurationException, ParserConfigurationException {
		
		TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();
		TransitionPersistenceObject trans = new TransitionPersistenceObject();
		trans.mediaLocation("C:/Something.avi");
		trans.displayType(DisplayType.Window);
		
		persist.gameName("Super Cool Name");
		persist.currentGameState("Main");
		persist.playersInline(false);
		persist.gameStatesInline(false);
		persist.gameStatesLocation("C:/Program Files (x86)/nilrem/UnitTests/TAL/GameStates1.xml");
		persist.playersLocation("C:/Program Files (x86)/nilrem/UnitTests/TAL/Players1.xml");
		persist.transition(trans);
		
		LayoutInfoPersistenceObject layout1 = new LayoutInfoPersistenceObject();
		layout1.setLayoutType(LayoutType.TextWithTextInput);
		LayoutInfoPersistenceObject layout2 = new LayoutInfoPersistenceObject();
		layout2.setLayoutType(LayoutType.TextWithTextInput);
		LayoutInfoPersistenceObject layout3 = new LayoutInfoPersistenceObject();
		layout3.setLayoutType(LayoutType.TextWithTextInput);
		
		GameStatePersistenceObject gameState1 = new GameStatePersistenceObject("Main");
		gameState1.layout(layout1);
		gameState1.textLog("Main Game State");
		GameStatePersistenceObject gameState2 = new GameStatePersistenceObject("GS1");
		gameState2.layout(layout2);
		gameState2.textLog("Game State 1");
		GameStatePersistenceObject gameState3 = new GameStatePersistenceObject("BossFight");
		gameState3.layout(layout3);
		gameState3.textLog("Boss fight");
		
		persist.addGameState(gameState1);
		persist.addGameState(gameState2);
		persist.addGameState(gameState3);
		
		List<PlayerPersistenceObject> players = new ArrayList<PlayerPersistenceObject>();
		PlayerPersistenceObject player = new PlayerPersistenceObject();

		players.add(player);
		persist.players(players);
		return persist;
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
