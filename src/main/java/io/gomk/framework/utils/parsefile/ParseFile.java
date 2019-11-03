package io.gomk.framework.utils.parsefile;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 解析文件生成es需要的信息
 *
 * @author baibing
 * @version 1.0
 * @date 2019-10-21
 */
@Slf4j
@Component
public class ParseFile {


    private final static String DOC = "doc";
    private final static String DOCX = "docx";
    private final static String PDF = "pdf";

    public final static List<String> ONE_TITLE = Arrays.asList(
            "第一章", "第二章", "第三章", "第四章", "第五章",
            "第六章", "第七章", "第八章", "第九章", "第十章",
            "第十一章", "第十二章", "第十三章", "第十四章", "第十五章");

    /**
     * 解析map获取投标资格，招标范围，技术要求，评标办法
     */
    public Map<String, StringBuilder> parseText(InputStream in, String extensionName) {
        Map<String, StringBuilder> result = init();
        Map<String, LinkedHashMap<Integer, String>> linkedHashMap = new LinkedHashMap<>();
        if (DOC.equalsIgnoreCase(extensionName)) {
            linkedHashMap = parseDoc(in, result);
        } else if (DOCX.equalsIgnoreCase(extensionName)) {
            linkedHashMap = parseDocx(in, result);
        } else if (PDF.equalsIgnoreCase(extensionName)) {
            linkedHashMap = parsePdf(in, result);
        }

        linkedHashMap.forEach((key, v) -> {
            String k = key.replaceAll(" ", "");
            for (Map.Entry<Integer, String> entry : v.entrySet()) {
                findTenderQualification(result, k, entry);
                findTenderScope(result, k, entry);
                findTechnicalRequirement(result, k, entry);
                findTenderMethod(result, k, entry);
            }
        });
        return result;
    }


    private void findTenderQualification(Map<String, StringBuilder> result, String firstTitle, Map.Entry<Integer, String> entry) {
        StringBuilder sb = new StringBuilder();
        String t1 = "投标资格";
        String t2 = "投标人资格";
        if (firstTitle.contains(t1) || firstTitle.contains(t2)) {
            sb.append(entry.getValue());
            result.put("1", sb);
        } else if (entry.getValue().contains(t1) || entry.getValue().contains(t2)) {
            sb.append(result.get("1")).append(entry.getValue());
            result.get("1").append(sb);
        }
    }

    private void findTenderScope(Map<String, StringBuilder> result, String firstTitle, Map.Entry<Integer, String> entry) {
        StringBuilder sb = new StringBuilder();
        String t1 = "招标范围";
        String t2 = "招标内容";
        String value = entry.getValue();
        String[] vs = value.split("&nbsp;");
        if (firstTitle.contains(t1) || firstTitle.contains(t2)) {
            sb.append(value.replaceAll("&nbsp;", ""));
            result.put("2", sb);
        } else if (vs.length > 1 && (vs[0].contains(t1) || vs[0].contains(t2))) {
            sb.append(result.get("2")).append(value.replaceAll("&nbsp;", ""));
            result.get("2").append(sb);
        }
    }

    private void findTechnicalRequirement(Map<String, StringBuilder> result, String firstTitle, Map.Entry<Integer, String> entry) {
        StringBuilder sb = new StringBuilder();
        String t1 = "技术要求";
        String t2 = "技术规格";
        String value = entry.getValue();
        String[] vs = value.split("&nbsp;");
        if (firstTitle.contains(t1) || firstTitle.contains(t2)) {
            sb.append(result.get("3")).append(value.replaceAll("&nbsp;", ""));
            result.put("3", sb);
        } else if (vs.length > 1 && (vs[0].contains(t1) || vs[0].contains(t2))) {
            sb.append(result.get("3")).append(value.replaceAll("&nbsp;", ""));
            result.get("3").append(sb);
        }

    }

