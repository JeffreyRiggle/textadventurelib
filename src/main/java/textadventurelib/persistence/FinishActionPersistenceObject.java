package textadventurelib.persistence;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import textadventurelib.actions.FinishAction;
import textadventurelib.actions.IAction;

/**
 * 
 * @author Jeff Riggle
 * 
 * Creates an action that will finish the game.
 *
 */
public class FinishActionPersistenceObject extends ActionPersistenceObject {

	private static final String FINISH_TYPE = "Finish";
	
	/**
	 * Base Ctor.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public FinishActionPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(FINISH_TYPE);
	}

	@Override
	public IAction convertToAction() {
		return new FinishAction();
	}
}
