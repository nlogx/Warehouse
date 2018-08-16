package loading;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import requests.PickingRequest;

public class TruckTest {
  /** A truck. */
  private static Truck truck1;
  /** A second truck. */
  private static Truck truck2;
  /** A third truck. */
  private static Truck truck3;
  /** A full truck. */
  private static Truck fullTruck;
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    truck1 = new Truck("Mail");
    truck2 = new Truck("Food");
    fullTruck = new Truck("iPhones");
    truck3 = new Truck("Underappreciated Trucks");
    for (int i = 0; i < 20; i++) {
      fullTruck.loadRequest(new PickingRequest(i));
    }
  }
  
  /**
   * Test that the item attribute is properly set
   * when Truck is constructed.
   */
  @Test
  public void testGetItem() {
    assertEquals("Mail", truck1.getItem());
    assertEquals("Food", truck2.getItem());
  }
  
//  /**
//   * Test that the loaded requests ArrayList
//   * is set.
//   */
//  @Test
//  public void testGetRequests() {
//    assertEquals(0, truck1.getRequests().size());
//  }
  
//  /**
//   * Test that capacity and space remaining
//   * are initially set correctly (an empty truck).
//   */
//  @Test
//  public void testGetSpace() {
//    assertEquals(20, truck1.getSpace());
//    assertEquals(20, truck1.getCapacity());
//  }
  
//  /**
//   * Test setter method for capacity.
//   */
//  @Test
//  public void testSetCapacity() {
//    truck1.setCapacity(10);
//    assertEquals(10, truck1.getCapacity());
//    truck1.setCapacity(20); // reset the Truck static capacity variable
//  }
  
  /**
   * Test isFull on empty truck.
   */
  @Test
  public void testIsFull() {
    assertFalse(truck1.isFull());
  }

//  /**
//   * Test loadRequest method loading 1.
//   */
//  @Test
//  public void testLoadRequest() {
//    PickingRequest req = new PickingRequest(1);
//    truck2.loadRequest(req);
//    assertEquals(1, truck2.getRequests().size());
//  }
  
  /**
   * Test isFull on partially full truck.
   */
  @Test
  public void testIsFull1() {
    PickingRequest req = new PickingRequest(1);
    truck1.loadRequest(req);
    assertFalse(truck1.isFull());
  }
  
  /**
   * Test isFull on full truck and that picking
   * requests are loaded.
   */
  @Test
  public void testIsFull2() {
    assertTrue(fullTruck.isFull());
    // assertEquals(20, fullTruck.getRequests().size());
  }
  
//  /**
//   * Test getSpace on partially full Truck.
//   */
//  @Test
//  public void getSpace1() {
//    truck3.loadRequest(new PickingRequest(7));
//    assertEquals(19, truck3.getSpace());
//  }
//  
//  /**
//   * Test getSpace on full Truck.
//   */
//  @Test
//  public void getSpaceNone() {
//    assertEquals(0, fullTruck.getSpace());
//  }
}
