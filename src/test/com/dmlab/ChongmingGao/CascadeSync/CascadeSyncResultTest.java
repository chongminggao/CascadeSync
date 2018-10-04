package test.com.dmlab.ChongmingGao.CascadeSync;

import main.com.dmlab.ChongmingGao.CascadeSync.CascadeSyncResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class CascadeSyncResultTest {

    MyTest myTest;
    @BeforeEach
    void setUp() throws Exception {
        myTest = new MyTest();
        myTest.setUp();
    }

    @Test
    void invokeHierarchicalCascadeSync() {
        CascadeSyncResult result = new CascadeSyncResult(myTest.data, myTest.param);
    }
}