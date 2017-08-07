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
import textadventurelib.persistence.StylePersistenceObject;
import textadventurelib.persistence.StylePropertyPersistenceObject;
import textadventurelib.persistence.StyleSelectorPersistenceObject;
import textadventurelib.persistence.StyleType;

public class StylePersistenceUnitTest {

	private final String styleUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/styleTest.xml";
	private final String expectedStyle = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Style><Selectors><StyleSelector selector=\"#someId\"><StyleProperties><StyleProperty styleType=\"Background\" styleValue=\"#343434\"/></StyleProperties></StyleSelector></Selectors></Style>";
	private final String compiledStyle = "#someId {\n\t-fx-background-color : #343434;\n}\n.button {\n\t-fx-background-color : #343434;\n}\n.button:hover {\n\t-fx-background-color : #343434;\n}\n";
	
	@Test
	public void testCreate() {
		try {
			StylePersistenceObject style = new StylePersistenceObject();
			assertNotNull(style);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testAddSelector() {
		try {
			StylePersistenceObject style = new StylePersistenceObject();
			StyleSelectorPersistenceObject selector = new StyleSelectorPersistenceObject();
			StylePropertyPersistenceObject prop1 = new StylePropertyPersistenceObject();
			prop1.setPropertyType(StyleType.Background);
			prop1.setPropertyValue("#343434");
			
			selector.setSelector("#someId");
			selector.addStyleProperty(prop1);
			
			StyleSelectorPersistenceObject selector2 = new StyleSelectorPersistenceObject();
			StylePropertyPersistenceObject prop2 = new StylePropertyPersistenceObject();
			prop2.setPropertyType(StyleType.Background);
			prop2.setPropertyValue("#343434");
			
			selector2.setSelector(".button");
			selector2.addStyleProperty(prop2);
			
			StyleSelectorPersistenceObject selector3 = new StyleSelectorPersistenceObject();
			StylePropertyPersistenceObject prop3 = new StylePropertyPersistenceObject();
			prop3.setPropertyType(StyleType.Background);
			prop3.setPropertyValue("#343434");
			
			selector3.setSelector(".button:hover");
			selector3.addStyleProperty(prop3);
			
			style.addSelector(selector);
			assertTrue(style.getSelectors().contains(selector));
			
			style.addSelector(selector2);
			assertTrue(style.getSelectors().contains(selector2));
			
			style.addSelector(selector3);
			assertTrue(style.getSelectors().contains(selector3));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testRemoveSelector() {
		try {
			StylePersistenceObject style = new StylePersistenceObject();
			StyleSelectorPersistenceObject selector = new StyleSelectorPersistenceObject();
			StylePropertyPersistenceObject prop1 = new StylePropertyPersistenceObject();
			prop1.setPropertyType(StyleType.Background);
			prop1.setPropertyValue("#343434");
			
			selector.setSelector("#someId");
			selector.addStyleProperty(prop1);
			
			StyleSelectorPersistenceObject selector2 = new StyleSelectorPersistenceObject();
			StylePropertyPersistenceObject prop2 = new StylePropertyPersistenceObject();
			prop2.setPropertyType(StyleType.Background);
			prop2.setPropertyValue("#343434");
			
			selector2.setSelector(".button");
			selector2.addStyleProperty(prop2);
			
			StyleSelectorPersistenceObject selector3 = new StyleSelectorPersistenceObject();
			StylePropertyPersistenceObject prop3 = new StylePropertyPersistenceObject();
			prop3.setPropertyType(StyleType.Background);
			prop3.setPropertyValue("#343434");
			
			selector3.setSelector(".button:hover");
			selector3.addStyleProperty(prop3);
			
			style.addSelector(selector);
			style.addSelector(selector2);
			style.addSelector(selector3);

			style.removeSelector(selector3);
			assertFalse(style.getSelectors().contains(selector3));
			
			style.removeSelector(selector2);
			assertFalse(style.getSelectors().contains(selector2));
			
			style.removeSelector(selector);
			assertFalse(style.getSelectors().contains(selector));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCompile() {
		try {
			StylePersistenceObject style = new StylePersistenceObject();
			StyleSelectorPersistenceObject selector = new StyleSelectorPersistenceObject();
			StylePropertyPersistenceObject prop1 = new StylePropertyPersistenceObject();
			prop1.setPropertyType(StyleType.Background);
			prop1.setPropertyValue("#343434");
			
			selector.setSelector("#someId");
			selector.addStyleProperty(prop1);
			
			StyleSelectorPersistenceObject selector2 = new StyleSelectorPersistenceObject();
			StylePropertyPersistenceObject prop2 = new StylePropertyPersistenceObject();
			prop2.setPropertyType(StyleType.Background);
			prop2.setPropertyValue("#343434");
			
			selector2.setSelector(".button");
			selector2.addStyleProperty(prop2);
			
			StyleSelectorPersistenceObject selector3 = new StyleSelectorPersistenceObject();
			StylePropertyPersistenceObject prop3 = new StylePropertyPersistenceObject();
			prop3.setPropertyType(StyleType.Background);
			prop3.setPropertyValue("#343434");
			
			selector3.setSelector(".button:hover");
			selector3.addStyleProperty(prop3);
			
			style.addSelector(selector);
			style.addSelector(selector2);
			style.addSelector(selector3);
			
			assertEquals(compiledStyle, style.compile());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSave() {
		try {
			XmlConfigurationManager manager = new XmlConfigurationManager(styleUnitTest);
			StylePersistenceObject style = new StylePersistenceObject();
			StyleSelectorPersistenceObject selector = new StyleSelectorPersistenceObject();
			StylePropertyPersistenceObject prop1 = new StylePropertyPersistenceObject();
			prop1.setPropertyType(StyleType.Background);
			prop1.setPropertyValue("#343434");
			
			selector.setSelector("#someId");
			selector.addStyleProperty(prop1);
			
			style.addSelector(selector);
			style.prepareXml();
			manager.addConfigurationObject(style);
			manager.save();
			
			String fileContents = fileContents(styleUnitTest);
			assertEquals(expectedStyle, fileContents);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testConvert() {
		try {
			try {
				XmlConfigurationManager manager = new XmlConfigurationManager(styleUnitTest);
				manager.load();
				
				StylePersistenceObject style = new StylePersistenceObject();
				style.convertFromPersistence((XmlConfigurationObject)manager.configurationObjects().get(0));
				
				assertEquals(1, style.getSelectors().size());
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
