package textadventurelib.persistence;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;

/**
 * 
 * @author Jeff Riggle
 *
 *	Creates xml persistence for transition data. Xml should be in the follow format.
 *
 * <pre>
 * {@code
 *	<Transition>
 *		<DisplayType>Window</DisplayType>
 *		<MediaLocation>PathToFile</MediaLocation>
 *	</Transition>
 * }
 * </pre>
 */
public class TransitionPersistenceObject extends XmlConfigurationObject{

	private final static String TRANSITION_NAME = "Transition";
	private final static String DISPLAY_TYPE_NAME = "DisplayType";
	private final static String MEDIA_LOCATION_NAME = "MediaLocation";
	
	private XmlConfigurationObject displayType;
	private XmlConfigurationObject mediaLocation;
	
	/**
	 * Ctor
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public TransitionPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(TRANSITION_NAME);
		
		displayType = new XmlConfigurationObject(DISPLAY_TYPE_NAME);
		mediaLocation = new XmlConfigurationObject(MEDIA_LOCATION_NAME);
	}
	
	/**
	 * 
	 * @param type The new @see DisplayType for this transition.
	 */
	public void displayType(DisplayType type) {
		displayType.value(type);
	}
	
	/**
	 * 
	 * @return The @see DisplayType for this transtion.
	 */
	public DisplayType displayType() {
		return displayType.value();
	}
	
	/**
	 * 
	 * @param path The new location to the display information.
	 */
	public void mediaLocation(String path) {
		mediaLocation.value(path);
	}
	
	/**
	 * 
	 * @return The display file associated with this transition.
	 */
	public String mediaLocation() {
		return mediaLocation.value();
	}
	
	/**
	 * Prepares the xml to be saved. This must be called before saving the xml.
	 */
	public void prepareXml() {
		super.clearChildren();
		super.clearConfigurationProperties();
		
		super.addChild(displayType);
		super.addChild(mediaLocation);
	}
	
	/**
	 * Updates this object based off information stored in a @see XmlConfigurationObject
	 * @param obj The @see XmlConfigurationObject to load data from.
	 */
	public void convertFromPersistence(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			switch (cChild.name()) {
				case DISPLAY_TYPE_NAME:
					displayType(convertToDisplayType(cChild.<String>value()));
					break;
				case MEDIA_LOCATION_NAME:
					mediaLocation(cChild.<String>value());
					break;
				default:
					break;
			}
		}
	}
	
	private DisplayType convertToDisplayType(String type) {
		if (type == null || type.isEmpty()) {
			return null;
		}
		
		if (type.equalsIgnoreCase("SplashScreen")) {
			return DisplayType.SplashScreen;
		}
		
		return DisplayType.Window;
	}
}
