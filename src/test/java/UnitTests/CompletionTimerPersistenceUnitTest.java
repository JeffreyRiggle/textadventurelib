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
import textadventurelib.persistence.CompletionTimerPersistenceObject;
import textadventurelib.timers.TimerHelper;

public class CompletionTimerPersistenceUnitTest {

	private XmlConfigurationManager _manager;

	private final String _completionTimerUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/completionTimerUnitTest.xml";
	private final String _completionTimerUnitTest2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/completionTimerUnitTest2.xml";
	
	private final String _expectedCompletionTimerText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Timer type=\"Completion\"><Duration ValueType=\"object\">5000</Duration><CompletionData ValueType=\"string\">SomeData</CompletionData></Timer>";

	private final long _duration1 = 5000;
	private final long _duration2 = 25000;
	private final long _duration3 = 45000;
	private final long initialValue = 0;
	
	private final String _completionData1 = "SomeData";
	private final String _completionData2 = "NextState";
	private final String _completionData3 = "AnotherState";
	
	@Test
	public void testCreate() {
		try {
			CompletionTimerPersistenceObject persist = new CompletionTimerPersistenceObject();
			
			assertNotNull(persist);
			assertEquals(initialValue, persist.duration());
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testSetDuration() {
		try {
			CompletionTimerPersistenceObject persist = new CompletionTimerPersistenceObject();
			
			persist.duration(_duration1);
			assertEquals(_duration1, persist.duration());
			
			persist.duration(_duration2);
			assertEquals(_duration2, persist.duration());
			
			persist.duration(_duration3);
			assertEquals(_duration3, persist.duration());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetCompletionData() {
		try {
			CompletionTimerPersistenceObject persist = new CompletionTimerPersistenceObject();
			
			persist.completionData(_completionData1);
			assertEquals(_completionData1, persist.completionData());
			
			persist.completionData(_completionData2);
			assertEquals(_completionData2, persist.completionData());
			
			persist.completionData(_completionData3);
			assertEquals(_completionData3, persist.completionData());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			_manager = new XmlConfigurationManager(_completionTimerUnitTest);
			CompletionTimerPersistenceObject persist = new CompletionTimerPersistenceObject();
			
			persist.completionData(_completionData1);
			persist.duration(_duration1);
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			String fileContents = fileContents(_completionTimerUnitTest);
			assertEquals(_expectedCompletionTimerText, fileContents);
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertToTimer() {
		try {
			CompletionTimerPersistenceObject persist = new CompletionTimerPersistenceObject();
			
			persist.completionData(_completionData1);
			persist.duration(_duration1);
			
			TimerHelper timer = persist.convertToTimer();
			
			assertEquals(_duration1, timer.duration());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertFromPersistence() {
		try {
			_manager = new XmlConfigurationManager(_completionTimerUnitTest2);
			CompletionTimerPersistenceObject persist = new CompletionTimerPersistenceObject();
			
			persist.completionData(_completionData1);
			persist.duration(_duration1);
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();

			XmlConfigurationManager manager2 = new XmlConfigurationManager(_completionTimerUnitTest2);
			manager2.load();
			
			CompletionTimerPersistenceObject persist2 = new CompletionTimerPersistenceObject();
			persist2.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals(_completionData1, persist2.completionData());
			assertEquals(_duration1, persist2.duration());
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
