package main.com.dmlab.ChongmingGao.CascadeSync;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ResultWriter {

    CascadeSyncResult result;
    String fileName;

    public ResultWriter(String fileName, CascadeSyncResult result) throws IOException {
        this.result = result;
        this.fileName = fileName;


        createFile(fileName);


        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false));
        Writting(writer);
        writer.close();
        System.out.println("\n=================================================================\n" +
                "Congratulation! The result is successfully written in path:\n" + fileName +
                "\n=================================================================\n");
    }

    void Writting(BufferedWriter writer) throws IOException {
        String str = "World";
        writer.append(" ");
        writer.append(str);


        str = "The initial point number: " + result.multipleSingleResult[0].map_fromPointToROI.size();
        writer.write(str);
        int layer = 1;
        for (SingleSyncResult singleSyncResult : result.multipleSingleResult) {
            int size = singleSyncResult.ROIs.size();
            str = String.format("\nLayer [%d] has ROI number [%d]." ,layer, size);
            writer.append(str);
            layer ++;
        }
        writer.append("\n");
        str = "The relationship between adjacent layer of ROIs can be retrieved from the CascadeSyncResult object!";
        writer.append("\n");
        writer.append(str);
    }





    public static boolean createFile(String destFileName) {
        File file = new File(destFileName);
        if(file.exists()) {
//            System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");
            return false;
        }
        if (destFileName.endsWith(File.separator)) {
            System.out.println("创建单个文件" + destFileName + "失败，目标文件不能为目录！");
            return false;
        }
        //判断目标文件所在的目录是否存在
        if(!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            System.out.println("目标文件所在目录不存在，准备创建它！");
            if(!file.getParentFile().mkdirs()) {
                System.out.println("创建目标文件所在目录失败！");
                return false;
            }
        }
        //创建目标文件
        try {
            if (file.createNewFile()) {
                System.out.println("创建单个文件" + destFileName + "成功！");
                return true;
            } else {
                System.out.println("创建单个文件" + destFileName + "失败！");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("创建单个文件" + destFileName + "失败！" + e.getMessage());
            return false;
        }
    }

    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
//            System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            System.out.println("创建目录" + destDirName + "成功！");
            return true;
        } else {
            System.out.println("创建目录" + destDirName + "失败！");
            return false;
        }
    }


}
