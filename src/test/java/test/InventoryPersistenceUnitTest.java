import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import playerlib.inventory.IInventory;
import playerlib.inventory.Inventory;
import playerlib.items.IItem;
import playerlib.items.Item;
import textadventurelib.persistence.player.InventoryPersistenceObject;
import textadventurelib.persistence.player.ItemPersistenceObject;
import textadventurelib.persistence.player.PropertyPersistenceObject;

public class InventoryPersistenceUnitTest {

	private XmlConfigurationManager _manager;
	private final String _inventoryUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/intentoryUnitTest.xml";
	private final String _inventoryUnitTest2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/intentoryUnitTest2.xml";
	private final String _expectedInventoryText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Inventory><Item Amount=\"50\"><Name ValueType=\"string\">Potion</Name><Description ValueType=\"string\">Restores Health Points</Description><Content/><Properties><NamedObject type=\"Property\"><Name ValueType=\"string\">Consumable</Name><Description/><Value ValueType=\"bool\">true</Value></NamedObject><NamedObject type=\"Property\"><Name ValueType=\"string\">RestoreHP</Name><Description/><Value ValueType=\"int\">50</Value></NamedObject></Properties></Item><Item Amount=\"4\"><Name ValueType=\"string\">Ether</Name><Description ValueType=\"string\">Restores MP</Description><Content/><Properties><NamedObject type=\"Property\"><Name ValueType=\"string\">Consumable</Name><Description/><Value ValueType=\"bool\">true</Value></NamedObject></Properties></Item></Inventory>";

