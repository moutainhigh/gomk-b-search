package io.gomk.framework.utils.jython;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.Base64;

import org.apache.catalina.startup.ClassLoaderFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import io.gomk.controller.response.ContrastVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RuntimeTest {
	
	public static void main(String[] args) throws IOException {
		//InputStream in  = Thread.currentThread().getContextClassLoader().getResourceAsStream("python/t1.py");
		InputStream initialStream  = RuntimeTest.class.getClassLoader().getResourceAsStream("read_table.py");
		//log.info(in.toString());
		StringBuilder sb = new StringBuilder();
		String rootPath = System.getProperty("user.dir");
		
		File targetFile = new File(rootPath + "/dd.py");
		FileUtils.copyInputStreamToFile(initialStream, targetFile);
		
		File file1 = new File("/Users/vko/Documents/my-code/testPDF/tb.pdf");
		File tempPDF = new File(rootPath + "/test.pdf");
		FileUtils.copyInputStreamToFile(new FileInputStream(file1), tempPDF);
		
        String[] args1 = new String[]{"python", targetFile.getAbsolutePath(), tempPDF.getAbsolutePath(), "9"};
		
		
		Process proc = Runtime.getRuntime().exec(args1);// 执行py文件
		BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String line = null;
		int i = 1;
		while ((line = in.readLine()) != null) {
			if (i == 0 && StringUtils.isNotBlank(line)) {
				NumberFormat format = NumberFormat.getPercentInstance();
				format.setMaximumFractionDigits(2);// 设置保留几位小数
				System.out.println("=====" + format.format(Double.parseDouble(line)));
			} else {
				System.out.println("=====" + line);
				sb.append(line);
			}
			i++;
		}
		// 获取异常输出流

		BufferedReader ine = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

		String linee = null;

		while ((linee = ine.readLine()) != null) {

			System.out.println(linee);

		}

		in.close();
		try {
			proc.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(sb.toString());
	}
	

}
