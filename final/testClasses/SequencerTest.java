/**
 * 
 */
package testClasses;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import capital.Pallet;
import employees.Sequencer;
import head.PickingRequest;


public class SequencerTest {
	private Sequencer sq1;
	private Sequencer sq2;
	private Sequencer sq3;
	private PickingRequest pr1;
	private PickingRequest pr2;
	private Pallet correctPicked = new Pallet(8);
	private Pallet incorrectPick1 = new Pallet(8);
	private Pallet corFront = new Pallet();
	private Pallet corBack = new Pallet();
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		sq1 = new Sequencer("P1");
	    sq2 = new Sequencer("P2");
	    sq3 = new Sequencer("P3");
	    
	    pr1 = new PickingRequest(1);
	    pr2 = new PickingRequest(2);
	    String[] order1 = new String[] {"blue", "XE"};
	    String[] order2 = new String[] {"green", "XE"};
	    String[] order3 = new String[] {"red", "XEX"};
	    String[] order4 = new String[] {"blue", "SE"};
	    String[] order5 = new String[] {"blue", "XE"};
	    String[] order6 = new String[] {"green", "XE"};
	    String[] order7 = new String[] {"red", "XEX"};
	    String[] order8 = new String[] {"blue", "SE"};
	    
	    pr1.addOrder(order1, "o1", "531514");
	    pr1.addOrder(order2, "o2", "54323");
	    pr1.addOrder(order3, "o3", "3157796714");
	    pr1.addOrder(order4, "o4", "7");

	    pr2.addOrder(order5, "o1", "531514");
	    pr2.addOrder(order6, "o2", "54323");
	    pr2.addOrder(order7, "o3", "3157796714");
	    pr2.addOrder(order8, "o4", "7");
	    
	    correctPicked.add("o2");
	    correctPicked.add("7");
	    correctPicked.add("o3");
	    correctPicked.add("3157796714");
	    correctPicked.add("54323");
	    correctPicked.add("531514");
	    correctPicked.add("o1");
	    correctPicked.add("o4");
	    
	    incorrectPick1.add("o2");
	    incorrectPick1.add("7");
	    incorrectPick1.add("o123");
	    incorrectPick1.add("3157796714");
	    incorrectPick1.add("23");
	    incorrectPick1.add("531514");
	    incorrectPick1.add("o1");
	    incorrectPick1.add("o4");
	    
	    corFront.add("o1");
	    corFront.add("o2");
	    corFront.add("o3");
	    corFront.add("o4");
	    
	    corBack.add("531514");
	    corBack.add("54323");
	    corBack.add("3157796714");
	    corBack.add("7");
	    
	    pr1.setAssociatedPallets(correctPicked);
	    pr2.setAssociatedPallets(incorrectPick1);
	    
	    sq1.setTask(pr1);
	    sq2.setTask(pr2);
	    sq3.setTask(null);
	}

	@Test
	public void testSetTask() {
		assertEquals(pr1, sq1.getTask());
		assertEquals(pr2, sq2.getTask());
		assertEquals(null, sq3.getTask());
		assertNotEquals(pr2, sq1.getTask());
	}
	@Test
	public void testSequencingWithCorrectlyPickedFascia() {
		PickingRequest req = sq1.sequencing();
		List<String> front = req.getPallets()[0].getAllItems();
		List<String> back =  req.getPallets()[1].getAllItems();
		assertEquals(pr1, req);
		assertTrue(corFront.getAllItems().equals(front));
		assertTrue(corBack.getAllItems().equals(back));		
	}
	
	@Test
	public void testSequencingWithIncorrectlyPickedFascia() {
		PickingRequest req = sq2.sequencing();
		assertEquals(pr2, req);
		assertEquals(null, req.getPallets()[0]);
		assertEquals(null, req.getPallets()[1]);
	}

}
