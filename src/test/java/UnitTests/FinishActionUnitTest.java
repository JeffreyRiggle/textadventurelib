package UnitTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import ilusr.gamestatemanager.IFinishListener;
import playerlib.player.IPlayer;
import textadventurelib.actions.FinishAction;
import textadventurelib.core.ExecutionParameters;

public class FinishActionUnitTest {

	@Test
	public void testCreate() {
		FinishAction action = new FinishAction();
		assertNotNull(action);
	}
	
	@Test
	public void testExecuteWithListener() {
		FinishAction action = new FinishAction();
		FinishListener listener = new FinishListener();
		action.addFinishListener(listener);
		action.execute(new ExecutionParameters(new ArrayList<IPlayer>()));
		assertTrue(listener.finished());
	}
	
	@Test
	public void testExecuteWithNoListener() {
		FinishAction action = new FinishAction();
		FinishListener listener = new FinishListener();
		action.addFinishListener(listener);
		action.removeFinishListener(listener);
		action.execute(new ExecutionParameters(new ArrayList<IPlayer>()));
		assertFalse(listener.finished());
	}
	
	private class FinishListener implements IFinishListener {

		private boolean finished;
		
		@Override
		public void onFinished() {
			finished = true;
		}
		
		public boolean finished() {
			return finished;
		}
	}
}
