package testClasses;

import static org.junit.Assert.*;

import java.util.ArrayList;

import divisionHeads.PickingManager;
import head.PickingRequest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;



public class PickingManagerTest {
  private PickingManager pm;
  private PickingRequest r1;
  private PickingRequest r2;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {}

  @AfterClass
  public static void tearDownAfterClass() throws Exception {}

  @Before
  public void setUp() throws Exception {
    pm = new PickingManager();
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

    r2 = new PickingRequest(2);
    r2.addOrder(order2, "o2", "54323");
    r2.addOrder(order2, "o2", "54323");
    r2.addOrder(order2, "o2", "54323");
    r2.addOrder(order2, "o2", "54323");
    locationList.clear();
    for (int i = 1; i < 5; i++) {
      locationList.add("3");
    }
    for (int i = 1; i < 5; i++) {
      locationList.add("4");
    }
    r2.addLocations(locationList);
  }

  @After
  public void tearDown() throws Exception {}

  @Test
  public void processOneTask() {
    pm.hire("bob");
    pm.addRequest(r1);
    assertEquals("1", pm.processTask("bob", "o1"));
  }

  @Test
  public void processFullTask() {
    pm.hire("bob");
    pm.addRequest(r1);
    assertEquals("1", pm.processTask("bob", "o1"));
    assertEquals("2", pm.processTask("bob", "531514"));
    assertEquals("3", pm.processTask("bob", "54323"));
    assertEquals("4", pm.processTask("bob", "3157796714"));
    assertEquals("5", pm.processTask("bob", "7"));
    assertEquals("6", pm.processTask("bob", "o1"));
    assertEquals("7", pm.processTask("bob", "o2"));
    assertEquals("8", pm.processTask("bob", "o3"));
  }
  
  @Test
  public void processOverTask() {
    pm.hire("bob");
    pm.addRequest(r1);
    assertEquals("1", pm.processTask("bob", "o1"));
    assertEquals("2", pm.processTask("bob", "531514"));
    assertEquals("3", pm.processTask("bob", "54323"));
    assertEquals("4", pm.processTask("bob", "3157796714"));
    assertEquals("5", pm.processTask("bob", "7"));
    assertEquals("6", pm.processTask("bob", "o1"));
    assertEquals("7", pm.processTask("bob", "o2"));
    assertEquals("8", pm.processTask("bob", "o3"));
    assertEquals(null, pm.processTask("bob", "o3"));
    assertEquals(null, pm.processTask("bob", "7"));
  }

  @Test
  public void marshalling() {
    pm.hire("bob");
    pm.addRequest(r1);
    assertEquals("1", pm.processTask("bob", "o1"));
    assertEquals("2", pm.processTask("bob", "531514"));
    assertEquals("3", pm.processTask("bob", "54323"));
    assertEquals("4", pm.processTask("bob", "3157796714"));
    assertEquals("5", pm.processTask("bob", "7"));
    assertEquals("6", pm.processTask("bob", "o1"));
    assertEquals("7", pm.processTask("bob", "o2"));
    assertEquals("8", pm.processTask("bob", "o3"));
    assertEquals(r1, pm.marshalling("bob"));
  }

  @Test
  public void processNoTask() {
    pm.hire("bob");
    assertEquals(null, pm.processTask("bob", "o1"));
  }

  @Test
  public void processNoPicker() {
    pm.addRequest(r2);
    pm.hire("bob");
    pm.hire("bob");
    assertEquals(null, pm.processTask("Alice", "o2"));
  }
  
  @Test
  public void noPickerMarshalling() {
    assertEquals(null, pm.marshalling("dino"));
  }
  @Test
  public void earlyMarshalling() {
    pm.addRequest(r2);
    pm.hire("steven");
    assertEquals("3", pm.processTask("steven", "o2"));
    assertEquals("3", pm.processTask("steven", "o2"));
    assertEquals("3", pm.processTask("steven", "o2"));
    assertEquals("3", pm.processTask("steven", "o2"));
    assertEquals(4, r2.getPallets()[0].getSize());
    assertEquals(null, pm.marshalling("steven"));
  }

  @Test
  public void processWrongTask() {
    pm.addRequest(r2);
    pm.hire("bob");
    assertEquals(null, pm.processTask("bob", "o1"));
  }

}
