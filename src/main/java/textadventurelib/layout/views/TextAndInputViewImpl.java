package textadventurelib.layout.views;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import ilusr.core.interfaces.ISuspend;
import ilusr.core.mvpbase.IView;
import ilusr.core.mvpbase.IViewListener;
import ilusr.core.mvpbase.ViewBase;
import ilusr.logrunner.LogRunner;
import textadventurelib.layout.views.inputviews.IInputView;
import textadventurelib.layout.views.inputviews.TextBoxInputView;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TextAndInputViewImpl extends JPanel implements IView, IInputListener, ISuspend{

	private static final long serialVersionUID = 1L;
	private JTextArea textLog;
	private IInputView input;
	private ViewBase notifier;
	
	/**
	 * Creates a text and input view.
	 */
	public TextAndInputViewImpl() {
		textLog = new JTextArea();
		textLog.setEditable(false);
		textLog.setSize(getWidth() - 5, 300);
		
		JScrollPane scroller = new JScrollPane(textLog, 
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		notifier = new ViewBase();
		
		BorderLayout layout = new BorderLayout();
		layout.setHgap(25);
		layout.setVgap(25);
		this.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));
		this.setLayout(layout);
		this.add(scroller, BorderLayout.CENTER);
	}
	
	/**
	 * 
	 * @param text The text to show in the view.
	 */
	public void textLog(String text) {
		textLog.setText(text);
	}
	
	/**
	 * 
	 * @param inputView The new input view.
	 */
	public void inputView(IInputView inputView) {
		Container view = (Container)input;
		
		if (view != null) {
			remove(view);
			input.removeListener(this);
		}
		
		input = inputView;
		view = (Container)input;
		
		if (view != null) {
			add((Container)input, BorderLayout.SOUTH);
			input.addListener(this);
		}
		
		revalidate();
		repaint();
	}

	@Override
	public void addListener(IViewListener listener) {
		notifier.add(listener);
	}

	@Override
	public void removeListener(IViewListener listener) {
		notifier.remove(listener);
	}

	@Override
	public void inputSent(String input) {
		notifier.raiseViewChanged("inputSent", input);
	}

	@Override
	public void suspend() {
		// No op.
	}

	@Override
	public void animate() {
		//Make sure to run on the ui thread.
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				textLog.setCaretPosition(textLog.getDocument().getLength());
				
				if (input instanceof TextBoxInputView) {
					LogRunner.logger().info("Setting focus to text input view");
					((Container)input).requestFocusInWindow();
				}
			}
		});
	}
}
