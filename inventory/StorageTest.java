package inventory;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class StorageTest {
  /** A full Storage object. */
  private Storage simpleStorageFull;
  /** A partially full Storage object. */
  private Storage simpleStoragePartial;
  /** An empty Storage object. */
  private Storage simpleStorageEmpty;
  /** An ArrayList of Storage objects. */
  private ArrayList<Storage> subStorage;
  /** A Storage object containing Storage objects. */
  private Storage subdividedStorage;
  
  /**
   * Sets up test fixture.  Runs before every test.
   */
  @Before
  public void setUp() {
    simpleStorageFull = new Storage("A Full Shelf");
    simpleStoragePartial = new Storage("A Partially Full Shelf", 20);
    simpleStorageEmpty = new Storage("An Empty Shelf", 0);
    subStorage = new ArrayList<Storage>();
    subStorage.add(simpleStorageFull);
    subStorage.add(simpleStoragePartial);
    subStorage.add(simpleStorageEmpty);
    subdividedStorage = new Storage("A Rack containing Shelves", subStorage);
  }
  
  /**
   * Test MAXCAPACITY is set at 30 for all four Storage units.
   */
  @Test
  public void testGetMaxCapacity() {
    assertEquals(30, simpleStorageFull.getMaxCapacity());
    assertEquals(30, simpleStoragePartial.getMaxCapacity());
    assertEquals(30, simpleStorageEmpty.getMaxCapacity());
    assertEquals(30, subdividedStorage.getMaxCapacity());
  }
  
  /**
   * Test name is set for all four Storage units.
   */
  @Test
  public void testGetName() {
    assertEquals("A Full Shelf", simpleStorageFull.getName());
    assertEquals("A Partially Full Shelf", simpleStoragePartial.getName());
    assertEquals("An Empty Shelf", simpleStorageEmpty.getName());
    assertEquals("A Rack containing Shelves", subdividedStorage.getName());
  }
  
  /**
   * Test FasciaAmt is set properly.
   */
  @Test
  public void testGetFasciaAmt() {
    assertEquals(30, simpleStorageFull.getFasciaAmt());
    assertEquals(20, simpleStoragePartial.getFasciaAmt());
    assertEquals(0, simpleStorageEmpty.getFasciaAmt());
    assertEquals(0, subdividedStorage.getFasciaAmt());
  }
  
  /**
   * Test setFasciaAmt method, setting fascia amount
   * to a number between 0 and maximum (30) inclusive.
   */
  @Test
  public void testSetFasciaAmt() {
    simpleStorageFull.setFasciaAmt(25);
    assertEquals(25, simpleStorageFull.getFasciaAmt());
  }
  
  /**
   * Test setFasciaAmt to a number above the maximum.
   */
  @Test
  public void testSetFasciaAmtOver() {
    simpleStorageFull.setFasciaAmt(35);
    assertEquals(30, simpleStorageFull.getFasciaAmt());
  }
  
  /**
   * Test setFasciaAmt to a number below zero.
   */
  @Test
  public void testSetFasicaAmtUnder() {
    simpleStorageFull.setFasciaAmt(-1);
    assertEquals(30, simpleStorageFull.getFasciaAmt());
  }
  
  /**
   * Test setFasciaAmt to zero.
   */
  @Test
  public void testSetFasciaAmtZero() {
    simpleStorageFull.setFasciaAmt(0);
    assertEquals(0, simpleStorageFull.getFasciaAmt());
  }
  
  /**
   * Test decrementFascia method
   */
  @Test
  public void testDecrementFascia() {
    simpleStorageFull.decrementFascia();
    assertEquals(29, simpleStorageFull.getFasciaAmt());
  }
  
  /**
   * Test subStorage is set properly.
   */
  @Test
  public void testGetSubsStorage() {
    assertEquals(0, simpleStorageFull.getSubStorage().size());
    assertEquals(0, simpleStoragePartial.getSubStorage().size());
    assertEquals(0, simpleStorageEmpty.getSubStorage().size());
    assertEquals(3, subdividedStorage.getSubStorage().size());
  }
  
  /**
   * Test addStorage()
   */
  @Test
  public void testAddStorage() {
    Storage simpleStorage2 = new Storage("Another partially full shelf", 10);
    Storage simpleStorage3 = new Storage("Another full shelf");
    ArrayList<Storage> additionalStorage = new ArrayList<>();
    additionalStorage.add(simpleStorage2);
    additionalStorage.add(simpleStorage3);
    subdividedStorage.addStorage(additionalStorage);
    assertEquals(5, subdividedStorage.getSubStorage().size());
  }

}
