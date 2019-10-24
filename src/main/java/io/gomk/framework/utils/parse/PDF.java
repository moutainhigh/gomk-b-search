package io.gomk.framework.utils.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDF {

	public static void main(String[] args) {
		String filePath = "/Users/vko/Documents/my-code/DOC/test/1.pdf";
		System.out.println(read(filePath));
		
		//String str="China|||||America::::::England&&&&&&&Mexica";
        //System.out.println(str.replaceAll("(.)\\1+","$1"));
	}
	public static String read(String filePath) {

		// 这个构造函数从InputStream中加载Word文档。
		String str = "";
		try {
			FileInputStream fis = new FileInputStream(filePath);
			str = read(fis);
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
			return "";
		}
		
		return str.trim();
	}
	public static String read(InputStream in) throws InvalidPasswordException, IOException {
		PDDocument helloDocument = PDDocument.load(in);
		PDFTextStripper textStripper = new PDFTextStripper();
		String str = textStripper.getText(helloDocument);
		helloDocument.close();
		return str;
	}
}
