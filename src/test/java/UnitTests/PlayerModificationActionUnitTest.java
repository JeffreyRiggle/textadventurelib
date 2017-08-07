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
import textadventurelib.actions.ModifyPlayerAction;
import textadventurelib.core.ChangeType;
import textadventurelib.core.ExecutionParameters;
import textadventurelib.core.ModificationArgs;
import textadventurelib.core.ModificationObject;
import textadventurelib.core.ModificationType;
import textadventurelib.core.PlayerModificationData;

public class PlayerModificationActionUnitTest {

	private IPlayer _player;
	private ModificationArgs _args;
	private ICharacteristic _age = new Characteristic("age", 25);
	
	private String _defaultName = "John";
	private String _changeName = "Deemo";
	private int _changeAge = 13;
	private int _addAge = 5;
	private int _removeAge = 3;
	
	private int _defaultHp = 200;
	private int _addHp = 1800;
	private IAttribute _hp = new Attribute("HP", _defaultHp);
	private IAttribute _mp = new Attribute("MP", 100);
	private IAttribute _class = new Attribute("Class", "Wizard");
	private String _changeClass = "Druid";
	private int _removeMp = 40;
	
	private IItem _clothHat = new Item("cloth hat");
	private IItem _catHat = new Item("Cat hat");
	private IItem _armor = new Item("armor");
	private IItem _bracelet = new Item("bracelet");
	private IItem _potion = new Item("potion");
	private IItem _ether = new Item("Ether");
	private IItem _key = new Item("key");
	
	private IProperty _breakable = new Property("Breakable", true);
	private IProperty _consumable = new Property("Consumable", true);
	
	private IBodyPart _head = new BodyPart("head");
	private IBodyPart _body = new BodyPart("body");
	private IBodyPart _arm = new BodyPart("arm", "Players arm", new ArrayList<ICharacteristic>());
	private IBodyPart _feet = new BodyPart("feet");
	
	private ICharacteristic _hairColor = new Characteristic("HairColor", "Black");
	
	@Test
	public void testCreatePlayerModificationAction() {
		_player = generatePlayer();
		ModifyPlayerAction modifyAction = new ModifyPlayerAction(_player.name());
		_args = new ModificationArgs(ModificationObject.Player);
		
		PlayerModificationData modificationData = new PlayerModificationData(ModificationType.Add, _args);
		modifyAction.data(modificationData);
		assertEquals(modificationData, modifyAction.data());
	}

	@Test
	public void testChangePlayer() {
		_player = generatePlayer();
		assertEquals(_defaultName, _player.name());
		
		ModifyPlayerAction modifyAction = new ModifyPlayerAction(_player.name());
		_args = new ModificationArgs(ModificationObject.Player, _changeName);
		PlayerModificationData modificationData = new PlayerModificationData(ModificationType.Change, _args);
		
		modifyAction.data(modificationData);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(_player);
		ExecutionParameters params = new ExecutionParameters(players);
		modifyAction.execute(params);
		assertEquals(_changeName, _player.name());
	}

	@Test
	public void testAddCharacteristic() {
		_player = generatePlayer();
		
		ModifyPlayerAction modifyAction = new ModifyPlayerAction(_player.name());
		ICharacteristic charToAdd = new Characteristic("a", "b");
		_args = new ModificationArgs(ModificationObject.Characteristic, charToAdd);
		PlayerModificationData modificationData = new PlayerModificationData(ModificationType.Add, _args);

		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(_player);
		ExecutionParameters params = new ExecutionParameters(players);
		
		assertTrue(!_player.characteristics().contains(charToAdd));
		modifyAction.data(modificationData);
		modifyAction.execute(params);
		assertTrue(_player.characteristics().contains(charToAdd));
	}

	@Test
	public void testRemoveCharacterstic() {
		_player = generatePlayer();
		
		ModifyPlayerAction modifyAction = new ModifyPlayerAction(_player.name());
		_args = new ModificationArgs(ModificationObject.Characteristic, _age);
		PlayerModificationData modificationData = new PlayerModificationData(ModificationType.Remove, _args);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(_player);
		ExecutionParameters params = new ExecutionParameters(players);
		
		assertTrue(_player.characteristics().contains(_age));
		modifyAction.data(modificationData);
		modifyAction.execute(params);
		assertTrue(!_player.characteristics().contains(_age));
	}
	
