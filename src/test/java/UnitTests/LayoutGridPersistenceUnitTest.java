package UnitTests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.persistence.LayoutGridPersistenceObject;
import textadventurelib.persistence.LayoutNodePersistenceObject;

public class LayoutGridPersistenceUnitTest {

	private final String layoutUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/layoutGridUnitTest.xml";
	
	private final String expectedCompile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<?import java.net.*?>\n<?import javafx.geometry.*?>\n<?import javafx.scene.control.*?>\n<?import javafx.scene.layout.*?>\n<?import javafx.scene.text.*?>\n<?import org.tbd.TextAdventureCreator.views.layout.*?>\n\n<?import textadventureLib.layout.fxviews.*?>\n\n<fx:root type=\"javafx.scene.layout.AnchorPane\" xmlns:fx=\"http://javafx.com/fxml\">\n\t<GridPane AnchorPane.topAnchor=\"0.0\"\n\t\tAnchorPane.leftAnchor=\"0.0\"\n\t\tAnchorPane.rightAnchor=\"0.0\"\n\t\tAnchorPane.bottomAnchor=\"0.0\">\n\t\t<columnConstraints>\n\t\t\t<ColumnConstraints hgrow=\"NEVER\" percentWidth=\"33\"/>\n\t\t\t<ColumnConstraints hgrow=\"NEVER\" percentWidth=\"33\"/>\n\t\t\t<ColumnConstraints hgrow=\"NEVER\" percentWidth=\"33\"/>\n\t\t</columnConstraints>\n\t\t<rowConstraints>\n\t\t\t<RowConstraints vgrow=\"NEVER\" percentHeight=\"33\"/>\n\t\t\t<RowConstraints vgrow=\"NEVER\" percentHeight=\"33\"/>\n\t\t\t<RowConstraints vgrow=\"NEVER\" percentHeight=\"33\"/>\n\t\t</rowConstraints>\n\t\t<Button fx:id=\"Node1\" GridPane.rowIndex=\"0\" GridPane.columnIndex=\"0\" />\n\t\t<TextField fx:id=\"Node2\" GridPane.rowIndex=\"1\" GridPane.columnIndex=\"0\" />\n\t\t<WebView fx:id=\"Node3\" GridPane.rowIndex=\"2\" GridPane.columnIndex=\"0\" />\n\t</GridPane>\n</fx:root>";
	private final String expectedFileData = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><LayoutGrid columns=\"3\" rows=\"3\"><LayoutNodes><LayoutNode column=\"0\" row=\"0\"><LayoutValue ValueType=\"string\">Button</LayoutValue><NodeID ValueType=\"string\">Node1</NodeID></LayoutNode><LayoutNode column=\"0\" row=\"1\"><LayoutValue ValueType=\"string\">TextField</LayoutValue><NodeID ValueType=\"string\">Node2</NodeID></LayoutNode><LayoutNode column=\"0\" row=\"2\"><LayoutValue ValueType=\"string\">WebView</LayoutValue><NodeID ValueType=\"string\">Node3</NodeID></LayoutNode></LayoutNodes></LayoutGrid>";
	
