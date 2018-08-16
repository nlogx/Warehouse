package capital;

import java.util.ArrayList;
import java.util.List;


/**
 * A object that represents a pallet of desired size. A pallet stores SKUs of fascia that are picked
 * or sequenced.
 * 
 * @author group_0393
 *
 */
public class Pallet {
  /**
   * A list of string that stores SKUs of fascia.
   */
  private List<String> pallet;

  /**
   * Overload the Pallet(int) to construct a pallet of size 4.
   */
  public Pallet() {
    this(4);
  }

  /**
   * Construct a pallet of input size
   * 
   * @param size an int that defines the size of the Pallet.
   */
  public Pallet(int size) {
    pallet = new ArrayList<String>(size);
  }

  /**
   * Add a SKU to the pallet.
   * 
   * @param sku a SKU for fascia
   */
  public void add(String sku) {
    pallet.add(sku);
  }
  
  /**
   * get all the SKUs in the pallet
   * 
   * @return an List of Strings and each String is a SKU.
   */
  public List<String> getAllItems() {
    return pallet;
  }

  /**
   * Get the size of a predefined pallet.
   * 
   * @return an int to show the size of the pallet
   */
  public int getSize() {
    return pallet.size();
  }

  public boolean isEmpty() {
    return pallet.isEmpty();
  }

  /**
   * Empty a Pallet.
   */
  public void clearPallet() {
    pallet.clear();
  }

//  /**
//   * Gives an array of SKUs which are stores in the pallet.
//   * 
//   * @return an String array of SKUs
//   */
//  public String[] toStringArr() {
//    return pallet.toArray(new String[0]);
//  }

//  /**
//   * Gives a string representation of pallet.
//   * 
//   * @return a string of all SKUs separated by a comma in between
//   */
//  @Override
//  public String toString() {
//    return String.join(",", pallet);
//  }
//
//  /**
//   * To see whether or not an object is pallet of the same size.
//   */
//  @Override
//  public boolean equals(Object obj) {
//    if (obj instanceof Pallet) {
//      Pallet temp = (Pallet) obj;
//      if (temp.getSize() == getSize()) {
//        return true;
//      }
//    }
//    return false;
//  }
}
