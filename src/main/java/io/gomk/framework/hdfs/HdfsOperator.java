package io.gomk.framework.hdfs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
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
    public static boolean CreatDir(String dst , Configuration conf) throws InterruptedException, URISyntaxException{
        Path dstPath = new Path(dst) ;
        try{
            FileSystem dhfs = FileSystem.get(new URI("hdfs://58.119.224.26:8020"), conf, "hdfsuser");
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
    
    /**
     * 下载HDFS文件
     * @param path
     * @param downloadPath
     * @throws Exception
     */
    public static void downloadFile(String path, String downloadPath, Configuration conf) throws Exception {
        if (StringUtils.isEmpty(path) || StringUtils.isEmpty(downloadPath)) {
            return;
        }
        // 上传路径
        Path clientPath = new Path(path);
        FileSystem fs = clientPath.getFileSystem(conf) ;
        // 目标路径
        Path serverPath = new Path(downloadPath);

        // 调用文件系统的文件复制方法，第一个参数是否删除原文件true为删除，默认为false
        fs.copyToLocalFile(false, clientPath, serverPath);
        fs.close();
    }
    /**
	 * 从hdfs下载
	 * 
	 * @param hdfsFilename
	 * @param localPath
	 * @return
	 */
	public static boolean downloadFileFromHdfs(String hdfsFilename, String localPath, Configuration conf) {
		try {
			Path f = new Path(hdfsFilename);
			FileSystem hdfs = f.getFileSystem(conf) ;
			FSDataInputStream dis = hdfs.open(f);
			File file = new File(localPath + "/" + f.getName());
			FileOutputStream os = new FileOutputStream(file);
 
			byte[] buffer = new byte[1024000];
			int length = 0;
			while ((length = dis.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
 
			os.close();
			dis.close();
 
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
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
