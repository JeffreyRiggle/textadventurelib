package textadventurelib.layout.views;

import ilusr.core.mvpbase.IViewListener;
import ilusr.core.mvpbase.ViewBase;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ContentOnlyView extends ContentViewImpl {

	private static final long serialVersionUID = 1L;
	private ViewBase notifier;
	
	/**
	 * Creates a content only view.
	 */
	public ContentOnlyView() {
		super();
		notifier = new ViewBase();
	}
	
	@Override
	public void completed() {
		notifier.raiseViewChanged("complete", null);
	}
	
	@Override
	public void addListener(IViewListener listener) {
		notifier.add(listener);
	}
	
	@Override
	public void removeListener(IViewListener listener) {
		notifier.remove(listener);
	}
}