	@Test
	public void testCreate() {
		try {
			LayoutGridPersistenceObject layout = new LayoutGridPersistenceObject();
			assertNotNull(layout);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSetRows() {
		try {
			LayoutGridPersistenceObject layout = new LayoutGridPersistenceObject();
			assertEquals(1, layout.getRows());
			
			layout.setRows(12);
			assertEquals(12, layout.getRows());
			
			layout.setRows(100);
			assertEquals(100, layout.getRows());
			
			layout.setRows(5);
			assertEquals(5, layout.getRows());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testSetColumns() {
		try {
			LayoutGridPersistenceObject layout = new LayoutGridPersistenceObject();
			assertEquals(1, layout.getColumns());
			
			layout.setColumns(12);
			assertEquals(12, layout.getColumns());

			layout.setColumns(100);
			assertEquals(100, layout.getColumns());
			
			layout.setColumns(7);
			assertEquals(7, layout.getColumns());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testAddNode() {
		try {
			LayoutGridPersistenceObject layout = new LayoutGridPersistenceObject();
			LayoutNodePersistenceObject node1 = new LayoutNodePersistenceObject();
			node1.setColumn(0);
			node1.setRow(0);
			node1.setId("Node1");
			node1.setLayoutValue("Button");
			
			layout.addNode(node1);
			assertTrue(layout.compile().contains("Button"));
			
			LayoutNodePersistenceObject node2 = new LayoutNodePersistenceObject();
			node2.setColumn(0);
			node2.setRow(1);
			node2.setId("Node2");
			node2.setLayoutValue("TextField");
			
			layout.addNode(node2);
			assertTrue(layout.compile().contains("TextField"));
			
			LayoutNodePersistenceObject node3 = new LayoutNodePersistenceObject();
			node3.setColumn(0);
			node3.setRow(0);
			node3.setId("Node3");
			node3.setLayoutValue("WebView");
			
			layout.addNode(node3);
			assertTrue(layout.compile().contains("WebView"));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testRemoveNode() {
		try {
			LayoutGridPersistenceObject layout = new LayoutGridPersistenceObject();
			LayoutNodePersistenceObject node1 = new LayoutNodePersistenceObject();
			node1.setColumn(0);
			node1.setRow(0);
			node1.setId("Node1");
			node1.setLayoutValue("Button");
			
			layout.addNode(node1);
			assertTrue(layout.compile().contains("Button"));
			
			layout.removeNode(node1);
			assertTrue(!layout.compile().contains("Button"));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCompile() {
		try {
			LayoutGridPersistenceObject layout = new LayoutGridPersistenceObject();
			layout.setColumns(3);
			layout.setRows(3);
			LayoutNodePersistenceObject node1 = new LayoutNodePersistenceObject();
			node1.setColumn(0);
			node1.setRow(0);
			node1.setId("Node1");
			node1.setLayoutValue("Button");
			
			
			LayoutNodePersistenceObject node2 = new LayoutNodePersistenceObject();
			node2.setColumn(0);
			node2.setRow(1);
			node2.setId("Node2");
			node2.setLayoutValue("TextField");
			
			
			LayoutNodePersistenceObject node3 = new LayoutNodePersistenceObject();
			node3.setColumn(0);
			node3.setRow(2);
			node3.setId("Node3");
			node3.setLayoutValue("WebView");
			
			layout.addNode(node1);
			layout.addNode(node2);
			layout.addNode(node3);
			
			assertEquals(expectedCompile, layout.compile());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSave() {
		try {
			XmlConfigurationManager manager = new XmlConfigurationManager(layoutUnitTest);
			LayoutGridPersistenceObject layout = new LayoutGridPersistenceObject();
			layout.setColumns(3);
			layout.setRows(3);
			LayoutNodePersistenceObject node1 = new LayoutNodePersistenceObject();
			node1.setColumn(0);
			node1.setRow(0);
			node1.setId("Node1");
			node1.setLayoutValue("Button");
			
			
			LayoutNodePersistenceObject node2 = new LayoutNodePersistenceObject();
			node2.setColumn(0);
			node2.setRow(1);
			node2.setId("Node2");
			node2.setLayoutValue("TextField");
			
			
			LayoutNodePersistenceObject node3 = new LayoutNodePersistenceObject();
			node3.setColumn(0);
			node3.setRow(2);
			node3.setId("Node3");
			node3.setLayoutValue("WebView");
			
			layout.addNode(node1);
			layout.addNode(node2);
			layout.addNode(node3);
			
			layout.prepareXml();
			manager.addConfigurationObject(layout);
			manager.save();
			
			String fileContents = fileContents(layoutUnitTest);
			assertEquals(expectedFileData, fileContents);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testConvert() {
		try {
			XmlConfigurationManager manager = new XmlConfigurationManager(layoutUnitTest);
			LayoutGridPersistenceObject layout = new LayoutGridPersistenceObject();
			layout.setColumns(3);
			layout.setRows(3);
			LayoutNodePersistenceObject node1 = new LayoutNodePersistenceObject();
			node1.setColumn(0);
			node1.setRow(0);
			node1.setId("Node1");
			node1.setLayoutValue("Button");
			
			
			LayoutNodePersistenceObject node2 = new LayoutNodePersistenceObject();
			node2.setColumn(0);
			node2.setRow(1);
			node2.setId("Node2");
			node2.setLayoutValue("TextField");
			
			
			LayoutNodePersistenceObject node3 = new LayoutNodePersistenceObject();
			node3.setColumn(0);
			node3.setRow(2);
			node3.setId("Node3");
			node3.setLayoutValue("WebView");
			
			layout.addNode(node1);
			layout.addNode(node2);
			layout.addNode(node3);
			
			layout.prepareXml();
			manager.addConfigurationObject(layout);
			manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(layoutUnitTest);
			manager2.load();
			
			LayoutGridPersistenceObject persist = new LayoutGridPersistenceObject();
			persist.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals(3, persist.getRows());
			assertEquals(3, persist.getColumns());
			assertEquals(expectedCompile, persist.compile());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	private String fileContents(String fileLocation) throws IOException {
		File file = new File(fileLocation);
		return getFileContent(file);
	}
	
	private String getFileContent(File file) throws IOException {
		String output = new String();
		FileInputStream inputStream = new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line = new String();
		
		while((line = reader.readLine()) != null) {
			output += line;
		}
		
		reader.close();
		return output;
	}
}
