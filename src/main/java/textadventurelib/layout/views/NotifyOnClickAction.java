package textadventurelib.layout.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class NotifyOnClickAction implements ActionListener{

	private INotifyOnClick listener;
	private String input;
	
	/**
	 * 
	 * @param listener The listener to notify.
	 * @param input The input to send.
	 */
	public NotifyOnClickAction(INotifyOnClick listener, String input) {
		this.listener = listener;
		this.input = input;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		listener.notify(input);
	}
}
