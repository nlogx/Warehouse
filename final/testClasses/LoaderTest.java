package testClasses;

import static org.junit.Assert.*;

import java.util.PriorityQueue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import capital.Pallet;
import capital.Truck;
import employees.Loader;
import head.PickingRequest;
import head.PickingRequestComparator;


public class LoaderTest {

	private Loader l1;
	private Loader l2;
	private Truck truck;
	private static Pallet p1;
	private static Pallet p2;
	private static Pallet p3;
	private static PickingRequest goodReq;
	private static PickingRequest badReq;
	private static PickingRequest badReq2;
	private PriorityQueue<PickingRequest> reqsG;
	private PriorityQueue<PickingRequest> reqsB;
	private PriorityQueue<PickingRequest> reqsM;
	private PickingRequestComparator c;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
	  p1 = new Pallet(2);
	  p2 = new Pallet(2);
	  p3 = new Pallet(2);
	  p1.add("f1");
	  p1.add("f2");
	  p2.add("b1");
	  p2.add("b2");
	  p3.add("front1");
	  p3.add("front2");



	  String[] order = new String[2];


	  goodReq = new PickingRequest(1);
	  goodReq.setAssociatedPallets(p1, p2);

	  badReq = new PickingRequest(2);
	  badReq.addOrder(order, "f1", "fff");
	  badReq.setAssociatedPallets(p1, p3);

	  badReq2 = new PickingRequest(3);
	  badReq2.addOrder(order, "fff", "f1");
	  badReq2.setAssociatedPallets(p3);
  }

  @Before
  public void setUp() throws Exception {
	  l1 = new Loader("l1");
	  l2 = new Loader("l2");
	  truck = new Truck(1,2,1);

	  l1.load(p1, truck);
	  l2.load(p2, truck);
	  l1.load(p3, truck);

	  c = new PickingRequestComparator();
	  reqsG = new PriorityQueue<PickingRequest>(c);
	  reqsB = new PriorityQueue<PickingRequest>(c);
	  reqsM = new PriorityQueue<PickingRequest>(c);

    reqsM.add(goodReq);
	  reqsM.add(badReq);
	  reqsG.add(goodReq);
	  reqsB.add(badReq2);


  }


  @Test
  public void testLoad() {
	  assertEquals(2, truck.getCurrent());
	  assertTrue(truck.isFull());
  }

  @Test
  public void testRescan() {
	  assertTrue(l1.rescan(reqsG));
	  assertFalse(l2.rescan(reqsB));
	  assertFalse(l1.rescan(reqsM));

  }
}
