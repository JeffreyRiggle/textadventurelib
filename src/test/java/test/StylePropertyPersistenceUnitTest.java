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
import textadventurelib.persistence.StyleType;

public class StylePropertyPersistenceUnitTest {

	private final String propertyUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/stylePropertyTest.xml";
	private final String expectedProperty = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><StyleProperty styleType=\"Background\" styleValue=\"#343434\"/>";
	
	@Test
	public void testCreate() {
		try {
			StylePropertyPersistenceObject property = new StylePropertyPersistenceObject();
			assertNotNull(property);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testValue() {
		try {
			StylePropertyPersistenceObject property = new StylePropertyPersistenceObject();
			
			property.setPropertyValue("#343434");
			assertEquals("#343434", property.getPropertyValue());
			
			property.setPropertyValue("Times New Roman");
			assertEquals("Times New Roman", property.getPropertyValue());
			
			property.setPropertyValue("14");
			assertEquals("14", property.getPropertyValue());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testType() {
		try {
			StylePropertyPersistenceObject property = new StylePropertyPersistenceObject();
			assertEquals(StyleType.None, property.getPropertyType());
			
			property.setPropertyType(StyleType.Background);
			assertEquals(StyleType.Background, property.getPropertyType());
			
			property.setPropertyType(StyleType.Color);
			assertEquals(StyleType.Color, property.getPropertyType());
			
			property.setPropertyType(StyleType.FontFamily);
			assertEquals(StyleType.FontFamily, property.getPropertyType());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCompileWithNoType() {
		try {
			StylePropertyPersistenceObject property = new StylePropertyPersistenceObject();
			property.setPropertyValue("Times New Roman");
			assertTrue(property.compile().isEmpty());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCompileColor() {
		try {
			StylePropertyPersistenceObject property = new StylePropertyPersistenceObject();
			
			property.setPropertyType(StyleType.Color);
			property.setPropertyValue("#343434");
			
			assertEquals("-fx-text-fill : #343434;", property.compile());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCompileBackgroundColor() {
		try {
			StylePropertyPersistenceObject property = new StylePropertyPersistenceObject();
			
			property.setPropertyType(StyleType.Background);
			property.setPropertyValue("#343434");
			
			assertEquals("-fx-background-color : #343434;", property.compile());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCompileBackgroundImage() {
		try {
			StylePropertyPersistenceObject property = new StylePropertyPersistenceObject();

			property.setPropertyType(StyleType.Background);
			property.setPropertyValue("https://somesite/someimage.jpg");
			
			assertEquals("-fx-background-image : url(https://somesite/someimage.jpg);", property.compile());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCompileBackgroundSize() {
		try {
			StylePropertyPersistenceObject property = new StylePropertyPersistenceObject();

			property.setPropertyType(StyleType.BackgroundSize);
			property.setPropertyValue("stretch");
			
			assertEquals("-fx-background-size : stretch;", property.compile());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCompileBackgroundRepeat() {
		try {
			StylePropertyPersistenceObject property = new StylePropertyPersistenceObject();

			property.setPropertyType(StyleType.BackgroundRepeat);
			property.setPropertyValue("no-repeat");
			
			assertEquals("-fx-background-repeat : no-repeat;", property.compile());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCompileFontFamily() {
		try {
			StylePropertyPersistenceObject property = new StylePropertyPersistenceObject();

			property.setPropertyType(StyleType.FontFamily);
			property.setPropertyValue("Times New Roman");
			
			assertEquals("-fx-font-family : \"Times New Roman\";", property.compile());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCompileFontSize() {
		try {
			StylePropertyPersistenceObject property = new StylePropertyPersistenceObject();

			property.setPropertyType(StyleType.FontSize);
			property.setPropertyValue("14");
			
			assertEquals("-fx-font-size : 14;", property.compile());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCompileFontStyle() {
		try {
			StylePropertyPersistenceObject property = new StylePropertyPersistenceObject();

			property.setPropertyType(StyleType.FontStyle);
			property.setPropertyValue("normal");
			
			assertEquals("-fx-font-style : normal;", property.compile());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testCompileFontStyleBold() {
		try {
			StylePropertyPersistenceObject property = new StylePropertyPersistenceObject();

			property.setPropertyType(StyleType.FontStyle);
			property.setPropertyValue("bold");
			
			assertEquals("-fx-font-weight : bold;", property.compile());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSave() {
		try {
			XmlConfigurationManager manager = new XmlConfigurationManager(propertyUnitTest);
			
			StylePropertyPersistenceObject property = new StylePropertyPersistenceObject();
			property.setPropertyType(StyleType.Background);
			property.setPropertyValue("#343434");
			
			property.prepareXml();
			manager.addConfigurationObject(property);
			manager.save();
			
			String fileContents = fileContents(propertyUnitTest);
			assertEquals(expectedProperty, fileContents);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testConvert() {
		try {
			XmlConfigurationManager manager = new XmlConfigurationManager(propertyUnitTest);
			manager.load();
			
			StylePropertyPersistenceObject property = new StylePropertyPersistenceObject();
			property.convertFromPersistence((XmlConfigurationObject)manager.configurationObjects().get(0));
			
			assertEquals(StyleType.Background, property.getPropertyType());
			assertEquals("#343434", property.getPropertyValue());
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
