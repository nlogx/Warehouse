package divisionHeads;

import employees.Picker;
import head.MainSystem;
import head.PickingRequest;
import head.PickingRequestComparator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * A PickingManager. This class handles events related to pickers: Assigning picking requests to
 * pickers, sending a picker to pick a product, and sending a picker to marshalling.
 * 
 * @author AlexChum
 *
 */
public class PickingManager {
  
  /**
   * A Map that help PickingManager to identify and keep track of all Picker Objects based on their
   * unique String identifiers.
   */
  private Map<String, Picker> pickers;

  /**
   * A List of available workers.
   */
  private List<Picker> available;

  /**
   * A PriorityQueues that stores the PickingRequest that needs to be processed.
   */
  private PriorityQueue<PickingRequest> unpickedRequests;

  /**
   * Instantiates a new picking manager.
   */
  public PickingManager() {
    Comparator<PickingRequest> comparator = new PickingRequestComparator();
    pickers = new HashMap<>();
    available = new ArrayList<>();
    unpickedRequests = new PriorityQueue<>(comparator);
  }

  /**
   * Registers pickers ready to receive a picking request.
   */
  public void hire(String name) {
    Picker picker = pickers.get(name);
    if (picker == null) {
      picker = new Picker(name);
      pickers.put(name, picker);
      MainSystem.log("Picker " + name + " has been registred in the system.");
    }
    if (picker.getTask() == null) {
      picker.setReady();
      MainSystem.log("Picker " + name + " is ready to receive a picking request.");
      giveTask(picker);
    } else {
      MainSystem.logWarning("Picker " + name + " is not ready.");
      MainSystem.logWarning(
          "Picker " + name + " is currently assigned picking request #" + picker.getTask().getId());
    }
  }

  /**
   * Hands a picking request, if any, to an available picker.
   * 
   * @param picker the available picker
   */
  private void giveTask(Picker picker) {
    String name = picker.getName();
    if (unpickedRequests.isEmpty()) {
      available.add(picker);
      MainSystem.log("Picker " + name + " is waiting to receive a picking request.");
    } else {
      picker.setRequest(unpickedRequests.poll());
      picker.setBusy();
    }
  }

  /**
   * Registers a new picking request.
   */
  public void addRequest(PickingRequest req) {
    if (!available.isEmpty()) {
      Picker picker = available.remove(0);
      picker.setRequest(req);
      picker.setBusy();
    } else {
      unpickedRequests.add(req);
    }
  }

  /**
   * Process a picking event from a picker and returns the location of the product if successful.
   *
   * @param name the name of the picker
   * @param sku the SKU of the product
   * @return location of the picked product, if any, null otherwise
   */
  public String processTask(String name, String sku) {
    if (sanityCheck(name)) {
      Picker picker = pickers.get(name);
      PickingRequest request = picker.getTask();
      String location = picker.getLocation();
      if (request.getSkus().contains(sku)) {
        location = picker.getFascia(sku);
        return location;
      } else {
        MainSystem.logWarning("Picking request " + String.valueOf(request.getId())
            + " does not contain SKU : " + sku);
        MainSystem.logWarning("Barcode Reader: WRONG SKU");
        MainSystem.logWarning(
            "Barcode Reader: Return the pallet to its original position and go to : " + location);
      }
    } else {
      MainSystem.logWarning("Barcode Reader: Return the pallet to its original position.");
    }
    return null;
  }

  /**
   * Sends a picker to marshalling and returns the picking request at marshalling if successful.
   *
   * @param name the name of the picker
   * @return the picking request the picker sent to marshalling, if any, null otherwise
   */
  public PickingRequest marshalling(String name) {
    if (sanityCheck(name)) {
      Picker picker = pickers.get(name);
      return picker.marshalling();
    }
    return null;
  }

  /**
   * Ensures the event and picker mentioned are in the system.
   *
   * @param name the name of the picker
   * @return true, if successful, false otherwise
   */
  private boolean sanityCheck(String name) {
    Picker picker = pickers.get(name);
    if (picker != null) {
      if (picker.getTask() != null) {
        return true;
      } else {
        MainSystem.logWarning("Picker " + " " + name + " has no assigned picking request.");
      }
    } else {
      MainSystem.logWarning("Picker " + " " + name + " has not been registred in the system.");
    }
    return false;
  }
}


