package textadventurelib.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.core.url.InternalURLProvider;
import ilusr.gamestatemanager.GameState;
import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.gamestates.TextAdventureGameState;
import textadventurelib.layout.ContentType;
import textadventurelib.layout.LayoutFactory;
import textadventurelib.layout.TextAdventureLayout;
import textadventurelib.layout.TextAdventureLayoutModel;
import textadventurelib.layout.models.ContentAndTextModel;
import textadventurelib.layout.models.ContentModel;
import textadventurelib.layout.models.ContentOnlyModel;
import textadventurelib.layout.models.TextAndInputModel;
import textadventurelib.layout.presenters.ContentAndTextPresenter;
import textadventurelib.layout.presenters.ContentOnlyPresenter;
import textadventurelib.layout.presenters.TextAndInputPresenter;
import textadventurelib.layout.views.ContentAndTextView;
import textadventurelib.layout.views.ContentOnlyView;
import textadventurelib.layout.views.TextAndInputViewImpl;
import textadventurelib.layout.views.inputviews.ButtonInputView;
import textadventurelib.layout.views.inputviews.IInputView;
import textadventurelib.layout.views.inputviews.TextBoxInputView;
import textadventurelib.macro.IMacroManager;
import textadventurelib.macro.MacroParameters;
import textadventurelib.macro.PlayerMacroManager;
import textadventurelib.options.IOption;
import textadventurelib.timers.TimerHelper;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameStatePersistenceObject extends XmlConfigurationObject{
	private static final String GAME_STATE_NAME = "GameState";
	private static final String STATE_ID_NAME = "StateId";
	private static final String TEXT_LOG_NAME = "TextLog";
	private static final String OPTIONS_NAME = "Options";
	private static final String TIMERS_NAME = "Timers";
	
	private final String Image_Extension_Pattern = ".*\\.(?i)(svg|tiff|jpeg|jpg|png|gif|bmp)";
	private final TimerPersistenceConverter timerConverter;
	private LayoutRepository layoutRepository;
	
	private Pattern imagePattern;
	private Matcher imageMatcher;
	private XmlConfigurationObject state;
	private XmlConfigurationObject textLog;
	private LayoutInfoPersistenceObject layout;
	private XmlConfigurationObject options;
	private XmlConfigurationObject timers;
	
	/**
	 * 
	 * @param stateId The state id to associate with this game state.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public GameStatePersistenceObject(String stateId) throws TransformerConfigurationException, ParserConfigurationException {
		this(stateId, new LayoutRepository());
	}
	
	/**
	 * 
	 * @param stateId The state id to associate with this game state.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public GameStatePersistenceObject(String stateId, LayoutRepository repository) throws TransformerConfigurationException, ParserConfigurationException {
		super(GAME_STATE_NAME);
		state = new XmlConfigurationObject(STATE_ID_NAME, stateId);
		textLog = new XmlConfigurationObject(TEXT_LOG_NAME);
		options = new XmlConfigurationObject(OPTIONS_NAME);
		timers = new XmlConfigurationObject(TIMERS_NAME);
		layout = new LayoutInfoPersistenceObject();
		
		this.layoutRepository = repository;
		imagePattern = Pattern.compile(Image_Extension_Pattern);
		timerConverter = new TimerPersistenceConverter();
	}
	
	/**
	 * 
	 * @param stateId The new state id to represent this game state.
	 */
	public void stateId(String stateId) {
		state.value(stateId);
	}
	
	/**
	 * 
	 * @return The state id currently associated with this game state.
	 */
	public String stateId() {
		return state.value();
	}
	
	/**
	 * 
	 * @param text The new text to associate with this game state.
	 */
	public void textLog(String text) {
		textLog.value(text);
	}
	
	/**
	 * 
	 * @return The text associated with this game state.
	 */
	public String textLog() {
		return textLog.value();
	}
	
	/**
	 * 
	 * @param layout The new @see LayoutPersistenceObject to define this game states
	 * 	display settings.
	 */
	public void layout(LayoutInfoPersistenceObject layout) {
		this.layout = layout;
	}
	
	/**
	 * 
	 * @return The @see LayoutPersistenceObject assoicated with this game state.
	 */
	public LayoutInfoPersistenceObject layout() {
		return layout;
	}
	
	/**
	 * 
	 * @param option A @see OptionPersistenceObject to add to this game state.
	 */
	public void addOption(OptionPersistenceObject option) {
		options.addChild(option);
	}
	
	/**
	 * 
	 * @param option A @see OptionPersistenceObject to remove from this game state.
	 */
	public void removeOption(OptionPersistenceObject option) {
		options.removeChild(option);
	}
	
	/**
	 * Removes all @see OptionPersistenceObject from this game state.
	 */
	public void clearOptions() {
		options.clearChildren();
	}
	
	/**
	 * 
	 * @return A list of @see OptionPersistenceObject 's that are associated with this game state.
	 */
	public List<OptionPersistenceObject> options() {
		List<OptionPersistenceObject> retVal = new ArrayList<OptionPersistenceObject>();
		
		for(PersistXml obj : options.children()) {
			OptionPersistenceObject option = (OptionPersistenceObject)obj;
			retVal.add(option);
		}
		
		return retVal;
	}
	
	/**
	 * 
	 * @param timer A @see TimerPersistenceObject to add to this game state.
	 */
	public void addTimer(TimerPersistenceObject timer) {
		timers.addChild(timer);
	}
	
	/**
	 * 
	 * @param timer A @see TimerPersistenceObject to remove from this game state.
	 */
	public void removeTimer(TimerPersistenceObject timer) {
		timers.removeChild(timer);
	}
	
	/**
	 * Removes all @see TimerPersistenceObject 's from this game state.
	 */
	public void clearTimers() {
		timers.clearChildren();
	}
	
	/**
	 * 
	 * @return A list of @see TimerPersistenceObject 's that are associated with this game state.
	 */
	public List<TimerPersistenceObject> timers() {
		List<TimerPersistenceObject> retVal = new ArrayList<TimerPersistenceObject>();
		
		for(PersistXml obj : timers.children()) {
			TimerPersistenceObject option = (TimerPersistenceObject)obj;
			retVal.add(option);
		}
		
		return retVal;
	}
	
	public void setLayoutRepository(LayoutRepository repository) {
		this.layoutRepository = repository;
	}
	
	/**
	 * Prepares this xml to be saved. This must be called before saving the xml!
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public void prepareXml() throws TransformerConfigurationException, ParserConfigurationException {
		super.clearChildren();
		super.addChild(state);
		
		layout.prepareXml();
		super.addChild(layout);
		
		super.addChild(textLog);
		
		for (PersistXml option : options.children()) {
			((OptionPersistenceObject)option).prepareXml();
		}
		
		super.addChild(options);
		
		for (PersistXml timer : timers.children()) {
			((TimerPersistenceObject)timer).prepareXml();
		}
		
		super.addChild(timers);
	}
	
	public GameState convertToGameState() {
		TextAdventureLayout layout = createLayout();
		List<IOption> options = new ArrayList<IOption>();
		
		for (OptionPersistenceObject opt : options()) {
			options.add(opt.convertToOption());
		}
		
		List<TimerHelper> timers = new ArrayList<TimerHelper>();
		
		for (TimerPersistenceObject tmr : timers()) {
			timers.add(tmr.convertToTimer());
		}
		
		List<IMacroManager> macros = new ArrayList<IMacroManager>();
		
		//TODO: This kind of feels like a hack should we be forcing this?
		MacroParameters parameters = new MacroParameters("\\{\\[", "\\]\\}", "<<", ">>", "@");
		//macros.add(new PlayerMacroManager(player, parameters));
		macros.add(new PlayerMacroManager(parameters));
		
		GameState game = new TextAdventureGameState(layout, options, timers, macros);
		return game;
	}
	
	private TextAdventureLayout createLayout() {
		TextAdventureLayout retVal = null;
		TextAndInputViewImpl textInputView = null;
		TextAndInputModel textInputModel = null;
		List<JButton> buttons = new ArrayList<JButton>();
		IInputView buttonView = null;
		ContentOnlyView contentView = null;
		ContentOnlyModel contentOnlyModel = null;
		ContentAndTextView contentTextView = null;
		ContentModel contentModel = null;
		ContentAndTextModel contentTextModel = null;
		String completionData = new String();
		
		switch (layout.getLayoutType()) {
			case TextWithTextInput:
				textInputView = new TextAndInputViewImpl();
				textInputModel = new TextAndInputModel(textLog(), new TextBoxInputView());
				retVal = new TextAndInputPresenter(textInputView, textInputModel);
				break;
			case TextWithButtonInput:
				textInputView = new TextAndInputViewImpl();
				
				for (OptionPersistenceObject option : options()) {
					for (TriggerPersistenceObject trigger : option.triggers()) {
						if (!trigger.type().equalsIgnoreCase("Text")) continue;
						
						JButton btn = new JButton(((TextTriggerPersistenceObject)trigger).text());
						buttons.add(btn);
					}
				}
				
				buttonView = new ButtonInputView(buttons);
				textInputModel = new TextAndInputModel(textLog(), buttonView);
				retVal = new TextAndInputPresenter(textInputView, textInputModel);
				break;
			case ContentOnly:
				contentView = new ContentOnlyView();
				
				//TODO: Is this really the right impl for this, kinda feels hacky.
				for (OptionPersistenceObject option : options()) {
					if (!option.action().type().equalsIgnoreCase("Completion")) continue;
					
					completionData = ((CompletionActionPersistence)option.action()).completionData();
				}
				
				contentOnlyModel = new ContentOnlyModel(completionData);
				
				//TODO: Can this be done better.
				imageMatcher = imagePattern.matcher(layout.getLayoutContent());
				if (imageMatcher.matches()) {
					contentOnlyModel.type(ContentType.Image);
				}
				else {
					contentOnlyModel.type(ContentType.Video);
				}
				
				contentOnlyModel.file(layout.getLayoutContent());
				retVal = new ContentOnlyPresenter(contentOnlyModel, contentView);
				break;
			case TextAndContentWithTextInput:
				contentTextView = new ContentAndTextView();
				textInputModel = new TextAndInputModel(textLog(), new TextBoxInputView());
				contentModel = new ContentModel();
				
				//TODO: Can this be done better.
				imageMatcher = imagePattern.matcher(layout.getLayoutContent());
				if (imageMatcher.matches()) {
					contentModel.type(ContentType.Image);
				}
				else {
					contentModel.type(ContentType.Video);
				}
				
				contentModel.file(layout.getLayoutContent());
				
				contentTextModel = new ContentAndTextModel(contentModel, textInputModel);
				retVal = new ContentAndTextPresenter(contentTextModel, contentTextView);
				break;
			case TextAndContentWithButtonInput:
				contentTextView = new ContentAndTextView();
				
				for (OptionPersistenceObject option : options()) {
					for (TriggerPersistenceObject trigger : option.triggers()) {
						if (!trigger.type().equalsIgnoreCase("Text")) continue;
						
						JButton btn = new JButton(((TextTriggerPersistenceObject)trigger).text());
						buttons.add(btn);
						break;
					}
				}
				
				buttonView = new ButtonInputView(buttons);
				textInputModel = new TextAndInputModel(textLog(), buttonView);
				
				contentModel = new ContentModel();
				
				//TODO: Can this be done better.
				imageMatcher = imagePattern.matcher(layout.getLayoutContent());
				if (imageMatcher.matches()) {
					contentModel.type(ContentType.Image);
				}
				else {
					contentModel.type(ContentType.Video);
				}
				
				contentModel.file(layout.getLayoutContent());
				
				contentTextModel = new ContentAndTextModel(contentModel, textInputModel);
				retVal = new ContentAndTextPresenter(contentTextModel, contentTextView);
				break;
			case Custom:
				TextAdventureLayoutModel model = new TextAdventureLayoutModel(textLog());
				LayoutPersistenceObject cLayout = layoutRepository.getLayout(layout.getLayoutId());
				
				if (cLayout == null) {
					break;
				}
				
				model.resource().set(layout.getLayoutContent());
				
				for (OptionPersistenceObject option : options()) {
					for (TriggerPersistenceObject trigger : option.triggers()) {
						if (!trigger.type().equalsIgnoreCase("Text")) continue;
						
						String text = ((TextTriggerPersistenceObject)trigger).text();
						if (!model.inputs().contains(text)) {
							model.inputs().add(text);
						}
						break;
					}
				}
				
				String stylePath = new String();
				
				if (cLayout.getStyle() != null && !cLayout.getStyle().compile().isEmpty()) {
					String id = UUID.randomUUID().toString();
					stylePath = InternalURLProvider.getInstance().prepareURL(cLayout.getStyle().compile(), id.replaceAll("-", "") + ".css");
				}
				
				return LayoutFactory.create(cLayout.getLayout().compile(), model, stylePath);
			default:
				break;
		}
		
		return retVal;
	}
	
	public void convertFromPersistence(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			
			switch (cChild.name()) {
				case STATE_ID_NAME:
					stateId(cChild.<String>value());
					break;
				case TEXT_LOG_NAME:
					textLog(cChild.<String>value());
					break;
				case OPTIONS_NAME:
					convertOptions(cChild);
					break;
				case TIMERS_NAME:
					convertTimers(cChild);
					break;
				case "LayoutInfo":
					try {
						LayoutInfoPersistenceObject layout = new LayoutInfoPersistenceObject();
						layout.convertFromPersistence(cChild);
						layout(layout);
					} catch (Exception e) {
						//TODO: What to do here.
					}
					break;
				default:
					break;
			}
		}
	}
	
	private void convertOptions(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			if (!cChild.name().equalsIgnoreCase("Option")) continue;
			
			try {
				//TODO: Is this safe to assume?
				if (cChild.configurationProperties().size() > 0 ) {
					PlayerModificationOptionPersistenceObject pOption = new PlayerModificationOptionPersistenceObject();
					pOption.convertFromPersistence(cChild);
					addOption(pOption);
					continue;
				}
				
				OptionPersistenceObject option = new OptionPersistenceObject();
				option.convertFromPersistence(cChild);
				addOption(option);
			} catch (Exception e) {
				//TODO What to do here.
			}
		}
	}
	
	private void convertTimers(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			TimerPersistenceObject timer = timerConverter.convert((XmlConfigurationObject)child);
			if (timer != null) {
				addTimer(timer);
			}
		}
	}
	
	@Override
	public String toString() {
		return stateId();
	}
}
