package UnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ilusr.core.io.FileUtilities;
import playerlib.player.IPlayer;
import playerlib.player.Player;
import textadventurelib.actions.ScriptedAction;
import textadventurelib.core.ExecutionParameters;

public class ScriptedActionUnitTest {

	private final String STATE = "State1";
	private String badActionScript;
	private String simpleActionScript;
	private ExecutionParameters params;
	private IPlayer player1;
	
	@Before
	public void setup() {
		try {
			File simple = new File(getClass().getResource("SimpleAction.js").toURI().getSchemeSpecificPart());
			simpleActionScript = FileUtilities.getFileContent(simple);
			File bad = new File(getClass().getResource("badAction.js").toURI().getSchemeSpecificPart());
			badActionScript = FileUtilities.getFileContent(bad);
			
			player1 = new Player("Player1");
			List<IPlayer> players = new ArrayList<IPlayer>();
			players.add(player1);
			
			params = new ExecutionParameters(players, STATE);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testNoParamCreate() {
		ScriptedAction action = new ScriptedAction();
		assertNotNull(action);
	}
	
	@Test
	public void testScriptCreate() {
		ScriptedAction action = new ScriptedAction(simpleActionScript);
		assertNotNull(action);
	}
	
	@Test
	public void testSetCondition() {
		ScriptedAction action = new ScriptedAction();
		action.setScript(simpleActionScript);
		
		assertEquals(simpleActionScript, action.getScript());
	}
	
	@Test
	public void testTriggerWithNoScript() {
		ScriptedAction action = new ScriptedAction();
		action.execute(params);
	}
	
	@Test
	public void testBadAction() {
		ScriptedAction action = new ScriptedAction(badActionScript);
		action.execute(params);
	}
	
	@Test
	public void testSimpleAction() {
		ScriptedAction action = new ScriptedAction(simpleActionScript);
		
		assertEquals(params.players().get(0).name(), "Player1");
		action.execute(params);
		assertEquals(params.players().get(0).name(), "Action Name");
	}
}
