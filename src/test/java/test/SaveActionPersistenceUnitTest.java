import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.persistence.SaveAction;
import textadventurelib.persistence.SaveActionPersistenceObject;

public class SaveActionPersistenceUnitTest {

	private final String _saveUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/saveUnitTest.xml";
	private final String _saveUnitTest2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/saveUnitTest2.xml";
	private final String _expectedText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Action type=\"Save\"><Parameters><SaveLocation ValueType=\"string\">" + System.getProperty("user.home") + "/ilusr/UnitTests/TAL/saveUnitTest.xml</SaveLocation><GameStatesLocation ValueType=\"string\">C:/GameStates</GameStatesLocation><Blocking ValueType=\"bool\">true</Blocking></Parameters></Action>";
	
	@Test
	public void testCreate() {
		try {
			SaveActionPersistenceObject action = new SaveActionPersistenceObject();
			
			assertNotNull(action);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testXmlCreate() {
		try {
			XmlConfigurationManager manager = new XmlConfigurationManager(_saveUnitTest2);
			SaveActionPersistenceObject action = new SaveActionPersistenceObject();
			
			action.saveLocation(_saveUnitTest2);
			action.gameStatesLocation("C:/GameStates");
			action.blocking(true);
			
			action.prepareXml();
			
			manager.addConfigurationObject(action);
			manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(_saveUnitTest2);
			manager2.load();
			
			SaveActionPersistenceObject action2 = new SaveActionPersistenceObject((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals(_saveUnitTest2, action2.saveLocation());
			assertEquals("C:/GameStates", action2.gameStatesLocation());
			assertEquals(true, action2.blocking());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSaveLocation() {
		try {
			SaveActionPersistenceObject action = new SaveActionPersistenceObject();
			
			action.saveLocation(_saveUnitTest);
			assertEquals(_saveUnitTest, action.saveLocation());
			
			action.saveLocation("C:/Test");
			assertEquals("C:/Test", action.saveLocation());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testGameStateLocation() {
		try {
			SaveActionPersistenceObject action = new SaveActionPersistenceObject();
			
			action.gameStatesLocation("C:/GameStates");
			assertEquals("C:/GameStates", action.gameStatesLocation());
			
			action.gameStatesLocation("D:/GameStates");
			assertEquals("D:/GameStates", action.gameStatesLocation());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testBlocking() {
		try {
			SaveActionPersistenceObject action = new SaveActionPersistenceObject();
			
			action.blocking(true);
			assertTrue(action.blocking());
			action.blocking(false);
			assertFalse(action.blocking());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			XmlConfigurationManager manager = new XmlConfigurationManager(_saveUnitTest);
			SaveActionPersistenceObject action = new SaveActionPersistenceObject();
			
			action.saveLocation(_saveUnitTest);
			action.gameStatesLocation("C:/GameStates");
			action.blocking(true);
			
			action.prepareXml();
			
			manager.addConfigurationObject(action);
			manager.save();
			
			String fileContents = fileContents(_saveUnitTest);
			assertEquals(_expectedText, fileContents);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertFromPersistence() {
		try {
			XmlConfigurationManager manager = new XmlConfigurationManager(_saveUnitTest2);
			SaveActionPersistenceObject action = new SaveActionPersistenceObject();
			
			action.saveLocation(_saveUnitTest2);
			action.gameStatesLocation("C:/GameStates");
			action.blocking(true);
			
			action.prepareXml();
			
			manager.addConfigurationObject(action);
			manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(_saveUnitTest2);
			manager2.load();
			
			SaveActionPersistenceObject action2 = new SaveActionPersistenceObject();
			action2.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals(_saveUnitTest2, action2.saveLocation());
			assertEquals("C:/GameStates", action2.gameStatesLocation());
			assertEquals(true, action2.blocking());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertToAction() {
		try {
			SaveActionPersistenceObject action = new SaveActionPersistenceObject();
			
			action.saveLocation(_saveUnitTest);
			action.gameStatesLocation("C:/GameStates");
			action.blocking(true);
			
			action.prepareXml();
			
			SaveAction act = (SaveAction)action.convertToAction();
			
			assertNotNull(act);
		} catch (Exception e) {
			fail(e.toString());
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