	@Test
	public void testChangeCharacteristic() {
		_player = generatePlayer();
		
		ModifyPlayerAction modifyAction = new ModifyPlayerAction(_player.name());
		_args = new ModificationArgs(ModificationObject.Characteristic, _changeAge, _age.name(), ChangeType.Assign);
		PlayerModificationData modificationData = new PlayerModificationData(ModificationType.Change, _args);
		modifyAction.data(modificationData);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(_player);
		ExecutionParameters params = new ExecutionParameters(players);
		
		//TODO: Fix this?
		assertEquals(_age.<Object>value(), _player.characteristics().get(0).<Object>value());
		modifyAction.execute(params);
		assertEquals(_changeAge, _player.characteristics().get(0).<Object>value());
		
		_args = new ModificationArgs(ModificationObject.Characteristic, _addAge, _age.name(), ChangeType.Add);
		modificationData = new PlayerModificationData(ModificationType.Change, _args);
		modifyAction.data(modificationData);
		
		//TODO: Fix this?
		assertEquals(_changeAge, _player.characteristics().get(0).<Object>value());
		modifyAction.execute(params);
		assertEquals(18, _player.characteristics().get(0).<Object>value());
		
		_args = new ModificationArgs(ModificationObject.Characteristic, _removeAge, _age.name(), ChangeType.Subtract);
		modificationData = new PlayerModificationData(ModificationType.Change, _args);
		modifyAction.data(modificationData);
		
		//TODO: Fix this?
		assertEquals(18, _player.characteristics().get(0).<Object>value());
		modifyAction.execute(params);
		assertEquals(15, _player.characteristics().get(0).<Object>value());
	}
	
	@Test
	public void testAddAttribute() {
		_player = generatePlayer();
		
		ModifyPlayerAction modifyAction = new ModifyPlayerAction(_player.name());
		_args = new ModificationArgs(ModificationObject.Attribute, _class);
		PlayerModificationData modificationData = new PlayerModificationData(ModificationType.Add, _args);
		modifyAction.data(modificationData);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(_player);
		ExecutionParameters params = new ExecutionParameters(players);
		
		assertTrue(!_player.attributes().contains(_class));
		modifyAction.execute(params);
		assertTrue(_player.attributes().contains(_class));
	}
	
	@Test
	public void testRemoveAttribute() {
		_player = generatePlayer();
		
		ModifyPlayerAction modifyAction = new ModifyPlayerAction(_player.name());
		_args = new ModificationArgs(ModificationObject.Attribute, _hp);
		PlayerModificationData modificationData = new PlayerModificationData(ModificationType.Remove, _args);
		modifyAction.data(modificationData);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(_player);
		ExecutionParameters params = new ExecutionParameters(players);
		
		assertTrue(_player.attributes().contains(_hp));
		modifyAction.execute(params);
		assertTrue(!_player.attributes().contains(_hp));
	}
	
	@Test
	public void testChangeAttribute() {
		_player = generatePlayer();
		
		_player.addAttribute(_class);
		ModifyPlayerAction modifyAction = new ModifyPlayerAction(_player.name());
		_args = new ModificationArgs(ModificationObject.Attribute, _changeClass, _class.name(), ChangeType.Assign);
		PlayerModificationData modificationData = new PlayerModificationData(ModificationType.Change, _args);
		modifyAction.data(modificationData);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(_player);
		ExecutionParameters params = new ExecutionParameters(players);
		
		assertEquals(_class.<Object>value(), _player.attributes().get(2).<Object>value());
		modifyAction.execute(params);
		assertEquals(_changeClass, _player.attributes().get(2).value());
		
		_args = new ModificationArgs(ModificationObject.Attribute, _addHp, _hp.name(), ChangeType.Add);
		modificationData = new PlayerModificationData(ModificationType.Change, _args);
		modifyAction.data(modificationData);
		
		assertEquals(_defaultHp, _player.attributes().get(0).<Object>value());
		modifyAction.execute(params);
		assertEquals(2000, _player.attributes().get(0).<Object>value());
		
		_args = new ModificationArgs(ModificationObject.Attribute, _removeMp, _mp.name(), ChangeType.Subtract);
		modificationData = new PlayerModificationData(ModificationType.Change, _args);
		modifyAction.data(modificationData);
		
		assertEquals(_mp.<Object>value(), _player.attributes().get(1).<Object>value());
		modifyAction.execute(params);
		assertEquals(60, _player.attributes().get(1).<Object>value());
	}
	
	@Test
	public void testAddEquipment() {
		_player = generatePlayer();
		
		ModifyPlayerAction modifyAction = new ModifyPlayerAction(_player.name());
		_args = new ModificationArgs(ModificationObject.Equipment, _bracelet, _arm);
		PlayerModificationData modificationData = new PlayerModificationData(ModificationType.Add, _args);
		modifyAction.data(modificationData);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(_player);
		ExecutionParameters params = new ExecutionParameters(players);
		
		assertTrue(!_player.equipment().equipted().contains(_bracelet));
		modifyAction.execute(params);
		assertTrue(_player.equipment().equipted().contains(_bracelet));
	}

