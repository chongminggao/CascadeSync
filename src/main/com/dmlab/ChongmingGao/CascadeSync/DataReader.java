package main.com.dmlab.ChongmingGao.CascadeSync;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataReader {

    /**
     * @param path is a absolute path to the root of project.
     * @throws Exception throw when the reading process fail.
     */

    public DataReader(String path, Data data) throws Exception {
        this.path = path;
        this.data = data;
        if(!isExist(path))
            throw new FileNotFoundException("No such file or directory path!");

        System.out.println("The assigned data reading path is: " + path);
        files = new ArrayList<>();
        if( isDir(path) ) {
            listf(path,files);
        }else{
            files.add(new File(path));
        }
        int numOfFiles = files.size();
        readFiles(files, data);
        String msg = "All Reading completes. The number of files is [" + numOfFiles + "]";
        System.out.println(msg);
        msg = String.format("The range of map: Longtitude: [%f, %f];\n\t\t\t\t\tLatitude: [%f, %f];", data.minLong, data.maxLong, data.minLat, data.maxLat);
        System.out.println(msg);
        double distLR = UtilClass.getDistanceLeftRight(data);
        double distUD = UtilClass.getDistanceUpDown(data);
        msg = String.format("The up-down range of map is [%f] m, and left-right distance is [%f]m", distUD, distLR);
        System.out.println(msg);
    }

    String path;
    Data data;
    List<File> files;

    void readFiles(List<File> files, Data data) throws Exception {
        int id = 0;
        for (File file : files) {
            String fileName = file.getPath();
            String oneFileData = readFileAsString(fileName);
            System.out.println("== Trying to read trajs file from: " + fileName);
            constructData(data, oneFileData, id);
            id ++;
        }
    }


    boolean isExist(String path){
        File file = new File(path);
        return file.exists();
    }

    boolean isDir(String path) {
        File file = new File(path);
        return file.isDirectory();
    }

    void listf(String directoryName, List<File> files) {
        File directory = new File(directoryName);

        // Get all the files from a directory.
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                listf(file.getAbsolutePath(), files);
            }
        }
    }

    public static String readFileAsString(String fileName)throws Exception
    {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }

    void constructData(Data data, String oneFile, int id) {
        String[] rows = oneFile.split("\\r\\n|\\r|\\n");

        int len = rows.length;
        List<Double> latitude = new ArrayList<>();
        List<Double> longitude = new ArrayList<>();

        Trajectory traj = new Trajectory(id);

        for (int i = 0; i < len; i++) {
            String[] strs = rows[i].split("\\s*,\\s*");
            if(strs.length != 7 || strs[6].split(":").length != 3) {
                strs[0] = "this line fails!";
            }
            try {
                double lat = new Double(strs[0]);
                double lng = new Double(strs[1]);
                String time = strs[5] + " " + strs[6];
                Point point = new Point(lat, lng, time);
                traj.addNewPoints(point);
//            System.out.println(time);

            } catch (Exception e) {
                String msg = "==== Warning!! The line [" + (i+1) + "] of this file cannot be parsed and has been ignored. Check yourself!";
                System.out.println(msg);
            }
        }
        data.addOneNewTrajectory(traj);
    }
}
