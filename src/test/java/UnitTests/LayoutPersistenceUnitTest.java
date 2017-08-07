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
import textadventurelib.persistence.LayoutGridPersistenceObject;
import textadventurelib.persistence.LayoutNodePersistenceObject;
import textadventurelib.persistence.LayoutPersistenceObject;
import textadventurelib.persistence.LayoutType;
import textadventurelib.persistence.StylePersistenceObject;
import textadventurelib.persistence.StylePropertyPersistenceObject;
import textadventurelib.persistence.StyleSelectorPersistenceObject;
import textadventurelib.persistence.StyleType;

public class LayoutPersistenceUnitTest {

	private XmlConfigurationManager _manager;
	private final String layoutUnitTest = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/layoutUnitTest.xml";
	private final String layoutUnitTest2 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/layoutUnitTest2.xml";
	private final String layoutUnitTest3 = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/layoutUnitTest3.xml";
	
	private final String expectedLayoutText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Layout><LayoutType ValueType=\"object\">ContentOnly</LayoutType><Content ValueType=\"string\">C:/Program Files (x86)/nilrem/something.avi</Content><LayoutGrid columns=\"1\" rows=\"1\"/><Style><Selectors/></Style><ID ValueType=\"string\">TestLayout</ID></Layout>";
	private final String expectedLayoutText2 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Layout><LayoutType ValueType=\"object\">Custom</LayoutType><Content ValueType=\"string\">C:/Program Files (x86)/nilrem/something.avi</Content><LayoutGrid columns=\"1\" rows=\"2\"><LayoutNodes><LayoutNode column=\"0\" row=\"0\"><LayoutValue ValueType=\"string\">MultiTypeContentView</LayoutValue><NodeID ValueType=\"string\">a395ebec122b4d089e0307bc98e3bd13</NodeID></LayoutNode><LayoutNode column=\"0\" row=\"1\"><LayoutValue ValueType=\"string\">ConsoleView</LayoutValue><NodeID ValueType=\"string\">9569eeb9acf2469aa685cecf3359b771</NodeID></LayoutNode></LayoutNodes></LayoutGrid><Style><Selectors><StyleSelector selector=\"#95e3e6aca47b4c978f06f55ae7cedcb5 *\"><StyleProperties><StyleProperty styleType=\"FontFamily\" styleValue=\"MV Boli\"/><StyleProperty styleType=\"FontSize\" styleValue=\"14\"/><StyleProperty styleType=\"BackgroundSize\" styleValue=\"stretch\"/><StyleProperty styleType=\"Background\" styleValue=\"https://i.ytimg.com/vi/iYyDbVUWgTI/hqdefault.jpg\"/><StyleProperty styleType=\"FontStyle\" styleValue=\"normal\"/><StyleProperty styleType=\"Color\" styleValue=\"#1a80ff\"/><StyleProperty styleType=\"BackgroundRepeat\" styleValue=\"no-repeat\"/></StyleProperties></StyleSelector></Selectors></Style><ID ValueType=\"string\">TestLayout</ID></Layout>";
	
