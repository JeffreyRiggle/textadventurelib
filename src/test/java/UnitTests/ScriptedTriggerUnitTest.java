package UnitTests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import ilusr.core.io.FileUtilities;
import playerlib.player.IPlayer;
import playerlib.player.Player;
import textadventurelib.core.TriggerParameters;
import textadventurelib.triggers.ScriptedTrigger;

public class ScriptedTriggerUnitTest {

	private final String MESSAGE = "some message";
	private String badTriggerScript;
	private String simpleTriggerScript;
	private String complexTriggerScript;
	private TriggerParameters params;
	private IPlayer player1;
	
	@Before
	public void setup() {
		try {
			File simple = new File(getClass().getResource("SimpleTrigger.js").toURI().getSchemeSpecificPart());
			simpleTriggerScript = FileUtilities.getFileContent(simple);
			File bad = new File(getClass().getResource("badTrigger.js").toURI().getSchemeSpecificPart());
			complexTriggerScript = FileUtilities.getFileContent(bad);
			File complex = new File(getClass().getResource("ComplexTrigger.js").toURI().getSchemeSpecificPart());
			complexTriggerScript = FileUtilities.getFileContent(complex);
			
			player1 = new Player("Player1");
			
			params = new TriggerParameters();
			params.message(MESSAGE);
			params.players().add(player1);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testNoParamCreate() {
		ScriptedTrigger trigger = new ScriptedTrigger();
		assertNotNull(trigger);
	}
	
	@Test
	public void testScriptCreate() {
		ScriptedTrigger trigger = new ScriptedTrigger(simpleTriggerScript);
		assertNotNull(trigger);
	}
	
	@Test
	public void testSetCondition() {
		ScriptedTrigger trigger = new ScriptedTrigger();
		trigger.condition(simpleTriggerScript);
		
		assertEquals(simpleTriggerScript, trigger.<String>condition());
	}
	
	@Test
	public void testTriggerWithNoScript() {
		ScriptedTrigger trigger = new ScriptedTrigger();
		assertFalse(trigger.shouldFire(params));
	}
	
	@Test
	public void testBadTrigger() {
		ScriptedTrigger trigger = new ScriptedTrigger(badTriggerScript);
		assertFalse(trigger.shouldFire(params));
	}
	
	@Test
	public void testSimpleTrigger() {
		ScriptedTrigger trigger = new ScriptedTrigger(simpleTriggerScript);
		
		assertTrue(trigger.shouldFire(params));
	}
	
	@Test
	public void testComplexTrigger() {
		ScriptedTrigger trigger = new ScriptedTrigger(complexTriggerScript);
		
		assertTrue(trigger.shouldFire(params));
	}
}
