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
public class LayoutInfoPersistenceObject extends XmlConfigurationObject {

	private final static String LAYOUT_INFO = "LayoutInfo";
	private final static String LAYOUT_ID = "LayoutID";
	private final static String LAYOUT_TYPE = "LayoutType";
	private final static String LAYOUT_CONTENT = "LayoutContent";
	
	private XmlConfigurationObject id;
	private XmlConfigurationObject type;
	private XmlConfigurationObject content;
	
	/**
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public LayoutInfoPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(LAYOUT_INFO);
		
		id = new XmlConfigurationObject(LAYOUT_ID, new String());
		type = new XmlConfigurationObject(LAYOUT_TYPE, LayoutType.TextWithTextInput);
		content = new XmlConfigurationObject(LAYOUT_CONTENT, new String());
	}
	
	/**
	 * 
	 * @param id The new layout id.
	 */
	public void setLayoutId(String id) {
		this.id.value(id);
	}
	
	/**
	 * 
	 * @return The layout id.
	 */
	public String getLayoutId() {
		return id.value();
	}
	
	/**
	 * 
	 * @param type The new layout type.
	 */
	public void setLayoutType(LayoutType type) {
		this.type.value(type);
	}
	
	/**
	 * 
	 * @return The layout type.
	 */
	public LayoutType getLayoutType() {
		return type.value();
	}
	
	/**
	 * 
	 * @param content The new content for the layout.
	 */
	public void setLayoutContent(String content) {
		this.content.value(content);
	}
	
	/**
	 * 
	 * @return The layouts content.
	 */
	public String getLayoutContent() {
		return content.value();
	}
	
	public void prepareXml() {
		super.clearChildren();
		super.addChild(id);
		super.addChild(type);
		super.addChild(content);
	}
	
	public void convertFromPersistence(XmlConfigurationObject config) {
		for (PersistXml child : config.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			switch (cChild.name()) {
				case LAYOUT_CONTENT:
					setLayoutContent(cChild.<String>value());
					break;
				case LAYOUT_ID:
					setLayoutId(cChild.<String>value());
					break;
				case LAYOUT_TYPE:
					setLayoutType(convertToLayoutType(cChild.<String>value()));
					break;
			}
		}
	}
	
	private LayoutType convertToLayoutType(String type) {
		switch (type) {
			case "TextWithTextInput":
				return LayoutType.TextWithTextInput;
			case "ContentOnly":
				return LayoutType.ContentOnly;
			case "TextAndContentWithButtonInput":
				return LayoutType.TextAndContentWithButtonInput;
			case "TextWithButtonInput":
				return LayoutType.TextWithButtonInput;
			case "TextAndContentWithTextInput":
				return LayoutType.TextAndContentWithTextInput;
			case "Custom":
				return LayoutType.Custom;
			default:
				return LayoutType.TextWithTextInput;
		}
	}
}
