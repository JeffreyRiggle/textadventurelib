package UnitTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import textadventurelib.actions.CompletionAction;
import textadventurelib.actions.IAction;
import textadventurelib.actions.ProcessAction;
import textadventurelib.options.IOption;
import textadventurelib.options.Option;
import textadventurelib.triggers.ITrigger;
import textadventurelib.triggers.TextTrigger;

public class OptionUnitTest {

	private IAction _action1 = new CompletionAction();
	private IAction _action2 = new ProcessAction("");
	private ITrigger _trigger1 = new TextTrigger("Dog");
	private ITrigger _trigger2 = new TextTrigger("Cat");
	
	@Test
	public void testCreateOption() {
		IOption option = new Option(_trigger1, _action1);
		assertNotNull(option);
		assertEquals(_trigger1, option.triggers().get(0));
		assertEquals(_action1, option.action());
	}

	@Test
	public void testSetTrigger() {
		IOption option = new Option(_trigger1, _action1);
		assertEquals(_trigger1, option.triggers().get(0));
		
		option.triggers(new ArrayList<ITrigger>(Arrays.asList(_trigger2)));
		assertEquals(_trigger2, option.triggers().get(0));
	}
	
	@Test
	public void testSetAction() {
		IOption option = new Option(_trigger1, _action1);
		assertEquals(_action1, option.action());
		
		option.action(_action2);
		assertEquals(_action2, option.action());
	}
}
