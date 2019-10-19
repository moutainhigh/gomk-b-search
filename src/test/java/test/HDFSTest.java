package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.gomk.framework.hbase.HBaseService;
import io.gomk.framework.hdfs.HdfsOperator;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class HDFSTest {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	HBaseService hbaseService; 
	
	static Configuration conf=null;
    static {
    	conf = new Configuration();
    	conf.set("fs.default.name", "hdfs://58.119.224.26:8020");
    }

	@Test
	public void testUpload() throws Exception {
		String dst = "20191019";
		//HdfsOperator.CreatDir(dst, conf);
		
		String src = "/Users/vko/Documents/my-code/DOC/表.xlsx";
		HdfsOperator.putToHDFS(src, dst ,conf);
		
		//String hdfsFileName = "/user/hdfsuser/hdfs/分布式体系在保险行业的试点分享.pdf";
		//String localDirPath = "/Users/vko/Documents/my-code";
		//HdfsOperator.downloadFileFromHdfs(hdfsFileName, localDirPath, conf);
		//HdfsOperator.downloadFile(hdfsFileName, localDirPath, conf);
		
		
		
	}
	
	public byte[] getSource(String URL) throws IOException{
		 File file = new File(URL);
		 file.length();
		 FileInputStream is = new FileInputStream(file);
		 byte[] bytes = new byte[(int) file.length()];
		 int len=0;
		 while( (len = is.read(bytes)) != -1 ){
			 is.read(bytes);
		 }
		 is.close();
		 BASE64Encoder be = new BASE64Encoder();
		 return be.encode(bytes).getBytes();
	}
	public void down(String text) throws IOException{
		BASE64Decoder be = new BASE64Decoder();
		byte []c = 	be.decodeBuffer(text);
		FileOutputStream out = new FileOutputStream(new File("/Users/vko/Documents/my-code/DOC/t222.pdf"));
		out.write(c);
		out.close();
	}
}
