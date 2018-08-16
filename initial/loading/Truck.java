package loading;

import java.util.ArrayList;
import requests.PickingRequest;

/**
 * Loads and stores items in this truck. This class Truck is responsible to store the orders
 * of a picking requests once they are ready to load. It is instantiated and used by the 
 * LoadingManager only.
 * 
 * @author AlexChum
 *
 */
public class Truck {

  /** The maximum number of items in this truck. */
  private int capacity = 20;
  
  /** The number of available spaces in this truck. */
  private int spaceRemaining;

  /** A string representation of the type of item in this truck. */
  private String item;
  
  /** The status of this truck. */
  private boolean full = false;
  
  /** The picking requests of this truck. */
  private ArrayList<PickingRequest> loadedRequests;
  

  /**
   * The Class Constructor. Instantiate a Truck object that gets loaded with item.
   * 
   * @param item the string representation of the item that this truck contains
   */
  public Truck(String item) {
    this.loadedRequests = new ArrayList<PickingRequest>(capacity);
    this.spaceRemaining = capacity;
    this.item = item;
  }

//  public int getCapacity() {
//    return capacity;
//  }

//  public int getSpaceRemaining() {
//    return spaceRemaining;
//  }
//
//  public ArrayList<PickingRequest> getLoadedRequests() {
//    return loadedRequests;
//  }

  /**
   * Loads a picking request into the truck and adjusts the remaining space.
   * 
   * @param request the picking request loaded into the truck
   */
  protected void loadRequest(PickingRequest request) {
    loadedRequests.add(request);
    if (--spaceRemaining == 0) {
      full = true;
    }
  }
  
//  /**
//   * Sets the capacity of a this truck. 
//   * 
//   * @param newCapacity  The integer representation of the new capacity.
//   */
//  public void setCapacity(int newCapacity) {
//    this.capacity = newCapacity;
//  }

//  /**
//   * Returns the available spaces remaining in this truck.
//   * 
//   * @return spaceRemaining  The integer representation of the spaces remaining.
//   */
//  protected int getSpace() {
//    return spaceRemaining;
//  }

  /**
   * Returns whether this truck has no spaces remaining.
   * 
   * @return full  The current status of this truck.
   */
  protected boolean isFull() {
    return full;
  }

  /**
   * Returns the string representation of the type of products in this truck.
   * 
   * @return item  The type of item stored in this truck.
   */
  protected String getItem() {
    return item;
  }
//  
//  /**
//   * @return an ArrayList of picking requests loaded onto this truck.
//   */
//  public ArrayList<PickingRequest> getRequests() {
//    return loadedRequests;
//  }
  
}
