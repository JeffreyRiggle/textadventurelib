package UnitTests;

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

import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.core.BufferedTextAdventureGameStateManager;
import textadventurelib.core.ITextAdventureGameStateManager;
import textadventurelib.persistence.DisplayType;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.LayoutInfoPersistenceObject;
import textadventurelib.persistence.LayoutType;
import textadventurelib.persistence.TextAdventurePersistenceObject;
import textadventurelib.persistence.TransitionPersistenceObject;
import textadventurelib.persistence.player.AttributePersistenceObject;
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;
import textadventurelib.persistence.player.EquipmentPersistenceObject;
import textadventurelib.persistence.player.InventoryPersistenceObject;
import textadventurelib.persistence.player.ItemPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

//TODO: A refactor/clean up might be nice.
public class TextAdventurePersistenceUnitTest {

	private XmlConfigurationManager _manager;
	private final String _gameState1UnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/tav1UnitTest.xml";
	private final String _expectedGameState1Text = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><TextAdventure inlineLayouts=\"false\" inlinegamestate=\"false\" inlineplayers=\"true\"><Name ValueType=\"string\">Super Cool Name</Name><Transition><DisplayType ValueType=\"object\">Window</DisplayType><MediaLocation ValueType=\"string\">C:/Something.avi</MediaLocation></Transition><CurrentGameState ValueType=\"string\">Main</CurrentGameState><Players><Player><Name ValueType=\"string\">Deemo</Name><Attributes><NamedObject type=\"Attribute\"><Name ValueType=\"string\">Age</Name><Description/><Value ValueType=\"int\">15</Value></NamedObject><NamedObject type=\"Attribute\"><Name ValueType=\"string\">Class</Name><Description/><Value ValueType=\"string\">Wizard</Value></NamedObject><NamedObject type=\"Attribute\"><Name ValueType=\"string\">Max Health</Name><Description/><Value ValueType=\"int\">5000</Value></NamedObject></Attributes><Characteristics><NamedObject type=\"Characteristic\"><Name ValueType=\"string\">Hair Color</Name><Description/><Value ValueType=\"string\">Brown</Value></NamedObject><NamedObject type=\"Characteristic\"><Name ValueType=\"string\">Scar</Name><Description/><Value ValueType=\"string\">Long</Value></NamedObject><NamedObject type=\"Characteristic\"><Name ValueType=\"string\">Skin Color</Name><Description/><Value ValueType=\"string\">White</Value></NamedObject></Characteristics><BodyParts><BodyPart><Name ValueType=\"string\">Head</Name><Description/><Characteristics/></BodyPart><BodyPart><Name ValueType=\"string\">Feet</Name><Description/><Characteristics/></BodyPart><BodyPart><Name ValueType=\"string\">Tourso</Name><Description/><Characteristics/></BodyPart></BodyParts><Inventory><Item Amount=\"5\"><Name ValueType=\"string\">Potion</Name><Description ValueType=\"string\">Restores HP</Description><Content/><Properties/></Item><Item Amount=\"2\"><Name ValueType=\"string\">Ether</Name><Description ValueType=\"string\">Restores MP</Description><Content/><Properties/></Item><Item Amount=\"10\"><Name ValueType=\"string\">Phenoix Down</Name><Description ValueType=\"string\">Revives</Description><Content/><Properties/></Item></Inventory><Equipment><Equiptable><Item><Name ValueType=\"string\">Helmet</Name><Description ValueType=\"string\">Protects the head</Description><Content/><Properties/></Item><BodyPart><Name ValueType=\"string\">Head</Name><Description/><Characteristics/></BodyPart></Equiptable><Equiptable><Item><Name ValueType=\"string\">Steel Boots</Name><Description ValueType=\"string\">Protects the feet</Description><Content/><Properties/></Item><BodyPart><Name ValueType=\"string\">Feet</Name><Description/><Characteristics/></BodyPart></Equiptable><Equiptable><Item><Name ValueType=\"string\">Leather Vest</Name><Description ValueType=\"string\">Protects the body</Description><Content/><Properties/></Item><BodyPart><Name ValueType=\"string\">Tourso</Name><Description/><Characteristics/></BodyPart></Equiptable></Equipment></Player></Players><GameStatesLocation ValueType=\"string\">" + System.getProperty("user.home") + "/nilrem/UnitTests/TAL/GameStates.xml</GameStatesLocation><LayoutLocation/><Buffer ValueType=\"int\">0</Buffer></TextAdventure>";
	private final String _gameState2UnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/tav2UnitTest.xml";
	private final String _expectedGameState2Text = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><TextAdventure inlineLayouts=\"false\" inlinegamestate=\"true\" inlineplayers=\"false\"><Name ValueType=\"string\">Super Cool Name</Name><Transition><DisplayType ValueType=\"object\">Window</DisplayType><MediaLocation ValueType=\"string\">C:/Something.avi</MediaLocation></Transition><CurrentGameState ValueType=\"string\">Main</CurrentGameState><PlayersLocation ValueType=\"string\">" + System.getProperty("user.home") + "/nilrem/UnitTests/TAL/Players.xml</PlayersLocation><GameStates><GameState><StateId ValueType=\"string\">Main</StateId><LayoutInfo><LayoutID ValueType=\"string\"/><LayoutType ValueType=\"object\">TextWithTextInput</LayoutType><LayoutContent ValueType=\"string\"/></LayoutInfo><TextLog ValueType=\"string\">Main Game State</TextLog><Options/><Timers/></GameState><GameState><StateId ValueType=\"string\">GS1</StateId><LayoutInfo><LayoutID ValueType=\"string\"/><LayoutType ValueType=\"object\">TextWithTextInput</LayoutType><LayoutContent ValueType=\"string\"/></LayoutInfo><TextLog ValueType=\"string\">Game State 1</TextLog><Options/><Timers/></GameState><GameState><StateId ValueType=\"string\">BossFight</StateId><LayoutInfo><LayoutID ValueType=\"string\"/><LayoutType ValueType=\"object\">TextWithTextInput</LayoutType><LayoutContent ValueType=\"string\"/></LayoutInfo><TextLog ValueType=\"string\">Boss fight</TextLog><Options/><Timers/></GameState></GameStates><LayoutLocation/><Buffer ValueType=\"int\">0</Buffer></TextAdventure>";
	private final String _gameState3UnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/tav3UnitTest.xml";
	private final String _expectedGameState3Text = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><TextAdventure inlineLayouts=\"false\" inlinegamestate=\"false\" inlineplayers=\"false\"><Name ValueType=\"string\">Super Cool Name</Name><Transition><DisplayType ValueType=\"object\">Window</DisplayType><MediaLocation ValueType=\"string\">C:/Something.avi</MediaLocation></Transition><CurrentGameState ValueType=\"string\">Main</CurrentGameState><PlayersLocation ValueType=\"string\">" + System.getProperty("user.home") + "/nilrem/UnitTests/TAL/Players.xml</PlayersLocation><GameStatesLocation ValueType=\"string\">" + System.getProperty("user.home") + "/nilrem/UnitTests/TAL/GameStates.xml</GameStatesLocation><LayoutLocation/><Buffer ValueType=\"int\">0</Buffer></TextAdventure>"; 
	
