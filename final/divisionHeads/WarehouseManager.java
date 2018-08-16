package divisionHeads;

import capital.Warehouse;
import employees.Replenisher;
import head.MainSystem;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Manages the warehouse inventory. This class keeps track of the amount of fascia in the pick faces
 * on the floor and replenishes the pick faces from the supply room. The WarehouseMangager keeps
 * track of a Warehouse of given size.
 * 
 * @author KeenaShang
 *
 */
public class WarehouseManager {
  // Fields
  /** A default warehouse. */
  private Warehouse warehouse;

  /** Hired replenishing workers. */
  private HashMap<String, Replenisher> replenishers;

  /** The minimum number of a fascia (on a shelf) before requiring resupply. */
  private final int minimum = 5;

  // Constructor
  /**
   * Create a WarehouseManager
   * 
   * @param format The size format for the warehouse. Should consist of a single string with comma
   *        separated values. Each value is a number denoting the number of subdivisions in a
   *        sublevel. The left to right order corresponds to a top to bottom storage heirarchy
   *        within the warehouse. The first number should be between 1 and 26 inclusive.
   * @param states The initial stock state of fascia in the warehouse.
   */
  public WarehouseManager(ArrayList<String> format, ArrayList<String> states) {
    super();
    String[] splitFormat = format.get(0).split(",");
    Integer[] integerFormat = new Integer[splitFormat.length];
    for (int i = 0; i < splitFormat.length; i++) {
      integerFormat[i] = Integer.parseInt(splitFormat[i].trim());
    }
    warehouse = new Warehouse("Warehouse", integerFormat);
    warehouse.setStates(states);
    replenishers = new HashMap<String, Replenisher>();
  }

  // Methods
  public Warehouse getWarehouse() {
    return warehouse;
  }

  /**
   * Create a new replenisher employee and add to the hired map. Log a message.
   * 
   * @param name The unique identifier of the new replenisher employee.
   */

  public void hire(String name) {
    replenishers.put(name, new Replenisher(name));
    replenishers.get(name).setReady();
    MainSystem.log(name + " has been hired as a replenisher.");

  }

  /**
   * Pick a fascia and log the event. This method decreases the fascia amount on the pick face level
   * and checks if the supply is low, sending a restock request if needed.
   * 
   * @param location The location of the fascia to be picked.
   */
  public void pick(String location) {
    pickOne(location);
    if (isLow(location)) {
      requestRestock(location);
    }
  }

  /**
   * Log a message indicating that one fascia has been successfully removed from the pick face.
   * 
   * @param location The location of the removed fascia.
   * @return A String stating that the fascia amount has been decremented by one.
   */
  private void pickOne(String location) {
    warehouse.getStorageAt(location).decrementFascia();
    MainSystem.log("1 fascia removed from " + location);
  }

  /**
   * Return true iff the number of fascia at the given location is less than or equal to the minimum
   * allowed before requiring resupply.
   * 
   * @param location The location of the fascia in standard format.
   * @return Whether this type of fascia needs to be restocked.
   */
  protected boolean isLow(String location) {
    return warehouse.getStorageAt(location).getFasciaAmt() <= minimum;
  }

  /**
   * Log a resupply request of fascia at the given location if supply is low.
   * 
   * @param location The location of the fascia in standard format.
   * @return A request for restocking.
   */
  private void requestRestock(String location) {
    MainSystem.log("Requesting fascia restock at " + location);
  }

  /**
   * Fully restock fascia on the pick face from the supply room if the specified replenisher is
   * available and log the event.
   * 
   * @param location The location of the fascia that need to be restocked.
   * @param name The name of the replenisher to execute restocking.
   */
  public void restock(String name, String location) {
    if (!replenishers.containsKey(name)) {
      MainSystem.logWarning(name + " is not an employee. Fascia not restocked.");
    } else if (replenishers.get(name).isReady()) {
      replenishers.get(name).setBusy();
      Warehouse storage = warehouse.getStorageAt(location);
      int addAmt = storage.getMaxCapacity() - storage.getFasciaAmt();
      storage.setFasciaAmt(storage.getMaxCapacity());
      MainSystem.log(name + " moved " + addAmt + " fascia from replenishing room to " + location
          + System.lineSeparator() + "Fascia at " + location + " have been restocked.");
    } else {
      MainSystem.log(name + " unavailable to restock fascia at " + location);
    }
  }

  /**
   * Create and write to the file denoting the stock state of the warehouse at the point when the
   * method is called. Each line in the file is a location and fascia amount separated by commas.
   * 
   * @throws IOException Possible when creating or closing FileWriter
   */
  public void writeFinalState() throws IOException {
    FileWriter file;
    file = new FileWriter("final.csv");
    HashMap<String, Warehouse> nonFull = warehouse.getNonFull();
    for (Entry<String, Warehouse> entry : nonFull.entrySet()) {
      String location = entry.getKey();
      // get location without the main warehouse's name
      String subLocation = location.substring(location.indexOf(",") + 1);
      int fasciaAmt = entry.getValue().getFasciaAmt();
      String toWrite = subLocation + "," + fasciaAmt + System.lineSeparator();
      file.write(toWrite);
    }
    file.close();
  }
}
