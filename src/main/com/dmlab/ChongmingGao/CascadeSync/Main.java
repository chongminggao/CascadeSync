package main.com.dmlab.ChongmingGao.CascadeSync;


public class Main {

    public static void main(String[] args) throws Exception {
        String dataName = System.getProperty("user.dir") + "/Data";

        Data data = new Data();
        DataReader dataReader = new DataReader(dataName,data);
        System.out.println(UtilClass.getDistanceMean(data));


        Parameter param = new Parameter(data);
        /**
         * You can set all kinds of parameters in param;
         * For example, set the layer of CascadeSync as follow:
         */
        param.setLayer(4);


        CascadeSyncResult result = new CascadeSyncResult(data, param);

        String resultPath = System.getProperty("user.dir") + "/result/result.txt";
        ResultWriter resultWriter = new ResultWriter(resultPath, result);
    }

}
