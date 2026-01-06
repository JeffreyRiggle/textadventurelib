import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.persistence.DisplayType;
import textadventurelib.persistence.TransitionPersistenceObject;

public class TransitionPersistenceUnitTest {

	private XmlConfigurationManager _manager;

	private final String _transitionUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/transitionUnitTest.xml";
	private final String _transitionUnitTest2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/transitionUnitTest2.xml";
	private final String _expectedTransitionText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Transition><DisplayType ValueType=\"object\">Window</DisplayType><MediaLocation ValueType=\"string\">C:/something.avi</MediaLocation></Transition>";

	@Test
	public void testCreate() {
		try {
			TransitionPersistenceObject persist = new TransitionPersistenceObject();
			
			assertNotNull(persist);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testSetDisplayType() {
		try {
			TransitionPersistenceObject persist = new TransitionPersistenceObject();
			
			persist.displayType(DisplayType.Window);
			assertEquals(DisplayType.Window, persist.displayType());
			
			persist.displayType(DisplayType.SplashScreen);
			assertEquals(DisplayType.SplashScreen, persist.displayType());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetMediaLocation() {
		try {
			TransitionPersistenceObject persist = new TransitionPersistenceObject();
			
			persist.mediaLocation("C:/something.jpg");
			assertEquals("C:/something.jpg", persist.mediaLocation());
			

			persist.mediaLocation("C:/something.png");
			assertEquals("C:/something.png", persist.mediaLocation());
			
			persist.mediaLocation("C:/something.avi");
			assertEquals("C:/something.avi", persist.mediaLocation());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			_manager = new XmlConfigurationManager(_transitionUnitTest);
			TransitionPersistenceObject persist = new TransitionPersistenceObject();
			
			persist.displayType(DisplayType.Window);
			persist.mediaLocation("C:/something.avi");
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			String fileContents = fileContents(_transitionUnitTest);
			assertEquals(_expectedTransitionText, fileContents);
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertFromPersistence() {
		try {
			_manager = new XmlConfigurationManager(_transitionUnitTest2);
			TransitionPersistenceObject persist = new TransitionPersistenceObject();
			
			persist.displayType(DisplayType.SplashScreen);
			persist.mediaLocation("C:/something.avi");
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(_transitionUnitTest2);
			manager2.load();
			
			TransitionPersistenceObject persist2 = new TransitionPersistenceObject();
			persist2.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals(DisplayType.SplashScreen, persist2.displayType());
			assertEquals("C:/something.avi", persist2.mediaLocation());
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
