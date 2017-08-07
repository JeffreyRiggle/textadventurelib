package textadventurelib.core;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface ICompletionListener {
	/**
	 * 
	 * @param data The completion data.
	 */
	<T> void completed(T data);
}