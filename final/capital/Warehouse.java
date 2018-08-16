package capital;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a warehouse.
 * A warehouse has a name and can have subdivisions for storage.
 * If the warehouse contains subunits, then it cannot hold any fascia.
 * Otherwise, it may contain from zero to a maximum number of fascia, inclusive.
 * 
 * @author KeenaShang
 *
 */
public class Warehouse {
  //   Fields   
  /** The name of this warehouse unit. */
  private String name;
  
  /** The number of fascia stored in this unit. */
  private int fasciaAmt;

  /** Subunits contained within this warehouse. */
  private ArrayList<Warehouse> sub;
  
  /** The maximum number of fascia a unit can hold. */
  private final int maxCapacity = 30;
  
  //   Constructors   
  /**
   * Create a partially full warehouse with no subdivisions.
   * 
   * @param name       The name of the warehouse.
   * @param fasciaAmt  The number of fascia in this warehouse.
   */
  public Warehouse(String name, int fasciaAmt) {
    this.name = name;
    this.fasciaAmt = fasciaAmt;
    this.sub = new ArrayList<>();
  }
   
  /**
   * Create a warehouse with no subdivisions and fully stocked.
   * 
   * @param name  The name of this warehouse.
   */
  public Warehouse(String name) {
    this(name, 30);
  }
  
  /**
   * Create a warehouse with subdivisions.
   * 
   * @param name  The name of this warehouse.
   * @param subunits  The sub-warehouses in this warehouse.
   */
  public Warehouse(String name, ArrayList<Warehouse> subunits) {
    this.name = name;
    this.sub = subunits;
    this.fasciaAmt = 0;
  }
  
  /**
   * Create a warehouse with sub levels.
   * The the first sublevel (zone) is labeled A through Z while the other levels are 
   * labeled by index starting from 0 up to n - 1.
   * 
   * @precondition  The number of zones must be between 1 and 26, inclusive.
   * @param format  Each integer in this Array denotes the number 
   *                of subdivisions in a sublevel. The left to right
   *                order of integers denotes a top to bottom storage
   *                heirarchy within the warehouse.
   */
  public Warehouse(String name, Integer[] format) {
    this.name = name;
    this.fasciaAmt = 0;
    
    this.sub = createZones(format);
    renameZones(this.sub);      
  }
  
  //   Methods   
  
  /**
   * Create the sub zones in for a warehouse.
   * 
   * @param levels Contains the numbers for each level of the warehouse.
   */
  private ArrayList<Warehouse> createZones(Integer[] levels) {
    ArrayList<Warehouse> zones = new ArrayList<>();
    if (levels.length == 0) {
      return zones;
    } else if (levels.length == 1) {
      zones = createShelves(levels[0]);
    } else { 
      Integer[] newLevels = new Integer[levels.length - 1];
      for (int i = 1; i < levels.length; i++) {
        Integer num = new Integer(levels[i]);
        newLevels[i - 1] = num;
      }
      for (int i = 0; i < levels[0]; i++) {
        Warehouse wh = new Warehouse(String.valueOf(i), createZones(newLevels));
        zones.add(wh);
      }
    }
    return zones;
  }
  
  /**
   * Rename the zones by letter.  The zone index corresponds
   * to an alphabetical letter with 0 corresponding to A and so on.
   * 
   * @param zones The zones.
   */
  private void renameZones(ArrayList<Warehouse> zones) {
    for (Warehouse zone : zones) {
      int oldName = Integer.parseInt(zone.getName());
      oldName += 65;
      char newName = (char) oldName;
      zone.setName(String.valueOf(newName));
    }
  }
  
  /**
   * Return an array list of fully stocked warehouses.
   * Warehouses are named by index starting at zero.
   * 
   * @param num  The number of warehouses.
   */
  private ArrayList<Warehouse> createShelves(int num) {
    ArrayList<Warehouse> subdivisions = new ArrayList<>();
    for (int i = 0; i < num; i++) {
      subdivisions.add(new Warehouse(String.valueOf(i)));
    }
    return subdivisions;
  }
  
  /**
   * Set the initial warehouse states.
   * 
   * @param states  Each state is a location and fascia amount
   *                in the format Zone, Aisle,Rack,Shelf,Amount
   */
  public void setStates(ArrayList<String> states) {
    for (String state : states) {
      setInitialState(state);
    }
  }
  
  /**
   * Set the initial amount of fascia in the given location.
   * 
   * @param state   Must be in the format: Zone,Aisle,Rack,Level,Amount
   *                where Zone,Aisle,Rack,Level gives the location and
   *                Amount is the number of fascia.
   */
  private void setInitialState(String state) {
    String location = state.substring(0, state.lastIndexOf(","));
    Integer amt = Integer.parseInt(state.substring(state.lastIndexOf(",") + 1));
    Warehouse level = this.getStorageAt(location);
    level.setFasciaAmt(amt);
  }
  
  /**
   * Return the sub warehouse object at the given location.
   * 
   * @param location  The location of the storage unit.
   * @return  The warehouse object.
   */
  public Warehouse getStorageAt(String location) {
    Warehouse storage = null;
    if (location.contains(",")) {  // need to go down multiple levels
      String firstLocationId = location.substring(0, location.indexOf(","));
      for (Warehouse subUnit : this.sub) {
        if (subUnit.getName().equals(firstLocationId)) {
          String nextLocation = location.substring(location.indexOf(",") + 1);
          storage = subUnit.getStorageAt(nextLocation);
          return storage;
        } 
      }
    } else {  // the desired warehouse is in the immediate sub level
      for (Warehouse subUnit : this.sub) {
        if (subUnit.getName().equals(location)) {
          storage = subUnit;
        }
      }
    }
    return storage;
  }
  
  /**
   * Decrement the number of fascia in this Storage unit by one.
   */
  public void decrementFascia() {
    this.fasciaAmt -= 1;
  }
  
  /**
   * Return the non-full warehouse(s) and corresponding locations where a
   * location is the string consisting of the names of the nodes traversed
   * on the path from this warehouse to non-full level, separated by commas.
   * 
   * @return  A HashMap where the keys are locations of fascia
   *          and the values are the units that are not fully stocked
   *          at the time this method is called.
   */
  public HashMap<String, Warehouse> getNonFull() {
    HashMap<String, Warehouse> nonFull = new HashMap<>();
    if (sub.isEmpty() && (fasciaAmt != maxCapacity)) {
      nonFull.put(name, this);
    } else {
      for (Warehouse subWare: sub) {
        HashMap<String, Warehouse> tempMap = new HashMap<>();
        tempMap.putAll(subWare.getNonFull());
        // build up location strings
        for (Map.Entry<String, Warehouse> warehouse : tempMap.entrySet()) {
          nonFull.put(name + "," + warehouse.getKey(), warehouse.getValue());          
        }
      }
    }
    return nonFull;
  }

  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public int getFasciaAmt() {
    return fasciaAmt;
  }
  
  public void setFasciaAmt(int fasciaAmt) {
    this.fasciaAmt = fasciaAmt;
  }

  public ArrayList<Warehouse> getSub() {
    return sub;
  }

  public int getMaxCapacity() {
    return maxCapacity;
  }
  
}
