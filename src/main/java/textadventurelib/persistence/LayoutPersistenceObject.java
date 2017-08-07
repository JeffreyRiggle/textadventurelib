package textadventurelib.persistence;

import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;

//TODO: This will be reworked/expanded at some point.
/**
 * 
 * @author Jeff Riggle
 *
 */
public class LayoutPersistenceObject extends XmlConfigurationObject{

	private static final String LAYOUT_NAME = "Layout";
	private static final String LAYOUT_TYPE = "LayoutType";
	private static final String CONTENT_NAME = "Content";
	private static final String LAYOUT_ID = "ID";
	
	private XmlConfigurationObject layoutType;
	private XmlConfigurationObject content;
	private LayoutGridPersistenceObject layout;
	private StylePersistenceObject style;
	private XmlConfigurationObject id;
	
	/**
	 * Ctor.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public LayoutPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(LAYOUT_NAME);
		
		layoutType = new XmlConfigurationObject(LAYOUT_TYPE);
		content = new XmlConfigurationObject(CONTENT_NAME);
		layout = new LayoutGridPersistenceObject();
		style = new StylePersistenceObject();
		id = new XmlConfigurationObject(LAYOUT_ID, UUID.randomUUID().toString());
	}
	
	/**
	 * 
	 * @param type The @see LayoutType to associate with this layout object.
	 */
	public void layoutType(LayoutType type) {
		layoutType.value(type);
	}
	
	/**
	 * 
	 * @return The @see LayoutType assoicated with this layout object.
	 */
	public LayoutType layoutType() {
		return layoutType.value();
	}
	
	/**
	 * 
	 * @param location The new location of the content to display.
	 */
	public void content(String location) {
		content.value(location);
	}
	
	/**
	 * 
	 * @return The location of the content to display.
	 */
	public String content() {
		return content.value();
	}
	
	public boolean hasContentArea() {
		LayoutType type = layoutType.value();
		if (type == LayoutType.ContentOnly || 
		    type == LayoutType.TextAndContentWithButtonInput || type == LayoutType.TextAndContentWithTextInput) {
			return true;
		}
		
		if (type != LayoutType.Custom) {
			return false;
		}
		
		if (layout.compile().contains("MultiTypeContentView")) {
			return true;
		}
		
		return false;
	}
	
	public LayoutGridPersistenceObject getLayout() {
		return layout;
	}
	
	public void setLayout(LayoutGridPersistenceObject layout) {
		this.layout = layout;
	}
	
	public StylePersistenceObject getStyle() {
		return style;
	}
	
	public void setStyle(StylePersistenceObject style) {
		this.style = style;
	}
	
	public String id() {
		return id.value();
	}
	
	public void id(String value) {
		id.value(value);
	}
	
	/**
	 * Prepares the xml to be saved. This must be called before saving the xml!
	 */
	public void prepareXml() {
		super.clearChildren();
		this.addChild(layoutType);
		this.addChild(content);
		
		layout.prepareXml();
		this.addChild(layout);
		
		style.prepareXml();
		this.addChild(style);
		
		this.addChild(id);
	}
	
	//TODO: Document and unit test.
	public void convertFromPersistence(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			switch (cChild.name()) {
				case LAYOUT_TYPE:
					if (cChild.value() instanceof LayoutType) {
						layoutType(cChild.value());
					} else {
						layoutType(convertToLayoutType(cChild.<String>value()));
					}
					break;
				case CONTENT_NAME:
					content(cChild.<String>value());
					break;
				case "LayoutGrid":
					convertLayout(cChild);
					break;
				case "Style":
					convertStyle(cChild);
					break;
				case LAYOUT_ID:
					id(cChild.<String>value());
				default:
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
	
	private void convertLayout(XmlConfigurationObject obj) {
		try {
			LayoutGridPersistenceObject layout = new LayoutGridPersistenceObject();
			layout.convertFromPersistence(obj);
			setLayout(layout);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void convertStyle(XmlConfigurationObject obj) {
		try {
			StylePersistenceObject style = new StylePersistenceObject();
			style.convertFromPersistence(obj);
			setStyle(style);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
