package io.gomk.framework.utils.parse;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class Word2007 {

	public static String read(String filePath) {

		String str = "";
		try {

			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			XWPFDocument xdoc = new XWPFDocument(fis);
			XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
			str = extractor.getText();
			fis.close();
			extractor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
