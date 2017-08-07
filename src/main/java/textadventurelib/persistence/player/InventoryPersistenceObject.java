package textadventurelib.persistence.player;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import ilusr.persistencelib.configuration.ConfigurationProperty;
import ilusr.persistencelib.configuration.PersistXml;
import ilusr.persistencelib.configuration.XmlConfigurationObject;
import playerlib.inventory.IInventory;
import playerlib.inventory.Inventory;
import playerlib.items.IItem;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class InventoryPersistenceObject extends XmlConfigurationObject{

	private static final String INVENTORY_NAME = "Inventory";
	private final String AMOUNT = "Amount";
	
	/**
	 * 
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public InventoryPersistenceObject() throws TransformerConfigurationException, ParserConfigurationException {
		super(INVENTORY_NAME);
	}
	
	/**
	 * 
	 * @param inventory The inventory to copy from.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public InventoryPersistenceObject(IInventory inventory) throws TransformerConfigurationException, ParserConfigurationException {
		super(INVENTORY_NAME);
		createFromInventory(inventory);
	}
	
	/**
	 * 
	 * @param item The item to add.
	 * @param amount The amount of the item.
	 */
	public void addItem(ItemPersistenceObject item, int amount) {
		item.addConfigurationProperty(new ConfigurationProperty(AMOUNT, Integer.toString(amount)));
		super.addChild(item);
	}
	
	/**
	 * 
	 * @param item The item to remove.
	 */
	public void removeItem(ItemPersistenceObject item) {
		super.removeChild(item);
	}
	
	/**
	 * Removes all items from inventory.
	 */
	public void clearItems() {
		super.clearChildren();
	}
	
	/**
	 * 
	 * @param item The item to change.
	 * @param amount The new amount for that item.
	 */
	public void changeItemAmount(ItemPersistenceObject item, int amount) {
		super.removeChild(item);
		item.clearConfigurationProperties();
		item.addConfigurationProperty(new ConfigurationProperty(AMOUNT, Integer.toString(amount)));
		super.addChild(item);
	}
	
	/**
	 * 
	 * @param item The item to find an amount for.
	 * @return The amount of that item.
	 */
	public int getAmount(ItemPersistenceObject item) {
		if (item.configurationProperties().size() == 0 || item.configurationProperties().get(0).value() == null) {
			return 0;
		}
		
		return Integer.parseInt(item.configurationProperties().get(0).value());
	}
	
	/**
	 * 
	 * @return The items in the inventory.
	 */
	public List<ItemPersistenceObject> items() {
		List<ItemPersistenceObject> retVal = new ArrayList<ItemPersistenceObject>();
		
		for (PersistXml item : super.children()) {
			retVal.add((ItemPersistenceObject)item);
		}
		
		return retVal;
	}
	
	/**
	 * Prepares this object to be persisted.
	 */
	public void prepareXml() {
		for (PersistXml item : super.children()) {
			ItemPersistenceObject obj = (ItemPersistenceObject)item;
			
			ConfigurationProperty temp = obj.configurationProperties().get(0);
			obj.prepareXml();
			obj.addConfigurationProperty(temp);
		}
	}
	
	/**
	 * 
	 * @return The converted inventory.
	 */
	public IInventory convertToInventory() {
		IInventory inv = new Inventory();
		
		for (ItemPersistenceObject item : items()) {
			IItem tempItem = item.convertToItem();
			int amount = Integer.parseInt(item.configurationProperties().get(0).value());
			inv.addItem(tempItem, amount);
		}
		
		return inv;
	}
	
	/**
	 * 
	 * @param inventory The inventory to copy from.
	 * @throws TransformerConfigurationException
	 * @throws ParserConfigurationException
	 */
	public void createFromInventory(IInventory inventory) throws TransformerConfigurationException, ParserConfigurationException {
		for (IItem item : inventory.items()) {
			addItem(new ItemPersistenceObject(item), inventory.getAmount(item));
		}
	}
	
	/**
	 * 
	 * @param obj The object to convert.
	 */
	public void convertFromPersistence(XmlConfigurationObject obj) {
		for (PersistXml child : obj.children()) {
			XmlConfigurationObject cChild = (XmlConfigurationObject)child;
			convert(cChild);
		}
	}
	
	private void convert(XmlConfigurationObject obj) {
		try {
			ItemPersistenceObject item = new ItemPersistenceObject();
			item.convertFromPersistence(obj);
			int amount = Integer.parseInt(obj.configurationProperties().get(0).value());
			addItem(item, amount);
		} catch (Exception e) {
			//TODO: what to do in this case.
		}
	}
}
