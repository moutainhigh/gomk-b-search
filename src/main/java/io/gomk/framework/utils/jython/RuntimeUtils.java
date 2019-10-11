package io.gomk.framework.utils.jython;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class RuntimeUtils {
	final static Base64.Encoder encoder = Base64.getEncoder();
	public static String getContrastResult(String str1, String str2) throws UnsupportedEncodingException {

		StringBuilder sb = new StringBuilder();
		try {
			String filePath = System.getProperty("user.dir")+"/src/main/resources/python/testText.py";
			// String filePath = "/root/python/difflib/diffString.py";
			String[] args1 = new String[] { "python", filePath, str1, str2 };
			Process proc = Runtime.getRuntime().exec(args1);// 执行py文件

			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;

			while ((line = in.readLine()) != null) {
				sb.append(line);

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
		return encodedText;
	}
}
