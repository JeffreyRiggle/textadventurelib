package textadventurelib.layout.models;

import ilusr.core.mvpbase.ModelBase;
import textadventurelib.layout.ContentType;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ContentModel extends ModelBase {

	private static final long serialVersionUID = 1L;
	private String file;
	private ContentType type;
	
	/**
	 * Creates a base content model with no file or type.
	 */
	public ContentModel() {
		this(null, null);
	}
	
	/**
	 * 
	 * @param file The location of the file to display.
	 * @param type The type of file (image/video).
	 */
	public ContentModel(String file, ContentType type) {
		this.type = type;
		this.file = file;
	}
	
	/**
	 * 
	 * @param file Sets the file to display.
	 */
	public void file(String file) {
		if (this.file == file) return;
		
		this.file = file;
	}
	
	/**
	 * 
	 * @return The location of the file that is being displayed.
	 */
	public String file() {
		return file;
	}
	
	/**
	 * 
	 * @param type The new type of file.
	 */
	public void type(ContentType type) {
		if (this.type == type) return;
		
		this.type = type;
	}
	
	/**
	 * 
	 * @return The type associated with the current file.
	 */
	public ContentType type() {
		return type;
	}	
}