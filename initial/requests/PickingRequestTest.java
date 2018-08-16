package requests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PickingRequestTest {
  PickingRequest req1;
  PickingRequest req2;
  String[] order1;
  String[] order2;
  String[] order3;
  String[] order4;
  String[] order5;
  String[] order6;
  String[] order7;
  String[] order8;
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {}

  @AfterClass
  public static void tearDownAfterClass() throws Exception {}

  @Before
  public void setUp() throws Exception {
    this.req1 = new PickingRequest(1);
    this.req2 = new PickingRequest(2);
    this.order1 = new String[2];
    order1[0] = "SES";
    order1[1] = "Grey";
    this.order2 = new String[2];
    order2[0] = "SE";
    order2[1] = "Grey";
    this.order3 = new String[2];
    order3[0] = "SES";
    order3[1] = "Grey";
    this.order4 = new String[2];
    order4[0] = "XE";
    order4[1] = "Blue";
    this.order5 = new String[2];
    order5[0] = "XE";
    order5[1] = "Red";
    this.order6 = new String[2];
    order6[0] = "F";
    order6[1] = "Green";
    this.order7 = new String[2];
    order7[0] = "BO";
    order7[1] = "Magenta";
    this.order8 = new String[2];
    order8[0] = "P";
    order8[1] = "Orange";
  }

  @After
  public void tearDown() throws Exception {}
  
  @Test
  public void id() {
    assertEquals(req1.getId(), 1);
    assertEquals(req2.getId(), 2);
  }
  
  @Test
  public void orders() {
    this.order1 = new String[2];
    order1[0] = "SES";
    order1[1] = "Grey";
    req1.addOrder(order1, "2134", "43242");
    req1.addOrder(order2, "980", "4321");
    req1.addOrder(order3, "87897", "423674");
    req1.addOrder(order4, "43212", "3426415");
    ArrayList<String[]> test1 = new ArrayList<String[]>();
    test1.add(order1);
    test1.add(order2);
    test1.add(order3);
    test1.add(order4);
    assertEquals(req1.getOrders(),test1);
    test1.set(3, order5);
    assertNotEquals(req1.getOrders(),test1);
  }
  
  @Test
  public void equal() {
    PickingRequest req3 = new PickingRequest(1);
    assertTrue(req1.equals(req3));
    assertFalse(req3.equals(req2));
  }
  
  @Test
  public void notequal() {
    req1.addOrder(order1, "2134", "43242");
    req1.addOrder(order2, "980", "4321");
    req1.addOrder(order3, "87897", "423674");
    req1.addOrder(order4, "43212", "3426415");
    
    req2.addOrder(order1, "2134", "43242");
    req2.addOrder(order2, "980", "4321");
    req2.addOrder(order3, "87897", "423674");
    req2.addOrder(order5, "43212", "3426415");
    assertFalse(req1.equals(req2));
  }
  
  @Test
  public void locations() {
    List<String> lst = new ArrayList<String>();
    lst.add("A,2,3,4");
    lst.add("A,3,2,4");
    lst.add("A,5,3,9");
    lst.add("A,6,2,4");
    lst.add("A,7,3,0");
    req1.addLocations(lst);
    assertEquals(req1.getLocation(), "A,2,3,4");
    assertEquals(req1.getLocation(), "A,3,2,4");
    assertEquals(req1.getLocation(), "A,5,3,9");
    assertEquals(req1.getLocation(), "A,6,2,4");
    assertEquals(req1.getLocation(), "A,7,3,0");
  }
  
  @Test
  public void pallet() {
    req1.addPallet("2131231,31231231,3213545,4325,342423");
    assertEquals(req1.getPallet(), "2131231,31231231,3213545,4325,342423");
  }
  
  @Test
  public void SKUS() {
    req1.addOrder(order1, "2134", "43242");
    req1.addOrder(order2, "980", "4321");
    req1.addOrder(order3, "87897", "423674");
    req1.addOrder(order4, "43212", "3426415");
    
    req2.addOrder(order1, "2134", "43242");
    req2.addOrder(order2, "980", "4321");
    req2.addOrder(order3, "87897", "423674");
    req2.addOrder(order5, "43212", "3426415");
    ArrayList<String> lst = new ArrayList<String>();
    lst.add("2134");
    lst.add("43242");
    lst.add("980");
    lst.add("4321");
    lst.add("87897");
    lst.add("423674");
    lst.add("43212");
    lst.add("3426415");
    assertEquals(req1.getSkus(), lst);
  }
  
  

}
