package textadventurelib.layout.fxviews;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import textadventurelib.layout.LayoutView;
import textadventurelib.layout.TextAdventureLayoutModel;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class TextInputView extends AnchorPane implements Initializable, LayoutView {

	@FXML
	private TextField input;
	
	private TextAdventureLayoutModel model;
	
	/**
	 * Creates a text input view.
	 */
	public TextInputView() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TextInputView.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		input.setOnKeyReleased((e) -> {
			if (e.getCode() == KeyCode.ENTER) {
				sendMessage();
				e.consume();
			}
		});
	}

	private void sendMessage() {
		if (model == null) {
			return;
		}
		
		model.sendMessage(input.getText());
		input.setText("");
	}
	
	@Override
	public void suspend() {
		input.setText("");
	}

	@Override
	public void animate() {
		input.requestFocus();
	}

	@Override
	public void setModel(TextAdventureLayoutModel model) {
		this.model = model;
	}
}
