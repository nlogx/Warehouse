package employees;

import java.util.PriorityQueue;

import capital.Pallet;
import capital.Truck;
import head.MainSystem;
import head.PickingRequest;

public class Loader extends Worker {

  public Loader(String name) {
    super(name);
  }

  /**
   * Loads sequenced pallet to truck.
   * @param pallet
   * @param truck
   */
  public void load(Pallet pallet, Truck truck) {
    truck.loadPallet(pallet);
  }
  
  /**
   * Rescans the fascia on the pallets to match
   * picking requests.
   * 
   * @param requests The picking requests.
   * @return  True iff the fascia on the pallets match the picking request orders.
   */
  public boolean rescan(PriorityQueue<PickingRequest> requests) {
    for (PickingRequest req : requests) { // for each picking request to load
      Pallet frontPallet = req.getPallets()[0];
      Pallet backPallet = req.getPallets()[1];
      for (int i = 0; i < req.getSkus().size(); i++) { // for each SKU
        if (i % 2 == 0) { // is a front fascia  
          if (!frontPallet.getAllItems().get(i / 2).equals(req.getSkus().get(i))) {
            MainSystem.logWarning("Fascia on front pallet at position " + (i / 2) + " incorrect. "
                + "Expected SKU: " + req.getSkus().get(i) + " Got: " + frontPallet.getAllItems().get(i / 2));
            return false;
          }
        } else {  // is a back fascia
          if (!backPallet.getAllItems().get(i / 2).equals(req.getSkus().get(i))) {
            MainSystem.logWarning("Fascia on back pallet at position " + (i / 2) + " incorrect. "
                + "Expected SKU: " + req.getSkus().get(i) + " Got: " + frontPallet.getAllItems().get(i / 2));
            return false;
          }
        }
      }
    }
    return true;
  }

}
