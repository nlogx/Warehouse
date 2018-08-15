package inventory;

import java.util.ArrayList;

/**
 * Represents a storage unit. A Storage unit has a name and 
 * each storage unit can be subdivided into smaller storage units
 * with unique names. If the storage unit contains
 * subunits, then it cannot hold any fascia. Otherwise, the storage unit
 * may contain up to a maximum number of fascia but not less than zero.
 * 
 * @author KeenaShang
 *
 */
class Storage {
  
  /** The name of this Storage unit. */
  private String name;
  
  /** The number of fascia stored in this Storage unit. */
  private int fasciaAmt;

  /** An ArrayList of Storage units contained within this instance. */
  private ArrayList<Storage> subStorage = new ArrayList<>();
  
  /** The maximum number of fascia a Storage unit can hold. */
  private final int maxCapacity = 30;
  
  
  /**
   * Create a new Storage object with no subunits containing
   * the maximum number of fascia allowed (30).
   * 
   * @param name    The name of this Storage unit.
   */
  public Storage(String name) {
    this(name, 30);
  }
  
  /**
   * Create a Storage object with no subunits that contains 
   * an amount of fascia less than or equal to the maximum capacity.
   * 
   * @param name       The name of this Storage unit.
   * @param fasciaAmt  The number of fasica contained in this unit.
   */
  public Storage(String name, int fasciaAmt) {
    this.name = name;
    this.fasciaAmt = fasciaAmt;
  }
  
  /**
   * Create a new Storage object with subunits.
   * 
   * @param name        The name of this Storage unit.
   * @param subStorage  An ArrayList of Storage subunits contained inside.
   */
  public Storage(String name, ArrayList<Storage> subStorage) {
    this(name, 0);
    this.subStorage = subStorage;
  }
  
  /**
   * Add additional subunits to a Storage unit that
   * already contains subunits.
   * 
   * @param additions  Storage units to add to the existing
   *                   ArrayList of Storage subunits.
   */
  protected void addStorage(ArrayList<Storage> additions) {
    for (Storage unit : additions) {
      this.subStorage.add(unit);
    }
  }
  
  /**
   * @return The number of fascia contained in this unit.
   */
  protected int getFasciaAmt() {
    return this.fasciaAmt;
  }
  
  /**
   * Set the amount of fascia in this Storage unit.
   * Precondition:The new amount cannot be greater than the maximum allowed
   *              or less than 0. Else method will print out a warning message
   *              and will not set the fascia amount.
   *              
   * @param newAmt  The new number of fascia in this unit.
   */
  protected void setFasciaAmt(int newAmt) {
    if (0 <= newAmt && newAmt <= maxCapacity) {
      this.fasciaAmt = newAmt;
    } else if (newAmt < 0) {
      System.out.println("Fascia amount not set: "
          + "attempting to set amount less than 0");
    } else { // newAmt > MAXCAPACITY
      System.out.println("Fascia amount not set: "
          + "exceeds maximum capacity allowed (" + maxCapacity + ").");
    }
  }
  
  /**
   * Decrement the number of fascia in this Storage unit by one.
   */
  protected void decrementFascia() {
    this.fasciaAmt -= 1;
  }
  
  /**
   * @return The name of this unit.
   */
  protected String getName() {
    return this.name;
  }
  
  /**
   * @return The maximum capacity of this unit.
   */
  protected int getMaxCapacity() {
    return this.maxCapacity;
  }
  
  /**
   * @return The ArrayList of subStorage units.
   */
  protected ArrayList<Storage> getSubStorage() {
    return this.subStorage;
  }
  
  
  /**
   * Return the Storage unit in subStorage with the specified name.
   * Precondition: The Storage unit must exist.
   * 
   * @param location  The name of the Storage object
   * @return  The storage object
   */
  protected Storage getStorageAt(String location) {
    Storage storage = null;
    if (location.contains(",")) {
      String firstLocationId = location.substring(0, location.indexOf(","));
      for (Storage sub : getSubStorage()) {
        if (sub.getName().equals(firstLocationId)) {
          String nextLocation = location.substring(location.indexOf(",") + 1);
          storage = sub.getStorageAt(nextLocation);
        } 
      }
    } else {
      for (Storage sub : getSubStorage()) {
        if (sub.getName().equals(location)) {
          storage = sub;
        }
      }
    }
    return storage;
  }
}
  
