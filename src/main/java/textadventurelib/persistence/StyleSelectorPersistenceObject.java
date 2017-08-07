package textadventurelib.persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.ConfigurationProperty;
import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class StyleSelectorPersistenceObject extends XmlConfigurationObject {

	private static final String STYLE_SELECTOR = "StyleSelector";
	private final String STYLE_PROPERTIES = "StyleProperties";
	private final String SELECTOR = "selector";
	
	private List<StylePropertyPersistenceObject> properties;
	private ConfigurationProperty selector;
	
	/**
	 * 
	 * @param styleSelector The persistence selector to clone.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public StyleSelectorPersistenceObject(StyleSelectorPersistenceObject styleSelector) throws TransformerConfigurationException, ParserConfigurationException {
		super(STYLE_SELECTOR);
		
		selector = new ConfigurationProperty(SELECTOR, styleSelector.getSelector());
		properties = new ArrayList<StylePropertyPersistenceObject>();
		
		for (StylePropertyPersistenceObject prop : styleSelector.getStyleProperties()) {
			properties.add(new StylePropertyPersistenceObject(prop.getPropertyType(), prop.getPropertyValue()));
		}
	}
	
	/**
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public StyleSelectorPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(STYLE_SELECTOR);
		
		selector = new ConfigurationProperty(SELECTOR, "");
		properties = new ArrayList<StylePropertyPersistenceObject>();
	}
	
	/**
	 * 
	 * @param prop The property to add to this selector.
	 */
	public void addStyleProperty(StylePropertyPersistenceObject prop) {
		properties.add(prop);
	}
	
	/**
	 * 
	 * @param prop The property to remove from this selector.
	 */
	public void removeStyleProperty(StylePropertyPersistenceObject prop) {
		properties.remove(prop);
	}
	
	/**
	 * 
	 * @return The properties associated with this selector.
	 */
	public List<StylePropertyPersistenceObject> getStyleProperties() {
		return Collections.unmodifiableList(properties);
	}
	
	/**
	 * 
	 * @param selector The new id for this selector.
	 */
	public void setSelector(String selector) {
		this.selector.value(selector);
	}
	
	/**
	 * 
	 * @return The id for this selector.
	 */
	public String getSelector() {
		return selector.value();
	}
	
	/**
	 * 
	 * @return The css string representation of this selector.
	 */
	public String compile() {
		StringBuilder builder = new StringBuilder();
		builder.append(selector.value());
		builder.append(" {\n");
		
		for (StylePropertyPersistenceObject value : properties) {
			String style = value.compile();
			
			if (style.isEmpty()) {
				continue;
			}
			
			builder.append("\t");
			builder.append(style);
			builder.append("\n");
		}
		
		builder.append("}\n");
		return builder.toString();
	}
	
	/**
	 * Prepare this object for persistence.
	 */
	public void prepareXml() {
		super.clearChildren();
		super.clearConfigurationProperties();
		
		super.addConfigurationProperty(selector);
		
		XmlConfigurationObject props = buildProperties();
		if (props != null) {
			super.addChild(props);
		}
	}
	
	private XmlConfigurationObject buildProperties() {
		XmlConfigurationObject retVal = null;
		if (properties.size() == 0) {
			return retVal;
		}
		
		try {
			retVal = new XmlConfigurationObject(STYLE_PROPERTIES);
			
			for (StylePropertyPersistenceObject value : properties) {
				value.prepareXml();
				retVal.addChild(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retVal;
	}
	
	/**
	 * 
	 * @param obj The object to convert from persistence.
	 */
	public void convertFromPersistence(XmlConfigurationObject obj) {
		convertProperties(obj);
		
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			if (cChild.name().equals(STYLE_PROPERTIES)) {
				convertStyleProperties(cChild);
			}
		}
	}
	
	private void convertProperties(XmlConfigurationObject obj) {
		for (ConfigurationProperty prop : obj.configurationProperties()) {
			if (prop.name().equals(SELECTOR)) {
				setSelector(prop.value());
			}
		}
	}
	
	private void convertStyleProperties(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			try {
				StylePropertyPersistenceObject prop = new StylePropertyPersistenceObject();
				prop.convertFromPersistence(cChild);
				addStyleProperty(prop);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
