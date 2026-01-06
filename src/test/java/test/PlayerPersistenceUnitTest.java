import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import playerlib.attributes.Attribute;
import playerlib.attributes.IAttribute;
import playerlib.equipment.BodyPart;
import playerlib.equipment.Equipment;
import playerlib.equipment.IBodyPart;
import playerlib.inventory.Inventory;
import playerlib.items.IItem;
import playerlib.items.IProperty;
import playerlib.items.Item;
import playerlib.items.Property;
import playerlib.player.IPlayer;
import playerlib.player.Player;
import textadventurelib.persistence.player.AttributePersistenceObject;
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;
import textadventurelib.persistence.player.EquipmentPersistenceObject;
import textadventurelib.persistence.player.InventoryPersistenceObject;
import textadventurelib.persistence.player.ItemPersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

public class PlayerPersistenceUnitTest {

	private XmlConfigurationManager _manager;
	
	private final String _playerUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/playerUnitTest.xml";
	private final String _playerUnitTest2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/playerUnitTest2.xml";
	private final String _expectedPlayerText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Player><Name ValueType=\"string\">Deemo</Name><Attributes><NamedObject type=\"Attribute\"><Name ValueType=\"string\">Age</Name><Description/><Value ValueType=\"int\">15</Value></NamedObject><NamedObject type=\"Attribute\"><Name ValueType=\"string\">Class</Name><Description/><Value ValueType=\"string\">Wizard</Value></NamedObject><NamedObject type=\"Attribute\"><Name ValueType=\"string\">Max Health</Name><Description/><Value ValueType=\"int\">5000</Value></NamedObject></Attributes><Characteristics><NamedObject type=\"Characteristic\"><Name ValueType=\"string\">Hair Color</Name><Description/><Value ValueType=\"string\">Brown</Value></NamedObject><NamedObject type=\"Characteristic\"><Name ValueType=\"string\">Scar</Name><Description/><Value ValueType=\"string\">Long</Value></NamedObject><NamedObject type=\"Characteristic\"><Name ValueType=\"string\">Skin Color</Name><Description/><Value ValueType=\"string\">White</Value></NamedObject></Characteristics><BodyParts><BodyPart><Name ValueType=\"string\">Head</Name><Description/><Characteristics/></BodyPart><BodyPart><Name ValueType=\"string\">Feet</Name><Description/><Characteristics/></BodyPart><BodyPart><Name ValueType=\"string\">Tourso</Name><Description/><Characteristics/></BodyPart></BodyParts><Inventory><Item Amount=\"5\"><Name ValueType=\"string\">Potion</Name><Description ValueType=\"string\">Restores HP</Description><Content/><Properties/></Item><Item Amount=\"2\"><Name ValueType=\"string\">Ether</Name><Description ValueType=\"string\">Restores MP</Description><Content/><Properties/></Item><Item Amount=\"10\"><Name ValueType=\"string\">Phenoix Down</Name><Description ValueType=\"string\">Revives</Description><Content/><Properties/></Item></Inventory><Equipment><Equiptable><Item><Name ValueType=\"string\">Helmet</Name><Description ValueType=\"string\">Protects the head</Description><Content/><Properties/></Item><BodyPart><Name ValueType=\"string\">Head</Name><Description/><Characteristics/></BodyPart></Equiptable><Equiptable><Item><Name ValueType=\"string\">Steel Boots</Name><Description ValueType=\"string\">Protects the feet</Description><Content/><Properties/></Item><BodyPart><Name ValueType=\"string\">Feet</Name><Description/><Characteristics/></BodyPart></Equiptable><Equiptable><Item><Name ValueType=\"string\">Leather Vest</Name><Description ValueType=\"string\">Protects the body</Description><Content/><Properties/></Item><BodyPart><Name ValueType=\"string\">Tourso</Name><Description/><Characteristics/></BodyPart></Equiptable></Equipment></Player>";

