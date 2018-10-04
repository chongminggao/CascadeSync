package main.com.dmlab.ChongmingGao.CascadeSync;


import javafx.util.Pair;

import java.util.*;

import static javax.swing.UIManager.get;

public class SingleSyncResult {

    Set<ROI> ROIs;
    Map<ROI,Set<Point>> map_fromROIToPoints;
    Map<Point,ROI> map_fromPointToROI;
    Map<Pair<Integer,Integer>, Set<Point>> map_fromGridtoPoints;
//    List<Map> map_fastrecord;
    Data data;
    Parameter param;
    int layer = 0;

    public SingleSyncResult() {
        ROIs = new HashSet<>();
        map_fromROIToPoints = new HashMap<>();
        map_fromPointToROI = new HashMap<>();
//        map_fastrecord = new ArrayList<>();
        map_fromGridtoPoints = new HashMap<>();
    }


    public SingleSyncResult(SingleSyncResult lastSingleSyncResult, Data data, Parameter param,int layer) {
        this.data = data;
        this.param = param;
        this.layer = layer + 1;

        map_fromROIToPoints = new HashMap<>();
        map_fromPointToROI = new HashMap<>();
        map_fromGridtoPoints = new HashMap<>();
//        map_fastrecord = new ArrayList<>();

        ROIs = invokeKNNSync(lastSingleSyncResult);

        for (ROI roi : ROIs) {
            map_fromROIToPoints.put(roi, roi.representPOI);
            for (Point poi : roi.representPOI) {
                map_fromPointToROI.put(poi,roi);
            }
        }
    }

    Set<ROI> invokeKNNSync (SingleSyncResult lastSingleSyncResult) {
        int numberOfGrid_last = 0;

        int Times = 1;
        Set<ROI> ROISet = lastSingleSyncResult.ROIs;
        while (true){
            Map<ROI, List<Pair<ROI, Double>>> ROIsandNeighbors = new HashMap<>();
            for (ROI roi: ROISet) {
                roi.setChangedValueBeforeInteraction();
                List<Pair<ROI, Double>> neighbors = KNNSearcher (roi, ROISet, param.getKnn());
                ROIsandNeighbors.put(roi, neighbors);
            }
            Set<ROI> ROISet_new = InvokeSync(ROIsandNeighbors, param.getMaxDisplacement(), 1);
            Map<Pair<Integer,Integer>, Set<ROI>> map_fromGridtoROIs = getGridResult(ROISet_new, param);

            int numberOfGrid = map_fromGridtoROIs.size();
            ROISet = new HashSet<>();
            int idx = 0;
            for (Map.Entry<Pair<Integer,Integer>, Set<ROI>> entry : map_fromGridtoROIs.entrySet()){

                Pair<Integer,Integer> key = entry.getKey();
                Point center = UtilClass.getCoordinateFromKey(key, data, param);

                int weightAll = 0;
                Set<Point> representPOI = new HashSet<>();
                Set<ROI> childrens = new HashSet<>();
                for (ROI children : entry.getValue()){
                    weightAll += children.weight;
                    representPOI.addAll(children.representPOI);
                    /**
                     * ROI.children is designed to record the mapping relationship between two BIG Layer,
                     * Note, we DO NOT use it as a intermediate buffer for recording the temporal knn layer.
                     */
                    if (Times == 1)
                        childrens.add(children);
                    else
                        childrens.addAll(children.childrens);
                }
                ROI roi = new ROI(idx++, layer, center, weightAll,representPOI,childrens);
                ROISet.add(roi);

                /**
                 *  This operation should have been conduced on last level of function stack, i.e. SingleSyncResult().
                 *  However, the Pair<Integer,Integer> is lost at that level, so I conduct operation as belows.
                 */
                if (numberOfGrid == numberOfGrid_last) {
                    map_fromGridtoPoints.put(entry.getKey(),representPOI);
                }
            }

            String msg = String.format("layer [%d], Loop [%d], intermediate ROI number [%d]...", layer, Times, ROISet.size());
            System.out.println(msg);

            if (numberOfGrid == numberOfGrid_last) {
                return ROISet;
            }
            numberOfGrid_last = numberOfGrid;
            Times ++;
        }
    }

    Set<ROI> InvokeSync(Map<ROI, List<Pair<ROI, Double>>> ROIsandNeighbors, double max_displacement, double alpha) {
//        alpha = 0.8;
        boolean isConverge = false;
        while (!isConverge) {
            double[] deltaLat_record = new double[ROIsandNeighbors.size()];
            double[] deltaLng_record = new double[ROIsandNeighbors.size()];

            double maxChange = 0;
            int idx = 0;
            for (Map.Entry<ROI, List<Pair<ROI, Double>>> entry : ROIsandNeighbors.entrySet()){
                ROI roi = entry.getKey();
                double deltaLat = 0;
                double deltaLng = 0;
                int weightAll = 0;
                for (Pair<ROI, Double> roijAndDistance : entry.getValue()) {
                    ROI roj = roijAndDistance.getKey();
                    deltaLat += Math.sin(roj.latNorm_changed - roi.latNorm_changed) * roj.weight;
                    deltaLng += Math.sin(roj.lngNorm_changed - roi.lngNorm_changed) * roj.weight;
                    weightAll += roj.weight;
                }
                if (entry.getValue().size() > 0) {
                    deltaLat = deltaLat * alpha / (weightAll + roi.weight);
                    maxChange = maxChange > deltaLat ? maxChange : deltaLat;
                    deltaLng = deltaLng * alpha / (weightAll + roi.weight);
                    maxChange = maxChange > deltaLng ? maxChange : deltaLng;
                }
                deltaLat_record[idx] = deltaLat;
                deltaLng_record[idx] = deltaLng;
                idx ++;
            }

            idx = 0;
            for (Map.Entry<ROI, List<Pair<ROI, Double>>> entry : ROIsandNeighbors.entrySet()){
                ROI roi = entry.getKey();
                roi.latNorm_changed += deltaLat_record[idx];
                roi.lngNorm_changed += deltaLng_record[idx];
                idx ++;
            }

            if (maxChange <= max_displacement) {
                isConverge = true;
            }
        }
        return ROIsandNeighbors.keySet();
    }

