package textadventurelib.persistence;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.ConfigurationProperty;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.timers.TimerHelper;

/**
 * 
 * @author Jeff Riggle
 *
 *	Creates an xml object similar to:
 *
 * <pre>
 * {@code
 *	<Timer type="Type of Timer">
 *		<Duration>time until action fires</Duration>
 *	</Timer>
 * }
 * </pre>
 * 
 */
public abstract class TimerPersistenceObject extends XmlConfigurationObject{

	private static final String TIMER_NAME = "Timer";
	private static final String DURATION_NAME = "Duration";
	private static final String TYPE_NAME = "type";
	
	private XmlConfigurationObject duration;
	private ConfigurationProperty type;
	
	/**
	 * 
	 * @param type The type of timer object this is representing.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public TimerPersistenceObject(String type) throws TransformerConfigurationException, ParserConfigurationException {
		super(TIMER_NAME);
		
		duration = new XmlConfigurationObject(DURATION_NAME, new Long(0));
		this.type = new ConfigurationProperty(TYPE_NAME, type);
	}
	
	/**
	 * 
	 * @param duration The new amount of time before the timer elapses.
	 */
	public void duration(long duration) {
		this.duration.value(duration);
	}
	
	/**
	 * 
	 * @return The amount of time before the timer elapses.
	 */
	public long duration() {
		return duration.value();
	}
	
	/**
	 * 
	 * @param type The new type for this timer.
	 */
	public void type(String type) {
		this.type.value(type);
	}
	
	/**
	 * 
	 * @return The type assoicated with this timer.
	 */
	public String type() {
		return type.value();
	}
	
	/**
	 * Prepares the xml to be saved. This must be called before saving the Xml!
	 */
	public void prepareXml() {
		super.addConfigurationProperty(type);
		super.addChild(duration);
	}
	
	public abstract TimerHelper convertToTimer();
}
