package textadventurelib.layout.views.inputviews;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import textadventurelib.layout.views.IInputListener;
import textadventurelib.layout.views.INotifyOnClick;
import textadventurelib.layout.views.NotifyOnClickAction;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ButtonInputView extends JPanel implements IInputView, INotifyOnClick{

	private static final long serialVersionUID = 1L;

	private List<IInputListener> listeners;
	private List<JButton> inputButtons;
	
	/**
	 * 
	 * @param buttons The buttons to show.
	 */
	public ButtonInputView(List<JButton> buttons) {
		listeners = new ArrayList<IInputListener>();
		inputButtons = buttons;
		
		GridLayout layout = new GridLayout(1,0);
		this.setLayout(layout);
		
		for (JButton button : inputButtons) {
			button.addActionListener(new NotifyOnClickAction(this, button.getText()));
			this.add(button);
		}
		
		this.revalidate();
		this.repaint();
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
	public void notify(String input) {
		for (IInputListener listener : listeners) {
			listener.inputSent(input);
		}
	}
}
