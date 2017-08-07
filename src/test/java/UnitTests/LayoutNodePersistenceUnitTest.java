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
import textadventurelib.persistence.LayoutNodePersistenceObject;

public class LayoutNodePersistenceUnitTest {

	private final String layoutUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/layoutNodeUnitTest.xml";
	
	private final String expectedCompile = "<Button fx:id=\"MyTestButton\" GridPane.rowIndex=\"1\" GridPane.columnIndex=\"1\" GridPane.rowSpan=\"12\" GridPane.columnSpan=\"12\" text=\"Hello World!\" />";
	private final String expectedFileData = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><LayoutNode column=\"1\" columnSpan=\"12\" row=\"1\" rowSpan=\"12\"><LayoutValue ValueType=\"string\">Button</LayoutValue><NodeID ValueType=\"string\">MyTestButton</NodeID><AssociatedProperties><text ValueType=\"string\">Hello World!</text></AssociatedProperties></LayoutNode>";
	
	@Test
	public void testCreate() {
		try {
			LayoutNodePersistenceObject node = new LayoutNodePersistenceObject();
			assertNotNull(node);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testSetRow() {
		try {
			LayoutNodePersistenceObject node = new LayoutNodePersistenceObject();

			assertEquals(0, node.getRow());
			
			node.setRow(12);
			assertEquals(12, node.getRow());
			
			node.setRow(2);
			assertEquals(2, node.getRow());
			
			node.setRow(1000);
			assertEquals(1000, node.getRow());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSetColumn() {
		try {
			LayoutNodePersistenceObject node = new LayoutNodePersistenceObject();

			assertEquals(0, node.getColumn());
			
			node.setColumn(12);
			assertEquals(12, node.getColumn());
			
			node.setColumn(2);
			assertEquals(2, node.getColumn());
			
			node.setColumn(1000);
			assertEquals(1000, node.getColumn());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSetRowSpan() {
		try {
			LayoutNodePersistenceObject node = new LayoutNodePersistenceObject();

			assertEquals(0, node.getRowSpan());
			
			node.setRowSpan(12);
			assertEquals(12, node.getRowSpan());
			
			node.setRowSpan(2);
			assertEquals(2, node.getRowSpan());
			
			node.setRowSpan(1000);
			assertEquals(1000, node.getRowSpan());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSetColumnSpan() {
		try {
			LayoutNodePersistenceObject node = new LayoutNodePersistenceObject();

			assertEquals(0, node.getColumnSpan());
			
			node.setColumnSpan(12);
			assertEquals(12, node.getColumnSpan());
			
			node.setColumnSpan(2);
			assertEquals(2, node.getColumnSpan());
			
			node.setColumnSpan(1000);
			assertEquals(1000, node.getColumnSpan());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSetLayoutValue() {
		try {
			LayoutNodePersistenceObject node = new LayoutNodePersistenceObject();

			node.setLayoutValue("Label");
			assertEquals("Label", node.getLayoutValue());
			
			node.setLayoutValue("Button");
			assertEquals("Button", node.getLayoutValue());
			
			node.setLayoutValue("HBox");
			assertEquals("HBox", node.getLayoutValue());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSetId() {
		try {
			LayoutNodePersistenceObject node = new LayoutNodePersistenceObject();
			
			assertNotNull(node.getId());
			assertTrue(!node.getId().isEmpty());
			
			node.setId("ID1");
			assertEquals("ID1", node.getId());
			
			node.setId("ID3");
			assertEquals("ID3", node.getId());
			
			node.setId("SOMETHING");
			assertEquals("SOMETHING", node.getId());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetProperty() {
		try {
			LayoutNodePersistenceObject node = new LayoutNodePersistenceObject();
			
			node.addProperty("text", "hello world");
			assertEquals("hello world", node.getPropertyValue("text"));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCompileWithoutSpans() {
		try {
			LayoutNodePersistenceObject node = new LayoutNodePersistenceObject();
			node.setRow(1);
			node.setColumn(1);
			
			String compiled = node.compile();
			assertTrue(!compiled.contains("rowSpan"));
			assertTrue(!compiled.contains("columnSpan"));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCompileWithSpans() {
		try {
			LayoutNodePersistenceObject node = new LayoutNodePersistenceObject();
			node.setRow(1);
			node.setRowSpan(12);
			node.setColumn(1);
			node.setColumnSpan(12);
			
			String compiled = node.compile();
			assertTrue(compiled.contains("rowSpan"));
			assertTrue(compiled.contains("columnSpan"));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCompileWithProperties() {
		try {
			LayoutNodePersistenceObject node = new LayoutNodePersistenceObject();
			node.addProperty("property1", "value1");
			
			String compiled = node.compile();
			assertTrue(compiled.contains("property1=\"value1\""));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testFullCompile() {
		try {
			LayoutNodePersistenceObject node = new LayoutNodePersistenceObject();
			node.setRow(1);
			node.setRowSpan(12);
			node.setColumn(1);
			node.setColumnSpan(12);
			node.setLayoutValue("Button");
			node.setId("MyTestButton");
			node.addProperty("text", "Hello World!");
			
			assertEquals(expectedCompile, node.compile());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testClone() {
		try {
			LayoutNodePersistenceObject node = new LayoutNodePersistenceObject();
			node.setRow(1);
			node.setRowSpan(12);
			node.setColumn(1);
			node.setColumnSpan(12);
			node.setLayoutValue("Button");
			node.setId("MyTestButton");
			node.addProperty("text", "Hello World!");
			
			LayoutNodePersistenceObject clone = node.clone();
			
			assertEquals(1, clone.getRow());
			assertEquals(12, clone.getRowSpan());
			assertEquals(1, clone.getColumn());
			assertEquals(12, clone.getColumnSpan());
			assertEquals("Button", clone.getLayoutValue());
			assertNotEquals("MyTestButton", clone.getId());
			assertEquals(clone.getPropertyValue("text"), "Hello World!");
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSave() {
		try {
			XmlConfigurationManager manager = new XmlConfigurationManager(layoutUnitTest);
			LayoutNodePersistenceObject node = new LayoutNodePersistenceObject();
			node.setRow(1);
			node.setRowSpan(12);
			node.setColumn(1);
			node.setColumnSpan(12);
			node.setLayoutValue("Button");
			node.setId("MyTestButton");
			node.addProperty("text", "Hello World!");
			
			node.prepareXml();
			manager.addConfigurationObject(node);
			manager.save();
			
			String fileContents = fileContents(layoutUnitTest);
			assertEquals(expectedFileData, fileContents);
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvert() {
		try {
			XmlConfigurationManager manager = new XmlConfigurationManager(layoutUnitTest);
			LayoutNodePersistenceObject node = new LayoutNodePersistenceObject();
			node.setRow(1);
			node.setRowSpan(12);
			node.setColumn(1);
			node.setColumnSpan(12);
			node.setLayoutValue("Button");
			node.setId("MyTestButton");
			node.addProperty("text", "Hello World!");
			
			node.prepareXml();
			manager.addConfigurationObject(node);
			manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(layoutUnitTest);
			manager2.load();
			
			LayoutNodePersistenceObject persist = new LayoutNodePersistenceObject();
			persist.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals(1, persist.getRow());
			assertEquals(1, persist.getColumn());
			assertEquals(12, persist.getRowSpan());
			assertEquals(12, persist.getColumnSpan());
			assertEquals("Button", persist.getLayoutValue());
			assertEquals("MyTestButton", persist.getId());
			assertEquals("Hello World!", persist.getPropertyValue("text"));
			assertEquals(expectedCompile, persist.compile());
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
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
