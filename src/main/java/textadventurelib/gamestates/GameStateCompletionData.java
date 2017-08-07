package textadventurelib.gamestates;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameStateCompletionData {

	private Object completionData;
	private Object extraData;
	
	/**
	 * 
	 * @param completionData The data to complete the state with.
	 * @param extraData The extra data to send along with the state change.
	 */
	public <T, D>GameStateCompletionData(T completionData, D extraData) {
		this.completionData = completionData;
		this.extraData = extraData;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @return The data to complete the state with.
	 */
	public <T> T completionData() {
		return (T)completionData;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @return The extra data to send along with the state change.
	 */
	public <D> D extraData() {
		return (D)extraData;
	}
}
