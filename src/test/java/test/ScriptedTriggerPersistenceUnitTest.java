import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import ilusr.core.io.FileUtilities;
import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.persistence.ScriptedTriggerPersistenceObject;
import textadventurelib.triggers.ScriptedTrigger;

public class ScriptedTriggerPersistenceUnitTest {

	private final String SCRIPT = "function shouldFire(triggerParameters) { return true; }";
	private final String ENCODED_SCRIPT = "ZnVuY3Rpb24gc2hvdWxkRmlyZSh0cmlnZ2VyUGFyYW1ldGVycykgeyByZXR1cm4gdHJ1ZTsgfQ==";
	private final String EXPECTED_SCRIPT_TEXT = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Trigger type=\"Script\"><Parameters><Script ValueType=\"string\">ZnVuY3Rpb24gc2hvdWxkRmlyZSh0cmlnZ2VyUGFyYW1ldGVycykgeyByZXR1cm4gdHJ1ZTsgfQ==</Script></Parameters></Trigger>";
	private final String SCRIPT_SAVE_LOCATION = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/scriptUnitTest.xml";
	private XmlConfigurationManager manager;
	
	@Test
	public void testCreate() {
		try {
			ScriptedTriggerPersistenceObject persistence = new ScriptedTriggerPersistenceObject();
			assertNotNull(persistence);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSetScript() {
		try {
			ScriptedTriggerPersistenceObject persistence = new ScriptedTriggerPersistenceObject();
			assertEquals(null, persistence.getScript());
			
			persistence.setScript(SCRIPT);
			assertEquals(SCRIPT, persistence.getScript());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetScriptAfterPrepare() {
		try {
			ScriptedTriggerPersistenceObject persistence = new ScriptedTriggerPersistenceObject();
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
			ScriptedTriggerPersistenceObject persistence = new ScriptedTriggerPersistenceObject();
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
			ScriptedTriggerPersistenceObject persistence = new ScriptedTriggerPersistenceObject();
			
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
			ScriptedTriggerPersistenceObject persistence = new ScriptedTriggerPersistenceObject();
			
			persistence.setScript(SCRIPT);
			
			ScriptedTrigger trigger = (ScriptedTrigger)persistence.convertToTrigger();
			
			assertEquals(SCRIPT, trigger.<String>condition());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertFromPersistence() {
		try {
			manager = new XmlConfigurationManager(SCRIPT_SAVE_LOCATION);
			ScriptedTriggerPersistenceObject persistence = new ScriptedTriggerPersistenceObject();
			
			persistence.setScript(SCRIPT);
			
			persistence.prepareXml();
			manager.addConfigurationObject(persistence);
			manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(SCRIPT_SAVE_LOCATION);
			manager2.load();
			ScriptedTriggerPersistenceObject persist = new ScriptedTriggerPersistenceObject();
			
			persist.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals(SCRIPT, persist.getScript());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
}
