package main.com.dmlab.ChongmingGao.CascadeSync;


public class CascadeSyncResult {

    public CascadeSyncResult(Data data, Parameter param) {
        this.data = data;
        this.param = param;
        this.layer = param.getLayer();
        multipleSingleResult = new SingleSyncResult[layer];
        UtilClass.normalization(data);
        invokeHierarchicalCascadeSync();
    }

    SingleSyncResult[] multipleSingleResult;
    int layer;
    Data data;
    Parameter param;

//    public SingleSyncResult[] getMultipleSingleResult() {
//        return multipleSingleResult;
//    }

    void invokeHierarchicalCascadeSync() {
        int paramLayer = param.getLayer();
        SingleSyncResult lastSingleSyncResult =  SingleSyncResult.constructFromRawData(data, param);

        for (int layer = 0; layer < paramLayer; layer++) {
            String msg = String.format("\n============Hierarchical CascadeSync Round [%d], Initial point number [%d]...============",layer, lastSingleSyncResult.ROIs.size());
            System.out.println(msg);

//            SingleSyncResult singleSyncResult = invokeOneLayerCascadeSync(lastSingleSyncResult,i);
            SingleSyncResult singleSyncResult = new SingleSyncResult(lastSingleSyncResult, data, param, layer);
            multipleSingleResult[layer] = singleSyncResult;
            lastSingleSyncResult =  singleSyncResult;
        }

    }

//
//    public SingleSyncResult invokeOneLayerCascadeSync(SingleSyncResult lastSingleSyncResult, int layer) {
//        SingleSyncResult result = new SingleSyncResult(lastSingleSyncResult, data, param, layer);
//
//        /* Todo */
//
//
//        return result;
//    }


}
