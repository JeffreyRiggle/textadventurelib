package textadventurelib.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.ConfigurationProperty;
import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;

public class LayoutGridPersistenceObject extends XmlConfigurationObject {

	private static final String LAYOUT_GRID = "LayoutGrid";
	private static final String LAYOUT_NODES = "LayoutNodes";
	private static final String ROWS = "rows";
	private static final String COLUMNS = "columns";
	
	private ConfigurationProperty rows;
	private ConfigurationProperty columns;
	private List<LayoutNodePersistenceObject> nodes;
	
	public LayoutGridPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(LAYOUT_GRID);
		
		rows = new ConfigurationProperty(ROWS, "1");
		columns = new ConfigurationProperty(COLUMNS, "1");
		nodes = new ArrayList<LayoutNodePersistenceObject>();
	}
	
	public int getRows() {
		return Integer.parseInt(rows.value());
	}
	
	public void setRows(int rows) {
		this.rows.value(Integer.toString(rows));
	}
	
	public int getColumns() {
		return Integer.parseInt(columns.value());
	}
	
	public void setColumns(int columns) {
		this.columns.value(Integer.toString(columns));
	}
	
	public void addNode(LayoutNodePersistenceObject node) {
		nodes.add(node);
	}
	
	public void removeNode(LayoutNodePersistenceObject node) {
		nodes.remove(node);
	}
	
	public List<LayoutNodePersistenceObject> getNodes() {
		return new ArrayList<LayoutNodePersistenceObject>(nodes);
	}
	
	public String compile() {
		StringBuilder builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		builder.append("<?import java.net.*?>\n");
		builder.append("<?import javafx.geometry.*?>\n");
		builder.append("<?import javafx.scene.control.*?>\n");
		builder.append("<?import javafx.scene.layout.*?>\n");
		builder.append("<?import javafx.scene.text.*?>\n");
		builder.append("<?import org.tbd.TextAdventureCreator.views.layout.*?>\n\n");
		builder.append("<?import textadventureLib.layout.fxviews.*?>\n\n");
		builder.append("<fx:root type=\"javafx.scene.layout.AnchorPane\" xmlns:fx=\"http://javafx.com/fxml\">\n");
		builder.append("\t<GridPane AnchorPane.topAnchor=\"0.0\"\n");
		builder.append("\t\tAnchorPane.leftAnchor=\"0.0\"\n");
		builder.append("\t\tAnchorPane.rightAnchor=\"0.0\"\n");
		builder.append("\t\tAnchorPane.bottomAnchor=\"0.0\">\n");
		
		builder.append("\t\t<columnConstraints>\n");
		for (int i = 0; i < getColumns(); i++) {
			builder.append(String.format("\t\t\t<ColumnConstraints hgrow=\"NEVER\" percentWidth=\"%s\"/>\n", 100 / getColumns()));
		}
		builder.append("\t\t</columnConstraints>\n");
		
		builder.append("\t\t<rowConstraints>\n");
		for (int i = 0; i < getRows(); i++) {
			builder.append(String.format("\t\t\t<RowConstraints vgrow=\"NEVER\" percentHeight=\"%s\"/>\n", 100 / getRows()));
		}
		builder.append("\t\t</rowConstraints>\n");
		
		for (LayoutNodePersistenceObject component : nodes) {
			builder.append("\t\t");
			builder.append(component.compile());
			builder.append("\n");
		}
		builder.append("\t</GridPane>\n");
		builder.append("</fx:root>");
		
		return builder.toString();
	}
	
	public void prepareXml() {
		super.clearChildren();
		this.addConfigurationProperty(rows);
		this.addConfigurationProperty(columns);
		
		XmlConfigurationObject nodes = buildNodes();
		if (nodes != null) {
			this.addChild(nodes);
		}
	}
	
	private XmlConfigurationObject buildNodes() {
		XmlConfigurationObject retVal = null;
		if (nodes.size() <= 0) {
			return retVal;
		}
		
		try {
			retVal = new XmlConfigurationObject(LAYOUT_NODES);
			for (LayoutNodePersistenceObject node : nodes) {
				node.prepareXml();
				retVal.addChild(node);
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
				case LAYOUT_NODES:
					convertNodes(cChild);
					break;
				default:
					break;
			}
		}
	}
	
	private void convertProperties(XmlConfigurationObject obj) {
		for (ConfigurationProperty prop : obj.configurationProperties()) {
			if (prop.name().equalsIgnoreCase(ROWS)) {
				setRows(Integer.parseInt(prop.value()));
			}
			
			if (prop.name().equalsIgnoreCase(COLUMNS)) {
				setColumns(Integer.parseInt(prop.value()));
			}
		}
	}
	
	private void convertNodes(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			try {
				LayoutNodePersistenceObject node = new LayoutNodePersistenceObject();
				node.convertFromPersistence(cChild);
				addNode(node);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
