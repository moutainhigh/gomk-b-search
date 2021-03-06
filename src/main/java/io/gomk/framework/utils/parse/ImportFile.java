package io.gomk.framework.utils.parse;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hankcs.hanlp.HanLP;

import io.gomk.controller.response.NumberVO;
import io.gomk.framework.utils.FileListUtil;
import io.gomk.framework.utils.ParseExcelUtil;
import io.gomk.task.ESInfoBean;

public class ImportFile {
	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) throws IOException {
		/*
		 * String directoryPath = "C:\\gitcode\\gomk\\DOC\\zhaobiao"; File directory =
		 * new File(directoryPath); File[] files = directory.listFiles(); for (File f :
		 * files) { if (f.isFile()) { String fileName = f.getName();
		 * System.out.println(fileName); String content = ""; if
		 * (fileName.endsWith(".doc")) { content = Word2003.read(f.getAbsolutePath()); }
		 * if (fileName.endsWith(".docx")) { content =
		 * Word2007.read(f.getAbsolutePath()); } System.out.println("content:" +
		 * content); } }
		 */

		/*
		 * System.out.println(NLPTokenizer.segment("我新造一个词叫幻想乡你能识别并标注正确词性吗？"));
		 * List<Term> termList = StandardTokenizer.segment("商品和服务");
		 * System.out.println(termList);
		 */
//		List<Term> termList = IndexTokenizer.segment("主副食品");
//		for (Term term : termList)
//		{
//		    System.out.println(term + " [" + term.offset + ":" + (term.offset + term.word.length()) + "]");
//		    System.out.println(term.word);
//		}
//		
//		String content = "定额采用2010年《内蒙古自治区市政维修养护工程预算定额》，不足部分采用2009年《内蒙古自治区建筑工程预算定额》《内蒙古自治区装饰装修工程预算定额》《内蒙古自治区安装工程预算定额》《内蒙古自治区园林绿化工程预算定额》、2013年《内蒙古自治区房屋修缮工程预算定额》;取费执行2009年《内蒙古自治区建设工程费用定额》，人工费调整执行内建工[2013]587号文，税金按内建工［2011］434号执行。";
//		List<String> keywordList = HanLP.extractKeyword(content, 5);
//		System.out.println(keywordList.size() + ":" +keywordList.toString());
//		List<String> phraseList = HanLP.extractPhrase(content, 10);
//		System.out.println(phraseList);

//		String document = "算法可大致分为基本算法、数据结构的算法、数论算法、计算几何的算法、图的算法、动态规划以及数值分析、加密算法、排序算法、检索算法、随机化算法、并行算法、厄米变形模型、随机森林算法。\n"
//				+ "算法可以宽泛的分为三类，\n" + "一，有限的确定性算法，这类算法在有限的一段时间内终止。他们可能要花很长时间来执行指定的任务，但仍将在一定的时间内终止。这类算法得出的结果常取决于输入值。\n"
//				+ "二，有限的非确定算法，这类算法在有限的时间内终止。然而，对于一个（或一些）给定的数值，算法的结果并不是唯一的或确定的。\n"
//				+ "三，无限的算法，是那些由于没有定义终止定义条件，或定义的条件无法由输入的数据满足而不终止运行的算法。通常，无限算法的产生是由于未能确定的定义终止条件。";
//		List<String> sentenceList = HanLP.extractSummary(document, 3);
//		System.out.println(sentenceList);
//		String p = "/Users/vko/Documents/my-code/DOC/zj/CSIE-神延-YB-16011 西湾露天煤矿生态试点项目－猪舍、羊舍、园区工程 下午10.49.11/1、成果文件/1、封面-标准.doc";
//		String directoryPath = "/Users/vko/Documents/my-code/DOC/zj";
//		System.out.println(p.replace(directoryPath, ""));
		
		
		String text = "本招标项目名称为:晋能集团王家岭煤业采煤机采购，项目\n" + 
				"招标编号为:CEZB190205192，招标人为山西王家岭煤业有限公司，资 金来源采煤机为自筹，招标代理机构为中国神华国际工程有限公司。本项目 已具备招标条件，现对该项目采煤机进行公开招标。";
				List<String> phraseList = HanLP.extractKeyword(text, 5);
				System.out.println(phraseList);
		//getCompletionMap();
	}

	public static List<Map<String, Object>> getZBMap() throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		String now = format.format(new Date());
		String directoryPath = "/Users/vko/Documents/my-code/DOC/zb";
		List<File> files = new ArrayList<>();
		FileListUtil.getFiles(directoryPath, 3, files);

		for (File f : files) {
			if (f.isFile()) {
				String fileName = f.getName();
				System.out.println(fileName);
				String content = "";
				if (fileName.endsWith(".doc")) {
					content = Word2003.read(f.getAbsolutePath());
				} else if (fileName.endsWith(".docx")) {
					content = Word2007.read(f.getAbsolutePath());
				} else if (fileName.endsWith(".pdf")) {
					content = PDF.read(f.getAbsolutePath());
				}
				if (!"".equals(content)) {
					Map<String, Object> map = new HashMap<>();
					fileName = fileName.substring(0, fileName.lastIndexOf("."));
					map.put("title", fileName);
					map.put("content", content);
					map.put("tag", "");
					map.put("abstract", HanLP.extractSummary(content, 3));
					map.put("add_date", now);
					list.add(map);
				}
			}
		}
		return list;
	}

	public static List<Map<String, Object>> getZGYQMap() throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();

		String now = format.format(new Date());
		// String directoryPath = "C:\\gitcode\\gomk\\DOC\\zhaobiao";
		String directoryPath = "/Users/vko/Documents/my-code/DOC/zgyq";
		List<File> files = new ArrayList<>();
		FileListUtil.getFiles(directoryPath, 3, files);

		for (File f : files) {
			if (f.isFile()) {
				String fileName = f.getName();
				System.out.println(fileName);
				String content = "";
				if (fileName.endsWith(".doc")) {
					content = Word2003.read(f.getAbsolutePath());
				} else if (fileName.endsWith(".docx")) {
					content = Word2007.read(f.getAbsolutePath());
				} else if (fileName.endsWith(".pdf")) {
					content = PDF.read(f.getAbsolutePath());
				}
				if (!"".equals(content)) {
					Map<String, Object> map = new HashMap<>();
					fileName = fileName.substring(0, fileName.lastIndexOf("."));
					map.put("title", fileName);
					map.put("content", content);
					int location = content.indexOf("资格要求") == -1 ? content.indexOf("资质要求") : content.indexOf("资格要求");
					map.put("zbfw", content.substring(0, location));
					map.put("zgyq", content.substring(location));
					map.put("add_date", now);
					list.add(map);
				}
			}
		}
		return list;
	}

	public static List<ESInfoBean> getJSYQMap() throws IOException {
		String directoryPath = "/Users/vko/Documents/my-code/DOC/JSYQ";
		List<ESInfoBean> list = getIndexMap(directoryPath);
		return list;
	}

	public static List<ESInfoBean> getPBBFMap() throws IOException {
		String directoryPath = "/Users/vko/Documents/my-code/DOC/pbbf";
		List<ESInfoBean> list = getIndexMap(directoryPath);
		return list;
	}

	private static List<ESInfoBean> getIndexMap(String directoryPath) throws IOException {
		List<ESInfoBean> list = new ArrayList<>();
		//
		List<File> files = new ArrayList<>();
		FileListUtil.getFiles(directoryPath, 3, files);

		for (File f : files) {
			if (f.isFile()) {
				String fileName = f.getName();
				System.out.println(fileName);
				String content = "";
				if (fileName.endsWith(".doc")) {
					content = Word2003.read(f.getAbsolutePath());
				} else if (fileName.endsWith(".docx")) {
					content = Word2007.read(f.getAbsolutePath());
				} else if (fileName.endsWith(".pdf")) {
					content = PDF.read(f.getAbsolutePath());
				}
				if (!"".equals(content)) {
					fileName = fileName.substring(0, fileName.lastIndexOf("."));
					ESInfoBean esBean = new ESInfoBean();
					esBean.setTitle(fileName);
					esBean.setContent(content);
					esBean.setAddDate(new Date());
					list.add(esBean);
				}
			}
		}
		return list;
	}

	public static List<ESInfoBean> getZJCGMap() throws IOException {
		List<ESInfoBean> list = new ArrayList<>();
		String now = format.format(new Date());
		String directoryPath = "/Users/vko/Documents/my-code/DOC/zj";
		//String directoryPath = "/soft/doc/zj";
		List<File> files = new ArrayList<>();
		//FileListUtil.getFiles(directoryPath, 3, files);
		StringBuffer sb = new StringBuffer();
		FileListUtil.findDir(directoryPath, 3, files, sb);
		for (File f : files) {
			if (f.isFile()) {
				String fileName = f.getName();
				System.out.println(fileName);
				String filePath = f.getAbsolutePath();
				if (filePath.indexOf("施工图") != -1 || filePath.indexOf("图纸") != -1 || filePath.contains("~")) {
					continue;
				}

				String content = "";
				if (fileName.endsWith(".doc")) {
					content = Word2003.read(f.getAbsolutePath());
				} else if (fileName.endsWith(".docx")) {
					content = Word2007.read(f.getAbsolutePath());
				} else if (fileName.endsWith(".pdf")) {
					content = PDF.read(f.getAbsolutePath());
				}
				if (!"".equals(content)) {
					Map<String, Object> map = new HashMap<>();
					fileName = fileName.substring(0, fileName.lastIndexOf("."));
					ESInfoBean esBean = new ESInfoBean();
					esBean.setTitle(fileName);
					esBean.setContent(content);
					esBean.setAddDate(new Date());
					esBean.setDirectoryTree(sb.toString());
					
					list.add(esBean);
				}
			}
		}
		return list;
	}

	public static List<Map<String, Object>> getCompletionMap() throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		//String filePath = "/Users/vko/Documents/my-code/DOC/bdw/t_g_completion.xls";
		String filePath = "/soft/t_g_completion.xls";
		String now = format.format(new Date());
		Map<String, NumberVO> tmp = new HashMap<>();
		Map<Integer, Map<Integer, Object>> content;
		try {
			content = ParseExcelUtil.getExcelContent(filePath);

			for (Map.Entry<Integer, Map<Integer, Object>> entry : content.entrySet()) {
				Map<Integer, Object> cellMap = entry.getValue();
				String bdw = cellMap.get(2).toString();
				String pkgCode = cellMap.get(3).toString();
				String supplCode = cellMap.get(4).toString();
				if (tmp.get(bdw) != null) {
					tmp.get(bdw).getPkgCode().add(pkgCode);
					tmp.get(bdw).getSupplDocumentCode().add(supplCode);
				} else {
					NumberVO vo = new NumberVO();
					Set<String> pkgSet = new HashSet<>();
					pkgSet.add(pkgCode);
					vo.setPkgCode(pkgSet);
					Set<String> supplSet = new HashSet<>();
					supplSet.add(supplCode);
					vo.setSupplDocumentCode(supplSet);
					tmp.put(bdw, vo);
				}
			}

			for (Map.Entry<String, NumberVO> n : tmp.entrySet()) {
				Map<String, Object> map = new HashMap<>();
				map.put("pkg_code", n.getValue().getPkgCode());
				map.put("suppl_document_code", n.getValue().getSupplDocumentCode());
				map.put("words", n.getKey());
				map.put("add_date", now);
				list.add(map);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	 
	public static List<ESInfoBean> getCommonESInfo(String directoryPath) throws IOException {
		
		return  getIndexMap(directoryPath);
	}

	

}
