package loading;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import requests.PickingRequest;
import requests.PickingRequestComparator;


/**
 * Manages any loading request. This class LoadingManager is responsible of loading every picking
 * requests on the trucks. It will only load a given picking request if it was the issued
 * immediately after the last one loaded. It keeps track of incompleteTrucks (one or many, depending
 * on the number of products) and filledTrucks. It also keeps track of every loaded picking
 * requests.
 * 
 * @author AlexChum
 *
 */
public class LoadingManager {
  //StringLengthComparator.java

  private ArrayList<Truck> incompleteTrucks;
  private PriorityQueue<PickingRequest> nextRequests;
  private PriorityQueue<PickingRequest> sequencedRequests;
  private ArrayList<Truck> filledTrucks;
  private ArrayList<PickingRequest> loadedRequests;


  /**
   * The class constructor. This constructor instantiates 3 ArrayList and 2 PriorityQueues.
   */
  public LoadingManager() {
    this.incompleteTrucks = new ArrayList<Truck>();
    this.filledTrucks = new ArrayList<Truck>();
    this.loadedRequests = new ArrayList<PickingRequest>();
    Comparator<PickingRequest> comparator = new PickingRequestComparator();
    this.sequencedRequests = new PriorityQueue<PickingRequest>(comparator);
    this.nextRequests = new PriorityQueue<PickingRequest>(comparator);
    addTruck("Fascia");
  }

  /**
   * Creates a new empty truck object and adds it to the incomplete trucks ArrayList.
   *
   * @param item the string representation of the item that the truck will contain
   */
  public void addTruck(String item) {
    Truck truck = new Truck(item);
    incompleteTrucks.add(truck);
  }

  /**
   * Receives a loading requests from the system and process the given picking request accordingly.
   * 
   * <p>If the request is the next picking request issued after the last one loaded. 
   * If it is the first loading request, it must be the first picking request issued. 
   * Otherwise, the picking request is kept in a loading queue and will be 
   * loaded only when every requests issued before has been loaded.
   */
  public String loadRequest() {
    String msg = "The following requests were loaded: ";
    while (!sequencedRequests.isEmpty() 
        && !nextRequests.isEmpty() 
        && nextRequests.peek().equals(sequencedRequests.peek())) {
      Truck truck = incompleteTrucks.get(0);
      truck.loadRequest(sequencedRequests.peek());
      loadedRequests.add(sequencedRequests.remove());
      msg += String.valueOf(nextRequests.remove().getId());
      msg += " ";
      if (truck.isFull()) {
        Truck temp = incompleteTrucks.remove(0);
        filledTrucks.add(temp);
        addTruck(temp.getItem());
      }
    }
    return msg;
  }
  
  public void addSequencedRequest(PickingRequest request) {
    sequencedRequests.add(request);
  }
  
  /**
   * Adds a picking request to the nextRequests.
   * 
   * @param request the Picking Request issued
   */
  public void addRequest(PickingRequest request) {
    nextRequests.add(request);
  }

  /**
   * Getter for every loaded requests.
   * 
   * @return loaded picking requests in an ArrayList of picking requests objects
   */
  public ArrayList<PickingRequest> getLoadedRequests() {
    return loadedRequests;
  }

  /**
   * Getter for every incomplete trucks.
   * 
   * @return incomplete trucks in an ArrayList of truck objects
   */
  public ArrayList<Truck> getIncompleteTrucks() {
    return incompleteTrucks;
  }

  /**
   * Getter for every filled trucks.
   * 
   * @return filled trucks in an ArrayList of truck objects
   */
  public ArrayList<Truck> getFilledTrucks() {
    return filledTrucks;
  }
  
  /**
   * Rescan the fascia on the given pallets and check
   * that they match the required fascia in the coming
   * up picking request.
   */
  public boolean rescan(ArrayList<String>[] pallets) {
    
  }
  
  /**
   * Write Orders file that details the orders loaded
   * onto the trucks in the order they were loaded.
   */
  public void writeOrders() {
    String delimiter = ",";
    String lineDelimiter = System.lineSeparator();
    
    FileWriter file = null;
    try {
      file = new FileWriter("orders.csv");
      for (PickingRequest request : loadedRequests) { // loop through loaded requests
        ArrayList<String[]> orders = request.getOrders();
        for (String[] order : orders) { // loop through individual orders
          file.write(order[0]);
          file.write(delimiter);
          file.write(order[1]);
          file.write(lineDelimiter);
        }
      }
      file.close();
    } catch (IOException exception) {
      System.out.println("Issue with creating and writing orders file");
      exception.printStackTrace();
    }
  }

}