	@Test
	public void testCreate() {
		try {
			InventoryPersistenceObject persist = new InventoryPersistenceObject();
			
			assertNotNull(persist);
		} catch(Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testInventoryCtor() {
		try {
			IInventory inventory = new Inventory();
			
			IItem potion = new Item("Potion");
			IItem ether = new Item("Ether");
			
			inventory.addItem(potion, 10);
			inventory.addItem(ether, 5);
			
			InventoryPersistenceObject persist = new InventoryPersistenceObject(inventory);
			
			assertEquals(2, persist.items().size());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testCreateFromInventory() {
		try {
			IInventory inventory = new Inventory();
			
			IItem potion = new Item("Potion");
			IItem ether = new Item("Ether");
			
			inventory.addItem(potion, 10);
			inventory.addItem(ether, 5);
			
			InventoryPersistenceObject persist = new InventoryPersistenceObject();
			persist.createFromInventory(inventory);
			
			assertEquals(2, persist.items().size());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testAdd() {
		try {
			InventoryPersistenceObject persist = new InventoryPersistenceObject();
			
			ItemPersistenceObject item1 = new ItemPersistenceObject();
			item1.itemName("Potion");
			item1.itemDescription("Restores HP");
			
			ItemPersistenceObject item2 = new ItemPersistenceObject();
			item2.itemName("Ether");
			item2.itemDescription("Restores MP");
			
			ItemPersistenceObject item3 = new ItemPersistenceObject();
			item1.itemName("Phenoix Down");
			item1.itemDescription("Revives");
			
			persist.addItem(item1, 13);
			assertEquals(1, persist.items().size());
			assertEquals(item1, persist.items().get(0));
			assertEquals("13", persist.items().get(0).configurationProperties().get(0).value());
			
			persist.addItem(item2, 15);
			assertEquals(2, persist.items().size());
			assertEquals(item2, persist.items().get(1));
			assertEquals("15", persist.items().get(1).configurationProperties().get(0).value());
			
			persist.addItem(item3, 2);
			assertEquals(3, persist.items().size());
			assertEquals(item3, persist.items().get(2));
			assertEquals("2", persist.items().get(2).configurationProperties().get(0).value());
			
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testRemove() {
		try {
			InventoryPersistenceObject persist = new InventoryPersistenceObject();
			
			ItemPersistenceObject item1 = new ItemPersistenceObject();
			item1.itemName("Potion");
			item1.itemDescription("Restores HP");
			
			ItemPersistenceObject item2 = new ItemPersistenceObject();
			item2.itemName("Ether");
			item2.itemDescription("Restores MP");
			
			ItemPersistenceObject item3 = new ItemPersistenceObject();
			item1.itemName("Phenoix Down");
			item1.itemDescription("Revives");
			
			persist.addItem(item1, 13);
			persist.addItem(item2, 12);
			persist.addItem(item3, 3);
			assertEquals(3, persist.items().size());
			
			persist.removeItem(item1);
			assertEquals(2, persist.items().size());
			
			persist.removeItem(item2);
			assertEquals(1, persist.items().size());
			
			persist.removeItem(item3);
			assertEquals(0, persist.items().size());
			
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testClear() {
		try {
			InventoryPersistenceObject persist = new InventoryPersistenceObject();
			
			ItemPersistenceObject item1 = new ItemPersistenceObject();
			item1.itemName("Potion");
			item1.itemDescription("Restores HP");
			
			ItemPersistenceObject item2 = new ItemPersistenceObject();
			item2.itemName("Ether");
			item2.itemDescription("Restores MP");
			
			ItemPersistenceObject item3 = new ItemPersistenceObject();
			item1.itemName("Phenoix Down");
			item1.itemDescription("Revives");
			
			persist.addItem(item1, 13);
			persist.addItem(item2, 12);
			persist.addItem(item3, 3);
			assertEquals(3, persist.items().size());
			
			persist.clearItems();
			assertEquals(0, persist.items().size());
			
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testChangeAmount() {
		try {
			InventoryPersistenceObject persist = new InventoryPersistenceObject();
			
			ItemPersistenceObject item = new ItemPersistenceObject();
			item.itemName("Potion");
			item.itemDescription("Restores HP");
			
			persist.addItem(item, 15);
			assertEquals("15", persist.items().get(0).configurationProperties().get(0).value());
			
			persist.changeItemAmount(item, 5);
			assertEquals("5", persist.items().get(0).configurationProperties().get(0).value());
			
			persist.changeItemAmount(item, 9);
			assertEquals("9", persist.items().get(0).configurationProperties().get(0).value());
			
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			_manager = new XmlConfigurationManager(_inventoryUnitTest);
			
			InventoryPersistenceObject persist = new InventoryPersistenceObject();
			
			ItemPersistenceObject item = new ItemPersistenceObject();
			PropertyPersistenceObject prop = new PropertyPersistenceObject();
			prop.objectName("Consumable");
			prop.objectValue(true);
			PropertyPersistenceObject prop2 = new PropertyPersistenceObject();
			prop2.objectName("RestoreHP");
			prop2.objectValue(50);
			
			item.itemName("Potion");
			item.itemDescription("Restores Health Points");
			item.addProperty(prop);
			item.addProperty(prop2);

			ItemPersistenceObject item2 = new ItemPersistenceObject();
			item2.itemName("Ether");
			item2.itemDescription("Restores MP");
			item2.addProperty(prop);
			
			persist.addItem(item, 50);
			persist.addItem(item2, 4);
			persist.prepareXml();
			
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			String fileContents = fileContents(_inventoryUnitTest);
			assertEquals(_expectedInventoryText, fileContents);
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertToInventory() {
		try {
			InventoryPersistenceObject persist = new InventoryPersistenceObject();
			
			ItemPersistenceObject item = new ItemPersistenceObject();
			PropertyPersistenceObject prop = new PropertyPersistenceObject();
			prop.objectName("Consumable");
			prop.objectValue(true);
			PropertyPersistenceObject prop2 = new PropertyPersistenceObject();
			prop2.objectName("RestoreHP");
			prop2.objectValue(50);
			
			item.itemName("Potion");
			item.itemDescription("Restores Health Points");
			item.addProperty(prop);
			item.addProperty(prop2);

			ItemPersistenceObject item2 = new ItemPersistenceObject();
			item2.itemName("Ether");
			item2.itemDescription("Restores MP");
			item2.addProperty(prop);
			
			persist.addItem(item, 50);
			persist.addItem(item2, 4);
			
			IInventory inv = persist.convertToInventory();
			assertEquals(2, inv.items().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertFromPersistence() {
		try {
			_manager = new XmlConfigurationManager(_inventoryUnitTest2);
			
			InventoryPersistenceObject persist = new InventoryPersistenceObject();
			
			ItemPersistenceObject item = new ItemPersistenceObject();
			PropertyPersistenceObject prop = new PropertyPersistenceObject();
			prop.objectName("Consumable");
			prop.objectValue(true);
			PropertyPersistenceObject prop2 = new PropertyPersistenceObject();
			prop2.objectName("RestoreHP");
			prop2.objectValue(50);
			
			item.itemName("Potion");
			item.itemDescription("Restores Health Points");
			item.addProperty(prop);
			item.addProperty(prop2);

			ItemPersistenceObject item2 = new ItemPersistenceObject();
			item2.itemName("Ether");
			item2.itemDescription("Restores MP");
			item2.addProperty(prop);
			
			persist.addItem(item, 50);
			persist.addItem(item2, 4);
			persist.prepareXml();
			
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(_inventoryUnitTest2);
			manager2.load();
			
			InventoryPersistenceObject inv = new InventoryPersistenceObject();
			inv.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals(2, inv.items().size());
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
