package textadventurelib.persistence;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.ValueTypes;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.actions.IAction;
import textadventurelib.actions.ModifyPlayerAction;
import textadventurelib.core.ChangeType;
import textadventurelib.core.ModificationArgs;
import textadventurelib.core.ModificationObject;
import textadventurelib.core.ModificationType;
import textadventurelib.core.PlayerModificationData;
import textadventurelib.persistence.player.AttributePersistenceObject;
import textadventurelib.persistence.player.BodyPartPersistenceObject;
import textadventurelib.persistence.player.CharacteristicPersistenceObject;
import textadventurelib.persistence.player.ItemPersistenceObject;
import textadventurelib.persistence.player.PropertyPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 * 
 *  Creates an XML node for the Append Text action. The current xml should look like
 * 	this:
 * 
 * <pre>
 * {@code
 *  <Action type="ModifyPlayer">
 *  	<Parameters>
 *  		<ModificationObject>Characteristic/Attribute/BodyPart/item/...</ModificationObject>
 *  		<Data>What to modify it with or to</Data>
 *  		<ID>What are we modifying (ex. age)</ID>
 *  		<ChangeType>Assign/Add/Subtract</ChangeType>
 *  		<ModificationType>Change/Add/Remove</ModificationType>
 *  	</Parameters>
 *  </Action>
 *  }
 *  </pre>
 *  
 *  minimally required parameters for this to work correctly are:
 *  ModificationObject, Data and ChangeType.
 */
public class ModifyPlayerActionPersistence extends ActionPersistenceObject{
	
	private static final String MOD_TYPE = "ModifyPlayer";
	private static final String MOD_OBJECT_TYPE = "ModificationObject";
	private static final String DATA_TYPE = "Data";
	private static final String ID_TYPE = "ID";
	private static final String CHANGE_TYPE_NAME = "ChangeType";
	private static final String MODIFICATION_TYPE_NAME = "ModificationType";
	private static final String PLAYER_NAME = "PlayerName";
	
	private XmlConfigurationObject modObject;
	private XmlConfigurationObject data;
	private XmlConfigurationObject id;
	private XmlConfigurationObject changeType;
	private XmlConfigurationObject modificationType;
	private XmlConfigurationObject player;
	
	/**
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public ModifyPlayerActionPersistence() throws TransformerConfigurationException, ParserConfigurationException {
		super(MOD_TYPE);
		
		modObject = new XmlConfigurationObject(MOD_OBJECT_TYPE);
		data = new XmlConfigurationObject(DATA_TYPE);
		id = new XmlConfigurationObject(ID_TYPE);
		changeType = new XmlConfigurationObject(CHANGE_TYPE_NAME);
		modificationType = new XmlConfigurationObject(MODIFICATION_TYPE_NAME);
		player = new XmlConfigurationObject(PLAYER_NAME);
	}
	
	/**
	 * 
	 * @param obj The new @see ModificationObject to associate with this player action.
	 */
	public void modificationObj(ModificationObject obj) {
		super.removeParameter(modObject);
		modObject.value(obj);
		super.addParameter(modObject);
	}
	
	/**
	 * 
	 * @return The current @see ModificationObject assoicated with this player action.
	 */
	public ModificationObject modificationObj() {
		return modObject.value();
	}
	