    List<Pair<ROI, Double>> KNNSearcher (ROI roi, Set<ROI> ROISet, int k) {
        List<Pair<ROI, Double>> neighbors = new ArrayList<>();
        for (ROI roij : ROISet) {
            if (roij.equals(roi))
                continue;
            tryToPutInKNNSet(roi ,neighbors, roij, param.getParameterLayers()[layer - 1].epsilon, k);
        }
        return neighbors;
    }

    void tryToPutInKNNSet (ROI roi,  List<Pair<ROI, Double>> neighbors, ROI roij, double toleranceDist, int k) {
        int pos = 0;
        double distij = distance(roi, roij);
        if (distij > toleranceDist)
            return;

        if (neighbors.size() == 0) {
            neighbors.add(pos, new Pair(roij, distij));
        }
        for ( Pair<ROI, Double> pair : neighbors) {
            double distik = pair.getValue();
            if (distij < distik) {
                neighbors.add(pos, new Pair(roij, distij));
                if (neighbors.size() > k) {
                    neighbors.remove(k);
                }
                return;
            }
            pos ++;
        }
    }

    double distance(ROI i, ROI j){
        return Math.sqrt((i.center.lat_norm - j.center.lat_norm) * (i.center.lat_norm - j.center.lat_norm)
                + (i.center.lng_norm - j.center.lng_norm) * (i.center.lng_norm - j.center.lng_norm));
    }



    /**
     * This is a static construction method, which resembles the use of constructor.
     * @param data global data
     * @param param global parameter
     * @return initial result for CascadeSync iteration.
     */
    public static SingleSyncResult constructFromRawData(Data data, Parameter param){
        SingleSyncResult result = new SingleSyncResult();

        result.data = data;
        result.param = param;
//        Data normedData = UtilClass.normalization(data);
        Map<Pair<Integer,Integer>, Set<Point>> map_fromGridtoPoints = getGridResult(data,param);

        result.map_fromGridtoPoints = map_fromGridtoPoints;
        int ROIid = 0;
        for (Map.Entry< Pair<Integer,Integer>, Set<Point> > entry : map_fromGridtoPoints.entrySet()) {
            Pair<Integer,Integer> key = entry.getKey();
            Set<Point> pointSet = entry.getValue();
            Point center = UtilClass.getCoordinateFromKey(key, data, param);
            ROI roi  = new ROI(ROIid++,0, center, pointSet.size(), pointSet);
            result.ROIs.add(roi);
            result.map_fromROIToPoints.put(roi,pointSet);
            for (Point poi : pointSet) {
                result.map_fromPointToROI.put(poi,roi);
            }
        }
        return result;
    }

    static Map<Pair<Integer,Integer>, Set<Point>> getGridResult(Data data, Parameter param) {
        /**
         * Attention, the Map default retrieval behavior is override.
         */
        Map<Pair<Integer,Integer>, Set<Point>> map_fromGridtoPoints = new HashMap<Pair<Integer,Integer>, Set<Point>>(){
            @Override
            public Set<Point> get(Object key) {
                Set<Point> set = super.get(key);
                if (set == null) {
                    set = new HashSet<>();
                    put((Pair<Integer, Integer>) key, set);
                }
                return set;
            }
        };

        for (Trajectory traj : data.trajs) {
            for (Point point : traj.points) {
                Pair<Integer,Integer> key = UtilClass.getKeyFromCoordinate(point,param);
                /**
                 * This is safe only in overrided definition of map_fromGridtoPoints.
                 */
                Set<Point> pointSet = map_fromGridtoPoints.get(key);
                pointSet.add(point);
            }
        }
        return map_fromGridtoPoints;
    }

    static Map<Pair<Integer,Integer>, Set<ROI>> getGridResult(Set<ROI> ROISet, Parameter param) {
        /**
         * Attention, the Map default retrieval behavior is override.
         */
        Map<Pair<Integer,Integer>, Set<ROI>> map_fromGridtoROI = new HashMap<Pair<Integer,Integer>, Set<ROI>>(){
            @Override
            public Set<ROI> get(Object key) {
                Set<ROI> set = super.get(key);
                if (set == null) {
                    set = new HashSet<>();
                    put((Pair<Integer, Integer>) key, set);
                }
                return set;
            }
        };

        for (ROI roi : ROISet) {
            Pair<Integer,Integer> key = UtilClass.getKeyFromCoordinate(roi.latNorm_changed, roi.lngNorm_changed, param);
            /**
             * This is safe only in overrided definition of map_fromGridtoPoints.
             */
            map_fromGridtoROI.get(key).add(roi);
        }

        return map_fromGridtoROI;
    }




}
