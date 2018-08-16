package testClasses;

import capital.Warehouse;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class WarehouseTest {
  /** A full warehouse object. */
  private Warehouse simpleWarehouseFull;
  /** A partially full Warehouse object. */
  private Warehouse simpleWarehousePartial;
  /** An empty Warehouse object. */
  private Warehouse simpleWarehouseEmpty;
  /** An ArrayList of Warehouse objects. */
  private ArrayList<Warehouse> subWarehouse;
  /** A Warehouse object containing Warehouse objects. */
  private Warehouse subdividedWarehouse;
  /** A default sized warehouse, fully stocked. */
  private Warehouse warehouse;

  /**
   * Sets up test fixture. Runs before every test.
   */
  @Before
  public void setUp() {
    simpleWarehouseFull = new Warehouse("A Full Shelf");
    simpleWarehousePartial = new Warehouse("A Partially Full Shelf", 20);
    simpleWarehouseEmpty = new Warehouse("An Empty Shelf", 0);
    subWarehouse = new ArrayList<Warehouse>();
    subWarehouse.add(simpleWarehouseFull);
    subWarehouse.add(simpleWarehousePartial);
    subWarehouse.add(simpleWarehouseEmpty);
    subdividedWarehouse = new Warehouse("A Rack containing Shelves", subWarehouse);
    
    Integer[] format = {2 ,2 ,3 ,4};
    warehouse = new Warehouse("A Warehouse", format);
  }
  
  /**
   * Create a warehouse with no substorage.
   * @return 
   */
  @Test
  public void testWarehouseEmpty() {
    Integer[] format = {};
    
    Warehouse w2 = new Warehouse("An empty warehouse", format);
    assertEquals(0, w2.getSub().size());
  }
  
  /**
   * Test MAXCAPACITY is set at 30 for all four Warehouse units.
   */
  @Test
  public void testWarehouseMaxCapacity() {
    assertEquals(30, simpleWarehouseFull.getMaxCapacity());
    assertEquals(30, simpleWarehousePartial.getMaxCapacity());
    assertEquals(30, simpleWarehouseEmpty.getMaxCapacity());
    assertEquals(30, subdividedWarehouse.getMaxCapacity());
  }
  
  /**
   * Test name is set for all four Warehouse units.
   */
  @Test
  public void testWarehouseName() {
    assertEquals("A Full Shelf", simpleWarehouseFull.getName());
    assertEquals("A Partially Full Shelf", simpleWarehousePartial.getName());
    assertEquals("An Empty Shelf", simpleWarehouseEmpty.getName());
    assertEquals("A Rack containing Shelves", subdividedWarehouse.getName());
  }
  
  /**
   * Test FasciaAmt is set properly.
   */
  @Test
  public void testWarehouseFasciaAmt() {
    assertEquals(30, simpleWarehouseFull.getFasciaAmt());
    assertEquals(20, simpleWarehousePartial.getFasciaAmt());
    assertEquals(0, simpleWarehouseEmpty.getFasciaAmt());
    assertEquals(0, subdividedWarehouse.getFasciaAmt());
  }
  
  /**
   * Test setFasciaAmt method, setting fascia amount
   * to a number between 0 and maximum (30) inclusive.
   */
  @Test
  public void testSetFasciaAmt() {
    simpleWarehouseFull.setFasciaAmt(25);
    assertEquals(25, simpleWarehouseFull.getFasciaAmt());
  }
  
  /**
   * Test setFasciaAmt to zero.
   */
  @Test
  public void testSetFasciaAmtZero() {
    simpleWarehouseFull.setFasciaAmt(0);
    assertEquals(0, simpleWarehouseFull.getFasciaAmt());
  }
  
  /**
   * Test decrementFascia method
   */
  @Test
  public void testDecrementFascia() {
    simpleWarehouseFull.decrementFascia();
    assertEquals(29, simpleWarehouseFull.getFasciaAmt());
  }
  
  /**
   * Test subWarehouse is set properly.
   */
  @Test
  public void testWarehouseSubWarehouse() {
    assertEquals(0, simpleWarehouseFull.getSub().size());
    assertEquals(0, simpleWarehousePartial.getSub().size());
    assertEquals(0, simpleWarehouseEmpty.getSub().size());
    assertEquals(3, subdividedWarehouse.getSub().size());
  }
  
  // Used to test setInitialState separately, set method to public to run.
//  /**
//   * Test setInitialState method
//   */
//  @Test
//  public void testSetInitialState() {
//    String state = "B,1,0,3,16";
//    warehouse.setInitialState(state);
//    Warehouse level = warehouse.getStorageAt("B,1,0,3");
//    assertEquals(16, level.getFasciaAmt());
//    assertEquals(30, warehouse.getStorageAt("A,0,1,1").getFasciaAmt());
//    assertEquals(30, warehouse.getStorageAt("B,0,0,2").getFasciaAmt());
//    assertEquals(30, warehouse.getStorageAt("B,1,1,2").getFasciaAmt());
//    assertEquals(30, warehouse.getStorageAt("B,1,0,2").getFasciaAmt());
//  }
  
  /**
   * Test getStorageAt()
   */
  @Test
  public void testGetStorageAt() {
    Warehouse w1 = warehouse.getSub().get(0);
    Warehouse w2 = warehouse.getStorageAt("A");
    assertEquals(w1, w2);
    Warehouse levelA0 = warehouse.getSub().get(0).getSub().get(0);
    assertEquals(levelA0, warehouse.getStorageAt("A,0"));
    Warehouse levelA00 = warehouse.getSub().get(0).getSub().get(0).getSub().get(0);
    assertEquals(levelA00, warehouse.getStorageAt("A,0,0"));
    Warehouse levelA000 = warehouse.getSub().get(0).getSub().get(0).getSub().get(0).getSub().get(0);
    assertEquals(levelA000, warehouse.getStorageAt("A,0,0,0"));
  }
  
  @Test
  public void testGetStorageAt1() {
    assertEquals(warehouse.getSub().get(1), warehouse.getStorageAt("B"));
    Warehouse levelB1 = warehouse.getSub().get(1).getSub().get(1);
    assertEquals(levelB1, warehouse.getStorageAt("B,1"));
    Warehouse levelB12 = warehouse.getSub().get(1).getSub().get(1).getSub().get(2);
    assertEquals(levelB12, warehouse.getStorageAt("B,1,2"));
    Warehouse levelB123 = warehouse.getSub().get(1).getSub().get(1).getSub().get(2).getSub().get(3);
    assertEquals(levelB123, warehouse.getStorageAt("B,1,2,3"));
  }
  
  /**
   * Test getStorageAt for a storage unit that does not exist (extra level).
   */
  @Test
  public void testGetStorageAt2() {
    assertNull(warehouse.getStorageAt("B,0,0,0,7"));
  }
  
  /**
   * Test getStorage At for a storage unit that does not exist (correct number of levels).
   */
  @Test
  public void testGetStorageAt3() {
    assertNull(warehouse.getStorageAt("B,0,0,7"));
  }
  
  /**
   * Test getStorage At for a storage unit that does not exist (correct number of levels).
   */
  @Test
  public void testGetStorageAt4() {
    assertNull(warehouse.getStorageAt("B,0,7,0"));
  }
  
  /**
   * Test setStates method
   */
  @Test
  public void testSetStates() {
    ArrayList<String> states = new ArrayList<>();
    states.add("A,0,0,0,1");
    states.add("B,1,0,1,16");
    warehouse.setStates(states);
    assertEquals(1, warehouse.getStorageAt("A,0,0,0").getFasciaAmt());
    assertEquals(16, warehouse.getStorageAt("B,1,0,1").getFasciaAmt());
    assertEquals(30, warehouse.getStorageAt("A,0,0,1").getFasciaAmt());
    assertEquals(30, warehouse.getStorageAt("A,0,1,1").getFasciaAmt());
  }
  
  /**
   * Test getNoneFull on a single level full warehouse.
   */
  @Test
  public void testGetNonFullNone() {
    HashMap<String, Warehouse> nonFull = simpleWarehouseFull.getNonFull();
    assertTrue(nonFull.isEmpty());
  }
  
  /**
   * Test getNonFull on a single level non-full warehouse.
   */
  @Test
  public void testGetNonFullSingle() {
    HashMap<String, Warehouse> nonFull = simpleWarehousePartial.getNonFull();
    assertEquals(1, nonFull.size());
    assertTrue(nonFull.containsKey("A Partially Full Shelf"));
    assertEquals(nonFull.get("A Partially Full Shelf"), simpleWarehousePartial);
  }
  
  /**
   * Test getNonFull on a two level non-full warehouse.
   */
  @Test
  public void testGetNonFullLevels() {
    HashMap<String, Warehouse> nonFull = subdividedWarehouse.getNonFull();
    assertEquals(2, nonFull.size());
    
//    HashMap<String, Warehouse> expected = new HashMap<>();
//    expected.put("A Rack containing Shelves,A Partially Full Shelf", simpleWarehousePartial);
//    expected.put("A Rack containing Shelves,An Empty Shelf", simpleWarehouseEmpty);
//    assertTrue(expected.equals(nonFull));
    assertTrue(nonFull.containsKey("A Rack containing Shelves,A Partially Full Shelf"));
    assertTrue(nonFull.containsKey("A Rack containing Shelves,An Empty Shelf"));
    assertTrue(nonFull.containsValue(simpleWarehousePartial));
    assertTrue(nonFull.containsValue(simpleWarehouseEmpty));
  }
  
  /**
   * Test getNonFull on a four level warehouse.
   */
  @Test
  public void testGetNonFull() {
    // Set Up
    ArrayList<String> states = new ArrayList<>();
    states.add("A,0,0,0,1");
    states.add("B,1,0,1,16");
    states.add("B,1,2,3,29");
    warehouse.setStates(states);
    
    HashMap<String, Warehouse> nonFull = warehouse.getNonFull();
    assertEquals(3, nonFull.size());
    assertTrue(nonFull.containsKey("A Warehouse,A,0,0,0"));
    assertTrue(nonFull.containsKey("A Warehouse,B,1,0,1"));
    assertTrue(nonFull.containsKey("A Warehouse,B,1,2,3"));
  }
  
//These tests were used for debugging createZones method
//to run: change access modifier for createZones to public
 
// /**
//  * Test createZones on an empty array list.
//  */
// @Test
// public void testCreateZonesEmpty() {
//   //Set Up
//   Integer[] empty = {};
//   
//   ArrayList<Warehouse> w = warehouse.createZones(empty); 
//   assertTrue(w.isEmpty());
// }
// 
// /**
//  * Test createZones on a list with one level.
//  */
// @Test
// public void testCreateZonesOneLevel() {
//   //Set Up
//   Integer[] one = {2};
//   
//   ArrayList<Warehouse> w = warehouse.createZones(one); 
//   assertEquals(2, w.size());
// }
// 
// /**
//  * Test createZones on a list with two levels.
//  */
// @Test
// public void testCreateZonesTwoLevels() {
//   //Set Up
//   Integer[] two = {2, 3};
//  
//   ArrayList<Warehouse> w = warehouse.createZones(two);
//   assertEquals(2, w.size());
//   for (Warehouse ware : w) {
//     assertEquals(3, ware.getSub().size());
//   }
// }
// 
// /**
//  * Test createZones on another list with two levels.
//  */
// @Test
// public void testCreateZonesTwoLevels1() {
//   //Set Up
//   Integer[] two = {17, 4};
//   
//   ArrayList<Warehouse> w = warehouse.createZones(two);
//   assertEquals(17, w.size());
//   for (Warehouse ware : w) {
//     assertEquals(4, ware.getSub().size());
//   }
// }
// 
// /**
//  * Test createZones on a list with three levels.
//  */
// @Test
// public void testCreateZonesThreeLevels() {
// //Set Up
//   Integer[] three = {2, 3, 4};
//   
//   ArrayList<Warehouse> w = warehouse.createZones(three);
//   assertEquals(2, w.size());
//   for (Warehouse ware : w) {
//     assertEquals(3, ware.getSub().size());
//     for (Warehouse wareSub : ware.getSub()) {
//       assertEquals(4, wareSub.getSub().size());
//     }
//   }
// }

}
