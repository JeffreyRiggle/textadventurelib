package textadventurelib.persistence;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.ConfigurationProperty;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import javafx.scene.paint.Color;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class StylePropertyPersistenceObject extends XmlConfigurationObject {

	private static final String STYLE_PROPERTY = "StyleProperty";
	private final String STYLE_TYPE = "styleType";
	private final String STYLE_VALUE = "styleValue";
	
	private ConfigurationProperty styleType;
	private ConfigurationProperty styleValue;
	
	/**
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public StylePropertyPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		this(StyleType.None, "");
	}
	
	/**
	 * 
	 * @param type The type of style
	 * @param value The initial value for the style.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public StylePropertyPersistenceObject(StyleType type, String value) throws TransformerConfigurationException, ParserConfigurationException {
		super(STYLE_PROPERTY);
		
		styleType = new ConfigurationProperty(STYLE_TYPE, type.toString());
		styleValue = new ConfigurationProperty(STYLE_VALUE, value);
	}
	
	/**
	 * 
	 * @return The type of style.
	 */
	public StyleType getPropertyType() {
		return styleType.value().isEmpty() ? StyleType.None : StyleType.valueOf(styleType.value());
	}
	
	/**
	 * 
	 * @param type The new type of style to use.
	 */
	public void setPropertyType(StyleType type) {
		styleType.value(type.toString());
	}
	
	/**
	 * 
	 * @param value The new value for the style.
	 */
	public void setPropertyValue(String value) {
		styleValue.value(value);
	}
	
	/**
	 * 
	 * @return The value for the style.
	 */
	public String getPropertyValue() {
		return styleValue.value();
	}
	
	/**
	 * 
	 * @return A css representation of the property.
	 */
	public String compile() {
		StyleType type = getPropertyType();
		
		if (type == StyleType.None) {
			return new String();
		}
		
		String val = styleValue.value();
		if (type == StyleType.Background && !isBackgroundColor()) {
			val = String.format("url(%s)", val);
		} else if (type == StyleType.FontFamily) {
			val = String.format("\"%s\"", val);
		}
		
		return String.format("%s : %s;", convertType(type), val);
	}
	
	private String convertType(StyleType type) {
		String retVal = new String();
		
		switch (type) {
			case Background:
				return convertBackground();
			case BackgroundRepeat:
				return "-fx-background-repeat";
			case BackgroundSize:
				return "-fx-background-size";
			case Color:
				return "-fx-text-fill";
			case FontFamily:
				return "-fx-font-family";
			case FontSize:
				return "-fx-font-size";
			case FontStyle:
				return styleValue.value().equalsIgnoreCase("bold") ? "-fx-font-weight" : "-fx-font-style";
			case None:
			default:
				break;
		}
		
		return retVal;
	}
	
	private String convertBackground() {
		if (isBackgroundColor()) {
			return "-fx-background-color";
		}
		return "-fx-background-image";
	}
	
	/**
	 * 
	 * @return If the property is a background color.
	 */
	public boolean isBackgroundColor() {
		if (getPropertyType() != StyleType.Background) {
			return false;
		}
		
		try {
			Color.valueOf(styleValue.value());
			return true;
		} catch (Exception e) { 
			return false;
		}
	}
	
	/**
	 * Prepares the property to be persisted.
	 */
	public void prepareXml() {
		super.clearConfigurationProperties();
		super.addConfigurationProperty(styleType);
		super.addConfigurationProperty(styleValue);
	}
	
	/**
	 * 
	 * @param obj The object to convert from persistence.
	 */
	public void convertFromPersistence(XmlConfigurationObject obj) {
		for (ConfigurationProperty prop : obj.configurationProperties()) {
			if (prop.name().equals(STYLE_TYPE)) {
				setPropertyType(prop.value().isEmpty() ? StyleType.None : StyleType.valueOf(prop.value()));
			} else if (prop.name().equals(STYLE_VALUE)) {
				setPropertyValue(prop.value());
			}
		}
	}
}
