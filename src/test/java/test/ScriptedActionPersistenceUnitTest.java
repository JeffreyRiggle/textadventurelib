import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

import ilusr.core.io.FileUtilities;
import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.actions.ScriptedAction;
import textadventurelib.persistence.ScriptedActionPersistenceObject;

public class ScriptedActionPersistenceUnitTest {

	private final String SCRIPT = "function execute(executionParameters) { executionParameters.players()[0].name('New Name'); }";
	private final String ENCODED_SCRIPT = "ZnVuY3Rpb24gZXhlY3V0ZShleGVjdXRpb25QYXJhbWV0ZXJzKSB7IGV4ZWN1dGlvblBhcmFtZXRlcnMucGxheWVycygpWzBdLm5hbWUoJ05ldyBOYW1lJyk7IH0=";
	private final String EXPECTED_SCRIPT_TEXT = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Action type=\"Script\"><Parameters><Script ValueType=\"string\">ZnVuY3Rpb24gZXhlY3V0ZShleGVjdXRpb25QYXJhbWV0ZXJzKSB7IGV4ZWN1dGlvblBhcmFtZXRlcnMucGxheWVycygpWzBdLm5hbWUoJ05ldyBOYW1lJyk7IH0=</Script></Parameters></Action>";
	private final String SCRIPT_SAVE_LOCATION = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/scriptActionUnitTest.xml";
	private XmlConfigurationManager manager;
	
	@Test
	public void testCreate() {
		try {
			ScriptedActionPersistenceObject persistence = new ScriptedActionPersistenceObject();
			assertNotNull(persistence);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSetScript() {
		try {
			ScriptedActionPersistenceObject persistence = new ScriptedActionPersistenceObject();
			assertEquals(new String(), persistence.getScript());
			
			persistence.setScript(SCRIPT);
			assertEquals(SCRIPT, persistence.getScript());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetScriptAfterPrepare() {
		try {
			ScriptedActionPersistenceObject persistence = new ScriptedActionPersistenceObject();
			persistence.setScript(SCRIPT);
			assertEquals(SCRIPT, persistence.getScript());
			
			persistence.prepareXml();
			assertEquals(ENCODED_SCRIPT, persistence.getScript());
			
			persistence.prepareXml();
			assertEquals(ENCODED_SCRIPT, persistence.getScript());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSetScriptAfterPrepare() {
		try {
			ScriptedActionPersistenceObject persistence = new ScriptedActionPersistenceObject();
			persistence.setScript(SCRIPT);
			assertEquals(SCRIPT, persistence.getScript());
			
			persistence.prepareXml();
			assertEquals(ENCODED_SCRIPT, persistence.getScript());
			
			persistence.setScript(SCRIPT);
			assertEquals(SCRIPT, persistence.getScript());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSave() {
		try {
			manager = new XmlConfigurationManager(SCRIPT_SAVE_LOCATION);
			ScriptedActionPersistenceObject persistence = new ScriptedActionPersistenceObject();
			
			persistence.setScript(SCRIPT);
			
			persistence.prepareXml();
			manager.addConfigurationObject(persistence);
			manager.save();
			
			String fileContents = FileUtilities.getFileContent(new File(SCRIPT_SAVE_LOCATION));
			assertEquals(EXPECTED_SCRIPT_TEXT, fileContents);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertToTrigger() {
		try {
			ScriptedActionPersistenceObject persistence = new ScriptedActionPersistenceObject();
			
			persistence.setScript(SCRIPT);
			
			ScriptedAction action = (ScriptedAction)persistence.convertToAction();
			
			assertEquals(SCRIPT, action.getScript());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertFromPersistence() {
		try {
			manager = new XmlConfigurationManager(SCRIPT_SAVE_LOCATION);
			ScriptedActionPersistenceObject persistence = new ScriptedActionPersistenceObject();
			
			persistence.setScript(SCRIPT);
			
			persistence.prepareXml();
			manager.addConfigurationObject(persistence);
			manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(SCRIPT_SAVE_LOCATION);
			manager2.load();
			ScriptedActionPersistenceObject persist = new ScriptedActionPersistenceObject();
			
			persist.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals(SCRIPT, persist.getScript());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
}
