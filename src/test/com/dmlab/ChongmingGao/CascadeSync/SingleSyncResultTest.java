package test.com.dmlab.ChongmingGao.CascadeSync;

import main.com.dmlab.ChongmingGao.CascadeSync.Data;
import main.com.dmlab.ChongmingGao.CascadeSync.Parameter;
import main.com.dmlab.ChongmingGao.CascadeSync.SingleSyncResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SingleSyncResultTest {
    MyTest myTest;

    @BeforeEach
    void setUp() throws Exception {
        myTest = new MyTest();
        myTest.setUp();
    }

    @Test
    void constructFromRawData() {
        SingleSyncResult initialResult = SingleSyncResult.constructFromRawData(myTest.data, myTest.param);
    }
}