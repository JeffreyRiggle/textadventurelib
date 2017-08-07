package textadventurelib.gamestates;

import playerlib.player.IPlayer;
import textadventurelib.actions.AppendTextAction;
import textadventurelib.actions.CompletionAction;
import textadventurelib.actions.FinishAction;
import textadventurelib.core.*;
import textadventurelib.layout.TextAdventureLayout;
import textadventurelib.macro.IMacroManager;
import textadventurelib.macro.PlayerMacroManager;
import textadventurelib.options.IOption;
import textadventurelib.timers.CompletionTimerTaskFactory;
import textadventurelib.timers.TimerHelper;

import java.util.*;

import ilusr.gamestatemanager.GameState;
import ilusr.gamestatemanager.IFinishListener;

//TODO: This class needs some TLC
/**
 * 
 * @author Jeff Riggle
 *
 */
public class TextAdventureGameState extends GameState 
	implements ICompletionListener, IMessageListener, IFinishListener {

	//TODO: Timer ranges. what if we do not want the timer to start until some time.
	private TextAdventureLayout layout;
	private List<IOption> options;
	private List<TimerHelper> timers;
	private List<IMacroManager> macroManagers;
	private final Object messagingLock = new Object();
	private List<IPlayer> players;
	private String stateId;
	
	/**
	 * 
	 * @param content The layout to display for this game state.
	 */
	public TextAdventureGameState(TextAdventureLayout content) {
		this(content, new ArrayList<IOption>(), new ArrayList<TimerHelper>());
	}
	
	/**
	 * 
	 * @param content The layout to display for this game state.
	 * @param options The options for this game state.
	 * @param timers The timers for this game state.
	 */
	public TextAdventureGameState(TextAdventureLayout content,
			List<IOption> options, List<TimerHelper> timers) {
		this(content, options, timers, new ArrayList<IMacroManager>());
	}
	
	/**
	 * 
	 * @param content The layout to display for this game state.
	 * @param options The options for this game state.
	 * @param timers The timers for this game state.
	 * @param macros The macros for this game state.
	 */
	public TextAdventureGameState(TextAdventureLayout content,
			List<IOption> options, List<TimerHelper> timers,
			List<IMacroManager> macros) {
		super(content.container());
		this.layout = content;
		this.options = options;
		this.timers = timers;
		this.macroManagers = macros;
		this.layout.messageListener(this);
		this.players = new ArrayList<IPlayer>();
	}
	
	@Override
	public void sendMessage(String message) {
		// Should only process one message at a time.
		synchronized(messagingLock) {
			for (IOption option : options) {
				if (option.shouldTrigger(new TriggerParameters(message, players))) {
					option.action().execute(new ExecutionParameters(players, stateId));
					break;
				}
			}
		}
	}
	
	@Override
	public void sendMessageNoProcessing(String message) {
		textLog(layout.textLog() + '\n' + message);
	}
	
	public String textLog() {
		return layout.textLog();
	}
	
	public void textLog(String value) {
		layout.textLog(value);
		preformSubstitution();
	}
	
	private void preformSubstitution() {
		//TODO: Should we allow users to insert their on macros in game?
		for (IMacroManager manager : macroManagers) {
			layout.textLog(manager.substitute(layout.textLog()));
		}
	}
	
	public List<IPlayer> players() {
		return players;
	}
	
	private void registerListeners() {
		for (IOption option : options) {
			if (option.action().getClass().isInstance(CompletionAction.class)) continue;
			
			//TODO: Liskov :(
			if (option.action() instanceof CompletionAction) {
				CompletionAction act = (CompletionAction)option.action();
				act.addListener(this);
			}
			if (option.action() instanceof AppendTextAction) {
				AppendTextAction action = (AppendTextAction)option.action();
				action.addListener(this);
			}
			if (option.action() instanceof FinishAction) {
				FinishAction fAction = (FinishAction)option.action();
				fAction.addFinishListener(this);
			}
		}
		
		//TODO: Fix this
		if (timers == null) return;
		
		for (TimerHelper timer : timers) {
			if (!(timer.taskFactory() instanceof CompletionTimerTaskFactory)) continue;
			
			CompletionTimerTaskFactory taskFactory = (CompletionTimerTaskFactory)timer.taskFactory();
			taskFactory.listener(this);
		}
	}
	
	private void unregisterListeners() {
		for (IOption option : options) {
			if (option.action().getClass().isInstance(CompletionAction.class)) continue;
			
			//TODO: Liskov :(
			if (option.action() instanceof CompletionAction) {
				CompletionAction act = (CompletionAction)option.action();
				act.removeListener(this);
			}
			if (option.action() instanceof AppendTextAction) {
				AppendTextAction action = (AppendTextAction)option.action();
				action.removeListener(this);
			}
			if (option.action() instanceof FinishAction) {
				FinishAction fAction = (FinishAction)option.action();
				fAction.removeFinishListener(this);
			}
		}
	}
	
	@Override
	public <T> void run(T data) {
		
		//TODO: Liskov :(
		if (data instanceof GameStateRuntimeData) {
			GameStateRuntimeData runtimeData = (GameStateRuntimeData)data;
			
			for (IPlayer player : runtimeData.players()) {
				System.out.println("Adding player: " + player.name() + " to game state");
			}
			
			players = runtimeData.players();
			stateId = runtimeData.currentGameState();
			
			updateMacros();
			if (!runtimeData.textLog().equalsIgnoreCase("")) {
				textLog(runtimeData.textLog() + '\n' + textLog());
			}
		}
		
		layout.animate();
		registerListeners();
		startTimers();
	}

	private void updateMacros() {
		for(IMacroManager macro : macroManagers) {
			if (macro instanceof PlayerMacroManager) {
				((PlayerMacroManager)macro).players(players);
			}
		}
	}
	
	@Override
	public <T> void completed(T data) {
		stateCompleted(new GameStateCompletionData(data, textLog()));
		layout.suspend();
		unregisterListeners();
		stopTimers();
	}
	
	private void startTimers() {
		if (timers == null) return;
		
		for (TimerHelper timer : timers) {
			timer.start();
		}
	}
	
	private void stopTimers() {
		if (timers == null) return;
		
		for (TimerHelper timer : timers) {
			timer.stop();
		}
	}
	
	@Override
	public void dispose() {
		//TODO: Add dispose logic
		super.dispose();
	}

	@Override
	public void onFinished() {
		layout.suspend();
		super.finish();
	}
}