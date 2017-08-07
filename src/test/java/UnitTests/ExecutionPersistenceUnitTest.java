package UnitTests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.actions.ProcessAction;
import textadventurelib.persistence.ExecutionActionPersistence;

public class ExecutionPersistenceUnitTest {

	private XmlConfigurationManager _manager;
	private final String _exeActionUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/exeUnitTest.xml";
	private final String _exeActionUnitTest2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/exeUnitTest2.xml";
	
	private final String _expectedExeText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Action type=\"Execute\"><Parameters><Executable ValueType=\"string\">Atom.exe</Executable><Blocking ValueType=\"bool\">false</Blocking></Parameters></Action>";

	private final String _exe1 = "FireFox.exe";
	private final String _exe2 = "Atom.exe";
	private final String _exe3 = "Excel.exe";
	
	@Test
	public void testCreate() {
		try {
			ExecutionActionPersistence persistence = new ExecutionActionPersistence();
			
			assertNotNull(persistence);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testSetExecutable() {
		try {
			ExecutionActionPersistence persistence = new ExecutionActionPersistence();
			
			persistence.executable(_exe1);
			assertEquals(_exe1, persistence.executable());
			
			persistence.executable(_exe2);
			assertEquals(_exe2, persistence.executable());
			
			persistence.executable(_exe3);
			assertEquals(_exe3, persistence.executable());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetBlocking() {
		try {
			ExecutionActionPersistence persistence = new ExecutionActionPersistence();
			
			persistence.blocking(true);
			assertEquals(true, persistence.blocking());
			
			persistence.blocking(false);
			assertEquals(false, persistence.blocking());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			_manager = new XmlConfigurationManager(_exeActionUnitTest);
			ExecutionActionPersistence persistence = new ExecutionActionPersistence();
			
			persistence.executable(_exe2);
			persistence.blocking(false);
			persistence.prepareXml();
			_manager.addConfigurationObject(persistence);
			_manager.save();
			
			String fileContents = fileContents(_exeActionUnitTest);
			assertEquals(_expectedExeText, fileContents);
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertToAction() {
		try {
			ExecutionActionPersistence persistence = new ExecutionActionPersistence();
			persistence.executable(_exe1);
			persistence.blocking(false);
			
			ProcessAction action = (ProcessAction)persistence.convertToAction();
			assertEquals(false, action.blocking());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertFromPersistence() {
		try {
			_manager = new XmlConfigurationManager(_exeActionUnitTest2);
			ExecutionActionPersistence persistence = new ExecutionActionPersistence();
			
			persistence.executable(_exe2);
			persistence.blocking(false);
			persistence.prepareXml();
			_manager.addConfigurationObject(persistence);
			_manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(_exeActionUnitTest2);
			manager2.load();
			
			ExecutionActionPersistence persist = new ExecutionActionPersistence();
			persist.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals(_exe2, persist.executable());
			assertEquals(false, persist.blocking());
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	private String fileContents(String fileLocation) throws IOException {
		File file = new File(fileLocation);
		return getFileContent(file);
	}
	
	private String getFileContent(File file) throws IOException {
		String output = new String();
		FileInputStream inputStream = new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line = new String();
		
		while((line = reader.readLine()) != null) {
			output += line;
		}
		
		reader.close();
		return output;
	}
}
