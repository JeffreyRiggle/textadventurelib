import static org.junit.Assert.*;

import org.junit.Test;

import textadventurelib.actions.FinishAction;
import textadventurelib.actions.IAction;
import textadventurelib.persistence.FinishActionPersistenceObject;

public class FinishActionPersistenceObjectUnitTest {

	@Test
	public void testCreate() {
		try {
			FinishActionPersistenceObject persistence = new FinishActionPersistenceObject();
			assertNotNull(persistence);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testConvert() {
		try {
			FinishActionPersistenceObject persistence = new FinishActionPersistenceObject();
			IAction action = persistence.convertToAction();
			assertTrue(action instanceof FinishAction);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
