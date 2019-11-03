package io.gomk.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import io.gomk.common.rs.response.ResponseData;
import io.gomk.common.utils.JsonUtils;
import io.gomk.common.utils.ReflectUtil;
import io.gomk.controller.request.CheckVO;
import io.gomk.enums.CheckScopeEnum;
import io.gomk.enums.CheckTypeEnum;
import io.gomk.model.entity.DPrjManufacturCost;
import io.gomk.model.entity.DZbPkg;
import io.gomk.model.entity.DZbPrj;
import io.gomk.service.DZbPkgService;
import io.gomk.service.IDPrjManufacturCostService;
import io.gomk.service.IDZbPrjService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * gomk-b-search
 *
 * @author chen
 * @Date 2019/10/23
 */
@RestController
@RequestMapping("/checkfile")
public class CheckFileController {

    @Value("classpath:zbwj_check.json")
    private Resource areaRes;
    @Value("classpath:pbbg_check.json")
    private Resource pbbgRes;
    @Value("classpath:zjcg_check.json")
    private Resource zjcgRes;
    @Autowired
    private IDZbPrjService idZbPrjService;
    @Autowired
    private DZbPkgService dZbPkgService;
    @Autowired
    private IDPrjManufacturCostService idPrjManufacturCostService;

    @PostMapping("/zbwjcheck")
    @ApiOperation(value = "招标文件自检",notes = "招标文件自检")
    public ResponseData checkResult(@ApiParam(name="file",value = "上传word文件",required = true) @RequestParam("file") MultipartFile file,
                                    @ApiParam(name="fileType",value = "word文件类型:doc docx",required = true) @RequestParam("fileType") String fileType,
                                    @ApiParam(name="type",value = "文件类型：工程类传2：其他传1",defaultValue = "1" ,required = true) @RequestParam("type") String type,
                                    @ApiParam(name="prjCode",value = "项目编号",required = true) @RequestParam("prjCode") String prjCode){
        ResponseData responseData = new ResponseData();
        List<String> result = new ArrayList<>();

        if(file.isEmpty()){
            responseData.setSuccess(false);
            responseData.setMessage("please upload file");
        }
        DZbPrj prj = idZbPrjService.getPrjByPrjCode(prjCode);
        if(prj == null){
            responseData.setSuccess(false);
            responseData.setMessage("项目不存在");
        }
        String areaData = null;
        try {
            areaData = IOUtils.toString(areaRes.getInputStream(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<CheckVO> checkVOs = null;
        try {
            checkVOs = JsonUtils.unmarshal(areaData, new TypeReference<List<CheckVO>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        if ("docx".equals(fileType)) {
            try {
                List<String> contexts = getDocxContent(file);
                for(String text : contexts){
                    checkText(prj,checkVOs, contexts,text);
                }
                checkZDBBH(prjCode,contexts,result);
                checkZDB(prjCode,contexts,result);
            } catch(IOException e){
                responseData.setSuccess(false);
                e.printStackTrace();
            }
        }else {
            try {
                List<String> resultList = getDocContent(file);
                for(String text : resultList){
                    checkText(prj, checkVOs, resultList, text);
                }
                checkZDBBH(prjCode,resultList,result);
                checkZDB(prjCode,resultList,result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        checkVOs.forEach(checkVO -> {
            if(!"true".equals(checkVO.getResult()) && !CheckTypeEnum.OTHER.toString().equals(checkVO.getType())){
                System.out.println(checkVO.toString());
                if(CheckTypeEnum.ALL.toString().equals(checkVO.getType())) {
                    result.add("否决信息：" + checkVO.getName() + "项不符合" + checkVO.getName() + "否决规则");
                }
                if(CheckTypeEnum.工程.toString().equals(checkVO.getType()) && "2".equals(type)){
                    result.add("否决信息：" + checkVO.getName() + "项不符合" + checkVO.getName() + "否决规则");
                }
            }
        });

        if(result.size()>0) {
            responseData.setSuccess(false);
            responseData.setMessage("自检不通过！");
            responseData.setData(result);
        }else {
            responseData.setSuccess(true);
            responseData.setMessage("自检通过！");
        }
        return responseData;
    }

    @PostMapping("/pbbgcheck")
    @ApiOperation(value = "评标报告自检",notes = "评标报告自检")
    public ResponseData checkResult(@ApiParam(name="file",value = "上传word文件",required = true) @RequestParam("file") MultipartFile file,
                                    @ApiParam(name="fileType",value = "word文件类型:doc docx",required = true) @RequestParam("fileType") String fileType,
                                    @ApiParam(name="prjCode",value = "项目编号",required = true) @RequestParam("prjCode") String prjCode){
        ResponseData responseData = new ResponseData();
        List<String> result = new ArrayList<>();
        if(file.isEmpty()){
            responseData.setSuccess(false);
            responseData.setMessage("please upload file");
        }
        DZbPrj prj = idZbPrjService.getPrjByPrjCode(prjCode);
        if(prj == null){
            responseData.setSuccess(false);
            responseData.setMessage("项目不存在");
        }

        String pbbgDate = null;
        try {
            pbbgDate = IOUtils.toString(pbbgRes.getInputStream(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<CheckVO> checkVOs = null;
        try {
            checkVOs = JsonUtils.unmarshal(pbbgDate, new TypeReference<List<CheckVO>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        if ("docx".equals(fileType)) {
            try {
                List<String> contexts = getDocxContent(file);
                pbbg(result, prj, checkVOs, contexts);
            } catch(IOException e){
                responseData.setSuccess(false);
                e.printStackTrace();
            }
        }else {
            try {
                List<String> contents = getDocContent(file);
                pbbg(result, prj, checkVOs, contents);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        checkVOs.forEach(checkVO -> {
            if(!"true".equals(checkVO.getResult()) && !CheckTypeEnum.OTHER.toString().equals(checkVO.getType())){
                System.out.println(checkVO.toString());
                if(CheckTypeEnum.ALL.toString().equals(checkVO.getType())) {
                    result.add("否决信息：" + checkVO.getName() + "项不符合" + checkVO.getName() + "否决规则");
                }
            }
        });
        if(result.size()>0) {
            responseData.setSuccess(false);
            responseData.setMessage("自检不通过！");
            responseData.setData(result);
        }else {
            responseData.setSuccess(true);
            responseData.setMessage("自检通过！");
        }
        return responseData;
    }

    private List<String> getDocContent(MultipartFile file) throws IOException {
        WordExtractor extractor = new WordExtractor(file.getInputStream());
        String[] paragraphs = extractor.getParagraphText();
        List<String> contents = new ArrayList<>(paragraphs.length);
        Collections.addAll(contents, paragraphs);
        return contents;
    }

    private List<String> getDocxContent(MultipartFile file) throws IOException {
        List<String> contexts = new ArrayList<>();
        System.out.println(file.getContentType());
        ZipSecureFile.setMinInflateRatio(-1.0d);
        XWPFDocument doc = new XWPFDocument(file.getInputStream());
        List<XWPFParagraph> paras = doc.getParagraphs();
        for (XWPFParagraph para : paras) {
            para.getParagraphText();
            String text = para.getParagraphText();
            if (!text.trim().isEmpty()) {
                contexts.add(text);
            }
        }
        return contexts;
    }

    private void pbbg(List<String> result, DZbPrj prj, List<CheckVO> checkVOs, List<String> contexts) {
        for (String text : contexts) {
            checkText(prj, checkVOs, contexts, text);
        }
        List<String> titles = new ArrayList<>();
        findPbbgtitle(contexts, titles);
        System.out.println(titles.size());
        titles.forEach(title -> {
            boolean b = false;
            for (String s : contexts) {
                s = s.replaceAll("\r\n", "").replaceAll("\f", "");
                System.out.println(s);
                if (!s.isEmpty() && title.contains(s)) {
                    b = true;
                    break;
                }
            }
            if (!b) {
                result.add("否决信息：" + title + "项不符合目录否决规则");
            }
        });
    }

    @PostMapping("/zjcgcheck")
    @ApiOperation(value = "造价成果自检",notes = "造价成果自检")
    public ResponseData checkResult(@ApiParam(name="file1",value = "封面、签署页（控制价审核）文件",required = true) @RequestParam("file1") MultipartFile file1,
                                    @ApiParam(name="file2",value = "目录（控制价审核）文件",required = true) @RequestParam("file2") MultipartFile file2,
                                    @ApiParam(name="file3",value = "审核说明（控制价审核）文件",required = true) @RequestParam("file3") MultipartFile file3,
                                    @ApiParam(name="fileType",value = "word文件类型:doc docx",required = true) @RequestParam("fileType") String fileType,
                                    @ApiParam(name="prjCode",value = "项目编号",required = true) @RequestParam("prjCode") String prjCode) {
        ResponseData responseData = new ResponseData();
        DPrjManufacturCost prj = idPrjManufacturCostService.getPrjCostByPrjCode(prjCode);
        List<String> result = new ArrayList<>();
        String pbbgDate = null;
        try {
            pbbgDate = IOUtils.toString(zjcgRes.getInputStream(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<CheckVO> checkVOs = null;
        try {
            checkVOs = JsonUtils.unmarshal(pbbgDate, new TypeReference<List<CheckVO>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        if ("docx".equals(fileType)) {
            try {
                List<String> contexts = getDocxContent(file1);
                for(String text : contexts){
                    checkZjcgText(prj,checkVOs, contexts,text);
                }

            } catch(IOException e){
                responseData.setSuccess(false);
                e.printStackTrace();
            }
        }else {
            try {
                List<String> resultList = getDocContent(file1);
                for(String text : resultList){
                    checkZjcgText(prj, checkVOs, resultList, text);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        checkVOs.forEach(checkVO -> {
            if(!"true".equals(checkVO.getResult()) && !CheckTypeEnum.OTHER.toString().equals(checkVO.getType())){
                System.out.println(checkVO.toString());
                if(CheckTypeEnum.ALL.toString().equals(checkVO.getType())) {
                    result.add("否决信息：" + checkVO.getName() + "项不符合" + checkVO.getName() + "否决规则");
                }
            }
        });
        if(result.size()>0) {
            responseData.setSuccess(false);
            responseData.setMessage("自检不通过！");
            responseData.setData(result);
        }else {
            responseData.setSuccess(true);
            responseData.setMessage("自检通过！");
        }
        return responseData;
    }


        private void checkZDBBH(String prjCode,List<String> contents,List<String> results){
        List<DZbPkg> pkgs = dZbPkgService.findPkg(prjCode);
        pkgs.forEach(dZbPkg -> {
            contents.forEach(content -> {
                if(content.contains(dZbPkg.getPkgCode())){
                    dZbPkg.setState("true");
                }
            });
        });
        pkgs.forEach( z -> {
            if(!"true".equals(z.getState()))
                results.add("否决信息："+ z.getPkgCode() +"项不符合标段包编号否决规则");
        });
    }

    private void checkZDB(String prjCode,List<String> contents,List<String> results){
        List<DZbPkg> pkgs = dZbPkgService.findPkg(prjCode);
        pkgs.forEach(dZbPkg -> {
            contents.forEach(content -> {
                if(content.contains(dZbPkg.getPkgName())){
                    dZbPkg.setState("true");
                }
            });
        });
        pkgs.forEach( z -> {
            if(!"true".equals(z.getState()))
                results.add("否决信息："+ z.getPkgName() +"项不符合标段包名称否决规则");
        });
    }

    private void findPbbgtitle(List<String> contents,List<String> title){
        String begin = "目录";
        String end = "致招标人函";
        boolean b = false;
        int i = 0;
        for (String content : contents) {
            if(end.equals(content.replaceAll("\r\n","").replaceAll(" ",""))){
                break;
            }
            if(b) {
                if(!"".equals(content.replaceAll("\r\n","").replaceAll("\f",""))) {
                    title.add(content);
                }
            }
            if(!b && begin.equals(content.replaceAll("\r\n","").replaceAll(" ",""))){
                b = true;
            }
        }
        title.forEach(t -> {
            contents.remove(t);
        });

    }

    private void checkText(DZbPrj prj, List<CheckVO> checkVOs, List<String> resultList, String text) {
        checkVOs.forEach(checkVO -> {
            if (CheckTypeEnum.ALL.toString().equals(checkVO.getType())) {
                if (CheckScopeEnum.ALL.toString().equals(checkVO.getScope())) {
                    String t = text.replaceAll("\r\n", "");
                    if (!t.isEmpty()){
                        if ("CONTAIN".equals(checkVO.getCalculate())) {
                            checkVO.getPatterns().forEach(pattern -> {
                                Matcher matcher = ConverCompile(t, pattern);
                                System.out.println(t);
                                boolean b = matcher.matches();
                                if (b) {
                                    checkVO.setResult(String.valueOf(b));
                                }
                            });
                        }
                        if ("findANDequals".equals(checkVO.getCalculate())) {
                            checkVO.getPatterns().forEach(pattern -> {
                                Matcher matcher = ConverCompile(StringUtils.trim(t), pattern);
                                System.out.println(t);
                                while(matcher.find()){
                                    System.out.println(matcher.group());
                                    Object v = ReflectUtil.getValueFormObject(prj,checkVO.getTablefleld());
                                    if(matcher.group().contains(v.toString())){
                                        checkVO.setResult(String.valueOf(true));
                                    }else{

                                    }
                                }
                            });
                        }
                        if("SUB".equals(checkVO.getCalculate())){
                            if(t.contains(checkVO.getName())){
                                for (String tex : resultList) {
                                    boolean b = tex.contains(checkVO.getSubcontext());
                                    if(b){
                                        checkVO.setResult(String.valueOf(b));
                                    }
                                }
                            }
                        }

                    }
                }
            }
            if(CheckTypeEnum.工程.equals(checkVO.getType())){
                String t = text.replaceAll("\r\n", "");
                if("CONTAIN".equals(checkVO.getCalculate())){
                    boolean b = t.contains(checkVO.getName());
                    if(b){
                        checkVO.setResult(String.valueOf(b));
                    }
                }
            }
        });
    }

    private void checkZjcgText(DPrjManufacturCost prj, List<CheckVO> checkVOs, List<String> resultList, String text) {
        checkVOs.forEach(checkVO -> {
            if (CheckTypeEnum.ALL.toString().equals(checkVO.getType())) {
                if (CheckScopeEnum.ALL.toString().equals(checkVO.getScope())) {
                    String t = text.replaceAll("\r\n", "");
                    if (!t.isEmpty()){
                        if ("CONTAIN".equals(checkVO.getCalculate())) {
                            checkVO.getPatterns().forEach(pattern -> {
                                Matcher matcher = ConverCompile(t, pattern);
                                System.out.println(t);
                                boolean b = matcher.matches();
                                if (b) {
                                    checkVO.setResult(String.valueOf(b));
                                }
                            });
                        }
                        if ("findANDequals".equals(checkVO.getCalculate())) {
                            checkVO.getPatterns().forEach(pattern -> {
                                Matcher matcher = ConverCompile(StringUtils.trim(t), pattern);
                                System.out.println(t);
                                while(matcher.find()){
                                    System.out.println(matcher.group());
                                    Object v = ReflectUtil.getValueFormObject(prj,checkVO.getTablefleld());
                                    if(matcher.group().contains(v.toString())){
                                        checkVO.setResult(String.valueOf(true));
                                    }else{

                                    }
                                }
                            });
                        }
                        if("SUB".equals(checkVO.getCalculate())){
                            if(t.contains(checkVO.getName())){
                                for (String tex : resultList) {
                                    boolean b = tex.contains(checkVO.getSubcontext());
                                    if(b){
                                        checkVO.setResult(String.valueOf(b));
                                    }
                                }
                            }
                        }

                    }
                }
            }
        });
    }

    /**
           * Word中的大纲级别，可以通过getPPr().getOutlineLvl()直接提取，但需要注意，Word中段落级别，通过如下三种方式定义：
           *  1、直接对段落进行定义；
           *  2、对段落的样式进行定义；
           *  3、对段落样式的基础样式进行定义。
           *  因此，在通过“getPPr().getOutlineLvl()”提取时，需要依次在如上三处读取。
           * @param doc
           * @param para
          * @return
           */
    private  String getTitleLvl(XWPFDocument doc, XWPFParagraph para) {
                 String titleLvl = "";
                 try {
                         //判断该段落是否设置了大纲级别
                         if (para.getCTP().getPPr().getOutlineLvl() != null) {
                                  System.out.println("getCTP()");
                               System.out.println(para.getParagraphText());
                               System.out.println(para.getCTP().getPPr().getOutlineLvl().getVal());

                                 return String.valueOf(para.getCTP().getPPr().getOutlineLvl().getVal());
                             }
                     } catch (Exception e) {

                     }

                 try {
                         //判断该段落的样式是否设置了大纲级别
                         if (doc.getStyles().getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl() != null) {

                                  System.out.println("getStyle");
                               System.out.println(para.getParagraphText());
                               System.out.println(doc.getStyles().getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl().getVal());

                                 return String.valueOf(doc.getStyles().getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl().getVal());
                             }
                     } catch (Exception e) {

                     }

                 try {
                         //判断该段落的样式的基础样式是否设置了大纲级别
                         if (doc.getStyles().getStyle(doc.getStyles().getStyle(para.getStyle()).getCTStyle().getBasedOn().getVal())
                                 .getCTStyle().getPPr().getOutlineLvl() != null) {
                                  System.out.println("getBasedOn");
                               System.out.println(para.getParagraphText());
                                 String styleName = doc.getStyles().getStyle(para.getStyle()).getCTStyle().getBasedOn().getVal();
                               System.out.println(doc.getStyles().getStyle(styleName).getCTStyle().getPPr().getOutlineLvl().getVal());

                                 return String.valueOf(doc.getStyles().getStyle(styleName).getCTStyle().getPPr().getOutlineLvl().getVal());
                             }
                     } catch (Exception e) {

                     }

                 try {
                         if(para.getStyleID()!=null){
                                 return para.getStyleID();
                             }
                     } catch (Exception e) {

                     }

                 return titleLvl;
             }

    public int contain(String str,String sub){
        int i = 0;
        int j = 0;
        while(i < str.length()){
            while(j < sub.length()){
                if(str.charAt(i) == sub.charAt(j)){
                    i++;
                    j++;
                }else{
                    i = i-j+1;
                    j =0;
                }
            }
            break;
        }
        return i-j;
    }

    public int notcontain(String str,String sub){
        return 1;
    }


    private Matcher ConverCompile(String result, String regEx ){
        Pattern c = Pattern.compile(regEx);
        Matcher mc=c.matcher(result);
        return mc;
    }

    public static void main(String [] args){
//        Pattern c = Pattern.compile(".*交货日期：.*");
//        Pattern c = Pattern.compile("项目编号:[a-zA-Z0-9]+");
        Pattern c = Pattern.compile("项目名称: .*");
        Matcher mc=c.matcher("项目名称: 国神集团彬长低热值煤660MW超超临界CFB示范项目汽轮发电机组公开招标");
        boolean f =  mc.find();
        boolean b = mc.matches();
        boolean p = Pattern.matches(".*交货日期：.*","本招标项目的计划交货日期：");
        System.out.println(b);

    }

}