package textadventurelib.actions;

import java.util.List;

import ilusr.logrunner.LogRunner;
import playerlib.attributes.IAttribute;
import playerlib.characteristics.ICharacteristic;
import playerlib.equipment.IBodyPart;
import playerlib.items.IItem;
import playerlib.items.IProperty;
import playerlib.player.IPlayer;
import textadventurelib.core.ExecutionParameters;
import textadventurelib.core.PlayerModificationData;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ModifyPlayerAction implements IAction{

	private IPlayer _player;
	private String _playerName;
	private PlayerModificationData _modificationData;
	
	/**
	 * 
	 * @param player The @see IPlayer to modify.
	 */
	public ModifyPlayerAction(String playerName) {
		_playerName = playerName;
	}
	
	/**
	 * 
	 * @return The @see PlayerModificationData associated with this action.
	 */
	public PlayerModificationData data() {
		return _modificationData;
	}

	/**
	 * 
	 * @param value The new @see PlayerModificationData to assoicate with this action.
	 */
	public void data(PlayerModificationData value) {
		_modificationData = value;
	}

	@Override
	public void execute(ExecutionParameters params) {
		setPlayer(params.players());
		
		switch (_modificationData.args().modificationObject()) {
			case Player:
				playerModification();
				break;
			case Characteristic:
				characteristicModification();
				break;
			case Attribute:
				attributeModification();
				break;
			case Equipment:
				equipmentModification();
				break;
			case Inventory:
				inventoryModification();
				break;
			case BodyPart:
				bodyPartModification();
				break;
		}
	}

	private void setPlayer(List<IPlayer> players) {
		for (IPlayer player : players) {
			LogRunner.logger().info(String.format("Determining if player: %s is equal to: %s ", player.name(), _playerName));
			if (player.name().equalsIgnoreCase(_playerName)) {
				_player = player;
				break;
			}
		}
	}
	
	/**
	 * Modifies the player. Currently this is only used to change the players name.
	 */
	private void playerModification() {
		switch(_modificationData.modificationType()) {
			case Add:
			case Remove:
				LogRunner.logger().warning(String.format("Unable to process modification for %s", _modificationData.modificationType()));
				break;
			case Change:
				LogRunner.logger().info(String.format("Changing player name to: %s", _modificationData.args().<String>data()));
				_player.name(_modificationData.args().<String>data());
				break;
		}
	}
	
	/**
	 * Called with add:
	 * 	adds a characteristic to the player.
	 * Called with remove:
	 * 	removes a characteristic from this player.
	 * Called with Change:
	 * 	Changes the value of the characteristic.
	 */
	private void characteristicModification() {
		switch(_modificationData.modificationType()) {
			case Add:
				LogRunner.logger().info(String.format("Adding Characteristic: %s", _modificationData.args().<ICharacteristic>data().name()));
				_player.addCharacteristic(_modificationData.args().<ICharacteristic>data());
				break;
			case Remove:
				LogRunner.logger().info(String.format("Removing Characteristic: %s", _modificationData.args().<ICharacteristic>data().name()));
				_player.removeCharacteristic(_modificationData.args().<ICharacteristic>data());
				break;
			case Change:
				LogRunner.logger().info(String.format("Changing Characteristic: %s", _modificationData.args().<Object>identifier()));
				for (ICharacteristic characteristic : _player.characteristics()) {
					if (characteristic.name().equals(_modificationData.args().identifier())) {
						characteristicChangeImpl(characteristic, _modificationData.args().data());
					}
				}
				break;
		}
	}

	/**
	 * Called with Assign:
	 * 	Sets the value of the characteristic to the passed in data.
	 * Called with Add:
	 * 	Adds to the value of the characteristic.
	 * Called with Subtract:
	 * 	Subtracts from the value of the characteristic.
	 * @param characteristic The @see ICharacteristic to change.
	 * @param data The data to change it with.
	 */
	private <T>void characteristicChangeImpl(ICharacteristic characteristic, T data) {
		switch (_modificationData.args().changeType()) {
			case Assign:
				LogRunner.logger().finer(String.format("Setting Characteristic: %s to %s", characteristic.name(), data));
				characteristic.value(data);
				break;
			case Add:
				//TODO: I am not sure if I really like this.
				LogRunner.logger().finer(String.format("Adding %s to Characteristic: %s ", data, characteristic.name()));
				if (characteristic.value() == int.class || characteristic.value() instanceof Integer) {
					characteristic.value(characteristic.<Integer>value() + (Integer)data);
				}
				if (characteristic.value() instanceof String) {
					characteristic.value(characteristic.<String>value() + (String)data);
				}
				break;
			case Subtract:
				LogRunner.logger().finer(String.format("Subtracting %s from Characteristic: %s ", data, characteristic.name()));

				//TODO: I am not sure if I really like this.
				if (characteristic.value() == int.class || characteristic.value() instanceof Integer) {
					characteristic.value(characteristic.<Integer>value() - (Integer)data);
				}
				break;
		}
	}
	
	/**
	 * Called with Add:
	 * 	Adds an attribute to the player.
	 * Called with Remove:
	 * 	removes an attribute from the player.
	 * Called with Change:
	 * 	Changes an existing attribute for this player.
	 */
	private void attributeModification() {
		switch(_modificationData.modificationType()) {
			case Add:
				LogRunner.logger().info(String.format("Adding Attribute: %s", _modificationData.args().<IAttribute>data().name()));
				_player.addAttribute(_modificationData.args().<IAttribute>data());
				break;
			case Remove:
				LogRunner.logger().info(String.format("Removing Attribute: %s", _modificationData.args().<IAttribute>data().name()));
				_player.removeAttribute(_modificationData.args().<IAttribute>data());
				break;
			case Change:
				LogRunner.logger().info(String.format("Changing Attribute: %s", _modificationData.args().<Object>identifier()));
				for (IAttribute attribute : _player.attributes()) {
					if (attribute.name().equals(_modificationData.args().identifier())) {
						attributeChangeImpl(attribute, _modificationData.args().data());
					}
				}
				break;
		}
	}

	/**
	 * Called with Assign:
	 * 	Changes the value of the attribute
	 * Called with Add:
	 * 	Adds to the current value of the attribute.
	 * Called with Subtract.
	 * 	Subtracts from the current value of the attribute.
	 * @param attribute The @see IAttribute to change.
	 * @param data The data to change the attribute with.
	 */
	private <T>void attributeChangeImpl(IAttribute attribute, T data) {
		switch (_modificationData.args().changeType()) {
			case Assign:
				LogRunner.logger().info(String.format("Setting Attribute: %s to %s", attribute.name(), data));

				attribute.value(data);
				break;
			case Add:
				LogRunner.logger().info(String.format("Adding %s to Attribute: %s", data, attribute.name()));

				//TODO: I am not sure if I really like this.
				if (attribute.value() == int.class || attribute.value() instanceof Integer) {
					attribute.value(attribute.<Integer>value() + (Integer)data);
				}
				if (attribute.value() instanceof String) {
					attribute.value(attribute.<String>value() + (String)data);					
				}
				break;
			case Subtract:
				LogRunner.logger().info(String.format("Subtracting %s from Attribute: %s", data, attribute.name()));

				//TODO: I am not sure if I really like this.
				if (attribute.value() == int.class || attribute.value() instanceof Integer) {
					attribute.value(attribute.<Integer>value() - (Integer)data);
				}
				break;
		}
	}
	
	/**
	 * Called with Add:
	 * 	Adds an item to an equipment slot.
	 * Called with Remove:
	 * 	Removes from an equipment slot.
	 * Called with Change:
	 * 	Changes the equipped item.
	 */
	private void equipmentModification() {
		switch(_modificationData.modificationType()) {
			case Add:
				LogRunner.logger().info(String.format("Equiping %s to %s", _modificationData.args().<IItem>data(), _modificationData.args().<IBodyPart>identifier()));
				_player.equipment().equip(_modificationData.args().<IBodyPart>identifier(), _modificationData.args().<IItem>data());
				break;
			case Remove:
				LogRunner.logger().info(String.format("Un-equiping %s from %s", _modificationData.args().<IItem>data(), _modificationData.args().<IBodyPart>identifier()));
				_player.equipment().unEquip(_modificationData.args().<IBodyPart>identifier());
				break;
			case Change:
				IItem item = _player.equipment().unEquip(_modificationData.args().<IBodyPart>identifier());
				item = itemChangeImpl(item, _modificationData.args().data());
				LogRunner.logger().info(String.format("Changing equipment for %s to %s", _modificationData.args().<IBodyPart>identifier(), item.name()));
				_player.equipment().equip(_modificationData.args().<IBodyPart>identifier(), item);
				break;
		}		
	}
	
	/**
	 * 
	 * @param item The @see IItem to change.
	 * @param data The data to change the item with.
	 * @return The new @see IItem.
	 */
	private <T> IItem itemChangeImpl(IItem item, T data) {
		switch (_modificationData.args().changeType()) {
			case Assign:
				item = (IItem)data;
				break;
			case Add:
				//TODO: I am not sure if I really like this.
				item.addProperty((IProperty)data);
				break;
			case Subtract:
				//TODO: I am not sure if I really like this.
				item.removeProperty((IProperty)data);
				break;
		}
		return item;
	}
	
	/**
	 * Called with Add:
	 * 	Adds a @see IItem to the players inventory.
	 * Called with Remove:
	 * 	Removes the @see IItem from the players inventory.
	 * Called with Change:
	 * 	Adds to the amount of the item or changes a property on the item.
	 */
	private void inventoryModification() {
		switch(_modificationData.modificationType()) {
			case Add:
				LogRunner.logger().info(String.format("Adding item %s to inventory.", _modificationData.args().<IItem>identifier().name()));
				_player.inventory().addItem(_modificationData.args().<IItem>identifier(), (int)_modificationData.args().data());
				break;
			case Remove:
				LogRunner.logger().info(String.format("Removing item %s from inventory.", _modificationData.args().<IItem>data().name()));
				_player.inventory().removeItem(_modificationData.args().<IItem>data());
				break;
			case Change:
				LogRunner.logger().info(String.format("Changing item %s in inventory.", _modificationData.args().<Object>identifier()));
				for (IItem item : _player.inventory().items()) {
					if (!item.name().equals(_modificationData.args().identifier())) continue;
					if (_modificationData.args().data() instanceof Integer || _modificationData.args().data() == int.class) {
						int newAmount = changeAmount(_player.inventory().getAmount(item), (int)_modificationData.args().data());
						_player.inventory().setAmount(item, newAmount);
						continue;
					}
					item = itemChangeImpl(item, _modificationData.args().data());
				}
				break;
		}
	}

	/**
	 * 
	 * @param originalAmount The original amount of the item.
	 * @param modifier The modification to preform on the item.
	 * @return The new amount.
	 */
	private int changeAmount(int originalAmount, int modifier) {
		int retVal = 0;
		switch (_modificationData.args().changeType()) {
			case Assign:
				retVal = modifier;
				break;
			case Add:
				retVal = originalAmount + modifier;
				break;
			case Subtract:
				retVal = originalAmount - modifier;
				break;
		}
		return retVal;
	}
	
	/**
	 * Called with Add:
	 * 	Adds a @see IBodyPart to the player.
	 * Called with Remove:
	 * 	Removes a @see IBodyPart from the player.
	 * Called with Change:
	 * 	Changes the @see IBodyPart.
	 */
	private void bodyPartModification() {
		switch(_modificationData.modificationType()) {
			case Add:
				LogRunner.logger().info(String.format("Adding Body part %s", _modificationData.args().<IBodyPart>data().name()));
				_player.addBodyPart(_modificationData.args().<IBodyPart>data());
				break;
			case Remove:
				LogRunner.logger().info(String.format("Removing Body part %s", _modificationData.args().<IBodyPart>data().name()));
				_player.removeBodyPart(_modificationData.args().<IBodyPart>data());
				break;
			case Change:
				LogRunner.logger().info(String.format("Changing Body part %s", _modificationData.args().<Object>identifier()));
				for (IBodyPart bPart : _player.bodyParts()) {
					if (bPart.name().equals(_modificationData.args().identifier())) {
						bodyPartChangeImpl(bPart, _modificationData.args().data());
					}
				}
				break;
		}	
	}
	
	/**
	 * 
	 * @param bodyPart The @see IBodyPart to modify.
	 * @param data The data to change the body part with.
	 */
	private <T>void bodyPartChangeImpl(IBodyPart bodyPart, T data) {
		switch (_modificationData.args().changeType()) {
			case Assign:
				bodyPart = (IBodyPart)data;
				break;
			case Add:
				//TODO: I am not sure if I really like this.
				bodyPart.addCharacteristic((ICharacteristic)data);
				break;
			case Subtract:
				//TODO: I am not sure if I really like this.
				bodyPart.removeCharacteristic((ICharacteristic)data);
				break;
		}
	}
}