package test.com.dmlab.ChongmingGao.CascadeSync;

import main.com.dmlab.ChongmingGao.CascadeSync.Data;
import main.com.dmlab.ChongmingGao.CascadeSync.DataReader;
import main.com.dmlab.ChongmingGao.CascadeSync.Parameter;
import main.com.dmlab.ChongmingGao.CascadeSync.UtilClass;
import org.junit.jupiter.api.BeforeEach;

public class MyTest {
    String dataName;
    Data data;
    Parameter param;

    @BeforeEach
    void setUp() throws Exception {
        dataName = System.getProperty("user.dir") + "/Data";

        data = new Data();
        DataReader dataReader = new DataReader(dataName,data);
        System.out.println(UtilClass.getDistanceMean(data));


        param = new Parameter(data);
        /**
         * You can set all kinds of parameters in param;
         * For example, set the layer of CascadeSync as follow:
         */
        param.setLayer(4);
//        System.out.println(param.getParameterLayers()[3].epsilon);
    }
}
