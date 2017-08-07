package textadventurelib.core;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ModificationArgs {

	private Object data;
	private Object identifier;
	private ModificationObject modificationObject;
	private ChangeType changeType;
	
	/**
	 * 
	 * @param obj @see ModificationObject to modify.
	 */
	public ModificationArgs(ModificationObject obj) {
		modificationObject = obj;
	}
	
	/**
	 * 
	 * @param obj @see ModificationObject to modify.
	 * @param data The data to modify with.
	 */
	public <T>ModificationArgs(ModificationObject obj, T data) {
		this.modificationObject = obj;
		this.data = (T)data;
	}
	
	/**
	 * 
	 * @param obj @see ModificationObject to modify.
	 * @param data The data to modify with.
	 * @param id The location of the data to modify.
	 */
	public <T, I>ModificationArgs(ModificationObject obj, T data, I id) {
		this.modificationObject = obj;
		this.data = (T)data;
		this.identifier = (I)id;
	}
	
	/**
	 * 
	 * @param obj @see ModificationObject to modify.
	 * @param data The data to modify with.
	 * @param id The location of the data to modify.
	 * @param type The @see ChangeType for this change.
	 */
	public <T, I>ModificationArgs(ModificationObject obj, T data, I id, ChangeType type) {
		this.modificationObject = obj;
		this.data = (T)data;
		this.identifier = (I)id;
		this.changeType = type;
	}
	
	/**
	 * 
	 * @param object Changes the @see ModificationObject.
	 */
	public void modificationObject(ModificationObject object) {
		modificationObject = object;
	}
	
	/**
	 * 
	 * @return The @see ModificationObject associated with these args.
	 */
	public ModificationObject modificationObject() {
		return modificationObject;
	}
	
	/**
	 * 
	 * @param id The new identifier for these args.
	 */
	public <T> void identifier(T id) {
		identifier = id;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @return The location of the data to modify.
	 */
	public <T> T identifier() {
		return (T)identifier;
	}
	
	/**
	 * 
	 * @param data The new location of the data to modify.
	 */
	public <T> void data(T data) {
		this.data = data;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @return The data to modify.
	 */
	public <T> T data() {
		return (T)data;
	}
	
	/**
	 * 
	 * @param type The @see ChangeType for this change.
	 */
	public void changeType(ChangeType type) {
		changeType = type;
	}
	
	/**
	 * 
	 * @return The @see ChangeType
	 */
	public ChangeType changeType() {
		return changeType;
	}
}