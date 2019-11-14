package io.gomk.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import io.gomk.common.rs.response.ResponseData;
import io.gomk.common.utils.JsonUtils;
import io.gomk.common.utils.ReflectUtil;
import io.gomk.controller.request.CheckVO;
import io.gomk.enums.CheckScopeEnum;
import io.gomk.enums.CheckTypeEnum;
import io.gomk.enums.FileTypeEnum;
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
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
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
    public ResponseData checkResult(@ApiParam(name="prjCode",value = "项目编号",required = true) @RequestParam("prjCode") String prjCode,
                                    @ApiParam(name="prjName",value = "项目名称",required = true) @RequestParam("prjName") String prjName,
                                    @ApiParam(name="pkgCodes",value = "标段包编号,以英文,分割",required = false) @RequestParam(name = "pkgCodes",required = false) String pkgCodes,
                                    @ApiParam(name="pkgNames",value = "标段包名称,以英文,分割",required = false) @RequestParam(name = "pkgNames",required = false) String pkgNames,
                                    @ApiParam(name="file",value = "上传word文件",required = true) @RequestParam("file") MultipartFile file,
                                    @ApiParam(name="fileType",value = "word文件类型:doc docx",required = true) @RequestParam("fileType") FileTypeEnum fileType,
                                    @ApiParam(name="type",value = "文件类型：工程类传2：其他传1",defaultValue = "1" ,required = true) @RequestParam("type") String type
                                    ){
        ResponseData responseData = new ResponseData();
        List<String> result = new ArrayList<>();

        if(file.isEmpty()){
            responseData.setSuccess(false);
            responseData.setMessage("please upload file");
            return responseData;
        }
        DZbPrj prj = new DZbPrj();
        prj.setPrjCode(prjCode);
        prj.setPrjName(prjName);

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
        if ("docx".equals(fileType.name())) {
            try {
                List<String> contexts = getDocxContent(file);
                for(String text : contexts){
                    checkText(prj,checkVOs, contexts,text);
                }
                if(StringUtils.isNotBlank(pkgCodes)) {
                    checkZDBBH(prjCode, contexts, result, Arrays.asList(pkgCodes.split(",")));
                }
                if(StringUtils.isNotBlank(pkgNames)){
                    checkZDB(prjCode,contexts,result, Arrays.asList(pkgNames.split(",")));
                }

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
                if(StringUtils.isNotEmpty(pkgCodes)) {
                    checkZDBBH(prjCode, resultList, result, Arrays.asList(pkgCodes.split(",")));
                }
                if(StringUtils.isNotEmpty(pkgNames)) {
                    checkZDB(prjCode, resultList, result, Arrays.asList(pkgNames.split(",")));
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
                                    @ApiParam(name="fileType",value = "word文件类型:doc docx",required = true) @RequestParam("fileType") FileTypeEnum fileType,
                                    @ApiParam(name="prjCode",value = "项目编号",required = true) @RequestParam("prjCode") String prjCode,
                                    @ApiParam(name="startDate",value = "开标日期,格式：yyyy年MM月dd日",required = true)@RequestParam("startDate") String startDate,
                                    @ApiParam(name="custName",value = "委托人信息",required = true) @RequestParam("custName") String custName,
                                    @ApiParam(name="purchasingStrategy",value = "招标方式",required = true) @RequestParam("purchasingStrategy") String purchasingStrategy
                                    ){
        ResponseData responseData = new ResponseData();
        List<String> result = new ArrayList<>();
        if(file.isEmpty()){
            responseData.setSuccess(false);
            responseData.setMessage("please upload file");
            return responseData;
        }
        DZbPrj prj = new DZbPrj();
        prj.setPrjCode(prjCode);
        prj.setCustName(custName);
        prj.setPurchasingStrategy(purchasingStrategy);
        if(prj == null){
            responseData.setSuccess(false);
            responseData.setMessage("项目不存在");
            return responseData;
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
                List<String> contexts = getDocxPbbgContent(file);
                pbbg(result, prj, checkVOs, contexts);
            } catch(IOException e){
                responseData.setSuccess(false);
                e.printStackTrace();
            }
        }else {
            try {
                List<String> contents = getDocPbbgContent(file);
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
        List<String> results = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (int i = 0, j=0; i < contents.size(); i++,j++) {
            if(j!=0 && j%80==0 || i == contents.size()-1) {
                results.add(builder.toString());
                builder.delete(0, builder.length());
                j=0;
            }
            builder.append(contents.get(i).replaceAll("\r\n","").replaceAll("\f",""));
        }
        return results;
    }
    private List<String> getDocPbbgContent(MultipartFile file) throws IOException {
        WordExtractor extractor = new WordExtractor(file.getInputStream());
        String[] paragraphs = extractor.getParagraphText();
        List<String> contents = new ArrayList<>(paragraphs.length);
        Collections.addAll(contents, paragraphs);
        return contents;
    }
    private List<String> getDocxContent(MultipartFile file) throws IOException {
        List<String> contents = new ArrayList<>();
        System.out.println(file.getContentType());
        ZipSecureFile.setMinInflateRatio(-1.0d);
        XWPFDocument doc = new XWPFDocument(file.getInputStream());
        List<XWPFParagraph> paras = doc.getParagraphs();
        for (XWPFParagraph para : paras) {
            para.getParagraphText();
            String text = para.getParagraphText();
            if (!text.trim().isEmpty()) {
                contents.add(text);
            }
        }
        List<String> results = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (int i = 0, j=0; i < contents.size(); i++,j++) {
            if(j!=0 && j%80==0 || i == contents.size()-1) {
                results.add(builder.toString());
                builder.delete(0, builder.length());
                j=0;
            }
            builder.append(contents.get(i).replaceAll("\r\n","").replaceAll("\f",""));
        }
        return results;
    }

    private List<String> getDocxPbbgContent(MultipartFile file) throws IOException {
        List<String> contents = new ArrayList<>();
        System.out.println(file.getContentType());
        ZipSecureFile.setMinInflateRatio(-1.0d);
        XWPFDocument doc = new XWPFDocument(file.getInputStream());
        List<XWPFParagraph> paras = doc.getParagraphs();
        for (XWPFParagraph para : paras) {
            para.getParagraphText();
            String text = para.getParagraphText();
            if (!text.trim().isEmpty()) {
                contents.add(text);
            }
        }
        return contents;
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
                                    @ApiParam(name="fileType",value = "word文件类型:doc docx",required = true) @RequestParam("fileType") FileTypeEnum fileType,
                                    @ApiParam(name="prjCode",value = "项目编号",required = true) @RequestParam("prjCode") String prjCode,
                                    @ApiParam(name="prjName",value = "项目名称",required = true) @RequestParam("prjName") String prjName,
                                    @ApiParam(name="contractCode",value = "成果编号",required = false) @RequestParam(name = "contractCode", required = false) String contractCode,
                                    @ApiParam(name="reportAmt",value = "估概预结中送审值",required = true) @RequestParam("reportAmt") double reportAmt
                                    ) {
        ResponseData responseData = new ResponseData();
        DPrjManufacturCost prj = new DPrjManufacturCost();//idPrjManufacturCostService.getPrjCostByPrjCode(prjCode);
        prj.setPrjCode(prjCode);
        prj.setPrjName(prjName);
        prj.setContractCode(contractCode);
        prj.setReportAmt(BigDecimal.valueOf(reportAmt));
//        if(prj == null){
//            responseData.setSuccess(false);
//            responseData.setMessage("造价成果数据不存在");
//            return responseData;
//        }
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
        if ("docx".equals(fileType.toString())) {
            try {
                List<String> contexts = getDocxContent(file1);
                for(String text : contexts){
                    checkZjcgText(prj,checkVOs, contexts,text);
                }
                zjcgAmt(file3, checkVOs, contexts);

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
                zjcgAmt(file3, checkVOs, resultList);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        checkVOs.forEach(checkVO -> {
            if(!"true".equals(checkVO.getResult())){
                System.out.println(checkVO.toString());
//                if(CheckTypeEnum.ALL.toString().equals(checkVO.getType())) {
                    result.add("否决信息：" + checkVO.getName() + "项不符合" + checkVO.getName() + "否决规则");
//                }
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

    private void zjcgAmt(@RequestParam("file3") @ApiParam(name = "file3", value = "审核说明（控制价审核）文件", required = true) MultipartFile file3, List<CheckVO> checkVOs, List<String> resultList) throws IOException {
        List<String> resultContents = getDocContent(file3);
        checkVOs.forEach(checkVO -> {
            if(CheckTypeEnum.OTHER.toString().equals(checkVO.getType())){
                resultContents.forEach(t -> {
                    checkVO.getPatterns().forEach(pattern -> {
                        Matcher matcher = ConverCompile(StringUtils.trim(t), pattern);
                        System.out.println(t);
                        while(matcher.find()){
                            System.out.println(matcher.group());
                            String group = matcher.group();
                            Matcher matcher1 = ConverCompile(group, "[0-9.]+");
                            if(matcher1.find()) {
                                System.out.println(matcher1.group());
                                resultList.forEach(r -> {
                                    if(r.contains(matcher1.group())){
                                        checkVO.setResult("true");
                                    }
                                });
                            }
                        }
                    });
                });
            }
        });
    }


    private void checkZDBBH(String prjCode, List<String> contents, List<String> results, List<String> pkgCodes){

        List<DZbPkg> pkgs = new ArrayList<>();
        for(String pkgCode : pkgCodes){
            DZbPkg pkg = new DZbPkg();
            pkg.setPkgCode(pkgCode);
            pkgs.add(pkg);
        }
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

    private void checkZDB(String prjCode, List<String> contents, List<String> results, List<String> pkgNames){
        List<DZbPkg> pkgs = new ArrayList<>();
        for(String pkgCode : pkgNames){
            DZbPkg pkg = new DZbPkg();
            pkg.setPkgName(pkgCode);
            pkgs.add(pkg);
        }
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
                                boolean b = false;
                                checkVO.setResult(String.valueOf(b));
                                for (String tex : resultList) {
                                    b = tex.contains(checkVO.getSubcontext());
                                    if(b){
                                        checkVO.setResult(String.valueOf(b));
                                    }
                                }
                            }else{
                                checkVO.setResult(String.valueOf(true));
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
                                boolean b = matcher.find();
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
                                    System.out.println(v.toString());

                                    if(matcher.group().contains(v.toString().replaceAll(".00",""))){
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
            if(CheckTypeEnum.OTHER.toString().equals(checkVO.getType())){

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
