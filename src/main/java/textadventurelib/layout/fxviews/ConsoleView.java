package textadventurelib.layout.fxviews;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import textadventurelib.layout.LayoutView;
import textadventurelib.layout.TextAdventureLayoutModel;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ConsoleView extends AnchorPane implements Initializable, LayoutView {
	
	private TextAdventureLayoutModel model;
	private ChangeListener<? super String> textLogListener;
	private StringProperty prefix;
	
	@FXML
	private TextArea text;
	
	public ConsoleView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ConsoleView.fxml"));
		prefix = new SimpleStringProperty(">");
		loader.setRoot(this);
		loader.setController(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return The prefix to show in the console (ex: >)
	 */
	public String getPrefix() {
		return prefix.get();
	}
	
	/**
	 * 
	 * @return The prefix to show in the console (ex: >)
	 */
	public StringProperty prefix() {
		return prefix;
	}
	
	/**
	 * 
	 * @param prefix The new prefix to show in the console (ex: >)
	 */
	public void setPrefix(String prefix) {
		this.prefix.set(prefix);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		textLogListener = (v, o, n) -> {
			text.textProperty().set(n + "\n" + prefix.get());
			text.positionCaret(text.textProperty().length().get());
		};
		
		setup();
		
		text.setOnKeyPressed((e) -> {
			if (e.getCode() == KeyCode.ENTER) {
				sendInput();
				e.consume();
			} else if (e.getCode() == KeyCode.BACK_SPACE || e.getCode() == KeyCode.LEFT) {
				if (isAtBeginning()){
					e.consume();
				}
			} else if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN) {
				e.consume();
			} else if (e.getCode() == KeyCode.A && e.isControlDown()) {
				e.consume();
			}
		});
		
		text.setOnMouseClicked((e) -> {
			text.positionCaret(text.textProperty().length().get());
			e.consume();
		});
	}
	
	private boolean isAtBeginning() {
		String currentText = text.getText();
		int position = text.getCaretPosition();
		
		return position == 1 || currentText.substring(0, position).endsWith("\n" + prefix.get());
	}

	private void sendInput() {
		if (model == null) {
			return;
		}
		
		int start = text.getText().lastIndexOf("\n" + prefix.get()) + prefix.get().length() + 1;
		model.sendMessage(text.getText().substring(start));
	}
	
	@Override
	public void suspend() {
		model.textLog().removeListener(textLogListener);
		text.textProperty().set("");
	}

	@Override
	public void animate() {
		setup();
	}

	@Override
	public void setModel(TextAdventureLayoutModel model) {
		this.model = model;
		setup();
	}
	
	private void setup() {
		if (model == null) {
			text.textProperty().set("" + prefix.get());
			text.positionCaret(text.textProperty().length().get());
			return;
		}
		
		text.textProperty().set(model.textLog().get() + "\n" + prefix.get());
		text.positionCaret(text.textProperty().length().get());
		
		model.textLog().addListener(textLogListener);
	}
}
