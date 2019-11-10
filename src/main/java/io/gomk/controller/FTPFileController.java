package io.gomk.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.gomk.es6.ESRestClient;
import io.gomk.es6.EsUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/ftp")
@Api(description = "ftp下载")
public class FTPFileController {
	
	@Autowired
	EsUtil esUtil;
	@Autowired
	protected ESRestClient esClient;
	
	@GetMapping("/es/{scope}/{id}")
	public void downloadAttachment(@PathVariable("id") String id, @PathVariable("scope") int scope, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		RestHighLevelClient client = esClient.getClient();
		GetRequest getRequest = new GetRequest(esUtil.getIndexname(scope), "_doc", id);
		GetResponse getResponse = client.get(getRequest);
		Object storeType = getResponse.getSourceAsMap().get("storeType");
		Object storeUrl = getResponse.getSourceAsMap().get("storeUrl");
		Object ext = getResponse.getSourceAsMap().get("ext");
		Object title = getResponse.getSourceAsMap().get("title");
		
		InputStream is = esUtil.getInputStreams(storeType.toString(), storeUrl.toString(), ext.toString());
		
		String fileName = title + "." + ext;
		BufferedOutputStream out = null;
		//InputStream is = null;
		//String filepath = "/Users/vko/Documents/my-code/testDOC/tb/1包投标文件.zip";
		String downloadMode = "attachment";
		try {
			response.reset();
			response.setCharacterEncoding("UTF-8");
			String agent = request.getHeader("User-Agent");
			boolean isMSIE = (agent != null && agent.indexOf("MSIE") != -1);
			if (isMSIE) {
				response.setHeader("Content-Disposition",
						downloadMode + ";filename=" + URLEncoder.encode(fileName, "UTF-8"));
			} else {
				response.setHeader("Content-Disposition",
						downloadMode + ";filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
			}
			//response.setContentType("application/octet-stream"); 
			out = new BufferedOutputStream(response.getOutputStream());
			//is = new BufferedInputStream(new FileInputStream(new File(filepath)));
			byte[] content = new byte[1024];
			int len = 0;
			while ((len = is.read(content)) > 0) {
				out.write(content, 0, len);
			}
			out.flush();
			//return "下载成功";

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		//return "下载失败";
	}
	@GetMapping("/ddd")
	public void downloadAttachment(HttpServletRequest request,
			HttpServletResponse response) {
		String fileName = "破口大骂.zip";
		BufferedOutputStream out = null;
		InputStream is = null;
		String filepath = "/Users/vko/Documents/my-code/testDOC/tb/1包投标文件.zip";
		//下载"attachment"; inline打开
		String downloadMode = "attachment";
		try {
			response.reset();
			response.setCharacterEncoding("UTF-8");
			String agent = request.getHeader("User-Agent");
			boolean isMSIE = (agent != null && agent.indexOf("MSIE") != -1);
			if (isMSIE) {
				response.setHeader("Content-Disposition",
						downloadMode + ";filename=" + URLEncoder.encode(fileName, "UTF-8"));
			} else {
				response.setHeader("Content-Disposition",
						downloadMode + ";filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
			}
			//response.setContentType("application/octet-stream"); 
			out = new BufferedOutputStream(response.getOutputStream());
			is = new BufferedInputStream(new FileInputStream(new File(filepath)));
			byte[] content = new byte[1024];
			int len = 0;
			while ((len = is.read(content)) > 0) {
				out.write(content, 0, len);
			}
			out.flush();
			//return "下载成功";

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		//return "下载失败";
	}
//	
//	@GetMapping("/{id}")
//    public ResponseEntity<byte[]> downloadFile(@PathVariable(required = false) Integer id) throws IOException {
//    	File f = new File("/Users/vko/Documents/my-code/testDOC/img/1.jpg");
//    	// 设置下载文件名称，以下两种方式均可
//        // String fileName = new String(image.getOldname().getBytes("UTF-8"), "iso-8859-1");
//    	
//    	
//        String fileName = URLEncoder.encode(f.getName(), "utf-8");
//        HttpHeaders httpHeaders = new HttpHeaders();
//        // 通知浏览器以下载文件方式打开
//        ContentDisposition contentDisposition =
//                    ContentDisposition.builder("attachment").filename(fileName).build();
//        httpHeaders.setContentDisposition(contentDisposition);
//        // application/octet_stream设置MIME为任意二进制数据
//        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        // 使用apache commons-io 里边的 FileUtils工具类
//        //return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(image.getLocation())),
//        //        httpHeaders, HttpStatus.OK);
//        // 使用spring自带的工具类也可以 FileCopyUtils
//        return new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(f),
//                httpHeaders, HttpStatus.OK);
//    }  
//	
//	@RequestMapping(value = "/download", method = RequestMethod.POST)
//	public void download(HttpServletRequest request,
//            HttpServletResponse response) throws Exception {
//		File f = new File("/Users/vko/Documents/my-code/testDOC/img/1.jpg");
//
//		InputStream fileIn = new FileInputStream(f);
//		response.setContentType("application/octet-stream");   
//	     // URLEncoder.encode(fileNameString, "UTF-8") 下载文件名为中文的，文件名需要经过url编码      
//	     response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(f.getName(), "UTF-8"));   
//	     ServletOutputStream out = null;   
//	     try {   
//	        out = response.getOutputStream();   
//	  
//	        byte[] outputByte = new byte[1024];   
//	        int readTmp = 0;   
//	        while ((readTmp = fileIn.read(outputByte)) != -1) {   
//	           out.write(outputByte, 0, readTmp); //并不是每次都能读到1024个字节，所有用readTmp作为每次读取数据的长度，否则会出现文件损坏的错误     
//	        }   
//	     }   
//	     catch (Exception e) {   
//	        log.error(e.getMessage());   
//	     }   
//	     finally {   
//	        fileIn.close();   
//	        out.flush();   
//	        out.close();   
//	     }   
//
//	}

}
