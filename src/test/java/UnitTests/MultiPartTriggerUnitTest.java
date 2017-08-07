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
import playerlib.items.Item;
import playerlib.items.Property;
import playerlib.player.IPlayer;
import playerlib.player.Player;
import textadventurelib.core.Condition;
import textadventurelib.core.ModificationObject;
import textadventurelib.core.PlayerConditionParameters;
import textadventurelib.core.TriggerParameters;
import textadventurelib.triggers.ITrigger;
import textadventurelib.triggers.MultiPartTrigger;
import textadventurelib.triggers.PlayerConditionTrigger;
import textadventurelib.triggers.TextTrigger;

public class MultiPartTriggerUnitTest {

	private IAttribute _mp = new Attribute("MP", 100);
	
	@Test
	public void testCreateMultiPartTrigger() {
		List<ITrigger> triggers = new ArrayList<ITrigger>();
		ITrigger trigger = new MultiPartTrigger(triggers);
		assertNotNull(trigger);
		assertNotNull(trigger.condition());
	}

	@Test
	public void testMultiTriggerWithNoTriggers() {
		List<ITrigger> triggers = new ArrayList<ITrigger>();
		ITrigger trigger = new MultiPartTrigger(triggers);
		assertFalse(trigger.shouldFire(null));
	}
	
	@Test
	public void testMultiTriggerWithSameTriggerType() {
		List<ITrigger> triggers = new ArrayList<ITrigger>();
		triggers.add(new TextTrigger(".*"));
		triggers.add(new TextTrigger("(?i)next"));
		
		ITrigger trigger = new MultiPartTrigger(triggers);
		assertTrue(trigger.shouldFire(new TriggerParameters("Next")));
		assertFalse(trigger.shouldFire(new TriggerParameters("Apple")));
	}
	
	@Test
	public void testMultiTriggerWithDifferentType() {
		List<ITrigger> triggers = new ArrayList<ITrigger>();
		IPlayer player = generatePlayer();
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.condition(Condition.Has);
		params.modificationObject(ModificationObject.Attribute);
		params.id(new String[]{"MP"});
		PlayerConditionTrigger playerTrigger = new PlayerConditionTrigger(player.name(), params);
		triggers.add(playerTrigger);
		triggers.add(new TextTrigger("(?i)next"));
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(player);
		
		ITrigger trigger = new MultiPartTrigger(triggers);
		assertTrue(trigger.shouldFire(new TriggerParameters("NeXt", players)));
		assertFalse(trigger.shouldFire(new TriggerParameters("last", players)));
		player.attributes().remove(_mp);
		assertFalse(trigger.shouldFire(new TriggerParameters("NeXt", players)));
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
		
		IEquipment equip = new Equipment();
		IItem headgear = new Item("cloth hat");
		headgear.addProperty(new Property("Armor", "AC+12"));
		IItem armor = new Item("armor");
		armor.addProperty(new Property("Armor", "AC+2"));
		
		IBodyPart body = new BodyPart("body");
		
		equip.equip(body, armor);
		
		List<IAttribute> attribs = new ArrayList<IAttribute>();
		attribs.add(new Attribute("HP", 200));
		attribs.add(_mp);
		
		List<ICharacteristic> chars = new ArrayList<ICharacteristic>();
		chars.add(new Characteristic("age", 25));
		chars.add(new Characteristic("Hair Color", "Brown"));
		
		List<IBodyPart> bparts = new ArrayList<IBodyPart>();
		List<ICharacteristic> armChars = new ArrayList<ICharacteristic>();
		armChars.add(new Characteristic("scar", "deep"));
		armChars.add(new Characteristic("color", "white"));
		bparts.add(new BodyPart("Arm", "Players Arm", armChars));
		bparts.add(body);
		
		//TODO: Challenge for playerlib. Should adding a bodypart update equipment?
		return new Player("John", null, inv, equip, attribs, chars, bparts);
	}
}
