package head;

import divisionHeads.LoadingManager;
import divisionHeads.PickingManager;
import divisionHeads.SequenceManager;
import divisionHeads.WarehouseManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * The MainSystem. This class serves as the top entity of our system. 
 * It handles Inputs and Outputs of files; Reads and processes the events; 
 * Serves as a medium between the different managers; Logs the events and
 * inputs accordingly in a console.
 * 
 * @author AlexChum
 *
 */
public class MainSystem {

  private static final Logger logger = Logger.getLogger(MainSystem.class.getName());
  private static final Handler consoleHandler = new ConsoleHandler();
  private static List<String> logtxt = new ArrayList<String>();

  private int requestSize = 4;
  private int pickingNum = 1;
  private List<String[]> orders;

  private LoadingManager loadingManager;
  private PickingManager pickingManager;
  private SequenceManager sequenceManager;
  private WarehouseManager whManager;

  private Map<Integer, String[]> skuTable;


  /**
   * Instantiates a new main system, the different managers and a translation table.
   * Sets up the console for logging.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public MainSystem() throws IOException {
    logger.setLevel(Level.ALL);
    consoleHandler.setLevel(Level.ALL);
    logger.addHandler(consoleHandler);

    loadingManager = new LoadingManager();
    pickingManager = new PickingManager();
    sequenceManager = new SequenceManager();
    // Temp
    ArrayList<String> format = new ArrayList<>();
    format.add("2,2,3,4");
    skuTable = new HashMap<Integer, String[]>();
    whManager = new WarehouseManager(format,
        readFromCsvFile("initial.csv"));
    translationTable("translation.csv");
    
  }


  /**
   * Process a given event properly and logs the aftermath of the event. This methods makes use of
   * other classes to delegate the work.
   * 
   * @param event the string representation of the event
   */
  private void assessEvent(String line) {
    String[] event = (line.trim()).split("\\s+");
    String name = event[1];
    String end = event[event.length - 1];

    if (event[0].equals("Order")) {
      String[] order = new String[] {event[1], event[2]};
      orders.add(order);
      if (orders.size() == requestSize) {
        PickingRequest request = new PickingRequest(pickingNum);
        pickingNum++;
        while (!orders.isEmpty()) {
          order = orders.remove(0);
          String[] skus = skuTable.get(getCode(order[0], order[1]));
          request.addOrder(order, skus[0], skus[1]);
        }
        request.addLocations(WarehousePicking.optimize(request.getSkus()));
        pickingManager.addRequest(request);
        log("Picking Request " + String.valueOf(request.getId()) + " has been created.");
      }
    } else if (event[0].equals("Picker")) {
      if (end.equals("ready")) {
        pickingManager.hire(name);
      } else if (end.equals("marshalling")) {
        PickingRequest pr = pickingManager.marshalling(name);
        sequenceManager.addRequest(pr);
      } else {
        String picked = pickingManager.processTask(name, end);
        if (picked != null) {
          whManager.pick(picked);
        }
      }
    } else if (event[0].equals("Sequencer")) {
      if (end.equals("ready")) {
        sequenceManager.hire(name);
      } else {
        PickingRequest pr = sequenceManager.processTask(name);
        if (pr.isSequenced()) {
          loadingManager.addSequencedRequest(pr);
        } else {
          pickingManager.addRequest(pr);
        }
      }
    } else if (event[0].equals("Loader")) {
      if (end.equals("ready")) {
        loadingManager.hire(name);
      } else {
        loadingManager.processTask(name);
      }
    } else if (event[0].equals("Replenisher")) {
      if (end.equals("ready")) {
        whManager.hire(name);
      } else {
        String location = Arrays.stream(event, 3, 6).collect(Collectors.joining(","));
        whManager.restock(name, location);
      }
    } else {
      logger.log(Level.WARNING, "Unrecognized event");
    }
  }

  /**
   * Logs a sentence to the console.
   *
   * @param sentence the sentence logged
   */
  public static void log(String sentence) {
    logtxt.add("INFO " + sentence);
    logger.log(Level.INFO, sentence);
  }

  /**
   * Logs a warning to the console.
   *
   * @param sentence the sentence logged
   */
  public static void logWarning(String sentence) {
    logtxt.add("WARNING " + sentence);
    logger.log(Level.WARNING, sentence);
  }

  /**
   * Instantiates a SKU Translation table.
   *
   * @param fileName the file name of the translation table
   * @throws FileNotFoundException the file not found exception
   */
  private void translationTable(String fileName) throws FileNotFoundException {
    Scanner scanner = openFile(fileName);
    String[] line;

    while (scanner.hasNextLine()) {
      line = scanner.nextLine().split(",");
      String[] skus = new String[] {line[2], line[3]};
      skuTable.put(getCode(line[0], line[1]), skus);
    }
    scanner.close();
  }

  /**
   * Generates 1 hashcode using 2 strings.
   *
   * @param word1 the first string 
   * @param word2 the second string
   * @return the hashcode
   */
  private Integer getCode(String word1, String word2) {
    return word1.hashCode() ^ word2.hashCode();
  }

  /**
   * Reads the event log and calls AssessEvent to process each line.
   * 
   * @param filePath the name of the file containing the events
   * @throws Exception 
   */
  public void readEvents(String filePath) throws Exception {

    Scanner scanner = new Scanner(new FileInputStream(filePath));
    String event;

    while (scanner.hasNextLine()) {
      event = scanner.nextLine();
      logtxt.add(event);
      assessEvent(event);
    }
    scanner.close();

    loadingManager.writeOrders();
    whManager.writeFinalState();
    writeLogFile();
  }

  /**
   * Reads a CSV file and return each line as a string in a ArrayList.
   * 
   * @param fileName the name of the data file
   * @throws FileNotFoundException if fileName is not a valid name
   */
  private ArrayList<String> readFromCsvFile(String fileName) throws FileNotFoundException {

    Scanner scanner = openFile(fileName);
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
   * Open a file and return it as a Scanner.
   *
   * @param fileName the file name
   * @return the scanner
   * @throws FileNotFoundException the file not found exception
   */
  private Scanner openFile(String fileName) throws FileNotFoundException {
    File file = new File(fileName);
    String filePath = file.getAbsolutePath();

    Scanner scanner = new Scanner(new FileInputStream(filePath));
    return scanner;
  }
  
  private void writeLogFile() throws IOException {
    try {
      Path file = Paths.get("log.txt");
      Files.write(file, logtxt, Charset.forName("UTF-8"));
    } catch (Exception myException) {
      log("Writing log.txt failed.");
    }
  }

}
