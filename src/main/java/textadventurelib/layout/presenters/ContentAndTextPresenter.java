package textadventurelib.layout.presenters;

import java.awt.Container;
import java.util.logging.Level;

import ilusr.core.mvpbase.PresenterBase;
import ilusr.logrunner.LogRunner;
import textadventurelib.core.IMessageListener;
import textadventurelib.layout.TextAdventureLayout;
import textadventurelib.layout.models.ContentAndTextModel;
import textadventurelib.layout.views.ContentAndTextView;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ContentAndTextPresenter extends PresenterBase implements TextAdventureLayout{

	private ContentAndTextView view;
	private ContentAndTextModel model;
	
	/**
	 * 
	 * @param model The model to bind to the view.
	 * @param view The view to bind with the model.
	 */
	public ContentAndTextPresenter(ContentAndTextModel model, ContentAndTextView view) {
		super(view, model);
		this.view = view;
		this.model = model;
		
		new TextAndInputPresenter(this.view.textView(), this.model.textModel());
		this.view.textView().textLog(this.model.textModel().textLog());
		this.view.textView().inputView(this.model.textModel().inputView());
		this.view.contentView().content(this.model.contentModel().file(), this.model.contentModel().type());
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
		model.textModel().textLog(text);
	}
	
	@Override
	public String textLog() {
		return model.textModel().textLog();
	}

	@Override
	public void animate() {
		LogRunner.logger().log(Level.INFO, "Animating content and text view");
		view.animate();
	}

	@Override
	public void suspend() {
		LogRunner.logger().log(Level.INFO, "Suspending content and text view");
		model.textModel().reset();
		view.suspend();
	}
}
