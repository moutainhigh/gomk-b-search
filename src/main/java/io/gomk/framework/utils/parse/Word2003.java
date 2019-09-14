package io.gomk.framework.utils.parse;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

public class Word2003 {

	public static String read(String filePath) throws IOException {

		// 这个构造函数从InputStream中加载Word文档。
		FileInputStream fis = new FileInputStream(filePath);
		HWPFDocument doc = new HWPFDocument(fis);
		try {

			return getContent(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		doc.close();
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
			content = doc3;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return content.replaceAll("", "");
	}

	public String matchString(Pattern pa, String str) {

		Matcher ma = pa.matcher(str);
		if (ma.find()) {
			return str;
		}
		return null;
	}
}