	@Test
	public void testRemoveEquipment() {
		_player = generatePlayer();
		
		ModifyPlayerAction modifyAction = new ModifyPlayerAction(_player.name());
		_args = new ModificationArgs(ModificationObject.Equipment, null, _head);
		PlayerModificationData modificationData = new PlayerModificationData(ModificationType.Remove, _args);
		modifyAction.data(modificationData);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(_player);
		ExecutionParameters params = new ExecutionParameters(players);
		
		assertTrue(_player.equipment().equipted().contains(_clothHat));
		modifyAction.execute(params);
		assertTrue(!_player.equipment().equipted().contains(_clothHat));		
	}
	
	@Test
	public void testChangeEquipment() {
		_player = generatePlayer();
		
		ModifyPlayerAction modifyAction = new ModifyPlayerAction(_player.name());
		_args = new ModificationArgs(ModificationObject.Equipment, _catHat, _head, ChangeType.Assign);
		PlayerModificationData modificationData = new PlayerModificationData(ModificationType.Change, _args);
		modifyAction.data(modificationData);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(_player);
		ExecutionParameters params = new ExecutionParameters(players);
		
		assertEquals(_player.equipment().equipted(_head), _clothHat);
		modifyAction.execute(params);
		assertEquals(_player.equipment().equipted(_head), _catHat);
		
		_args = new ModificationArgs(ModificationObject.Equipment, _breakable, _head, ChangeType.Add);
		modificationData = new PlayerModificationData(ModificationType.Change, _args);
		modifyAction.data(modificationData);
		
		assertTrue(!_player.equipment().equipted(_head).properties().contains(_breakable));
		modifyAction.execute(params);
		assertTrue(_player.equipment().equipted(_head).properties().contains(_breakable));
		
		_args = new ModificationArgs(ModificationObject.Equipment, _breakable, _head, ChangeType.Subtract);
		modificationData = new PlayerModificationData(ModificationType.Change, _args);
		modifyAction.data(modificationData);
		
		assertTrue(_player.equipment().equipted(_head).properties().contains(_breakable));
		modifyAction.execute(params);
		assertTrue(!_player.equipment().equipted(_head).properties().contains(_breakable));
	}
	
	@Test
	public void testAddItem() {
		_player = generatePlayer();
		
		ModifyPlayerAction modifyAction = new ModifyPlayerAction(_player.name());
		_args = new ModificationArgs(ModificationObject.Inventory, 1, _key);
		PlayerModificationData modificationData = new PlayerModificationData(ModificationType.Add, _args);
		modifyAction.data(modificationData);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(_player);
		ExecutionParameters params = new ExecutionParameters(players);
		
		assertTrue(!_player.inventory().items().contains(_key));
		modifyAction.execute(params);
		assertTrue(_player.inventory().items().contains(_key));
	}
	
	@Test
	public void testRemoveItem() {
		_player = generatePlayer();
		
		ModifyPlayerAction modifyAction = new ModifyPlayerAction(_player.name());
		_args = new ModificationArgs(ModificationObject.Inventory, _potion);
		PlayerModificationData modificationData = new PlayerModificationData(ModificationType.Remove, _args);
		modifyAction.data(modificationData);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(_player);
		ExecutionParameters params = new ExecutionParameters(players);
		
		assertTrue(_player.inventory().items().contains(_potion));
		modifyAction.execute(params);
		assertTrue(!_player.inventory().items().contains(_potion));
	}
	
	@Test
	public void testChangeItem() {
		_player = generatePlayer();
		
		ModifyPlayerAction modifyAction = new ModifyPlayerAction(_player.name());
		_args = new ModificationArgs(ModificationObject.Inventory, 12, _potion.name(), ChangeType.Assign);
		PlayerModificationData modificationData = new PlayerModificationData(ModificationType.Change, _args);
		modifyAction.data(modificationData);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(_player);
		ExecutionParameters params = new ExecutionParameters(players);
		
		assertEquals(_player.inventory().getAmount(_potion), 5);
		modifyAction.execute(params);
		assertEquals(_player.inventory().getAmount(_potion), 12);
		
		_args = new ModificationArgs(ModificationObject.Inventory, 12, _potion.name(), ChangeType.Add);
		modificationData = new PlayerModificationData(ModificationType.Change, _args);
		modifyAction.data(modificationData);
		
		assertEquals(_player.inventory().getAmount(_potion), 12);
		modifyAction.execute(params);
		assertEquals(_player.inventory().getAmount(_potion), 24);
		
		_args = new ModificationArgs(ModificationObject.Inventory, 20, _potion.name(), ChangeType.Subtract);
		modificationData = new PlayerModificationData(ModificationType.Change, _args);
		modifyAction.data(modificationData);
		
		assertEquals(_player.inventory().getAmount(_potion), 24);
		modifyAction.execute(params);
		assertEquals(_player.inventory().getAmount(_potion), 4);
		
		_args = new ModificationArgs(ModificationObject.Inventory, _consumable, _potion.name(), ChangeType.Add);
		modificationData = new PlayerModificationData(ModificationType.Change, _args);
		modifyAction.data(modificationData);
		
		assertTrue(!_player.inventory().items().get(0).properties().contains(_consumable));
		modifyAction.execute(params);
		assertTrue(_potion.properties().contains(_consumable));
		//assertTrue(_player.inventory().items().get(0).properties().contains(_consumable));
		
		_args = new ModificationArgs(ModificationObject.Inventory, _consumable, _potion.name(), ChangeType.Subtract);
		modificationData = new PlayerModificationData(ModificationType.Change, _args);
		modifyAction.data(modificationData);
		
		assertTrue(_potion.properties().contains(_consumable));
		//assertTrue(_player.inventory().items().get(0).properties().contains(_consumable));
		modifyAction.execute(params);
		assertTrue(!_player.inventory().items().get(0).properties().contains(_consumable));
	}
	
