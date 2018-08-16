package requests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A table to store all the SKUs of fascia's. This Class TranslationTable stores every SKU
 * number and allows other objects and instances to get those Sku's from a model and color
 * of a car. 
 *  
 * @author AlexChum
 *
 */
class TranslationTable {
  
  /**
   * A nested class that holds the value of a frontSku and backSku as Integers.
   * 
   * @author AlexChum
   *
   */
  public class Node {
    
    public String frontSku;
    public String backSku;
    
    public Node(String front, String back) {
      this.frontSku = front;
      this.backSku = back;
    }
  }
  
  private Map<String, Map<String, Node>> skuTable;
  
  /**
   * Class Constructor. This creates a map from model and colors to SKU numbers from an
   * ArrayList containing the information.
   * 
   * @param skus the Array containing information of the SKU numbers
   */
  TranslationTable(ArrayList<String> skus) {
    this.skuTable = new HashMap<String, Map<String, Node>>();
    for (String sku : skus) {
      this.addFascia(sku);
    }
  }
  
  /**
   * Returns the front and back fascia SKU numbers given a model and color.
   * 
   * @param model the model of the car
   * @param colour the color of the car
   * @return an Array of Integer corresponding to the SKU numbers
   */
  String[] getSkus(String model, String colour) {
    Node node = skuTable.get(model).get(colour);
    String[] array = new String[2];
    array[0] = node.frontSku;
    array[1] = node.backSku;
    return array;
  }
  
  /**
   * Adds a fascia SKU number to the TranslationTable.
   * 
   * @param sku a string containing the model,color, and SKU numbers
   */
  private void addFascia(String sku) {
    String[] parts = sku.split(",");
    Node node = new Node(parts[2], parts[3]);
    if (skuTable.containsKey(parts[0])) {
      skuTable.get(parts[0]).put(parts[1], node);
    } else {
      HashMap<String, Node> map = new HashMap<String, Node>();
      map.put(parts[1], node);
      skuTable.put(parts[0], map);
    }
  }

}
