import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.actions.CompletionAction;
import textadventurelib.core.ICompletionListener;
import textadventurelib.persistence.CompletionActionPersistence;

public class CompletionPersistenceUnitTest {

	private boolean _bCompletionData = true;
	private int _iCompletionData = 12;
	private String _strCompletionData = "Complete";
	private XmlConfigurationManager _manager;
	
	private final String _completeActionUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/completeUnitTest.xml";
	private final String _completeActionUnitTest2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/completeUnitTest2.xml";
	private final String _expectedCompleteActionText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Action type=\"Completion\"><Parameters><CompletionData ValueType=\"string\">Complete</CompletionData></Parameters></Action>";

	
	@Test
	public void testCreate() {
		try {
			CompletionActionPersistence action = new CompletionActionPersistence();
			
			assertNotNull(action);
			//TODO: Check type.
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetData() {
		try {
			CompletionActionPersistence action = new CompletionActionPersistence();
			
			action.completionData(_bCompletionData);
			assertEquals(_bCompletionData, action.completionData());
			
			action.completionData(_iCompletionData);
			assertEquals(_iCompletionData, action.<Object>completionData());
			
			action.completionData(_strCompletionData);
			assertEquals(_strCompletionData, action.completionData());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			_manager = new XmlConfigurationManager(_completeActionUnitTest);
			CompletionActionPersistence action = new CompletionActionPersistence();
			
			action.completionData(_strCompletionData);
			action.prepareXml();
			_manager.addConfigurationObject(action);
			_manager.save();
			
			String fileContents = fileContents(_completeActionUnitTest);
			assertEquals(_expectedCompleteActionText, fileContents);
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertToAction() {
		try {
			CompletionActionPersistence persist = new CompletionActionPersistence();
			persist.completionData(_strCompletionData);
			
			MessageListener listener = new MessageListener();
			CompletionAction action = (CompletionAction)persist.convertToAction();
			
			action.addListener(listener);
			action.execute(null);
			
			assertEquals(_strCompletionData, listener.cachedMessage());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertFromPersistence() {
		try {
			_manager = new XmlConfigurationManager(_completeActionUnitTest2);
			CompletionActionPersistence action = new CompletionActionPersistence();
			
			action.completionData(_strCompletionData);
			action.prepareXml();
			_manager.addConfigurationObject(action);
			_manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(_completeActionUnitTest2);
			manager2.load();
			
			CompletionActionPersistence persist = new CompletionActionPersistence();
			persist.persistFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals(_strCompletionData, persist.<String>completionData());
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
	
	private class MessageListener implements ICompletionListener {
		private String _cachedValue;
		
		public MessageListener() {
			_cachedValue = new String();
		}
		
		public String cachedMessage() {
			return _cachedValue;
		}

		@Override
		public <T> void completed(T data) {
			_cachedValue = (String)data;
		}
	}
}