	/**
	 * 
	 * @param data The new data to modify the player with.
	 */
	public <T> void data(T data) {
		super.removeParameter(this.data);
		this.data.value(data);
		super.addParameter(this.data);
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @return The data associated with this player action.
	 */
	public <T> T data() {
		return (T)data.value();
	}
	
	/**
	 * 
	 * @param id The new id for this modify player action.
	 */
	public <T> void id(T id) {
		super.removeParameter(this.id);
		this.id.value(id);
		super.addParameter(this.id);
	}
	
	/**
	 * 
	 * @return The id associated with this player action.
	 */
	public <T> T id() {
		return id.value();
	}
	
	/**
	 * 
	 * @param type The new @see ChangeType to associate with this player action.
	 */
	public void changeType(ChangeType type) {
		super.removeParameter(changeType);
		changeType.value(type);
		super.addParameter(changeType);
	}
	
	/**
	 * 
	 * @return The @see ChangeType associated with this player action.
	 */
	public ChangeType changeType() {
		return changeType.value();
	}
	
	/**
	 * 
	 * @param type The new @see ModificationType to assoicate with this player action.
	 */
	public void modificationType(ModificationType type) {
		super.removeParameter(modificationType);
		modificationType.value(type);
		super.addParameter(modificationType);
	}
	
	/**
	 * 
	 * @return The @see ModificationType assoicated with this player action.
	 */
	public ModificationType modificationType() {
		return modificationType.value();
	}
	
	//TODO: Document
	public String playerName() {
		return player.value();
	}
	
	public void playerName(String name) {
		super.removeParameter(player);
		player.value(name);
		super.addParameter(player);
	}
	
	@Override
	public void prepareXml() throws TransformerConfigurationException, ParserConfigurationException {
		prepareData();
		prepareId();
		super.prepareXml();
	}
	
	private void prepareData() {
		if (data() instanceof AttributePersistenceObject) {
			AttributePersistenceObject att = (AttributePersistenceObject)data();
			att.prepareXml();
		} else if (data() instanceof CharacteristicPersistenceObject) {
			CharacteristicPersistenceObject charc = (CharacteristicPersistenceObject)data();
			charc.prepareXml();
		} else if (data() instanceof PropertyPersistenceObject) {
			PropertyPersistenceObject prop = (PropertyPersistenceObject)data();
			prop.prepareXml();
		} else if (data() instanceof BodyPartPersistenceObject) {
			BodyPartPersistenceObject body = (BodyPartPersistenceObject)data();
			body.prepareXml();
		} else if (data() instanceof ItemPersistenceObject) {
			ItemPersistenceObject item = (ItemPersistenceObject)data();
			item.prepareXml();
		}
	}
	
	private void prepareId() {
		if (id() instanceof ItemPersistenceObject) {
			ItemPersistenceObject item = (ItemPersistenceObject)id();
			item.prepareXml();
		} else if (id() instanceof BodyPartPersistenceObject) {
			BodyPartPersistenceObject bodyPart = (BodyPartPersistenceObject)id();
			bodyPart.prepareXml();
		}
	}
	
	@Override
	public IAction convertToAction() {
		ModifyPlayerAction action = new ModifyPlayerAction(playerName());
		ModificationArgs args = new ModificationArgs(modificationObj());
		setActionData(args);
		setId(args);
		args.changeType(changeType());
		PlayerModificationData data = new PlayerModificationData(modificationType(), args);
		action.data(data);
		return action;
	}
	
	private void setId(ModificationArgs args) {
		if (id() instanceof ItemPersistenceObject) {
			ItemPersistenceObject item = (ItemPersistenceObject)id();
			args.identifier(item.convertToItem());
		} else if(id() instanceof BodyPartPersistenceObject) {
			BodyPartPersistenceObject bodyPart = (BodyPartPersistenceObject)id();
			args.identifier(bodyPart.convertToBodyPart());
		} else {
			args.identifier(id());
		}
	}
	
	private void setActionData(ModificationArgs args) {
		if (data() instanceof AttributePersistenceObject) {
			AttributePersistenceObject att = (AttributePersistenceObject)data();
			args.data(att.convertToAttribute());
		} else if (data() instanceof CharacteristicPersistenceObject) {
			CharacteristicPersistenceObject charc = (CharacteristicPersistenceObject)data();
			args.data(charc.convertToCharacteristic());
		} else if (data() instanceof PropertyPersistenceObject) {
			PropertyPersistenceObject prop = (PropertyPersistenceObject)data();
			args.data(prop.convertToProperty());
		} else if (data() instanceof BodyPartPersistenceObject) {
			BodyPartPersistenceObject body = (BodyPartPersistenceObject)data();
			args.data(body.convertToBodyPart());
		} else if (data() instanceof ItemPersistenceObject) {
			ItemPersistenceObject item = (ItemPersistenceObject)data();
			args.data(item.convertToItem());
		} else {
			args.data(data());
		}
	}
	
	public void convertFromPersistence(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			if (cChild.name().equalsIgnoreCase("Parameters")) {
				convert(cChild);
				return;
			}
		}
	}
	
