import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import playerlib.attributes.Attribute;
import playerlib.attributes.IAttribute;
import textadventurelib.persistence.player.AttributePersistenceObject;

public class AttributePersistenceUnitTest {

	private XmlConfigurationManager _manager;
	private final String _attributeUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/attributeUnitTest.xml";
	private final String _attributeUnitTest2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/attributeUnitTest2.xml";
	
	private final String _expectedAttributeText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><NamedObject type=\"Attribute\"><Name ValueType=\"string\">Class</Name><Description ValueType=\"string\">The type of player</Description><Value ValueType=\"string\">Wizard</Value></NamedObject>";
	
	@Test
	public void testCreate() {
		try {
			AttributePersistenceObject persist = new AttributePersistenceObject();
			
			assertNotNull(persist);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testAttributeCtort() {
		try {
			IAttribute clas = new Attribute("Class", "Wizard");
			clas.description("The players class");
			
			AttributePersistenceObject persist = new AttributePersistenceObject(clas);
			
			assertEquals("Class", persist.objectName());
			assertEquals("Wizard", persist.objectValue());
			assertEquals("The players class", persist.description());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testCreateFromAttribute() {
		try {
			IAttribute clas = new Attribute("Class", "Wizard");
			clas.description("The players class");
			
			AttributePersistenceObject persist = new AttributePersistenceObject();
			persist.createFromAttribute(clas);
			
			assertEquals("Class", persist.objectName());
			assertEquals("Wizard", persist.objectValue());
			assertEquals("The players class", persist.description());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetName() {
		try {
			AttributePersistenceObject persist = new AttributePersistenceObject();
			
			persist.objectName("Class");
			assertEquals("Class", persist.objectName());
			
			persist.objectName("Age");
			assertEquals("Age", persist.objectName());
			
			persist.objectName("HP");
			assertEquals("HP", persist.objectName());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetValue() {
		try {
			AttributePersistenceObject persist = new AttributePersistenceObject();
			
			persist.objectValue("Wizard");
			assertEquals("Wizard", persist.objectValue());
			
			persist.objectValue(13);
			assertEquals(13, persist.<Object>objectValue());
			
			persist.objectValue(5000);
			assertEquals(5000, persist.<Object>objectValue());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testSetDescription() {
		try {
			AttributePersistenceObject persist = new AttributePersistenceObject();
			
			persist.description("The type of player");
			assertEquals("The type of player", persist.description());
			
			persist.description("How old the player is");
			assertEquals("How old the player is", persist.description());
			
			persist.description("How much health the player has");
			assertEquals("How much health the player has", persist.description());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			_manager = new XmlConfigurationManager(_attributeUnitTest);
			AttributePersistenceObject persist = new AttributePersistenceObject();
			
			persist.objectName("Class");
			persist.objectValue("Wizard");
			persist.description("The type of player");
			
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			String fileContents = fileContents(_attributeUnitTest);
			assertEquals(_expectedAttributeText, fileContents);
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testAttibuteConversion() {
		try {
			AttributePersistenceObject persist = new AttributePersistenceObject();
			
			persist.objectName("Class");
			persist.objectValue("Wizard");
			persist.description("The type of player");
			
			IAttribute attrib = persist.convertToAttribute();
			
			assertEquals("Class", attrib.name());
			assertEquals("Wizard", attrib.value());
			assertEquals("The type of player", attrib.description());
			
			persist.objectName("HP");
			persist.objectValue(5000);
			persist.description("The amount of health a player has");
			
			IAttribute attrib2 = persist.convertToAttribute();
			
			assertEquals("HP", attrib2.name());
			assertEquals(5000, attrib2.<Object>value());
			assertEquals("The amount of health a player has", attrib2.description());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertFromXml() {
		try {
			_manager = new XmlConfigurationManager(_attributeUnitTest2);
			AttributePersistenceObject persist = new AttributePersistenceObject();
			
			persist.objectName("HP");
			persist.objectValue(5000);
			persist.description("The amount of health a player has");
			
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(_attributeUnitTest2);
			manager2.load();
			
			AttributePersistenceObject persist2 = new AttributePersistenceObject();
			persist2.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals("HP", persist2.objectName());
			assertEquals(5000, persist2.<Object>objectValue());
			assertEquals("The amount of health a player has", persist2.description());
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
