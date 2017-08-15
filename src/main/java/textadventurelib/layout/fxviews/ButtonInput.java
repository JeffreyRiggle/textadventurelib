package textadventurelib.layout.fxviews;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.logrunner.LogRunner;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import textadventurelib.layout.LayoutView;
import textadventurelib.layout.TextAdventureLayoutModel;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ButtonInput extends AnchorPane implements Initializable, LayoutView{

	@FXML
	private GridPane container;
	
	private IntegerProperty maxColumns;
	
	private TextAdventureLayoutModel model;
	private boolean initialized;
	
	/**
	 * Creates a button input.
	 */
	public ButtonInput() {
		maxColumns = new SimpleIntegerProperty(0);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ButtonInput.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}
	
	/**
	 * 
	 * @return The maximum number of columns to display.
	 */
	public IntegerProperty maxColumns() {
		return maxColumns;
	}
	
	/**
	 * 
	 * @return The maximum number of columns to display.
	 */
	public Integer getMaxColumns() {
		return maxColumns.get();
	}
	
	/**
	 * 
	 * @param value The new maximum number of columns to display.
	 */
	public void setMaxColumns(Integer value) {
		maxColumns.set(value);
	}
	
	@Override
	public void suspend() {
		tearDown();
	}

	@Override
	public void animate() {
		setup();
		container.requestFocus();
	}

	@Override
	public void setModel(TextAdventureLayoutModel model) {
		tearDown();
		this.model = model;
		setup();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initialized = true;
		setup();
	}
	
	private void setup() {
		if (model == null || !initialized) {
			return;
		}
		
		int curCol = 0;
		int curRow = 0;
		for (String input : model.inputs()) {
			if (maxColumns.get() == 0 || curCol < maxColumns.get()) {
				container.add(createButton(input), curCol, curRow);
				curCol++;
				continue;
			}
			
			curCol = 0;
			curRow++;
			container.add(createButton(input), curCol, curRow);
			curCol++;
		}
	}
	
	private Button createButton(String input) {
		Button retVal = new Button(input);
		retVal.setOnAction((e) -> {
			if (model != null) {
				model.sendMessage(input);
			}
		});
		return retVal;
	}
	
	private void tearDown() {
		if (model == null || !initialized) {
			return;
		}
		
		container.getChildren().clear();
		container.getRowConstraints().clear();
		container.getColumnConstraints().clear();
	}
}
