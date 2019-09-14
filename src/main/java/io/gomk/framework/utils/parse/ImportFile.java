package io.gomk.framework.utils.parse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportFile {
	public static void main(String[] args) throws IOException {
		String directoryPath = "C:\\gitcode\\gomk\\DOC\\zhaobiao";
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
		}
	}
	public static List<Map<String, Object>>  getSourceMap() throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		
		String directoryPath = "C:\\gitcode\\gomk\\DOC\\zhaobiao";
		File directory = new File(directoryPath);
		File[] files = directory.listFiles();
		for (File f : files) {
			if (f.isFile()) {
				String fileName = f.getName();
				//System.out.println(fileName);
				String content = "";
				if (fileName.endsWith(".doc")) {
					content = Word2003.read(f.getAbsolutePath());
				}
				if (fileName.endsWith(".docx")) {
					content = Word2007.read(f.getAbsolutePath());
				}
				Map<String, Object> map = new HashMap<>();
				map.put("title", fileName);
				map.put("content", content);
				list.add(map);
			}
		}
		return list;
	}
	
}
