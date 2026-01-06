import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import playerlib.equipment.BodyPart;
import playerlib.equipment.Equipment;
import playerlib.equipment.IBodyPart;
import playerlib.equipment.IEquipment;
import playerlib.items.IItem;
import playerlib.items.Item;
import playerlib.player.IPlayer;
import playerlib.player.Player;
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.EquipmentPersistenceObject;
import textadventurelib.persistence.player.ItemPersistenceObject;

public class EquipmentPersistenceUnitTest {
	
	private XmlConfigurationManager _manager;
	
	private final String _equipmentUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/equipmentUnitTest.xml";
	private final String _equipmentUnitTest2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/equipmentUnitTest2.xml";
	private final String _expectedEquipmentText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Equipment><Equiptable><Item><Name ValueType=\"string\">Helmet</Name><Description ValueType=\"string\">Protects the head</Description><Content/><Properties/></Item><BodyPart><Name ValueType=\"string\">Head</Name><Description/><Characteristics/></BodyPart></Equiptable><Equiptable><Item><Name ValueType=\"string\">Steel Boots</Name><Description ValueType=\"string\">Protects the feet</Description><Content/><Properties/></Item><BodyPart><Name ValueType=\"string\">Feet</Name><Description/><Characteristics/></BodyPart></Equiptable><Equiptable><Item><Name ValueType=\"string\">Leather Vest</Name><Description ValueType=\"string\">Protects the tourso</Description><Content/><Properties/></Item><BodyPart><Name ValueType=\"string\">Tourso</Name><Description/><Characteristics/></BodyPart></Equiptable></Equipment>";
	
