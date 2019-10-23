package io.gomk.framework.utils.parsefile;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.extractor.WordExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析文件生成es需要的信息
 *
 * @author baibing
 * @version 1.0
 * @date 2019-10-21
 */
@Slf4j
public class ParseFile {


    private final static String DOC = "doc";
    private final static String DOCX = "docx";
    private final static String PDF = "pdf";

    /**
     * 解析map获取投标资格，招标范围，技术要求，评标办法
     */
    public Map<String, StringBuilder> parseText(InputStream in, String extensionName) {
        Map<String, StringBuilder> result = init();

        if (DOC.equalsIgnoreCase(extensionName)) {
            Map<String, LinkedHashMap<Integer, String>> linkedHashMap = parseDoc(in, result);
            linkedHashMap.forEach((k, v) -> {
                for (Map.Entry<Integer, String> entry : v.entrySet()) {
                    findTenderQualification(result, k, entry);
                    findTenderScope(result, k, entry);
                    findTechnicalRequirement(result, k, entry);
                    findTenderMethod(result, k, entry);
                }
            });
        }
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

        if (firstTitle.contains(t1) || firstTitle.contains(t2)) {
            sb.append(entry.getValue());
            result.put("2", sb);
        } else if (entry.getValue().contains(t1) || entry.getValue().contains(t2)) {
            sb.append(result.get("2")).append(entry.getValue());
            result.get("2").append(sb);
        }
    }

    private void findTechnicalRequirement(Map<String, StringBuilder> result, String firstTitle, Map.Entry<Integer, String> entry) {
        StringBuilder sb = new StringBuilder();
        String t1 = "技术要求";
        if (firstTitle.contains(t1)) {
            sb.append(entry.getValue());
            result.put("3", sb);
        } else if (entry.getValue().contains(t1)) {
            sb.append(result.get("3")).append(entry.getValue());
            result.get("3").append(sb);
        }

    }

    private void findTenderMethod(Map<String, StringBuilder> result, String firstTitle, Map.Entry<Integer, String> entry) {
        StringBuilder sb = new StringBuilder();
        String t1 = "评标办法";
        if (firstTitle.contains(t1)) {
            sb.append(entry.getValue());
            result.put("4", sb);
            ;
        }
        if (entry.getValue().contains(t1)) {
            sb.append(result.get("4")).append(entry.getValue());
            result.get("4").append(sb);
        }
    }


    private Map<String, StringBuilder> init() {
        Map<String, StringBuilder> map = new LinkedHashMap<>(5);
        map.put("0", new StringBuilder());
        map.put("1", new StringBuilder());
        map.put("2", new StringBuilder());
        map.put("3", new StringBuilder());
        map.put("4", new StringBuilder());
        return map;
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
            int totalParagraph = paragraph.length;
            List<Integer> fIndexList = new ArrayList<>();
            for (int i = 0; i < totalParagraph; i++) {
                String p = paragraph[i].trim();
                if (!"".equalsIgnoreCase(p)) {
                    for (String s : ONE_TITLE) {
                        if (p.startsWith(s)) {
                            fIndexList.add(i);
                        }
                    }
                    result.get("0").append(p);
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
                List<Integer> tIndexList = new ArrayList<>();
                for (int o = oneStart; o < oneEnd; o++) {
                    String oneC = paragraph[o].trim();
                    if (!"".equals(oneC)) {
                        if (regTwoTitle(oneC)) {
                            tIndexList.add(o);
                        }
                    }
                }

                int tIndexListSize = tIndexList.size();
                int j = 0;
                while (j < tIndexListSize - 1 && j < totalParagraph) {
                    int twoStart = tIndexList.get(j);
                    j++;
                    int twoEnd = tIndexList.get(j);

                    StringBuilder twoSb = new StringBuilder();
                    for (int t = twoStart; t < twoEnd; t++) {
                        String twoC = paragraph[t].trim();
                        if (!"".equals(twoC)) {
                            twoSb.append(twoC);
                        }
                    }
                    twoContent.putIfAbsent(j, twoSb.toString());
                }

                contentByTitles.putIfAbsent(paragraph[fIndexList.get(i - 1)].replaceAll("\\s+", ""), twoContent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentByTitles;
    }

    /**
     * 判断二级标题正则，如果遇到其他版本则需要修改
     * @param twoTitle 二级标题
     * @return 是否二级标题
     */
    private boolean regTwoTitle(String twoTitle) {
        return twoTitle.matches("^([0-9]{1,}[.\\s\\u4e00-\\u9fa5]{6,10}$)$");

    }

    /**
     * 调用示例
     * @param args
     */
    public static void main(String[] args) {
        File file = new File("/Users/baibing6/Desktop/CSIEZB17020188.doc");
        try (InputStream in = new FileInputStream(file)) {
            Map<String, StringBuilder> map = new ParseFile().parseText(in, DOC);
            log.info("{}", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static List<String> ONE_TITLE = Arrays.asList(
            "第一章", "第二章", "第三章", "第四章", "第五章",
            "第六章", "第七章", "第八章", "第九章", "第十章",
            "第十一章", "第十二章", "第十三章", "第十四章", "第十五章");
}
