package testClasses;

import static org.junit.Assert.*;

import capital.Pallet;
import capital.Truck;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;

public class TruckTest {
  /** A standard sized truck */
  private Truck t;
  
  /** Pallets */
  private static Pallet p1;
  private static Pallet p2;
  private static Pallet p3;
  private static Pallet p4;
  private static Pallet p5;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    // Create Pallets
    p1 = new Pallet();
    p2 = new Pallet();
    p3 = new Pallet();
    p4 = new Pallet();
    p5 = new Pallet();
    
  }
  
  @Before
  public void setUp() {
    t = new Truck();
  }

  /**
   * Test loadPallet loads pallets into the correct locations.
   */
  @Test
  public void testLoadPallet() {
    t.loadPallet(p1);
    t.loadPallet(p2);
    t.loadPallet(p3);
    t.loadPallet(p4);
    t.loadPallet(p5);
    Pallet[][][] bed = t.getBed();
    
    assertEquals(bed[0][0][0], p1);
    assertEquals(bed[0][1][0], p2);
    assertEquals(bed[1][0][0], p3);
    assertEquals(bed[1][1][0], p4);
    assertEquals(bed[0][0][1], p5);
  }
  
  /**
   * Test that a truck cannot be filled past its maximum capacity.
   */
  @Test
  public void testLoadPalletMax() {
    for (int i = 0; i < 40; i++) {
      t.loadPallet(p2);
    }
    t.loadPallet(p5);
    assertEquals(40, t.getCurrent());
  }
  
  /**
   * Test isFull on an empty truck
   */
  @Test
  public void testIsFullEmpty() {
    assertFalse(t.isFull());
  }
  
  /**
   * Test isFull on a partially filled truck.
   */
  @Test
  public void testIsFullPartial() {
    t.loadPallet(p1);
    assertFalse(t.isFull());
  }
  /**
   * Test isFull on a full truck.
   */
  @Test
  public void testisFull() {
    for (int i = 0; i < 40; i++) {
      t.loadPallet(p1);
    }
    assertTrue(t.isFull());
  }

}
