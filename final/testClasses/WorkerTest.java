package testClasses;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import employees.Worker;
import head.PickingRequest;

public class WorkerTest {

	private Worker w1;
	private Worker w2;
	private PickingRequest r1;
	private PickingRequest r2;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		w1 = new Worker("w1");
		w2 = new Worker("w2");
		if (w1.isReady()) {
			w1.setTask(r1);
		}
		w2.setBusy();
		if (w2.getTask() == null) {
			w2.setReady();
		}
		
		
	}

	@Test
	public void testIsReady() {
	  assertEquals(false, w1.isReady());
	  assertEquals(true, w2.isReady());
	  
	}
	
    @Test
	public void testGetName() {
      assertEquals("w1", w1.getName());
      assertEquals("w2", w2.getName());
	}
    
    @Test
	public void testSetAndGetTask() {
	  w2.setTask(r2);
	  assertEquals(r2, w2.getTask());
	  assertEquals(r1, w1.getTask());
	}

}
