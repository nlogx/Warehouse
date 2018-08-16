package testClasses;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import employees.Picker;
import head.PickingRequest;

public class PickerTest {
  private Picker p1;
  private Picker p2;
  private Picker p3;
  private PickingRequest r1;
  private PickingRequest r2;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {

  }

  @Before
  public void setUp() throws Exception {
    p1 = new Picker("P1");
    
    r1 = new PickingRequest(1);
    String[] order1 = new String[] {"blue", "XE"};
    String[] order2 = new String[] {"green", "XE"};
    String[] order3 = new String[] {"red", "XEX"};
    String[] order4 = new String[] {"blue", "SE"};
    
    r1.addOrder(order1, "o1", "531514");
    r1.addOrder(order2, "o2", "54323");
    r1.addOrder(order3, "o3", "3157796714");
    r1.addOrder(order4, "o4", "7");
    ArrayList<String> locationList = new ArrayList<String>();
    for (int i = 1; i < 9; i++) {
      locationList.add(String.valueOf(i));
    }
    r1.addLocations(locationList);
    p1.setRequest(r1);

  }


  @Test
  public void testSetRequest() {
    assertEquals("1", p1.getLocation());
    assertEquals(r1, p1.getTask());
  }

  @Test
  public void testEmptyMarshalling() {
    assertEquals(null, p1.marshalling());
  }
  
  @Test
  public void testEarlyMarshalling() {
    assertEquals("1", p1.getFascia("o1"));
    assertEquals(null, p1.marshalling());
  }
  
  @Test
  public void testMarshalling() {
    assertEquals("1", p1.getFascia("o1"));
    assertEquals("2", p1.getFascia("531514"));
    assertEquals("3", p1.getFascia("54323"));
    assertEquals("4", p1.getFascia("3157796714"));
    assertEquals("5", p1.getFascia("7"));
    assertEquals("6", p1.getFascia("o1"));
    assertEquals("7", p1.getFascia("o2"));
    assertEquals("8", p1.getFascia("o3"));
    p1.marshalling();
  }

  @Test
  public void testGetFascia() {
    assertEquals("1", p1.getFascia("o1"));
  }

  @Test
  public void testGetOverFascia() {
    assertEquals("1", p1.getFascia("o1"));
    assertEquals("2", p1.getFascia("531514"));
    assertEquals("3", p1.getFascia("54323"));
    assertEquals("4", p1.getFascia("3157796714"));
    assertEquals("5", p1.getFascia("7"));
    assertEquals("6", p1.getFascia("o1"));
    assertEquals("7", p1.getFascia("o2"));
    assertEquals("8", p1.getFascia("o3")); 
    
    assertEquals(null , p1.getFascia("o1")); 
    assertEquals(null, p1.getFascia("54323")); 

  }
}
