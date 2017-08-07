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
import textadventurelib.macro.MacroParameters;
import textadventurelib.macro.PlayerMacroManager;

public class PlayerMacroManagerUnitTest {

	private IPlayer _player;
	private MacroParameters _parameters1 = new MacroParameters("\\{\\[", "\\]\\}", "\\(", "\\)", "@");
	
	private final String _playerSub1 = "Player name: {[player(John)@attribute(DisplayName)@value]} Player age: {[player(John)@characteristic(age)@value]}";
	private final String _expectedResult1 = "Player name: John Player age: 25";
	private final String _expectedResultAfterUpdate1 = "Player name: Deemo Player age: 13";
	
	private final String _playerSub2 = "Player name: {[player(John)@name]} Player HP: {[player(John)@attribute(HP)@value]}, MP: {[player(John)@attribute(MP)@value]}";
	private final String _expectedResult2 = "Player name: John Player HP: 200, MP: 100";
	
	private final String _playerSub3 = "Player name: {[player(John)@name]} BodyPart {[player(John)@bodyPart(Arm)@name]}: {[player(John)@bodyPart(Arm)@characteristic(scar)@value]}, {[player(John)@bodyPart(Arm)@characteristic(color)@value]}";
	private final String _expectedResult3 = "Player name: John BodyPart Arm: deep, white";
	
	private final String _playerSub4 = "Player name: {[player(John)@name]} Item {[player(John)@inventory(potion)@name]} with Property {[player(John)@inventory(potion)@property(HPUP)@value]}";
	private final String _expectedResult4 = "Player name: John Item potion with Property +50";
	
	private final String _playerSub5 = "Player name: {[player(John)@name]} Headgear {[player(John)@equipment(head)@name]} body {[player(John)@equipment(body)@name]}";
	private final String _expectedResult5 = "Player name: John Headgear cloth hat body armor";

	private final String _multilineSub = "Player name: {[player(John)@name]} \nHeadgear {[player(John)@equipment(head)@name]} \nbody {[player(John)@equipment(body)@name]}";
	private final String _multilineResult = "Player name: John \nHeadgear cloth hat \nbody armor";
	
	private final String _noSubTest1 = "This is a basic String no subsititution should happen here.";
	private final String _noSubTest2 = "This is a {[test]} test string [with@some] special (chars)";
	
	private final String _caseTestSub = "Player name: {[Player(John)@attribute(DisplayName)@value]} \nHeadgear {[pLaYeR(John)@EqUiPmEnT(head)@name]} \nbody {[PLAYER(John)@equipMENT(body)@name]}";
	private final String _caseTestResult = "Player name: John \nHeadgear cloth hat \nbody armor";
	
	@Test
	public void testBasic() {
		_player = generatePlayer();
		
		PlayerMacroManager manager = new PlayerMacroManager(_parameters1);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(_player);
		manager.players(players);
		
		String result = manager.substitute(_playerSub1);
		assertEquals(_expectedResult1, result);
		
		String result2 = manager.substitute(_playerSub2);
		assertEquals(_expectedResult2, result2);
		
		String result3 = manager.substitute(_playerSub3);
		assertEquals(_expectedResult3, result3);
		
		String result4 = manager.substitute(_playerSub4);
		assertEquals(_expectedResult4, result4);
		
		String result5 = manager.substitute(_playerSub5);
		assertEquals(_expectedResult5, result5);
	}

	@Test
	public void testWithUpdate() {
		_player = generatePlayer();
		
		PlayerMacroManager manager = new PlayerMacroManager(_parameters1);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(_player);
		manager.players(players);
		
		String result = manager.substitute(_playerSub1);
		assertEquals(_expectedResult1, result);
		
		_player.attributes().get(2).value("Deemo");
		_player.characteristics().get(0).value("13");
		
		String result1 = manager.substitute(_playerSub1);
		assertEquals(_expectedResultAfterUpdate1, result1);
	}
	
	@Test
	public void testMultiline() {
		_player = generatePlayer();
		
		PlayerMacroManager manager = new PlayerMacroManager(_parameters1);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(_player);
		manager.players(players);
		
		String result = manager.substitute(_multilineSub);
		assertEquals(_multilineResult, result);
	}
	
	@Test
	public void testNoSubstitutions() {
		_player = generatePlayer();
		
		PlayerMacroManager manager = new PlayerMacroManager(_parameters1);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(_player);
		manager.players(players);
		
		String result = manager.substitute(_noSubTest1);
		assertEquals(_noSubTest1, result);
		
		String result2 = manager.substitute(_noSubTest2);
		assertEquals(_noSubTest2, result2);
	}
	
	@Test
	public void testCaseSubstitution() {
		_player = generatePlayer();
		
		PlayerMacroManager manager = new PlayerMacroManager(_parameters1);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(_player);
		manager.players(players);
		
		String result = manager.substitute(_caseTestSub);
		assertEquals(_caseTestResult, result);
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
		IBodyPart head = new BodyPart("head");
		
		equip.equip(head, headgear);
		equip.equip(body, armor);
		
		List<IAttribute> attribs = new ArrayList<IAttribute>();
		attribs.add(new Attribute("HP", 200));
		attribs.add(new Attribute("MP", 100));
		attribs.add(new Attribute("DisplayName", "John"));
		
		List<ICharacteristic> chars = new ArrayList<ICharacteristic>();
		chars.add(new Characteristic("age", "25"));
		
		List<IBodyPart> bparts = new ArrayList<IBodyPart>();
		List<ICharacteristic> armChars = new ArrayList<ICharacteristic>();
		armChars.add(new Characteristic("scar", "deep"));
		armChars.add(new Characteristic("color", "white"));
		bparts.add(new BodyPart("Arm", "Players Arm", armChars));
		bparts.add(body);
		bparts.add(head);
		
		//TODO: Challenge for playerlib. Should adding a bodypart update equipment?
		return new Player("John", null, inv, equip, attribs, chars, bparts);
	}
}