	private final String _gameState4UnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/tav4UnitTest.xml";
	private final String _gameState5UnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/tav5UnitTest.xml";
	private final String _gameState6UnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/tav6UnitTest.xml";
	private final String _gameState7UnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/tav7UnitTest.xml";
	
	@Test
	public void testCreate() {
		try {
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();
			
			assertNotNull(persist);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testSetGameStateInline() {
		try {
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();
			
			persist.gameStatesInline(true);
			assertEquals(true, persist.gameStatesInline());
			
			persist.gameStatesInline(false);
			assertEquals(false, persist.gameStatesInline());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetPlayersInline() {
		try {
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();
			
			persist.playersInline(true);
			assertEquals(true, persist.playersInline());
			
			persist.playersInline(false);
			assertEquals(false, persist.playersInline());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetCurrentGameState() {
		try {
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();
			
			persist.currentGameState("Main");
			assertEquals("Main", persist.currentGameState());
			
			persist.currentGameState("GS1");
			assertEquals("GS1", persist.currentGameState());
			
			persist.currentGameState("BossFight");
			assertEquals("BossFight", persist.currentGameState());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetTransition() {
		try {
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();
			
			TransitionPersistenceObject trans = new TransitionPersistenceObject();
			trans.displayType(DisplayType.Window);
			trans.mediaLocation("C:/Something.avi");
			
			persist.transition(trans);
			assertEquals(trans, persist.transition());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testAddGameState() {
		try {
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();
			
			GameStatePersistenceObject gameState1 = new GameStatePersistenceObject("Main");
			GameStatePersistenceObject gameState2 = new GameStatePersistenceObject("GS1");
			GameStatePersistenceObject gameState3 = new GameStatePersistenceObject("BossFight");
			
			assertEquals(0, persist.gameStates().size());
			persist.addGameState(gameState1);
			assertEquals(1, persist.gameStates().size());			
			persist.addGameState(gameState2);
			assertEquals(2, persist.gameStates().size());
			persist.addGameState(gameState3);
			assertEquals(3, persist.gameStates().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testRemoveGameState() {
		try {
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();
			
			GameStatePersistenceObject gameState1 = new GameStatePersistenceObject("Main");
			GameStatePersistenceObject gameState2 = new GameStatePersistenceObject("GS1");
			GameStatePersistenceObject gameState3 = new GameStatePersistenceObject("BossFight");
			
			persist.addGameState(gameState1);
			persist.addGameState(gameState2);
			persist.addGameState(gameState3);
			assertEquals(3, persist.gameStates().size());
			
			persist.removeGameState(gameState1);
			assertEquals(2, persist.gameStates().size());
			
			persist.removeGameState(gameState2);
			assertEquals(1, persist.gameStates().size());
			
			persist.removeGameState(gameState3);
			assertEquals(0, persist.gameStates().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testClearGameState() {
		try {
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();
			
			GameStatePersistenceObject gameState1 = new GameStatePersistenceObject("Main");
			GameStatePersistenceObject gameState2 = new GameStatePersistenceObject("GS1");
			GameStatePersistenceObject gameState3 = new GameStatePersistenceObject("BossFight");
			
			persist.addGameState(gameState1);
			persist.addGameState(gameState2);
			persist.addGameState(gameState3);
			assertEquals(3, persist.gameStates().size());
			
			persist.clearGameStates();
			assertEquals(0, persist.gameStates().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testGameStateLocation() {
		try {
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();
			
			persist.gameStatesLocation("C:/Path1/GameStates.xml");
			assertEquals("C:/Path1/GameStates.xml", persist.gameStatesLocation());
			persist.gameStatesLocation("C:/Path2/GameStates.xml");
			assertEquals("C:/Path2/GameStates.xml", persist.gameStatesLocation());
			persist.gameStatesLocation("C:/Path3/GameStates.xml");
			assertEquals("C:/Path3/GameStates.xml", persist.gameStatesLocation());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetPlayers() {
		try {
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();
			
			List<PlayerPersistenceObject> players = new ArrayList<PlayerPersistenceObject>();
			PlayerPersistenceObject player = new PlayerPersistenceObject();
			player.playerName("Player1");
			players.add(player);
			
			PlayerPersistenceObject player2 = new PlayerPersistenceObject();
			player2.playerName("Player2");
			players.add(player2);
			
			//TODO: More testing?
			persist.players(players);
			assertEquals(players, persist.players());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetPlayersLocation() {
		try {
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();
			
			persist.playersLocation("C:/Path1/Players.xml");
			assertEquals("C:/Path1/Players.xml", persist.playersLocation());
			persist.playersLocation("C:/Path2/Players.xml");
			assertEquals("C:/Path2/Players.xml", persist.playersLocation());
			persist.playersLocation("C:/Path3/Players.xml");
			assertEquals("C:/Path3/Players.xml", persist.playersLocation());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetGameName() {
		try {
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();
			
			persist.gameName("Super Cool Game");
			assertEquals("Super Cool Game", persist.gameName());
			
			persist.gameName("Game Name 2");
			assertEquals("Game Name 2", persist.gameName());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testBuffer() {
		try {
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();
			
			persist.buffer(12);
			assertEquals(12, persist.buffer());
			
			persist.buffer(2);
			assertEquals(2, persist.buffer());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSaveWithInlinePlayer() {
		try {
			_manager = new XmlConfigurationManager(_gameState1UnitTest);
		
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();
		
			TransitionPersistenceObject trans = new TransitionPersistenceObject();
			trans.mediaLocation("C:/Something.avi");
			trans.displayType(DisplayType.Window);
			
			persist.gameName("Super Cool Name");
			persist.currentGameState("Main");
			persist.playersInline(true);
			persist.gameStatesInline(false);
			persist.gameStatesLocation(System.getProperty("user.home") + "/nilrem/UnitTests/TAL/GameStates.xml");
			persist.transition(trans);

			List<PlayerPersistenceObject> players = new ArrayList<PlayerPersistenceObject>();
			PlayerPersistenceObject player = generatePlayer();
			players.add(player);
			persist.players(players);
			
			//Add game states.
			GameStatePersistenceObject gameState1 = new GameStatePersistenceObject("Main");
			gameState1.textLog("Main Game State");
			GameStatePersistenceObject gameState2 = new GameStatePersistenceObject("GS1");
			gameState2.textLog("Game State 1");
			GameStatePersistenceObject gameState3 = new GameStatePersistenceObject("BossFight");
			gameState3.textLog("Boss fight");
			
			persist.addGameState(gameState1);
			persist.addGameState(gameState2);
			persist.addGameState(gameState3);
			
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
		
			String fileContents = fileContents(_gameState1UnitTest);
			assertEquals(_expectedGameState1Text, fileContents);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSaveWithInlineGameStates() {
		try {
			_manager = new XmlConfigurationManager(_gameState2UnitTest);
			
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();

			persist.gameName("Super Cool Name");
			persist.currentGameState("Main");
			TransitionPersistenceObject trans = new TransitionPersistenceObject();
			trans.mediaLocation("C:/Something.avi");
			trans.displayType(DisplayType.Window);
			
			persist.playersInline(false);
			persist.gameStatesInline(true);
			persist.gameStatesLocation("C:/Path1/GameStates.xml");
			persist.playersLocation(System.getProperty("user.home") + "/nilrem/UnitTests/TAL/Players.xml");
			persist.transition(trans);
			
			GameStatePersistenceObject gameState1 = new GameStatePersistenceObject("Main");
			gameState1.textLog("Main Game State");
			GameStatePersistenceObject gameState2 = new GameStatePersistenceObject("GS1");
			gameState2.textLog("Game State 1");
			GameStatePersistenceObject gameState3 = new GameStatePersistenceObject("BossFight");
			gameState3.textLog("Boss fight");
			
			persist.addGameState(gameState1);
			persist.addGameState(gameState2);
			persist.addGameState(gameState3);
			
			List<PlayerPersistenceObject> players = new ArrayList<PlayerPersistenceObject>();
			PlayerPersistenceObject player = generatePlayer();
			players.add(player);
			persist.players(players);
			
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
		
			String fileContents = fileContents(_gameState2UnitTest);
			assertEquals(_expectedGameState2Text, fileContents);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSaveWithNoInlines() {
		try {
			_manager = new XmlConfigurationManager(_gameState3UnitTest);
			
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();
		
			persist.gameName("Super Cool Name");
			TransitionPersistenceObject trans = new TransitionPersistenceObject();
			trans.mediaLocation("C:/Something.avi");
			trans.displayType(DisplayType.Window);
			
			persist.currentGameState("Main");
			persist.playersInline(false);
			persist.gameStatesInline(false);
			persist.gameStatesLocation(System.getProperty("user.home") + "/nilrem/UnitTests/TAL/GameStates.xml");
			persist.playersLocation(System.getProperty("user.home") + "/nilrem/UnitTests/TAL/Players.xml");
			persist.transition(trans);
			
			GameStatePersistenceObject gameState1 = new GameStatePersistenceObject("Main");
			gameState1.textLog("Main Game State");
			GameStatePersistenceObject gameState2 = new GameStatePersistenceObject("GS1");
			gameState2.textLog("Game State 1");
			GameStatePersistenceObject gameState3 = new GameStatePersistenceObject("BossFight");
			gameState3.textLog("Boss fight");
			
			persist.addGameState(gameState1);
			persist.addGameState(gameState2);
			persist.addGameState(gameState3);
			
			List<PlayerPersistenceObject> players = new ArrayList<PlayerPersistenceObject>();
			PlayerPersistenceObject player = new PlayerPersistenceObject();

			players.add(player);
			persist.players(players);
			
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
		
			String fileContents = fileContents(_gameState3UnitTest);
			assertEquals(_expectedGameState3Text, fileContents);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertToTextAdventure() {
		try {
			_manager = new XmlConfigurationManager(_gameState6UnitTest);
			
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();
		
			TransitionPersistenceObject trans = new TransitionPersistenceObject();
			trans.mediaLocation("C:/Something.avi");
			trans.displayType(DisplayType.Window);
			
			persist.gameName("Super Cool Name");
			persist.currentGameState("Main");
			persist.playersInline(false);
			persist.gameStatesInline(false);
			persist.gameStatesLocation(System.getProperty("user.home") + "/nilrem/UnitTests/TAL/GameStates3.xml");
			persist.playersLocation(System.getProperty("user.home") + "/nilrem/UnitTests/TAL/Players3.xml");
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
			
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			XmlConfigurationManager manager1 = new XmlConfigurationManager(_gameState6UnitTest);
			
			manager1.load();
			
			TextAdventurePersistenceObject persistedGame = new TextAdventurePersistenceObject();
			
			persistedGame.convertFromPersistence(((XmlConfigurationObject)manager1.configurationObjects().get(0)));
			ITextAdventureGameStateManager game = persistedGame.convertToGameStateManager();
			
			assertNotNull(game);
			assertEquals(1, game.players().size());
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertToTextAdventureWithPlayerInline() {
		try {
			_manager = new XmlConfigurationManager(_gameState4UnitTest);
			
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();
		
			TransitionPersistenceObject trans = new TransitionPersistenceObject();
			trans.mediaLocation("C:/Something.avi");
			trans.displayType(DisplayType.Window);
			
			persist.gameName("Super Cool Name");
			persist.currentGameState("Main");
			persist.playersInline(true);
			persist.gameStatesInline(false);
			persist.gameStatesLocation(System.getProperty("user.home") + "/nilrem/UnitTests/TAL/GameStates1.xml");
			persist.transition(trans);

			List<PlayerPersistenceObject> players = new ArrayList<PlayerPersistenceObject>();
			PlayerPersistenceObject player = generatePlayer();
			players.add(player);
			persist.players(players);
			
			LayoutInfoPersistenceObject layout1 = new LayoutInfoPersistenceObject();
			layout1.setLayoutType(LayoutType.TextWithTextInput);
			LayoutInfoPersistenceObject layout2 = new LayoutInfoPersistenceObject();
			layout2.setLayoutType(LayoutType.TextWithTextInput);
			LayoutInfoPersistenceObject layout3 = new LayoutInfoPersistenceObject();
			layout3.setLayoutType(LayoutType.TextWithTextInput);
			
			//Add game states.
			GameStatePersistenceObject gameState1 = new GameStatePersistenceObject("Main");
			gameState1.textLog("Main Game State");
			gameState1.layout(layout1);
			GameStatePersistenceObject gameState2 = new GameStatePersistenceObject("GS1");
			gameState2.textLog("Game State 1");
			gameState2.layout(layout2);
			GameStatePersistenceObject gameState3 = new GameStatePersistenceObject("BossFight");
			gameState3.textLog("Boss fight");
			gameState3.layout(layout3);
			
			persist.addGameState(gameState1);
			persist.addGameState(gameState2);
			persist.addGameState(gameState3);
			
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			XmlConfigurationManager manager1 = new XmlConfigurationManager(_gameState4UnitTest);
			
			manager1.load();
			
			TextAdventurePersistenceObject persistedGame = new TextAdventurePersistenceObject();
			
			persistedGame.convertFromPersistence(((XmlConfigurationObject)manager1.configurationObjects().get(0)));
			ITextAdventureGameStateManager game = persistedGame.convertToGameStateManager();
			
			assertNotNull(game);
			assertEquals(1, game.players().size());
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertToTextAdventureWithGameStateInline() {
		try {
			_manager = new XmlConfigurationManager(_gameState5UnitTest);
			
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();

			persist.currentGameState("Main");
			TransitionPersistenceObject trans = new TransitionPersistenceObject();
			trans.mediaLocation("C:/Something.avi");
			trans.displayType(DisplayType.Window);
			
			persist.gameName("Super Cool Name");
			persist.playersInline(false);
			persist.gameStatesInline(true);
			persist.gameStatesLocation("C:/Path1/GameStates2.xml");
			persist.playersLocation(System.getProperty("user.home") + "/nilrem/UnitTests/TAL/Players.xml");
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
			PlayerPersistenceObject player = generatePlayer();
			players.add(player);
			persist.players(players);
			
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			XmlConfigurationManager manager1 = new XmlConfigurationManager(_gameState5UnitTest);
			
			manager1.load();
			
			TextAdventurePersistenceObject persistedGame = new TextAdventurePersistenceObject();
			
			persistedGame.convertFromPersistence(((XmlConfigurationObject)manager1.configurationObjects().get(0)));
			ITextAdventureGameStateManager game = persistedGame.convertToGameStateManager();
			
			assertNotNull(game);
			assertEquals(1, game.players().size());
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertFromPersistenceNoInlines() {
		try {
			_manager = new XmlConfigurationManager(_gameState3UnitTest);
			
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();
		
			persist.gameName("Super Cool Name");
			TransitionPersistenceObject trans = new TransitionPersistenceObject();
			trans.mediaLocation("C:/Something.avi");
			trans.displayType(DisplayType.Window);
			
			persist.currentGameState("Main");
			persist.playersInline(false);
			persist.gameStatesInline(false);
			persist.gameStatesLocation(System.getProperty("user.home") + "/nilrem/UnitTests/TAL/GameStates.xml");
			persist.playersLocation(System.getProperty("user.home") + "/nilrem/UnitTests/TAL/Players.xml");
			persist.transition(trans);
			
			GameStatePersistenceObject gameState1 = new GameStatePersistenceObject("Main");
			gameState1.textLog("Main Game State");
			GameStatePersistenceObject gameState2 = new GameStatePersistenceObject("GS1");
			gameState2.textLog("Game State 1");
			GameStatePersistenceObject gameState3 = new GameStatePersistenceObject("BossFight");
			gameState3.textLog("Boss fight");
			
			persist.addGameState(gameState1);
			persist.addGameState(gameState2);
			persist.addGameState(gameState3);
			
			List<PlayerPersistenceObject> players = new ArrayList<PlayerPersistenceObject>();
			PlayerPersistenceObject player = new PlayerPersistenceObject();

			players.add(player);
			persist.players(players);
			
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			XmlConfigurationManager manager1 = new XmlConfigurationManager(_gameState3UnitTest);
			manager1.load();
			
			TextAdventurePersistenceObject persist2 = new TextAdventurePersistenceObject();
			persist2.convertFromPersistence((XmlConfigurationObject)manager1.configurationObjects().get(0));
			
			assertEquals("Super Cool Name", persist2.gameName());
			assertEquals("C:/Something.avi", persist2.transition().mediaLocation());
			assertEquals(DisplayType.Window, persist2.transition().displayType());
			assertEquals(3, persist.gameStates().size());
			assertEquals(1, persist.players().size());
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertFromPersistenceNoInlinesAndBuffer() {
		try {
			_manager = new XmlConfigurationManager(_gameState7UnitTest);
			
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();
		
			persist.gameName("Super Cool Name");
			TransitionPersistenceObject trans = new TransitionPersistenceObject();
			trans.mediaLocation("C:/Something.avi");
			trans.displayType(DisplayType.Window);
			
			persist.currentGameState("Main");
			persist.playersInline(false);
			persist.gameStatesInline(false);
			persist.gameStatesLocation(System.getProperty("user.home") + "/nilrem/UnitTests/TAL/GameStates.xml");
			persist.playersLocation(System.getProperty("user.home") + "/nilrem/UnitTests/TAL/Players.xml");
			persist.transition(trans);
			
			GameStatePersistenceObject gameState1 = new GameStatePersistenceObject("Main");
			gameState1.textLog("Main Game State");
			LayoutInfoPersistenceObject gs1Layout = new LayoutInfoPersistenceObject();
			gs1Layout.setLayoutType(LayoutType.TextWithTextInput);
			gameState1.layout(gs1Layout);
			GameStatePersistenceObject gameState2 = new GameStatePersistenceObject("GS1");
			gameState2.textLog("Game State 1");
			LayoutInfoPersistenceObject gs2Layout = new LayoutInfoPersistenceObject();
			gs2Layout.setLayoutType(LayoutType.TextWithTextInput);
			gameState2.layout(gs2Layout);
			GameStatePersistenceObject gameState3 = new GameStatePersistenceObject("BossFight");
			LayoutInfoPersistenceObject gs3Layout = new LayoutInfoPersistenceObject();
			gs3Layout.setLayoutType(LayoutType.TextWithTextInput);
			gameState3.layout(gs3Layout);
			gameState3.textLog("Boss fight");
			
			persist.addGameState(gameState1);
			persist.addGameState(gameState2);
			persist.addGameState(gameState3);
			
			List<PlayerPersistenceObject> players = new ArrayList<PlayerPersistenceObject>();
			PlayerPersistenceObject player = new PlayerPersistenceObject();

			players.add(player);
			persist.players(players);
			persist.buffer(2);
			
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			XmlConfigurationManager manager1 = new XmlConfigurationManager(_gameState7UnitTest);
			manager1.load();
			
			TextAdventurePersistenceObject persist2 = new TextAdventurePersistenceObject();
			persist2.convertFromPersistence((XmlConfigurationObject)manager1.configurationObjects().get(0));
			
			assertEquals("Super Cool Name", persist2.gameName());
			assertEquals("C:/Something.avi", persist2.transition().mediaLocation());
			assertEquals(DisplayType.Window, persist2.transition().displayType());
			assertEquals(3, persist.gameStates().size());
			assertEquals(1, persist.players().size());
			assertEquals(2, persist2.buffer());
			
			ITextAdventureGameStateManager gameManager = persist2.convertToGameStateManager();
			assertTrue(gameManager instanceof BufferedTextAdventureGameStateManager);
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertFromPersistencePlayerInline() {
		try {
			_manager = new XmlConfigurationManager(_gameState4UnitTest);
			
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();
		
			TransitionPersistenceObject trans = new TransitionPersistenceObject();
			trans.mediaLocation("C:/Something.avi");
			trans.displayType(DisplayType.Window);
			
			persist.gameName("Super Cool Name");
			persist.currentGameState("Main");
			persist.playersInline(true);
			persist.gameStatesInline(false);
			persist.gameStatesLocation(System.getProperty("user.home") + "/nilrem/UnitTests/TAL/GameStates1.xml");
			persist.transition(trans);

			List<PlayerPersistenceObject> players = new ArrayList<PlayerPersistenceObject>();
			PlayerPersistenceObject player = generatePlayer();
			players.add(player);
			persist.players(players);
			
			LayoutInfoPersistenceObject layout1 = new LayoutInfoPersistenceObject();
			layout1.setLayoutType(LayoutType.TextWithTextInput);
			LayoutInfoPersistenceObject layout2 = new LayoutInfoPersistenceObject();
			layout2.setLayoutType(LayoutType.TextWithTextInput);
			LayoutInfoPersistenceObject layout3 = new LayoutInfoPersistenceObject();
			layout3.setLayoutType(LayoutType.TextWithTextInput);
			
			//Add game states.
			GameStatePersistenceObject gameState1 = new GameStatePersistenceObject("Main");
			gameState1.textLog("Main Game State");
			gameState1.layout(layout1);
			GameStatePersistenceObject gameState2 = new GameStatePersistenceObject("GS1");
			gameState2.textLog("Game State 1");
			gameState2.layout(layout2);
			GameStatePersistenceObject gameState3 = new GameStatePersistenceObject("BossFight");
			gameState3.textLog("Boss fight");
			gameState3.layout(layout3);
			
			persist.addGameState(gameState1);
			persist.addGameState(gameState2);
			persist.addGameState(gameState3);
			
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			XmlConfigurationManager manager1 = new XmlConfigurationManager(_gameState4UnitTest);
			manager1.load();
			
			TextAdventurePersistenceObject persist2 = new TextAdventurePersistenceObject();
			persist2.convertFromPersistence((XmlConfigurationObject)manager1.configurationObjects().get(0));
			
			assertEquals("Super Cool Name", persist2.gameName());
			assertEquals("C:/Something.avi", persist2.transition().mediaLocation());
			assertEquals(DisplayType.Window, persist2.transition().displayType());
			assertEquals(3, persist.gameStates().size());
			assertEquals(1, persist.players().size());
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertFromPersistenceGameInline() {
		try {
			_manager = new XmlConfigurationManager(_gameState5UnitTest);
			
			TextAdventurePersistenceObject persist = new TextAdventurePersistenceObject();

			persist.currentGameState("Main");
			TransitionPersistenceObject trans = new TransitionPersistenceObject();
			trans.mediaLocation("C:/Something.avi");
			trans.displayType(DisplayType.Window);
			
			persist.gameName("Super Cool Name");
			persist.playersInline(false);
			persist.gameStatesInline(true);
			persist.gameStatesLocation("C:/Path1/GameStates2.xml");
			persist.playersLocation(System.getProperty("user.home") + "/nilrem/UnitTests/TAL/Players.xml");
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
			PlayerPersistenceObject player = generatePlayer();
			players.add(player);
			persist.players(players);
			
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			XmlConfigurationManager manager1 = new XmlConfigurationManager(_gameState5UnitTest);
			manager1.load();
			
			TextAdventurePersistenceObject persist2 = new TextAdventurePersistenceObject();
			persist2.convertFromPersistence((XmlConfigurationObject)manager1.configurationObjects().get(0));
			
			assertEquals("Super Cool Name", persist2.gameName());
			assertEquals("C:/Something.avi", persist2.transition().mediaLocation());
			assertEquals(DisplayType.Window, persist2.transition().displayType());
			assertEquals(3, persist.gameStates().size());
			assertEquals(1, persist.players().size());
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	private PlayerPersistenceObject generatePlayer() throws TransformerConfigurationException, ParserConfigurationException {
		PlayerPersistenceObject player = new PlayerPersistenceObject();
		
		//Name
		player.playerName("Deemo");
	
		//Attributes
		AttributePersistenceObject att1 = new AttributePersistenceObject();
		att1.objectName("Age");
		att1.objectValue(15);
		AttributePersistenceObject att2 = new AttributePersistenceObject();
		att2.objectName("Class");
		att2.objectValue("Wizard");
		AttributePersistenceObject att3 = new AttributePersistenceObject();
		att3.objectName("Max Health");
		att3.objectValue(5000);
		player.addAttribute(att1);
		player.addAttribute(att2);
		player.addAttribute(att3);
	
		//Characteristics
		CharacteristicPersistenceObject char1 = new CharacteristicPersistenceObject();
		char1.objectName("Hair Color");
		char1.objectValue("Brown");
		CharacteristicPersistenceObject char2 = new CharacteristicPersistenceObject();
		char2.objectName("Scar");
		char2.objectValue("Long");
		CharacteristicPersistenceObject char3 = new CharacteristicPersistenceObject();
		char3.objectName("Skin Color");
		char3.objectValue("White");
		player.addCharacteristic(char1);
		player.addCharacteristic(char2);
		player.addCharacteristic(char3);
	
	
		//Body Parts
		BodyPartPersistenceObject body1 = new BodyPartPersistenceObject();
		body1.objectName("Head");
		BodyPartPersistenceObject body2 = new BodyPartPersistenceObject();
		body2.objectName("Feet");
		BodyPartPersistenceObject body3 = new BodyPartPersistenceObject();
		body3.objectName("Tourso");
	
		player.addBodyPart(body1);
		player.addBodyPart(body2);
		player.addBodyPart(body3);
	
		//Inventory
		InventoryPersistenceObject inv = new InventoryPersistenceObject();
		ItemPersistenceObject item1 = new ItemPersistenceObject();
		item1.itemName("Potion");
		item1.itemDescription("Restores HP");
		ItemPersistenceObject item2 = new ItemPersistenceObject();
		item2.itemName("Ether");
		item2.itemDescription("Restores MP");
		ItemPersistenceObject item3 = new ItemPersistenceObject();
		item3.itemName("Phenoix Down");
		item3.itemDescription("Revives");
		inv.addItem(item1, 5);
		inv.addItem(item2, 2);
		inv.addItem(item3, 10);
		player.inventory(inv);
	
		//Equipment
		EquipmentPersistenceObject equip = new EquipmentPersistenceObject();
		ItemPersistenceObject item4 = new ItemPersistenceObject();
		item4.itemName("Helmet");
		item4.itemDescription("Protects the head");
		ItemPersistenceObject item5 = new ItemPersistenceObject();
		item5.itemName("Steel Boots");
		item5.itemDescription("Protects the feet");
		ItemPersistenceObject item6 = new ItemPersistenceObject();
		item6.itemName("Leather Vest");
		item6.itemDescription("Protects the body");
		BodyPartPersistenceObject ebody1 = new BodyPartPersistenceObject();
		ebody1.objectName("Head");
		BodyPartPersistenceObject ebody2 = new BodyPartPersistenceObject();
		ebody2.objectName("Feet");
		BodyPartPersistenceObject ebody3 = new BodyPartPersistenceObject();
		ebody3.objectName("Tourso");
	
		equip.equip(item4, ebody1);
		equip.equip(item5, ebody2);
		equip.equip(item6, ebody3);
		player.equipment(equip);
		return player;
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
