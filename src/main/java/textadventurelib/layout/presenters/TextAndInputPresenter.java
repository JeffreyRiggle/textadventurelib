package textadventurelib.layout.presenters;

import java.awt.Container;

import ilusr.core.mvpbase.PresenterBase;
import ilusr.logrunner.LogRunner;
import textadventurelib.core.IMessageListener;
import textadventurelib.layout.TextAdventureLayout;
import textadventurelib.layout.models.TextAndInputModel;
import textadventurelib.layout.views.TextAndInputViewImpl;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TextAndInputPresenter extends PresenterBase implements TextAdventureLayout{

	private TextAndInputViewImpl view;
	private TextAndInputModel model;
	
	/**
	 * 
	 * @param view The view to bind with the model.
	 * @param model The model to bind to the view.
	 */
	public TextAndInputPresenter(TextAndInputViewImpl view, TextAndInputModel model) {
		super(view, model);
		this.view = view;
		this.model = model;
		
		this.view.textLog(this.model.textLog());
		this.view.inputView(this.model.inputView());
	}
	
	/**
	 * 
	 * @return The text and input view.
	 */
	public TextAndInputViewImpl view() {
		return this.view;
	}
	
	/**
	 * 
	 * @return The text and input model.
	 */
	public TextAndInputModel model() {
		return model;
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
		model.textLog(text);
	}

	@Override
	public String textLog() {
		return model.textLog();
	}

	@Override
	public void animate() {
		LogRunner.logger().info("Animating text and input view.");
		view.animate();
	}

	@Override
	public void suspend() {
		LogRunner.logger().info("Suspending text and input view.");
		model.reset();
		view.suspend();		
	}
}
