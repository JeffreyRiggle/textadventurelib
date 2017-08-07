package textadventurelib.persistence;

import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.logrunner.LogRunner;
import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.actions.IAction;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class SaveActionPersistenceObject  extends ActionPersistenceObject {

	private static final String SAVE_ACTION = "Save";
	private static final String SAVE_LOCATION = "SaveLocation";
	private static final String GAME_STATE_LOCATION = "GameStatesLocation";
	private static final String BLOCKING = "Blocking";
	
	private XmlConfigurationObject saveLocation;
	private XmlConfigurationObject gameStatesLocation;
	private XmlConfigurationObject blocking;
	
	/**
	 * Basic ctor.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public SaveActionPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(SAVE_ACTION);
		saveLocation = new XmlConfigurationObject(SAVE_LOCATION, new String());
		gameStatesLocation = new XmlConfigurationObject(GAME_STATE_LOCATION, new String());
		this.blocking = new XmlConfigurationObject(BLOCKING, false);
	}
	
	/**
	 * Creates a SaveActionPersistenceObject based off of a @see XmlConfigurationObject
	 * 
	 * @param obj The @see XmlConfigurationObject to create this action off of.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public SaveActionPersistenceObject(XmlConfigurationObject obj) throws TransformerConfigurationException, ParserConfigurationException {
		this();
		convertFromPersistence(obj);
	}

	/**
	 * 
	 * @return The location to save the game to.
	 */
	public String saveLocation() {
		return saveLocation.<String>value();
	}
	
	/**
	 * 
	 * @param location The new location to save the game to.
	 */
	public void saveLocation(String location) {
		super.removeParameter(saveLocation);
		saveLocation.value(location);
		super.addParameter(saveLocation);
	}
	
	/**
	 * 
	 * @return The location where the game states are stored at.
	 */
	public String gameStatesLocation() {
		return gameStatesLocation.<String>value();
	}
	
	/**
	 * 
	 * @param location The new location where the game states are stored at.
	 */
	public void gameStatesLocation(String location) {
		super.removeParameter(gameStatesLocation);
		gameStatesLocation.value(location);
		super.addParameter(gameStatesLocation);
	}
	
	/**
	 * 
	 * @return If the save action should be ran synchronously or not.
	 */
	public boolean blocking() {
		return blocking.<Boolean>value();
	}
	
	/**
	 * 
	 * @param blocking If the save action should be ran synchronously or not.
	 */
	public void blocking(boolean blocking) {
		super.removeParameter(this.blocking);
		this.blocking.value(blocking);
		super.addParameter(this.blocking);
	}
	
	@Override
	public void prepareXml() throws TransformerConfigurationException, ParserConfigurationException {
		super.prepareXml();
	}
	
	@Override
	public IAction convertToAction() {
		SaveAction action = null;
		
		try {
			action = new SaveAction(saveLocation(), blocking(), gameStatesLocation());
		} catch (Exception e) {
			LogRunner.logger().log(Level.INFO, "Failed to create action");
		}
		
		return action;
	}
	
	/**
	 * 
	 * @param obj The @see XmlConfigurationObject to update this action with.
	 */
	public void convertFromPersistence(XmlConfigurationObject obj) {
		
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			switch (cChild.name()) {
				case "Parameters":
					convert(cChild);
					break;
				default:
					break;
			}
		}
	}
	
	private void convert(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			switch (cChild.name()) {
				case SAVE_LOCATION:
					saveLocation(cChild.<String>value());
					break;
				case GAME_STATE_LOCATION:
					gameStatesLocation(cChild.<String>value());
					break;
				case BLOCKING:
					blocking(cChild.<Boolean>value());
					break;
				default:
					break;
			}
		}
	}
}