	private final String contentLocation1 = "C:/Program Files (x86)/nilrem/something.avi";
	private final String contentLocation2 = "C:/Program Files (x86)/nilrem/something.fla";
	private final String contentLocation3 = "C:/Program Files (x86)/nilrem/something.png";
	private final String layoutString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<?import java.net.*?>\n<?import javafx.geometry.*?>\n<?import javafx.scene.control.*?>\n<?import javafx.scene.layout.*?>\n<?import javafx.scene.text.*?>\n<?import org.tbd.TextAdventureCreator.views.layout.*?>\n\n<?import textadventureLib.layout.fxviews.*?>\n\n<fx:root type=\"javafx.scene.layout.AnchorPane\" xmlns:fx=\"http://javafx.com/fxml\">\n\t<GridPane AnchorPane.topAnchor=\"0.0\"\n\t\tAnchorPane.leftAnchor=\"0.0\"\n\t\tAnchorPane.rightAnchor=\"0.0\"\n\t\tAnchorPane.bottomAnchor=\"0.0\">\n\t\t<columnConstraints>\n\t\t\t<ColumnConstraints hgrow=\"NEVER\" percentWidth=\"100\"/>\n\t\t</columnConstraints>\n\t\t<rowConstraints>\n\t\t\t<RowConstraints vgrow=\"NEVER\" percentHeight=\"50\"/>\n\t\t\t<RowConstraints vgrow=\"NEVER\" percentHeight=\"50\"/>\n\t\t</rowConstraints>\n\t\t<MultiTypeContentView fx:id=\"a395ebec122b4d089e0307bc98e3bd13\" GridPane.rowIndex=\"0\" GridPane.columnIndex=\"0\" />\n\t\t<ConsoleView fx:id=\"9569eeb9acf2469aa685cecf3359b771\" GridPane.rowIndex=\"1\" GridPane.columnIndex=\"0\" />\n\t</GridPane>\n</fx:root>";
	private final String styleString = "#95e3e6aca47b4c978f06f55ae7cedcb5 * {\n\t-fx-font-family : \"MV Boli\";\n\t-fx-font-size : 14;\n\t-fx-background-size : stretch;\n\t-fx-background-image : url(https://i.ytimg.com/vi/iYyDbVUWgTI/hqdefault.jpg);\n\t-fx-font-style : normal;\n\t-fx-text-fill : #1a80ff;\n\t-fx-background-repeat : no-repeat;\n}\n";
	
