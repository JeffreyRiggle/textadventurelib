package textadventurelib.persistence;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.xml.sax.SAXException;

import ilusr.logrunner.LogRunner;
import playerlib.player.IPlayer;
import textadventurelib.actions.IAction;
import textadventurelib.core.ExecutionParameters;
import textadventurelib.persistence.player.PlayerPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class SaveAction implements IAction{

	private boolean blocking;
	private Object saveLock;
	private TextAdventurePersistenceManager manager;
	private String saveLocation;
	
	/**
	 * Creates a save action which allows a game state to be saved.
	 * 
	 * <p>
	 * Note: a relative path can be specified for saveLocation and gameStateLocation.
	 * To specify a relative location use the following format {@code ./pathtofile.xml}.
	 * In the case of relative location the location will be relative to the executing jar file.
	 * </p>
	 * 
	 * @param saveLocation The file location to save to.
	 * @param blocking if the save should block the application thread.
	 * @param gameStateLocation The location of the game states file.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public SaveAction(String saveLocation, boolean blocking, String gameStateLocation) throws TransformerConfigurationException, ParserConfigurationException, SAXException, IOException {
		this.blocking = blocking;
		saveLock = new Object();
		this.saveLocation = buildSaveLocation(saveLocation);
		manager = new TextAdventurePersistenceManager(this.saveLocation);
		manager.textAdventure().gameStatesLocation(buildSaveLocation(gameStateLocation));
		manager.textAdventure().gameStatesInline(false);
		manager.textAdventure().playersInline(true);
	}
	
	private String buildSaveLocation(String location) {
		if (!location.startsWith("./")) {
			return location;
		}
		
		File jarDomain = null;
		
		try {
			jarDomain = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jarDomain == null ? location : jarDomain.getParent() + location.substring(1);
	}
	
	@Override
	public void execute(final ExecutionParameters params) {
		if (blocking) {
			save(params);
			return;
		}
		
		new Thread(() -> {
			save(params);
		}).start();
	}
	
	private void save(ExecutionParameters params) {
		synchronized(saveLock) {
			LogRunner.logger().info(String.format("Saving data to %s", saveLocation));
			updateManager(params);
			
			try {
				manager.save();
			} catch (Exception e) {
				LogRunner.logger().warning("Unable to create player object");
			}
			
			LogRunner.logger().info("Finished saving.");
		}
	}
	
	private void updateManager(ExecutionParameters params) {
		manager.textAdventure().players().clear();
		
		for (IPlayer player : params.players()) {
			try {
				manager.textAdventure().players().add(new PlayerPersistenceObject(player));
			} catch (Exception e) {
				LogRunner.logger().warning(String.format("Failed to save player: %s", player.name()));
			}
		}
		
		manager.textAdventure().currentGameState(params.currentGameState());
	}
}
