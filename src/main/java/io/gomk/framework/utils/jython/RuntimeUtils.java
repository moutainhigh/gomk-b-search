package io.gomk.framework.utils.jython;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;

import io.gomk.controller.response.ContrastVO;

public class RuntimeUtils {
	final static Base64.Encoder encoder = Base64.getEncoder();
	public static ContrastVO getContrastResult(String str1, String str2) throws UnsupportedEncodingException {
		ContrastVO vo = new ContrastVO();
		StringBuilder sb = new StringBuilder();
		try {
			String filePath = System.getProperty("user.dir")+"/src/main/resources/python/testText.py";
			// String filePath = "/root/python/difflib/diffString.py";
			str1 = "222";
			str2 = "3333";
			String[] args1 = new String[] { "python", filePath, str1, str2 };
			Process proc = Runtime.getRuntime().exec(args1);// 执行py文件

			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			int i = 0;
			while ((line = in.readLine()) != null) {
				if (i == 0 && StringUtils.isNotBlank(line)) {
					NumberFormat format = NumberFormat.getPercentInstance();
				    format.setMaximumFractionDigits(2);//设置保留几位小数
					vo.setSimilarity(format.format(Double.parseDouble(line)));
				} else {
					//System.out.println("=====" + line);
					sb.append(line);
				}
				i++;
			}
			in.close();
			proc.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//System.out.println(sb.toString());
		String encodedText = encoder.encodeToString(sb.toString().getBytes());
		vo.setContent(encodedText);
		return vo;
	}
}
