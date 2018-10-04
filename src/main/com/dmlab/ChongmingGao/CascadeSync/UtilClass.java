package main.com.dmlab.ChongmingGao.CascadeSync;

import javafx.util.Pair;

public class UtilClass {
    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 通过经纬度获取距离(单位：米)
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double getDistance(double lat1, double lng1, double lat2,
                                     double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s*1000;
        return s;
    }

    public static double getDistanceMean(double lat1, double lng1, double lat2, double lng2) {
        double distance_upDown = getDistanceUpDown(lat1,lng1,lat2,lng2);
        double distance_leftRight = getDistanceLeftRight(lat1,lng1,lat2,lng2);
        return (distance_leftRight + distance_upDown) / 2;
    }

    public static double getDistanceUpDown(double lat1, double lng1, double lat2, double lng2) {
        double longMedian = (lng1 + lng2) / 2;
        double distance_upDown = getDistance(lat1,longMedian,lat2,longMedian);
        return distance_upDown;
    }

    public static double getDistanceLeftRight(double lat1, double lng1, double lat2, double lng2) {
        double latMedian = (lat1 + lat2) / 2;
        double distance_leftRight = getDistance(latMedian,lng1,latMedian,lng2);
        return distance_leftRight;
    }

    public static double getDistanceMean(Data data) {
        double distance_upDown = getDistanceUpDown(data);
        double distance_leftRight = getDistanceLeftRight(data);
        return (distance_leftRight + distance_upDown) / 2;
    }

    public static double getDistanceUpDown(Data data) {
        double lat1 = data.minLat;
        double lat2 = data.maxLat;
        double lng1 = data.minLong;
        double lng2 = data.maxLong;
        double longMedian = (lng1 + lng2) / 2;
        double distance_upDown = getDistance(lat1,longMedian,lat2,longMedian);
        return distance_upDown;
    }

    public static double getDistanceLeftRight(Data data) {
        double lat1 = data.minLat;
        double lat2 = data.maxLat;
        double lng1 = data.minLong;
        double lng2 = data.maxLong;
        double latMedian = (lat1 + lat2) / 2;
        double distance_leftRight = getDistance(latMedian,lng1,latMedian,lng2);
        return distance_leftRight;
    }

    public static Pair<Integer, Integer> getKeyFromCoordinate(Point poi, Parameter param) {
        return getKeyFromCoordinate(poi.lat_norm, poi.lng_norm, param);
    }

    public static Pair<Integer, Integer> getKeyFromCoordinate(double lat, double lng, Parameter param) {
        double maxDisplacement = param.getMaxDisplacement();
        int num = param.getNumberOfPartition();
        int latKey = (int) (lat / maxDisplacement);
        int lngKey = (int) (lng / maxDisplacement);
        latKey = latKey >= num ? num - 1 : latKey;
        lngKey = lngKey >= num ? num - 1 : lngKey;
        return new Pair<>(latKey,lngKey);
    }

    public static Point getCoordinateFromKey(Pair<Integer, Integer> key, Data data, Parameter param) {
        double lat_norm = (key.getKey() + 0.5) * param.getMaxDisplacement();
        double lng_norm = (key.getValue() + 0.5) * param.getMaxDisplacement();
        double lat = lat_norm / Math.PI * 2 * (data.maxLat - data.minLat) + data.minLat;
        double lng = lng_norm / Math.PI * 2 * (data.maxLong - data.minLong) + data.minLong;
        return new Point(lat, lng, lat_norm, lng_norm);
    }

    public static void normalization(Point poi, Data data) {
        poi.lat_norm = (poi.lat - data.minLat) / (data.maxLat - data.minLat) * Math.PI / 2;
        poi.lng_norm = (poi.lng - data.minLong) / (data.maxLong - data.minLong) * Math.PI / 2;
        poi.isNormed = true;
//        return poi;
    }

    public static void denormalization(Point poi, Data data) {
        poi.lat = poi.lat_norm / Math.PI * 2 * (data.maxLat - data.minLat);
        poi.lng = poi.lng_norm / Math.PI * 2 * (data.maxLong - data.minLong);
//        return poi;
    }

//    public static Data normalization(Data data) {
//        Data normalizedData = new Data();
//        for (Trajectory traj : data.trajs) {
//            Trajectory normalizedTraj = new Trajectory(traj.id);
//            for (Point poi : traj.points) {
//                Point normalizedPoi = normalization(poi, data);
//                normalizedTraj.addNewPoints(normalizedPoi);
//            }
//            normalizedData.addOneNewTrajectory(normalizedTraj);
//        }
//        return normalizedData;
//    }

    public static void normalization(Data data) {
        for (Trajectory traj : data.trajs) {
            for (Point poi : traj.points) {
                normalization(poi, data);
            }
        }
    }

}
