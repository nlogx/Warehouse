/**
 * 
 */
package requests;

import static org.junit.Assert.*;


import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import loading.LoadingManager;

/**
 * @author Ming Yuan
 *
 */
public class OrderManagerTest {

	private OrderManager om;
	private ArrayList<String> data;
	private ArrayList<String[]> orders;
	private String name;
	private LoadingManager lm;
	private Picker picker;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() {
		data = new ArrayList<String>();
		data.add("SES,Blue,1,2");
		data.add("SES,Red,3,4");
		data.add("SE,White,5,6");
		data.add("SES,Green,7,8");
		this.lm = new LoadingManager();
		this.om = new OrderManager(data, lm);
		String[] tem1 = {"SES", "Red"};
		String[] tem2 = {"SES", "Blue"};
		String[] tem3 = {"SE", "White"};
		String[] tem4 = {"SES", "Green"};
		orders = new ArrayList<String[]>();
		orders.add(tem1);
		orders.add(tem2);
		orders.add(tem3);
		orders.add(tem4);
		name = "David";
	}
	
	/**
	 * Test method for {@link requests.OrderManager#createRequests(java.util.ArrayList)}.
	 */
	@Test
	public void testCreateRequests() {
		String x = "Picking request 1 has been issued.";
		assertEquals(x, om.createRequests(orders));
	}

	/**
	 * Test method for {@link requests.OrderManager#processRequest(java.lang.String)}.
	 */
	@Test
	public void testProcessRequest() {
		String x = om.createRequests(orders);
		String[] temp = {name + " is now assigned request: 1", null, null};
		assertEquals(temp[0], om.processRequest(name)[0]);
	}

	/**
	 * Test method for {@link requests.OrderManager#marshaling(java.lang.String)}.
	 */
	@Test
	public void testMarshalling() {
		String x = "Picker " + name + " never handled a picking request.";
		String z = om.createRequests(orders);
		assertEquals(x, om.marshalling(name));
		for (int i=0; i < 9; i++) {
			String[] w = om.processRequest(name);
		}
		String y = om.marshalling(name);
		x = "Picker " + name + " went to marshalling.";
		assertEquals(x, y);
		
		String a = om.createRequests(orders);
		String[] b = om.processRequest(name);
		String c = name + " is now assigned request: " + 2;
		assertEquals(c, b[0]);
	}

	/**
	 * Test method for {@link requests.OrderManager#sequencing()}.
	 */
	@Test
	public void testSequencing() {
		String z = om.createRequests(orders);
		for (int i=0; i < 9; i++) {
			String[] w = om.processRequest(name);
		}
		String y = om.marshalling(name);
		ArrayList<String> temp = new ArrayList<String>(8);
		temp.add("PickedFascia is correct");
		temp.add("The 1-th front SKU:3");
		temp.add("The 2-th front SKU:1");
		temp.add("The 3-th front SKU:5");
		temp.add("The 4-th front SKU:7");
		temp.add("The 1-th rear SKU:4");
		temp.add("The 2-th rear SKU:2");
		temp.add("The 3-th rear SKU:6");
		temp.add("The 4-th rear SKU:8");
		assertEquals(temp, om.sequencing());
	}

}
