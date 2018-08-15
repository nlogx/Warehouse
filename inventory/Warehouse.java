package inventory;

import java.util.ArrayList;

/**
 * Represents a warehouse.
 * A Warehouse contains two zones (A and B) that each contain
 * 2 aisles, each containing 3 racks, each containing 4 levels.
 * The names of each storage subcomponent is its index within the sublevel.
 * @author KeenaShang
 *
 */
class Warehouse extends Storage {
  
  /**
   * Create a default warehouse in which all fascia are fully stocked.
   */
  public Warehouse() {
    super("2 Zone Warehouse");
    this.setFasciaAmt(0);
    ArrayList<Storage> zones = new ArrayList<>();
    Storage zoneA = createZone("A");
    Storage zoneB = createZone("B");
    zones.add(zoneA);
    zones.add(zoneB);
    this.addStorage(zones);
  }
  
  /**
   * Create a Warehouse that begins with a non-full stock.
   * If the amount of fascia for a location is not specified, then
   * that location is fully stocked.
   * 
   * @param states  An ArrayList of Strings in the format: Zone,Aisle,Rack,Level,Amount
   *                where Zone is the capital letter denoting which zone (in the range ['A'..'B']),
   *                Aisle denotes which aisle in the zone (range [0..1]),
   *                Rack denotes which rack within the given aisle (range [0..2]),
   *                and Level denotes which level on the rack (range [0..3]).
   *                Amount is the number of fascia in the given level.
   */
  public Warehouse(ArrayList<String> states) {
    this();
    for (String state : states) {
      this.setInitialState(state);
    }
  }
  
  /**
   * Create a warehouse zone.
   * 
   * @param zoneName  The name of the zone
   * @return  A Storage object containing the correct number of substorage units
   *          for a default warehouse.
   */
  private Storage createZone(String zoneName) {
    ArrayList<Storage> subZones = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      ArrayList<Storage> subAisles = new ArrayList<>();
      for (int j = 0; j < 3; j++) {
        ArrayList<Storage> subRacks = new ArrayList<>();
        for (int k = 0; k < 4; k++) {
          Storage level = new Storage(String.valueOf(k));
          subRacks.add(level);
        }
        Storage rack = new Storage(String.valueOf(j), subRacks);
        subAisles.add(rack);
      }
      Storage aisle = new Storage(String.valueOf(i), subAisles);
      subZones.add(aisle);
    }
    Storage zone = new Storage(zoneName, subZones);
    return zone;
  }
    
  /**
   * Return the Storage unit at the given location.
   * 
   * @param location  The location of the storage unit.
   */
  @Override
  public Storage getStorageAt(String location) {
    Storage storage = null;
    if (location.contains(",")) {  // need to go down a level
      String firstLocationId = location.substring(0, location.indexOf(","));
      for (Storage sub : getSubStorage()) {
        if (sub.getName().equals(firstLocationId)) {
          String nextLocation = location.substring(location.indexOf(",") + 1);
          storage = sub.getStorageAt(nextLocation);
          return storage;
        } 
      }
    } else {  // the correct Storage unit is in warehouse subStorage
      for (Storage sub : this.getSubStorage()) {
        if (sub.getName().equals(location)) {
          storage = sub;
        }
      }
    }
    return storage;
  }
  
  /**
   * Set the initial amount of fascia in the given location.
   * 
   * @param state   Must be in the format: Zone,Aisle,Rack,Level,Amount
   *                where Zone,Aisle,Rack,Level gives the location and
   *                Amount is the number of fascia.
   */
  protected void setInitialState(String state) {
    String location = state.substring(0, state.lastIndexOf(","));
    Integer amt = Integer.parseInt(state.substring(state.lastIndexOf(",") + 1));
    Storage level = this.getStorageAt(location);
    level.setFasciaAmt(amt);
  }
}
