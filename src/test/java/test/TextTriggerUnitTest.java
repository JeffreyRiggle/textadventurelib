import static org.junit.Assert.*;

import org.junit.Test;

import textadventurelib.core.*;
import textadventurelib.triggers.ITrigger;
import textadventurelib.triggers.TextTrigger;


public class TextTriggerUnitTest {

	private final String _expression1 = ".*Fox.*";
	private final String _expression2 = ".*Hare.*";
	private final String _expression3 = ".*ow.*";
	private final String _expression4 = "The.*";
	private final String _compare1 = "The Lazy Brown Fox";
	private final String _compare2 = "The Quick Hare";
	private final String _compare3 = "The Fox and the Hare";
	
	@Test
	public void testCreateTextTrigger() {
		ITrigger txtTrigger = new TextTrigger(_expression1);
		assertEquals(_expression1, txtTrigger.condition());
	}

	@Test
	public void testChangeCondition() {
		ITrigger txtTrigger = new TextTrigger(_expression1);
		assertEquals(_expression1, txtTrigger.condition());
		
		txtTrigger.condition(_expression2);
		assertEquals(_expression2, txtTrigger.condition());
	}
	
	@Test
	public void testFire() {
		ITrigger txtTrigger = new TextTrigger(_expression1);
		assertEquals(true, txtTrigger.shouldFire(new TriggerParameters(_compare1)));
		assertEquals(false, txtTrigger.shouldFire(new TriggerParameters(_compare2)));
		assertEquals(true, txtTrigger.shouldFire(new TriggerParameters(_compare3)));
		
		txtTrigger.condition(_expression2);
		assertEquals(false, txtTrigger.shouldFire(new TriggerParameters(_compare1)));
		assertEquals(true, txtTrigger.shouldFire(new TriggerParameters(_compare2)));
		assertEquals(true, txtTrigger.shouldFire(new TriggerParameters(_compare3)));
		
		txtTrigger.condition(_expression3);
		assertEquals(true, txtTrigger.shouldFire(new TriggerParameters(_compare1)));
		assertEquals(false, txtTrigger.shouldFire(new TriggerParameters(_compare2)));
		assertEquals(false, txtTrigger.shouldFire(new TriggerParameters(_compare3)));
		
		txtTrigger.condition(_expression4);
		assertEquals(true, txtTrigger.shouldFire(new TriggerParameters(_compare1)));
		assertEquals(true, txtTrigger.shouldFire(new TriggerParameters(_compare2)));
		assertEquals(true, txtTrigger.shouldFire(new TriggerParameters(_compare3)));
	}
}