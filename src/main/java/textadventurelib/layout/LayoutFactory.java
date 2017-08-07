package textadventurelib.layout;

import javafx.application.Platform;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LayoutFactory {
	
	/**
	 * 
	 * @param layout The fxml to use for the view.
	 * @param model The model to associate with the view.
	 * @param cssLocation The location of the css file.
	 * @return The created layout.
	 */
	public static TextAdventureLayout create(String layout, TextAdventureLayoutModel model, String cssLocation) {
		if (Platform.isFxApplicationThread()){
			return new JFXTextAdventureLayout(layout, model, cssLocation);
		}
		
		CreationRunnable create = new CreationRunnable(layout, model, cssLocation);

		new Thread(() -> {
			Platform.runLater(create);
		}).start();
			
		try {
			synchronized (create) {
				while (!create.isFinished()) {
					Thread.sleep(10);
				}
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
		return create.layout();
	}
	
	private static class CreationRunnable implements Runnable {
		private String layout;
		private TextAdventureLayoutModel model;
		private String cssLocation;
		private TextAdventureLayout retVal;
		private boolean isFinished;
		
		public CreationRunnable(String layout, TextAdventureLayoutModel model, String cssLocation) {
			this.layout = layout;
			this.model = model;
			this.cssLocation = cssLocation;
			this.isFinished = false;
		}
		
		public boolean isFinished() {
			return isFinished;
		}
		
		public TextAdventureLayout layout() {
			return retVal;
		}
		
		@Override
		public void run() {
			retVal = new JFXTextAdventureLayout(layout, model, cssLocation);
			isFinished = true;
		}
	}
}
