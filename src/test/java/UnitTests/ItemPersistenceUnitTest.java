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
import playerlib.items.IItem;
import playerlib.items.IProperty;
import playerlib.items.Item;
import playerlib.items.Property;
import textadventurelib.persistence.player.ItemPersistenceObject;
import textadventurelib.persistence.player.PropertyPersistenceObject;

public class ItemPersistenceUnitTest {

	private XmlConfigurationManager _manager;
	private final String _itemUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/itemUnitTest.xml";
	private final String _itemUnitTest2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/itemUnitTest2.xml";
	private final String _expectedItemText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Item><Name ValueType=\"string\">Potion</Name><Description ValueType=\"string\">Restores Health Points</Description><Content/><Properties><NamedObject type=\"Property\"><Name ValueType=\"string\">Consumable</Name><Description/><Value ValueType=\"bool\">true</Value></NamedObject><NamedObject type=\"Property\"><Name ValueType=\"string\">RestoreHP</Name><Description/><Value ValueType=\"int\">50</Value></NamedObject></Properties></Item>";
	
	@Test
	public void testCreate() {
		try {
			ItemPersistenceObject persist = new ItemPersistenceObject();
			
			assertNotNull(persist);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testItemCtor() {
		try {
			IItem item = new Item("Potion");
			item.description("Restores HP");
			
			IProperty consume = new Property("Consumable", true);
			IProperty value = new Property("value", 100);
			
			item.addProperty(consume);
			item.addProperty(value);
			
			ItemPersistenceObject persist = new ItemPersistenceObject(item);
			
			assertEquals("Potion", persist.itemName());
			assertEquals("Restores HP", persist.itemDescription());
			assertEquals(2, persist.properties().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testCreateFromItem() {
		try {
			IItem item = new Item("Potion");
			item.description("Restores HP");
			
			IProperty consume = new Property("Consumable", true);
			IProperty value = new Property("value", 100);
			
			item.addProperty(consume);
			item.addProperty(value);
			
			ItemPersistenceObject persist = new ItemPersistenceObject();
			persist.createFromItem(item);
			
			assertEquals("Potion", persist.itemName());
			assertEquals("Restores HP", persist.itemDescription());
			assertEquals(2, persist.properties().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetName() {
		try {
			ItemPersistenceObject persist = new ItemPersistenceObject();
			
			persist.itemName("Potion");
			assertEquals("Potion", persist.itemName());
			
			persist.itemName("Ether");
			assertEquals("Ether", persist.itemName());
			
			persist.itemName("Harp");
			assertEquals("Harp", persist.itemName());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetDescription() {
		try {
			ItemPersistenceObject persist = new ItemPersistenceObject();
			
			persist.itemDescription("Restores HP");
			assertEquals("Restores HP", persist.itemDescription());
			
			persist.itemDescription("Restores MP");
			assertEquals("Restores MP", persist.itemDescription());
			
			persist.itemDescription("A key item");
			assertEquals("A key item", persist.itemDescription());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetContent() {
		try {
			ItemPersistenceObject persist = new ItemPersistenceObject();
			
			persist.itemContent("Something");
			assertEquals("Something", persist.itemContent());
			
			persist.itemContent(true);
			assertEquals(true, persist.itemContent());
			
			persist.itemContent(1000);
			assertEquals(1000, persist.<Object>itemContent());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testAddProperty() {
		try {
			ItemPersistenceObject persist = new ItemPersistenceObject();
			PropertyPersistenceObject prop1 = new PropertyPersistenceObject();
			prop1.objectName("Breakable");
			prop1.value(true);
			PropertyPersistenceObject prop2 = new PropertyPersistenceObject();
			prop2.objectName("Consumable");
			prop2.value(true);
			PropertyPersistenceObject prop3 = new PropertyPersistenceObject();
			prop3.objectName("Enchanted");
			prop3.objectValue(false);

			assertEquals(0, persist.properties().size());
			
			persist.addProperty(prop1);
			assertEquals(1, persist.properties().size());
			assertEquals(prop1, persist.properties().get(0));
			
			persist.addProperty(prop2);
			assertEquals(2, persist.properties().size());
			assertEquals(prop2, persist.properties().get(1));
			
			persist.addProperty(prop3);
			assertEquals(3, persist.properties().size());
			assertEquals(prop3, persist.properties().get(2));
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testRemoveProperty() {
		try {
			ItemPersistenceObject persist = new ItemPersistenceObject();
			PropertyPersistenceObject prop1 = new PropertyPersistenceObject();
			prop1.objectName("Breakable");
			prop1.value(true);
			PropertyPersistenceObject prop2 = new PropertyPersistenceObject();
			prop2.objectName("Consumable");
			prop2.value(true);
			PropertyPersistenceObject prop3 = new PropertyPersistenceObject();
			prop3.objectName("Enchanted");
			prop3.objectValue(false);
			
			persist.addProperty(prop1);
			persist.addProperty(prop2);
			persist.addProperty(prop3);
			
			assertEquals(3, persist.properties().size());
			
			persist.removeProperty(prop1);
			assertEquals(2, persist.properties().size());
			persist.removeProperty(prop2);
			assertEquals(1, persist.properties().size());
			persist.removeProperty(prop3);
			assertEquals(0, persist.properties().size());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testClearProperties() {
		try {
			ItemPersistenceObject persist = new ItemPersistenceObject();
			
			PropertyPersistenceObject prop1 = new PropertyPersistenceObject();
			prop1.objectName("Breakable");
			prop1.objectValue(true);
			PropertyPersistenceObject prop2 = new PropertyPersistenceObject();
			prop2.objectName("Consumable");
			prop2.objectValue(true);
			PropertyPersistenceObject prop3 = new PropertyPersistenceObject();
			prop3.objectName("Enchanted");
			prop3.objectValue(false);
			
			persist.addProperty(prop1);
			persist.addProperty(prop2);
			persist.addProperty(prop3);
			
			assertEquals(3, persist.properties().size());
			
			persist.clearProperties();
			assertEquals(0, persist.properties().size());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testSave() {
		try {
			_manager = new XmlConfigurationManager(_itemUnitTest);
			ItemPersistenceObject persist = new ItemPersistenceObject();
			PropertyPersistenceObject prop = new PropertyPersistenceObject();
			prop.objectName("Consumable");
			prop.objectValue(true);
			PropertyPersistenceObject prop2 = new PropertyPersistenceObject();
			prop2.objectName("RestoreHP");
			prop2.objectValue(50);
			
			persist.itemName("Potion");
			persist.itemDescription("Restores Health Points");
			persist.addProperty(prop);
			persist.addProperty(prop2);

			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			String fileContents = fileContents(_itemUnitTest);
			assertEquals(_expectedItemText, fileContents);
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertToItem() {
		try {
			ItemPersistenceObject persist = new ItemPersistenceObject();
			PropertyPersistenceObject prop = new PropertyPersistenceObject();
			prop.objectName("Consumable");
			prop.objectValue(true);
			PropertyPersistenceObject prop2 = new PropertyPersistenceObject();
			prop2.objectName("RestoreHP");
			prop2.objectValue(50);
			
			persist.itemName("Potion");
			persist.itemDescription("Restores Health Points");
			persist.addProperty(prop);
			persist.addProperty(prop2);
			
			IItem item = persist.convertToItem();
			assertEquals("Potion", item.name());
			assertEquals("Restores Health Points", item.description());
			assertEquals(2, item.properties().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testPersistFromXml() {
		try {
			_manager = new XmlConfigurationManager(_itemUnitTest2);
			ItemPersistenceObject persist = new ItemPersistenceObject();
			PropertyPersistenceObject prop = new PropertyPersistenceObject();
			prop.objectName("Consumable");
			prop.objectValue(true);
			PropertyPersistenceObject prop2 = new PropertyPersistenceObject();
			prop2.objectName("RestoreHP");
			prop2.objectValue(50);
			
			persist.itemName("Potion");
			persist.itemDescription("Restores Health Points");
			persist.addProperty(prop);
			persist.addProperty(prop2);

			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			XmlConfigurationManager manager = new XmlConfigurationManager(_itemUnitTest2);
			manager.load();
			
			ItemPersistenceObject persist2 = new ItemPersistenceObject();
			persist2.convertFromPersistence((XmlConfigurationObject)manager.configurationObjects().get(0));
			assertEquals("Potion", persist2.itemName());
			assertEquals("Restores Health Points", persist2.itemDescription());
			assertEquals(2, persist2.properties().size());
		} catch (Exception e) {
			fail(e.toString());
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
