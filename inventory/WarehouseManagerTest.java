package inventory;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class WarehouseManagerTest {
  /** A WarehouseManager.*/
  private WarehouseManager warehouseManager;
  
  /** Initial warehouse states */
  private ArrayList<String> states = new ArrayList<>();
  
  /** A worker. */
  private String replenisher = "Ready Freddy";

  @Before
  public void setUp() throws Exception {
    states.add("A,0,0,0,1");
    states.add("B,1,0,1,16");
    warehouseManager = new WarehouseManager(states);
    warehouseManager.addReplenisher(replenisher);
  }

  /**
   * Test writeFinalState
   */
  @Test
  public void testWriteFinalState() {
    warehouseManager.writeFinalState();
  }
  
  /**
   * Test pickOne method returns the correct string
   * and decrements the fascia amount.
   */
  @Test
  public void testPickOne() {
    String message = warehouseManager.pickOne("A,0,2,1");
    Storage s = warehouseManager.getWarehouse().getStorageAt("A,0,2,1");
    int sAmt = s.getFasciaAmt();
    assertEquals("1 fascia removed from A,0,2,1\n", message);
    assertEquals(29, sAmt);
  }
  
  /**
   * Test requestRestock returns the correct string
   */
  @Test
  public void testRequestRestock() {
    String message = warehouseManager.requestRestock("A,0,2,1");
    assertEquals("Requesting fascia restock at A,0,2,1\n",message);
  }
  
  /**
   * Test isLow
   */
  @Test
  public void testIsLow() {
    Warehouse w = warehouseManager.getWarehouse();
    w.getStorageAt("B,0,1,0").setFasciaAmt(5);
    w.getStorageAt("B,0,1,1").setFasciaAmt(3);
    w.getStorageAt("B,0,1,2").setFasciaAmt(10);
    assertTrue(warehouseManager.isLow("B,0,1,0"));
    assertTrue(warehouseManager.isLow("B,0,1,1"));
    assertFalse(warehouseManager.isLow("B,0,1,2"));
  }
  
  /**
   * Test restock with an available replenisher
   */
  @Test
  public void testRestock() {
    Warehouse w = warehouseManager.getWarehouse();
    Storage s = w.getStorageAt("A,1,1,1");
    s.setFasciaAmt(5);
    
    String message = warehouseManager.restock("Ready Freddy", "A,1,1,1");
    assertEquals(30, s.getFasciaAmt());
    assertEquals("Ready Freddy moved 25 fascia from replenishing room to A,1,1,1\nFascia at A,1,1,1 have been restocked.\n", message);
    
  }
  
  /**
   * Test restock on a replenisher that is already working.
   */
  @Test
  public void testRestockCannot() {
    // Set Up
    Warehouse w = warehouseManager.getWarehouse();
    Storage s = w.getStorageAt("A,1,1,1");
    warehouseManager.restock("Ready Freddy", "A,1,1,1");  // only available replenisher removed
    
    s.setFasciaAmt(5);
    String message2 = warehouseManager.restock("Ready Freddy", "A,1,1,1");
    assertEquals(5, s.getFasciaAmt());
    assertEquals("Ready Freddy unavailable to restock fascia at A,1,1,1\n", message2);
  }
  
  /**
   * Test restock with a replenisher that does not exist"
   */
  @Test
  public void testRestockNone() {
    //Set Up
    Warehouse w = warehouseManager.getWarehouse();
    Storage s = w.getStorageAt("A,1,1,1");
    s.setFasciaAmt(5);

    String m = warehouseManager.restock("Sauron", "A,1,1,1");
    assertEquals( 5, s.getFasciaAmt());
    assertEquals("Sauron unavailable to restock fascia at A,1,1,1\n", m);
  }
  
  /**
   * Test pick
   */
  @Test
  public void testPick() {
    String[] messages = warehouseManager.pick("A,1,1,1");
    assertEquals("1 fascia removed from A,1,1,1\n", messages[0]);
    assertNull(messages[1]);
  }
  
  /**
   * Test pick on fascia that are running low and 
   * need to be restocked afterwards.
   */
  @Test
  public void testPick1() {
    Warehouse w = warehouseManager.getWarehouse();
    Storage s = w.getStorageAt("A,1,1,1");
    s.setFasciaAmt(6);
    String[] messages2 = warehouseManager.pick("A,1,1,1");
    assertEquals("1 fascia removed from A,1,1,1\n", messages2[0]);
    assertEquals("Requesting fascia restock at A,1,1,1\n", messages2[1]);
  }

}
