package main.com.dmlab.ChongmingGao.CascadeSync;

import java.util.Set;

public class ROI {

    public ROI(int id, int layer, Point center) {
        this.id = id;
        this.layer = layer;
        this.center = center;
    }

    public ROI(int id, int layer, Point center, int weight, Set<Point> representPOI) {
        this.id = id;
        this.layer = layer;
        this.center = center;
        this.weight = weight;
        this.representPOI = representPOI;
    }

    public ROI(int id, int layer, Point center, int weight, Set<Point> representPOI, Set<ROI> childrens) {
        this.id = id;
        this.layer = layer;
        this.center = center;
        this.weight = weight;
        this.childrens = childrens;
        this.representPOI = representPOI;
    }

    int id;
    int layer;
    Point center;
    double latNorm_changed;
    double lngNorm_changed;

    public void setChangedValueBeforeInteraction() {
        latNorm_changed = center.lat_norm;
        lngNorm_changed = center.lng_norm;
    }

    int weight;
    Set<ROI> childrens;
    ROI Parent;
    Set<Point> representPOI;
}
