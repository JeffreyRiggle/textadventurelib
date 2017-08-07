package textadventurelib.gamestates;

import java.util.List;

import playerlib.player.IPlayer;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameStateRuntimeData {

	private List<IPlayer> players;
	private String textLog;
	private String currentGameState;
	
	/**
	 * Creates game state runtime data.
	 */
	public GameStateRuntimeData() {
		textLog = new String();
	}
	
	/**
	 * 
	 * @param players The players to use at runtime.
	 */
	public GameStateRuntimeData(List<IPlayer> players) {
		this.players = players;
		this.textLog = new String();
	}
	
	/**
	 * 
	 * @return The players in the runtime.
	 */
	public List<IPlayer> players() {
		return players;
	}
	
	/**
	 * 
	 * @param players The new players for the runtime.
	 */
	public void players(List<IPlayer> players) {
		this.players = players;
	}
	
	/**
	 * 
	 * @param textLog The new text for the text log.
	 */
	public void textLog(String textLog) {
		this.textLog = textLog;
	}
	
	/**
	 * 
	 * @return The text log.
	 */
	public String textLog() {
		return textLog;
	}
	
	/**
	 * 
	 * @param stateId The new current game state.
	 */
	public void currentGameState(String stateId) {
		currentGameState = stateId;
	}
	
	/**
	 * 
	 * @return The current game state.
	 */
	public String currentGameState() {
		return currentGameState;
	}
}
