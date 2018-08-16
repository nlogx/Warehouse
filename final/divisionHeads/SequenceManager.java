package divisionHeads;

import employees.Picker;
import employees.Sequencer;

import head.MainSystem;
import head.PickingRequest;
import head.PickingRequestComparator;

import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;


/**
 * A manager that manages the sequence division.
 * A Manager hires, assigns task to, and keep track of worker that sequences items
 * @author group_0393
 *
 */
public class SequenceManager {
	
	/**
	 * A Map that help SequenceManager to identify and keep track of all Sequencer Objects
	 * based on their unique String identifiers 
	 */
	private Map<String, Sequencer> sequencers;
	
	/**
	 * A PriorityQueues that stores the PickingRequest that needs to be sequenced.
	 */
	private PriorityQueue<PickingRequest> marshalingArea;
	
	/**
	 * A List of available workers
	 */
	private List<Sequencer> available;
	
	/**
	 * Constructs an new SequenceManager and instantiate map for sequencers
	 * and PriorityQueue that will put PickingRequest with the lowest id to
	 * the head of the queue.
	 */
	public SequenceManager() {
		Comparator<PickingRequest> comparator = new PickingRequestComparator();
		this.sequencers = new HashMap<>();
		this.marshalingArea = new PriorityQueue<>(comparator);
		this.available = new ArrayList<>();
	}
	
	/**
	 * Add a PickingRequest that needs to be sequenced
	 * @param pr a PickingRequest that is picked and waits to be sequenced.
	 */
	public void addRequest(PickingRequest req) {
		this.marshalingArea.add(req);
	}
	
	/**
	 * Creates new Sequencer with given identifier
	 * @param name an unique id for the sequencer
	 */
	public void hire(String name) {
		Sequencer sequencer = this.sequencers.get(name);
		if (sequencer == null) {
			sequencer = new Sequencer(name);
			this.sequencers.put(name, sequencer);
			// Log
			MainSystem.log("Sequencer " + name + " is ready");
		} else {
			if (sequencer.getTask() == null) {
				sequencer.setReady();
				MainSystem.log("Sequencer " + name + " is ready to receive a picked request");
			} else {
				MainSystem.logWarning("Sequencer " + name + " is not ready");
				MainSystem.logWarning("Sequencer " + name + " is currently assigned a picked request " 
				+ String.valueOf(sequencer.getTask().getId()));
			}
		}
	}

	
	/**
	 * Tells a hired Sequencer to process the picking request
	 * that waits to be sequenced.
	 * @param name a unique identifier of hired sequencer.
	 * @return an PickingRequest that is processed by assigned Sequencer
	 */
	public PickingRequest processTask(String name) {
		PickingRequest processedReq = marshalingArea.poll();
		Sequencer sequencer = this.sequencers.get(name);
		if (sequencer != null) {
			if (sequencer.isReady()) {
				sequencer.setTask(processedReq);
				// Log
				MainSystem.log("Sequencer " + name + " sequences picked request " + String.valueOf(sequencer.getTask().getId()));
				processedReq = sequencer.sequencing();
				sequencer.setTask(null);
				return processedReq;
			} else {
				//Log
				MainSystem.logWarning("Sequencer " + name + " is not ready");
			}
			
		} else {
			//Log
			MainSystem.logWarning("Sequencer " + name + " is not registered in the system");
		}
		return processedReq;
	}
	
	public void rescan(String name) {
		Sequencer sq = sequencers.get(name);
		if (sq != null) {
			MainSystem.log("Sequencer " + name + "rescans most recent sequenced request");
			sq.rescan();
		} else {
			MainSystem.logWarning("Sequencer " + name + " is not in the system. Rescan cannot be performed");
		}
		
	}
	
}
