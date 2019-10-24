package io.gomk.framework.utils.parse;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

public class Word2003 {

	public static void main(String[] args) {
		String filePath = "/Users/vko/Documents/my-code/DOC/test/3.doc";
		try {
			System.out.println(read(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(str);
		//String str="China|||||America::::::England&&&&&&&Mexica";
        //System.out.println(str.replaceAll("(.)\\1+","$1"));
	}
	public static String read(String filePath) throws IOException {
		StringBuffer sbf = new StringBuffer();
		if (filePath.contains("~")) {
			return "";
		}
		try {
			System.out.println("filePath:" + filePath);
			// 这个构造函数从InputStream中加载Word文档。
			FileInputStream fis = new FileInputStream(filePath);
//			BufferedReader br = new BufferedReader(new InputStreamReader(fis, "gbk"));
//			String tmp = "";
//			while((tmp = br.readLine())!=null){
//				if(!tmp.equals("")) {
//					sbf.append(tmp);
//				}
//			}
//			br.close();
//			fis.close();
//			InputStream is = new ByteArrayInputStream(sbf.toString().getBytes());
//			HWPFDocument doc = new HWPFDocument(is);
			
			return read(fis);
		} catch (Exception e) {
			System.out.println("error:" + e.getMessage());
		} 
		return "";
	}

	public static String getContent(HWPFDocument doc) {
		String content = "";
		try {

			// String doc1 = doc.getDocumentText();
			// content = doc1;

			// StringBuilder doc2 = doc.getText();
			// content = doc2.toString();

			Range rang = doc.getRange();
			String doc3 = rang.text();
			//content = doc3.replaceAll("(\r)\\1+","$1").replace("\t", "").replace("\r", " ");
			content = doc3.trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	public String matchString(Pattern pa, String str) {

		Matcher ma = pa.matcher(str);
		if (ma.find()) {
			return str;
		}
		return null;
	}
	public static String read(InputStream in) throws IOException {
		HWPFDocument doc = new HWPFDocument(in);
		String content = getContent(doc);
		doc.close();
		return content;
	}
}
