package textadventurelib.layout.models;

import textadventurelib.core.IMessageListener;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ContentOnlyModel extends ContentModel {

	private static final long serialVersionUID = 1L;
	
	private IMessageListener messageListener;
	private String data;
	
	/**
	 * 
	 * @param data The data to complete with.
	 */
	public ContentOnlyModel(String data) {
		super();
		this.data = data;
	}
	
	/**
	 * 
	 * @param listener The message listener to apply to this model.
	 */
	public void messageListener(IMessageListener listener) {
		messageListener = listener;
	}
	
	/**
	 * sends completion data.
	 */
	public void complete() {
		if (messageListener == null) return;
		
		messageListener.sendMessage(data);
	}
}
