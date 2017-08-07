package textadventurelib.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LayoutRepository {

	private Map<String, LayoutPersistenceObject> layouts;
	
	/**
	 * Creates a new layout repository.
	 */
	public LayoutRepository() {
		layouts = new HashMap<String, LayoutPersistenceObject>();
	}
	
	/**
	 * 
	 * @param layout The layout to add to the repository.
	 */
	public void addLayout(LayoutPersistenceObject layout) {
		layouts.put(layout.id(), layout);
	}
	
	/**
	 * 
	 * @param layout The layout to remove from the repository.
	 */
	public void removeLayout(LayoutPersistenceObject layout) {
		layouts.remove(layout.id());
	}
	
	/**
	 * 
	 * @param layout The id of the layout to remove from this repository.
	 */
	public void removeLayout(String layout) {
		layouts.remove(layout);
	}
	
	/**
	 * Removes all layouts from this repository.
	 */
	public void clearLayouts() {
		layouts.clear();
	}
	
	/**
	 * 
	 * @return All layouts in this repository.
	 */
	public List<LayoutPersistenceObject> getLayouts() {
		return new ArrayList<LayoutPersistenceObject>(layouts.values());
	}
	
	/**
	 * 
	 * @param layoutid The id of the layout.
	 * @return The layout.
	 */
	public LayoutPersistenceObject getLayout(String layoutid) {
		if (!layouts.containsKey(layoutid)) {
			return null;
		}
		
		return layouts.get(layoutid);
	}
}
