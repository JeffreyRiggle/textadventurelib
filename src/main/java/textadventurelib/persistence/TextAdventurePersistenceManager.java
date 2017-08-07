package textadventurelib.persistence;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TextAdventurePersistenceManager {

	private XmlConfigurationManager configurationManager;
	private TextAdventurePersistenceObject textAdventure;
	
	/**
	 * Object that manages save data for a game.
	 * @param saveLocation The location to save and load the game data from.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public TextAdventurePersistenceManager(String saveLocation) throws TransformerConfigurationException, ParserConfigurationException, SAXException, IOException {
		configurationManager = new XmlConfigurationManager(saveLocation);
		textAdventure = new TextAdventurePersistenceObject();
	}
	
	/**
	 * 
	 * @param tav A new @see TextAdventurePersistenceObject to assoicate with this game.
	 */
	public void textAdventure(TextAdventurePersistenceObject tav) {
		textAdventure = tav;
	}
	
	/**
	 * 
	 * @return The current @see TextAdventurePersistenceObject associated with this game.
	 */
	public TextAdventurePersistenceObject textAdventure() {
		return textAdventure;
	}
	
	/**
	 * Loads the game from the save location specified in the constructor of this class. 
	 * After the load completes {@link #textAdventure()} will return the data retrived from the save location.
	 * 
	 * @throws TransformerConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public void load() throws TransformerConfigurationException, SAXException, IOException, ParserConfigurationException {
		configurationManager.load();
		
		TextAdventurePersistenceObject textAdventure = new TextAdventurePersistenceObject();
		textAdventure.convertFromPersistence((XmlConfigurationObject)configurationManager.configurationObjects().get(0));
		textAdventure(textAdventure);
	}
	
	/**
	 * Saves the persistence data for {@link #textAdventure()} to the save location specified in the constructor for this class.
	 * 
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public void save() throws ParserConfigurationException, TransformerException {
		configurationManager.clearConfigurationObjects();
		
		textAdventure.prepareXml();
		configurationManager.addConfigurationObject(textAdventure);
		configurationManager.save();
	}
}
