package textadventurelib.layout.views.inputviews;

import textadventurelib.layout.views.IInputListener;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface IInputView {
	/**
	 * 
	 * @param listener The listener to add to this input view.
	 */
	void addListener(IInputListener listener);
	/**
	 * 
	 * @param listener The listener to remove from this input view.
	 */
	void removeListener(IInputListener listener);
}
