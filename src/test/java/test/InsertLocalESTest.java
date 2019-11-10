package test;

import java.io.IOException;

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

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.DocumentException;

import io.gomk.Application;
import io.gomk.es6.EsUtil;
import lombok.extern.slf4j.Slf4j;
/**
 * 本地文件入es
 * @author vko
 *
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class )
public class InsertLocalESTest {
	

	@Autowired
    private EsUtil esUtil;
	
	@Test
	public void tests() {
		//招标文件 
		String zbDir = "/Users/vko/Documents/my-code/DOC/zb";
		//投标文件
		String tbDir = "/Users/vko/Documents/my-code/testDOC/tb";
		try {
			//esUtil.parseLocalZBFileSaveEs(zbDir);
			esUtil.parseLocalTBFileSaveEs(tbDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
