package requests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A picking request object. This class PickingRequest is an object representation of a picking
 * request. It stores its unique ID; its orders;its SKU numbers; and the optimized path for the
 * pickers.
 * 
 * @author AlexChum
 *
 */
public class PickingRequest {

  private int id;
  private ArrayList<String[]> orders;
  private ArrayList<String> skus;
  private String pallet;
  private ArrayList<String> locations;
  private ArrayList<String> nextLocation;

  /**
   * Class Constructor. Instantiates a picking request with a the given parameter as its id.
   * 
   * @param id the unique id of the picking request
   */
  public PickingRequest(int id) {
    this.id = id;
    this.orders = new ArrayList<String[]>(4);
    this.skus = new ArrayList<String>(8);
    this.locations = new ArrayList<String>(8);
    this.nextLocation = new ArrayList<String>(8);
  }

  /**
   * @return returns the unique id of this picking request as an integer.
   */
  public int getId() {
    return id;
  }

  /**
   * @return returns the 4 orders that constitute this picking request.
   */
  public ArrayList<String[]> getOrders() {
    return orders;
  }

  // TODO
  /**
   * Adds the location of every SKU in this picking request in the order they should be picked up.
   * 
   * @param locationList a list of String where each string represents the location of a request
   */
  public void addLocations(List<String> locationList) {
    Iterator<String> iterator = locationList.iterator();
    while (iterator.hasNext()) {
      String next = iterator.next();
      locations.add(next);
      nextLocation.add(next);
    }
  }

  /**
   * Overrides the original equal method of object to compare two picking requests by their id.
   * 
   * @param other another instance of a PickingRequest object
   * @return true if and only if this PickingRequest is the same as other
   */
  public boolean equals(PickingRequest other) {
    if (this.id == other.getId()) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * @return returns the location of the next SKU to pick up.
   */
  String getLocation() {
    String location = nextLocation.remove(0);
    return location;
  }

  /**
   * @param pallet keeps track of the unsequenced pallet picked up by the picker.
   */
  void addPallet(String pallet) {
    this.pallet = pallet;
  }

  /**
   * @return return the unsequenced pallet of this request.
   */
  String getPallet() {
    return this.pallet;
  }

  /**
   * @return return the SKUs of every order in front,back,... ordering
   */
  ArrayList<String> getSkus() {
    return skus;
  }

  /**
   * Stores a new order in the picking request.
   * 
   * @param order the list of string representation of the order
   * @param skus2 the serial of the front SKU
   * @param skus3 the serial of the back SKU
   */
  public void addOrder(String[] order, String skus2, String skus3) {
    orders.add(order);
    skus.add(skus2);
    skus.add(skus3);
  }
}
