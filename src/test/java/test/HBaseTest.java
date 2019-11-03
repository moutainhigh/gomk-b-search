package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.gomk.framework.hbase.HBaseService;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class HBaseTest {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	//@Autowired
	//HBaseService service; 
//	
//	static Configuration conf=null;
//    static {
//        conf= HBaseConfiguration.create();
//        conf.set("hbase.zookeeper.quorum","58.119.224.26");
//        conf.set("hbase.zookeeper.property.clientPort","2181");
//        conf.set("log4j.logger.org.apache.hadoop.hbase","WARN");
//    }

	@Test
	public void testUpload() throws IOException {
		//System.setProperty("hadoop.home.dir", "hadoop");
//		HBaseService service = new HBaseService();
//		
//		String filePath = "/Users/vko/Documents/my-code/DOC/分布式体系在保险行业的试点分享.pdf";
//		String tableName = "table2";
//		String familyName = "content";
//		String columnName = "c1";
//		String rowKey = "5875cb00-ebbd-4486-8dd8-4216bf9378";
//		
//		//create table 
//		//service.creatTable(tableName, new ArrayList<>(Arrays.asList(familyName)));
//		
//		logger.info(service.getAllTableNames().toString());
//		//service.putData(tableName, rowKey, familyName, columnName, getSource(filePath).getBytes());
//		//Map<String, Map<String, String>> m = service.getResultScanner(tableName);
//		//logger.info(m.toString());
//		//logger.info("========" + service.getRowData(tableName, rowKey).get(columnName));
//		String tmp = service.getRowData(tableName, rowKey).get(columnName);
//		down(tmp);
		
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
		FileOutputStream out = new FileOutputStream(new File("/Users/vko/Documents/my-code/temp/t2.pdf"));
		out.write(c);
		out.close();
	}
}
