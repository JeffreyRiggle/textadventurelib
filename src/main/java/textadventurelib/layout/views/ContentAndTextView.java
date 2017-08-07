package textadventurelib.layout.views;

import java.awt.GridLayout;

import javax.swing.JPanel;

import ilusr.core.interfaces.ISuspend;
import ilusr.core.mvpbase.IView;
import ilusr.core.mvpbase.IViewListener;
import ilusr.core.mvpbase.ViewBase;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ContentAndTextView extends JPanel implements IView, ISuspend{

	private static final long serialVersionUID = 1L;
	
	private TextAndInputViewImpl textView;
	private ContentViewImpl contentView;
	private ViewBase notifier;
	
	/**
	 * Creates a content and text view.
	 */
	public ContentAndTextView() {
		notifier = new ViewBase();
		textView = new TextAndInputViewImpl();
		contentView = new ContentViewImpl();
		
		GridLayout layout = new GridLayout(0,1);
		layout.setVgap(25);
		setLayout(layout);
		add(contentView);
		add(textView);
	}

	@Override
	public void addListener(IViewListener listener) {
		notifier.add(listener);
	}

	@Override
	public void removeListener(IViewListener listener) {
		notifier.remove(listener);
	}
	
	/**
	 * 
	 * @return The text view.
	 */
	public TextAndInputViewImpl textView() {
		return textView;
	}
	
	/**
	 * 
	 * @return The content view.
	 */
	public ContentViewImpl contentView() {
		return contentView;
	}

	@Override
	public void suspend() {
		textView.suspend();
		contentView.suspend();
	}

	@Override
	public void animate() {
		textView.animate();
		contentView.animate();
	}
}
