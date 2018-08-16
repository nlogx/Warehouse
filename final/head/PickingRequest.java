package head;

import java.util.List;
import java.util.ArrayList;

import capital.Pallet;

/**
 * Represents a Request created from 4 orders that needs to be picked.
 * PickingRequest has an unique identification number and has info. for 4 orders.
 * 
 * @author group_0393
 *
 */

public class PickingRequest {
	/**
	 * Unique identification for the picking request
	 */
	private int id;
	/**
	 * A list of String arrays to keep track of the 4 input orders
	 */
	private List<String[]> orders;
	/**
	 * A list of Strings to keep track of the SKUs of fasica from the order
	 */
	private List<String> skus;
	/**
	 * A list of Strings that represent location of SKUs
	 */
	private List<String> locations;
	/**
	 * A list of Strings that represent the next location of SKUs
	 */
	private List<String> nextLocation;

	/**
	 * check whether the Picking Request is sequenced
	 */
	private boolean sequenced = false;
	/**
	 * check whether the Picking Request is loaded
	 */
	private boolean loaded = false;
	/**
	 * A Pallet array that stores the associated Pallets
	 * to be sequenced or to be loaded.
	 */
	private Pallet[] pallets;
	
	/**
	 * Constructs a new PickingRequest Object
	 * 
	 * @param x unique identification
	 */
	public PickingRequest(int x) {
		id = x;
		orders = new ArrayList<String[]>(4);
		skus = new ArrayList<String>(8);
		locations = new ArrayList<String>(8);
		nextLocation = new ArrayList<String>(8);
		pallets = new Pallet[2];
	}
	
	/**
	 * add an order to the picking Request
	 * <p>
	 * add an order and its associated SKUs
	 * for front and back fascia
	 * 
	 * @param order an order
	 * @param frontSku the SKU of front fascia of the order
	 * @param backSku the SKU of back fascia of the order
	 */
	public void addOrder(String[] order, String frontSku, String backSku) {
		orders.add(order);
		skus.add(frontSku);
		skus.add(backSku);
	}
	
	/**
	 * 
	 * @param locationList
	 */
	public void addLocations(List<String> locationList) {
		for (String str : locationList) {
			locations.add(str);
			nextLocation.add(str);
		}
	}

	/**
	 * Set condition for this request so that 
	 * it's passed sequencer inspection and been sequenced
	 */
	public void passSequencing() {
		sequenced = true;
	}
	
	/**
	 * Getter to see whether this request 
	 * has passed sequencer inspection and been sequenced
	 * 
	 * @return a boolean to see whether or not the request is seqeunced
	 */
	public boolean isSequenced() {
		return sequenced;
	}
	
	//TODO: don't know if it's needed (keep?)
	/**
	 * Set condition for this request so that
	 * it's passed loader's inspection
	 */
	public void passLoading() {
		loaded = true;
	}
	
	//TODO: don't know if it's needed (keep?)
	/**
	 * Getter to see whether this request has
	 * passed loader inspection and been loaded
	 * 
	 * @return a boolean to see whether or not the request is loaded
	 */
	public boolean isLoaded() {
		return loaded;
	}
	
	/**
	 * Set the sequenced pallets, front and back, of this request
	 * 
	 * @param front a pallet contains four front fascia
	 * @param back a pallet contains four back fascia
	 */
	public void setAssociatedPallets(Pallet front, Pallet back) {
		pallets[0] = front;
		pallets[1] = back;
	}
	
	/**
	 * Overload the setAssociatedPallets(Pallet, Pallet) to set
	 * the picked pallet to index 0 and null to index 1 of the array
	 * 
	 * @param picked a pallet contains all 8 fascia picked by picker
	 */
	public void setAssociatedPallets(Pallet picked) {
		pallets[0] = picked;
		pallets[1] = null;
	}
	
	/**
	 * Getter to get the array of Pallets that 
	 * is either picked or sequenced
	 * 
	 * @return a Pallet array that is either picked or sequenced
	 */
	public Pallet[] getPallets() {
		return pallets;
	}
	
	/**
	 * Getter to get the unique identifier for this picking request
	 * 
	 * @return an int that uniquely identifies the picking request.
	 */
	public int getId() {
		return id;
	}
	
	public List<String[]> getOrders() {
		return orders;
	}
	
	public List<String> getSkus() {
		return skus;
	}
	
	public String getLocation() {
		return nextLocation.remove(0);
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PickingRequest && id == ((PickingRequest) obj).getId()) {
			return true;
		}
		return false;
	}	
}