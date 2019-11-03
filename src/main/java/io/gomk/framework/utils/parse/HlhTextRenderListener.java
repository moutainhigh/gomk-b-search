package io.gomk.framework.utils.parse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.awt.geom.RectangularShape;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
 
/**
 * Description 使用Itext 5 获取PDF中文字和图片的解析类<br/>
 * date:<br/>
 * 
 * @author 
 * @version
 * @see
 * @param
 */
public class HlhTextRenderListener implements RenderListener {
    // 用来存放文字的矩形
    List<Rectangle2D.Float> rectText = new ArrayList<Rectangle2D.Float>();
 
    // 用来存放文字
    List<String> textList = new ArrayList<String>();
 
    // 用来存放文字的Y坐标
    List<Float> listY = new ArrayList<Float>();
 
    // 用来存放每一行文字的坐标位置
    List<Map<String, Rectangle2D.Float>> row_text_rect = new ArrayList<Map<String, Rectangle2D.Float>>();
 
    // 图片坐标
    List<float[]> arrays = new ArrayList<float[]>();
 
    // 图片
    List<byte[]> arraysByte = new ArrayList<byte[]>();
 
    // PDF文件的路径
    protected String filePath = null;
 
    public HlhTextRenderListener() {
    }
 
    // 2 遇到"BT"执行
    @Override
    public void beginTextBlock() {
        // TODO Auto-generated method stub
 
    }
 
    /**
     * 3 文字主要处理方法
     */
    @Override
    public void renderText(TextRenderInfo renderInfo) {
        String text = renderInfo.getText();
        if (text.length() > 0) {
            RectangularShape rectBase = renderInfo.getBaseline().getBoundingRectange();
            // 获取文字下面的矩形
            Rectangle2D.Float rectAscen = renderInfo.getAscentLine().getBoundingRectange();
            // 计算出文字的边框矩形
            float leftX = (float) rectBase.getMinX();
            float leftY = (float) (rectBase.getMinY() - 1);
            float rightX = (float) rectBase.getMaxX();
            float rightY = (float) (rectBase.getMaxY() - 1);
            Rectangle r = rectBase.getBounds();
            // System.out.println("float:" + leftX + ":" + leftY + ":" + rightX
            // + ":" + rightY);
            Rectangle2D.Float rect = new Rectangle2D.Float(leftX, leftY, rightX - leftX, rightY - leftY);
            // System.out.println("text:" + text + "X:" + rect.x + "Y:" + rect.y
            // + "width:" + rect.width + "height:"
            // + rect.height);
            if (listY.contains(rect.y)) {
                int index = listY.indexOf(rect.y);
                float tempx = rect.x > rectText.get(index).x ? rectText.get(index).x : rect.x;
                rectText.set(index, new Rectangle2D.Float(tempx, rect.y, rect.width + rectText.get(index).width,
                        rect.height));
                textList.set(index, textList.get(index) + text);
            } else {
                rectText.add(rect);
                textList.add(text);
                listY.add(rect.y);
            }
            Map<String, Rectangle2D.Float> map = new HashMap<String, Rectangle2D.Float>();
            map.put(text, rect);
            row_text_rect.add(map);
        }
    }
 
    // 4 最后执行 只执行一次 ，遇到"ET"执行
    @Override
    public void endTextBlock() {
        // TODO Auto-generated method stub
 
    }
 
    // 1 图片处理方法
    @Override
    public void renderImage(ImageRenderInfo renderInfo) {
        PdfImageObject image0;
        try {
            image0 = renderInfo.getImage();
            byte[] imageByte = image0.getImageAsBytes();
            Image imageInPDF;
            imageInPDF = Image.getInstance(imageByte);
            if (image0 != null && imageInPDF.equals(image0)) {
                float[] resu = new float[3];
                // 0=>x;1=>y;2=>z;
                // z的值始终未1
                resu[0] = renderInfo.getStartPoint().get(0);
                resu[1] = renderInfo.getStartPoint().get(1);
                resu[2] = 1;
                arrays.add(resu);
            }
            byte[] by = image0.getImageAsBytes();
            arraysByte.add(by);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}