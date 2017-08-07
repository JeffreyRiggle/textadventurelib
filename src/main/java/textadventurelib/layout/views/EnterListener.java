package textadventurelib.layout.views;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class EnterListener implements KeyListener {
	private INotifyOnEnter listener;
	
	public EnterListener(INotifyOnEnter listener) {
		this.listener = listener;
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			listener.notifing();
			arg0.consume();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// No op
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// No op
	}
}
