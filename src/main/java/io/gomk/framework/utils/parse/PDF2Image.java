package io.gomk.framework.utils.parse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.events.IndexEvents.Entry;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

/**
 * 提取PDF中的图片
 * 
 * **/

public class PDF2Image {
	// 获取PDF文本内容和图片及坐标
    public static void parse(String src, String target) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        // 新建一个TestRenderListener对象，该对象实现了RenderListener接口，作为处理PDF的主要类
        HlhTextRenderListener listener = new HlhTextRenderListener();
        // 解析PDF，并处理里面的文字
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            parser.processContent(i, listener);
            // 获取文字的矩形边框
          //  List<Rectangle2D.Float> rectText = listener.rectText;
           // List<String> textList = listener.textList;
           // List<Float> listY = listener.listY;
           /// List<Map<String, Rectangle2D.Float>> list_text = listener.row_text_rect;
            List<byte[]> arraysByte = listener.arraysByte;
            // 图片
            for (int j = 0; j < arraysByte.size(); j++) {
            	byte[] by = arraysByte.get(j);
                FileOutputStream out = new FileOutputStream(target + j + ".jpg");
                out.write(by);
                out.flush();
                out.close();
            }
//            // 文本
//            for (int j = 0; j < list_text.size(); j++) {
//                Map<String, Rectangle2D.Float> map = list_text.get(j);
//                System.out.println("list_text:" + list_text.size());
//                System.out.println("map:" + map.size());
//                for (java.util.Map.Entry<String, com.itextpdf.awt.geom.Rectangle2D.Float> entry : map.entrySet()) {
//                    System.out.println(i + ":" + entry.getKey() + "::" + entry.getValue().y);
//                }
//            }
        }
//        TextExtractionStrategy text = new SimpleTextExtractionStrategy();
//        String t = PdfTextExtractor.getTextFromPage(reader, 1, text);
//        Rectangle rec = reader.getBoxSize(1, "trim");
    }
    public static void main(String[] args) throws IOException, DocumentException {
    	String filePath = "/Users/vko/Documents/my-code/testDOC/tb/（259第一包电力电缆）投标文件—1.pdf";
    	String targetDir = "/Users/vko/Documents/my-code/testDOC/img/";
    	File f = new File(targetDir);
    	if (!f.exists()) {
    		f.mkdir();
    	}
        parse(filePath, targetDir);
    }

}