	@Test
	public void testCreate() {
		try {
			LayoutPersistenceObject persist = new LayoutPersistenceObject();
			
			assertNotNull(persist);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testSetLayout() {
		try {
			LayoutPersistenceObject persist = new LayoutPersistenceObject();
			
			persist.layoutType(LayoutType.ContentOnly);
			assertEquals(LayoutType.ContentOnly, persist.layoutType());
			
			persist.layoutType(LayoutType.TextWithButtonInput);
			assertEquals(LayoutType.TextWithButtonInput, persist.layoutType());
			
			persist.layoutType(LayoutType.TextAndContentWithTextInput);
			assertEquals(LayoutType.TextAndContentWithTextInput, persist.layoutType());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSetContent() {
		try {
			LayoutPersistenceObject persist = new LayoutPersistenceObject();
			
			persist.content(contentLocation1);
			assertEquals(contentLocation1, persist.content());
			
			persist.content(contentLocation2);
			assertEquals(contentLocation2, persist.content());
			
			persist.content(contentLocation3);
			assertEquals(contentLocation3, persist.content());
			
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testID() {
		try {
			LayoutPersistenceObject persist = new LayoutPersistenceObject();
			
			persist.id("TestLayout");
			assertEquals("TestLayout", persist.id());
			
			persist.id("TestLayout2");
			assertEquals("TestLayout2", persist.id());
			
			persist.id("TestLayout3");
			assertEquals("TestLayout3", persist.id());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testLayout() {
		try {
			LayoutPersistenceObject persist = new LayoutPersistenceObject();
			LayoutGridPersistenceObject layout = new LayoutGridPersistenceObject();
			layout.setColumns(1);
			layout.setRows(2);
			
			LayoutNodePersistenceObject node1 = new LayoutNodePersistenceObject();
			node1.setId("a395ebec122b4d089e0307bc98e3bd13");
			node1.setRow(0);
			node1.setColumn(0);
			node1.setLayoutValue("MultiTypeContentView");
			
			LayoutNodePersistenceObject node2 = new LayoutNodePersistenceObject();
			node2.setId("9569eeb9acf2469aa685cecf3359b771");
			node2.setRow(1);
			node2.setColumn(0);
			node2.setLayoutValue("ConsoleView");
			
			layout.addNode(node1);
			layout.addNode(node2);
			
			persist.setLayout(layout);
			assertEquals(layoutString, persist.getLayout().compile());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testStyle() {
		try {
			LayoutPersistenceObject persist = new LayoutPersistenceObject();
			StylePersistenceObject style = new StylePersistenceObject();
			StyleSelectorPersistenceObject selector = new StyleSelectorPersistenceObject();
			selector.setSelector("#95e3e6aca47b4c978f06f55ae7cedcb5 *");
			
			StylePropertyPersistenceObject font = new StylePropertyPersistenceObject();
			font.setPropertyType(StyleType.FontFamily);
			font.setPropertyValue("MV Boli");
			
			StylePropertyPersistenceObject fontSize = new StylePropertyPersistenceObject();
			fontSize.setPropertyType(StyleType.FontSize);
			fontSize.setPropertyValue("14");
			
			StylePropertyPersistenceObject backgroundSize = new StylePropertyPersistenceObject();
			backgroundSize.setPropertyType(StyleType.BackgroundSize);
			backgroundSize.setPropertyValue("stretch");
			
			StylePropertyPersistenceObject backgroundImage = new StylePropertyPersistenceObject();
			backgroundImage.setPropertyType(StyleType.Background);
			backgroundImage.setPropertyValue("https://i.ytimg.com/vi/iYyDbVUWgTI/hqdefault.jpg");
			
			StylePropertyPersistenceObject fontStyle = new StylePropertyPersistenceObject();
			fontStyle.setPropertyType(StyleType.FontStyle);
			fontStyle.setPropertyValue("normal");
			
			StylePropertyPersistenceObject color = new StylePropertyPersistenceObject();
			color.setPropertyType(StyleType.Color);
			color.setPropertyValue("#1a80ff");
			
			StylePropertyPersistenceObject repeat = new StylePropertyPersistenceObject();
			repeat.setPropertyType(StyleType.BackgroundRepeat);
			repeat.setPropertyValue("no-repeat");
			
			selector.addStyleProperty(font);
			selector.addStyleProperty(fontSize);
			selector.addStyleProperty(backgroundSize);
			selector.addStyleProperty(backgroundImage);
			selector.addStyleProperty(fontStyle);
			selector.addStyleProperty(color);
			selector.addStyleProperty(repeat);
			style.addSelector(selector);
			
			persist.setStyle(style);
			assertEquals(styleString, persist.getStyle().compile());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testHasContentForStaticType() {
		try {
			LayoutPersistenceObject persist = new LayoutPersistenceObject();
			
			persist.layoutType(LayoutType.ContentOnly);
			assertTrue(persist.hasContentArea());
			
			persist.layoutType(LayoutType.Custom);
			assertFalse(persist.hasContentArea());
			
			persist.layoutType(LayoutType.TextAndContentWithButtonInput);
			assertTrue(persist.hasContentArea());
			
			persist.layoutType(LayoutType.TextAndContentWithTextInput);
			assertTrue(persist.hasContentArea());
			
			persist.layoutType(LayoutType.TextWithButtonInput);
			assertFalse(persist.hasContentArea());
			
			persist.layoutType(LayoutType.TextWithTextInput);
			assertFalse(persist.hasContentArea());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testHasContentForCustomType() {
		try {
			LayoutPersistenceObject persist = new LayoutPersistenceObject();
			persist.layoutType(LayoutType.Custom);
			LayoutGridPersistenceObject layout = new LayoutGridPersistenceObject();
			layout.setColumns(1);
			layout.setRows(1);
			
			LayoutNodePersistenceObject node1 = new LayoutNodePersistenceObject();
			node1.setId("a395ebec122b4d089e0307bc98e3bd13");
			node1.setRow(0);
			node1.setColumn(0);
			node1.setLayoutValue("MultiTypeContentView");
			
			layout.addNode(node1);
			persist.setLayout(layout);
			assertTrue(persist.hasContentArea());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testSave() {
		try {
			_manager = new XmlConfigurationManager(layoutUnitTest);
			LayoutPersistenceObject persist = new LayoutPersistenceObject();
			
			persist.layoutType(LayoutType.ContentOnly);
			persist.id("TestLayout");
			persist.content(contentLocation1);
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			String fileContents = fileContents(layoutUnitTest);
			assertEquals(expectedLayoutText, fileContents);
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void testSave2() {
		try {
			_manager = new XmlConfigurationManager(layoutUnitTest3);
			LayoutPersistenceObject persist = new LayoutPersistenceObject();
			
			persist.layoutType(LayoutType.Custom);
			persist.content(contentLocation1);
			LayoutGridPersistenceObject layout = new LayoutGridPersistenceObject();
			layout.setColumns(1);
			layout.setRows(2);
			
			LayoutNodePersistenceObject node1 = new LayoutNodePersistenceObject();
			node1.setId("a395ebec122b4d089e0307bc98e3bd13");
			node1.setRow(0);
			node1.setColumn(0);
			node1.setLayoutValue("MultiTypeContentView");
			
			LayoutNodePersistenceObject node2 = new LayoutNodePersistenceObject();
			node2.setId("9569eeb9acf2469aa685cecf3359b771");
			node2.setRow(1);
			node2.setColumn(0);
			node2.setLayoutValue("ConsoleView");
			
			layout.addNode(node1);
			layout.addNode(node2);
			
			persist.setLayout(layout);
			StylePersistenceObject style = new StylePersistenceObject();
			StyleSelectorPersistenceObject selector = new StyleSelectorPersistenceObject();
			selector.setSelector("#95e3e6aca47b4c978f06f55ae7cedcb5 *");
			
			StylePropertyPersistenceObject font = new StylePropertyPersistenceObject();
			font.setPropertyType(StyleType.FontFamily);
			font.setPropertyValue("MV Boli");
			
			StylePropertyPersistenceObject fontSize = new StylePropertyPersistenceObject();
			fontSize.setPropertyType(StyleType.FontSize);
			fontSize.setPropertyValue("14");
			
			StylePropertyPersistenceObject backgroundSize = new StylePropertyPersistenceObject();
			backgroundSize.setPropertyType(StyleType.BackgroundSize);
			backgroundSize.setPropertyValue("stretch");
			
			StylePropertyPersistenceObject backgroundImage = new StylePropertyPersistenceObject();
			backgroundImage.setPropertyType(StyleType.Background);
			backgroundImage.setPropertyValue("https://i.ytimg.com/vi/iYyDbVUWgTI/hqdefault.jpg");
			
			StylePropertyPersistenceObject fontStyle = new StylePropertyPersistenceObject();
			fontStyle.setPropertyType(StyleType.FontStyle);
			fontStyle.setPropertyValue("normal");
			
			StylePropertyPersistenceObject color = new StylePropertyPersistenceObject();
			color.setPropertyType(StyleType.Color);
			color.setPropertyValue("#1a80ff");
			
			StylePropertyPersistenceObject repeat = new StylePropertyPersistenceObject();
			repeat.setPropertyType(StyleType.BackgroundRepeat);
			repeat.setPropertyValue("no-repeat");
			
			selector.addStyleProperty(font);
			selector.addStyleProperty(fontSize);
			selector.addStyleProperty(backgroundSize);
			selector.addStyleProperty(backgroundImage);
			selector.addStyleProperty(fontStyle);
			selector.addStyleProperty(color);
			selector.addStyleProperty(repeat);
			style.addSelector(selector);
			persist.setStyle(style);
			persist.id("TestLayout");
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			String fileContents = fileContents(layoutUnitTest3);
			assertEquals(expectedLayoutText2, fileContents);
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void convertFromPersistence() {
		try {
			_manager = new XmlConfigurationManager(layoutUnitTest2);
			LayoutPersistenceObject persist = new LayoutPersistenceObject();
			
			persist.layoutType(LayoutType.ContentOnly);
			persist.content(contentLocation1);
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(layoutUnitTest2);
			manager2.load();
			
			LayoutPersistenceObject persist2 = new LayoutPersistenceObject();
			persist2.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals(LayoutType.ContentOnly, persist2.layoutType());
			assertEquals(contentLocation1, persist2.content());
		} catch (Exception e) {
			fail(e.getStackTrace().toString());
		}
	}
	
	@Test
	public void convertFromPersistence2() {
		try {
			_manager = new XmlConfigurationManager(layoutUnitTest3);
			LayoutPersistenceObject persist = new LayoutPersistenceObject();
			
			persist.layoutType(LayoutType.Custom);
			persist.content(contentLocation1);
			LayoutGridPersistenceObject layout = new LayoutGridPersistenceObject();
			layout.setColumns(1);
			layout.setRows(2);
			
			LayoutNodePersistenceObject node1 = new LayoutNodePersistenceObject();
			node1.setId("a395ebec122b4d089e0307bc98e3bd13");
			node1.setRow(0);
			node1.setColumn(0);
			node1.setLayoutValue("MultiTypeContentView");
			
			LayoutNodePersistenceObject node2 = new LayoutNodePersistenceObject();
			node2.setId("9569eeb9acf2469aa685cecf3359b771");
			node2.setRow(1);
			node2.setColumn(0);
			node2.setLayoutValue("ConsoleView");
			
			layout.addNode(node1);
			layout.addNode(node2);
			
			persist.setLayout(layout);
			StylePersistenceObject style = new StylePersistenceObject();
			StyleSelectorPersistenceObject selector = new StyleSelectorPersistenceObject();
			selector.setSelector("#95e3e6aca47b4c978f06f55ae7cedcb5 *");
			
			StylePropertyPersistenceObject font = new StylePropertyPersistenceObject();
			font.setPropertyType(StyleType.FontFamily);
			font.setPropertyValue("MV Boli");
			
			StylePropertyPersistenceObject fontSize = new StylePropertyPersistenceObject();
			fontSize.setPropertyType(StyleType.FontSize);
			fontSize.setPropertyValue("14");
			
			StylePropertyPersistenceObject backgroundSize = new StylePropertyPersistenceObject();
			backgroundSize.setPropertyType(StyleType.BackgroundSize);
			backgroundSize.setPropertyValue("stretch");
			
			StylePropertyPersistenceObject backgroundImage = new StylePropertyPersistenceObject();
			backgroundImage.setPropertyType(StyleType.Background);
			backgroundImage.setPropertyValue("https://i.ytimg.com/vi/iYyDbVUWgTI/hqdefault.jpg");
			
			StylePropertyPersistenceObject fontStyle = new StylePropertyPersistenceObject();
			fontStyle.setPropertyType(StyleType.FontStyle);
			fontStyle.setPropertyValue("normal");
			
			StylePropertyPersistenceObject color = new StylePropertyPersistenceObject();
			color.setPropertyType(StyleType.Color);
			color.setPropertyValue("#1a80ff");
			
			StylePropertyPersistenceObject repeat = new StylePropertyPersistenceObject();
			repeat.setPropertyType(StyleType.BackgroundRepeat);
			repeat.setPropertyValue("no-repeat");
			
			selector.addStyleProperty(font);
			selector.addStyleProperty(fontSize);
			selector.addStyleProperty(backgroundSize);
			selector.addStyleProperty(backgroundImage);
			selector.addStyleProperty(fontStyle);
			selector.addStyleProperty(color);
			selector.addStyleProperty(repeat);
			style.addSelector(selector);
			persist.setStyle(style);
			persist.prepareXml();
			_manager.addConfigurationObject(persist);
			_manager.save();
			
			XmlConfigurationManager manager2 = new XmlConfigurationManager(layoutUnitTest3);
			manager2.load();
			
			LayoutPersistenceObject persist2 = new LayoutPersistenceObject();
			persist2.convertFromPersistence((XmlConfigurationObject)manager2.configurationObjects().get(0));
			
			assertEquals(LayoutType.Custom, persist2.layoutType());
			assertEquals(contentLocation1, persist2.content());
			assertEquals(layoutString, persist2.getLayout().compile());
			assertEquals(styleString, persist2.getStyle().compile());
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
