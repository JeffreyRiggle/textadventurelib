package textadventurelib.macro;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class MacroParameters {
	private String prefix;
	private String suffix;
	private String parameterPrefix;
	private String parameterSuffix;
	private String separator;
	
	/**
	 * 
	 * @param prefix The macro prefix.
	 * @param suffix The macro suffix.
	 */
	public MacroParameters(String prefix, String suffix) {
		this(prefix, suffix, new String(), new String(), new String());
	}
	
	/**
	 * 
	 * @param prefix The macro prefix.
	 * @param suffix The macro suffix.
	 * @param parameterPrefix The macro parameter prefix.
	 * @param parameterSuffix The macro parameter suffix.
	 * @param separator The macro separator.
	 */
	public MacroParameters(String prefix, String suffix, String parameterPrefix,
			String parameterSuffix, String separator) {
		this.prefix = prefix;
		this.suffix = suffix;
		this.parameterPrefix = parameterPrefix;
		this.parameterSuffix = parameterSuffix;
		this.separator = separator;
	}
	
	/**
	 * 
	 * @return The macro prefix.
	 */
	public String prefix() {
		return prefix;
	}
	
	/**
	 * 
	 * @return The macro suffix.
	 */
	public String suffix() {
		return suffix;
	}
	
	/**
	 * 
	 * @return The macro parameter prefix.
	 */
	public String parameterPrefix() {
		return parameterPrefix;
	}
	
	/**
	 * 
	 * @return The macro parameter suffix.
	 */
	public String parameterSuffix() {
		return parameterSuffix;
	}
	
	/**
	 * 
	 * @return The macro separator.
	 */
	public String separator() {
		return separator;
	}
}
