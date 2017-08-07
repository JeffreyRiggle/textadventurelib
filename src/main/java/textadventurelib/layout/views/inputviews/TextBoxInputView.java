package textadventurelib.layout.views.inputviews;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

import textadventurelib.layout.views.EnterListener;
import textadventurelib.layout.views.IInputListener;
import textadventurelib.layout.views.INotifyOnEnter;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TextBoxInputView extends JTextArea implements IInputView, INotifyOnEnter{

	private static final long serialVersionUID = 1L;
	
	private List<IInputListener> listeners;
	
	/**
	 * Creates a text box input view.
	 */
	public TextBoxInputView() {
		listeners = new ArrayList<IInputListener>();
		super.addKeyListener(new EnterListener(this));
		
		setSize(getWidth() - 5, 25);
		super.setColumns(1);
	}
	
	@Override
	public void addListener(IInputListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(IInputListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void notifing() {
		for (IInputListener listener : listeners) {
			listener.inputSent(super.getText());
		}
		
		super.setText(new String());
	}
}
