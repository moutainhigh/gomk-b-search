package io.gomk.framework.utils.parse;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.tinkerpop.shaded.minlog.Log;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

import io.gomk.framework.utils.parsefile.ParseFile;

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
    
 // 获取PDF文本内容和图片及坐标
    public static void getImage(InputStream src, String target, int start, int end) throws IOException {
        PdfReader reader = new PdfReader(src);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        // 新建一个TestRenderListener对象，该对象实现了RenderListener接口，作为处理PDF的主要类
        HlhTextRenderListener listener = new HlhTextRenderListener();
        // 解析PDF，并处理里面的文字
        for (int i = start; i <= end; i++) {
            try {
				parser.processContent(i, listener);
			} catch (NullPointerException e) {
				Log.error("error :  pdf2img1" + e.getMessage());
				break;
				//e.printStackTrace();
			} catch (IOException e) {
				Log.error("error :  pdf2img2" + e.getMessage());
				break;
				//e.printStackTrace();
			}
            List<byte[]> arraysByte = listener.arraysByte;
            // 图片
            for (int j = 0; j < arraysByte.size(); j++) {
            	byte[] by = arraysByte.get(j);
                FileOutputStream out = new FileOutputStream(target + j + ".jpg");
                out.write(by);
                out.flush();
                out.close();
            }
        }
    }
    public static void main(String[] args) throws IOException, DocumentException {
    	String filePath = "/Users/vko/Documents/my-code/testDOC/tb/（259第一包电力电缆）投标文件—1.pdf";
    	String targetDir = "/Users/vko/Documents/my-code/testDOC/img/";
    	File f = new File(targetDir);
    	if (!f.exists()) {
    		f.mkdir();
    	}
    	File pdf = new File(filePath);
    	getImage(new FileInputStream(pdf), targetDir, 176, 177);
    	
    	
    	// int page = ParseFile.getPageIdCardContent(new FileInputStream(pdf));
       // parse(filePath, targetDir);
    	// getImage(new FileInputStream(pdf), targetDir, page, page+1);
    }

}