	@Test
	public void testCreate() {
		try {
			PlayerPersistenceObject persist = new PlayerPersistenceObject();
			
			assertNotNull(persist);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testPlayerCtor() {
		try {
			IPlayer player = new Player("John");
			IAttribute clas = new Attribute("Class", "Wizard");
			player.addAttribute(clas);
			IBodyPart arm = new BodyPart("Arm");
			IBodyPart head = new BodyPart("Head");
			player.addBodyPart(arm);
			player.addBodyPart(head);
			IProperty consumable = new Property("Consumable", true);
			IProperty breakable = new Property("Breakable", true);
			IItem bracer = new Item("Bracer");
			bracer.addProperty(breakable);
			IItem hood = new Item("Hood");
			hood.addProperty(breakable);
			IItem potion = new Item("Potion");
			potion.addProperty(consumable);
			IItem ether = new Item("Ether");
			ether.addProperty(consumable);
			player.equipment(new Equipment());
			player.equipment().equip(head, hood);
			player.equipment().equip(arm, bracer);
			player.inventory(new Inventory());
			player.inventory().addItem(potion, 10);
			player.inventory().addItem(ether, 5);
			
			PlayerPersistenceObject persist = new PlayerPersistenceObject(player);
			
			assertEquals("John", persist.playerName());
			assertEquals(1, persist.attributes().size());
			assertEquals(2, persist.bodyParts().size());
			assertEquals(2, persist.equipment().equipment().keySet().size());
			assertEquals(2, persist.inventory().items().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testCreateFromPlayer() {
		try {
			IPlayer player = new Player("John");
			IAttribute clas = new Attribute("Class", "Wizard");
			player.addAttribute(clas);
			IBodyPart arm = new BodyPart("Arm");
			IBodyPart head = new BodyPart("Head");
			player.addBodyPart(arm);
			player.addBodyPart(head);
			IProperty consumable = new Property("Consumable", true);
			IProperty breakable = new Property("Breakable", true);
			IItem bracer = new Item("Bracer");
			bracer.addProperty(breakable);
			IItem hood = new Item("Hood");
			hood.addProperty(breakable);
			IItem potion = new Item("Potion");
			potion.addProperty(consumable);
			IItem ether = new Item("Ether");
			ether.addProperty(consumable);
			player.equipment(new Equipment());
			player.equipment().equip(head, hood);
			player.equipment().equip(arm, bracer);
			player.inventory(new Inventory());
			player.inventory().addItem(potion, 10);
			player.inventory().addItem(ether, 5);
			
			PlayerPersistenceObject persist = new PlayerPersistenceObject();
			persist.createFromPlayer(player);
			
			assertEquals("John", persist.playerName());
			assertEquals(1, persist.attributes().size());
			assertEquals(2, persist.bodyParts().size());
			assertEquals(2, persist.equipment().equipment().keySet().size());
			assertEquals(2, persist.inventory().items().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testAddAttribute() {
		try {
			PlayerPersistenceObject persist = new PlayerPersistenceObject();
			
			AttributePersistenceObject att1 = new AttributePersistenceObject();
			att1.objectName("Age");
			att1.objectValue(15);
			AttributePersistenceObject att2 = new AttributePersistenceObject();
			att2.objectName("Class");
			att2.objectValue("Wizard");
			AttributePersistenceObject att3 = new AttributePersistenceObject();
			att3.objectName("Max Health");
			att3.objectValue(5000);
			
			assertEquals(0, persist.attributes().size());
			
			persist.addAttribute(att1);
			assertEquals(1, persist.attributes().size());
			
			persist.addAttribute(att2);
			assertEquals(2, persist.attributes().size());
			
			persist.addAttribute(att3);
			assertEquals(3, persist.attributes().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testRemoveAttribute() {
		try {
			PlayerPersistenceObject persist = new PlayerPersistenceObject();
			
			AttributePersistenceObject att1 = new AttributePersistenceObject();
			att1.objectName("Age");
			att1.objectValue(15);
			AttributePersistenceObject att2 = new AttributePersistenceObject();
			att2.objectName("Class");
			att2.objectValue("Wizard");
			AttributePersistenceObject att3 = new AttributePersistenceObject();
			att3.objectName("Max Health");
			att3.objectValue(5000);
			
			persist.addAttribute(att1);
			persist.addAttribute(att2);
			persist.addAttribute(att3);
			
			assertEquals(3, persist.attributes().size());
			
			persist.removeAttribute(att1);
			assertEquals(2, persist.attributes().size());
			
			persist.removeAttribute(att2);
			assertEquals(1, persist.attributes().size());
			
			persist.removeAttribute(att3);
			assertEquals(0, persist.attributes().size());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testClearAttribute() {
		try {
			PlayerPersistenceObject persist = new PlayerPersistenceObject();
			
			AttributePersistenceObject att1 = new AttributePersistenceObject();
			att1.objectName("Age");
			att1.objectValue(15);
			AttributePersistenceObject att2 = new AttributePersistenceObject();
			att2.objectName("Class");
			att2.objectValue("Wizard");
			AttributePersistenceObject att3 = new AttributePersistenceObject();
			att3.objectName("Max Health");
			att3.objectValue(5000);
			
			persist.addAttribute(att1);
			persist.addAttribute(att2);
			persist.addAttribute(att3);
			
			assertEquals(3, persist.attributes().size());
			
			persist.clearAttributes();
			assertEquals(0, persist.attributes().size());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetName() {
		try {
			PlayerPersistenceObject persist = new PlayerPersistenceObject();
			
			persist.playerName("Deemo");
			assertEquals("Deemo", persist.playerName());
			
			persist.playerName("John");
			assertEquals("John", persist.playerName());
			
			persist.playerName("Miku");
			assertEquals("Miku", persist.playerName());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testAddCharacteristic() {
		try {
			PlayerPersistenceObject persist = new PlayerPersistenceObject();
			
			CharacteristicPersistenceObject char1 = new CharacteristicPersistenceObject();
			char1.objectName("Hair Color");
			char1.objectValue("Brown");
			CharacteristicPersistenceObject char2 = new CharacteristicPersistenceObject();
			char2.objectName("Scar");
			char2.objectValue("Long");
			CharacteristicPersistenceObject char3 = new CharacteristicPersistenceObject();
			char2.objectName("Skin Color");
			char2.objectValue("White");
			
			assertEquals(0, persist.characteristics().size());
			persist.addCharacteristic(char1);
			assertEquals(1, persist.characteristics().size());
			
			persist.addCharacteristic(char2);
			assertEquals(2, persist.characteristics().size());
			
			persist.addCharacteristic(char3);
			assertEquals(3, persist.characteristics().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testRemoveCharacteristic() {
		try {
			PlayerPersistenceObject persist = new PlayerPersistenceObject();
			
			CharacteristicPersistenceObject char1 = new CharacteristicPersistenceObject();
			char1.objectName("Hair Color");
			char1.objectValue("Brown");
			CharacteristicPersistenceObject char2 = new CharacteristicPersistenceObject();
			char2.objectName("Scar");
			char2.objectValue("Long");
			CharacteristicPersistenceObject char3 = new CharacteristicPersistenceObject();
			char2.objectName("Skin Color");
			char2.objectValue("White");
			
			persist.addCharacteristic(char1);
			persist.addCharacteristic(char2);
			persist.addCharacteristic(char3);
			
			assertEquals(3, persist.characteristics().size());
			
			persist.removeCharacteristic(char1);
			assertEquals(2, persist.characteristics().size());
			
			persist.removeCharacteristic(char2);
			assertEquals(1, persist.characteristics().size());
			
			persist.removeCharacteristic(char3);
			assertEquals(0, persist.characteristics().size());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testClearCharacteristic() {
		try {
			PlayerPersistenceObject persist = new PlayerPersistenceObject();
			
			CharacteristicPersistenceObject char1 = new CharacteristicPersistenceObject();
			char1.objectName("Hair Color");
			char1.objectValue("Brown");
			CharacteristicPersistenceObject char2 = new CharacteristicPersistenceObject();
			char2.objectName("Scar");
			char2.objectValue("Long");
			CharacteristicPersistenceObject char3 = new CharacteristicPersistenceObject();
			char2.objectName("Skin Color");
			char2.objectValue("White");
			
			persist.addCharacteristic(char1);
			persist.addCharacteristic(char2);
			persist.addCharacteristic(char3);
			
			assertEquals(3, persist.characteristics().size());
			
			persist.clearCharacteristics();
			assertEquals(0, persist.characteristics().size());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testAddBodyPart() {
		try {
			PlayerPersistenceObject persist = new PlayerPersistenceObject();
			
			BodyPartPersistenceObject body1 = new BodyPartPersistenceObject();
			body1.objectName("Head");
			BodyPartPersistenceObject body2 = new BodyPartPersistenceObject();
			body2.objectName("Feet");
			BodyPartPersistenceObject body3 = new BodyPartPersistenceObject();
			body3.objectName("Tourso");
			
			assertEquals(0, persist.bodyParts().size());
			
			persist.addBodyPart(body1);
			assertEquals(1, persist.bodyParts().size());
			
			persist.addBodyPart(body2);
			assertEquals(2, persist.bodyParts().size());
			
			persist.addBodyPart(body3);
			assertEquals(3, persist.bodyParts().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testRemoveBodyPart() {
		try {
			PlayerPersistenceObject persist = new PlayerPersistenceObject();
			
			BodyPartPersistenceObject body1 = new BodyPartPersistenceObject();
			body1.objectName("Head");
			BodyPartPersistenceObject body2 = new BodyPartPersistenceObject();
			body2.objectName("Feet");
			BodyPartPersistenceObject body3 = new BodyPartPersistenceObject();
			body3.objectName("Tourso");
			
			persist.addBodyPart(body1);
			persist.addBodyPart(body2);
			persist.addBodyPart(body3);
			assertEquals(3, persist.bodyParts().size());
			
			persist.removeBodyPart(body1);
			assertEquals(2, persist.bodyParts().size());
			
			persist.removeBodyPart(body2);
			assertEquals(1, persist.bodyParts().size());
			
			persist.removeBodyPart(body3);
			assertEquals(0, persist.bodyParts().size());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testClearBodyPart() {
		try {
			PlayerPersistenceObject persist = new PlayerPersistenceObject();
			
			BodyPartPersistenceObject body1 = new BodyPartPersistenceObject();
			body1.objectName("Head");
			BodyPartPersistenceObject body2 = new BodyPartPersistenceObject();
			body2.objectName("Feet");
			BodyPartPersistenceObject body3 = new BodyPartPersistenceObject();
			body3.objectName("Tourso");
			
			persist.addBodyPart(body1);
			persist.addBodyPart(body2);
			persist.addBodyPart(body3);
			assertEquals(3, persist.bodyParts().size());
			
			persist.clearBodyParts();
			assertEquals(0, persist.bodyParts().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetInventory() {
		try {
			PlayerPersistenceObject persist = new PlayerPersistenceObject();
			
			InventoryPersistenceObject inv = new InventoryPersistenceObject();
			ItemPersistenceObject item1 = new ItemPersistenceObject();
			item1.itemName("Potion");
			item1.itemDescription("Restores HP");
			ItemPersistenceObject item2 = new ItemPersistenceObject();
			item2.itemName("Ether");
			item2.itemDescription("Restores MP");
			ItemPersistenceObject item3 = new ItemPersistenceObject();
			item3.itemName("Phenoix Down");
			item3.itemDescription("Revives");
			
			inv.addItem(item1, 5);
			inv.addItem(item2, 2);
			inv.addItem(item3, 10);
			
			persist.inventory(inv);
			assertEquals(inv, persist.inventory());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetEquipment() {
		try {
			PlayerPersistenceObject persist = new PlayerPersistenceObject();
			
			EquipmentPersistenceObject equip = new EquipmentPersistenceObject();
			BodyPartPersistenceObject body1 = new BodyPartPersistenceObject();
			body1.objectName("Head");
			BodyPartPersistenceObject body2 = new BodyPartPersistenceObject();
			body2.objectName("Feet");
			BodyPartPersistenceObject body3 = new BodyPartPersistenceObject();
			body3.objectName("Tourso");
			
			ItemPersistenceObject item1 = new ItemPersistenceObject();
			item1.itemName("Helmet");
			item1.itemDescription("Protects the head");
			ItemPersistenceObject item2 = new ItemPersistenceObject();
			item2.itemName("Steel Boots");
			item2.itemDescription("Protects the feet");
			ItemPersistenceObject item3 = new ItemPersistenceObject();
			item3.itemName("Leather Vest");
			item3.itemDescription("Protects the body");
			
			equip.equip(item1, body1);
			equip.equip(item2, body2);
			equip.equip(item3, body3);
			
			persist.equipment(equip);
			assertEquals(equip, persist.equipment());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			_manager = new XmlConfigurationManager(_playerUnitTest);
			
			PlayerPersistenceObject persist = new PlayerPersistenceObject();
			
			//Name
			persist.playerName("Deemo");
			
			//Attributes
			AttributePersistenceObject att1 = new AttributePersistenceObject();
			att1.objectName("Age");
			att1.objectValue(15);
			AttributePersistenceObject att2 = new AttributePersistenceObject();
			att2.objectName("Class");
			att2.objectValue("Wizard");
			AttributePersistenceObject att3 = new AttributePersistenceObject();
			att3.objectName("Max Health");
			att3.objectValue(5000);
			persist.addAttribute(att1);
			persist.addAttribute(att2);
			persist.addAttribute(att3);
			
			//Characteristics
			CharacteristicPersistenceObject char1 = new CharacteristicPersistenceObject();
			char1.objectName("Hair Color");
			char1.objectValue("Brown");
			CharacteristicPersistenceObject char2 = new CharacteristicPersistenceObject();
			char2.objectName("Scar");
			char2.objectValue("Long");
			CharacteristicPersistenceObject char3 = new CharacteristicPersistenceObject();
			char3.objectName("Skin Color");
			char3.objectValue("White");
			persist.addCharacteristic(char1);
			persist.addCharacteristic(char2);
			persist.addCharacteristic(char3);
			
			
			//Body Parts
			BodyPartPersistenceObject body1 = new BodyPartPersistenceObject();
			body1.objectName("Head");
			BodyPartPersistenceObject body2 = new BodyPartPersistenceObject();
			body2.objectName("Feet");
			BodyPartPersistenceObject body3 = new BodyPartPersistenceObject();
			body3.objectName("Tourso");
			
			persist.addBodyPart(body1);
			persist.addBodyPart(body2);
			persist.addBodyPart(body3);
			
			//Inventory
			InventoryPersistenceObject inv = new InventoryPersistenceObject();
			ItemPersistenceObject item1 = new ItemPersistenceObject();
			item1.itemName("Potion");
			item1.itemDescription("Restores HP");
			ItemPersistenceObject item2 = new ItemPersistenceObject();
			item2.itemName("Ether");
			item2.itemDescription("Restores MP");
			ItemPersistenceObject item3 = new ItemPersistenceObject();
			item3.itemName("Phenoix Down");
			item3.itemDescription("Revives");
			inv.addItem(item1, 5);
			inv.addItem(item2, 2);
			inv.addItem(item3, 10);
			persist.inventory(inv);
			
			//Equipment
			EquipmentPersistenceObject equip = new EquipmentPersistenceObject();
			ItemPersistenceObject item4 = new ItemPersistenceObject();
			item4.itemName("Helmet");
			item4.itemDescription("Protects the head");
			ItemPersistenceObject item5 = new ItemPersistenceObject();
			item5.itemName("Steel Boots");
			item5.itemDescription("Protects the feet");
			ItemPersistenceObject item6 = new ItemPersistenceObject();
			item6.itemName("Leather Vest");
			item6.itemDescription("Protects the body");
			BodyPartPersistenceObject ebody1 = new BodyPartPersistenceObject();
			ebody1.objectName("Head");
			BodyPartPersistenceObject ebody2 = new BodyPartPersistenceObject();
			ebody2.objectName("Feet");
			BodyPartPersistenceObject ebody3 = new BodyPartPersistenceObject();
			ebody3.objectName("Tourso");
			
			equip.equip(item4, ebody1);
			equip.equip(item5, ebody2);
			equip.equip(item6, ebody3);
			persist.equipment(equip);
			
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			String fileContents = fileContents(_playerUnitTest);
			assertEquals(_expectedPlayerText, fileContents);
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertToPlayer() {
		try {
			PlayerPersistenceObject persist = new PlayerPersistenceObject();
			
			//Name
			persist.playerName("Deemo");
			
			//Attributes
			AttributePersistenceObject att1 = new AttributePersistenceObject();
			att1.objectName("Age");
			att1.objectValue(15);
			AttributePersistenceObject att2 = new AttributePersistenceObject();
			att2.objectName("Class");
			att2.objectValue("Wizard");
			AttributePersistenceObject att3 = new AttributePersistenceObject();
			att3.objectName("Max Health");
			att3.objectValue(5000);
			persist.addAttribute(att1);
			persist.addAttribute(att2);
			persist.addAttribute(att3);
			
			//Characteristics
			CharacteristicPersistenceObject char1 = new CharacteristicPersistenceObject();
			char1.objectName("Hair Color");
			char1.objectValue("Brown");
			CharacteristicPersistenceObject char2 = new CharacteristicPersistenceObject();
			char2.objectName("Scar");
			char2.objectValue("Long");
			CharacteristicPersistenceObject char3 = new CharacteristicPersistenceObject();
			char3.objectName("Skin Color");
			char3.objectValue("White");
			persist.addCharacteristic(char1);
			persist.addCharacteristic(char2);
			persist.addCharacteristic(char3);
			
			
			//Body Parts
			BodyPartPersistenceObject body1 = new BodyPartPersistenceObject();
			body1.objectName("Head");
			BodyPartPersistenceObject body2 = new BodyPartPersistenceObject();
			body2.objectName("Feet");
			BodyPartPersistenceObject body3 = new BodyPartPersistenceObject();
			body3.objectName("Tourso");
			
			persist.addBodyPart(body1);
			persist.addBodyPart(body2);
			persist.addBodyPart(body3);
			
			//Inventory
			InventoryPersistenceObject inv = new InventoryPersistenceObject();
			ItemPersistenceObject item1 = new ItemPersistenceObject();
			item1.itemName("Potion");
			item1.itemDescription("Restores HP");
			ItemPersistenceObject item2 = new ItemPersistenceObject();
			item2.itemName("Ether");
			item2.itemDescription("Restores MP");
			ItemPersistenceObject item3 = new ItemPersistenceObject();
			item3.itemName("Phenoix Down");
			item3.itemDescription("Revives");
			inv.addItem(item1, 5);
			inv.addItem(item2, 2);
			inv.addItem(item3, 10);
			persist.inventory(inv);
			
			//Equipment
			EquipmentPersistenceObject equip = new EquipmentPersistenceObject();
			ItemPersistenceObject item4 = new ItemPersistenceObject();
			item4.itemName("Helmet");
			item4.itemDescription("Protects the head");
			ItemPersistenceObject item5 = new ItemPersistenceObject();
			item5.itemName("Steel Boots");
			item5.itemDescription("Protects the feet");
			ItemPersistenceObject item6 = new ItemPersistenceObject();
			item6.itemName("Leather Vest");
			item6.itemDescription("Protects the body");
			BodyPartPersistenceObject ebody1 = new BodyPartPersistenceObject();
			ebody1.objectName("Head");
			BodyPartPersistenceObject ebody2 = new BodyPartPersistenceObject();
			ebody2.objectName("Feet");
			BodyPartPersistenceObject ebody3 = new BodyPartPersistenceObject();
			ebody3.objectName("Tourso");
			
			equip.equip(item4, ebody1);
			equip.equip(item5, ebody2);
			equip.equip(item6, ebody3);
			persist.equipment(equip);
			
			IPlayer player = persist.convertToPlayer();
			assertEquals("Deemo", player.name());
			assertEquals(3, player.attributes().size());
			assertEquals(3, player.characteristics().size());
			assertEquals(3, player.bodyParts().size());
			assertEquals(3, player.inventory().items().size());
			assertEquals(3, player.equipment().equipted().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertFromPersistence() {
		try {
			_manager = new XmlConfigurationManager(_playerUnitTest2);
			
			PlayerPersistenceObject persist = new PlayerPersistenceObject();
			
			//Name
			persist.playerName("Deemo");
			
			//Attributes
			AttributePersistenceObject att1 = new AttributePersistenceObject();
			att1.objectName("Age");
			att1.objectValue(15);
			AttributePersistenceObject att2 = new AttributePersistenceObject();
			att2.objectName("Class");
			att2.objectValue("Wizard");
			AttributePersistenceObject att3 = new AttributePersistenceObject();
			att3.objectName("Max Health");
			att3.objectValue(5000);
			persist.addAttribute(att1);
			persist.addAttribute(att2);
			persist.addAttribute(att3);
			
			//Characteristics
			CharacteristicPersistenceObject char1 = new CharacteristicPersistenceObject();
			char1.objectName("Hair Color");
			char1.objectValue("Brown");
			CharacteristicPersistenceObject char2 = new CharacteristicPersistenceObject();
			char2.objectName("Scar");
			char2.objectValue("Long");
			CharacteristicPersistenceObject char3 = new CharacteristicPersistenceObject();
			char3.objectName("Skin Color");
			char3.objectValue("White");
			persist.addCharacteristic(char1);
			persist.addCharacteristic(char2);
			persist.addCharacteristic(char3);
			
			
			//Body Parts
			BodyPartPersistenceObject body1 = new BodyPartPersistenceObject();
			body1.objectName("Head");
			BodyPartPersistenceObject body2 = new BodyPartPersistenceObject();
			body2.objectName("Feet");
			BodyPartPersistenceObject body3 = new BodyPartPersistenceObject();
			body3.objectName("Tourso");
			
			persist.addBodyPart(body1);
			persist.addBodyPart(body2);
			persist.addBodyPart(body3);
			
			//Inventory
			InventoryPersistenceObject inv = new InventoryPersistenceObject();
			ItemPersistenceObject item1 = new ItemPersistenceObject();
			item1.itemName("Potion");
			item1.itemDescription("Restores HP");
			ItemPersistenceObject item2 = new ItemPersistenceObject();
			item2.itemName("Ether");
			item2.itemDescription("Restores MP");
			ItemPersistenceObject item3 = new ItemPersistenceObject();
			item3.itemName("Phenoix Down");
			item3.itemDescription("Revives");
			inv.addItem(item1, 5);
			inv.addItem(item2, 2);
			inv.addItem(item3, 10);
			persist.inventory(inv);
			
			//Equipment
			EquipmentPersistenceObject equip = new EquipmentPersistenceObject();
			ItemPersistenceObject item4 = new ItemPersistenceObject();
			item4.itemName("Helmet");
			item4.itemDescription("Protects the head");
			ItemPersistenceObject item5 = new ItemPersistenceObject();
			item5.itemName("Steel Boots");
			item5.itemDescription("Protects the feet");
			ItemPersistenceObject item6 = new ItemPersistenceObject();
			item6.itemName("Leather Vest");
			item6.itemDescription("Protects the body");
			BodyPartPersistenceObject ebody1 = new BodyPartPersistenceObject();
			ebody1.objectName("Head");
			BodyPartPersistenceObject ebody2 = new BodyPartPersistenceObject();
			ebody2.objectName("Feet");
			BodyPartPersistenceObject ebody3 = new BodyPartPersistenceObject();
			ebody3.objectName("Tourso");
			
			equip.equip(item4, ebody1);
			equip.equip(item5, ebody2);
			equip.equip(item6, ebody3);
			persist.equipment(equip);
			
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(_playerUnitTest2);
			manager2.load();
			
			PlayerPersistenceObject player = new PlayerPersistenceObject();
			player.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals("Deemo", player.playerName());
			assertEquals(3, player.attributes().size());
			assertEquals(3, player.characteristics().size());
			assertEquals(3, player.bodyParts().size());
			assertEquals(3, player.inventory().items().size());
			assertEquals(3, player.equipment().equipment().size());
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
