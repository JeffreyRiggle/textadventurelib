package textadventurelib.core;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import ilusr.gamestatemanager.BufferedGameStateManager;
import ilusr.gamestatemanager.GameState;
import ilusr.logrunner.LogRunner;
import javafx.scene.layout.AnchorPane;
import playerlib.player.IPlayer;
import textadventurelib.gamestates.GameStateCompletionData;
import textadventurelib.gamestates.GameStateProvider;
import textadventurelib.gamestates.GameStateRuntimeData;

//TODO: Test this. Implement it. Document it. Consider if it should be in this package.
public class BufferedTextAdventureGameStateManager extends BufferedGameStateManager implements ITextAdventureGameStateManager {

	private WindowProvider windowProvider;
	private GameStateRuntimeData runtimeData;
	private List<ICompletionListener> listeners;
	
	public <T>BufferedTextAdventureGameStateManager(GameStateProvider provider, int bufferSize, T id, GameState gameState) {
		this(provider, bufferSize, id, gameState, null, new ArrayList<IPlayer>());
	}
	
	public <T>BufferedTextAdventureGameStateManager(GameStateProvider provider, int bufferSize, T id, GameState gameState, JFrame mainWindow, List<IPlayer> players) {
		super(bufferSize, provider, id, gameState, null);
		runtimeData = new GameStateRuntimeData(players);
		super.runtimeData(runtimeData);
		listeners = new ArrayList<ICompletionListener>();
	}
	
	@Override
	public void setFrame(JFrame frame) {
		windowProvider = new WindowProvider(frame);
	}

	@Override
	public void setStage(AnchorPane pane) {
		windowProvider = new WindowProvider(pane);
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
		
		//TODO: Changes to this are iffy.
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

	@Override
	public void addCompletionListener(ICompletionListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeCompletionListener(ICompletionListener listener) {
		listeners.remove(listener);
	}
	
	private <T>void notifyListeners(T data) {
		for(ICompletionListener listener : listeners) {
			listener.completed(data);
		}
	}
}
