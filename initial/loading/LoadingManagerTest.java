package loading;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import requests.PickingRequest;


public class LoadingManagerTest {
  private LoadingManager manager;
  private PickingRequest req1;
  private PickingRequest req2;

  @Before
  public void setUp() {
    manager = new LoadingManager();
    manager.addTruck("Fascia");
    manager.addTruck("Tires");
    req1 = new PickingRequest(1);
    req2 = new PickingRequest(2);
    manager.addRequest(req1);
    manager.addRequest(req2);
    manager.addSequencedRequest(req1);
    manager.addSequencedRequest(req2);
  }
  
  /**
   * Test getLoadedRequests after loadRequest method is called.
   */
 private void testGetLoadedRequests() {
   assertEquals(req1, manager.getLoadedRequests().get(0));
   assertEquals(req2, manager.getLoadedRequests().get(1));
  }
  
	
  /**
   * Test loadRequest method with same requests
   * in nextRequests and sequencedRequests
   */
  @Test
  public void testLoadRequest() {
	String msg = manager.loadRequest();
	
	assertEquals(3, manager.getIncompleteTrucks().size());
	testGetLoadedRequests();
	assertEquals("The following requests were loaded: 1 2 ", msg);
	}
  
  /**
   * Test loadRequest method with different requests
   * in nextRequests and sequencedRequests
   */
  @Test
  public void testLoadRequestMismatch() {
    //Set Up
    manager.loadRequest(); // remove all picking requests
    PickingRequest req3 = new PickingRequest(3);
    manager.addRequest(req3);
    manager.addSequencedRequest(req1); // add mismatching requests
    
    String m = manager.loadRequest();
    assertEquals("The following requests were loaded: ", m);
    // only contains the 1 request loaded in Set Up, not req3 or req1
    assertEquals(1, manager.getLoadedRequests().size()); 
  }
  
  /**
   * Test loadRequest method with one more request in
   * sequencedRequests than nextRequests
   */
  @Test
  public void testLoadRequestExtra() {
    // add extra request
    PickingRequest p3 = new PickingRequest(3);
    manager.addSequencedRequest(p3);
    
    String msg = manager.loadRequest();
    assertEquals(3, manager.getIncompleteTrucks().size());
    testGetLoadedRequests();
    assertEquals("The following requests were loaded: 1 2 ", msg);
  }
  
  /**
   * Test loadRequest with one more request in
   * nextRequests than sequencedRequests
   */
  @Test
  public void testLoadRequestExtra1() {
 // add extra request
    PickingRequest p3 = new PickingRequest(3);
    manager.addRequest(p3);
    
    String msg = manager.loadRequest();
    assertEquals(3, manager.getIncompleteTrucks().size());
    testGetLoadedRequests();
    assertEquals("The following requests were loaded: 1 2 ", msg);
  }
  
  
	/**
	 * test getFilledTrucks() {
	 */
	@Test
	public void testGetFilledTrucks() {
	  // Set Up
	  for (int i = 0; i < 18; i++) {
	    PickingRequest req = new PickingRequest(i);
	    manager.addRequest(req);
	    manager.addSequencedRequest(req);
	  }
	  manager.loadRequest(); // First truck is filled
	  
	  assertEquals(1, manager.getFilledTrucks().size());
	  assertEquals(3, manager.getIncompleteTrucks().size());
	}
	
	/**
	 * Test getIncompleteTrucks and therefore
	 * that the incomplete trucks were added correctly.
	 */
	@Test
	public void testGetIncompleteTrucks() {
	  assertEquals(3, manager.getIncompleteTrucks().size());
	}
	
	/**
	 * Check that an orders file is written
	 */
	@Test
	public void testWriteOrders() {
	  for (int i = 0; i < 4; i++) {
	    String[] order = {String.valueOf(i), String.valueOf(i + 1)};
	    req1.addOrder(order, String.valueOf(2* i), String.valueOf(5 * i));
	  }
	  manager.loadRequest();
	  manager.writeOrders();
	}
}

