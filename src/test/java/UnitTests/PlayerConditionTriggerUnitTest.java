package UnitTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import playerlib.attributes.Attribute;
import playerlib.attributes.IAttribute;
import playerlib.characteristics.Characteristic;
import playerlib.characteristics.ICharacteristic;
import playerlib.equipment.BodyPart;
import playerlib.equipment.Equipment;
import playerlib.equipment.IBodyPart;
import playerlib.equipment.IEquipment;
import playerlib.inventory.IInventory;
import playerlib.inventory.Inventory;
import playerlib.items.IItem;
import playerlib.items.IProperty;
import playerlib.items.Item;
import playerlib.items.Property;
import playerlib.player.IPlayer;
import playerlib.player.Player;
import textadventurelib.core.Condition;
import textadventurelib.core.ModificationObject;
import textadventurelib.core.PlayerConditionParameters;
import textadventurelib.core.TriggerParameters;
import textadventurelib.triggers.PlayerConditionTrigger;

public class PlayerConditionTriggerUnitTest {
	
	private IItem _key = new Item("key");
	private IItem _boot = new Item("boot");
	private IBodyPart _foot = new BodyPart("foot");
	private IBodyPart _head = new BodyPart("head");
	private IBodyPart _shoulder = new BodyPart("shoulder");
	
