package master;

import inventory.WarehouseManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import loading.LoadingManager;
import requests.OrderManager;
import requests.WarehousePicking;

/**
 * The top-level entity of this warehouse system. This class reads given files to instantiate
 * the amounts of fascia in the warehouse and the table that maps car models and colours to SKU
 * numbers. It also handles every events from the event log (coming from barcode readers and FAX)
 * and does the appropriate action requested.After the action is done, this system will log 
 * information in a console about the event.
 * 
 * @author AlexChum
 */
public class SystemManager {
  

  // All instances of SystemManager will share one Logger, so it's
  // static.
  // We use SystemManager.class.getName() to get the fully-qualified
  // name of this class, which happens to be "master.SystemManager".
  // We use this name as the name of our Logger object.
  // Using the class class is called "reflection".
  private static final Logger logger = Logger.getLogger(SystemManager.class.getName());
  // private static final Handler consoleHandler = new ConsoleHandler();

  
  private ArrayList<String[]> orders = new ArrayList<String[]>(4);
  private OrderManager orderManager;
  private WarehouseManager warehouseManager;
  private LoadingManager loadingManager;
  
  /**
   * Class Constructor. It associates the consoleHandler to the logger. It instantiates a 
   * warehouseManager, orderManager and loadingManager by reading 2 file paths. One leading to 
   * the SKU table and the other,to the warehouse initial state.
   * 
   * @param filePath the path to the file containing the table with the SKU's
   * @param filePath2 the path to the file containing the initial state of the warehouse 
   * @throws IOException if the file(s) path is(are) not valid
   */
  public SystemManager(String filePath, String filePath2) throws IOException {
    
    // Associate the handler with the logger.
    logger.setLevel(Level.ALL);
//    consoleHandler.setLevel(Level.ALL);
//    logger.addHandler(consoleHandler);
    
    warehouseManager = new WarehouseManager(readFromCsvFile(filePath2));
    loadingManager = new LoadingManager();
    orderManager = new OrderManager(readFromCsvFile(filePath), loadingManager);
  }
  
  /**
   * Class Constructor. It associates the consoleHandler to the logger. It instantiates a 
   * warehouseManager, orderManager and loadingManager by reading 1 file path.
   * 
   * @param filePath the path to the file containing the table with the SKU's
   * @throws IOException if the file(s) path is(are) not valid
   */
  public SystemManager(String filePath) throws IOException {
    
    // Associate the handler with the logger.
    logger.setLevel(Level.ALL);
    
    warehouseManager = new WarehouseManager();
    loadingManager = new LoadingManager();
    orderManager = new OrderManager(readFromCsvFile(filePath), loadingManager);
  }
  
  
  /**
   * Reads a CSV file and return each line as a string in a ArrayList.
   * 
   * @param filePath the path of the data file
   * @throws FileNotFoundException if filePath is not a valid path
   */
  public ArrayList<String> readFromCsvFile(String filePath) throws FileNotFoundException {

    // FileInputStream can be used for reading raw bytes, like an image.
    Scanner scanner = new Scanner(new FileInputStream(filePath));
    String record;
    
    // Returns an Array list to become the arguments of a constructor
    ArrayList<String> args = new ArrayList<String>();
    while (scanner.hasNextLine()) {
      record = scanner.nextLine();
      args.add(record);
    }
    scanner.close();
    return args;
  }
  
  /**
   * Reads the event log and calls AssessEvent to process each line. 
   * 
   * @param fileName the name of the file containing the events
   * @throws IOException if fileName is not a valid file
   */
  public void readEvents(String fileName) throws IOException {
    Scanner scanner = new Scanner(new FileInputStream(fileName));
    String line;
    
    while (scanner.hasNextLine()) {
      line = scanner.nextLine();
      assessEvent(line);
    }
    scanner.close();
    
    loadingManager.writeOrders();
    warehouseManager.writeFinalState();
  }
  
  /**
   * Process a given event properly and logs the aftermath of the event. This methods makes use of
   * other classes to delegate the work.
   * 
   * @param event the string representation of the event
   */
  private void assessEvent(String event) {
    String[] action = (event.trim()).split("\\s+");
    if (action[0].equals("Order")) {
      String[] order = {action[1], action[2]};     
      orders.add(order);
      if (orders.size() == 4) {
        logger.log(Level.INFO, orderManager.createRequests(orders));
        orders.clear();
      }
    } else if (action[0].equals("Picker")) {
      String name = action[1];
      if (action[action.length - 1].equals("Marshaling")) {
        String msg = orderManager.marshalling(name);
        if (msg.contains("never") | msg.contains("not")) {
          logger.log(Level.WARNING, msg);
        } else {
          logger.log(Level.INFO, msg);
        }
      } else {
        String[] msg = orderManager.processRequest(name);
        if (msg[0].contains("full")) {
          logger.log(Level.WARNING, msg[0]);
          logger.log(Level.WARNING, msg[1] + msg[2]);
        } else if (msg[0].contains("busy")) {
          logger.log(Level.WARNING, msg[0]);
          logger.log(Level.WARNING, msg[1] + msg[2]);
        } else {
          logger.log(Level.INFO, msg[0]);
          if (msg[2] != null) {
            String[] whmsg = warehouseManager.pick(msg[2]);
            logger.log(Level.INFO, whmsg[0]);
          }
          logger.log(Level.INFO, msg[1]);
        }
      } 
    } else if (action[0].equals("Sequencer")) {
      ArrayList<String> msg = orderManager.sequencing();
      if (msg.size() == 1) {
        logger.log(Level.WARNING, msg.get(0));
      } else {
        for (int i = 1; i < msg.size(); i++) {
          logger.log(Level.INFO, msg.get(i));
        }
        logger.log(Level.INFO, msg.get(0));
      }
    } else if (action[0].equals("Loader")) {
      logger.log(Level.INFO, loadingManager.loadRequest());
    } else if (action[0].equals("Replenisher")) {
      if (action[2].equals("ready")) {
        warehouseManager.addReplenisher(action[1]);
      } else {
        StringJoiner joiner = new StringJoiner(",");
        joiner.add(action[3]).add(action[4]).add(action[5]).add(action[6]);
        String location = joiner.toString();
        logger.log(Level.INFO, warehouseManager.restock(action[1], location));
      }
    }
  }
  
  public static void main (String[] args) throws IOException {
    SystemManager systemManager;
    
    File events = new File("16orders.txt");
    File traversalTable = new File("traversal_table.csv");
    File translation = new File("translation.csv");
    String eventsPath = events.getAbsolutePath();
    String traversalPath = traversalTable.getAbsolutePath();
    String translationPath = translation.getAbsolutePath();
    
    File initialstate = new File("initial.csv");
    if (initialstate.exists() && initialstate.isFile()){
      String statePath = initialstate.getAbsolutePath();
      systemManager = new SystemManager(translationPath, statePath);
    } else {
      systemManager = new SystemManager(translationPath);
    }
    try {
      WarehousePicking warehousePicking = new WarehousePicking(systemManager.readFromCsvFile(traversalPath));
    } catch (Exception e){
    }
    systemManager.readEvents(eventsPath);

  }
}


