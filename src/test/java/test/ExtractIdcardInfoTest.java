package test;

import java.io.File;

import org.junit.Test;
//import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

import io.gomk.Application;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class )
public class ExtractIdcardInfoTest {
	

	@Autowired
    private RestTemplate restTemplate;
	
	@Test
	public void testParseImg() {
        String filePath = "/Users/vko/Documents/my-code/testDOC/img/4.jpg";
      //设置请求头
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);
        
      //设置请求体，注意是LinkedMultiValueMap
        FileSystemResource fileSystemResource = new FileSystemResource(filePath);
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("pic", fileSystemResource);
        //form.add("filename",fileName);

        //用HttpEntity封装整个请求报文
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);
        
        JSONObject postData = new JSONObject();
        // 调用接口即可
        JSONObject json = restTemplate.postForEntity("http://58.119.224.26:8080", files,JSONObject.class).getBody();
        log.info(json.toJSONString());
        log.info(json.getString("name"));
        log.info("end.");
	}
	
}
