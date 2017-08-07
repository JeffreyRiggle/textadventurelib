package textadventurelib.layout.models;

import ilusr.core.mvpbase.ModelBase;
import textadventurelib.core.IMessageListener;
import textadventurelib.layout.views.inputviews.IInputView;
import textadventurelib.layout.views.inputviews.TextBoxInputView;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TextAndInputModel extends ModelBase{

	private static final long serialVersionUID = 1L;
	private String textLog;
	private IInputView input;
	private IMessageListener messageListener;
	private String initialText;
	
	/**
	 * Creates a text an input model.
	 */
	public TextAndInputModel() {
		this(new String(), new TextBoxInputView());
	}
	
	/**
	 * 
	 * @param textLog The text log.
	 */
	public TextAndInputModel(String textLog) {
		this(textLog, new TextBoxInputView());
	}
	
	/**
	 * 
	 * @param textLog The text log.
	 * @param input The input view to use.
	 */
	public TextAndInputModel(String textLog, IInputView input) {
		this.textLog = textLog;
		this.initialText = textLog;
		this.input = input;
	}
	
	/**
	 * Resets layout to initial text.
	 */
	public void reset() {
		textLog(initialText);
	}
	
	/**
	 * 
	 * @return The text log.
	 */
	public String textLog() {
		return textLog;
	}
	
	/**
	 * 
	 * @param text The new text log.
	 */
	public void textLog(String text) {
		if (textLog == text) return;
		
		textLog = text;
		super.raiseModelChanged("textLog", text);
	}
	
	/**
	 * 
	 * @return The input view.
	 */
	public IInputView inputView() {
		return input;
	}
	
	/**
	 * 
	 * @param view The new input view.
	 */
	public void inputView(IInputView view) {
		if (input == view) return;
		
		input = view;
		super.raiseModelChanged("inputView", input);
	}
	
	/**
	 * 
	 * @param listener The message listener to apply to this model.
	 */
	public void messageListener(IMessageListener listener) {
		messageListener = listener;
	}
	
	/**
	 * 
	 * @param input The input to process.
	 */
	public void inputSent(String input) {
		if (messageListener == null) return;
		
		//TODO: what if we overflow our string?
		textLog(textLog + "\n" + input);
		messageListener.sendMessage(input);
	}
}