	@Test
	public void testCreate() {
		IPlayer player = generatePlayer();
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.EqualTo);
		params.modificationObject(ModificationObject.Player);
		params.dataMember("name");
		params.comparisionData("John");
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		
		assertNotNull(trigger);
		assertNotNull(trigger.condition());
	}
	
	@Test
	public void testPlayerEquals() {
		IPlayer player = generatePlayer();
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.EqualTo);
		params.modificationObject(ModificationObject.Player);
		params.dataMember("name");
		params.comparisionData("John");
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		
		player.name("Deemo");
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
	}
	
	@Test
	public void testPlayerNotEquals() {
		IPlayer player = generatePlayer();
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.NotEqual);
		params.modificationObject(ModificationObject.Player);
		params.dataMember("name");
		params.comparisionData("John");
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
		
		player.name("Deemo");
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
	}
	
	@Test
	public void testAttributeGreaterThan() {
		IPlayer player = generatePlayer();
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.GreaterThan);
		params.modificationObject(ModificationObject.Attribute);
		params.id(new String[]{"HP"});
		params.dataMember("value");
		params.comparisionData(100);
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		player.attributes().get(0).value(50);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
	}

	@Test
	public void testAttributeLessThan() {
		IPlayer player = generatePlayer();
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.LessThan);
		params.modificationObject(ModificationObject.Attribute);
		params.id(new String[]{"HP"});
		params.dataMember("value");
		params.comparisionData(300);
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		player.attributes().get(0).value(3000);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
	}
	
	@Test
	public void testAttributeEqualTo() {
		IPlayer player = generatePlayer();
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.EqualTo);
		params.modificationObject(ModificationObject.Attribute);
		params.id(new String[]{"HP"});
		params.dataMember("value");
		params.comparisionData(200);
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		player.attributes().get(0).value(3000);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
	}

	@Test
	public void testAttributeNotEqual() {
		IPlayer player = generatePlayer();
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.NotEqual);
		params.modificationObject(ModificationObject.Attribute);
		params.id(new String[]{"HP"});
		params.dataMember("value");
		params.comparisionData(300);
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		player.attributes().get(0).value(300);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
	}

	@Test
	public void testAttributeHas() {
		IPlayer player = generatePlayer();
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.Has);
		params.modificationObject(ModificationObject.Attribute);
		params.id(new String[]{"MP"});
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		player.attributes().remove(1);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
	}
	
	@Test
	public void testCharacteristicGreaterThan() {
		IPlayer player = generatePlayer();
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.GreaterThan);
		params.modificationObject(ModificationObject.Characteristic);
		params.id(new String[]{"age"});
		params.dataMember("value");
		params.comparisionData(10);
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		player.characteristics().get(0).value(5);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
	}
	
	@Test
	public void testCharacteristicLessThan() {
		IPlayer player = generatePlayer();
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.LessThan);
		params.modificationObject(ModificationObject.Characteristic);
		params.id(new String[]{"age"});
		params.dataMember("value");
		params.comparisionData(30);
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		player.characteristics().get(0).value(31);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
	}
	
	@Test
	public void testCharacteristicEqualTo() {
		IPlayer player = generatePlayer();
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.EqualTo);
		params.modificationObject(ModificationObject.Characteristic);
		params.id(new String[]{"Hair Color"});
		params.dataMember("value");
		params.comparisionData("Brown");
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		player.characteristics().get(1).value("Blue");
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
	}

	@Test
	public void testCharacteristicNotEqual() {
		IPlayer player = generatePlayer();
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.NotEqual);
		params.modificationObject(ModificationObject.Characteristic);
		params.id(new String[]{"Hair Color"});
		params.dataMember("value");
		params.comparisionData("Blue");
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		player.characteristics().get(1).value("Blue");
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
	}

	@Test
	public void testCharacteristicHas() {
		IPlayer player = generatePlayer();
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.Has);
		params.modificationObject(ModificationObject.Characteristic);
		params.id(new String[]{"Hair Color"});
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		player.characteristics().remove(1);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
	}
	
	@Test
	public void testBodyPartLessThan() {
		IPlayer player = generatePlayer();
		ICharacteristic toes = new Characteristic("toes", 3);
		_foot.addCharacteristic(toes);
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.LessThan);
		params.modificationObject(ModificationObject.BodyPart);
		params.id(new String[]{"foot", "toes"});
		params.dataMember("value");
		params.comparisionData(5);
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		toes.value(10);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
		_foot.clearCharacteristics();
	}
	
	@Test
	public void testBodyPartGreaterThan() {
		IPlayer player = generatePlayer();
		ICharacteristic toes = new Characteristic("toes", 5);
		_foot.addCharacteristic(toes);
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.GreaterThan);
		params.modificationObject(ModificationObject.BodyPart);
		params.id(new String[]{"foot", "toes"});
		params.dataMember("value");
		params.comparisionData(4);
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		toes.value(3);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
		_foot.clearCharacteristics();
	}
	
	@Test
	public void testBodyPartEqualTo() {
		IPlayer player = generatePlayer();
		ICharacteristic hairColor = new Characteristic("Hair Color", "Green");
		_head.addCharacteristic(hairColor);
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.EqualTo);
		params.modificationObject(ModificationObject.BodyPart);
		params.id(new String[]{"head", "Hair Color"});
		params.dataMember("value");
		params.comparisionData("Green");
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		hairColor.value("Blue");
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
		_head.clearCharacteristics();
	}
	
	@Test
	public void testBodyPartNotEqual() {
		IPlayer player = generatePlayer();
		ICharacteristic hairColor = new Characteristic("Hair Color", "Green");
		_head.addCharacteristic(hairColor);
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.NotEqual);
		params.modificationObject(ModificationObject.BodyPart);
		params.id(new String[]{"head", "Hair Color"});
		params.dataMember("value");
		params.comparisionData("Blue");
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		hairColor.value("Blue");
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
		_head.clearCharacteristics();
	}
	
	@Test
	public void testBodyPartHas() {
		IPlayer player = generatePlayer();
		player.addBodyPart(_shoulder);
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.Has);
		params.modificationObject(ModificationObject.BodyPart);
		params.id(new String[]{"shoulder"});
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		player.removeBodyPart(_shoulder);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
	}
	
	@Test
	public void testInventoryCountGreaterThan() {
		IPlayer player = generatePlayer();
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.GreaterThan);
		params.modificationObject(ModificationObject.Inventory);
		params.id(new String[]{"key"});
		params.comparisionData(4);
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		player.inventory().setAmount(_key, 2);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
	}
	
	@Test
	public void testInventoryPropertyGreaterThan() {
		IPlayer player = generatePlayer();
		IProperty uses = new Property("uses", 5);
		_key.addProperty(uses);
		
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.GreaterThan);
		params.modificationObject(ModificationObject.Inventory);
		params.id(new String[]{"key", "uses"});
		params.dataMember("value");
		params.comparisionData(4);
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		uses.value(2);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
		_key.clearProperties();
	}
	
	@Test
	public void testInventoryCountLessThan() {
		IPlayer player = generatePlayer();
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.LessThan);
		params.modificationObject(ModificationObject.Inventory);
		params.id(new String[]{"key"});
		params.comparisionData(10);
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		player.inventory().setAmount(_key, 15);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
	}
	
	@Test
	public void testInventoryPropertyLessThan() {
		IPlayer player = generatePlayer();
		IProperty uses = new Property("uses", 5);
		_key.addProperty(uses);
		
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.LessThan);
		params.modificationObject(ModificationObject.Inventory);
		params.id(new String[]{"key", "uses"});
		params.dataMember("value");
		params.comparisionData(7);
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		uses.value(10);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
		_key.clearProperties();		
	}
	
	@Test
	public void testInventoryEqualTo() {
		IPlayer player = generatePlayer();
		IProperty breakable = new Property("breakable", false);
		_key.addProperty(breakable);
		
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.EqualTo);
		params.modificationObject(ModificationObject.Inventory);
		params.id(new String[]{"key", "breakable"});
		params.dataMember("value");
		params.comparisionData(false);
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		breakable.value(true);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
		_key.clearProperties();
	}
	
	@Test
	public void testInventoryNotEqual() {
		IPlayer player = generatePlayer();
		IProperty breakable = new Property("breakable", true);
		_key.addProperty(breakable);
		
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.NotEqual);
		params.modificationObject(ModificationObject.Inventory);
		params.id(new String[]{"key", "breakable"});
		params.dataMember("value");
		params.comparisionData(false);
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		breakable.value(false);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
		_key.clearProperties();
	}
	
	@Test
	public void testInventoryHas() {
		IPlayer player = generatePlayer();
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.Has);
		params.modificationObject(ModificationObject.Inventory);
		params.id(new String[]{"key"});
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		player.inventory().removeItem(_key);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
	}
	
	@Test
	public void testEquipmentGreaterThan() {
		IPlayer player = generatePlayer();
		IProperty durability = new Property("durability", 75);
		_boot.addProperty(durability);
		
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.GreaterThan);
		params.modificationObject(ModificationObject.Equipment);
		params.id(new String[]{"boot", "durability"});
		params.dataMember("value");
		params.comparisionData(50);
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		durability.value(25);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
		_boot.clearProperties();
	}
	
	@Test
	public void testEquipmentLessThan() {
		IPlayer player = generatePlayer();
		IProperty durability = new Property("durability", 75);
		_boot.addProperty(durability);
		
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.LessThan);
		params.modificationObject(ModificationObject.Equipment);
		params.id(new String[]{"boot", "durability"});
		params.dataMember("value");
		params.comparisionData(80);
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		durability.value(100);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
		_boot.clearProperties();
	}
	
	@Test
	public void testEquipmentEqualTo() {
		IPlayer player = generatePlayer();
		IProperty durability = new Property("slotted", true);
		_boot.addProperty(durability);
		
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.EqualTo);
		params.modificationObject(ModificationObject.Equipment);
		params.id(new String[]{"boot", "slotted"});
		params.dataMember("value");
		params.comparisionData(true);
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		durability.value(false);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
		_boot.clearProperties();
	}
	
	@Test
	public void testEquipmentNotEqual() {
		IPlayer player = generatePlayer();
		IProperty durability = new Property("slotted", true);
		_boot.addProperty(durability);
		
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.NotEqual);
		params.modificationObject(ModificationObject.Equipment);
		params.id(new String[]{"boot", "slotted"});
		params.dataMember("value");
		params.comparisionData(false);
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		durability.value(false);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
		_boot.clearProperties();
	}
	
	@Test
	public void testEquipmentHas() {
		IPlayer player = generatePlayer();
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.Has);
		params.modificationObject(ModificationObject.Equipment);
		params.id(new String[]{"boot"});
		
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(player.name(), params);
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		assertTrue(trigger.shouldFire(new TriggerParameters(players)));
		player.equipment().unEquip(_foot);
		assertFalse(trigger.shouldFire(new TriggerParameters(players)));
	}
	
	private IPlayer generatePlayer() {
		IInventory inv = new Inventory();
		IItem potion = new Item("potion");
		potion.description("Restores HP");
		potion.addProperty(new Property("HPUP", "+50"));
		
		IItem ether = new Item("Ether");
		potion.description("Restores MP");
		potion.addProperty(new Property("MPUP", "+20"));
		
		inv.addItem(potion, 5);
		inv.addItem(ether, 3);
		inv.addItem(_key, 5);
		
		IEquipment equip = new Equipment();
		IItem headgear = new Item("cloth hat");
		headgear.addProperty(new Property("Armor", "AC+12"));
		IItem armor = new Item("armor");
		armor.addProperty(new Property("Armor", "AC+2"));
		
		IBodyPart body = new BodyPart("body");
		
		equip.equip(_head, headgear);
		equip.equip(body, armor);
		equip.equip(_foot, _boot);
		
		List<IAttribute> attribs = new ArrayList<IAttribute>();
		attribs.add(new Attribute("HP", 200));
		attribs.add(new Attribute("MP", 100));
		
		List<ICharacteristic> chars = new ArrayList<ICharacteristic>();
		chars.add(new Characteristic("age", 25));
		chars.add(new Characteristic("Hair Color", "Brown"));
		
		List<IBodyPart> bparts = new ArrayList<IBodyPart>();
		List<ICharacteristic> armChars = new ArrayList<ICharacteristic>();
		armChars.add(new Characteristic("scar", "deep"));
		armChars.add(new Characteristic("color", "white"));
		bparts.add(new BodyPart("Arm", "Players Arm", armChars));
		bparts.add(body);
		bparts.add(_head);
		bparts.add(_foot);
		
		//TODO: Challenge for playerlib. Should adding a bodypart update equipment?
		return new Player("John", null, inv, equip, attribs, chars, bparts);
	}
}
