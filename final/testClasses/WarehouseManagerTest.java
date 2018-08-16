package testClasses;

import static org.junit.Assert.*;

import divisionHeads.WarehouseManager;

import java.io.IOException;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

import capital.Warehouse;



public class WarehouseManagerTest {
  
  public static void setUpBeforeClass() throws Exception {}

  /** A WarehouseManager.*/
  private WarehouseManager warehouseManager;
  /** The warehouse managed by the warehouseManager. */
  private Warehouse w; 
  /** Initial warehouse format. */
  ArrayList<String> format; 
  /** Initial warehouse states. */
  private ArrayList<String> states;
  /** A worker. */
  private String replenisher = "Frodo";

  @Before
  public void setUp() throws Exception {
    states = new ArrayList<>();
    states.add("A,0,2,3,6");
    states.add("A,0,0,0,1");
    states.add("B,1,2,3,5");
    format = new ArrayList<>();
    format.add("2,2,3,4");
    warehouseManager = new WarehouseManager(format, states);
    warehouseManager.hire(replenisher);
    w = warehouseManager.getWarehouse();
  }

  /**
   * Check writeFinalState creates and writes to a csv file.
   * @throws IOException Possibly when creating and closing new FileWriter
   */
  @Test
  public void testWriteFinalState() throws IOException {
    warehouseManager.writeFinalState();
  }
    
  /**
   * Test restock with an available replenisher
   * with fascia amount at minimum (5)
   */
  @Test
  public void testRestock() {
    warehouseManager.restock("Frodo", "B,1,2,3");
    assertEquals(30, w.getStorageAt("B,1,2,3").getFasciaAmt());  
  }
  
  /**
   * Test restock with an available replenisher
   * with fascia amount below minimum.
   */
  @Test
  public void testRestock1() {
    warehouseManager.restock("Frodo", "A,0,0,0");
    assertEquals(30, w.getStorageAt("A,0,0,0").getFasciaAmt());
  }
  
  /**
   * Test restock with full fascia stock.
   */
  @Test
  public void testRestock2() {
    warehouseManager.restock("Frodo", "B,0,1,3");
    assertEquals(30, w.getStorageAt("B,0,1,3").getFasciaAmt());
  }
  
  /**
   * Test restock on a replenisher that is already working.
   */
  @Test
  public void testRestockCannot() {
    warehouseManager.restock("Frodo", "B,1,2,3");  // only available replenisher set to busy
    // Call restock on busy replenisher
    warehouseManager.restock("Frodo", "A,0,0,0");
    assertEquals(1, w.getStorageAt("A,0,0,0").getFasciaAmt());
  }
  
  /**
   * Test restock with a replenisher that does not exist"
   */
  @Test
  public void testRestockNone() {
    warehouseManager.restock("Sauron", "B,1,2,3");
    assertEquals(5, w.getStorageAt("B,1,2,3").getFasciaAmt());
  }
  
  /**
   * Test pick on full shelf of fascia
   */
  @Test
  public void testPick() {
    warehouseManager.pick("A,1,1,1");
    assertEquals(29, w.getStorageAt("A,1,1,1").getFasciaAmt());
  }
  
  /**
   * Test pick on shelf of fascia will become the minimum allowed.
   */
  @Test
  public void testPick1() {
    warehouseManager.pick("A,0,2,3");
    assertEquals(5, w.getStorageAt("A,0,2,3").getFasciaAmt());
  }
}
