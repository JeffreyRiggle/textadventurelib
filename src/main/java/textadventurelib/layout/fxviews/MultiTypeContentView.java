package textadventurelib.layout.fxviews;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebView;
import textadventurelib.layout.LayoutView;
import textadventurelib.layout.TextAdventureLayoutModel;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class MultiTypeContentView extends AnchorPane implements Initializable, LayoutView {

	@FXML
	private AnchorPane content;
	
	private final String VIDEO_EXTENSION_PATTERN = ".*\\.(?i)(aif|aiff|fxm|flv|m3u8|mp3|mp4|m4a|m4v|wav)";
	private final String IMAGE_EXTENSION_PATTERN = ".*\\.(?i)(svg|tiff|jpeg|jpg|png|gif|bmp)";
	private final String WEB_EXTENSION_PATTERN = "(?i)((http|https)://.*)|(www\\..*)|(.*(\\.htm|\\.html))";
	
	private final Pattern imagePattern = Pattern.compile(IMAGE_EXTENSION_PATTERN);
	private final Pattern videoPattern = Pattern.compile(VIDEO_EXTENSION_PATTERN);
	private final Pattern webPattern = Pattern.compile(WEB_EXTENSION_PATTERN);
	
	private TextAdventureLayoutModel model;
	private SimpleStringProperty resource;
	private Runnable playAction;
	private Runnable stopAction;
	private boolean usingModelResource;
	
	/**
	 * Creates a multi type content view.
	 */
	public MultiTypeContentView() {
		resource = new SimpleStringProperty();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MultiTypeContentView.fxml"));
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
	 * @return The resource to display.
	 */
	public StringProperty contentResource() {
		return resource;
	}
	
	/**
	 * 
	 * @return The resource to display.
	 */
	public String getContentResource() {
		return resource.get();
	}
	
	/**
	 * 
	 * @param value The new resource to display.
	 */
	public void setContentResource(String value) {
		resource.set(value);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		updateDisplay(resource.get());
		
		resource.addListener((v, o, n) -> {
			updateDisplay(n);
		});
	}
	
	private void updateDisplay(String resouce) {
		Node contentItem = getContentItem(resouce);
		content.getChildren().clear();
		content.getChildren().add(contentItem);
		
		AnchorPane.setBottomAnchor(contentItem, 0.0);
		AnchorPane.setTopAnchor(contentItem, 0.0);
		AnchorPane.setLeftAnchor(contentItem, 0.0);
		AnchorPane.setRightAnchor(contentItem, 0.0);
	}
	
	private Node getContentItem(String resource) {
		Node retVal = new Label("Unable to find content");
		playAction = null;
		
		if (resource == null) {
			return retVal;
		}
		
		if (isVideoContent(resource)) {
			retVal = createVideoPlayer(resource);
		} else if (isImageContent(resource)) {
			retVal = createImageView(resource);
		} else if (isWebContent(resource)) {
			retVal = createWebView(resource);
		} 
		
		return retVal;
	}
	
	private MediaView createVideoPlayer(String resource) {
		MediaPlayer player = new MediaPlayer(new Media(resource));
		player.setAutoPlay(false);
		MediaView view = new MediaView(player);
		view.setPreserveRatio(false);
		view.fitHeightProperty().bind(this.heightProperty());
		view.fitWidthProperty().bind(this.widthProperty());
		playAction = () -> { player.play(); };
		stopAction = () -> { player.stop(); };
		return view;
	}
	
	private Node createImageView(String resource) {
		ImageView img = new ImageView(resource);
		img.setPreserveRatio(false);
		img.fitWidthProperty().set(400);
		img.fitHeightProperty().set(300);
		Platform.runLater(() -> {
			img.fitWidthProperty().bind(this.widthProperty());
			img.fitHeightProperty().bind(this.heightProperty());
		});
		return img;
	}
	
	private WebView createWebView(String resource) {
		WebView browser = new WebView();
		playAction = () -> { browser.getEngine().load(resource); };
		stopAction = () -> { browser.getEngine().load("about:blank"); };
		return browser;
	}
	
	private boolean isVideoContent(String resource) {
		return videoPattern.matcher(resource).matches();
	}
	
	private boolean isWebContent(String resource) {
		return webPattern.matcher(resource).matches();
	}
	
	private boolean isImageContent(String resource) {
		return imagePattern.matcher(resource).matches();
	}

	@Override
	public void suspend() {
		if (stopAction != null) {
			stopAction.run();
		}
	}

	@Override
	public void animate() {
		if (playAction != null) {
			playAction.run();
		}
	}

	@Override
	public void setModel(TextAdventureLayoutModel model) {
		if (this.model != null && usingModelResource) {
			resource.unbind();
		}
		
		this.model = model;
		
		if (this.model != null && (resource.get().isEmpty() || usingModelResource)) {
			resource.bind(model.resource());
			updateDisplay(model.resource().get());
			usingModelResource = true;
		}
	}
}
