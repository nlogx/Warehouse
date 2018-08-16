package testClasses;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({WarehouseManagerTest.class, WarehouseTest.class, PickerTest.class, SequencerTest.class,
  PickingManagerTest.class, LoaderTest.class, LoadingManagerTest.class, WorkerTest.class, TruckTest.class})
public class AllTests {

}