    private void findTenderMethod(Map<String, StringBuilder> result, String firstTitle, Map.Entry<Integer, String> entry) {
        StringBuilder sb = new StringBuilder();
        String t1 = "评标办法";
        String t2 = "评标程序及方法";
        String t3 = "评标标准";
        String value = entry.getValue();
        String[] vs = value.split("&nbsp;");
        if (firstTitle.contains(t1) || firstTitle.contains(t2) || firstTitle.contains(t3)) {
            sb.append(result.get("4")).append(value.replaceAll("&nbsp;", ""));
            result.put("4", sb);
        }
        if (vs.length > 1 && (vs[0].contains(t1) || vs[0].contains(t2) || vs[0].contains(t3))) {
            sb.append(result.get("4")).append(value.replaceAll("&nbsp;", ""));
            result.get("4").append(sb);
        }
    }


    private Map<String, StringBuilder> init() {
        Map<String, StringBuilder> map = new LinkedHashMap<>(5);
        map.put("1", new StringBuilder());
        map.put("2", new StringBuilder());
        map.put("3", new StringBuilder());
        map.put("4", new StringBuilder());
        return map;
    }

    private Map<String, LinkedHashMap<Integer, String>> parsePdf(InputStream in, Map<String, StringBuilder> result) {
        Map<String, LinkedHashMap<Integer, String>> contentByTitles = new LinkedHashMap<>(20);
        List<String> paras = getPdfFullContent(in);
        String[] paragraph = new String[paras.size()];
        String[] styles = new String[paras.size()];
        for (int i = 0; i < paras.size(); i++) {
            paragraph[i] = paras.get(i);
        }
        generateByWord(result, contentByTitles, paragraph, styles);
        return contentByTitles;
    }

