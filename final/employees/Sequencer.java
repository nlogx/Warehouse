package employees;

// import
import capital.Pallet;
import head.MainSystem;
import head.PickingRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



/**
 * Represents a worker that do sequencing.
 * Sequencer has a unique identifier (its name) and status to show its availability.
 * 
 * @author group_0393
 *
 */
public class Sequencer extends Worker {
  
  /**
   * A new Pallet that stores only all four front fascia in the picked pallet.
   */
  private Pallet front;
  /**
   * A new Pallet that stores only all four back fascia in the piced pallet.
   */
  private Pallet back;
  
  /**
   * A most recently sequenced Picked Request
   */
  private PickingRequest prevSeqReq;

  /**
   * Constructs an new worker to sequence picked pallet in the picking request.
   * 
   * @param name an unique identifier for this worker
   */
  public Sequencer(String name) {
    super(name);

  }

  // public boolean scanning() {
  // forklift = this.getTask().getPallets()[0];
  // List<String> ordered = this.getTask().getSkus();
  // List<String> picked = forklift.getAllItems();
  // for (Iterator<String> temp = picked.iterator(); temp.hasNext(); ) {
  // String str2 = temp.next();
  // MainSystem.log("Sequencer " + getName() + " scanned Fasica:");
  // for (String str1: ordered) {
  // if (str1.equals(str2)) {
  //
  // }
  // }
  // }
  //
  // return check == 0;
  // }

  /**
   * Scans the all 8 picked fascia, and then sequences. Returns an PickingRequest.
   * <p>
   * Scans the picked fascia with the assigned picked request, one at a time.
   * Then if detects wrongly picked fascia, discards all fascia. Otherwise,
   * sequence them into a front and back pallets in the corresponding order.
   * 
   * @return a PickingRequest that has been processed by the Sequencer
   */
  public PickingRequest sequencing() {
    
	Pallet forklift = this.getTask().getPallets()[0];
    List<String> ordered = this.getTask().getSkus();
    List<String> picked = forklift.getAllItems();

    List<Integer> index = new ArrayList<>(8);
    for (int i = 0; i < ordered.size(); i++) {
      index.add(i);
    }
    // Scanning the picked fascia one at a time;
    first: for (String str : picked) {
      MainSystem.log("Sequencer " + getName() + " scans a picked fascia(SKU:" + str + ")");
      for (Iterator<Integer> temp = index.iterator(); temp.hasNext();) {
        int i = temp.next();
        if (str.equals(ordered.get(i))) {
          MainSystem.log("BarcodeReader: scanned picked fascia(SKU: " + str + ") is correct");
          temp.remove();
          continue first;
        }
      }
      MainSystem.logWarning("BarcodeReader(Warning): picked fascia(SKU:" + str
          + ") has no matches in the ordered fascias of request "
          + String.valueOf(this.getTask().getId()));
      this.getTask().setAssociatedPallets(null);
      MainSystem.logWarning("Sequencer " + getName()
          + " discards the 8 picked pallets due to incorrect picked fascias");
      return this.getTask();
    }
    // Sequencing the correctly picked fascia and assigning Pallets 
    this.sortAndAssignPallets(ordered);
    prevSeqReq = this.getTask();
   
    return this.getTask();
  }
  
  private void sortAndAssignPallets(List<String> skus) {
	  front = new Pallet();
	  back = new Pallet();
	  for (int i = 0; i < 8; i += 2) {
	  	front.add(skus.get(i));
	  	System.out.println(skus.get(i));
	  	back.add(skus.get(i+1));
	  }
	  //Log
	  MainSystem.log("Sequencer " + getName() + " is heading to loading");
	  this.getTask().setAssociatedPallets(front, back);
	  this.getTask().passSequencing();
  }
  
  public void rescan() {
	  List<String> ordered = this.getTask().getSkus();
	  List<String> fskus = this.getTask().getPallets()[0].getAllItems();
	  List<String> bskus = this.getTask().getPallets()[1].getAllItems();

	  for (int i = 0; i < ordered.size(); i++) {
		  if (i < 4 && fskus.get(i).equals(ordered.get(i))) {
			  MainSystem.log("Sequencer " + getName() + " rescans a sequenced front fascia(SKU:" + fskus.get(i) + ")");
		  } else if (i >= 4 && bskus.get(i).equals(ordered.get(i))){
			  MainSystem.log("Sequencer " + getName() + " rescans a sequenced back fascia(SKU:" + bskus.get(i) + ")");
		  }
	  }
  }


 
}
