package textadventurelib.persistence;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;


/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameInfoPersistenceObject extends ParameterizedPersistenceObject {

	private static final String GAME_INFO_NAME = "GameInfo";
	private static final String DESCRIPTION_NAME = "Description";
	private static final String GAME_NAME = "GameName";
	private static final String CREATOR_NAME = "Creator";
	
	private XmlConfigurationObject gameName;
	private XmlConfigurationObject description;
	private XmlConfigurationObject creator;
	
	/**
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public GameInfoPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(GAME_INFO_NAME);
		
		gameName = new XmlConfigurationObject(GAME_NAME);
		description = new XmlConfigurationObject(DESCRIPTION_NAME);
		creator = new XmlConfigurationObject(CREATOR_NAME);
	}
	
	/**
	 * 
	 * @param obj The object to convert from.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public GameInfoPersistenceObject(XmlConfigurationObject obj) throws TransformerConfigurationException, ParserConfigurationException {
		this();
		convertFromPersistence(obj);
	}
	
	/**
	 * 
	 * @return The name of the game.
	 */
	public String gameName() {
		return gameName.<String>value();
	}
	
	/**
	 * 
	 * @param name The new name for the game.
	 */
	public void gameName(String name) {
		super.removeParameter(gameName);
		gameName.value(name);
		super.addParameter(gameName);
	}
	
	/**
	 * 
	 * @return The description of the game.
	 */
	public String description() {
		return description.<String>value();
	}
	
	/**
	 * 
	 * @param description The new description of the game.
	 */
	public void description(String description) {
		super.removeParameter(this.description);
		this.description.value(description);
		super.addParameter(this.description);
	}
	
	/**
	 * 
	 * @return The creator of the game.
	 */
	public String creator() {
		return creator.<String>value();
	}
	
	/**
	 * 
	 * @param creator The new creator of the game.
	 */
	public void creator(String creator) {
		super.removeParameter(this.creator);
		this.creator.value(creator);
		super.addParameter(this.creator);
	}
	
	@Override
	public void prepareXml() throws TransformerConfigurationException, ParserConfigurationException {
		super.prepareXml();
	}
	
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
				case GAME_NAME:
					gameName(cChild.<String>value());
					break;
				case CREATOR_NAME:
					creator(cChild.<String>value());
					break;
				case DESCRIPTION_NAME:
					description(cChild.<String>value());
					break;
				default:
					break;
			}
		}
	}
}
