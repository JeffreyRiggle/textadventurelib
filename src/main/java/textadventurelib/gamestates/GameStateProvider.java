package textadventurelib.gamestates;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import ilusr.gamestatemanager.IDataSource;
import ilusr.gamestatemanager.IGameState;
import ilusr.logrunner.LogRunner;
import textadventurelib.persistence.CompletionActionPersistence;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.OptionPersistenceObject;
import textadventurelib.persistence.TextAdventurePersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameStateProvider implements IDataSource{

	private TextAdventurePersistenceObject source;
	
	/**
	 * 
	 * @param source The @see TextAdventurePersistenceObject to convert game states from.
	 */
	public GameStateProvider(TextAdventurePersistenceObject source) {
		this.source = source;
	}
	
	@Override
	public Map<Object, IGameState> provideGameStates(int bufferSize,
			Object state) {
		LogRunner.logger().log(Level.INFO, String.format("Getting Game States with a buffer size of %s", bufferSize));
		Map<Object, GameStatePersistenceObject> persistList = new LinkedHashMap<Object, GameStatePersistenceObject>();
		TextAdventureGameState nextState = null;
		GameStatePersistenceObject persistedState = null;
		
		//Find the game state that maps to state.
		for (GameStatePersistenceObject gs : source.gameStates()) {
			if (gs.stateId().equals(state)) {
				persistedState = gs;
				nextState = (TextAdventureGameState)gs.convertToGameState();
				LogRunner.logger().log(Level.INFO, String.format("Found next game state: %s", gs.stateId()));
				break;
			}
		}
		
		if (nextState == null || persistedState == null) {
			//Error condition.
			LogRunner.logger().log(Level.WARNING, "Unable to find any game states");
			return new HashMap<Object, IGameState>();
		}
		
		//Find the states it maps to
		persistList.putAll(findChildren(persistedState, bufferSize));
		if (persistList.size() == bufferSize) {
			return convertGameMap(persistList);
		}
		
		Map<Object, GameStatePersistenceObject> additionalStates = getAdditionalStates(persistList, persistList, bufferSize);
		
		if (additionalStates.size() > 0) {
			persistList.putAll(additionalStates);
		}
		
		return convertGameMap(persistList);
	}

	private Map<Object, GameStatePersistenceObject> getAdditionalStates(Map<Object, GameStatePersistenceObject> allStates, Map<Object, GameStatePersistenceObject> currentStates, int targetBufferSize) {
		Map<Object, GameStatePersistenceObject> additionalStates = new LinkedHashMap<Object, GameStatePersistenceObject>();
		
		for (Entry<Object, GameStatePersistenceObject> entry : currentStates.entrySet()) {
			if (additionalStates.size() >= targetBufferSize) break;
			
			Map<Object, GameStatePersistenceObject> tempStates = findChildren(entry.getValue(), targetBufferSize);
			
			additionalStates.putAll(tempStates);
		}

		Map<Object, GameStatePersistenceObject> mergedStates = mergeMaps(allStates, additionalStates);
		
		int offset = mergedStates.size() - allStates.size();
		allStates = mergedStates;
		
		if (offset == 0) {
			LogRunner.logger().log(Level.INFO, String.format("No Diff found in lists, resetting bufferSize to %s", targetBufferSize));
			return allStates;
		}
		
		if (targetBufferSize <= additionalStates.size()) {
			return allStates;
		}
		
		return getAdditionalStates(allStates, additionalStates, targetBufferSize);
	}
	
	private Map<Object, GameStatePersistenceObject> findChildren(GameStatePersistenceObject gameState, int bufferSize) {
		Map <Object, GameStatePersistenceObject> retVal = new LinkedHashMap<Object, GameStatePersistenceObject>();
		retVal.put(gameState.stateId(), gameState);
		
		if (retVal.size() == bufferSize) {
			LogRunner.logger().log(Level.INFO, String.format("Not finding child game states for game state: %s since buffer is full", gameState.stateId()));
			return retVal;
		}
		
		LogRunner.logger().log(Level.INFO, String.format("Finding child game states for game state: %s", gameState.stateId()));
		
		for (OptionPersistenceObject opt : gameState.options()) {
			if (!(opt.action() instanceof CompletionActionPersistence)) continue;
			
			String id = ((CompletionActionPersistence)opt.action()).<String>completionData();
			GameStatePersistenceObject gs = findGameState(id);
			
			if (gs == null) continue;
			
			LogRunner.logger().log(Level.INFO, String.format("Found Child: %s", id));
			retVal.put(id, gs);
			
			if (retVal.size() == bufferSize) {
				return retVal;
			}
		}
		
		return retVal;
	}
	
	private Map<Object, IGameState> convertGameMap(Map<Object, GameStatePersistenceObject> gameMap) {
		Map<Object, IGameState> retVal = new LinkedHashMap<Object, IGameState>();
		
		for (Entry<Object, GameStatePersistenceObject> entry : gameMap.entrySet()) {
			retVal.put(entry.getKey(), entry.getValue().convertToGameState());
		}
		
		return retVal;
	}
	
	private GameStatePersistenceObject findGameState(String id) {
		for (GameStatePersistenceObject gs : source.gameStates()) {
			if (gs.stateId().equals(id)) {
				return gs;
			}
		}
		
		return null;
	}
	
	private Map<Object, GameStatePersistenceObject> mergeMaps(Map<Object, GameStatePersistenceObject> m1, Map<Object, GameStatePersistenceObject> m2) {
		Map<Object, GameStatePersistenceObject> retVal = new LinkedHashMap<Object, GameStatePersistenceObject>();
		
		for (Entry<Object, GameStatePersistenceObject> entry : m1.entrySet()) {
			retVal.put(entry.getKey(), entry.getValue());
		}
		
		for (Entry<Object, GameStatePersistenceObject> entry : m2.entrySet()) {
			if (retVal.containsKey(entry.getKey())) continue;
			
			retVal.put(entry.getKey(), entry.getValue());
		}
		
		return retVal;
	}
}