	private void convert(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			switch (cChild.name()) {
				case PLAYER_NAME:
					playerName(cChild.<String>value());
					break;
				case MODIFICATION_TYPE_NAME:
					modificationType(convertToModificationType(cChild.<String>value()));
					break;
				case MOD_OBJECT_TYPE:
					modificationObj(convertToModificationObject(cChild.<String>value()));
					break;
				case DATA_TYPE:
					convertData(cChild);
					break;
				case ID_TYPE:
					convertId(cChild);
					break;
				case CHANGE_TYPE_NAME:
					changeType(convertToChangeType(cChild.<String>value()));
					break;
				default:
					//TODO: What should be done here?
					break;
			}
		}
	}
	
	private ChangeType convertToChangeType(String type) {
		switch (type) {
			case "Add":
				return ChangeType.Add;
			case "Assign":
				return ChangeType.Assign;
			case "Subtract":
				return ChangeType.Subtract;
			default:
				return ChangeType.Add;
		}
	}
	
	private ModificationType convertToModificationType(String type) {
		switch (type) {
			case "Add":
				return ModificationType.Add;
			case "Remove":
				return ModificationType.Remove;
			case "Change":
				return ModificationType.Change;
			default:
				return ModificationType.Add;	
		}
	}
	
	private ModificationObject convertToModificationObject(String obj) {
		switch (obj) {
			case "Player":
				return ModificationObject.Player;
			case "Attribute":
				return ModificationObject.Attribute;
			case "Characteristic":
				return ModificationObject.Characteristic;
			case "BodyPart":
				return ModificationObject.BodyPart;
			case "Inventory":
				return ModificationObject.Inventory;
			case "Equipment":
				return ModificationObject.Equipment;
			default:
				return ModificationObject.Player;
		}
	}
	
	private void convertData(XmlConfigurationObject obj) {
		String type = obj.getValueType();
		
		if (type.equals(ValueTypes.StringType)) {
			data(obj.<String>value());
		}
		else if (type.equals(ValueTypes.BooleanType)) {
			data(obj.<Boolean>value());
		}
		else if (type.equals(ValueTypes.IntegerType)) {
			data(obj.<Integer>value());
		}
		else if (type.equals(ValueTypes.FloatType)) {
			data(obj.<Float>value());
		}
		else if (type.equals(ValueTypes.DoubleType)) {
			data(obj.<Double>value());
		}
		else {
			if (!attemptPlayerDataConversion(obj)) {
				data(obj.<Object>value());
			}
		}
	}
	
	private void convertId(XmlConfigurationObject obj) {
		String type = obj.getValueType();
		
		if (type.equals(ValueTypes.StringType)) {
			id(obj.<String>value());
		}
		else if (type.equals(ValueTypes.BooleanType)) {
			id(obj.<Boolean>value());
		}
		else if (type.equals(ValueTypes.IntegerType)) {
			id(obj.<Integer>value());
		}
		else if (type.equals(ValueTypes.FloatType)) {
			id(obj.<Float>value());
		}
		else if (type.equals(ValueTypes.DoubleType)) {
			id(obj.<Double>value());
		}
		else {
			if (!attemptPlayerIdConversion(obj)) {
				id(obj.<Object>value());
			}
		}
	}
	
	private boolean attemptPlayerDataConversion(XmlConfigurationObject obj) {
		try {
			if (obj.children().size() < 1) {
				return false;
			}
			
			XmlConfigurationObject firstChild = (XmlConfigurationObject)obj.children().get(0);
			if (firstChild.name().equals("BodyPart")) {
				BodyPartPersistenceObject bodyPart = new BodyPartPersistenceObject();
				bodyPart.convertFromPersistence(firstChild);
				data(bodyPart);
				return true;
			}
		
			if (firstChild.name().equals("Item")) {
				ItemPersistenceObject item = new ItemPersistenceObject();
				item.convertFromPersistence(firstChild);
				data(item);
				return true;
			}
		
			if (!firstChild.name().equals("NamedObject")) {
				return false;
			}
			
			switch (firstChild.configurationProperties().get(0).value()) {
				case "Attribute":
					AttributePersistenceObject att = new AttributePersistenceObject();
					att.convertFromPersistence(firstChild);
					data(att);
					break;
				case "Characteristic":
					CharacteristicPersistenceObject characteristic = new CharacteristicPersistenceObject();
					characteristic.convertFromPersistence(firstChild);
					data(characteristic);
					break;
				case "Property":
					PropertyPersistenceObject property = new PropertyPersistenceObject();
					property.convertFromPersistence(firstChild);
					data(property);
					break;
				default:
					return false;
			}
		} catch (Exception e) {
			//TODO:
			return false;
		}
		
		return true;
	}
	
	private boolean attemptPlayerIdConversion(XmlConfigurationObject obj) {
		try {
			if (obj.children().size() < 1) {
				return false;
			}
			
			XmlConfigurationObject firstChild = (XmlConfigurationObject)obj.children().get(0);
			if (firstChild.name().equals("BodyPart")) {
				BodyPartPersistenceObject bodyPart = new BodyPartPersistenceObject();
				bodyPart.convertFromPersistence(firstChild);
				id(bodyPart);
				return true;
			}
		
			if (firstChild.name().equals("Item")) {
				ItemPersistenceObject item = new ItemPersistenceObject();
				item.convertFromPersistence(firstChild);
				id(item);
				return true;
			}
		
			if (!firstChild.name().equals("NamedObject")) {
				return false;
			}
			
			switch (firstChild.configurationProperties().get(0).value()) {
				case "Attribute":
					AttributePersistenceObject att = new AttributePersistenceObject();
					att.convertFromPersistence(firstChild);
					id(att);
					break;
				case "Characteristic":
					CharacteristicPersistenceObject characteristic = new CharacteristicPersistenceObject();
					characteristic.convertFromPersistence(firstChild);
					id(characteristic);
					break;
				case "Property":
					PropertyPersistenceObject property = new PropertyPersistenceObject();
					property.convertFromPersistence(firstChild);
					id(property);
					break;
				default:
					return false;
			}
		} catch (Exception e) {
			//TODO:
			return false;
		}
		
		return true;
	}
}
