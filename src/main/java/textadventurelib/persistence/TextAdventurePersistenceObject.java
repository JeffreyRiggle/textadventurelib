package textadventurelib.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.xml.sax.SAXException;

import ilusr.gamestatemanager.GameState;
import ilusr.logrunner.LogRunner;
import ilusr.persistencelib.configuration.ConfigurationProperty;
import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import playerlib.player.IPlayer;
import textadventurelib.core.BufferedTextAdventureGameStateManager;
import textadventurelib.core.ITextAdventureGameStateManager;
import textadventurelib.core.TextAdventureGameStateManager;
import textadventurelib.gamestates.GameStateProvider;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TextAdventurePersistenceObject extends XmlConfigurationObject{

	private static final String TEXT_ADVENTURE_NAME = "TextAdventure";
	private static final String GAMESTATE_INLINE_NAME = "inlinegamestate";
	private static final String PLAYERS_INLINE_NAME = "inlineplayers";
	private static final String LAYOUTS_INLINE_NAME = "inlineLayouts";
	private static final String CURRENT_GAMESTATE_NAME = "CurrentGameState";
	private static final String GAMESTATES_NAME = "GameStates";
	private static final String GAMESTATES_LOCATION_NAME = "GameStatesLocation";
	private static final String PLAYERS_LOCATION_NAME = "PlayersLocation";
	private static final String PLAYERS_NAME = "Players";
	private static final String LAYOUT_LOCATION_NAME = "LayoutLocation";
	private static final String LAYOUTS_NAME = "Layouts";
	private static final String GAME_NAME = "Name";
	private static final String BUFFER = "Buffer";
	
	private final LayoutRepository layoutRepository;
	
	private ConfigurationProperty gameStateInline;
	private ConfigurationProperty playersInline;
	private ConfigurationProperty layoutsInline;
	private XmlConfigurationObject currentGameState;
	private List<GameStatePersistenceObject> gameStates;
	private XmlConfigurationObject gameStatesLocation;
	private XmlConfigurationObject playersLocation;
	private TransitionPersistenceObject transition;
	private List<PlayerPersistenceObject> players;
	private XmlConfigurationObject layoutsLocation;
	private XmlConfigurationObject gameName;
	private XmlConfigurationObject buffer;
	
	/**
	 * Creates a new Persistence object for a Text Adventure game.
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public TextAdventurePersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(TEXT_ADVENTURE_NAME);
		
		layoutRepository = new LayoutRepository();
		gameStateInline = new ConfigurationProperty(GAMESTATE_INLINE_NAME, "false");
		playersInline = new ConfigurationProperty(PLAYERS_INLINE_NAME, "false");
		layoutsInline = new ConfigurationProperty(LAYOUTS_INLINE_NAME, "false");
		currentGameState = new XmlConfigurationObject(CURRENT_GAMESTATE_NAME);
		gameStates = new ArrayList<GameStatePersistenceObject>();
		gameStatesLocation = new XmlConfigurationObject(GAMESTATES_LOCATION_NAME);
		playersLocation = new XmlConfigurationObject(PLAYERS_LOCATION_NAME);
		transition = new TransitionPersistenceObject();
		players = new ArrayList<PlayerPersistenceObject>();
		layoutsLocation = new XmlConfigurationObject(LAYOUT_LOCATION_NAME);
		gameName = new XmlConfigurationObject(GAME_NAME);
		buffer = new XmlConfigurationObject(BUFFER, 0);
	}
	
	/**
	 * Controls whether game states will be inlined into the text adventure file or linked.
	 * In order to link the game state this value must be false and {@link #gameStatesLocation(String)} must be set.
	 * 
	 * @param inline If the Game States should be inlined into the text adventure.
	 */
	public void gameStatesInline(boolean inline) {
		gameStateInline.value(Boolean.toString(inline));
	}
	
	/**
	 * 
	 * @return If the game states should be inlined into the text adventure file or not.
	 */
	public boolean gameStatesInline() {
		return Boolean.parseBoolean(gameStateInline.value());
	}
	
	/**
	 * Controls whether players will be inlined into the text adventure file or linked.
	 * In order to link the players this value must be false and {@link #playersLocation(String)} must be set.
	 * 
	 * @param inline If the players should be inlined into the text adventure file.
	 */
	public void playersInline(boolean inline) {
		playersInline.value(Boolean.toString(inline));
	}
	
	/**
	 * 
	 * @return If the players should be inlined into the text adventure file.
	 */
	public boolean playersInline() {
		return Boolean.parseBoolean(playersInline.value());
	}
	
	/**
	 * Controls whether layouts will be inlined into the text adventure file or linked.
	 * In order to link the layouts this value must be false and {@link #layoutsLocation(String)} must be set.
	 * 
	 * @param inline If the layouts should be inlined into the text adventure file.
	 */
	public void layoutsInline(boolean inline) {
		layoutsInline.value(Boolean.toString(inline));
	}
	
	/**
	 * 
	 * @return If the layouts should be inlined into the text adventure file.
	 */
	public boolean layoutsInline() {
		return Boolean.parseBoolean(layoutsInline.value());
	}
	
	/**
	 * The current game state of the text adventure. This will control how the text adventure is created and
	 * what state the game will start on.
	 * 
	 * @param state The state to start the game on.
	 */
	public void currentGameState(String state) {
		currentGameState.value(state);
	}
	
	/**
	 * 
	 * @return The state the game will be started on.
	 */
	public String currentGameState() {
		return currentGameState.value();
	}
	
	/**
	 * This will set the transition object. The transition object will tell the hosting application 
	 * what to display while the game is being created and loaded.
	 * 
	 * @param transition The transition object to display while the game is loading.
	 */
	public void transition(TransitionPersistenceObject transition) {
		this.transition = transition;
	}
	
	/**
	 * 
	 * @return The transition object to display while the game is loading.
	 */
	public TransitionPersistenceObject transition() {
		return transition;
	}
	
	/**
	 * Adds a game state to the collection of game states for this game.
	 * 
	 * @param gameState The @see GameStatePersistenceObject to add to this game.
	 */
	public void addGameState(GameStatePersistenceObject gameState) {
		gameState.setLayoutRepository(layoutRepository);
		gameStates.add(gameState);
	}
	
	/**
	 * Removes a game state from collection of game states for this game.
	 * 
	 * @param gameState The @see GameStatePersistenceObject to remove from this game.
	 */
	public void removeGameState(GameStatePersistenceObject gameState) {
		gameStates.remove(gameState);
	}

	/**
	 * Clears all @see GameStatePersistenceObjects from this game.
	 */
	public void clearGameStates() {
		gameStates.clear();
	}
	
	/**
	 * 
	 * @return A list of @see GameStatePersistenceObject associated with this game.
	 */
	public List<GameStatePersistenceObject> gameStates() {
		return gameStates;
	}
	
	/**
	 * The location to save off game states. This will only matter if {@link #gameStatesInline()} is set to false.
	 * 
	 * Example path:
	 * 	C:/GameStates.xml
	 * 
	 * @param path The path to save the game states to.
	 */
	public void gameStatesLocation(String path) {
		gameStatesLocation.value(path);
	}
	
	/**
	 * 
	 * @return The path to save the game states to.
	 */
	public String gameStatesLocation() {
		return gameStatesLocation.value();
	}
	
	/**
	 * Assigns a collection of @see PlayerPersistenceObject players to be used in this game.
	 * 
	 * @param players A list of @see PlayerPersistenceObject to be used for this game.
	 */
	public void players(List<PlayerPersistenceObject> players) {
		this.players = players;
	}
	
	/**
	 * 
	 * @param player The @see PlayerPersistenceObject to add to this game.
	 */
	public void addPlayer(PlayerPersistenceObject player) {
		players.add(player);
	}
	
	/**
	 * 
	 * @param player The @see PlayerPersistenceObject to remove from this game.
	 */
	public void removePlayer(PlayerPersistenceObject player) {
		players.remove(player);
	}
	
	/**
	 * 
	 * @return A list of @see PlayerPersistenceObject associated with this game.
	 */
	public List<PlayerPersistenceObject> players() {
		return players;
	}
	
	/**
	 * The location to save off players. This will only matter if {@link #playersInline()} returns false.
	 * 
	 * Example path:
	 *  C:/Player.xml
	 *  
	 * @param path The location to save the players off to.
	 */
	public void playersLocation(String path) {
		playersLocation.value(path);
	}
	
	/**
	 * 
	 * @return The location the players will be saved off to.
	 */
	public String playersLocation() {
		return playersLocation.value();
	}
	
	/**
	 * Assigns a collection of @see LayoutPersistenceObject layouts to be used in this game.
	 * 
	 * @param layouts A list of @see LayoutPersistenceObject to be used for this game.
	 */
	public void setLayouts(List<LayoutPersistenceObject> layouts) {
		layoutRepository.clearLayouts();
		
		for (LayoutPersistenceObject layout : layouts) {
			layoutRepository.addLayout(layout);
		}
	}
	
	/**
	 * 
	 * @return A list of @see LayoutPersistenceObject associated with this game.
	 */
	public List<LayoutPersistenceObject> getLayouts() {
		return layoutRepository.getLayouts();
	}
	
	/**
	 * Adds a @see LayoutPersistenceObject layout to be used in this game.
	 * 
	 * @param layout A @see LayoutPersistenceObject layout to add.
	 */
	public void addLayout(LayoutPersistenceObject layout) {
		layoutRepository.addLayout(layout);
	}
	
	/**
	 * Removes a @see LayoutPersistenceObject layout from this game.
	 * 
	 * @param layout A @see LayoutPersistenceObject layout to remove.
	 */
	public void removeLayout(LayoutPersistenceObject layout) {
		layoutRepository.removeLayout(layout);
	}
	
	/**
	 * The location to save off layouts. This will only matter if {@link #layoutsInline()} returns false.
	 * 
	 * Example path:
	 *  C:/Layout.xml
	 *  
	 * @param path The location to save the layouts off to.
	 */
	public void layoutsLocation(String path) {
		layoutsLocation.value(path);
	}
	
	/**
	 * 
	 * @return The location the layouts will be saved off to.
	 */
	public String layoutsLocation() {
		return layoutsLocation.value();
	}
	
	/**
	 * 
	 * @param name The new name for this game.
	 */
	public void gameName(String name) {
		gameName.value(name);
	}
	
	/**
	 * 
	 * @return The name to associate with this game.
	 */
	public String gameName() {
		return gameName.value();
	}
	
	/**
	 * Sets the buffer size of a text adventure game. If buffer size is 0 or less 
	 * the game will not be buffered.
	 * 
	 * @param size The size of a buffer to have
	 */
	public void buffer(int size) {
		buffer.value(size);
	}
	
	/**
	 * Gets the buffer size of a text adventure game. If buffer size is 0 or less 
	 * the game will not be buffered.
	 * 
	 * @return The size of a buffer to have.
	 */
	public int buffer() {
		return buffer.value();
	}
	
	//Refactor?
	/**
	 * Prepares the xml file to be saved. This must be called before saving the xml file.
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public void prepareXml() throws TransformerConfigurationException, ParserConfigurationException {
		super.clearChildren();
		super.clearConfigurationProperties();
		
		super.addConfigurationProperty(playersInline);
		super.addConfigurationProperty(gameStateInline);
		super.addConfigurationProperty(layoutsInline);
		
		super.addChild(gameName);
		transition.prepareXml();
		super.addChild(transition);
		super.addChild(currentGameState);
		
		if (playersInline()) {
			XmlConfigurationObject players = new XmlConfigurationObject(PLAYERS_NAME);
			
			for (PlayerPersistenceObject playerObj : this.players) {
				playerObj.prepareXml();
				players.addChild(playerObj);
			}
			
			super.addChild(players);
		}
		else {
			super.addChild(playersLocation);
			//TODO: Does this really need to be done?
			
			if (players.size() > 0) {
				try {
					XmlConfigurationManager playerManager = new XmlConfigurationManager(playersLocation());
					XmlConfigurationObject playerPersistence = new XmlConfigurationObject(PLAYERS_NAME);
					for (PlayerPersistenceObject playerObj : players()) {
						playerObj.prepareXml();
						playerPersistence.addChild(playerObj);
					}
					playerManager.addConfigurationObject(playerPersistence);
					playerManager.save();
				} catch (Exception e) {
					//TODO: What to do here?
				}
			}
		}
		
		if (gameStatesInline()) {
			XmlConfigurationObject gameStates = new XmlConfigurationObject(GAMESTATES_NAME);
			
			for (GameStatePersistenceObject gs : this.gameStates) {
				gs.prepareXml();
				gameStates.addChild(gs);
			}
			
			super.addChild(gameStates);
		}
		else {
			super.addChild(gameStatesLocation);
			//TODO: Does this really need to be done?
			
			if (gameStates.size() > 0) {
				LogRunner.logger().info(String.format("Not inlining games. creating a new manager to save to %s", gameStatesLocation()));
				try {
					XmlConfigurationManager gameStateManager = new XmlConfigurationManager(gameStatesLocation());
					XmlConfigurationObject gameStatePersistence = new XmlConfigurationObject(GAMESTATES_NAME);
					for (GameStatePersistenceObject gameObj : gameStates()) {
						gameObj.prepareXml();
						gameStatePersistence.addChild(gameObj);
					}
					gameStateManager.addConfigurationObject(gameStatePersistence);
					gameStateManager.save();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if (layoutsInline()) {
			XmlConfigurationObject layouts = new XmlConfigurationObject(LAYOUTS_NAME);
			
			for (LayoutPersistenceObject layout : getLayouts()) {
				layout.prepareXml();
				layouts.addChild(layout);
			}
			
			super.addChild(layouts);
		}
		else {
			super.addChild(layoutsLocation);
			//TODO: Does this really need to be done?
			
			if (getLayouts().size() > 0) {
				LogRunner.logger().info(String.format("Not inlining layouts. creating a new manager to save to %s", layoutsLocation()));
				try {
					XmlConfigurationManager layoutManager = new XmlConfigurationManager(layoutsLocation());
					XmlConfigurationObject layoutPersistence = new XmlConfigurationObject(LAYOUTS_NAME);
					for (LayoutPersistenceObject layout : getLayouts()) {
						layout.prepareXml();
						layoutPersistence.addChild(layout);
					}
					layoutManager.addConfigurationObject(layoutPersistence);
					layoutManager.save();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		super.addChild(buffer);
	}
	
	/**
	 * Converts This persistence object into a @see TextAdventureGameStateManager.
	 * 
	 * @return The converted @see TextAdventureGameStateManager that this persistence object represents.
	 */
	public ITextAdventureGameStateManager convertToGameStateManager() {
		ITextAdventureGameStateManager manager = null;
		List<IPlayer> players = new ArrayList<IPlayer>();
		Map<String, GameState> gameStates = null;
		
		if (!playersInline()) {
			try {
				players.add(generatePlayerFromFile());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			for (PlayerPersistenceObject playerObj : this.players) {
				players.add(playerObj.convertToPlayer());
			}
		}
		
		if (!gameStatesInline()) {
			try {
				gameStates = generateGameStatesFromFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			gameStates = new LinkedHashMap<String, GameState>();
			
			for (GameStatePersistenceObject gs : gameStates()) {
				gameStates.put(gs.stateId(), gs.convertToGameState());
			}
		}
		
		if (buffer() <= 0) {
			manager = createNonBufferedManager(gameStates);
		} else {
			manager = createBufferedManager(gameStates, buffer());
		}
		
		manager.players(players);
		return manager;
	}
	
	private ITextAdventureGameStateManager createNonBufferedManager(Map<String, GameState> gameStates) {
		TextAdventureGameStateManager retVal = null;
		boolean first = true;
		
		for (Map.Entry<String, GameState> entry : gameStates.entrySet()) {
			if (first) {
				retVal = new TextAdventureGameStateManager(entry.getKey(), entry.getValue());
				first = false;
				continue;
			}
			
			retVal.addGameState(entry.getKey(), entry.getValue());
			
			if (entry.getKey().equalsIgnoreCase(currentGameState())) {
				retVal.currentGameState(entry.getValue());
			}
		}
		
		return retVal;
	}
	
	private ITextAdventureGameStateManager createBufferedManager(Map<String, GameState> gameStates, int bufferSize) {
		BufferedTextAdventureGameStateManager retVal = null;
		
		for (Map.Entry<String, GameState> entry : gameStates.entrySet()) {
			if (entry.getKey().equalsIgnoreCase(currentGameState())) {
				GameStateProvider provider = new GameStateProvider(this);
				retVal = new BufferedTextAdventureGameStateManager(provider, bufferSize, entry.getKey(), entry.getValue());
			}
		}
		
		return retVal;
	}
	
	/**
	 * Updates this persistence object based off of a @see XmlConfigurationObject.
	 * 
	 * @param obj The @see XmlConfigurationObject to load data from.
	 */
	public void convertFromPersistence(XmlConfigurationObject obj) {
		gameStatesInline(false);
		playersInline(false);
		layoutsInline(false);
		convertInlineProperties(obj);
		
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			switch (cChild.name()) {
				case PLAYERS_LOCATION_NAME:
					playersLocation(cChild.<String>value());
					break;
				case CURRENT_GAMESTATE_NAME:
					currentGameState(cChild.<String>value());
					break;
				case GAMESTATES_LOCATION_NAME:
					gameStatesLocation(cChild.<String>value());
					break;
				case GAMESTATES_NAME:
					convertAndAddGameStates(cChild);
					break;
				case "Players":
					convertAndAddPlayers(cChild);
					break;
				//TODO: I don't like this.
				case "Transition":
					convertAndAddTransition(cChild);
					break;
				case GAME_NAME:
					gameName(cChild.<String>value());
					break;
				case BUFFER:
					buffer(cChild.<Integer>value());
					break;
				case LAYOUT_LOCATION_NAME:
					layoutsLocation(cChild.<String>value());
					break;
				case LAYOUTS_NAME:
					convertAndAddLayouts(cChild);
					break;
				default:
					break;
			}
		}
	}
	
	private void convertInlineProperties(XmlConfigurationObject obj) {
		for (ConfigurationProperty prop : obj.configurationProperties()) {
			if (prop.name().equalsIgnoreCase(PLAYERS_INLINE_NAME)) {
				playersInline(Boolean.parseBoolean(prop.value()));
			}
			
			if (prop.name().equalsIgnoreCase(GAMESTATE_INLINE_NAME)) {
				gameStatesInline(Boolean.parseBoolean(prop.value()));
			}
			
			if (prop.name().equalsIgnoreCase(LAYOUTS_INLINE_NAME)) {
				layoutsInline(Boolean.parseBoolean(prop.value()));
			}
		}
	}
	
	private void convertAndAddGameStates(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			try {
				XmlConfigurationObject cChild = (XmlConfigurationObject)child;
				addGameState(convertGameState(cChild));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void convertAndAddPlayers(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = ((XmlConfigurationObject)child);
			
			if (!cChild.name().equalsIgnoreCase("Player")) continue;
			
			convertAndAddPlayer(cChild);
		}
	}
	
	private void convertAndAddPlayer(XmlConfigurationObject obj) {
		try {
			PlayerPersistenceObject player = new PlayerPersistenceObject();
			player.convertFromPersistence(obj);
			players.add(player);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void convertAndAddLayouts(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = ((XmlConfigurationObject)child);
			
			if (!cChild.name().equalsIgnoreCase("Layout")) continue;
			
			convertAndAddLayout(cChild);
		}
	}
	
	private void convertAndAddLayout(XmlConfigurationObject obj) {
		try {
			LayoutPersistenceObject layout = new LayoutPersistenceObject();
			layout.convertFromPersistence(obj);
			layoutRepository.addLayout(layout);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void convertAndAddTransition(XmlConfigurationObject obj) {
		try {
			TransitionPersistenceObject transition = new TransitionPersistenceObject();
			transition.convertFromPersistence(obj);
			transition(transition);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private IPlayer generatePlayerFromFile() throws TransformerConfigurationException, ParserConfigurationException, SAXException, IOException {
		IPlayer player = null;
		XmlConfigurationManager manager = new XmlConfigurationManager(playersLocation());
		
		manager.load();
		
		PlayerPersistenceObject playerPersistence = new PlayerPersistenceObject();
		playerPersistence.convertFromPersistence((XmlConfigurationObject)manager.configurationObjects().get(0));
		
		player = playerPersistence.convertToPlayer();
		return player;
	}
	
	private Map<String, GameState> generateGameStatesFromFile() throws TransformerConfigurationException, ParserConfigurationException, SAXException, IOException {
		Map<String, GameState> gameStates = new HashMap<String, GameState>();
		XmlConfigurationManager manager = new XmlConfigurationManager(gameStatesLocation());
		
		manager.load();
		List<GameStatePersistenceObject> gameStatePersistence = convertGameStates((XmlConfigurationObject)manager.configurationObjects().get(0));
		
		for(GameStatePersistenceObject gs : gameStatePersistence) {
			gameStates.put(gs.stateId(), gs.convertToGameState());
		}
		
		return gameStates;
	}
	
	private List<GameStatePersistenceObject> convertGameStates(XmlConfigurationObject obj) {
		List<GameStatePersistenceObject> retVal = new ArrayList<GameStatePersistenceObject>();
		
		for(PersistXml child : obj.children()) {
			try {
				XmlConfigurationObject cChild = (XmlConfigurationObject)child;
				retVal.add(convertGameState(cChild));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return retVal;
	}
	
	private GameStatePersistenceObject convertGameState(XmlConfigurationObject obj) throws TransformerConfigurationException, ParserConfigurationException {
		GameStatePersistenceObject gameState = new GameStatePersistenceObject("", layoutRepository);
		gameState.convertFromPersistence(obj);
		return gameState;
	}
}