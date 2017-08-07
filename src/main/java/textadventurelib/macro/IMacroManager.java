package textadventurelib.macro;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface IMacroManager {
	/**
	 * 
	 * @param originalText The text to perform substitutions on.
	 * @return The substituted text.
	 */
	String substitute(String originalText);
}
