package main.com.dmlab.ChongmingGao.CascadeSync;

import java.util.Comparator;

public class Point {

    public Point(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Point(double lat, double lng, String time) {
        this.lat = lat;
        this.lng = lng;
        this.time = time;
    }

    public Point(double lat, double lng, double lat_norm, double lng_norm) {
        this.lat = lat;
        this.lng = lng;
        this.lat_norm = lat_norm;
        this.lng_norm = lng_norm;
        this.isNormed = true;
    }

    double lat;
    double lng;
    double lat_norm;
    double lng_norm;
    boolean isNormed = false;
    String time;
}


class LatComparator implements Comparator<Point> {
    @Override
    public int compare(Point o1, Point o2) {
        if (o1.lat > o2.lat)
            return 1;
        if (o1.lat < o2.lat)
            return -1;
        else
            return 0;
    }
}

class LongComparator implements Comparator<Point> {
    @Override
    public int compare(Point o1, Point o2) {
        if (o1.lng > o2.lng)
            return 1;
        if (o1.lng < o2.lng)
            return -1;
        else
            return 0;
    }
}

