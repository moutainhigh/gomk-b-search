package io.gomk.framework.utils.parse;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;

import io.gomk.framework.utils.FileListUtil;

public class ImportFile {
	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	public static void main(String[] args) throws IOException {
		/*String directoryPath = "C:\\gitcode\\gomk\\DOC\\zhaobiao";
		File directory = new File(directoryPath);
		File[] files = directory.listFiles();
		for (File f : files) {
			if (f.isFile()) {
				String fileName = f.getName();
				System.out.println(fileName);
				String content = "";
				if (fileName.endsWith(".doc")) {
					content = Word2003.read(f.getAbsolutePath());
				}
				if (fileName.endsWith(".docx")) {
					content = Word2007.read(f.getAbsolutePath());
				}
				System.out.println("content:" + content);
			}
		}*/
		
		/*System.out.println(NLPTokenizer.segment("我新造一个词叫幻想乡你能识别并标注正确词性吗？"));
		List<Term> termList = StandardTokenizer.segment("商品和服务");
		System.out.println(termList);*/
		List<Term> termList = IndexTokenizer.segment("主副食品");
		for (Term term : termList)
		{
		    System.out.println(term + " [" + term.offset + ":" + (term.offset + term.word.length()) + "]");
		    System.out.println(term.word);
		}
		
		String content = "定额采用2010年《内蒙古自治区市政维修养护工程预算定额》，不足部分采用2009年《内蒙古自治区建筑工程预算定额》《内蒙古自治区装饰装修工程预算定额》《内蒙古自治区安装工程预算定额》《内蒙古自治区园林绿化工程预算定额》、2013年《内蒙古自治区房屋修缮工程预算定额》;取费执行2009年《内蒙古自治区建设工程费用定额》，人工费调整执行内建工[2013]587号文，税金按内建工［2011］434号执行。";
		List<String> keywordList = HanLP.extractKeyword(content, 5);
		System.out.println(keywordList.size() + ":" +keywordList.toString());
		List<String> phraseList = HanLP.extractPhrase(content, 10);
		System.out.println(phraseList);
	}
	public static List<Map<String, Object>>  getZBMap() throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		 
		String now = format.format(new Date());
		//String directoryPath = "C:\\gitcode\\gomk\\DOC\\zhaobiao";
		String directoryPath = "/Users/vko/Documents/my-code/DOC/zb";
		File directory = new File(directoryPath);
		File[] files = directory.listFiles();
		for (File f : files) {
			if (f.isFile()) {
				String fileName = f.getName();
				System.out.println(fileName);
				String content = "";
				if (fileName.endsWith(".doc")) {
					content = Word2003.read(f.getAbsolutePath());
				}
				if (fileName.endsWith(".docx")) {
					content = Word2007.read(f.getAbsolutePath());
				}
				if (!"".equals(content)) {
					Map<String, Object> map = new HashMap<>();
					fileName = fileName.substring(0, fileName.lastIndexOf("."));
					map.put("title", fileName);
					map.put("content", content);
					map.put("keyword_suggest", fileName);
					map.put("tag", "");
					map.put("add_date", now);
					map.put("file_url", "");
					
					//map.put("keyword_suggest", content);
					list.add(map);
				}
				
			}
		}
		return list;
	}
	
	public static List<Map<String, Object>>  getZGYQMap() throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		 
		String now = format.format(new Date());
		//String directoryPath = "C:\\gitcode\\gomk\\DOC\\zhaobiao";
		String directoryPath = "/Users/vko/Documents/my-code/DOC/zb";
		File directory = new File(directoryPath);
		File[] files = directory.listFiles();
		for (File f : files) {
			if (f.isFile()) {
				String fileName = f.getName();
				System.out.println(fileName);
				String content = "";
				if (fileName.endsWith(".doc")) {
					content = Word2003.read(f.getAbsolutePath());
				}
				if (fileName.endsWith(".docx")) {
					content = Word2007.read(f.getAbsolutePath());
				}
				Map<String, Object> map = new HashMap<>();
				fileName = fileName.substring(0, fileName.lastIndexOf("."));
				map.put("title", fileName);
				map.put("content", content);
				map.put("keyword_suggest", fileName);
				map.put("tag", "");
				map.put("add_date", now);
				map.put("file_url", "");
				
				//map.put("keyword_suggest", content);
				
				if (content.indexOf("资格要求") != -1) {
					list.add(map);
				}
			}
		}
		return list;
	}
	
	public static List<Map<String, Object>>  getZJMap() throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		 
		String now = format.format(new Date());
		//String directoryPath = "C:\\gitcode\\gomk\\DOC\\zhaobiao";
		String directoryPath = "/Users/vko/Documents/my-code/DOC/zj";
		FileListUtil.find(directoryPath, directoryPath, 3, list);
//		
//		File directory = new File(directoryPath);
//		File[] files = directory.listFiles();
//		for (File f : files) {
//			if (f.isFile()) {
//				String fileName = f.getName();
//				System.out.println(fileName);
//				String content = "";
//				if (fileName.endsWith(".doc")) {
//					content = Word2003.read(f.getAbsolutePath());
//				}
//				if (fileName.endsWith(".docx")) {
//					content = Word2007.read(f.getAbsolutePath());
//				}
//				
//				if (!"".equals(content)) {
//					Map<String, Object> map = new HashMap<>();
//					fileName = fileName.substring(0, fileName.lastIndexOf("."));
//					map.put("title", fileName);
//					map.put("content", content);
//					map.put("keyword_suggest", fileName);
//					List<String> keywordList = HanLP.extractKeyword(content, 5);
//					
//					map.put("tag", keywordList.toString());
//					map.put("add_date", now);
//					map.put("file_url", "");
//					
//					//map.put("keyword_suggest", content);
//					list.add(map);
//				}
//				
//			}
//		}
		return list;
	}
	
	
}
