package textadventurelib.core;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import ilusr.gamestatemanager.GameState;
import ilusr.gamestatemanager.GameStateManager;
import ilusr.logrunner.LogRunner;
import javafx.scene.layout.AnchorPane;
import playerlib.player.IPlayer;
import textadventurelib.gamestates.GameStateCompletionData;
import textadventurelib.gamestates.GameStateRuntimeData;

//TODO: Should we provide all players or should the
// game state let us know what players it cares about.
/**
 * 
 * @author Jeff Riggle
 *
 */
public class TextAdventureGameStateManager extends GameStateManager implements ITextAdventureGameStateManager{
	private WindowProvider windowProvider;
	private GameStateRuntimeData runtimeData;
	private List<ICompletionListener> listeners;
	
	/**
	 * 
	 * @param id The id of the starting game state.
	 * @param gameState The starting game state.
	 */
	public <T>TextAdventureGameStateManager(T id, GameState gameState) {
		this(id, gameState, new ArrayList<IPlayer>());
	}
	
	/**
	 * 
	 * @param id The id of the starting game state.
	 * @param gameState The starting game state.
	 * @param players The players in this game.
	 */
	public <T>TextAdventureGameStateManager(T id, GameState gameState, List<IPlayer> players) {
		this(id, gameState, null, null, players);
	}
	
	/**
	 * 
	 * @param id The id of the starting game state.
	 * @param gameState The starting game state.
	 * @param mainWindow The frame to use for this game.
	 */
	public <T>TextAdventureGameStateManager(T id, GameState gameState, JFrame mainWindow) {
		this(id, gameState, mainWindow, new ArrayList<IPlayer>());
	}

	/**
	 * 
	 * @param id The id of the starting game state.
	 * @param gameState The starting game state.
	 * @param pane The AnchorPane to use for this game.
	 */
	public <T>TextAdventureGameStateManager(T id, GameState gameState, AnchorPane pane) {
		this(id, gameState, null, pane, new ArrayList<IPlayer>());
	}
	
	/**
	 * 
	 * @param id The id of the starting game state.
	 * @param gameState The starting game state.
	 * @param mainWindow The frame to use for this game.
	 * @param players The players in this game.
	 */
	public <T>TextAdventureGameStateManager(T id, GameState gameState, JFrame mainWindow, List<IPlayer> players) {
		this(id, gameState, mainWindow, null, new ArrayList<IPlayer>());
	}
	
	/**
	 * 
	 * @param id The id of the starting game state.
	 * @param gameState The starting game state.
	 * @param pane The AnchorPane to use for this game.
	 * @param players The players in this game.
	 */
	public <T>TextAdventureGameStateManager(T id, GameState gameState, AnchorPane pane, List<IPlayer> players) {
		this(id, gameState, null, pane, new ArrayList<IPlayer>());
	}
	
	private <T>TextAdventureGameStateManager(T id, GameState gameState, JFrame frame, AnchorPane pane, List<IPlayer> players) {
		super(id, gameState);
		if (frame != null) {
			windowProvider = new WindowProvider(frame);
		} else if (pane != null) {
			windowProvider = new WindowProvider(pane);
		}
		
		listeners = new ArrayList<ICompletionListener>();
		runtimeData = new GameStateRuntimeData(players);
		runtimeData.currentGameState((String)id);
		super.runtimeData(runtimeData);
	}
	
	@Override
	public void setFrame(JFrame frame) {
		windowProvider = new WindowProvider(frame);
	}
	
	public void setStage(AnchorPane pane) {
		windowProvider = new WindowProvider(pane);
	}
	
	@Override
	public void addCompletionListener(ICompletionListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removeCompletionListener(ICompletionListener listener) {
		listeners.remove(listener);
	}
	
	@Override
	public void players(List<IPlayer> players) {
		runtimeData.players(players);
		super.runtimeData(runtimeData);
	}
	
	@Override
	public List<IPlayer> players() {
		return runtimeData.players();
	}
	
	@Override
	public void start() {
		LogRunner.logger().info("Starting text adventure game");
		super.start();
		
		//TODO: is there a better way to do this?
		Container cont = (Container)super.currentGameState().stateContent();
		windowProvider.setContent(cont);
	}
	
	@Override
	public <T>void completed(T data) {
		GameStateCompletionData gameStateData = null;
		
		if (data instanceof GameStateCompletionData) {
			gameStateData = (GameStateCompletionData)data;
			LogRunner.logger().info(String.format("Setting text log to: '%s'", gameStateData.<String>extraData()));
			runtimeData.textLog(gameStateData.<String>extraData());
		}
		
		if (gameStateData != null) {
			runtimeData.currentGameState(gameStateData.<String>completionData());
			super.runtimeData(runtimeData);
			super.completed(gameStateData.completionData());
		} else {
			runtimeData.currentGameState((String)data);
			super.runtimeData(runtimeData);
			super.completed(data);
		}
		
		//TODO: Find a better way to get the container.
		Container cont = (Container)super.currentGameState().stateContent();
		windowProvider.setContent(cont);
		notifyListeners(data);
	}
	
	private <T>void notifyListeners(T data) {
		for(ICompletionListener listener : listeners) {
			listener.completed(data);
		}
	}
}
