package textadventurelib.core;

import java.util.ArrayList;
import java.util.List;

import playerlib.player.IPlayer;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TriggerParameters {

	private List<IPlayer> players;
	private String message;
	
	/**
	 * Creates trigger parameters.
	 */
	public TriggerParameters() {
		this(new String(), new ArrayList<IPlayer>());
	}
	
	/**
	 * 
	 * @param message The message to process.
	 */
	public TriggerParameters(String message) {
		this(message, new ArrayList<IPlayer>());
	}
	
	/**
	 * 
	 * @param players The players to process.
	 */
	public TriggerParameters(List<IPlayer> players) {
		this(new String(), players);
	}
	
	/**
	 * 
	 * @param message The message to process.
	 * @param players The players to process.
	 */
	public TriggerParameters(String message, List<IPlayer> players) {
		this.message = message;
		this.players = players;
	}
	
	/**
	 * 
	 * @return The message to process.
	 */
	public String message() {
		return message;
	}
	
	/**
	 * 
	 * @param message The new message to process.
	 */
	public void message(String message) {
		this.message = message;
	}
	
	/**
	 * 
	 * @return The players to process.
	 */
	public List<IPlayer> players() {
		return players;
	}
	
	/**
	 * 
	 * @param players The new players to process.
	 */
	public void players(List<IPlayer> players) {
		this.players = players;
	}
}
