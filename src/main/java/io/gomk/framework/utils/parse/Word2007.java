package io.gomk.framework.utils.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Word2007 {

	public static String read(String filePath) {

		String str = "";
		try {

			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			str = read(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//str = str.replaceAll("(\r)\\1+","$1").replace("\t", "").replace("\r", " ");
		return str;
	}

	public static String read(InputStream fis) {
		XWPFDocument xdoc;
		try {
			xdoc = new XWPFDocument(fis);
			XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
			String str = extractor.getText();
			fis.close();
			extractor.close();
			return str;
		} catch (IOException e) {
			log.info("word2007-error:"+e.getMessage());
		}
		
		return "";
	}
}
