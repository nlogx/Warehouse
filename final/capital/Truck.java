package capital;

/**
 * Represents a truck that holds pallets.
 * 
 * @author group_0393
 *
 */
public class Truck {
  // Fields
  /** Cargo bed to hold pallets. */
  private Pallet[][][] bed;

  /** The truck dimensions. */
  private int width;
  private int length;
  private int height;
  
  /** The indices of the location as (wx, ly, hz) -> (width, length, height). */
  private int wx;
  private int ly;
  private int hz;

  /** Determines the coordinates of the next loading location within the truck. */
  private int num;

  /** The maximum number of pallets this truck can hold. */
  private final int capacity;

  /** The current number of pallets on this truck. */
  private int current;

  // Constructors
  /**
   * Construct a default truck that holds pallets stacked2x2x10.
   */
  public Truck() {
    this(2, 2, 10);
  }

  /**
   * Construct a truck that holds pallets stacked width x length x height.
   * 
   * @param width the width of the truck
   * @param length the length of the truck
   * @param height the height of the truck
   */
  public Truck(int width, int length, int height) {
    bed = new Pallet[width][length][height];
    this.width = width;
    this.length = length;
    this.height = height;
    this.capacity = width * length * height;
    this.current = 0;
    wx = 0;
    ly = 0;
    hz = 0;
    num = 1;
  }

  /**
   * Load a pallet onto the truck iff there is room. Pallets are loaded by column, left to right,
   * bottom to top.
   * 
   * @param pallet The pallet to load
   */
  public void loadPallet(Pallet pallet) {
    if (!isFull()) {
      bed[wx][ly][hz] = pallet;
      current++;
      nextLocation();
    }
  }

  /**
   * @return true iff the truck is fully loaded.
   */
  public boolean isFull() {
    return (current == capacity);
  }

  /**
   * Change the loading coordinates to the next location.
   */
  private void nextLocation() {
    if ((num % 2) == 0 && (wx == width - 1)) {
      wx = 0;
    } else if ((num % 2) == 0) {
      wx++;
    }
    if (ly == length - 1) {
      ly = 0;
    } else {
      ly++;
    }
    if ((num % 4) == 0 && (hz == height - 1)) {
      hz = 0;
    } else if ((num % 4) == 0) {
      hz++;
    }
    num++;
  }
  
  public Pallet[][][] getBed() {
    return bed;
  }

  public int getCurrent() {
    return current;
  }

}
