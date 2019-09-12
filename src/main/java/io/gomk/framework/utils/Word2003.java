package io.gomk.framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.model.StyleDescription;
import org.apache.poi.hwpf.model.StyleSheet;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;

public class Word2003 {

	public String read(String filePath) throws IOException {

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

	public String getContent(HWPFDocument doc) {
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