	@Test
	public void testCreate() {
		try {
			EquipmentPersistenceObject persist = new EquipmentPersistenceObject();
			
			assertNotNull(persist);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testPlayerCtor() {
		try {
			IPlayer player = new Player("John");
			//This is stupid playerlib needs to be updated.
			player.equipment(new Equipment());
			
			IBodyPart arm = new BodyPart("Arm");
			IBodyPart head = new BodyPart("Head");
			
			IItem gauntlet = new Item("Gauntlet");
			IItem hood = new Item("Hood");
			
			player.addBodyPart(arm);
			player.addBodyPart(head);
			player.equipment().equip(arm, gauntlet);
			player.equipment().equip(head, hood);
			
			EquipmentPersistenceObject persist = new EquipmentPersistenceObject(player);
			
			assertEquals(2, persist.equipment().keySet().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testCreateFromPlayer() {
		try {
			IPlayer player = new Player("John");
			player.equipment(new Equipment());
			
			IBodyPart arm = new BodyPart("Arm");
			IBodyPart head = new BodyPart("Head");
			
			IItem gauntlet = new Item("Gauntlet");
			IItem hood = new Item("Hood");
			
			player.addBodyPart(arm);
			player.addBodyPart(head);
			player.equipment().equip(arm, gauntlet);
			player.equipment().equip(head, hood);
			
			EquipmentPersistenceObject persist = new EquipmentPersistenceObject();
			persist.createFromPlayer(player);
			
			assertEquals(2, persist.equipment().keySet().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testEquip() {
		try {
			EquipmentPersistenceObject persist = new EquipmentPersistenceObject();
			
			ItemPersistenceObject item1 = new ItemPersistenceObject();
			item1.itemName("Helmet");
			item1.itemDescription("Protects the head");
			ItemPersistenceObject item2 = new ItemPersistenceObject();
			item2.itemName("Steel Boots");
			item2.itemDescription("Protects the feet");
			ItemPersistenceObject item3 = new ItemPersistenceObject();
			item3.itemName("Leather Vest");
			item3.itemDescription("Protects the tourso");
			
			BodyPartPersistenceObject bodyPart1 = new BodyPartPersistenceObject();
			bodyPart1.objectName("Head");
			BodyPartPersistenceObject bodyPart2 = new BodyPartPersistenceObject();
			bodyPart2.objectName("Feet");
			BodyPartPersistenceObject bodyPart3 = new BodyPartPersistenceObject();
			bodyPart3.objectName("Tourso");
			
			assertEquals(0, persist.equipment().size());
			persist.equip(item1, bodyPart1);
			assertEquals(1, persist.equipment().size());
			persist.equip(item2, bodyPart2);
			assertEquals(2, persist.equipment().size());
			persist.equip(item3, bodyPart3);
			assertEquals(3, persist.equipment().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testUnEquip() {
		try {
			EquipmentPersistenceObject persist = new EquipmentPersistenceObject();
			
			ItemPersistenceObject item1 = new ItemPersistenceObject();
			item1.itemName("Helmet");
			item1.itemDescription("Protects the head");
			ItemPersistenceObject item2 = new ItemPersistenceObject();
			item2.itemName("Steel Boots");
			item2.itemDescription("Protects the feet");
			ItemPersistenceObject item3 = new ItemPersistenceObject();
			item3.itemName("Leather Vest");
			item3.itemDescription("Protects the tourso");
			
			BodyPartPersistenceObject bodyPart1 = new BodyPartPersistenceObject();
			bodyPart1.objectName("Head");
			BodyPartPersistenceObject bodyPart2 = new BodyPartPersistenceObject();
			bodyPart2.objectName("Feet");
			BodyPartPersistenceObject bodyPart3 = new BodyPartPersistenceObject();
			bodyPart3.objectName("Tourso");
			
			persist.equip(item1, bodyPart1);
			persist.equip(item2, bodyPart2);
			persist.equip(item3, bodyPart3);
			assertEquals(3, persist.equipment().size());
			
			persist.unequip(bodyPart1);
			assertEquals(2, persist.equipment().size());
			
			persist.unequip(bodyPart2);
			assertEquals(1, persist.equipment().size());
			
			persist.unequip(bodyPart3);
			assertEquals(0, persist.equipment().size());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			_manager = new XmlConfigurationManager(_equipmentUnitTest);
			
			EquipmentPersistenceObject persist = new EquipmentPersistenceObject();
			
			ItemPersistenceObject item1 = new ItemPersistenceObject();
			item1.itemName("Helmet");
			item1.itemDescription("Protects the head");
			ItemPersistenceObject item2 = new ItemPersistenceObject();
			item2.itemName("Steel Boots");
			item2.itemDescription("Protects the feet");
			ItemPersistenceObject item3 = new ItemPersistenceObject();
			item3.itemName("Leather Vest");
			item3.itemDescription("Protects the tourso");
			
			BodyPartPersistenceObject bodyPart1 = new BodyPartPersistenceObject();
			bodyPart1.objectName("Head");
			BodyPartPersistenceObject bodyPart2 = new BodyPartPersistenceObject();
			bodyPart2.objectName("Feet");
			BodyPartPersistenceObject bodyPart3 = new BodyPartPersistenceObject();
			bodyPart3.objectName("Tourso");
			
			persist.equip(item1, bodyPart1);
			persist.equip(item2, bodyPart2);
			persist.equip(item3, bodyPart3);
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			String fileContents = fileContents(_equipmentUnitTest);
			assertEquals(_expectedEquipmentText, fileContents);
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertToEquipment() {
		try {
			EquipmentPersistenceObject persist = new EquipmentPersistenceObject();
			
			ItemPersistenceObject item1 = new ItemPersistenceObject();
			item1.itemName("Helmet");
			item1.itemDescription("Protects the head");
			ItemPersistenceObject item2 = new ItemPersistenceObject();
			item2.itemName("Steel Boots");
			item2.itemDescription("Protects the feet");
			ItemPersistenceObject item3 = new ItemPersistenceObject();
			item3.itemName("Leather Vest");
			item3.itemDescription("Protects the tourso");
			
			BodyPartPersistenceObject bodyPart1 = new BodyPartPersistenceObject();
			bodyPart1.objectName("Head");
			BodyPartPersistenceObject bodyPart2 = new BodyPartPersistenceObject();
			bodyPart2.objectName("Feet");
			BodyPartPersistenceObject bodyPart3 = new BodyPartPersistenceObject();
			bodyPart3.objectName("Tourso");
			
			persist.equip(item1, bodyPart1);
			persist.equip(item2, bodyPart2);
			persist.equip(item3, bodyPart3);
			
			IEquipment equip = persist.convertToEquipment();
			assertEquals(3, equip.equipted().size());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertFromPersistence() {
		try {
			_manager = new XmlConfigurationManager(_equipmentUnitTest2);
			
			EquipmentPersistenceObject persist = new EquipmentPersistenceObject();
			
			ItemPersistenceObject item1 = new ItemPersistenceObject();
			item1.itemName("Helmet");
			item1.itemDescription("Protects the head");
			ItemPersistenceObject item2 = new ItemPersistenceObject();
			item2.itemName("Steel Boots");
			item2.itemDescription("Protects the feet");
			ItemPersistenceObject item3 = new ItemPersistenceObject();
			item3.itemName("Leather Vest");
			item3.itemDescription("Protects the tourso");
			
			BodyPartPersistenceObject bodyPart1 = new BodyPartPersistenceObject();
			bodyPart1.objectName("Head");
			BodyPartPersistenceObject bodyPart2 = new BodyPartPersistenceObject();
			bodyPart2.objectName("Feet");
			BodyPartPersistenceObject bodyPart3 = new BodyPartPersistenceObject();
			bodyPart3.objectName("Tourso");
			
			persist.equip(item1, bodyPart1);
			persist.equip(item2, bodyPart2);
			persist.equip(item3, bodyPart3);
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			XmlConfigurationManager manager = new XmlConfigurationManager(_equipmentUnitTest2);
			manager.load();
			
			EquipmentPersistenceObject persist2 = new EquipmentPersistenceObject();
			persist2.convertFromPersistence((XmlConfigurationObject)manager.configurationObjects().get(0));
			
			assertEquals(3, persist2.equipment().size());
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
