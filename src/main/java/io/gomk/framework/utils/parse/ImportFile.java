package io.gomk.framework.utils.parse;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;

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
		
	}
	public static List<Map<String, Object>>  getSourceMap() throws IOException {
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
				list.add(map);
			}
		}
		return list;
	}
	
}
