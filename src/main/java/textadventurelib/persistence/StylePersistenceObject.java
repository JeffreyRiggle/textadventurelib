package textadventurelib.persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class StylePersistenceObject extends XmlConfigurationObject {

	private static final String STYLE_PERSISTENCE = "Style";
	private final String SELECTORS = "Selectors";
	
	private List<StyleSelectorPersistenceObject> selectors;
	
	/**
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public StylePersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(STYLE_PERSISTENCE);
		
		selectors = new ArrayList<StyleSelectorPersistenceObject>();
	}
	
	/**
	 * 
	 * @param selector The selector to add.
	 */
	public void addSelector(StyleSelectorPersistenceObject selector) {
		selectors.add(selector);
	}
	
	/**
	 * 
	 * @param selector The selector to remove.
	 */
	public void removeSelector(StyleSelectorPersistenceObject selector) {
		selectors.remove(selector);
	}
	
	/**
	 * 
	 * @return All selectors
	 */
	public List<StyleSelectorPersistenceObject> getSelectors() {
		return Collections.unmodifiableList(selectors);
	}
	
	/**
	 * 
	 * @return A css string based off of configured selectors
	 */
	public String compile() {
		StringBuilder builder = new StringBuilder();
		
		for (StyleSelectorPersistenceObject selector : selectors) {
			builder.append(selector.compile());
		}
		
		return builder.toString();
	}
	
	/**
	 * Prepares this object to be persisted.
	 */
	public void prepareXml() {
		super.clearChildren();
		
		try {
			XmlConfigurationObject selectorsNode = new XmlConfigurationObject(SELECTORS);
			
			for (StyleSelectorPersistenceObject selector : selectors) {
				selector.prepareXml();
				selectorsNode.addChild(selector);
			}
			
			super.addChild(selectorsNode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param obj The persistence object to convert.
	 */
	public void convertFromPersistence(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			if (cChild.name().equals(SELECTORS)) {
				convertSelectors(cChild);
			}
		}
	}
	
	private void convertSelectors(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			try {
				StyleSelectorPersistenceObject selector = new StyleSelectorPersistenceObject();
				selector.convertFromPersistence(cChild);
				addSelector(selector);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
