package textadventurelib.layout.views;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ilusr.core.mvpbase.IViewListener;
import ilusr.logrunner.LogRunner;
import textadventurelib.layout.ContentType;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ContentViewImpl extends JPanel implements ContentView {
	private static final long serialVersionUID = 1L;
	private Object content;
	private ContentType type;
	private String file;
	private MediaView mediaView;
	private MediaPlayer mediaPlayer;
	private JFXPanel videoPanel;
	
	/**
	 * Creates a empty content pane.
	 */
	public ContentViewImpl() {
		this(null, null);
	}
	
	//TODO: Can the content type be removed?
	/**
	 * 
	 * @param file The location of the file to display.
	 * @param type The type of file.
	 */
	public ContentViewImpl(String file, ContentType type) {
		this.type = type;
		this.file = file;
		
		initialize();
	}
	
	//TODO: Should we really be catching this exception?
	/**
	 * Initializes the content view with the current file and type.
	 */
	private void initialize() {
		if (type == null) return;
		
		try {
			switch (type) {
				case Image:
					initializeImage();
					break;
				case Video:
					initializeVideo();
					break;
				default:
					break;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void initializeImage() throws IOException {
		if (file == null) return;
		
		String imgPath = file;
		File imgFile = new File(imgPath);
		content = ImageIO.read(imgFile);
	}
	
	/**
	 * Initialize the video content in this view.
	 */
	private void initializeVideo() {
		setLayout(new BorderLayout());
		removeAll();
		videoPanel = new JFXPanel();
		add(videoPanel, BorderLayout.CENTER);
		
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				initFX();
			}
		});
	}
	
	private void initFX() {
		Group root = new Group();
		Scene scene = new Scene(root);
		videoPanel.setScene(scene);
		
		File file = new File(this.file);
		String path = file.toURI().toString();
		mediaPlayer = new MediaPlayer(new Media(path));
		mediaPlayer.setAutoPlay(false);
		mediaView = new MediaView(mediaPlayer);
		
		mediaView.setPreserveRatio(false);
		root.getChildren().add(mediaView);
		Platform.setImplicitExit(false);
	}

	@Override
	public void content(String file, ContentType type) {
		this.file = file;
		this.type = type;
		initialize();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponents(g);
		
		if (type == ContentType.Image) {
			g.drawImage((BufferedImage)content, 0, 0, getWidth(), getHeight(), this);
		}
	}
	
	/**
	 * Plays the video content in the view.
	 */
	private void playVideoContent() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				LogRunner.logger().log(Level.INFO, String.format("Setting media view width: %s, and height: %s", videoPanel.getWidth(), videoPanel.getHeight()));
				mediaView.setFitHeight(videoPanel.getHeight());
				mediaView.setFitWidth(videoPanel.getWidth());
				mediaPlayer.play();
				
				mediaPlayer.setOnEndOfMedia(new Runnable() {
					@Override
					public void run() {
						completed();
					}
				});
			}
		});
	}
	
	/**
	 * Entry point to override for custom completion actions.
	 * By default this call does nothing.
	 */
	protected void completed() {
		
	}
	
	@Override
	public void addListener(IViewListener listener) {
		// No op
	}

	@Override
	public void removeListener(IViewListener listener) {
		// No op
	}

	@Override
	public void suspend() {
		LogRunner.logger().log(Level.INFO, "Stopping media");
		mediaPlayer.stop();
	}

	@Override
	public void animate() {
		LogRunner.logger().log(Level.INFO, "Starting media");
		if (type == ContentType.Video) {
			playVideoContent();
		}
	}
}
