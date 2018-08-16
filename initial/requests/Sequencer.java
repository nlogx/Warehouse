package requests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;


public class Sequencer {
	private ArrayList<Integer> frontPallet = new ArrayList<Integer>(4);
	private ArrayList<Integer> backPallet = new ArrayList<Integer>(4);
	private ArrayList<String> log;
	private static Comparator<PickingRequest> comparator = new PickingRequestComparator();
	private static PriorityQueue<PickingRequest> sequenceLine = new PriorityQueue<PickingRequest>(comparator);
	private static int pickingId = 1;
	private PriorityQueue<PickingRequest> sequencedRequests = new PriorityQueue<PickingRequest>(comparator);
	private String name;
	private boolean status;
	private static Map<String, Sequencer> sequencers;
	
	public Sequencer() {
		this("John Doe");
	}
	
	public Sequencer(String name) {
		this.name = name;
		this.status = true;
		log = new ArrayList<String>();
		sequencers.put(name, this);
	}
	
	public void sequencing() {
		if (pickingId == sequenceLine.peek().getId()) {
			PickingRequest currReq = sequenceLine.poll();
			ArrayList<String> skus = currReq.getSkus();
			for (int i = 0; i < 8; i++) {
				frontPallet.add(Integer.valueOf(skus.get(i)));
				backPallet.add(Integer.valueOf(skus.get(i+1)));
			}
			String[] arrTemp = currReq.getPallet().split(",");
			ArrayList<String> listTemp = new ArrayList<String>(Arrays.asList(arrTemp));
			boolean check = this.inspection(skus, listTemp);
			if (check) {
				log.set(0, "PickedFascia is correct");
				this.scanning(frontPallet, "front fascia");
				this.scanning(backPallet, "back fascia");
				sequencedRequests.add(currReq);
				pickingId++;
			} else {
				log.set(0,  "PickedFascia is incorrect");
				this.discardPallets();
				//TODO
				// Add the currReq back to the nextRequests in OrderManager.
				
			}
		} else {
			//TODO
			// Assuming that the sequencer sequences request in a chronological order
			System.out.println("Waiting for the earliest unsequenced request");
		}
	}
	
	
	public void scanning(ArrayList<Integer> arrList, String type) {
		String msg;
		for(int i = 0; i < 4; i++) {
			msg = "The " + String.valueOf(i) + "-th " + type + "SKU:"
					+ String.valueOf(arrList.get(i));
			log.add(msg);
		}
	}
	public boolean inspection(ArrayList<String> requested, ArrayList<String> picked ) {
		if (requested.size() != picked.size()) {
			return false;
		} else {
			return requested.containsAll(picked) && picked.containsAll(requested);
		}
	}
	public boolean getStatus() {
		return status;
	}
	
	public void discardPallets() {
		frontPallet.clear();
		backPallet.clear();
		frontPallet.ensureCapacity(4);
		backPallet.ensureCapacity(4);
	}
	
	public static void addPickedRequest(PickingRequest pr) {
		Sequencer.sequenceLine.add(pr);
	}
	
	public ArrayList<Integer> getFrontPallet() {
		return frontPallet;
	}
	
	public ArrayList<Integer> getBackPallet() {
		return backPallet;
	}
	
	public ArrayList<String> getLog() {
		return log;
	}
	
	public PriorityQueue<PickingRequest> getSequencedReq() {
		return sequencedRequests;
	}
	
	public String getName() {
		return name;
	}
}
