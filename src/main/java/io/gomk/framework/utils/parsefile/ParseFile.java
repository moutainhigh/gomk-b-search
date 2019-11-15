package io.gomk.framework.utils.parsefile;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import io.gomk.framework.utils.jython.RuntimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

//    /**
//     * 解析map获取投标资格，招标范围，技术要求，评标办法
//     */
//    public Map<String, StringBuilder> parseText(InputStream in, String extensionName) {
//        Map<String, StringBuilder> result = init();
//        Map<String, LinkedHashMap<Integer, String>> linkedHashMap = new LinkedHashMap<>();
//        if (DOC.equalsIgnoreCase(extensionName)) {
//            linkedHashMap = parseDoc(in, result);
//        } else if (DOCX.equalsIgnoreCase(extensionName)) {
//            linkedHashMap = parseDocx(in, result);
//        } else if (PDF.equalsIgnoreCase(extensionName)) {
//            linkedHashMap = parsePdf(in, result);
//        }
//
//        linkedHashMap.forEach((key, v) -> {
//            String k = key.replaceAll(" ", "");
//            for (Map.Entry<Integer, String> entry : v.entrySet()) {
//                findTenderQualification(result, k, entry);
//                findTenderScope(result, k, entry);
//                findTechnicalRequirement(result, k, entry);
//                findTenderMethod(result, k, entry);
//            }
//        });
//        return result;
//    }


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

    private Map<String, LinkedHashMap<Integer, String>> parsePdf(InputStream in, Map<String, StringBuilder> result, boolean readOne) {
        Map<String, LinkedHashMap<Integer, String>> contentByTitles = new LinkedHashMap<>(20);
        List<String> paras = getPdfFullContent(in);
        String[] paragraph = new String[paras.size()];
        String[] styles = new String[paras.size()];
        for (int i = 0; i < paras.size(); i++) {
            paragraph[i] = paras.get(i);
        }
        if (readOne) {
            generateResult(result, contentByTitles, paragraph, styles);
        } else {
            generateByWord(result, contentByTitles, paragraph, styles);
        }
        return contentByTitles;
    }

    private Map<String, LinkedHashMap<Integer, String>> parseDocx(InputStream in, Map<String, StringBuilder> result, boolean readOne) {
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
            if (readOne) {
                generateResult(result, contentByTitles, paragraph, styles);
            } else {
                generateByWord(result, contentByTitles, paragraph, styles);
            }
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
    private Map<String, LinkedHashMap<Integer, String>> parseDoc(InputStream in, Map<String, StringBuilder> result, boolean readOne) {
        Map<String, LinkedHashMap<Integer, String>> contentByTitles = new LinkedHashMap<>(20);
        try {
            WordExtractor wordExtractor = new WordExtractor(in);
            String[] paragraph = wordExtractor.getParagraphText();

            if (readOne) {
                generateResult(result, contentByTitles, paragraph, new String[0]);
            } else {
                generateByWord(result, contentByTitles, paragraph, new String[0]);
            }
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
        //log.info("{}", l);
        int totalParagraph = paragraph.length;
        List<Integer> fIndexList = new ArrayList<>();
        for (int i = 0; i < totalParagraph; i++) {
            String p = paragraph[i].trim();
            if (!"".equalsIgnoreCase(p)) {
                for (String s : ONE_TITLE) {
                    if (p.startsWith(s) && !p.contains("....")) {
//                        System.out.println("第-->" + i + "-->" + s);
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
            if (tIndexListSize == 1) {
                tIndexList.add(oneEnd);
                tIndexListSize++;
            }
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
                        System.out.println("-->" + twoC);
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

    private void generateResult(Map<String, StringBuilder> result, Map<String, LinkedHashMap<Integer, String>> contentByTitles, String[] paragraph, String[] styles) {
        int totalParagraph = paragraph.length;
        List<Integer> fIndexList = new ArrayList<>();
        for (int i = 0; i < totalParagraph; i++) {
            String p = paragraph[i].trim();
            if (!"".equalsIgnoreCase(p)) {
                for (String s : ONE_TITLE) {
                    if (p.startsWith(s) && !p.contains("....")) {
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
            int oneStart = fIndexList.get(i) + 1;
            i++;
            int oneEnd = fIndexList.get(i);
            LinkedHashMap<Integer, String> twoContent = new LinkedHashMap<>(50);

            for (int o = oneStart; o < oneEnd - 1; o++) {
                StringBuilder twoSb = new StringBuilder();
                String oneC = paragraph[o].trim().replaceAll(" ", "");
                if (!"".equals(oneC)) {
//                    System.out.println("-->" + oneC);
                    twoSb.append(oneC);
                    if (oneC.length() > 2) {
                        twoSb.append("\r");
                    }
                    twoContent.put(o, twoSb.toString());
                }
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
//        File file1 = new File("/Users/baibing6/Desktop/file/CSIEZB16020090.docx");
//        File file1 = new File("/Users/baibing6/Desktop/file/CSIEZB17020188.doc");
//        File file1 = new File("/Users/baibing6/Desktop/file/2018.doc");
        // File file1 = new File("/Users/baibing6/Desktop/file/招标文件正文.pdf");
//        File file1 = new File("/Users/baibing6/Desktop/file/tb5.pdf");
      //  File file1 = new File("/Users/vko/Documents/my-code/testDOC/气化炉砖-招标文件-CSIEZB160400524.doc");
        File file1 = new File("/Users/vko/Documents/my-code/testPDF/招标文件正文.pdf");
      // File file1 = new File("/Users/baibing6/Desktop/file/pdf/第二批电缆采购投标文件—5.pdf");
        try (
             InputStream in1 = new FileInputStream(file1);
             InputStream in2 = new FileInputStream(file1);
             InputStream in3 = new FileInputStream(file1);
             InputStream in4 = new FileInputStream(file1);
        	 InputStream in5 = new FileInputStream(file1);
        ) {
//            Map<String, StringBuilder> map = new ParseFile().parseText(in1, DOC);
//            List<String> lst = new ParseFile().parseTenderQualification(in1, DOC);
//            String a0 = new ParseFile().parseTenderScope(in2, DOC);
//            String a1 = new ParseFile().parseTechnicalRequirement(in3, DOCX);
//            String a2 = new ParseFile().parseTenderMethod(in4, DOC);
           // List<String> lst = new ParseFile().parseTenderQualification(in1, PDF);
           // String a0 = new ParseFile().parseTenderScope(in2, PDF);
            String a1 = new ParseFile().parseTechnicalRequirement(in3, PDF);
//            String a2 = new ParseFile().parseTenderMethod(in4, PDF);
//            List<LinkedHashMap<Integer, String>> list = new ParseFile().parsePdfTable(in5);
//            String a = "[['序号', '材料名称', '规格型号', '生产厂家', '数量', '单价', '总价'], ['1', '矿用聚乙烯绝缘\\n编织屏蔽通讯电\\n缆', 'MHYVR\\n1×4×7/0.43mm', '天津万博', '10000.0', '3.5', '34800.0'], ['', '矿用聚乙烯绝缘\\n编织屏蔽通讯电\\n缆', 'MHYVR\\n1×4×7/0.43mm', '天津万博', '10000.0', '3.5', '34800.0'], ['', '矿用通信电缆', 'MHYV\\n4×2×7/0.37mm', '天津万博', '5000.0', '5.0', '25200.0'], ['', '矿用聚氯乙烯绝\\n缘通信电缆', 'MHYVP\\n1×4×7/0.52mm', '天津万博', '170200.\\n0', '6.8', '1159062.0'], ['', '矿用聚氯乙烯绝\\n缘通信电缆', 'MHYVP\\n1×4×1/1.38mm', '天津万博', '76800.0', '5.9', '453888.0'], ['', '矿用通信软线', 'MHYV\\n10×2×0.75mm\\n10×2×1/0.97m\\nm', '天津万博', '90200.0', '11.0', '989494.0'], ['', '矿用通信软线', 'MHYV\\n5×2×0.75mm\\nMHYV\\n5×2×1/0.97mm', '天津万博', '99000.0', '6.0', '593010.0'], ['', '矿用聚乙烯绝缘\\n铝聚乙烯粘结护\\n层通信电缆', 'MHYAV\\n10×2×0.8mm', '天津万博', '800.0', '11.0', '8808.0'], ['2', '运输至最终目的地运杂费、保险费、卸车费等', '', '', '', '', '0.0'], ['3', '其它', '', '', '', '', '0.0'], ['4', '投标总价\\n3299062.0', '', '', '', '', '']]";
//            new ParseFile().parsePythonCmd(a);
            
//            List<LinkedHashMap<Integer, String>> list = new ParseFile().parsePdfTable(in5);
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
        linkedHashMap = getStringLinkedHashMapMap(in, extensionName, result, linkedHashMap, false);
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
        String t3 = "以下条件";
        String t4 = "以下资格条件";
        for (String s : infos) {
        	if (!s.contains(t1) && !s.contains(t2) && !s.contains(t3) && !s.contains(t4)) {
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
        linkedHashMap = getStringLinkedHashMapMap(in, extensionName, result, linkedHashMap, false);

        linkedHashMap.forEach((k, v) -> {
            for (Map.Entry<Integer, String> entry : v.entrySet()) {
                findTenderScope(result, k, entry);
            }
        });
        return result.get("2").toString();
    }

    private Map<String, LinkedHashMap<Integer, String>> getStringLinkedHashMapMap(InputStream in, String extensionName,
                                                                                  Map<String, StringBuilder> result,
                                                                                  Map<String, LinkedHashMap<Integer, String>> linkedHashMap,
                                                                                  boolean readOne) {
        if (DOC.equalsIgnoreCase(extensionName)) {
            linkedHashMap = parseDoc(in, result, readOne);
        } else if (DOCX.equalsIgnoreCase(extensionName)) {
            linkedHashMap = parseDocx(in, result, readOne);
        } else if (PDF.equalsIgnoreCase(extensionName)) {
            linkedHashMap = parsePdf(in, result, readOne);
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
        linkedHashMap = getStringLinkedHashMapMap(in, extensionName, result, linkedHashMap, false);

        linkedHashMap.forEach((k, v) -> {
            for (Map.Entry<Integer, String> entry : v.entrySet()) {
                findTechnicalRequirement(result, k, entry);
            }
        });

        if (StringUtils.isEmpty(result.get("3").toString())) {
            linkedHashMap = getStringLinkedHashMapMap(in, extensionName, result, linkedHashMap, false);
            linkedHashMap.forEach((k, v) -> {
                for (Map.Entry<Integer, String> entry : v.entrySet()) {
                    findTechnicalRequirement(result, k, entry);
                }
            });
        }
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
        linkedHashMap = getStringLinkedHashMapMap(in, extensionName, result, linkedHashMap, true);

        if (linkedHashMap.entrySet().isEmpty()) {
            linkedHashMap = getStringLinkedHashMapMap(in, extensionName, result, linkedHashMap, false);
        }
        linkedHashMap.forEach((k, v) -> {
            for (Map.Entry<Integer, String> entry : v.entrySet()) {
                findTenderMethod(result, k, entry);
            }
        });
        return result.get("4").toString();
    }

    public List<LinkedHashMap<Integer, String>> parsePdfTable(InputStream in) throws IOException {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		byte[] buffer = new byte[1024];  
		int len;  
		while ((len = in.read(buffer)) > -1 ) {  
			baos.write(buffer, 0, len);  
		}  
		baos.flush(); 
        List<LinkedHashMap<Integer, String>> list = null;
        try {
           
            int page = getPdfContent(new ByteArrayInputStream(baos.toByteArray()));
            log.info("分项报价所在页：" + page);
            list = readByPython(new ByteArrayInputStream(baos.toByteArray()), page + "");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private int getPdfContent(ByteArrayInputStream byteArrayInputStream) {
        int page = 0;
        PdfReader reader = null;
        try {
            reader = new PdfReader(byteArrayInputStream);
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);
            int num = reader.getNumberOfPages();

            TextExtractionStrategy strategy;
            for (int i = 1; i <= num; i++) {
                strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
                String text = strategy.getResultantText().replaceAll(" ", "");

                if (text.contains("投标分项报价表") && text.contains("材料名称") && text.contains("规格型号")) {
                    page = i;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return page;
    }
    

	public static int getPageIdCardContent(InputStream in) {
		String card3 = "身份证复印件";
		int page = 0;
		PdfReader reader = null;
		try {
			reader = new PdfReader(in);
			PdfReaderContentParser parser = new PdfReaderContentParser(reader);
			int num = reader.getNumberOfPages();

			TextExtractionStrategy strategy;
			for (int i = 1; i <= num; i++) {
				strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
				String text = strategy.getResultantText().replaceAll(" ", "");

				if (text.contains(card3)) {
					page = i;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return page;
	}

    /**
     * 调用python文件
     *
     * @param byteArrayInputStream pdf文件地址
     * @param page 要解析的页数
     */
    private List<LinkedHashMap<Integer, String>> readByPython(ByteArrayInputStream byteArrayInputStream, String page) throws IOException {
        StringBuilder sb = new StringBuilder();
        //String filePath = DIR + "read_table.py";
        
        InputStream initialStream  = RuntimeUtils.class.getClassLoader().getResourceAsStream("python/read_table.py");
		String projectPath = System.getProperty("user.dir");
		File targetFile = new File(projectPath + "/read_table.py");
		File tempPDF = new File(projectPath + "/test.pdf");
		FileUtils.copyInputStreamToFile(initialStream, targetFile);
		FileUtils.copyInputStreamToFile(byteArrayInputStream, tempPDF);
		
        String[] args1 = new String[]{"python", targetFile.getAbsolutePath(), tempPDF.getAbsolutePath(), page};
        Process proc = Runtime.getRuntime().exec(args1);

        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "utf-8"));
        String line;
        while ((line = in.readLine()) != null && StringUtils.isNotBlank(line)) {
        	//log.info("decodeUnicode:" + decodeUnicode(line));
            sb.append(line);
        }
     // 获取异常输出流

		BufferedReader ine = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

		String linee = null;

		while ((linee = ine.readLine()) != null) {

			log.error("===Contrast proc error===" +linee);

		}
		
		in.close();
		
        return parsePythonCmd(sb.toString());
    }

    /**
     * 解析python命令行内容
     *
     * @param str 命令行输出内容
     * @return list
     */
    private List<LinkedHashMap<Integer, String>> parsePythonCmd(String str) {
        List<LinkedHashMap<Integer, String>> lst = new ArrayList<>();
        if (str.length() < 100) {
            return lst;
        }
        String[] d = str.split("],");
        for (int i = 1; i < d.length - 1; i++) {
            String[] e = d[i].split(",");
            if (e.length < 6) {
                continue;
            }
            LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
            map.putIfAbsent(1, e[1]);
            map.putIfAbsent(2, e[2]);
            map.putIfAbsent(3, e[3]);
            map.putIfAbsent(4, e[4]);
            map.putIfAbsent(5, e[5]);
            map.putIfAbsent(6, e[6]);
            lst.add(map);
        }
        return lst;
    }
}

