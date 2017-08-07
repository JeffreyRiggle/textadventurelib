package textadventurelib.core;

import java.util.List;

import playerlib.player.IPlayer;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ExecutionParameters {

	private List<IPlayer> players;
	private String currentGameState;
	
	/**
	 * 
	 * @param players The players to execute with.
	 */
	public ExecutionParameters(List<IPlayer> players) {
		this(players, new String());
	}
	
	/**
	 * 
	 * @param players The players to execute with.
	 * @param currentGameState The current game state.
	 */
	public ExecutionParameters(List<IPlayer> players, String currentGameState) {
		this.players = players;
		this.currentGameState = currentGameState;
	}
	
	/**
	 * 
	 * @return The players for this execution.
	 */
	public List<IPlayer> players() {
		return players;
	}
	
	/**
	 * 
	 * @return The current game state.
	 */
	public String currentGameState() {
		return currentGameState;
	}
}
