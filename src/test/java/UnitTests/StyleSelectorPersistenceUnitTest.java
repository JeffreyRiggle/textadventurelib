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
import textadventurelib.persistence.StylePropertyPersistenceObject;
import textadventurelib.persistence.StyleSelectorPersistenceObject;
import textadventurelib.persistence.StyleType;

public class StyleSelectorPersistenceUnitTest {

	private final String selectorUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/styleSelectorTest.xml";
	private final String expectedSelector = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><StyleSelector selector=\"#someId\"><StyleProperties><StyleProperty styleType=\"Background\" styleValue=\"#343434\"/></StyleProperties></StyleSelector>";
	
	@Test
	public void testCreate() {
		try {
			StyleSelectorPersistenceObject selector = new StyleSelectorPersistenceObject();
			assertNotNull(selector);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testSelector() {
		try {
			StyleSelectorPersistenceObject selector = new StyleSelectorPersistenceObject();

			selector.setSelector(".button");
			assertEquals(".button", selector.getSelector());
			
			selector.setSelector("#someId");
			assertEquals("#someId", selector.getSelector());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testAddProperty() {
		try {
			StyleSelectorPersistenceObject selector = new StyleSelectorPersistenceObject();
			StylePropertyPersistenceObject prop1 = new StylePropertyPersistenceObject();
			prop1.setPropertyType(StyleType.Background);
			prop1.setPropertyValue("#343434");
			
			StylePropertyPersistenceObject prop2 = new StylePropertyPersistenceObject();
			prop2.setPropertyType(StyleType.Color);
			prop2.setPropertyValue("#343434");
			
			StylePropertyPersistenceObject prop3 = new StylePropertyPersistenceObject();
			prop3.setPropertyType(StyleType.FontFamily);
			prop3.setPropertyValue("Times New Roman");
			
			selector.addStyleProperty(prop1);
			assertTrue(selector.getStyleProperties().contains(prop1));
			
			selector.addStyleProperty(prop2);
			assertTrue(selector.getStyleProperties().contains(prop2));
			
			selector.addStyleProperty(prop3);
			assertTrue(selector.getStyleProperties().contains(prop3));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testRemoveProperty() {
		try {
			StyleSelectorPersistenceObject selector = new StyleSelectorPersistenceObject();
			StylePropertyPersistenceObject prop1 = new StylePropertyPersistenceObject();
			prop1.setPropertyType(StyleType.Background);
			prop1.setPropertyValue("#343434");
			
			StylePropertyPersistenceObject prop2 = new StylePropertyPersistenceObject();
			prop2.setPropertyType(StyleType.Color);
			prop2.setPropertyValue("#343434");
			
			StylePropertyPersistenceObject prop3 = new StylePropertyPersistenceObject();
			prop3.setPropertyType(StyleType.FontFamily);
			prop3.setPropertyValue("Times New Roman");
			
			selector.addStyleProperty(prop1);
			selector.addStyleProperty(prop2);
			selector.addStyleProperty(prop3);
			selector.removeStyleProperty(prop3);
			assertFalse(selector.getStyleProperties().contains(prop3));
			
			selector.removeStyleProperty(prop2);
			assertFalse(selector.getStyleProperties().contains(prop2));
			
			selector.removeStyleProperty(prop1);
			assertFalse(selector.getStyleProperties().contains(prop1));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCompile() {
		try {
			StyleSelectorPersistenceObject selector = new StyleSelectorPersistenceObject();
			StylePropertyPersistenceObject prop1 = new StylePropertyPersistenceObject();
			prop1.setPropertyType(StyleType.Background);
			prop1.setPropertyValue("#343434");
			
			selector.setSelector("#someId");
			selector.addStyleProperty(prop1);
			
			assertEquals("#someId {\n\t-fx-background-color : #343434;\n}\n", selector.compile());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSave() {
		try {
			XmlConfigurationManager manager = new XmlConfigurationManager(selectorUnitTest);
			StyleSelectorPersistenceObject selector = new StyleSelectorPersistenceObject();
			StylePropertyPersistenceObject prop1 = new StylePropertyPersistenceObject();
			prop1.setPropertyType(StyleType.Background);
			prop1.setPropertyValue("#343434");
			
			selector.setSelector("#someId");
			selector.addStyleProperty(prop1);
			
			selector.prepareXml();
			manager.addConfigurationObject(selector);
			manager.save();
			
			String fileContents = fileContents(selectorUnitTest);
			assertEquals(expectedSelector, fileContents);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testConvert() {
		try {
			try {
				XmlConfigurationManager manager = new XmlConfigurationManager(selectorUnitTest);
				manager.load();
				
				StyleSelectorPersistenceObject selector = new StyleSelectorPersistenceObject();
				selector.convertFromPersistence((XmlConfigurationObject)manager.configurationObjects().get(0));
				
				assertEquals("#someId", selector.getSelector());
				assertEquals(1, selector.getStyleProperties().size());
			} catch (Exception e) {
				fail(e.getMessage());
			}
		} catch (Exception e) {
			fail(e.getMessage());
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
