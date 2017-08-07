package textadventurelib.macro;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import playerlib.attributes.IAttribute;
import playerlib.characteristics.ICharacteristic;
import playerlib.equipment.IBodyPart;
import playerlib.items.IItem;
import playerlib.items.IProperty;
import playerlib.player.IPlayer;

//TODO: AUDIT
/**
 * 
 * @author Jeff Riggle
 *
 */
public class PlayerMacroManager implements IMacroManager{

	private final String ANY = "(.*)";
	private final String MULTILINE = "(?s)";
	private final String IGNORECASE = "(?i)";
	private final String PLAYER = "player";
	private final String CHARACTERISTIC = "characteristic";
	private final String INVENTORY = "inventory";
	private final String ATTRIBUTE = "attribute";
	private final String PROPERTY = "property";
	private final String BODYPART = "bodyPart";
	private final String EQUIPMENT = "equipment";
	
	private MacroParameters macroParameters;
	private List<IPlayer> players;
	private IPlayer currentPlayer;
	
	/**
	 * 
	 * @param parameters The @see MacroParameters to base the macro substitution off of.
	 */
	public PlayerMacroManager(MacroParameters parameters) {
		macroParameters = parameters;
		players = new ArrayList<IPlayer>();
	}
	
	/**
	 * 
	 * @param players The players to search for macro conversions.
	 */
	public void players(List<IPlayer> players) {
		this.players = players;
	}
	
	@Override
	public String substitute(String originalText) {
		String matchString = IGNORECASE + MULTILINE + ANY + macroParameters.prefix() + ANY + macroParameters.suffix() + ANY;
		
		if (!originalText.matches(matchString)) return originalText;
		
		List<String> subs = getSubstitutions(originalText);
		List<String> retList = new ArrayList<String>();

		for(String macro : subs) {
			if (!macro.matches(IGNORECASE + PLAYER + macroParameters.parameterPrefix() + ANY)) {
				continue;
			}

			assignPlayer(macro);
			
			// Get the un-escaped values for each parameter.
			int prefixLength = getUnescapedLength(macroParameters.parameterPrefix());
			int suffixLength = getUnescapedLength(macroParameters.parameterSuffix());
			int separatorLength = getUnescapedLength(macroParameters.separator());
			
			macro = macro.substring(PLAYER.length() + currentPlayer.name().length() + prefixLength + suffixLength + separatorLength);
			if (macro.matches("name" + ANY)) {
				retList.add(currentPlayer.name());
				continue;
			}
			
			retList.add(getSubstitution(macro));
		}

		String retVal = originalText;
		String reg = "(" + macroParameters.prefix() + ".*?)" + "(.+?)" + macroParameters.suffix();
		for (String str : retList) {
			retVal = retVal.replaceFirst(reg, str);
		}
		return retVal;
	}
	
	//TODO: Is there a better way to do this?
	private int getUnescapedLength(String text) {
		int retVal = 0;
		
		for (char character : text.toCharArray()) {
			if (character == '\\') continue;
			retVal++;
		}
		
		return retVal;
	}
	
	private void assignPlayer(String text) {
		String playerName = getParameter(text);
		
		for (IPlayer player : players) {
			if (player.name().equalsIgnoreCase(playerName)) {
				currentPlayer = player;
				break;
			}
		}
	}
	
	/**
	 * 
	 * @param text A @see String to get substitutions from.
	 * @return A List of @see String containing the Strings to substitute.
	 */
	private List<String> getSubstitutions(String text) {
		List<String> retVal = new ArrayList<String>();
		
		String[] matches = text.split(macroParameters.prefix());
		
		for(int i = 0; i < matches.length; i++) {
			String match = matches[i];
			String[] sub = match.split(macroParameters.suffix());
			retVal.add(sub[0]);
		}
		
		return retVal;
	}
	
	/**
	 * 
	 * @param text The text to get the substitution from.
	 * @return The substituted text.
	 */
	private String getSubstitution(String text) {
		if (text.matches(IGNORECASE + INVENTORY + ANY)) {
			return inventorySubstitution(text);
		}
		if (text.matches(IGNORECASE + EQUIPMENT + ANY)) {
			return equipmentSubstitution(text);
		}
		if (text.matches(IGNORECASE + ATTRIBUTE + ANY)) {
			return attributeSubstitution(text);
		}
		if (text.matches(IGNORECASE + BODYPART + ANY)) {
			return bodyPartSubstitution(text);
		}
		if (text.matches(IGNORECASE + CHARACTERISTIC + ANY)) {
			return characteristicSubstitution(text);
		}
		return new String();
	}
	
