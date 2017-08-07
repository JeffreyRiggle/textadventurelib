package textadventurelib.layout.presenters;

import java.awt.Container;
import java.util.logging.Level;

import ilusr.core.mvpbase.PresenterBase;
import ilusr.logrunner.LogRunner;
import textadventurelib.core.IMessageListener;
import textadventurelib.layout.TextAdventureLayout;
import textadventurelib.layout.models.ContentOnlyModel;
import textadventurelib.layout.views.ContentOnlyView;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ContentOnlyPresenter extends PresenterBase implements TextAdventureLayout{

	private ContentOnlyModel model;
	private ContentOnlyView view;
	
	/**
	 * 
	 * @param model The model to bind with the view.
	 * @param view The view to bind to the model.
	 */
	public ContentOnlyPresenter(ContentOnlyModel model, ContentOnlyView view) {
		super(view, model);
		this.model = model;
		this.view = view;
		
		this.view.content(this.model.file(), this.model.type());
	}

	@Override
	public Container container() {
		return (Container)view;
	}

	@Override
	public void messageListener(IMessageListener listener) {
		model.messageListener(listener);
	}

	@Override
	public void textLog(String text) {
		// No op	
	}

	@Override
	public String textLog() {
		//No op
		return new String();
	}

	@Override
	public void animate() {
		LogRunner.logger().log(Level.INFO, "Animating content view.");
		view.animate();
	}

	@Override
	public void suspend() {
		LogRunner.logger().log(Level.INFO, "Suspending content view.");
		view.suspend();
	}
}
