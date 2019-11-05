package io.gomk.framework.utils.jython;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import io.gomk.controller.response.ContrastVO;
import io.gomk.framework.context.ApplicationContextUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RuntimeUtils {
	final static Base64.Encoder encoder = Base64.getEncoder();

	public static ContrastVO getContrastResult(String str1, String str2) throws UnsupportedEncodingException {
		ContrastVO vo = new ContrastVO();
		StringBuilder sb = new StringBuilder();
		//String rootPath = System.getProperty("user.dir") + "/src/main/resources/python/";
		String str1Path  = "";
		String str2Path = "";
		Resource resource = new ClassPathResource("python/t1.py");
		try {
			File f = resource.getFile();
			log.info("path:" + resource.getFile().getAbsolutePath());
			
			String filePath = f.getAbsolutePath();
			// String filePath = "/root/python/difflib/diffString.py";
			//str1 = "/Users/vko/Documents/my-code/python/v11.txt";
			//str2 = "/Users/vko/Documents/my-code/python/v12.txt";
			str1Path = f.getParent() + "/1.txt";
			str2Path = f.getParent() + "/2.txt";
			string2File(str1, str1Path);
			string2File(str2, str2Path);
			
			String[] args1 = new String[] { "python", filePath, str1Path, str2Path };
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
			 File file1 = new File(str1Path);
			 File file2 = new File(str2Path);
			 file1.delete();
			 file2.delete();
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
		StringBuilder sb = new StringBuilder();
		String rootPath = System.getProperty("user.dir") + "/src/main/resources/python/";
		String filePath = rootPath + "t1.py";
		// String filePath = "/root/python/difflib/diffString.py";
		String str1 = "对比nginx配置文件的差异34343";
		String str2 = "对比nginx配置文件的差异";
		String str1Path = rootPath + "1.txt";
		String str2Path = rootPath + "2.txt";
		string2File(str1, str1Path);
		string2File(str2, str2Path);
		
		String[] args1 = new String[] { "python", filePath, str1Path, str2Path };
		Process proc = Runtime.getRuntime().exec(args1);// 执行py文件

		BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String line = null;
		int i = 0;
		while ((line = in.readLine()) != null) {
			if (i == 0 && StringUtils.isNotBlank(line)) {
				NumberFormat format = NumberFormat.getPercentInstance();
				format.setMaximumFractionDigits(2);// 设置保留几位小数
				System.out.println("=====" + format.format(Double.parseDouble(line)));
			} else {
				//System.out.println("=====" + line);
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
