package main.com.dmlab.ChongmingGao.CascadeSync;

import java.util.ArrayList;
import java.util.List;

public class Trajectory {

    public Trajectory(int id, List<Point> points) {
        this.id = id;
        this.points = points;
    }

    public Trajectory(int id) {
        this.id = id;
        this.points = new ArrayList<>();
    }

    int id;
    List<Point> points;


    public void addNewPoints(Point point) {
        this.points.add(point);
    }

    public void addNewPoints(List<Point> points) {
        this.points.addAll(points);
    }

    public void mergeAnotherTraj(Trajectory trajectory) {
        points.addAll(trajectory.points);
    }

}
