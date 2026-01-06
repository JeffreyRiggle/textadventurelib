import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import playerlib.items.IProperty;
import playerlib.items.Property;
import textadventurelib.persistence.player.PropertyPersistenceObject;

public class PropertyPersistenceUnitTest {

	private XmlConfigurationManager _manager;
	private final String _propertyUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/propertyUnitTest.xml";
	private final String _propertyUnitTest2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/propertyUnitTest2.xml";
	private final String _expectedPropertyText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><NamedObject type=\"Property\"><Name ValueType=\"string\">HPUP</Name><Description ValueType=\"string\">Restores HP</Description><Value ValueType=\"int\">50</Value></NamedObject>";
	
	@Test
	public void testCreate() {
		try {
			PropertyPersistenceObject persist = new PropertyPersistenceObject();
			
			assertNotNull(persist);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testPropertyCtor() {
		try {
			IProperty prop = new Property("Breakable", true);
			prop.description("If an item can be broken.");
			
			PropertyPersistenceObject persist = new PropertyPersistenceObject(prop);
			
			assertEquals("Breakable", persist.objectName());
			assertEquals(true, persist.<Boolean>objectValue());
			assertEquals("If an item can be broken.", persist.description());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testCreateFromProperty() {
		try {
			IProperty prop = new Property("Breakable", true);
			prop.description("If an item can be broken.");
			
			PropertyPersistenceObject persist = new PropertyPersistenceObject();
			persist.createFromProperty(prop);
			
			assertEquals("Breakable", persist.objectName());
			assertEquals(true, persist.<Boolean>objectValue());
			assertEquals("If an item can be broken.", persist.description());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetName() {
		try {
			PropertyPersistenceObject persist = new PropertyPersistenceObject();
			
			persist.objectName("HPUP");
			assertEquals("HPUP", persist.objectName());
			
			persist.objectName("MPUP");
			assertEquals("MPUP", persist.objectName());
			
			persist.objectName("Key");
			assertEquals("Key", persist.objectName());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetValue() {
		try {
			PropertyPersistenceObject persist = new PropertyPersistenceObject();
			
			persist.objectValue(13);
			assertEquals(13, persist.<Object>objectValue());
			
			persist.objectValue(50);
			assertEquals(50, persist.<Object>objectValue());
			
			persist.objectValue(true);
			assertEquals(true, persist.objectValue());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testSetDescription() {
		try {
			PropertyPersistenceObject persist = new PropertyPersistenceObject();
			
			persist.description("Restores HP");
			assertEquals("Restores HP", persist.description());
			
			persist.description("Restores MP");
			assertEquals("Restores MP", persist.description());
			
			persist.description("A critical item");
			assertEquals("A critical item", persist.description());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			_manager = new XmlConfigurationManager(_propertyUnitTest);
			PropertyPersistenceObject persist = new PropertyPersistenceObject();
			
			persist.objectName("HPUP");
			persist.objectValue(50);
			persist.description("Restores HP");
			
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			String fileContents = fileContents(_propertyUnitTest);
			assertEquals(_expectedPropertyText, fileContents);
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testConvertToProperty() {
		try {
			PropertyPersistenceObject persist = new PropertyPersistenceObject();
			
			persist.objectName("HPUP");
			persist.objectValue(50);
			persist.description("Restores HP");
			
			IProperty prop = persist.convertToProperty();
			assertEquals("HPUP", prop.name());
			assertEquals(50, prop.<Object>value());
			assertEquals("Restores HP", prop.description());
		} catch(Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testConvertFromXml() {
		try {
			_manager = new XmlConfigurationManager(_propertyUnitTest2);
			PropertyPersistenceObject persist = new PropertyPersistenceObject();
			
			persist.objectName("HPUP");
			persist.objectValue(50);
			persist.description("Restores HP");
			
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(_propertyUnitTest2);
			manager2.load();
			
			PropertyPersistenceObject persist2 = new PropertyPersistenceObject();
			persist2.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals("HPUP", persist2.objectName());
			assertEquals(50, persist2.<Object>objectValue());
			assertEquals("Restores HP", persist2.description());
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
