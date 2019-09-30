package io.gomk.framework.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hankcs.hanlp.HanLP;

import io.gomk.controller.response.SearchResultVO;
import io.gomk.framework.utils.parse.PDF;
import io.gomk.framework.utils.parse.Word2003;
import io.gomk.framework.utils.parse.Word2007;

public class FileListUtil {
	private static int depth=1;  
	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    public static List<Map<String, Object>> find(String pathName, String delPath, int depth, List<Map<String, Object>> list) throws IOException{  
    	//List<Map<String, Object>> list = new ArrayList<>();
    	String now = format.format(new Date());
        //获取pathName的File对象  
        File dirFile = new File(pathName);  
        //判断该文件或目录是否存在，不存在时在控制台输出提醒  
        if (!dirFile.exists()) {  
            System.out.println("do not exit");  
            return null;  
        }  
        //判断如果不是一个目录，就判断是不是一个文件，时文件则输出文件路径  
        if (!dirFile.isDirectory()) {  
            if (dirFile.isFile()) {  
                System.out.println(dirFile.getCanonicalFile());  
            }  
            return null;  
        }  
          
//        for (int j = 0; j < depth; j++) {  
//            System.out.print("  ");  
//        }  
//        System.out.print("|--");  
//        System.out.println(dirFile.getName());  
        //获取此目录下的所有文件名与目录名  
        String[] fileList = dirFile.list();  
        int currentDepth=depth+1;  
        for (int i = 0; i < fileList.length; i++) {  
            //遍历文件目录  
            String string = fileList[i];  
            //File("documentName","fileName")是File的另一个构造器  
            File file = new File(dirFile.getPath(),string);  
            String fileName = file.getName();  
            //如果是一个目录，搜索深度depth++，输出目录名后，进行递归  
            if (file.isDirectory()) {  
                //递归  
                find(file.getCanonicalPath(), delPath, currentDepth, list);  
            }else{  
//                //如果是文件，则直接输出文件名  
//                for (int j = 0; j < currentDepth; j++) {  
//                    System.out.print("   ");  
//                }  
            	
            	String filePath = file.getAbsolutePath();
                System.out.print("|--");  
                System.out.println(filePath); 
                
                if (filePath.indexOf("施工图") != -1 || filePath.indexOf("图纸") != -1 || filePath.contains("~")) {
					continue;
				}
                String content = "";
				if (fileName.endsWith(".doc")) {
					content = Word2003.read(filePath);
				} else if (fileName.endsWith(".docx")) {
					content = Word2007.read(filePath);
				} else if (fileName.endsWith(".pdf")) {
					content = PDF.read(filePath);
				}
				if ("".equals(content.trim()) || "\n".equals(content)) {
					continue;
				}
				if (content.length() > 4000) {
					content = content.substring(0,4000);
				}
				
				Map<String, Object> map = new HashMap<>();
				fileName = fileName.substring(0, fileName.lastIndexOf("."));
				map.put("title", fileName);
				map.put("content", content);
				map.put("keyword_suggest", fileName);
			//	List<String> keywordList = HanLP.extractKeyword(content, 5);
				List<String> keywordList = HanLP.extractPhrase(content, 3);
				map.put("tag", keywordList.toString());
				map.put("add_date", now);
				map.put("file_url", filePath.replace(delPath, "|--"));
				list.add(map);
                  
            }  
        }  
        return list;
    }  
    
//    public static void find(String pathName,int depth) throws IOException{  
//        int filecount=0;  
//        //获取pathName的File对象  
//        File dirFile = new File(pathName);  
//        //判断该文件或目录是否存在，不存在时在控制台输出提醒  
//        if (!dirFile.exists()) {  
//            System.out.println("do not exit");  
//            return ;  
//        }  
//        //判断如果不是一个目录，就判断是不是一个文件，时文件则输出文件路径  
//        if (!dirFile.isDirectory()) {  
//            if (dirFile.isFile()) {  
//                System.out.println(dirFile.getCanonicalFile());  
//            }  
//            return ;  
//        }  
//          
//        for (int j = 0; j < depth; j++) {  
//            System.out.print("  ");  
//        }  
//        System.out.print("|--");  
//        System.out.println(dirFile.getName());  
//        //获取此目录下的所有文件名与目录名  
//        String[] fileList = dirFile.list();  
//        int currentDepth=depth+1;  
//        for (int i = 0; i < fileList.length; i++) {  
//            //遍历文件目录  
//            String string = fileList[i];  
//            //File("documentName","fileName")是File的另一个构造器  
//            File file = new File(dirFile.getPath(),string);  
//            String name = file.getName();  
//            //如果是一个目录，搜索深度depth++，输出目录名后，进行递归  
//            if (file.isDirectory()) {  
//                //递归  
//                find(file.getCanonicalPath(),currentDepth);  
//            }else{  
//                //如果是文件，则直接输出文件名  
//                for (int j = 0; j < currentDepth; j++) {  
//                    System.out.print("   ");  
//                }  
//                System.out.print("|--");  
//                System.out.println(name);  
//                  
//            }  
//        }  
//    }  
    public static void getFiles(String pathName,int depth, List<File> list) throws IOException{  
        //获取pathName的File对象  
        File dirFile = new File(pathName);  
        //判断该文件或目录是否存在，不存在时在控制台输出提醒  
        if (!dirFile.exists()) {  
            System.out.println("do not exit");  
            return ;  
        }  
        //判断如果不是一个目录，就判断是不是一个文件，时文件则输出文件路径  
        if (!dirFile.isDirectory()) {  
            if (dirFile.isFile()) {  
                System.out.println(dirFile.getCanonicalFile());  
            }  
            return ;  
        }  
          
        for (int j = 0; j < depth; j++) {  
            System.out.print("  ");  
        }  
        System.out.print("|--");  
      System.out.println(dirFile.getName());  
        //获取此目录下的所有文件名与目录名  
        String[] fileList = dirFile.list();  
        int currentDepth=depth+1;  
        for (int i = 0; i < fileList.length; i++) {  
            //遍历文件目录  
            String string = fileList[i];  
            //File("documentName","fileName")是File的另一个构造器  
            File file = new File(dirFile.getPath(),string);  
            String name = file.getName();  
            //如果是一个目录，搜索深度depth++，输出目录名后，进行递归  
            if (file.isDirectory()) {  
                //递归  
            	getFiles(file.getCanonicalPath(),currentDepth, list);  
            }else{  
                //如果是文件，则直接输出文件名  
                for (int j = 0; j < currentDepth; j++) {  
                    System.out.print("   ");  
                }  
                System.out.print("|--");  
                System.out.println(name);  
                list.add(file);
                  
            }  
        }  
    }  
      
    public static void main(String[] args) throws IOException{  
    	String directoryPath = "/Users/vko/Documents/my-code/DOC/zj";
    	List<Map<String, Object>> list = new ArrayList<>();
        find(directoryPath, directoryPath, depth, list);  
    } 
}
