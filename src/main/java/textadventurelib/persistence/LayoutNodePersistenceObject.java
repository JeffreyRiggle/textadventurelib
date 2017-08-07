package textadventurelib.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

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
public class LayoutNodePersistenceObject extends XmlConfigurationObject {

	private static final String LAYOUT_NODE_NAME = "LayoutNode";
	private static final String LAYOUT_VALUE = "LayoutValue";
	private static final String NODE_ID = "NodeID";
	private static final String ROW = "row";
	private static final String COLUMN = "column";
	private static final String ROW_SPAN = "rowSpan";
	private static final String COLUMN_SPAN = "columnSpan";
	private static final String ASSOCIATED_PROPERTIES = "AssociatedProperties";
	
	private ConfigurationProperty row;
	private ConfigurationProperty column;
	private ConfigurationProperty rowSpan;
	private ConfigurationProperty columnSpan;
	private XmlConfigurationObject value;
	private XmlConfigurationObject id;
	private Map<String, String> properties;
	
	/**
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public LayoutNodePersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(LAYOUT_NODE_NAME);
		
		properties = new HashMap<String, String>();
		value = new XmlConfigurationObject(LAYOUT_VALUE);
		id = new XmlConfigurationObject(NODE_ID, UUID.randomUUID().toString().replaceAll("-", ""));
		row = new ConfigurationProperty(ROW, "0");
		column = new ConfigurationProperty(COLUMN, "0");
		rowSpan = new ConfigurationProperty(ROW_SPAN, "0");
		columnSpan = new ConfigurationProperty(COLUMN_SPAN, "0");
	}
	
	/**
	 * 
	 * @return The row for this layout node.
	 */
	public int getRow() {
		return Integer.parseInt(row.value());
	}
	
	/**
	 * 
	 * @param row The new row for this layout node.
	 */
	public void setRow(int row) {
		this.row.value(Integer.toString(row));
	}
	
	/**
	 * 
	 * @return The column for this layout node.
	 */
	public int getColumn() {
		return Integer.parseInt(column.value());
	}
	
	/**
	 * 
	 * @param column The new column for this layout node.
	 */
	public void setColumn(int column) {
		this.column.value(Integer.toString(column));
	}
	
	/**
	 * 
	 * @return The number of rows this node spans.
	 */
	public int getRowSpan() {
		return Integer.parseInt(rowSpan.value());
	}
	
	/**
	 * 
	 * @param rowSpan The new number of rows for this node to span.
	 */
	public void setRowSpan(int rowSpan) {
		this.rowSpan.value(Integer.toString(rowSpan));
	}
	
	/**
	 * 
	 * @return The number of columns this node spans.
	 */
	public int getColumnSpan() {
		return Integer.parseInt(columnSpan.value());
	}
	
	/**
	 * 
	 * @param columnSpan The new number of columns this node spans.
	 */
	public void setColumnSpan(int columnSpan) {
		this.columnSpan.value(Integer.toString(columnSpan));
	}
	
	/**
	 * 
	 * @return The value for this node.
	 */
	public String getLayoutValue() {
		return value.value();
	}
	
	/**
	 * 
	 * @param value The new value for this node.
	 */
	public void setLayoutValue(String value) {
		this.value.value(value);
	}
	
	/**
	 * 
	 * @return The id for this node.
	 */
	public String getId() {
		return id.value();
	}
	
	/**
	 * 
	 * @param id The new id for this node.
	 */
	public void setId(String id) {
		this.id.value(id);
	}
	
	/**
	 * 
	 * @param id The id of the property to add to this node.
	 * @param value The value of the property.
	 */
	public void addProperty(String id, String value) {
		properties.put(id, value);
	}
	
	/**
	 * 
	 * @param id The id to lookup.
	 * @return The value associated with the id.
	 */
	public String getPropertyValue(String id) {
		return properties.get(id);
	}
	
	/**
	 * 
	 * @param id The id of the property to remove.
	 */
	public void removeProperty(String id) {
		properties.remove(id);
	}
	