	@Test
	public void testAddBodyPart() {
		_player = generatePlayer();
		
		ModifyPlayerAction modifyAction = new ModifyPlayerAction(_player.name());
		_args = new ModificationArgs(ModificationObject.BodyPart, _feet);
		PlayerModificationData modificationData = new PlayerModificationData(ModificationType.Add, _args);
		modifyAction.data(modificationData);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(_player);
		ExecutionParameters params = new ExecutionParameters(players);
		
		assertTrue(!_player.bodyParts().contains(_feet));
		modifyAction.execute(params);
		assertTrue(_player.bodyParts().contains(_feet));
	}
	
	@Test
	public void testRemoveBodyPart() {
		_player = generatePlayer();
		
		ModifyPlayerAction modifyAction = new ModifyPlayerAction(_player.name());
		_args = new ModificationArgs(ModificationObject.BodyPart, _arm);
		PlayerModificationData modificationData = new PlayerModificationData(ModificationType.Remove, _args);
		modifyAction.data(modificationData);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(_player);
		ExecutionParameters params = new ExecutionParameters(players);
		
		assertTrue(_player.bodyParts().contains(_arm));
		modifyAction.execute(params);
		assertTrue(!_player.bodyParts().contains(_arm));		
	}

	@Test
	public void testChangeBodyPart() {
		_player = generatePlayer();
		
		ModifyPlayerAction modifyAction = new ModifyPlayerAction(_player.name());
		_args = new ModificationArgs(ModificationObject.BodyPart, _hairColor, _head.name(), ChangeType.Add);
		PlayerModificationData modificationData = new PlayerModificationData(ModificationType.Change, _args);
		modifyAction.data(modificationData);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(_player);
		ExecutionParameters params = new ExecutionParameters(players);
		
		assertTrue(!_player.bodyParts().get(2).getCharacteristics().contains(_hairColor));
		modifyAction.execute(params);
		assertTrue(_player.bodyParts().get(2).getCharacteristics().contains(_hairColor));
		
		_args = new ModificationArgs(ModificationObject.BodyPart, _hairColor, _head.name(), ChangeType.Subtract);
		modificationData = new PlayerModificationData(ModificationType.Change, _args);
		modifyAction.data(modificationData);
		
		assertTrue(_player.bodyParts().get(2).getCharacteristics().contains(_hairColor));
		modifyAction.execute(params);
		assertTrue(!_player.bodyParts().get(2).getCharacteristics().contains(_hairColor));
	}

	private IPlayer generatePlayer() {
		IInventory inv = new Inventory();
		_potion.description("Restores HP");
		_potion.addProperty(new Property("HPUP", "+50"));
		
		_ether.description("Restores MP");
		_ether.addProperty(new Property("MPUP", "+20"));
		
		inv.addItem(_potion, 5);
		inv.addItem(_ether, 3);
		
		IEquipment equip = new Equipment();
		_clothHat.addProperty(new Property("Armor", "AC+12"));
		_armor.addProperty(new Property("Armor", "AC+2"));
		
		equip.equip(_head, _clothHat);
		equip.equip(_body, _armor);
		
		List<IAttribute> attribs = new ArrayList<IAttribute>();
		attribs.add(_hp);
		attribs.add(_mp);
		
		List<ICharacteristic> chars = new ArrayList<ICharacteristic>();
		chars.add(_age);
		
		List<IBodyPart> bparts = new ArrayList<IBodyPart>();
		List<ICharacteristic> armChars = new ArrayList<ICharacteristic>();
		armChars.add(new Characteristic("scar", "deep"));
		armChars.add(new Characteristic("color", "white"));
		_arm.addCharacteristics(armChars);
		bparts.add(_arm);
		bparts.add(_body);
		bparts.add(_head);
		
		//TODO: Challenge for playerlib. Should adding a bodypart update equipment?
		return new Player(_defaultName, null, inv, equip, attribs, chars, bparts);
	}
}
