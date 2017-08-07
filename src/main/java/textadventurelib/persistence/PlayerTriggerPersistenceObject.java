package textadventurelib.persistence;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.ValueTypes;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.core.Condition;
import textadventurelib.core.ModificationObject;
import textadventurelib.core.PlayerConditionParameters;
import textadventurelib.triggers.ITrigger;
import textadventurelib.triggers.PlayerConditionTrigger;

/**
 * 
 * @author Jeff Riggle
 *
 * Generates Xml in the current format.
 * 
 * <pre>
 * {@code
 * <Trigger type="Player">
 *	<Parameters>
 *	 <ModificationObject>Player,Attribute,etc.</ModificationObject>
 *	 <ID>Key</ID>
 *	 <Condition>gt, lt, equal, not equal, has, has not(to add?)</Condition>
 *	 <DataMember>The data to compare against(Piped |)</DataMember>
 *	 <ComparisionData>The expected data</ComparisonData>
 *	</Parameters>
 * </Trigger>
 * }
 * </pre>
 */
public class PlayerTriggerPersistenceObject extends TriggerPersistenceObject{

	private static final String PLAYER_TYPE = "Player";
	private static final String MOD_OBJECT_NAME = "ModificationObject";
	private static final String ID_NAME = "ID";
	private static final String CONDITION_NAME = "Condition";
	private static final String DATAMEMBER_NAME = "DataMember";
	private static final String COMPARISIONDATA_NAME = "ComparisionData";
	private static final String PLAYER_NAME = "PlayerName";
	private final String deliminator = "|,";
	private final String escapedDeliminator = "\\|,";
	
	private XmlConfigurationObject modObject;
	private XmlConfigurationObject id;
	private XmlConfigurationObject condition;
	private XmlConfigurationObject dataMember;
	private XmlConfigurationObject comparisonData;
	private XmlConfigurationObject player;
	
	/**
	 * Ctor.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public PlayerTriggerPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(PLAYER_TYPE);
		
		modObject = new XmlConfigurationObject(MOD_OBJECT_NAME, ModificationObject.Player);
		id = new XmlConfigurationObject(ID_NAME);
		condition = new XmlConfigurationObject(CONDITION_NAME, Condition.EqualTo);
		dataMember = new XmlConfigurationObject(DATAMEMBER_NAME);
		comparisonData = new XmlConfigurationObject(COMPARISIONDATA_NAME);
		player = new XmlConfigurationObject(PLAYER_NAME, new String());
	}
	
	/**
	 * 
	 * @param mod The new @see ModificationObject to associate with this player trigger.
	 */
	public void modificationObject(ModificationObject mod) {
		super.removeParameter(modObject);
		modObject.value(mod);
		super.addParameter(modObject);
	}
	
	/**
	 * 
	 * @return The @see ModificationObject associated with this player trigger.
	 */
	public ModificationObject modificationObject() {
		return modObject.value();
	}
	
	/**
	 * 
	 * @param id The new id of the property to compare against (example: age).
	 */
	public void id(String[] id) {
		super.removeParameter(this.id);
		this.id.value(arrayToString(id));
		super.addParameter(this.id);
	}
	
	/**
	 * 
	 * @return The id of the property to compare against.
	 */
	public String[] id() {
		String[] retVal;
		if (id.<String>value() != null ) {
			retVal = id.<String>value().split(escapedDeliminator);
		}else {
			retVal = new String[0];
		}
		
		return retVal;
	}
	
	/**
	 * 
	 * @param cond a new @see Condition to associate with this player trigger.
	 */
	public void condition(Condition cond) {
		super.removeParameter(condition);
		condition.value(cond);
		super.addParameter(condition);
	}
	
	/**
	 * 
	 * @return The @see Condition associated with this player trigger.
	 */
	public Condition condition() {
		return condition.value();
	}
	
	/**
	 * 
	 * @param dataMember The new data member to associate with this player trigger.
	 * 	data member should be piped (example: potion|value).
	 */
	public void dataMember(String dataMember) {
		super.removeParameter(this.dataMember);
		this.dataMember.value(dataMember);
		super.addParameter(this.dataMember);
	}
	
