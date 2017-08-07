package textadventurelib.triggers;

import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;

import ilusr.logrunner.LogRunner;
import playerlib.attributes.IAttribute;
import playerlib.characteristics.ICharacteristic;
import playerlib.equipment.IBodyPart;
import playerlib.items.IItem;
import playerlib.items.IProperty;
import playerlib.player.IPlayer;
import textadventurelib.core.PlayerConditionParameters;
import textadventurelib.core.TriggerParameters;

//TODO: Can this be refactored.
/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayerConditionTrigger implements ITrigger{

	private String playerName;
	private IPlayer player;
	private PlayerConditionParameters conditionData;
	
	/**
	 * 
	 * @param player The @see IPlayer to base condition data off of.
	 * @param data The @see PlayerConditionParameters to use.
	 */
	public PlayerConditionTrigger(String playerName, PlayerConditionParameters data) {
		this.playerName = playerName;
		this.conditionData = data;
	}
	
	@Override
	public <T> void condition(T value) {
		conditionData = (PlayerConditionParameters)value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T condition() {
		return (T)conditionData;
	}

	@Override
	public boolean shouldFire(TriggerParameters data) {
		//TODO: How can a change of player name be accounted for?
		setPlayer(data.players());
		boolean retVal = false;
		
		switch (conditionData.modificationObject()) {
			case Attribute:
				retVal = attributeCondition();
				break;
			case Characteristic:
				retVal = characteristicCondition();
				break;
			case BodyPart:
				retVal = bodyPartCondition();
				break;
			case Player:
				retVal = playerCondition();
				break;
			case Inventory:
				retVal = inventoryCondition();
				break;
			case Equipment:
				retVal = equipmentCondition();
				break;
		}
		
		LogRunner.logger().log(Level.INFO, String.format("Trigger condition processing finished with a value of: %s", retVal));
		return retVal;
	}

	private void setPlayer(List<IPlayer> players) {
		for (IPlayer player : players) {
			if (!player.name().equalsIgnoreCase(playerName)) continue;
			
			LogRunner.logger().log(Level.INFO, String.format("Setting player to: %s", player.name()));
			this.player = player;
			break;
		}
	}
	
	/**
	 * 
	 * @return a boolean based off of if the condition data is met.
	 */
	private boolean attributeCondition() {
		LogRunner.logger().log(Level.INFO, "Validating attribute condition");
		boolean retVal = false;
		
		IAttribute attribute = findAttribute(conditionData.id()[0]);
		
		switch(conditionData.condition()) {
			case GreaterThan:
				if (attribute == null) break;
				
				Integer data = reflectForData(attribute, conditionData.dataMember());
				LogRunner.logger().log(Level.INFO, String.format("Seeing if %s is greater than %s", data, conditionData.<Integer>comparisonData()));
				retVal =  data > conditionData.<Integer>comparisonData();
				break;
			case LessThan:
				if (attribute == null) break;
				
				Integer data1 = reflectForData(attribute, conditionData.dataMember());
				LogRunner.logger().log(Level.INFO, String.format("Seeing if %s is less than %s", data1, conditionData.<Integer>comparisonData()));
				retVal = data1 < conditionData.<Integer>comparisonData();
				break;
			case EqualTo:
				if (attribute == null) break;
				
				retVal = conditionData.comparisonData().equals(reflectForData(attribute, conditionData.dataMember()));
				break;
			case NotEqual:
				if (attribute == null) break;
				
				retVal = !conditionData.comparisonData().equals(reflectForData(attribute, conditionData.dataMember()));
				break;
			case Has:
				retVal = attribute != null;
				break;
		}
		
		return retVal;
	}

	//TODO: Should this be a static helper.
	/**
	 * 
	 * @param id A @see String representing an attribute
	 * @return A @see IAttribute belonging to the player, if 
	 * 	no attribute is found null will be returned.
	 */
	private IAttribute findAttribute(String id) {
		IAttribute attribute = null;
		
		for (IAttribute att : player.attributes()) {
			if (!att.name().equalsIgnoreCase(id)) continue;
			attribute = att;
		}
		
		return attribute;
	}
	
	/**
	 * 
	 * @return a boolean based off of if the condition data is met.
	 */
	private boolean characteristicCondition() {
		LogRunner.logger().log(Level.INFO, "Validating characteristic condition");
		boolean retVal = false;
		
		ICharacteristic characteristic = findCharacteristic(conditionData.id()[0]);
		
		switch(conditionData.condition()) {
			case GreaterThan:
				if (characteristic == null) break;
				
				Integer data = reflectForData(characteristic, conditionData.dataMember());
				LogRunner.logger().log(Level.INFO, String.format("Seeing if %s is greater than %s", data, conditionData.<Integer>comparisonData()));
				retVal =  data > conditionData.<Integer>comparisonData();
				break;
			case LessThan:
				if (characteristic == null) break;
				
				Integer data1 = reflectForData(characteristic, conditionData.dataMember());
				LogRunner.logger().log(Level.INFO, String.format("Seeing if %s is less than %s", data1, conditionData.<Integer>comparisonData()));
				retVal = data1 < conditionData.<Integer>comparisonData();
				break;
			case EqualTo:
				if (characteristic == null) break;
				
				retVal = conditionData.comparisonData().equals(reflectForData(characteristic, conditionData.dataMember()));
				break;
			case NotEqual:
				if (characteristic == null) break;
				
				retVal = !conditionData.comparisonData().equals(reflectForData(characteristic, conditionData.dataMember()));
				break;
			case Has:
				retVal = characteristic != null;
				break;
		}
		
		return retVal;
	}

	//TODO: Should this be a static helper.
	/**
	 * 
	 * @param id A @see String representing a characteristic.
	 * @return A @see ICharacteristic belonging to the player, if 
	 * 	no characteristic is found null will be returned.
	 */	
	private ICharacteristic findCharacteristic(String id) {
		ICharacteristic characteristic = null;
		
		for (ICharacteristic character : player.characteristics()) {
			if (!character.name().equalsIgnoreCase(id)) continue;
			characteristic = character;
		}
		
		return characteristic;
	}

	/**
	 * 
	 * @return a boolean based off of if the condition data is met.
	 */
	private boolean bodyPartCondition() {
		LogRunner.logger().log(Level.INFO, "Validating body part condition");
		boolean retVal = false;
		
		IBodyPart bodyPart = findBodyPart(conditionData.id()[0]);
		ICharacteristic characteristic = null;
		
		if (conditionData.id().length >= 2) {
			characteristic = bodyPart.getCharacteristic(conditionData.id()[1]);	
		}
		
		switch(conditionData.condition()) {
			case GreaterThan:
				if (bodyPart == null || characteristic == null) break;
				
				Integer data = reflectForData(characteristic, conditionData.dataMember());
				LogRunner.logger().log(Level.INFO, String.format("Seeing if %s is greater than %s", data, conditionData.<Integer>comparisonData()));
				retVal =  data > conditionData.<Integer>comparisonData();
				break;
			case LessThan:
				if (bodyPart == null || characteristic == null) break;
				
				Integer data1 = reflectForData(characteristic, conditionData.dataMember());
				LogRunner.logger().log(Level.INFO, String.format("Seeing if %s is less than %s", data1, conditionData.<Integer>comparisonData()));
				retVal = data1 < conditionData.<Integer>comparisonData();
				break;
			case EqualTo:
				if (bodyPart == null) break;
				
				if (characteristic != null) {
					retVal = conditionData.comparisonData().equals(reflectForData(characteristic, conditionData.dataMember()));
					break;
				}
				
				retVal = conditionData.comparisonData().equals(reflectForData(bodyPart, conditionData.dataMember()));
				break;
			case NotEqual:
				if (bodyPart == null) break;
				
				if (characteristic != null) {
					retVal = !conditionData.comparisonData().equals(reflectForData(characteristic, conditionData.dataMember()));
					break;
				}
				retVal = !conditionData.comparisonData().equals(reflectForData(bodyPart, conditionData.dataMember()));
				break;
			case Has:
				retVal = bodyPart != null;
				break;
		}
		
		return retVal;
	}

	//TODO: Should this be a static helper.
	/**
	 * 
	 * @param id A @see String representing a body part.
	 * @return A @see IBodyPart belonging to the player, if 
	 * 	no body part is found null will be returned.
	 */
	private IBodyPart findBodyPart(String id) {
		IBodyPart bodyPart = null;
		
		for (IBodyPart bPart : player.bodyParts()) {
			if (!bPart.name().equalsIgnoreCase(id)) continue;
			bodyPart = bPart;
		}
		
		return bodyPart;
	}
	
	/**
	 * 
	 * @return a boolean based off of if the condition data is met.
	 */
	private boolean inventoryCondition() {
		LogRunner.logger().log(Level.INFO, "Validating inventory condition");
		boolean retVal = false;
		
		IItem item = findInventoryItem(conditionData.id()[0]);
		IProperty property = null;
		
		switch(conditionData.condition()) {
			case GreaterThan:
				if (item == null) break;
				
				if (conditionData.id().length < 2) {
					Integer data = player.inventory().getAmount(item);
					retVal =  data > conditionData.<Integer>comparisonData();
					break;
				}
				
				property = findProperty(item, conditionData.id()[1]);
				if (property == null) break;
				
				Integer pNum = reflectForData(property, conditionData.dataMember());
				LogRunner.logger().log(Level.INFO, String.format("Seeing if %s is greater than %s", pNum, conditionData.<Integer>comparisonData()));
				retVal = pNum > conditionData.<Integer>comparisonData();
				break;
			case LessThan:
				if (item == null) break;
				
				if (conditionData.id().length < 2) {
					Integer data = player.inventory().getAmount(item);
					retVal =  data < conditionData.<Integer>comparisonData();
					break;
				}
				
				property = findProperty(item, conditionData.id()[1]);
				if (property == null) break;
				
				Integer num = reflectForData(property, conditionData.dataMember());
				LogRunner.logger().log(Level.INFO, String.format("Seeing if %s is less than %s", num, conditionData.<Integer>comparisonData()));
				retVal = num < conditionData.<Integer>comparisonData();
				break;
			case EqualTo:
				if (item == null) break;
				
				//Must be something off of the item
				if (conditionData.id().length < 2) {
					retVal = conditionData.comparisonData().equals(reflectForData(item, conditionData.dataMember()));
					break;
				}
				
				property = findProperty(item, conditionData.id()[1]);
				if (property == null) break;
				
				retVal = conditionData.comparisonData().equals(reflectForData(property, conditionData.dataMember()));
				break;
			case NotEqual:
				if (item == null) break;
				
				//Must be something off of the item
				if (conditionData.id().length < 2) {
					retVal = !conditionData.comparisonData().equals(reflectForData(item, conditionData.dataMember()));
					break;
				}
				
				property = findProperty(item, conditionData.id()[1]);
				if (property == null) break;
				
				retVal = !conditionData.comparisonData().equals(reflectForData(property, conditionData.dataMember()));
				break;
			case Has:
				retVal = item != null;
				break;
		}
		
		return retVal;
	}

	//TODO: Should this be a static helper.
	/**
	 * 
	 * @param id A @see String representing an item.
	 * @return A @see IItem belonging to the player, if 
	 * 	no item is found null will be returned.
	 */
	private IItem findInventoryItem(String id) {
		IItem item = null;
		
		for (IItem itm : player.inventory().items()) {
			if (!itm.name().equalsIgnoreCase(id)) continue;
			item = itm;
		}
		
		return item;
	}
	
	/**
	 * 
	 * @param item The @see IItem to find the property on.
	 * @param id The name of the property to find.
	 * @return A @see IProperty based off of the id. If no property is found null will be returned.
	 */
	private IProperty findProperty(IItem item, String id) {
		IProperty prop = null;
		
		for (IProperty property : item.properties()) {
			if (!property.name().equalsIgnoreCase(id)) continue;
			prop = property;
		}
		
		return prop;
	}
	
	/**
	 * 
	 * @return a boolean based off of if the condition data is met.
	 */
	private boolean equipmentCondition() {
		LogRunner.logger().log(Level.INFO, "Validating equipment condition");
		boolean retVal = false;
		
		IItem item = findEquipmentItem(conditionData.id()[0]);
		IProperty property = null;
		
		switch(conditionData.condition()) {
			case GreaterThan:
				if (item == null) break;
				
				if (conditionData.id().length < 2) break;
				
				property = findProperty(item, conditionData.id()[1]);
				if (property == null) break;
				
				Integer pNum = reflectForData(property, conditionData.dataMember());
				LogRunner.logger().log(Level.INFO, String.format("Seeing if %s is greater than %s", pNum, conditionData.<Integer>comparisonData()));
				retVal = pNum > conditionData.<Integer>comparisonData();
				break;
			case LessThan:
				if (item == null) break;
				
				if (conditionData.id().length < 2) break;
				
				property = findProperty(item, conditionData.id()[1]);
				if (property == null) break;
				
				Integer num = reflectForData(property, conditionData.dataMember());
				LogRunner.logger().log(Level.INFO, String.format("Seeing if %s is less than %s", num, conditionData.<Integer>comparisonData()));
				retVal = num < conditionData.<Integer>comparisonData();
				break;
			case EqualTo:
				if (item == null) break;
				
				if (conditionData.id().length < 2) {
					retVal = conditionData.comparisonData().equals(reflectForData(item, conditionData.dataMember()));
					break;
				}
				
				property = findProperty(item, conditionData.id()[1]);
				if (property == null) break;
				
				retVal = conditionData.comparisonData().equals(reflectForData(property, conditionData.dataMember()));
				break;
			case NotEqual:
				if (item == null) break;
				
				if (conditionData.id().length < 2) {
					retVal = !conditionData.comparisonData().equals(reflectForData(item, conditionData.dataMember()));
					break;
				}
				
				property = findProperty(item, conditionData.id()[1]);
				if (property == null) break;
				
				retVal = !conditionData.comparisonData().equals(reflectForData(property, conditionData.dataMember()));
				break;
			case Has:
				retVal = item != null;
				break;
		}
		
		return retVal;
	}

	//TODO: Should this be a static helper.
	/**
	 * 
	 * @param id A @see String representing an item.
	 * @return A @see IItem belonging to the player, if 
	 * 	no attribute is found null will be returned.
	 */
	private IItem findEquipmentItem(String id) {
		IItem item = null;
		
		for (IItem itm : player.equipment().equipted()) {
			if (!itm.name().equalsIgnoreCase(id)) continue;
			item = itm;
		}
		
		return item;
	}
	
	/**
	 * 
	 * @return a boolean based off of if the condition data is met.
	 */
	private boolean playerCondition() {
		LogRunner.logger().log(Level.INFO, "Validating player condition");
		boolean retVal = false;
		
		switch (conditionData.condition()) {
			case EqualTo:
				retVal = conditionData.comparisonData() == reflectForData(player, conditionData.dataMember());
				break;
			case NotEqual:
				retVal = conditionData.comparisonData() != reflectForData(player, conditionData.dataMember());
				break;
			default:
				//TODO: Add an error or something here.
				break;
		}
		
		return retVal;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @param dataObject The Object to reflect on to get the data.
	 * @param method The name of the method to call.
	 * @return A @see String representing the methods result.
	 */
	private <T> T reflectForData(Object dataObject, String method) {
		T retVal = null;
		try {
			Method func = dataObject.getClass().getMethod(method);
			if (func != null) {
				func.setAccessible(true);
				retVal = (T)func.invoke(dataObject);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}
}
