package io.gomk.es6;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.DocumentException;

import io.gomk.framework.utils.parse.PDF2Image;
import io.gomk.framework.utils.parse.ZipAndRarTools;
import io.gomk.framework.utils.parsefile.ParseFile;
import io.gomk.mapper.GTbIdcardExtractMapper;
import io.gomk.model.GTbIdcardExtract;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class IdcardOcrUtil {
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	GTbIdcardExtractMapper idcardExtractMapper;

	@Value("${util.idcardtool.path}")
	protected String idcardToolpath;
	

	public void insertIdcardInfo(ByteArrayInputStream in1, ByteArrayInputStream in2, GTbIdcardExtract entity) throws IOException, DocumentException {
		String targetDir = "/tmp/idcard-img/";
		File dir = new File(targetDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		int page = ParseFile.getPageIdCardContent(in1);
		PDF2Image.getImage(in2, targetDir, page, page+1);
		
		// 设置请求头
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("multipart/form-data");
		headers.setContentType(type);
		
		for (File f : dir.listFiles()) {
			// 设置请求体，注意是LinkedMultiValueMap
			FileSystemResource fileSystemResource = new FileSystemResource(f);
			MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
			form.add("pic", fileSystemResource);

			// 用HttpEntity封装整个请求报文
			HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);

			// 调用接口即可
			JSONObject json = restTemplate.postForEntity(idcardToolpath, files, JSONObject.class)
					.getBody();
			if (json != null && "0".equals(json.getString("error"))) {
				log.info("idcard info:" + json.toJSONString());
				entity.setIdcardName(json.getString("name"));
				entity.setIdcardNumber(json.getString("idnum"));
				idcardExtractMapper.insert(entity);
			}
		}
		ZipAndRarTools.delDir(targetDir);
	}

}
