package requests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class PickerTest {
  /**Pickers*/
  private static Picker p1;
  private static Picker p2;
  
  /**PickingRequests. */
  private static PickingRequest req;
  private static PickingRequest req2;
  
  @Before
  public void setUp() throws Exception {
    p1 = new Picker("Alice");
    p2 = new Picker("Bob");
    
    req = new PickingRequest(101);
    ArrayList<String> locations = new ArrayList<>(8);
    for (int i = 0; i < 8; i++) {
      locations.add(String.valueOf(i)); // add locations
    }
    req.addLocations(locations);
    for (int i = 0; i < 8; i += 2) {
      String[] order = {String.valueOf(i), String.valueOf(i + 1)}; // create an order
      req.addOrder(order, String.valueOf(i), String.valueOf(i + 1));
    }
    req2 = new PickingRequest(207);
  }
  
  /**
   * Test request.
   * Test that an unoccupied picker is sent to fill a picking request.
   */
  @Test
  public void testRequest() {
    String[] msg = p1.request(req);
    
    assertEquals("Alice is now assigned request: 101", msg[0]);
    assertEquals("Alice go to: 0", msg[1]);
    assertEquals(null, msg[2]);
    assertEquals(req, p1.getTask());
    assertFalse(p1.getStatus());
    assertEquals("0", p1.getLocation());
  }

  /**
   * Check the message sent when an occupied picker
   * is sent another picking request.  Check that the 
   * picker does not get reassigned.
   */
  @Test
  public void testRequestBusy() {
    //SetUp
    p1.request(req);
    
    String[] msg = p1.request(req2);
    assertEquals("Alice is busy with request: 101", msg[0]);
    assertEquals("Alice previous destination was: 0", msg[1]);
    assertEquals(null, msg[2]);
    assertEquals(req, p1.getTask());
    assertFalse(p1.getStatus());
    assertEquals("0", p1.getLocation());
  }
  
  /**
   * Test getFascia method on with incomplete
   * picking request (therefore, forklift is not full).
   * Check message returned.
   */
  @Test
  public void testGetFascia() {
    //Set Up
    p1.request(req);
    
    for (int i = 0; i < 7; i++) {
      // Check correct message
      String[] msg = p1.getFascia();
      assertEquals("Alice picked up fascia: " + i, msg[0]);
      assertEquals("Alice go to: " + (i + 1), msg[1]);
      assertEquals(String.valueOf(i), msg[2]);
      
      // Check correct attributes
      assertEquals(req, p1.getTask());
      assertFalse(p1.getStatus());
    }
    // for last fascia
    String[] endMsg = p1.getFascia();
    assertEquals("Alice picked up fascia: 7", endMsg[0]);
    assertEquals("Alice is ready to go to marshalling.", endMsg[1]);
    assertEquals("7", endMsg[2]);
  }
  
  /**
   * Test getFascia method for a picker
   * who has completed a picking request
   * (therefore the forklift is full).
   * Check message returned.
   */
  @Test
  public void testGetFasciaFull() {
    //Set Up
    p1.request(req);
    for (int i = 0; i < 8; i++) {
      p1.getFascia();
    }
    String[] msg = p1.getFascia();
    assertEquals("Forklift already full.", msg[0]);
    assertEquals("Alice previous location was: ", msg[1]);
    assertEquals("7", msg[2]);
  }
  
  /**
   * Test marshaling on a picker that has not completed
   * a picking request.
   */
  @Test
  public void testMarshalling() {
    String[] msg = p2.marshalling();
    assertEquals("Picker Bob is not ready to go to marshalling.", msg[0]);
  }
  
  /**
   * Test marshaling on a picker that is has completed
   * a picking request.
   */
  @Test
  public void testMarshallingReady() {
    //Set Up
    p1.request(req);
    for (int i = 0; i < 8; i++) {
      p1.getFascia();
    }
    
    String[] msg = p1.marshalling();
    assertEquals("Picker Alice went to marshalling.", msg[0]);
    assertEquals("0,1,2,3,4,5,6,7,", msg[1]);
    assertTrue(p1.getStatus());
  }

}
