package io.gomk.framework.utils.parse;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDF {

	public String read(String filePath) {

		// 这个构造函数从InputStream中加载Word文档。
		PDDocument helloDocument = null;
		String str = "";
		try {
			helloDocument = PDDocument.load(new File(filePath));
			PDFTextStripper textStripper = new PDFTextStripper();
			str = textStripper.getText(helloDocument);
			helloDocument.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
}
