package divisionHeads;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;

import head.PickingRequest;
import head.PickingRequestComparator;
import head.MainSystem;
import capital.Truck;
import employees.Loader;
//
//
/**
 * Manages any loading request. This class LoadingManager is responsible of loading every picking
 * requests on the trucks. It will only load a given picking request if it was the issued
 * immediately after the last one loaded. It keeps track of incompleteTrucks (one or many, depending
 * on the number of products) and filledTrucks. It also keeps track of every loaded picking
 * requests.
 * 
 * @author group_0393
 *
 */
public class LoadingManager {
  // Fields
  /** The ID of the next request to be loaded. */
  private int id;
  /** Non-full trucks. */
  private ArrayList<Truck> incompleteTrucks;
  /** Filled trucks. */
  private ArrayList<Truck> filledTrucks; 
  /** Employees who work as loaders. */
  private Map<String, Loader> loaders;
  /** Pallets ready to be loaded. The head of the queue is the pair of pallets with picking request with lowest id.
  /*  Pallets are assigned to a picking request. */
  private PriorityQueue<PickingRequest> loadingArea;
  /** Picking Requests that have been loaded onto trucks. */
	private PriorityQueue<PickingRequest> loadedReqs;

  // Constructor
  /**
   * The class constructor. This constructor instantiates 3 ArrayList and 2 PriorityQueues.
   */
  public LoadingManager() {
    // Trucks
    incompleteTrucks = new ArrayList<Truck>();
    incompleteTrucks.add(new Truck());
    filledTrucks = new ArrayList<Truck>();
    id = 1;
    
    Comparator<PickingRequest> comparator = new PickingRequestComparator();
	this.loaders = new HashMap<>();
	this.loadingArea = new PriorityQueue<>(comparator);
	this.loadedReqs = new PriorityQueue<>(comparator);
  }
  // Methods
  /**
   * Creates a new empty truck object and adds it to the incomplete trucks ArrayList.
   */
  public void addTruck() {
    Truck truck = new Truck();
    incompleteTrucks.add(truck);
  }

  public void addSequencedRequest(PickingRequest req) {
    loadingArea.add(req);
  }

  public PriorityQueue<PickingRequest> getSequencedRequests() {
    return loadingArea;
  }

  public PriorityQueue<PickingRequest> getLoadedReqs() {
    return loadedReqs;
  }
  /**
   * Receives a loading requests from the system and process the given picking request accordingly.
   * 
   * <p>If the request is the next picking request issued after the last one loaded. 
   * If it is the first loading request, it must be the first picking request issued. 
   * Otherwise, the picking request is kept in a loading queue and will be 
   * loaded only when every requests issued before has been loaded.
   * 
   */
  public void processTask(String name) {
    String msg = "";
    if (loaders.containsKey(name) && loaders.get(name).isReady()) { // if this is an existing and ready employee
      Loader bob = (Loader) loaders.get(name);
      bob.setBusy();
      msg += name + " loaded the following requests: ";
      while (!loadingArea.isEmpty() // if the next pallet pair matches the next picking request
          && loadingArea.peek().getId() == id) {
        // Load onto the first truck in line
        Truck truck = incompleteTrucks.get(0);
        PickingRequest toLoad = loadingArea.remove();
        bob.load(toLoad.getPallets()[0], truck);
        bob.load(toLoad.getPallets()[1], truck);
        loadedReqs.add(toLoad);
        toLoad.passLoading();
        id ++;
        // Set return message
        msg += String.valueOf(toLoad.getId());
        msg += " ";
        if (truck.isFull()) {
          Truck temp = incompleteTrucks.remove(0);
          filledTrucks.add(temp);
          addTruck();
        }
      }
    } else if (loaders.containsKey(name)) { // else if this employee is busy
      msg += name + " is unavailable for loading.";
    } else { // else this employee does not exist
      msg += name + " is not an employed loader.";
    }
    MainSystem.log(msg);
  }  

  public void hire(String name) {
    loaders.put(name, new Loader(name));
    loaders.get(name).setReady();
  }


  /**
   * Getter for every incomplete trucks.
   * 
   * @return incomplete trucks in an ArrayList of truck objects
   */
  public ArrayList<Truck> getIncompleteTrucks() {  // for testing
    return incompleteTrucks;
  }

  /**
   * Getter for every filled trucks.
   * 
   * @return filled trucks in an ArrayList of truck objects
   */
  public ArrayList<Truck> getFilledTrucks() {  // for testing
    return filledTrucks;
  }

  /**
   * Rescan the fascia on the given pallets and check
   * that they match the required fascia in the coming
   * up picking request.
   *
   *@param name The unique ID of the loading employee.
   *@return True iff the rescan was performed and the 
   *        fascia SKUs match the picking request orders.
   */
  public boolean requestRescan(String name) {
    Loader loader = loaders.get(name);
    if (loader == null) {
      MainSystem.logWarning(name + " is not an employee.  Rescan not performed.");   
      return false;
    } else if (loader.isReady()) { // employee is ready
      loader.setBusy();
      if (loader.rescan(loadingArea)) {
        MainSystem.log(name + " rescanned fasica.  May proceed to loading.");
      } else {  // incorrect fascia
        return false;
      }
    } else { // employee is busy
      MainSystem.logWarning(name + " is busy.  Rescan not performed.");
      return false;
    }
    return true;
  }

  /**
   * Write Orders file that details the orders loaded
   * onto the trucks in the order they were loaded.
   */
  public void writeOrders() throws Exception {
    String delimiter = ",";
    String lineDelimiter = System.lineSeparator();

    FileWriter file = null;
      file = new FileWriter("orders.csv");
      for (PickingRequest request : loadedReqs) { // loop through loaded requests
        ArrayList<String[]> orders = (ArrayList<String[]>) request.getOrders();
        for (String[] order : orders) { // loop through individual orders
          file.write(order[0]);
          file.write(delimiter);
          file.write(order[1]);
          file.write(lineDelimiter);
        }
      }
      file.close();
  }
}
