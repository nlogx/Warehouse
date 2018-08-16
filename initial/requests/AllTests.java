package requests;

import inventory.*;
import loading.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({OrderManagerTest.class, PickerTest.class, PickingRequestTest.class,
    TranslationTableTest.class, LoadingManagerTest.class, TruckTest.class, 
    StorageTest.class, WarehouseTest.class, WarehouseManagerTest.class})
public class AllTests {

}
