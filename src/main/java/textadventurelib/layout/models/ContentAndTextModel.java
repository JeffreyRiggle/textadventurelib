package textadventurelib.layout.models;

import ilusr.core.mvpbase.ModelBase;
import textadventurelib.core.IMessageListener;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ContentAndTextModel extends ModelBase{

	private static final long serialVersionUID = 1L;

	private TextAndInputModel textModel;
	private ContentModel contentModel;
	
	/**
	 * 
	 * @param contentModel The content model.
	 * @param textModel The text model.
	 */
	public ContentAndTextModel(ContentModel contentModel, TextAndInputModel textModel) {
		this.contentModel = contentModel;
		this.textModel = textModel;
	}
	
	/**
	 * 
	 * @param listener The listener to apply.
	 */
	public void messageListener(IMessageListener listener) {
		textModel.messageListener(listener);
	}
	
	/**
	 * 
	 * @return The text model.
	 */
	public TextAndInputModel textModel() {
		return textModel;
	}
	
	/**
	 * 
	 * @return The content model.
	 */
	public ContentModel contentModel() {
		return contentModel;
	}
}
