package head;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A Mock-up of the Warehouse Picking object. A table to store every location, 
 * and let other object to get the location of an desired SKU.
 * It should be replaced or override by the original WarehousePicking Object.
 * 
 * @author MingYuan
 *
 */
public class WarehousePicking {
	
	private static Map<String, String> travelTable = new HashMap<String, String>();
	
	/**
	 * A class constructor. It creates a map from SKUs to SKUs' location in the warehouse.
	 * 
	 * @param table the table that contains the SKUs and its location in the warehouse
	 */
	public WarehousePicking (ArrayList<String> table) {
		for (String line: table) {
			String temp = line.trim();
			travelTable.put(temp.substring(8),temp.substring(0,7));
		}
	}
	
	/**
	 * Based on the Integer SKUs in List 'skus', return a List of locations,
	 * where each location is a String containing 5 pieces of information: the
	 * zone character (in the range ['A'..'B']), the aisle number (an integer
	 * in the range [0..1]), the rack number (an integer in the range ([0..2]),
	 * and the level on the rack (an integer in the range [0..3]), and the SKU
	 * number.
	 * 
	 * It's a Mock-up of optimzie() method. it should be override or replaced by the original
	 * method.
	 * 
	 * @param arrayList the list of SKUs to retrieve.
	 * @return the List of locations.
	 */
	public static List<String> optimize(List<String> arrayList) {
		List<String> localList = new ArrayList<String>();
		for(int i = 0; i < arrayList.size(); i++) {
			localList.add(travelTable.get(arrayList.get(i)));
		}
		return localList;
	}
}
