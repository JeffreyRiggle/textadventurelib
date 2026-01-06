import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import textadventurelib.macro.MacroSubstitutionManager;

public class MacroSubstitutionManangerUnitTest {

	private final String _match1Prefix = "<<";
	private final String _match1Suffix = ">>";
	private final String _value1 = "value1";
	private final String _value2 = "value2";
	private final DataSource _dataSource1 = new DataSource(_value1, _value2);
	private final String _substitution1 = "This is object 1 <<obj1>>. This is object 2 <<obj2>>";
	private final String _expectedResult = "This is object 1 value1. This is object 2 value2";
	
	private final String _match2Prefix = "\\{\\{";
	private final String _match2Suffix = "\\}\\}";
	private final String _value3 = "Someone";
	private final String _value4 = "Person";
	private final DataSource _dataSource2 = new DataSource(_value3, _value4);
	private final String _substitution2 = "This is a person {{obj1}}. That is a {{obj2}}";
	private final String _expectedResult2 = "This is a person Someone. That is a Person";
	
	private final String _match3Prefix = "\\{\\[";
	private final String _match3Suffix = "\\]\\}";
	private final String _value5 = "John";
	private final String _value6 = "25";
	private final DataSource _dataSource3 = new DataSource(_value5, _value6);
	private final String _substitution3 = "Player {[obj1]} has age {[obj2]}";
	private final String _expectedResult3 = "Player John has age 25";
	
	private final String _match4Prefix = "\\{\\[";
	private final String _match4Suffix = "\\]\\}";
	private final String _match4ParameterPrefix = "<<";
	private final String _match4ParameterSuffix = ">>";
	private final String _match4Separator = "@";
	private final String _value7 = "John";
	private final String _value8 = "25";
	private final String _index = "11";
	private final String _substitution4 = "Player {[obj1<<11>>]} has age {[obj2<<11>>]}";
	private final String _expectedResult4 = "Player John has age 25";
	
	private final String _nonSubstitutedString = "Player John has age 25";
	
	@Test
	public void testBasicMacroSub() {
		MacroSubstitutionManager manager = new MacroSubstitutionManager(_match1Prefix, _match1Suffix, _dataSource1);
		
		String result = manager.substitute(_substitution1);
		assertEquals(_expectedResult, result);
		
		MacroSubstitutionManager manager2 = new MacroSubstitutionManager(_match2Prefix, _match2Suffix, _dataSource2);
		
		String result2 = manager2.substitute(_substitution2);
		assertEquals(_expectedResult2, result2);
		
		MacroSubstitutionManager manager3 = new MacroSubstitutionManager(_match3Prefix, _match3Suffix, _dataSource3);
		
		String result3 = manager3.substitute(_substitution3);
		assertEquals(_expectedResult3, result3);
		
		String result4 = manager.substitute(_nonSubstitutedString);
		assertEquals(_nonSubstitutedString, result4);
	}

	@Test
	public void testAdvancedMacroSub() {
		Map<String, String> playerMap = new HashMap<String, String>();
		playerMap.put("12", "false");
		playerMap.put(_index, _value7);
		Map<String, String> ageMap = new HashMap<String, String>();
		ageMap.put("12", "bad");
		ageMap.put(_index, _value8);
		
		AdvancedDataSource dataSource = new AdvancedDataSource(playerMap, ageMap);
		MacroSubstitutionManager manager = new MacroSubstitutionManager(_match4Prefix, _match4Suffix, _match4ParameterPrefix, _match4ParameterSuffix, _match4Separator, dataSource);
		
		String result = manager.substitute(_substitution4);
		assertEquals(_expectedResult4, result);
		
		String result2 = manager.substitute(_nonSubstitutedString);
		assertEquals(_nonSubstitutedString, result2);
	}
	
	private class DataSource {
		private String _obj1;
		private String _obj2;
		
		public DataSource(String obj1, String obj2) {
			_obj1 = obj1;
			_obj2 = obj2;
		}
		
		@SuppressWarnings("unused")
		public String obj1() {
			return _obj1;
		}
		
		@SuppressWarnings("unused")
		public String obj2() {
			return _obj2;
		}
	}
	
	private class AdvancedDataSource {
		private Map<String, String> _obj1;
		private Map<String, String> _obj2;
		
		public AdvancedDataSource(Map<String, String> obj1, Map<String, String> obj2) {
			_obj1 = obj1;
			_obj2 = obj2;
		}
		
		@SuppressWarnings("unused")
		public String obj1(String location) {
			return _obj1.get(location);
		}
		
		@SuppressWarnings("unused")
		public String obj2(String location) {
			return _obj2.get(location);
		}
	}
}
