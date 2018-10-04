package main.com.dmlab.ChongmingGao.CascadeSync;


public class Parameter {

    public Parameter() {
        parameterLayers = constructParamterLayer();

    }

    public Parameter(Data data) {
        this.data = data;
        distanceMean = UtilClass.getDistanceMean(data);
        parameterLayers = constructParamterLayer();
    }

    private Data data;
    private int layer = 4;
    private int Knn = 5;
    private int maxLayer = 50;
    private int numberOfPartition = 2 * 1024;
    private double maxDisplacement = 1 / (double)numberOfPartition * Math.PI / 2;
    private double distanceMean;
    private double maxEpsilonRatio = 0.15;



    public ParameterLayer[] parameterLayers;

    public ParameterLayer[] getParameterLayers() {
        return parameterLayers;
    }


    ParameterLayer[] constructParamterLayer() {
        ParameterLayer[] parameterLayers = new ParameterLayer[layer];
        double maxEpsilon = distanceMean * maxEpsilonRatio;
        for (int l = 0; l < layer; l++) {
            double epsilon = maxEpsilon / layer * (l + 1) / distanceMean * Math.PI / 2;
            parameterLayers[l] = new ParameterLayer(epsilon);
        }
        return parameterLayers;
    }

    public class ParameterLayer {
        public ParameterLayer(double epsilon) {
            this.epsilon = epsilon;
        }
        double epsilon;
    }


//    private ParameterMultiLayer paramMulti;
//
//    public ParameterMultiLayer getParamMulti() {
//        return paramMulti;
//    }
//
//    public void setParamMulti(ParameterMultiLayer paramMulti) {
//        this.paramMulti = paramMulti;
//    }

    public double getMaxEpsilonRatio() {
        return maxEpsilonRatio;
    }

    public void setMaxEpsilonRatio(double maxEpsilonRatio) {
        this.maxEpsilonRatio = maxEpsilonRatio;
        parameterLayers = constructParamterLayer();
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
        distanceMean = UtilClass.getDistanceMean(data);
        parameterLayers = constructParamterLayer();
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getKnn() {
        return Knn;
    }

    public void setKnn(int knn) {
        Knn = knn;
    }

    public int getMaxLayer() {
        return maxLayer;
    }

    public void setMaxLayer(int maxLayer) {
        this.maxLayer = maxLayer;
    }

    public int getNumberOfPartition() {
        return numberOfPartition;
    }

    public void setNumberOfPartition(int numberOfPartition) {
        this.numberOfPartition = numberOfPartition;
    }

    public double getMaxDisplacement() {
        return maxDisplacement;
    }

    public void setMaxDisplacement(double maxDisplacement) {
        this.maxDisplacement = maxDisplacement;
    }

    public double getDistanceMean() {
        return distanceMean;
    }

    public void setDistanceMean(double distanceMean) {
        this.distanceMean = distanceMean;
        parameterLayers = constructParamterLayer();
    }

}
