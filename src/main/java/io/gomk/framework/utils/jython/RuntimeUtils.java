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
public class RuntimeUtils {
	final static Base64.Encoder encoder = Base64.getEncoder();

	public static ContrastVO getContrastResult(String str1, String str2) throws UnsupportedEncodingException {
		ContrastVO vo = new ContrastVO();
		StringBuilder sb = new StringBuilder();
		//String rootPath = System.getProperty("user.dir") + "/src/main/resources/python/";
		String projectPath = System.getProperty("user.dir");
		File temp1 = new File(projectPath + "/1.txt");
		File temp2 = new File(projectPath + "/2.txt");
		
		
		try {
			InputStream initialStream  = RuntimeUtils.class.getClassLoader().getResourceAsStream("python/t1.py");
			File targetFile = new File(projectPath + "/diff.py");
			FileUtils.copyInputStreamToFile(initialStream, targetFile);
			log.info("projectPath:" + projectPath);
			
			InputStream txt1Stream = new ByteArrayInputStream(str1.getBytes());
			InputStream txt2Stream = new ByteArrayInputStream(str2.getBytes());
			FileUtils.copyInputStreamToFile(txt1Stream, temp1);
			FileUtils.copyInputStreamToFile(txt2Stream, temp2);
		 
			String[] args1 = new String[] { "python", targetFile.getAbsolutePath(), temp1.getAbsolutePath(), temp2.getAbsolutePath() };
			Process proc = Runtime.getRuntime().exec(args1);// 执行py文件

			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			int i = 0;
			while ((line = in.readLine()) != null) {
				if (i == 0 && StringUtils.isNotBlank(line)) {
					NumberFormat format = NumberFormat.getPercentInstance();
					format.setMaximumFractionDigits(2);// 设置保留几位小数
					vo.setSimilarity(format.format(Double.parseDouble(line)));
				} else {
					// System.out.println("=====" + line);
					sb.append(line);
				}
				i++;
			}
			// 获取异常输出流

			BufferedReader ine = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

			String linee = null;

			while ((linee = ine.readLine()) != null) {

				log.error("===Contrast proc error===" +linee);

			}
			
			in.close();
			proc.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			temp1.delete();
			temp1.delete();
		}
		
		// System.out.println(sb.toString());
		String encodedText = encoder.encodeToString(sb.toString().getBytes());
		vo.setContent(encodedText);
		return vo;
	}

	/**
	 * 将字符串写入指定文件(当指定的父路径中文件夹不存在时，会最大限度去创建，以保证保存成功！)
	 * 
	 * @param res      原字符串
	 * @param filePath 文件路径
	 * @return 成功标记
	 */
	public static boolean string2File(String res, String filePath) {
		boolean flag = true;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		try {
			File distFile = new File(filePath);
			if (!distFile.getParentFile().exists())
				distFile.getParentFile().mkdirs();
			bufferedReader = new BufferedReader(new StringReader(res));
			bufferedWriter = new BufferedWriter(new FileWriter(distFile));
			char buf[] = new char[1024]; // 字符缓冲区
			int len;
			while ((len = bufferedReader.read(buf)) != -1) {
				bufferedWriter.write(buf, 0, len);
			}
			bufferedWriter.flush();
			bufferedReader.close();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	public static void main(String[] args) throws IOException {
		//InputStream in  = Thread.currentThread().getContextClassLoader().getResourceAsStream("python/t1.py");
		InputStream initialStream  = RuntimeUtils.class.getClassLoader().getResourceAsStream("python/read_table.py");
		//log.info(in.toString());
		StringBuilder sb = new StringBuilder();
		String rootPath = System.getProperty("user.dir");
		
		File targetFile = new File(rootPath + "/dd.py");
		FileUtils.copyInputStreamToFile(initialStream, targetFile);
		
//		String str1 = "对比nginx配置文件的差异34343";
//		String str2 = "对比nginx配置文件的差异";
//		String str1Path = rootPath + "1.txt";
//		String str2Path = rootPath + "2.txt";
//		string2File(str1, str1Path);
//		string2File(str2, str2Path);
//		
//		String[] args1 = new String[] { "python", targetFile.getAbsolutePath(), str1Path, str2Path };
//		
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
