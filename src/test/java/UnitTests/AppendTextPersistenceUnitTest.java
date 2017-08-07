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
import textadventurelib.actions.AppendTextAction;
import textadventurelib.core.IMessageListener;
import textadventurelib.persistence.AppendTextActionPersistence;

public class AppendTextPersistenceUnitTest {
	
	private XmlConfigurationManager _manager;
	private final String _appendText1 = "This is the first append text test";
	private final String _appendText2 = "This is the second append text test";
	
	private final String _appendActionUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/appendUnitTest.xml";
	private final String _appendActionUnitTest2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/appendUnitTest2.xml";
	private final String _expectedAppendActionText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Action type=\"AppendText\"><Parameters><AppendText ValueType=\"string\">This is the first append text test</AppendText></Parameters></Action>";
	
	@Test
	public void testCreate() {
		try {
			AppendTextActionPersistence persistence = new AppendTextActionPersistence();
			
			assertNotNull(persistence.appendText());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetText() {
		try {
			AppendTextActionPersistence persistence = new AppendTextActionPersistence();
			assertNotNull(persistence.appendText());
			
			persistence.appendText(_appendText1);
			assertEquals(_appendText1, persistence.appendText());
			
			persistence.appendText(_appendText2);
			assertEquals(_appendText2, persistence.appendText());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			_manager = new XmlConfigurationManager(_appendActionUnitTest);
			AppendTextActionPersistence persistence = new AppendTextActionPersistence();
			
			persistence.appendText(_appendText1);
			persistence.prepareXml();
			_manager.addConfigurationObject(persistence);
			_manager.save();
			
			String fileContents = fileContents(_appendActionUnitTest);
			assertEquals(_expectedAppendActionText, fileContents);
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertToAction() {
		try {
			AppendTextActionPersistence persistence = new AppendTextActionPersistence();
			
			persistence.appendText("This is some game state text");
			AppendTextAction action = (AppendTextAction)persistence.convertToAction();
			
			MessageListener listener = new MessageListener();
			action.addListener(listener);
			action.execute(null);
			
			assertEquals("This is some game state text", listener.cachedMessage());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertFromAction() {
		try {
			_manager = new XmlConfigurationManager(_appendActionUnitTest2);
			AppendTextActionPersistence persistence = new AppendTextActionPersistence();
			
			persistence.appendText(_appendText1);
			persistence.prepareXml();
			_manager.addConfigurationObject(persistence);
			_manager.save();

			XmlConfigurationManager manager2 = new XmlConfigurationManager(_appendActionUnitTest2);
			manager2.load();
			
			AppendTextActionPersistence persist2 = new AppendTextActionPersistence();
			persist2.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals(_appendText1, persist2.appendText());
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
	
	private class MessageListener implements IMessageListener {
		private String _cachedValue;
		
		public MessageListener() {
			_cachedValue = new String();
		}
		
		@Override
		public void sendMessage(String message) {
			
		}

		@Override
		public void sendMessageNoProcessing(String message) {
			_cachedValue = message;
		}
		
		public String cachedMessage() {
			return _cachedValue;
		}
	}
}