	/**
	 *
	 * @param text The text to replace with an inventory object value.
	 * @return The @see String representing the macro.
	 */
	private String inventorySubstitution(String text) {
		String temp = text.substring(INVENTORY.length());
		String parameter = getParameter(temp);
		for (IItem item : currentPlayer.inventory().items()) {
			if (!item.name().equalsIgnoreCase(parameter)) continue;
			
			String[] chain = temp.split(macroParameters.separator());
			if (chain[1].matches(IGNORECASE + PROPERTY + ANY)) {
				String param = getParameter(chain[1]);
				for (IProperty prop : item.properties()) {
					if (!prop.name().equalsIgnoreCase(param)) continue;
					
					String method = chain[2];
					return reflectForData(prop, method);
				}
				return new String();
			}
			
			return reflectForData(item, chain[1]);
		}
		
		return new String();
	}
	
	/**
	 *
	 * @param text The text to replace with an equipment object value.
	 * @return The @see String representing the macro.
	 */
	private String equipmentSubstitution(String text) {
		String temp = text.substring(EQUIPMENT.length());
		String parameter = getParameter(temp);
		
		for (IBodyPart bodyPart : currentPlayer.bodyParts()) {
			if (!bodyPart.name().equalsIgnoreCase(parameter)) continue;
			
			IItem item = currentPlayer.equipment().equipted(bodyPart);
			
			String[] chain = temp.split(macroParameters.separator());
			if (chain[1].matches(IGNORECASE + PROPERTY + ANY)) {
				String param = getParameter(chain[1]);
				for (IProperty prop : item.properties()) {
					if (!prop.name().equalsIgnoreCase(param)) continue;
					
					String method = chain[2];
					return reflectForData(prop, method);
				}
				return new String();
			}
			
			return reflectForData(item, chain[1]);
		}
		
		return new String();
	}

	/**
	 *
	 * @param text The text to replace with an attribute object value.
	 * @return The @see String representing the macro.
	 */
	private String attributeSubstitution(String text) {
		String temp = text.substring(ATTRIBUTE.length());
		String parameter = getParameter(temp);
		for (IAttribute attribute : currentPlayer.attributes()) {
			if (!attribute.name().equalsIgnoreCase(parameter)) continue;
			
			String[] chain = temp.split(macroParameters.separator());
			return reflectForData(attribute, chain[1]);
		}
		
		return new String();
	}
	
	/**
	 *
	 * @param text The text to replace with an bodyPart object value.
	 * @return The @see String representing the macro.
	 */
	private String bodyPartSubstitution(String text) {
		String temp = text.substring(BODYPART.length());
		String parameter = getParameter(temp);
		for (IBodyPart bodyPart : currentPlayer.bodyParts()) {
			if (!bodyPart.name().equalsIgnoreCase(parameter)) continue;
			
			String[] chain = temp.split(macroParameters.separator());
			if (chain[1].matches(IGNORECASE + CHARACTERISTIC + ANY)) {
				String param = getParameter(chain[1]);
				for (ICharacteristic character : bodyPart.getCharacteristics()) {
					if (!character.name().equalsIgnoreCase(param)) continue;
					
					String method = chain[2];
					return reflectForData(character, method);
				}
				return new String();
			}
			
			return reflectForData(bodyPart, chain[1]);
		}
		
		return new String();
	}

	/**
	 *
	 * @param text The text to replace with an characteristic object value.
	 * @return The @see String representing the macro.
	 */
	private String characteristicSubstitution(String text) {
		String temp = text.substring(CHARACTERISTIC.length());
		String parameter = getParameter(temp);
		for (ICharacteristic characteristic : currentPlayer.characteristics()) {
			if (!characteristic.name().equalsIgnoreCase(parameter)) continue;
			
			String[] chain = temp.split(macroParameters.separator());
			
			return reflectForData(characteristic, chain[1]);
		}
		
		return new String();
	}
		
	/**
	 * 
	 * @param text The @see String to get a parameter off of.
	 * @return The @see String represening the parameter.
	 */
	private String getParameter(String text) {
		String parameter = new String();
		
		String[] matches = text.split(macroParameters.parameterPrefix());
		
		if (matches.length > 1) {
			if (matches[1].matches(ANY + macroParameters.parameterSuffix() + ANY)) {
				String[] matches2 = matches[1].split(macroParameters.parameterSuffix());
				parameter = matches2[0];
			}
		}
		return parameter;
	}
	
	/**
	 * 
	 * @param dataObject The Object to reflect on to get the data.
	 * @param method The name of the method to call.
	 * @return A @see String representing the methods result.
	 */
	private String reflectForData(Object dataObject, String method) {
		String retVal = new String();
		try {
			Method func = dataObject.getClass().getMethod(method);
			if (func != null) {
				func.setAccessible(true);
				Object result = func.invoke(dataObject);
				
				if (result instanceof String) {
					return (String)result;
				}
				
				retVal = result.toString();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}
}