	/**
	 * 
	 * @return The properties associated with this node.
	 */
	public Map<String, String> getProperties() {
		return new HashMap<String, String>(properties);
	}
	
	/**
	 * 
	 * @return javafx fxml string.
	 */
	public String compile() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("<");
		builder.append(value.<String>value());
		builder.append(String.format(" fx:id=\"%s\"", id.<String>value()));
		builder.append(String.format(" GridPane.rowIndex=\"%s\"", row.value()));
		builder.append(String.format(" GridPane.columnIndex=\"%s\"", column.value()));
		
		if (getRowSpan() > 0) {
			builder.append(String.format(" GridPane.rowSpan=\"%s\"", rowSpan.value()));
		}
		
		if (getColumnSpan() > 0) {
			builder.append(String.format(" GridPane.columnSpan=\"%s\"", columnSpan.value()));
		}

		for (Entry<String, String> prop : properties.entrySet()) {
			builder.append(String.format(" %s=\"%s\"", prop.getKey(), prop.getValue()));
		}
		
		builder.append(" />");
		
		return builder.toString();
	}
	
	public void prepareXml() {
		super.clearChildren();
		this.addConfigurationProperty(row);
		this.addConfigurationProperty(column);
		
		if (getRowSpan() > 0) {
			this.addConfigurationProperty(rowSpan);
		}
		
		if (getColumnSpan() > 0) {
			this.addConfigurationProperty(columnSpan);
		}
		
		this.addChild(value);
		this.addChild(id);
		
		XmlConfigurationObject props = buildProperties();
		if (props != null) {
			this.addChild(props);
		}
	}
	
	public LayoutNodePersistenceObject clone() {
		LayoutNodePersistenceObject retVal = null;
		
		try {
			retVal = new LayoutNodePersistenceObject();
			retVal.setColumn(getColumn());
			retVal.setRow(getRow());
			retVal.setColumnSpan(getColumnSpan());
			retVal.setRowSpan(getRowSpan());
			retVal.setLayoutValue(getLayoutValue());

			for (Entry<String, String> prop : properties.entrySet()) {
				retVal.addProperty(prop.getKey(), prop.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retVal;
	}
	
	private XmlConfigurationObject buildProperties() {
		XmlConfigurationObject retVal = null;
		if (properties.size() <= 0) {
			return retVal;
		}
		
		try {
			retVal = new XmlConfigurationObject(ASSOCIATED_PROPERTIES);
			for (Entry<String, String> prop : properties.entrySet()) {
				retVal.addChild(new XmlConfigurationObject(prop.getKey(), prop.getValue()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retVal;
	}
	
	public void convertFromPersistence(XmlConfigurationObject obj) {
		convertProperties(obj);
		
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			switch (cChild.name()) {
				case NODE_ID:
					setId(cChild.<String>value());
					break;
				case LAYOUT_VALUE:
					setLayoutValue(cChild.<String>value());
					break;
				case ASSOCIATED_PROPERTIES:
					convertAssociatedProperties(cChild);
				default:
					break;
			}
		}
	}
	
	private void convertProperties(XmlConfigurationObject obj) {
		for (ConfigurationProperty prop : obj.configurationProperties()) {
			if (prop.name().equalsIgnoreCase(ROW)) {
				setRow(Integer.parseInt(prop.value()));
			}
			
			if (prop.name().equalsIgnoreCase(COLUMN)) {
				setColumn(Integer.parseInt(prop.value()));
			}
			
			if (prop.name().equalsIgnoreCase(ROW_SPAN)) {
				setRowSpan(Integer.parseInt(prop.value()));
			}
			
			if (prop.name().equalsIgnoreCase(COLUMN_SPAN)) {
				setColumnSpan(Integer.parseInt(prop.value()));
			}
		}
	}
	
	private void convertAssociatedProperties(XmlConfigurationObject obj) {
		properties.clear();
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			properties.put(cChild.name(), cChild.<String>value());
		}
	}
}
