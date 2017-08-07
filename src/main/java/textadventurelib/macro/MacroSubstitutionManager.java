package textadventurelib.macro;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class MacroSubstitutionManager implements IMacroManager{

	private final String ANY = "(.*)";
	
	private String prefix;
	private String suffix;
	private String parameterPrefix;
	private String parameterSuffix;
	private String separator;
	
	private Object dataSource;
	private Map<String, Method> methodMap;
	
	/**
	 * 
	 * @param prefix The prefix.
	 * @param suffix The suffix.
	 * @param dataSource The data source to pull from.
	 */
	public MacroSubstitutionManager (String prefix, String suffix, Object dataSource) {
		this(prefix, suffix, null, null, "@", dataSource);
	}
	
	/**
	 * 
	 * @param prefix The prefix.
	 * @param suffix The suffix.
	 * @param parameterPrefix The parameter prefix.
	 * @param parameterSuffix The parameter suffix.
	 * @param separator The separator.
	 * @param dataSource The data source to pull from.
	 */
	public MacroSubstitutionManager (String prefix, String suffix,
			String parameterPrefix, String parameterSuffix, String separator, Object dataSource) {
		this.prefix = prefix;
		this.suffix = suffix;
		this.dataSource = dataSource;
		this.parameterPrefix = parameterPrefix;
		this.parameterSuffix = parameterSuffix;
		this.separator = separator;
		this.methodMap = generateMethodMap(this.dataSource);
	}
	
	private Map<String, Method> generateMethodMap(Object dataSource) {
		Map<String, Method> retMap = new HashMap<String, Method>();
		Method[] methods = dataSource.getClass().getMethods();
		
		for (int i = 0; i < methods.length; i++) {
			retMap.put(methods[i].getName(), methods[i]);
		}
		
		return retMap;
	}
	
	@Override
	public String substitute(String originalText) {
		String substitutedString = new String();
		//Get a list of string were the pattern was matched.
		String[] matches = originalText.split(prefix);
		
		for (int i = 0; i < matches.length; i++) {
			String match = matches[i];
			
			if (match.matches(ANY + suffix + ANY)) {
				String[] matches2 = match.split(suffix);
				match = reflectForData(matches2[0]);
				for (int j = 1; j < matches2.length; j++) {
					match += matches2[j];
				}
			}
			
			substitutedString += match;
		}
		
		return substitutedString;
	}
	
	private String reflectForData(String data) {
		if (data.matches(ANY + separator + ANY)) {
			return reflect(data.split(separator));
		}
		
		String[] chain = new String[1];
		chain[0] = data;
		return reflect(chain);
	}
	
	private String reflect(String[] dataChain) {
		String retVal = new String();
		
		Object dataObject = dataSource;
		
		for (int i = 0; i < dataChain.length; i++) {
			String data = dataChain[i];
			String parameter = getParameter(data);
			
			if (parameter.length() != 0) {
				data = data.substring(0, data.length() - parameter.length() - parameterPrefix.length() - parameterSuffix.length());
			}
			
			if (i != dataChain.length - 1) {
				dataObject = reflect(data, parameter, dataObject);
				methodMap = generateMethodMap(dataObject);
				continue;
			}
			
			retVal = (String)reflect(data, parameter, dataObject);
		}
		
		methodMap = generateMethodMap(dataSource);
		return retVal;
	}
	
	private Object reflect(String data, String parameter, Object dataObject) {
		Object retVal = null;
		try {
			Method func = methodMap.get(data);
		
			if (func != null) {
				func.setAccessible(true);
				if (parameter.length() == 0) {
					return (String)func.invoke(dataObject);
				}
				
				retVal = func.invoke(dataObject, parameter);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}
	
	private String getParameter(String data) {
		String retVal = new String();
		
		if (parameterPrefix == null || parameterSuffix == null) return retVal;
		
		String[] matches = data.split(parameterPrefix);
		
		if (matches.length > 1) {
			if (matches[1].matches(ANY + parameterSuffix + ANY)) {
				String[] matches2 = matches[1].split(parameterSuffix);
				retVal = matches2[0];
			}
		}
		
		return retVal;
	}
}
