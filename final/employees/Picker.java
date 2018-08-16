package employees;

import capital.Pallet;
import head.MainSystem;
import head.PickingRequest;

/**
 * A picker who works on the floor of the warehouse. This class stores a picker's name, status,
 * current location and current task. Every picker also has a handheld device with a barcode reader
 * that directs them to the next location. Pickers ask for picking requests when ready, they pick
 * and place fascia onto pallets and take fascia to sequencing.
 * 
 * @author xiaolina
 *
 */
public class Picker extends Worker {
  private String location;

  /**
   * Creates a Picker.
   * 
   * @param name The name of the picker.
   */
  public Picker(String name) {
    super(name);
  }
  
  /**
   * Returns this picker's current location (Zone,Aisle,Rack,Level).
   * 
   * @return The current location of this picker.
   */
  public String getLocation() {
    return location;
  }

  
  /**
   * Unloads the pallet of the picker at the marshalling.
   * 
   * @return the Picking Request gone to marshalling
   */
  public PickingRequest marshalling() {
    Pallet forklift = getTask().getPallets()[0];
    if (forklift != null && forklift.getSize() == 8) {
      PickingRequest request = getTask();
      setTask(null);
      return request;
    } else {
      MainSystem.logWarning("Picker " + getName() + " is not ready to go to marshalling.");
      return null;
    }
  }

  /**
   * Retrieves the fascia at the picker's current location and assign a new picking location.
   * 
   * @param sku the SKU of the fascia
   * @return the location where the fascia was picked up
   */
  public String getFascia(String sku) {
    Pallet forklift = getTask().getPallets()[0];
    if (forklift == null) {
      forklift = new Pallet(8);
      getTask().setAssociatedPallets(forklift);
    }
    if (forklift.getSize() < 8) {
      forklift.add(sku);
      MainSystem.log("Picker " + getName() + " picked up SKU : " + sku);
      String location = this.location;
      if (forklift.getSize() != 8) {
        this.location = getTask().getLocation();
        MainSystem.log("Barcode Reader: Go to " + this.location);
      } else {
        MainSystem.log("Barcode Reader: You have picked up all your fascias. Go to Marshalling."); 
      }
      return location;
    } else {
      MainSystem.logWarning("Forklift is full.");
      return null;
    }
  }

  /**
   * Assigns a picking request to this picker.
   * 
   * @param task the picking request assigned
   */
  public void setRequest(PickingRequest task) {
    setTask(task);
    location = task.getLocation();
    MainSystem.log("Picker " + getName() + " is now assigned picking request : " 
        + String.valueOf(task.getId()));
    MainSystem.log("Barcode Reader: Go to " + location);
  }
}
