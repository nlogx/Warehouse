package testClasses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import capital.Pallet;
import divisionHeads.LoadingManager;
import head.PickingRequest;
import java.io.IOException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LoadingManagerTest {
  /** A loading manager. */
  private LoadingManager manager;
  
  /** Picking Requests. */
  private static PickingRequest req1;
  private static PickingRequest req2;
  
  /** Pallets. */
  private static Pallet p1front;
  private static Pallet p1back;
  private static Pallet p2front;
  private static Pallet p2back;

  /**
   * Runs once before class.
   */
  @BeforeClass
  public static void setUpBeforeClass() {
    // Create Picking Requests
    req1 = new PickingRequest(1);
    req2 = new PickingRequest(2);
    
    // Add SKUs of orders to picking requests
    for (int i = 0; i < 4; i++) {
      String[] order = {"Color" + i, "Model" + i};
      req1.addOrder(order, String.valueOf(i), String.valueOf(2 * i));
      req2.addOrder(order, String.valueOf(3 * i), String.valueOf(4 * i)); 
    }
    
    // Create Pallets
    p1front = new Pallet();
    p1back = new Pallet();
    p2front = new Pallet();
    p2back = new Pallet();
    
    // Add fascia SKUs to pallets
    for (int i = 0; i < 4; i++) {
      p1front.add(String.valueOf(i));
      p1back.add(String.valueOf(2 * i));
      p2front.add(String.valueOf(3 * i));
      p2back.add(String.valueOf(4 * i));
    }
    
    // Assign Pallets to Picking Requests
    req1.setAssociatedPallets(p1front, p1back);
    req2.setAssociatedPallets(p2front, p2back);
  }
  
  /**
   * Runs once before each test case.
   */
  @Before
  public void setUp() {
    // Create loading manager
    manager = new LoadingManager();
    manager.hire("Ready Freddy");
    
    // Add picking requests and pallets to queue
    manager.addSequencedRequest(req2);
    manager.addSequencedRequest(req1);
  }
  
  /**
   * Test processTasks on an unavailable employee.
   */
  @Test
  public void testprocessTasksBusy() {
    // Create new picking requests and pallets
    Pallet p3front = new Pallet();
    Pallet p3back = new Pallet();
    Pallet p4front = new Pallet();
    Pallet p4back = new Pallet();
    PickingRequest req3 = new PickingRequest(3);
    PickingRequest req4 = new PickingRequest(4);
    req3.setAssociatedPallets(p3front, p3back);
    req4.setAssociatedPallets(p4front, p4back);
    //Set Ready Freddy to busy
    manager.processTask("Ready Freddy");
    // Add new picking requests and pallets.
    manager.addSequencedRequest(req3);
    manager.addSequencedRequest(req4);

    // call processTasks on a busy worker
    manager.processTask("Ready Freddy");
    
    // Check that only first two requests were loaded
    assertEquals(2, manager.getSequencedRequests().size());
    assertTrue(manager.getSequencedRequests().contains(req3));
    assertTrue(manager.getSequencedRequests().contains(req4));
    assertEquals(2, manager.getLoadedReqs().size());
    assertTrue(manager.getLoadedReqs().contains(req1));
    assertTrue(manager.getLoadedReqs().contains(req2));
  }
  
  /**
   * Test processTasks on a non-existent worker.
   */
  @Test
  public void testprocessTasksNon() {
    manager.processTask("Freddy Ready");
    assertEquals(2, manager.getSequencedRequests().size());
    assertEquals(0, manager.getLoadedReqs().size());
  }
  
  /**
   * Test processTasks on a ready employee.
   */
  @Test
  public void testprocessTasksReady() {
    manager.processTask("Ready Freddy");
    // Check picking requests were added to loaded requests queue
    assertEquals(2, manager.getLoadedReqs().size());
    assertTrue(manager.getLoadedReqs().contains(req1));
    assertTrue(manager.getLoadedReqs().contains(req2));
    assertTrue(req1.isLoaded());
    assertTrue(req2.isLoaded());
    // Check picking requests were removed from pallet and requests queues
    assertEquals(0, manager.getSequencedRequests().size());
    // Check that the truck is still listed as incomplete.
    assertEquals(1, manager.getIncompleteTrucks().size());
    assertEquals(0, manager.getFilledTrucks().size());
  }
  
  /**
   * Test processTask with missing picking requests.
   * Assume worker is ready.
   */
  @Test
  public void testprocessTasksMissing() {
    // Set Up
    PickingRequest req3 = new PickingRequest(3);
    Pallet p3front = new Pallet();
    Pallet p3back = new Pallet();
    req3.setAssociatedPallets(p3front, p3back);
    LoadingManager lm = new LoadingManager();
    lm.hire("Ready Freddy");
    lm.addSequencedRequest(req1);
    lm.addSequencedRequest(req3);
    
    // Call processTask
    lm.processTask("Ready Freddy");

    // Check only one request loaded
    assertEquals(1, lm.getLoadedReqs().size());
    assertTrue(lm.getLoadedReqs().contains(req1));
    assertEquals(1, lm.getSequencedRequests().size());
    assertTrue(lm.getSequencedRequests().contains(req3));
  }
  
  /**
   * Test processTask with no picking requests queued.
   */
  @Test
  public void testprocessTaskNone() {
    // SetUp
    LoadingManager lm = new LoadingManager();
    lm.hire("Ready Freddy");
    
    lm.processTask("Ready Freddy");
    assertEquals(0, lm.getLoadedReqs().size());
  }
  
  /**
   * Test processTask on 20 requests that fill up a truck.
   */
  @Test
  public void testprocessTasksFull() {
    // Create 18 more requests and add to sequenced requests.
    for (int i = 3; i <= 20; i++) {
      manager.addSequencedRequest(new PickingRequest(i));      
    }
    // Call processTasks and check assertions
    manager.processTask("Ready Freddy");
    assertEquals(0, manager.getSequencedRequests().size());
    assertEquals(20, manager.getLoadedReqs().size());
    assertEquals(1, manager.getFilledTrucks().size());
    assertEquals(1, manager.getIncompleteTrucks().size()); // since new incomplete truck gets added
  }
  
  /**
   * Test setToReady on a worker who has finished loading a picking request
   * and is called to load another.
   */
  @Test
  public void testSetToReady() {
    // Create new picking requests and pallets
    Pallet p3front = new Pallet();
    Pallet p3back = new Pallet();
    Pallet p4front = new Pallet();
    Pallet p4back = new Pallet();
    PickingRequest req3 = new PickingRequest(3);
    PickingRequest req4 = new PickingRequest(4);
    req3.setAssociatedPallets(p3front, p3back);
    req4.setAssociatedPallets(p4front, p4back);
    //Set Ready Freddy to busy
    manager.processTask("Ready Freddy");
    // Re-add picking requests and pallets.
    manager.addSequencedRequest(req3);
    manager.addSequencedRequest(req4);
    // Set Ready Freddy to ready
    manager.hire("Ready Freddy");
    // Call processTasks and check assertions
    manager.processTask("Ready Freddy");
    // Check picking requests were added to loaded requests queue
    assertEquals(4, manager.getLoadedReqs().size());
    assertTrue(manager.getLoadedReqs().contains(req1));
    assertTrue(manager.getLoadedReqs().contains(req2));
    assertTrue(manager.getLoadedReqs().contains(req3));
    assertTrue(manager.getLoadedReqs().contains(req4));
    // Check picking requests were removed from pallet and requests queues
    assertEquals(0, manager.getSequencedRequests().size());
    // Check that the truck is still listed as incomplete.
    assertEquals(1, manager.getIncompleteTrucks().size());
    assertEquals(0, manager.getFilledTrucks().size());
  }
  
  /**
   * Test rescan method on a busy employee.
   */
  @Test
  public void testRescanBusy() {
    // Make employee busy
    manager.requestRescan("Ready Freddy");
    
    // call method on now busy worker
    assertFalse(manager.requestRescan("Ready Freddy"));
  }
  
  /**
   * Test rescan method on a non-employee.
   */
  @Test
  public void testRescanNon() {
    assertFalse(manager.requestRescan("Ted"));
  }
  
  /**
   * Test rescan method on an available employee
   * with correct fascia.
   */
  @Test
  public void testRescanCorrect() {
    // change the fascia on the pallets back to correct
    Pallet pfront = new Pallet();
    for (int i = 0; i < 4; i++) {
      pfront.add(String.valueOf(3 * i));
    }
    req2.setAssociatedPallets(pfront, p2back);
    
    boolean rescan = manager.requestRescan("Ready Freddy");
    assertTrue(rescan);
  }
  
  /**
   * Test rescan method on an available employee
   * with incorrect fascia.
   */
  @Test
  public void testRescanIncorrect() {
    // change the last fascia on the second front pallet to incorrect
    Pallet pfront = new Pallet();
    for (int i = 0; i < 3; i++) {
      pfront.add(String.valueOf(3 * i));
    }
    pfront.add("the wrong fascia");
    req2.setAssociatedPallets(pfront, p2back);;
    
    boolean temp = manager.requestRescan("Ready Freddy");
    assertFalse(manager.requestRescan("Ready Freddy"));
  }
  
  /**
   * Check that a csv file with loaded orders is created and written to.
   * @throws IOException Possibly when instantiating or closing FileWriter
   */
  @Test
  public void testWriteOrders() throws Exception {
    manager.processTask("Ready Freddy");

    manager.writeOrders();
  }
  

}
