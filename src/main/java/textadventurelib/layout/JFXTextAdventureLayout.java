package textadventurelib.layout;

import java.awt.Container;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import textadventurelib.core.IMessageListener;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class JFXTextAdventureLayout extends AnchorPane implements TextAdventureLayout, Initializable {

	private TextAdventureLayoutModel model;
	private List<LayoutView> layoutChildren;
	private String resource;
	private JFXPanel host;
	
	/**
	 * 
	 * @param fxml The fxml to use to create the view.
	 * @param model The model to associate with the view.
	 * @param resource The location of the style for the view.
	 */
	public JFXTextAdventureLayout(String fxml,
								  TextAdventureLayoutModel model, 
								  String resource) {
		this.model = model;
		this.resource = resource;
		layoutChildren = new ArrayList<LayoutView>();
		host = new JFXPanel();
		Platform.runLater(() -> {
			Scene scene = new Scene(this);
			host.setScene(scene);
		});
		
		FXMLLoader loader = new FXMLLoader();
		loader.setRoot(this);
		loader.setController(this);
		
		try {
			loader.load(new ByteArrayInputStream(fxml.getBytes()));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void suspend() {
		Platform.runLater(() -> {
			for (LayoutView layout : layoutChildren) {
				layout.suspend();
			}
			
			model.reset();
		});
	}

	@Override
	public void animate() {
		Platform.runLater(() -> {
			for (LayoutView layout : layoutChildren) {
				layout.animate();
			}
			
			if (resource != null && !resource.isEmpty()) {
				super.getStylesheets().setAll(resource); 
			}
		});
	}

	@Override
	public Container container() {
		return host;
	}

	@Override
	public void messageListener(IMessageListener listener) {
		model.setMessageListener(listener);
	}

	@Override
	public void textLog(String text) {
		model.textLog().set(text);
	}

	@Override
	public String textLog() {
		return model.textLog().get();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		 for (Node node : findAllNodes(this)) {
			 if (!(node instanceof LayoutView)) {
				 continue;
			 }
			 
			 LayoutView layout = (LayoutView)node;
			 layout.setModel(model);
			 layoutChildren.add(layout);
		 }
	}

	private List<Node> findAllNodes(Parent root) {
		List<Node> retVal = new ArrayList<Node>();
		addDescendents(root, retVal);
		return retVal;
	}
	
	private void addDescendents(Parent root, List<Node> nodes) {
		for (Node node : root.getChildrenUnmodifiable()) {
			nodes.add(node);
			if (node instanceof Parent) {
				addDescendents((Parent)node, nodes);
			}
		}
	}
}
