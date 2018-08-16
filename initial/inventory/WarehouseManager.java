package inventory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Manages the warehouse inventory. This class keeps track of the amount of fascia
 * in the pick faces on the floor and replenishes the pick faces from the supply room.
 * The WarehouseMangager will keep track of a standard sized Warehouse.
 * 
 * @author KeenaShang
 *
 */
public class WarehouseManager {
 
  /** A warehouse. */
  private Warehouse warehouse;
  
  /** Workers ready to replenish low fascia stocks. */
  private ArrayList<String> replenishers;
  
  /** The minimum number of a fascia (on a shelf) before requiring resupply. */
  private final int minimum = 5;
  
  /**
   * Create a WarehouseManager.
   * @param states An ArrayList of Strings that denote the initial state
   *               of fascia in the warehouse in the format: Zone,Aisle,Rack,Level,Amount
   *               All other locations are assumed fully stocked.
   */
  public WarehouseManager(ArrayList<String> states) {
    warehouse = new Warehouse(states);
    replenishers = new ArrayList<String>();
  }
  
  /**
   * Create a WarehouseManager. Without an initial state.
   */
  public WarehouseManager() {
    warehouse = new Warehouse();
    replenishers = new ArrayList<String>();
  }
  
  /**
   * @return The warehouse.
   */
  protected Warehouse getWarehouse() {
    return warehouse;
  }
  
  /**
   * Add an available replenisher to the back of the list of ready replenishers.
   * @param name  The name of the replenisher
   */
  public void addReplenisher(String name) {
    replenishers.add(name);
  }

  /**
   * Request a resupply of fascia at the given location
   * if supply is low.
   * 
   * @param location  The location of the fascia in standard format.
   * @return  A request for restocking.
   */
  protected String requestRestock(String location) {
    return "Requesting fascia restock at " + location + System.lineSeparator();
  }
    
  /**
   * Fully restock fascia on the pick face from the supply room
   * if there are replenishers available.
   * 
   * @param location  The location of the fascia that need to be restocked.
   * @param name      The name of the replenisher to  execute restocking.
   * @return A String stating whether these fascia have been successfully restocked.
   */

  public String restock(String name, String location) { 
    if (replenishers.contains(name)) {
      Storage storage = warehouse.getStorageAt(location);
      int addAmt = storage.getMaxCapacity() - storage.getFasciaAmt();
      storage.setFasciaAmt(storage.getMaxCapacity());
      replenishers.remove(name);
      return name + " moved " + addAmt + " fascia from replenishing room to " 
      + location + System.lineSeparator() 
      + "Fascia at " + location + " have been restocked." + System.lineSeparator();
    } else {
      return  name + " unavailable to restock fascia at " 
    + location + System.lineSeparator();
    }
  }
  
  /**
   * Return a String indicating that one fascia has been
   * successfully removed from the pick face.
   * 
   * @param location  The location of the removed fascia.
   * @return  A String stating that the fascia amount has been decremented by one.
   */
  protected String pickOne(String location) {
    getWarehouse().getStorageAt(location).decrementFascia();
    return "1 fascia removed from " + location + System.lineSeparator();
  }
  
  /**
   * Pick a fascia. 
   * This method decreases the fascia amount on the pick face level
   * and checks if the supply is low, sending a restock request if needed.
   * 
   * @param location  The location of the fascia to be picked.
   * @return  A list of Strings where each string is an event that
   *          logs picking and resupplying (if occurs).
   */
  public String[] pick(String location) {
    String[] log = new String[3];
    log[0] = pickOne(location);
    if (isLow(location)) {
      log [1] = requestRestock(location);
    }
    return log;
  }
  
  /**
   * Return true iff the number of fascia at the given location is
   * less than or equal to the minimum allowed before requiring resupply.
   * 
   * @param location  The location of the fascia in standard format.
   * @return  Whether this type of fascia needs to be restocked.
   */
  protected boolean isLow(String location) {
    return getWarehouse().getStorageAt(location).getFasciaAmt() <= minimum;
  }
  
  
  /**
   * Create and write to the file denoting the stock state of the warehouse
   * at the point when the method is called.
   */
  public void writeFinalState() {
    FileWriter file;
    try {
      file = new FileWriter("final.csv");
      //iterate through storage units
      //keeping track of location
      String location = "";
      for (char zone = 'A'; zone <= 'B'; zone++) { // for each zone
        location += String.valueOf(zone);
        for (int aisle = 0; aisle < 2; aisle++) { // for each aisle
          location += "," + String.valueOf(aisle);
          for (int rack = 0; rack < 3; rack++) { // for each rack
            location += "," + String.valueOf(rack);
            for (int level = 0; level < 4; level++) { // for each level
              location += "," + String.valueOf(level);
              Storage storage = getWarehouse().getStorageAt(location);
              if (storage.getFasciaAmt() != storage.getMaxCapacity()) { // check if not full
                file.write(location + "," 
                     + String.valueOf(storage.getFasciaAmt()) + System.lineSeparator());
              }
              location = location.substring(0, location.lastIndexOf(",")); // remove last location
            }
            location = location.substring(0, location.lastIndexOf(","));
          }
          location = location.substring(0, location.lastIndexOf(","));
        }
        location = "";
      }
      file.close();
    } catch (IOException exception) {
      System.out.println("Error when writing final file");
      exception.printStackTrace();
    }
  }
}
