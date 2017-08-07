package textadventurelib.layout.views;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface IInputListener {
	/**
	 * TODO: Should this take in a T or something other than string?
	 * @param input The String received from the input.
	 */
	void inputSent(String input);
}
