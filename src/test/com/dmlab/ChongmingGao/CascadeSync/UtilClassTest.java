package test.com.dmlab.ChongmingGao.CascadeSync;

import main.com.dmlab.ChongmingGao.CascadeSync.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilClassTest {
    MyTest myTest;

    @BeforeEach
    void setUp() throws Exception {
        myTest = new MyTest();
        myTest.setUp();
    }

    @Test
    void normalization() {
        UtilClass.normalization(myTest.data);
        System.out.println(myTest.data.getMaxLat());
        assertEquals(myTest.data.getMaxLat(),Math.PI/2);
        assertEquals(myTest.data.getMaxLong(),Math.PI/2);
        assertEquals(myTest.data.getMinLat(),0);
        assertEquals(myTest.data.getMinLong(),0);
    }

    @Test
    void getKeyFromCoordinate() {
        Point poi1 = new Point(1.0001,0.0001);
        Point poi2 = new Point( 1.007, 0.0003);
        assertEquals(UtilClass.getKeyFromCoordinate(poi1, myTest.param), UtilClass.getKeyFromCoordinate(poi2, myTest.param));
    }
}