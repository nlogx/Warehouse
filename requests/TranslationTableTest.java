package requests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TranslationTableTest {

  TranslationTable table;
  
  @Before
  public void setUp() throws Exception {
    ArrayList<String> lst = new ArrayList<String>();
    lst.add("SES,Blue,1365,278");
    lst.add("X,Red,387,54324");
    lst.add("SE,Red,4432,543");
    lst.add("XE,Green,17865,26575");
    lst.add("XE,Magenta,671,263754");
    lst.add("SES,Grey,15243,2142");

    table = new TranslationTable(lst);
  }

  @Test
  public void getSku() {
    String[] test1 = table.getSkus("SES", "Blue");
    String[] c1 = {"1365", "278"};
    assertEquals(test1[0], c1[0]);
    assertEquals(test1[1], c1[1]);

    String[] test2 = table.getSkus("XE", "Magenta");
    String[] c2 = {"671", "263754"};
    assertEquals(test2[0], c2[0]);
    assertEquals(test2[1], c2[1]);
  }

}
