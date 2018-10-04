package main.com.dmlab.ChongmingGao.CascadeSync;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.max;
import static java.util.Collections.min;


public class Data {
    public Data() {
        trajs = new ArrayList<>();
    }

    int numberOfGPS = 0;
    int numberOfTraj = 0;
    double maxLat = -1000;
    double minLat = 1000;
    double maxLong = -1000;
    double minLong = 1000;
    List<Trajectory> trajs;

//    public void addOneNewTrajectory(int id, List<Point> points) {
//        Trajectory oneNewTrajectory = new Trajectory(id, points);
    public void  addOneNewTrajectory(Trajectory traj) {
        trajs.add(traj);
        numberOfTraj ++;
        List<Point> points = traj.points;
        numberOfGPS += points.size();
        maxLat = maxLat < max(points, new LatComparator()).lat ? max(points, new LatComparator()).lat : maxLat;
        maxLong = maxLong < max(points, new LongComparator()).lng ? max(points, new LongComparator()).lng : maxLong;
        minLat = minLat > min(points, new LatComparator()).lat ? min(points, new LatComparator()).lat : minLat;
        minLong = minLong > min(points, new LongComparator()).lng ? min(points, new LongComparator()).lng : minLong;
    }

    public int getNumberOfGPS() {
        return numberOfGPS;
    }

    public void setNumberOfGPS(int numberOfGPS) {
        this.numberOfGPS = numberOfGPS;
    }

    public int getNumberOfTraj() {
        return numberOfTraj;
    }

    public void setNumberOfTraj(int numberOfTraj) {
        this.numberOfTraj = numberOfTraj;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(double maxLat) {
        this.maxLat = maxLat;
    }

    public double getMinLat() {
        return minLat;
    }

    public void setMinLat(double minLat) {
        this.minLat = minLat;
    }

    public double getMaxLong() {
        return maxLong;
    }

    public void setMaxLong(double maxLong) {
        this.maxLong = maxLong;
    }

    public double getMinLong() {
        return minLong;
    }

    public void setMinLong(double minLong) {
        this.minLong = minLong;
    }

    public List<Trajectory> getTrajs() {
        return trajs;
    }

    public void setTrajs(List<Trajectory> trajs) {
        this.trajs = trajs;
    }
}
