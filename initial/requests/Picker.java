package requests;

import java.util.ArrayList;
import java.util.List;


/**
 * A picker who works on the floor of the warehouse. This class stores a picker's name, status,
 * current location and current task. Every picker also has a handheld device with a barcode reader
 * that directs them to the next location. Pickers ask for picking requests when ready, they pick
 * and place fascia onto pallets and take fascia to sequencing.
 * 
 * @author xiaolina
 *
 */
public class Picker {
  private String name;
  private boolean status = true;
  private String location;
  private ArrayList<String> forklift;
  private PickingRequest currTask;

  /**
   * Creates a Picker.
   * 
   * @param name The name of the picker.
   */
  public Picker(String name) {
    this.name = name;
    this.forklift = new ArrayList<>(8);
  }

  PickingRequest getTask() {
    return currTask;
  }

  boolean getStatus() {
    return status;
  }

  /**
   * Returns this picker's current location (Zone,Aisle,Rack,Level).
   * 
   * @return The current location of this picker.
   */
  String getLocation() {
    return location;
  }


  /**
   * Unloads the pallet of the picker at the marshalling.
   * 
   * @return the strings to be logged
   */
  public String[] marshalling() {
    String[] msg = new String[2];
    if (forklift.size() != 8) {
      msg[0] = "Picker " + name + " is not ready to go to marshalling.";
      return msg;
    } else {
      status = true;
      String skus = new String();
      for (String sku : forklift) {
        skus += String.valueOf(sku) + ",";
      }
      forklift.clear();
      msg[0] = "Picker " + name + " went to marshalling.";
      msg[1] = skus;
      return msg;
    }
  }


  /**
   * Sends a request to the system to process the next picking.
   * 
   * @param req The next picking request.
   */
  public String[] request(PickingRequest req) {
    String[] msg = new String[3];
    if (status) {
      currTask = req;
      status = false;
      location = req.getLocation();
      msg[0] = name + " is now assigned request: " + currTask.getId();
      msg[1] = name + " go to: " + location;
    } else {
      msg[0] = name + " is busy with request: " + currTask.getId();
      msg[1] = name + " previous destination was: " + location;
    }
    return msg;
  }

  /**
   * Retrieves the fascia at the picker's current location and assign a new picking location.
   * 
   * @return the Strings to be logged
   */
  public String[] getFascia() {
    String[] msg = new String[3];
    List<String> skus = currTask.getSkus();
    if (forklift.size() < 8) {
      String sku = skus.get(forklift.size());
      forklift.add(sku);
      msg[2] = this.location; // previous location
      msg[0] = name + " picked up fascia: " + String.valueOf(sku);
      if (forklift.size() != 8) { // if there are fascia left to pick
        location = currTask.getLocation();
        msg[1] = name + " go to: " + location;
      } else {
        msg[1] = name + " is ready to go to marshalling.";
      }
    } else {
      msg[0] = "Forklift already full.";
      msg[1] = name + " previous location was: ";
      msg[2] = location; // previous location
    }
    return msg;
  }
}
