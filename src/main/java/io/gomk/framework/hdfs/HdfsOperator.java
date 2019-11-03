package io.gomk.framework.hdfs;

import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HdfsOperator {
    private static final Logger LOGGER = LoggerFactory.getLogger(HdfsOperator.class);
//    public static void main(String[] args) {
//        boolean status = false ;
//        String dst1 = "hdfs://10.212.164.42:8020/EBLearn_data/new" ;
//        System.setProperty("HADOOP_USER_NAME","hdfs");
//        Configuration conf = new Configuration() ;
////jva.lang.IllegalArgumentException: Wrong FS:            hdfs://192.168.1.225:9000/EBLearn_data/hello.txt, expected: file:///
// //解决这个错误的两个方案：
////方案1：下面这条命令必须加上，否则出现上面这个错误
//        conf.set("fs.default.name", "hdfs://10.212.164.42:8020"); // "hdfs://master:9000"  
////        conf.set("fs.default.name", "hdfs://10.212.164.42:8020"); // "hdfs://master:9000"  
// //方案2： 将core-site.xml 和hdfs-site.xml放入当前工程中
////        status = CreatDir( dst1 ,conf) ;
////        System.out.println("status="+status) ;
//
//        String dst = "hdfs://10.212.164.42:8020/ibs" ;
////        String src = "D:/PointId.txt" ;
////
////        status = putToHDFS( src , dst ,conf) ;
////        System.out.println("status="+status) ;
////        String src = "hdfs://10.212.164.42:8020/EBLearn_data/PointId.txt" ;
////        String dst = "D:/" ;
////        status = getFromHDFS( src , dst , conf) ;
////        System.out.println("status="+status) ;
////
////        dst = "hdfs://192.168.1.225:9000/ibs" ;
//        status = checkAndDel( dst , conf) ;
//        System.out.println("status="+status) ;
//    }
    public static boolean CreatDir(String dst , Configuration conf){
        Path dstPath = new Path(dst) ;
        try{
            FileSystem dhfs = FileSystem.get(conf);
            dhfs.mkdirs(dstPath);
        }
        catch(IOException ie){
            ie.printStackTrace() ;
            return false ;
        }
        return true ;
    }
//  src 数据源位置 dst为hdfs上位置
    public static boolean putToHDFS(String src , String dst , Configuration conf){
//        System.out.println("PUT value To HDFS..............................................");
        LOGGER.info("PUT value To HDFS  Start...........................................");
        Path dstPath = new Path(dst);
        try{
            FileSystem hdfs = dstPath.getFileSystem(conf) ;
            hdfs.copyFromLocalFile(false, new Path(src), dstPath) ;
            LOGGER.info("PUT File To HDFS SCUESS................................");
        }
        catch(IOException e){
            LOGGER.info("failed:    "+e.toString());
            e.printStackTrace() ;
            return false ;
        }
        return true ;
    }
//
    public static boolean getFromHDFS(String src , String dst , Configuration conf){
        Path dstPath = new Path(dst) ;
        try{
            FileSystem dhfs = dstPath.getFileSystem(conf) ;
            dhfs.copyToLocalFile(false, new Path(src), dstPath) ;
        }catch(IOException ie){
            ie.printStackTrace() ;
            return false ;
        }
        return true ;
    }
    
    public static InputStream getInputStreamFromHDFS(String src , String dst , Configuration conf){
        Path dstPath = new Path(dst) ;
        try{
            FileSystem dhfs = dstPath.getFileSystem(conf) ;
            return  dhfs.open(new Path(src));
        }catch(IOException ie){
            ie.printStackTrace() ;
            return null ;
        }
    }

    /**
     * 文件删除
     */
    public static boolean checkAndDel(final String path , Configuration conf){
        Path dstPath = new Path(path) ;
        try{
            FileSystem dhfs = dstPath.getFileSystem(conf) ;
            System.out.println("开始删除hdfs");
            if(dhfs.exists(dstPath)){
                dhfs.delete(dstPath, true) ;
                System.out.println("删除hdfs");
            }else{
                return false ;
            }
        }catch(IOException ie ){
            ie.printStackTrace() ;
            return false ;
        }
        return true ;
    }

}
