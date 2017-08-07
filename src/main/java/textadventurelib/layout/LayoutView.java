package textadventurelib.layout;

import ilusr.core.interfaces.ISuspend;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface LayoutView extends ISuspend {
	/**
	 * 
	 * @param model The model to associate with the view.
	 */
	void setModel(TextAdventureLayoutModel model);
}