    private Map<String, LinkedHashMap<Integer, String>> parseDocx(InputStream in, Map<String, StringBuilder> result) {
        Map<String, LinkedHashMap<Integer, String>> contentByTitles = new LinkedHashMap<>(20);
        try (XWPFDocument doc = new XWPFDocument(in)) {
            List<XWPFParagraph> paras = doc.getParagraphs();
            String[] paragraph = new String[paras.size()];
            String[] styles = new String[paras.size()];
            for (int i = 0; i < paras.size(); i++) {
                String style = Optional.ofNullable(paras.get(i).getStyle()).orElse("-1000");
                paragraph[i] = paras.get(i).getText();
                styles[i] = style;
//                System.out.println("第"+i+"段====>"+paragraph[i]);
            }
            generateByWord(result, contentByTitles, paragraph, styles);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentByTitles;
    }

    /**
     * 解析doc文件内容
     *
     * @return map
     */
    private Map<String, LinkedHashMap<Integer, String>> parseDoc(InputStream in, Map<String, StringBuilder> result) {
        Map<String, LinkedHashMap<Integer, String>> contentByTitles = new LinkedHashMap<>(20);
        try {
            WordExtractor wordExtractor = new WordExtractor(in);
            String[] paragraph = wordExtractor.getParagraphText();

            generateByWord(result, contentByTitles, paragraph, new String[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentByTitles;
    }


    private List<String> getPdfFullContent(InputStream in) {
        List<String> list = new ArrayList<>();
        PdfReader reader = null;
        try {
            reader = new PdfReader(in);
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);
            // 获得页数
            int num = reader.getNumberOfPages();
            TextExtractionStrategy strategy;
            for (int i = 1; i <= num; i++) {
                strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
                String text = strategy.getResultantText().trim();
                String[] ts = text.split("\n");
                for (int j = 0; j < ts.length; j++) {
                    if (!"".equalsIgnoreCase(ts[j])) {
                        list.add(ts[j]);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return list;
    }


    private void generateByWord(Map<String, StringBuilder> result, Map<String, LinkedHashMap<Integer, String>> contentByTitles, String[] paragraph, String[] styles) {
        List<String> l = Arrays.asList(paragraph);
        log.info("{}", l);
        int totalParagraph = paragraph.length;
        List<Integer> fIndexList = new ArrayList<>();
        for (int i = 0; i < totalParagraph; i++) {
            String p = paragraph[i].trim();
            if (!"".equalsIgnoreCase(p)) {
                for (String s : ONE_TITLE) {
                    if (p.startsWith(s) && !p.contains("....")) {
                        System.out.println("第-->" +i + "-->" + s);
                        fIndexList.add(i);
                    }
                }
            }
        }
        //增加最后一段
        fIndexList.add(totalParagraph);

        int fIndexListSize = fIndexList.size();
        int i = 0;

        while (i < fIndexListSize - 1 && i < totalParagraph) {
            int oneStart = fIndexList.get(i);
            i++;
            int oneEnd = fIndexList.get(i);

            LinkedHashMap<Integer, String> twoContent = new LinkedHashMap<>(50);
            List<Integer> tIndexList = new ArrayList<>();
            boolean hasStyles = styles.length > 0;
            for (int o = oneStart; o < oneEnd; o++) {

                String oneC = paragraph[o].trim().replaceAll(" ", "");
//                System.out.println("段落="+o+"==>"+oneC);

//                if (oneC.contains("招标范围")) {
//                    log.info("");
//                }
                if (!"".equals(oneC)) {
                    if (matchTwoTitle(oneC)) {
//                        System.out.println("第"+o+"段"+oneC);
                        tIndexList.add(o);
                    }
                    if (hasStyles && "aa".equalsIgnoreCase(styles[o])) {
                        tIndexList.add(o);
                    }
                }
            }

//            if (tIndexList.isEmpty()) {
//                for (int o = oneStart; o < oneEnd; o++) {
//                    String oneC = paragraph[o].trim();
//                    if (!"".equals(oneC) && oneC.length() > 2) {
////                        System.out.println("段落="+o+"==>"+oneC);
//                        tIndexList.add(o);
//                    }
//                }
//            }

            int tIndexListSize = tIndexList.size();
            int j = 0;
            while (j < tIndexListSize - 1 && j < totalParagraph) {
                int twoStart = tIndexList.get(j);
                j++;
                int twoEnd = tIndexList.get(j);

                StringBuilder twoSb = new StringBuilder();
                for (int t = twoStart; t < twoEnd; t++) {
                	String twoC = paragraph[t].trim();
                    //String twoC = paragraph[t].trim().replaceAll("[^\u4e00-\u9fa5]", "");
                    if (!"".equals(twoC)) {
                        System.out.println("-->"+twoC);
                        twoSb.append(twoC);
                        if (t == twoStart) {
                            twoSb.append("&nbsp;");
                        }
                        if (twoC.length() > 2) {
                            twoSb.append("\r");
                        }
                    }
                }
                twoContent.putIfAbsent(j, twoSb.toString());
            }

            contentByTitles.putIfAbsent(paragraph[fIndexList.get(i - 1)], twoContent);
        }
    }

    /**
     * 判断二级标题正则，如果遇到其他版本则需要修改
     *
     * @param twoTitle 二级标题
     * @return 是否二级标题
     */
    private boolean matchTwoTitle(String twoTitle) {
        return twoTitle.matches("^([0-9]{1,}[.\\s\\u4e00-\\u9fa5]{4,10}$)$");

    }

    /**
     * 调用示例
     *
     * @param args
     */
    public static void main(String[] args) {
//        File file1 = new File("/Users/baibing6/Desktop/CSIEZB16020090.docx");
//        File file1 = new File("/Users/baibing6/Desktop/CSIEZB17020188.doc");
//        File file1 = new File("/Users/baibing6/Desktop/2018.doc");
       // File file1 = new File("/Users/baibing6/Desktop/file/CEZB190205192.pdf");
        File file1 = new File("/Users/vko/Documents/my-code/testDOC/内蒙古国华呼伦贝尔发电有限公司2017-2018年全厂专职消防队服务项目招标文件.doc");
        
        try (InputStream in1 = new FileInputStream(file1);
             InputStream in2 = new FileInputStream(file1);
             InputStream in3 = new FileInputStream(file1);
             InputStream in4 = new FileInputStream(file1);
        ) {
//            Map<String, StringBuilder> map = new ParseFile().parseText(in1, DOC);
            List<String> lst = new ParseFile().parseTenderQualification(in1, DOC);
            String a0 = new ParseFile().parseTenderScope(in2, DOC);
            String a1 = new ParseFile().parseTechnicalRequirement(in3, DOC);
            String a2 = new ParseFile().parseTenderMethod(in4, DOC);
//            List<String> lst = new ParseFile().parseTenderQualification(in1, PDF);
//            String a0 = new ParseFile().parseTenderScope(in2, PDF);
//            String a1 = new ParseFile().parseTechnicalRequirement(in3, PDF);
//            String a2 = new ParseFile().parseTenderMethod(in4, PDF);
            log.info("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取投标资格List
     *
     * @param in            文件流
     * @param extensionName 扩展名
     * @return list
     */
    public List<String> parseTenderQualification(InputStream in, String extensionName) {
        Map<String, StringBuilder> result = init();
        Map<String, LinkedHashMap<Integer, String>> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap = getStringLinkedHashMapMap(in, extensionName, result, linkedHashMap);
        String t1 = "投标资格";
        String t2 = "投标人资格";
        StringBuilder sb = new StringBuilder();

        boolean breakEntry = false;
        for (Map.Entry<String, LinkedHashMap<Integer, String>> oneEntry : linkedHashMap.entrySet()) {
            String k = oneEntry.getKey();
            LinkedHashMap<Integer, String> v = oneEntry.getValue();
            for (Map.Entry<Integer, String> entry : v.entrySet()) {
                if (k.contains(t1) || k.contains(t2)) {
                    sb.append(entry.getValue().replaceAll("&nbsp;", ""));
                } else if (entry.getValue().contains(t1) || entry.getValue().contains(t2)) {
                    sb.append(entry.getValue().replaceAll("&nbsp;", ""));
                    breakEntry = true;
                    break;
                }
            }
            if (breakEntry) {
                break;
            }

        }
        
        List<String> infos = Arrays.asList(sb.toString().split("\r"));
        List<String> results = new ArrayList<>();
        String t3 = "满足以下条件";
        for (String s : infos) {
        	if (!s.contains(t1) && !s.contains(t2) && !s.contains(t3)) {
        		results.add(s.replaceFirst("^+\\s*\\d+.\\d*", ""));
        	}
        }
        
        return results;
    }

    /**
     * 获取招标范围
     *
     * @param in            文件流
     * @param extensionName 扩展名
     * @return string
     */
    public String parseTenderScope(InputStream in, String extensionName) {
        Map<String, StringBuilder> result = init();
        Map<String, LinkedHashMap<Integer, String>> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap = getStringLinkedHashMapMap(in, extensionName, result, linkedHashMap);

        linkedHashMap.forEach((k, v) -> {
            for (Map.Entry<Integer, String> entry : v.entrySet()) {
                findTenderScope(result, k, entry);
            }
        });
        return result.get("2").toString();
    }

    private Map<String, LinkedHashMap<Integer, String>> getStringLinkedHashMapMap(InputStream in, String extensionName, Map<String, StringBuilder> result, Map<String, LinkedHashMap<Integer, String>> linkedHashMap) {
        if (DOC.equalsIgnoreCase(extensionName)) {
            linkedHashMap = parseDoc(in, result);
        } else if (DOCX.equalsIgnoreCase(extensionName)) {
            linkedHashMap = parseDocx(in, result);
        } else if (PDF.equalsIgnoreCase(extensionName)) {
            linkedHashMap = parsePdf(in, result);
        }
        return linkedHashMap;
    }

    /**
     * 获取技术要求
     *
     * @param in            文件流
     * @param extensionName 扩展名
     * @return string
     */
    public String parseTechnicalRequirement(InputStream in, String extensionName) {
        Map<String, StringBuilder> result = init();
        Map<String, LinkedHashMap<Integer, String>> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap = getStringLinkedHashMapMap(in, extensionName, result, linkedHashMap);

        linkedHashMap.forEach((k, v) -> {
            for (Map.Entry<Integer, String> entry : v.entrySet()) {
                findTechnicalRequirement(result, k, entry);
            }
        });
        return result.get("3").toString();
    }

    /**
     * 获取评标办法
     *
     * @param in            文件流
     * @param extensionName 扩展名
     * @return string
     */
    public String parseTenderMethod(InputStream in, String extensionName) {
        Map<String, StringBuilder> result = init();
        Map<String, LinkedHashMap<Integer, String>> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap = getStringLinkedHashMapMap(in, extensionName, result, linkedHashMap);

        linkedHashMap.forEach((k, v) -> {
            for (Map.Entry<Integer, String> entry : v.entrySet()) {
                findTenderMethod(result, k, entry);
            }
        });
        return result.get("4").toString();
    }
}

