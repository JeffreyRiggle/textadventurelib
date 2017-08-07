package textadventurelib.core;

import java.util.List;
import javax.swing.JFrame;

import ilusr.gamestatemanager.IGameStateManager;
import javafx.scene.layout.AnchorPane;
import playerlib.player.IPlayer;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface ITextAdventureGameStateManager extends IGameStateManager {
	/**
	 * 
	 * @param frame The JFrame for the game state manager.
	 */
	void setFrame(JFrame frame);
	/**
	 * 
	 * @param pane The AnchorPane for the game state manager.
	 */
	void setStage(AnchorPane pane);
	/**
	 * 
	 * @param players The players to set for this game.
	 */
	void players(List<IPlayer> players);
	/**
	 * 
	 * @return The players in this game.
	 */
	List<IPlayer> players();
	/**
	 * 
	 * @param listener A listener to add.
	 */
	void addCompletionListener(ICompletionListener listener);
	/**
	 * 
	 * @param listener A listener to remove.
	 */
	void removeCompletionListener(ICompletionListener listener);
}
