import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import textadventurelib.persistence.LayoutInfoPersistenceObject;
import textadventurelib.persistence.LayoutType;

public class LayoutInfoPersistenceUnitTest {

	private final String layoutUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/layoutInfoUnitTest.xml";
	private final String expectedLayout = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><LayoutInfo><LayoutID ValueType=\"string\">Id1</LayoutID><LayoutType ValueType=\"object\">Custom</LayoutType><LayoutContent ValueType=\"string\">c:/test/testfile.html</LayoutContent></LayoutInfo>";
	
	@Test
	public void testCreate() {
		try {
			LayoutInfoPersistenceObject info = new LayoutInfoPersistenceObject();
			assertNotNull(info);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testSetId() {
		try {
			LayoutInfoPersistenceObject info = new LayoutInfoPersistenceObject();
			
			info.setLayoutId("Id1");
			assertEquals("Id1", info.getLayoutId());
			
			info.setLayoutId("IDD1");
			assertEquals("IDD1", info.getLayoutId());
			
			info.setLayoutId("ISF12");
			assertEquals("ISF12", info.getLayoutId());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetLayoutType() {
		try {
			LayoutInfoPersistenceObject info = new LayoutInfoPersistenceObject();
			
			info.setLayoutType(LayoutType.TextAndContentWithButtonInput);
			assertEquals(LayoutType.TextAndContentWithButtonInput, info.getLayoutType());
			
			info.setLayoutType(LayoutType.Custom);
			assertEquals(LayoutType.Custom, info.getLayoutType());
			
			info.setLayoutType(LayoutType.TextWithTextInput);
			assertEquals(LayoutType.TextWithTextInput, info.getLayoutType());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetContent() {
		try {
			LayoutInfoPersistenceObject info = new LayoutInfoPersistenceObject();
			
			info.setLayoutContent("c:/test/testfile.avi");
			assertEquals("c:/test/testfile.avi", info.getLayoutContent());
			
			info.setLayoutContent("c:/test/testfile.mp3");
			assertEquals("c:/test/testfile.mp3", info.getLayoutContent());
			
			info.setLayoutContent("c:/test/testfile.html");
			assertEquals("c:/test/testfile.html", info.getLayoutContent());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			XmlConfigurationManager manager = new XmlConfigurationManager(layoutUnitTest);
			
			LayoutInfoPersistenceObject info = new LayoutInfoPersistenceObject();
			info.setLayoutType(LayoutType.Custom);
			info.setLayoutContent("c:/test/testfile.html");
			info.setLayoutId("Id1");
			info.prepareXml();
			manager.addConfigurationObject(info);
			manager.save();
			
			String fileContents = fileContents(layoutUnitTest);
			assertEquals(expectedLayout, fileContents);
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(layoutUnitTest);
			manager2.load();
			
			LayoutInfoPersistenceObject info2 = new LayoutInfoPersistenceObject();
			info2.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals(LayoutType.Custom, info2.getLayoutType());
			assertEquals("c:/test/testfile.html", info2.getLayoutContent());
			assertEquals("Id1", info2.getLayoutId());
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