	/**
	 * 
	 * @return The data member associated with this player trigger.
	 */
	public String dataMember() {
		return dataMember.value();
	}
	
	/**
	 * 
	 * @param data The new data to compare against (example player.age > 13).
	 */
	public <T> void comparisonData(T data) {
		super.removeParameter(comparisonData);
		comparisonData.value(data);
		super.addParameter(comparisonData);
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @return The data to compare against.
	 */
	public <T> T comparisonData() {
		return (T)comparisonData.value();
	}
	
	/**
	 * 
	 * @param player The new player name.
	 */
	public void playerName(String player) {
		super.removeParameter(this.player);
		this.player.value(player);
		super.addParameter(this.player);
	}
	
	/**
	 * 
	 * @return The players name.
	 */
	public String playerName() {
		return player.value();
	}
	
	@Override
	public void prepareXml() throws TransformerConfigurationException, ParserConfigurationException {
		super.prepareXml();
	}
	
	@Override
	public ITrigger convertToTrigger() {
		PlayerConditionParameters params = new PlayerConditionParameters();
		params.comparisionData(comparisonData());
		params.condition(condition());
		params.dataMember(dataMember());
		params.modificationObject(modificationObject());
		params.id(id());
		PlayerConditionTrigger trigger = new PlayerConditionTrigger(playerName(), params);
		return trigger;
	}
	
	public void convertFromPersistence(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			switch (cChild.name()) {
				case "Parameters":
					convert(cChild);
					break;
				default:
					break;
			}
		}
	}
	
	private String arrayToString(String[] array) {
		String retVal = new String();
		
		for (int i = 0; i < array.length; i++) {
			retVal = retVal + array[i] + deliminator;
		}
		
		return retVal;
	}
	
	private void convert(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			switch (cChild.name()) {
			case MOD_OBJECT_NAME:
				modificationObject(convertToModificationObject(cChild.<String>value()));
				break;
			case ID_NAME:
				String[] val = cChild.<String>value().split(escapedDeliminator);
				id(val);
				break;
			case CONDITION_NAME:
				condition(convertToCondition(cChild.<String>value()));
				break;
			case DATAMEMBER_NAME:
				dataMember(cChild.<String>value());
				break;
			case COMPARISIONDATA_NAME:
				convertComparisonData(cChild);
				break;
			case PLAYER_NAME:
				playerName(cChild.<String>value());
				break;
			default:
				break;
			}
		}
	}
	
	private void convertComparisonData(XmlConfigurationObject obj) {
		String type = obj.getValueType();
		
		if (type.equals(ValueTypes.StringType)) {
			comparisonData(obj.<String>value());
		}
		else if (type.equals(ValueTypes.BooleanType)) {
			comparisonData(obj.<Boolean>value());
		}
		else if (type.equals(ValueTypes.IntegerType)) {
			comparisonData(obj.<Integer>value());
		}
		else if (type.equals(ValueTypes.FloatType)) {
			comparisonData(obj.<Float>value());
		}
		else if (type.equals(ValueTypes.DoubleType)) {
			comparisonData(obj.<Double>value());
		}
		else {
			comparisonData(obj.<Object>value());
		}
	}
	
	private ModificationObject convertToModificationObject(String modName) {
		switch (modName) {
			case "Player":
				return ModificationObject.Player;
			case "Attribute":
				return ModificationObject.Attribute;
			case "Characteristic":
				return ModificationObject.Characteristic;
			case "Inventory":
				return ModificationObject.Inventory;
			case "Equipment":
				return ModificationObject.Equipment;
			case "BodyPart":
				return ModificationObject.BodyPart;
			default:
				//TODO What to do here?
				return ModificationObject.Player;
		}
	}
	
	private Condition convertToCondition(String condition) {
		switch (condition) {
			case "GreaterThan":
				return Condition.GreaterThan;
			case "EqualTo":
				return Condition.EqualTo;
			case "Has":
				return Condition.Has;
			case "LessThan":
				return Condition.LessThan;
			case "NotEqual":
				return Condition.NotEqual;
			default:
				//TODO: what to do here.
				return Condition.EqualTo;
		}
	}
}
