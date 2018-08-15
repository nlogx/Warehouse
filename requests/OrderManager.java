package requests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Map;
import loading.LoadingManager;
import requests.WarehousePicking;

/**
 * Manages orders and generates new picking requests. This class OrderManager is responsible to
 * create a new picking request when 4 orders are received. It holds a translation table that maps
 * models and colors to SKU numbers and keeps track of picking requests ready to be issued to
 * pickers.
 * 
 * @author AlexChum
 *
 */
public class OrderManager {

  private int pickingId = 1;
  private ArrayList<PickingRequest> requests;
  private PriorityQueue<PickingRequest> nextRequests;
  private TranslationTable table;
  private Map<String, Picker> pickers;
  private LoadingManager manager;

  /**
   * Class Constructor. Instantiate an OrderManager with the translation table passed as the
   * parameter.
   * 
   * @param data the data to get the SKUs number
   * @param manager a LoadingManager instance
   */
  public OrderManager(ArrayList<String> data, LoadingManager manager) {
    table = new TranslationTable(data);
    requests = new ArrayList<PickingRequest>();
    Comparator<PickingRequest> comparator = new PickingRequestComparator();
    nextRequests = new PriorityQueue<PickingRequest>(comparator);
    pickers = new HashMap<String, Picker>();
    this.manager = manager;
  }

  /**
   * Creates a picking request using the ArrayList of orders passed as the arguments.
   * 
   * @param orders a list containing the string representation of 4 orders
   * @return a string to be logged as the creation of a picking request
   */
  public String createRequests(ArrayList<String[]> orders) {
    Iterator<String[]> ordersIterator = orders.iterator();
    PickingRequest request = new PickingRequest(pickingId);
    while (ordersIterator.hasNext()) {
      String[] order = ordersIterator.next();
      String[] skus = table.getSkus(order[0], order[1]);
      request.addOrder(order, skus[0], skus[1]);
    }
    request.addLocations(WarehousePicking.optimize(request.getSkus()));
    requests.add(request);
    manager.addRequest(request);
    nextRequests.add(request);
    pickingId++;
    return "Picking request " + String.valueOf(pickingId - 1) + " has been issued.";
  }
  
  /**
   * Assigns a new request to a picker or gives the picker the next location to pick.
   * 
   * @param name the name of the picker
   * @return the strings to be logged in the console
   */
  public String[] processRequest(String name) {
    if (!pickers.containsKey(name)) {
      Picker picker = new Picker(name);
      pickers.put(name, picker);
      return picker.request(nextRequest());
    } else {
      Picker picker = pickers.get(name);
      if (picker.getStatus()) {
        return picker.request(nextRequest());
      } else {
        return picker.getFascia();
      }
    }
  }

  /**
   * Unloads the pallet of a given picker at the marshalling area.
   * 
   * @param name the name of the picker
   * @return the string to be logged in the console
   */
  public String marshalling(String name) {
    if (pickers.containsKey(name)) {
      Picker picker = pickers.get(name);
      String[] temp = picker.marshalling();
      if (temp[1] != null) {
        PickingRequest req = picker.getTask();
        req.addPallet(temp[1]);
        Sequencer.addPickedRequest(req);
      }
      return temp[0];
    } else {
      return "Picker " + name + " never handled a picking request.";
    }
  }

  /**
   * Returns the next picking request to a picker.
   * 
   * @return the object representation of a picking request
   */
  private PickingRequest nextRequest() {
    return nextRequests.poll();
  }
  
  public void reAddFailedPickedReq(PickingRequest pr) {
	  nextRequests.add(pr);
  }

}
