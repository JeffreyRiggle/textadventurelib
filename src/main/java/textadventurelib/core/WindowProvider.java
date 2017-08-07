package textadventurelib.core;

import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class WindowProvider {

	private final JFrame frame;
	private final AnchorPane pane;
	
	/**
	 * 
	 * @param frame The frame to use.
	 */
	public WindowProvider(JFrame frame) {
		this(frame, null);
	}
	
	/**
	 * 
	 * @param stage The anchor pane to use.
	 */
	public WindowProvider(AnchorPane stage) {
		this(null, stage);
	}
	
	/**
	 * 
	 * @param frame The frame to use.
	 * @param pane The anchor pane to use.
	 */
	private WindowProvider(JFrame frame, AnchorPane pane) {
		this.pane = pane;
		this.frame = frame;
	}
	
	/**
	 * 
	 * @param container The content to show in the main window.
	 */
	public void setContent(Container container) {
		if (frame != null) {
			setFrameContent(container);
		} else {
			setStageContent(container);
		}
	}
	
	private void setFrameContent(Container container) {
		frame.setContentPane(container);
		frame.revalidate();
		frame.repaint();
	}
	
	private void setStageContent(Container container) {
		final SwingNode node = new SwingNode();
		
		SwingUtilities.invokeLater(() -> {
			node.setContent((JComponent)container);
			Platform.runLater(() -> {
				pane.getChildren().clear();
				pane.getChildren().add(node);
				AnchorPane.setBottomAnchor(node, 0.0);
				AnchorPane.setTopAnchor(node, 0.0);
				AnchorPane.setLeftAnchor(node, 0.0);
				AnchorPane.setRightAnchor(node, 0.0);
			});
		}); 
	}
}
