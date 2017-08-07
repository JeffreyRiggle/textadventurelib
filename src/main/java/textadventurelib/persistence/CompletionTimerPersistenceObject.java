package textadventurelib.persistence;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.timers.CompletionTimerTaskFactory;
import textadventurelib.timers.TimerHelper;

/**
 * 
 * @author Jeff Riggle
 *
 *	Creates xml in the following format:
 *	
 * <pre>
 * {@code
 *	<Timer type="Completion">
 *		<Duration>Time until timer fires</Duration>
 *		<CompletionData>Data to complete with</CompletionData>
 *	</Timer>
 * }
 * </pre>
 */
public class CompletionTimerPersistenceObject extends TimerPersistenceObject{

	private static final String COMPLETION_TYPE = "Completion";
	private static final String COMPLETION_DATA_NAME = "CompletionData";
	
	private XmlConfigurationObject completionData;
	
	/**
	 * Ctor.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public CompletionTimerPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(COMPLETION_TYPE);
		
		completionData = new XmlConfigurationObject(COMPLETION_DATA_NAME);
	}
	
	/**
	 * 
	 * @param data The new data to complete with for this timer.
	 */
	public <T> void completionData(T data) {
		completionData.value(data);
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @return The data to complete with for this timer.
	 */
	public <T> T completionData() {
		return (T)completionData.value();
	}
	
	@Override
	public void prepareXml() {
		super.prepareXml();
		super.addChild(completionData);
	}
	
	@Override
	public TimerHelper convertToTimer() {
		TimerHelper timer = new TimerHelper(new CompletionTimerTaskFactory(completionData()), duration());
		return timer;
	}
	
	public void convertFromPersistence(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			switch (cChild.name()) {
				case COMPLETION_DATA_NAME:
					completionData(cChild.value());
					break;
				case "Duration":
					String dur = cChild.<String>value();
					duration(Long.parseLong(dur));
					break;
				default:
					break;
			}
		}
	}
}
