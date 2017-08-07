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
import textadventurelib.persistence.MatchType;
import textadventurelib.persistence.TextTriggerPersistenceObject;
import textadventurelib.triggers.TextTrigger;

public class TextTriggerPersistenceUnitTest {

	private XmlConfigurationManager _manager;
	private final String _textTriggerUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/textTriggerUnitTest.xml";
	private final String _textTriggerUnitTest2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/textTriggerUnitTest2.xml";
	
	private final String _expectedTextTriggerText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Trigger type=\"Text\"><Parameters><Text ValueType=\"string\">Simple Test</Text><CaseSensitive ValueType=\"bool\">false</CaseSensitive><MatchType ValueType=\"object\">Contains</MatchType></Parameters></Trigger>";

	private final String _text1 = "Simple Test";
	private final String _text2 = "Matcher text";
	private final String _text3 = "Something";
	
	private final String _reg1 = "Simple Test";
	private final String _reg2 = "(?i).*Simple Test.*";
	private final String _reg3 = "(?i).*Simple Test";
	private final String _reg4 = "(?i)Simple Test.*";
	
	@Test
	public void testCreate() {
		try {
			TextTriggerPersistenceObject persist = new TextTriggerPersistenceObject();
			
			assertNotNull(persist);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testSetText() {
		try {
			TextTriggerPersistenceObject persist = new TextTriggerPersistenceObject();
			
			persist.text(_text1);
			assertEquals(_text1, persist.text());
			
			persist.text(_text2);
			assertEquals(_text2, persist.text());
			
			persist.text(_text3);
			assertEquals(_text3, persist.text());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetCaseSensitive() {
		try {
			TextTriggerPersistenceObject persist = new TextTriggerPersistenceObject();
			
			persist.caseSensitive(true);
			assertTrue(persist.caseSensitive());
			
			persist.caseSensitive(false);
			assertTrue(!persist.caseSensitive());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetMatchType() {
		try {
			TextTriggerPersistenceObject persist = new TextTriggerPersistenceObject();
			
			persist.matchType(MatchType.Contains);
			assertEquals(MatchType.Contains, persist.matchType());
			
			persist.matchType(MatchType.Exact);
			assertEquals(MatchType.Exact, persist.matchType());
			
			persist.matchType(MatchType.Prefix);
			assertEquals(MatchType.Prefix, persist.matchType());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testRegexConversion() {
		try {
			TextTriggerPersistenceObject persist = new TextTriggerPersistenceObject();
			persist.text(_text1);
			persist.matchType(MatchType.Exact);
			persist.caseSensitive(true);
			
			assertEquals(_reg1, persist.toRegEx());
			
			persist.caseSensitive(false);
			persist.matchType(MatchType.Contains);
			assertEquals(_reg2, persist.toRegEx());
			
			persist.matchType(MatchType.Postfix);
			assertEquals(_reg3, persist.toRegEx());
			
			persist.matchType(MatchType.Prefix);
			assertEquals(_reg4, persist.toRegEx());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			_manager = new XmlConfigurationManager(_textTriggerUnitTest);
			TextTriggerPersistenceObject persistence = new TextTriggerPersistenceObject();
			
			persistence.text(_text1);
			persistence.caseSensitive(false);
			persistence.matchType(MatchType.Contains);
			
			persistence.prepareXml();
			_manager.addConfigurationObject(persistence);
			_manager.save();
			
			String fileContents = fileContents(_textTriggerUnitTest);
			assertEquals(_expectedTextTriggerText, fileContents);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertToTrigger() {
		try {
			TextTriggerPersistenceObject persistence = new TextTriggerPersistenceObject();
			
			persistence.text(_text1);
			persistence.caseSensitive(false);
			persistence.matchType(MatchType.Contains);
			
			TextTrigger trigger = (TextTrigger)persistence.convertToTrigger();
			
			assertEquals(_reg2, trigger.<String>condition());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertFromPersistence() {
		try {
			_manager = new XmlConfigurationManager(_textTriggerUnitTest2);
			TextTriggerPersistenceObject persistence = new TextTriggerPersistenceObject();
			
			persistence.text(_text1);
			persistence.caseSensitive(false);
			persistence.matchType(MatchType.Contains);
			
			persistence.prepareXml();
			_manager.addConfigurationObject(persistence);
			_manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(_textTriggerUnitTest2);
			manager2.load();
			TextTriggerPersistenceObject persist = new TextTriggerPersistenceObject();
			
			persist.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals(_text1, persist.text());
			assertEquals(false, persist.caseSensitive());
			assertEquals(MatchType.Contains, persist.matchType());
			
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
