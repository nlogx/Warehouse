package inventory;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class WarehouseTest {
  
  /**
   * A default warehouse.
   * @throws Exception
   */
  private Warehouse warehouse;

  @Before
  public void setUp() throws Exception {
    warehouse = new Warehouse();
  }
  
  /**
   * Test setInitialState method
   */
  @Test
  public void testSetInitialState() {
    String state = "B,1,0,3,16";
    warehouse.setInitialState(state);
    Storage level = warehouse.getStorageAt("B,1,0,3");
    assertEquals(16, level.getFasciaAmt());
    assertEquals(30, warehouse.getStorageAt("A,0,1,1").getFasciaAmt());
    assertEquals(30, warehouse.getStorageAt("B,0,0,2").getFasciaAmt());
    assertEquals(30, warehouse.getStorageAt("B,1,1,2").getFasciaAmt());
    assertEquals(30, warehouse.getStorageAt("B,1,0,2").getFasciaAmt());
  }
  
  /**
   * Test getStorageAt()
   */
  @Test
  public void testGetStorageAt() {
    assertEquals(warehouse.getSubStorage().get(0), warehouse.getStorageAt("A"));
    Storage levelA0 = warehouse.getSubStorage().get(0).getSubStorage().get(0);
    assertEquals(levelA0, warehouse.getStorageAt("A,0"));
    Storage levelA00 = warehouse.getSubStorage().get(0).getSubStorage().get(0).getSubStorage().get(0);
    assertEquals(levelA00, warehouse.getStorageAt("A,0,0"));
    Storage levelA000 = warehouse.getSubStorage().get(0).getSubStorage().get(0).getSubStorage().get(0).getSubStorage().get(0);
    assertEquals(levelA000, warehouse.getStorageAt("A,0,0,0"));
  }
  
  @Test
  public void testGetStorageAt1() {
    assertEquals(warehouse.getSubStorage().get(1), warehouse.getStorageAt("B"));
    Storage levelB1 = warehouse.getSubStorage().get(1).getSubStorage().get(1);
    assertEquals(levelB1, warehouse.getStorageAt("B,1"));
    Storage levelB12 = warehouse.getSubStorage().get(1).getSubStorage().get(1).getSubStorage().get(2);
    assertEquals(levelB12, warehouse.getStorageAt("B,1,2"));
    Storage levelB123 = warehouse.getSubStorage().get(1).getSubStorage().get(1).getSubStorage().get(2).getSubStorage().get(3);
    assertEquals(levelB123, warehouse.getStorageAt("B,1,2,3"));
  }
  
  /**
   * Test Warehouse constructor that takes in a states ArrayList parameter.
   */
  @Test
  public void testWarehouse() {
    ArrayList<String> states = new ArrayList<>();
    states.add("A,0,0,0,1");
    states.add("B,1,0,1,16");
    warehouse = new Warehouse(states);
    assertEquals(1, warehouse.getStorageAt("A,0,0,0").getFasciaAmt());
    assertEquals(16, warehouse.getStorageAt("B,1,0,1").getFasciaAmt());
    assertEquals(30, warehouse.getStorageAt("A,0,0,1").getFasciaAmt());
    assertEquals(30, warehouse.getStorageAt("A,0,1,1").getFasciaAmt());
  }


}
