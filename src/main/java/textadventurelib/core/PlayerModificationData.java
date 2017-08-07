package textadventurelib.core;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayerModificationData {

	private ModificationType modificationType;
	private ModificationArgs modification;
	
	/**
	 * 
	 * @param type The @see ModificationType to preform.
	 * @param modification The @see ModificationArgs for the modification.
	 */
	public PlayerModificationData(ModificationType type, ModificationArgs modification) {
		this.modificationType = type;
		this.modification = modification;
	}
	
	/**
	 * 
	 * @return The @see ModificationType to preform.
	 */
	public ModificationType modificationType() {
		return modificationType;
	}
	
	/**
	 * 
	 * @return The @see ModificationArgs for the modification.
	 */
	public ModificationArgs args() {
		return modification;
	}
}
