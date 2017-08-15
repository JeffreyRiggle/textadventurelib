package textadventurelib.layout.fxviews;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.logrunner.LogRunner;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import textadventurelib.layout.LayoutView;
import textadventurelib.layout.TextAdventureLayoutModel;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TextOnlyView extends AnchorPane implements Initializable, LayoutView {

	@FXML
	private TextArea text;
	
	private TextAdventureLayoutModel model;
	private ChangeListener<? super String> scrollListener;
	
	private boolean initialized;
	
	/**
	 * Creates a text only view
	 */
	public TextOnlyView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TextOnlyView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}

	@Override
	public void suspend() {
		tearDown();
	}

	@Override
	public void animate() {
		setup();
	}

	@Override
	public void setModel(TextAdventureLayoutModel model) {
		tearDown();
		this.model = model;
		setup();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initialized = true;
		scrollListener = (v, o, n) -> {
			scrollToBottom();
		};
		
		setup();
	}
	
	private void setup() {
		if (model == null || !initialized) {
			return;
		}
		
		text.textProperty().addListener(scrollListener);
		text.textProperty().bind(model.textLog());
	}
	
	private void tearDown() {
		if (model == null || !initialized) {
			return;
		}
		
		text.textProperty().removeListener(scrollListener);
		text.textProperty().unbind();
	}
	
	private void scrollToBottom() {
		Platform.runLater(() -> {
			text.setEditable(true);
			text.positionCaret(text.getText().length());
			text.setEditable(false);
		});
	}
}
