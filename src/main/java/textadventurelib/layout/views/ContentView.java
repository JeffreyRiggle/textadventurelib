package textadventurelib.layout.views;

import ilusr.core.interfaces.ISuspend;
import ilusr.core.mvpbase.IView;
import textadventurelib.layout.ContentType;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface ContentView extends IView, ISuspend{
	/**
	 * 
	 * @param file The file to display in this view.
	 * @param type The type of file to display.
	 */
	void content(String file, ContentType type);
}
