package textadventurelib.persistence;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import textadventurelib.actions.IAction;


/**
 * 
 * @author Jeff Riggle
 *
 *  Current use:
 *  Currently there is a type that tells you what kind of action this is. (Current types are:
 *  Append Text, completion, modify player and execute).
 *  
 *  current planned xml that is formed for each:
 *  Append Text
 *  <pre>
 *  {@code
 *  <Action type="AppendText">
 *  	<Parameters>
 *  		<AppendText>Text to append</AppendText>
 *  	</Parameters>
 *  </Action>
 *  }
 *  </pre>
 *  Completion
 *  <pre>
 *  {@code
 *  <Action type="Completion">
 *  	<Parameters>
 *  		<CompletionData>Data for completion</CompletionData>
 *  	</Parameters>
 *  </Action>
 *  }
 *  </pre>
 *  Modify Player
 *  <pre>
 *  {@code
 *  <Action type="ModifyPlayer">
 *  	<Parameters>
 *  		<ModificationObject>Characteristic/Attribute/BodyPart/item/...</ModificationObject>
 *  		<Data>What to modify it with or to&</Data>
 *  		<ID>What are we modifying (ex. age)</ID>
 *  		<ChangeType>Assign/Add/Subtract</ChangeType>
 *  		<ModificationType>Change/Add/Remove</ModificationType>
 *  	</Parameters>
 *  </Action>
 *  }
 *  </pre>
 *  Execute
 *  <pre>
 *  {@code
 *  <Action type="Execute">
 *  	<Parameters>
 *  		<Executable>The application to run</Executable>
 *  	</Parameters>
 *  </Action>
 *  }
 *  </pre>
 */
public abstract class ActionPersistenceObject extends ParameterizedPersistenceObject {

	private static final String ACTION_NAME = "Action";
	
	/**
	 * 
	 * @param type The type of action.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public ActionPersistenceObject(String type) throws TransformerConfigurationException, ParserConfigurationException {
		super(ACTION_NAME);
		super.type(type);
	}
	
	public String type() {
		return super.type();
	}
	
	public abstract IAction convertToAction();
}
