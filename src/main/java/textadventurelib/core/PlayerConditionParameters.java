package textadventurelib.core;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayerConditionParameters {

	private Condition condition;
	private ModificationObject object;
	private String[] id;
	private String dataMember;
	private Object comparisionData;
	
	/**
	 * Empty constructor.
	 */
	public PlayerConditionParameters() {
		
	}
	
	/**
	 * 
	 * @param condition The new @see Condition to associated with this player condition.
	 */
	public void condition(Condition condition) {
		this.condition = condition;
	}
	
	/**
	 * 
	 * @return The @see Condition associated with this player condition.
	 */
	public Condition condition() {
		return condition;
	}
	
	/**
	 * 
	 * @param object The @see ModificationObject to associate with this player condition.
	 */
	public void modificationObject(ModificationObject object) {
		this.object = object;
	}
	
	/**
	 * 
	 * @return The @see ModificationObject associated with this player condition.
	 */
	public ModificationObject modificationObject() {
		return object;
	}
	
	//TODO: Can this be reworked.
	/**
	 * This needs to be elaborated on.
	 * @param id The new id to be associated with this player condition.
	 */
	public void id(String[] id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return The id associated with this player condition.
	 */
	public String[] id() {
		return id;
	}
	
	/**
	 * This needs to be elaborated on.
	 * @param dataMember The data member to pull data from.
	 */
	public void dataMember(String dataMember) {
		this.dataMember = dataMember;
	}
	
	/**
	 * 
	 * @return The data member associated with this player condition.
	 */
	public String dataMember() {
		return dataMember;
	}
	
	/**
	 * 
	 * @param data The new data to compare against.
	 */
	public <T> void comparisionData(T data) {
		comparisionData = data;
	}
	
	@SuppressWarnings("unchecked")	
	/**
	 * 
	 * @return The data to compare against.
	 */
	public <T> T comparisonData() {
		return (T)comparisionData;
	}
}
