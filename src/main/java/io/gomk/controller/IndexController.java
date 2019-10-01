package io.gomk.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.gomk.common.rs.response.ResponseData;
import io.gomk.es6.ESRestClient;
import io.gomk.framework.controller.SuperController;
import io.gomk.service.IIndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 索引操作
 * </p>
 *
 * @author Robinxiao
 * @since 2019-09-08
 */
@RestController
@RequestMapping("/es/index")
@Api(description = "索引操作")
public class IndexController extends SuperController {

	@Autowired
	IIndexService indexService;
	@Autowired
	protected ESRestClient esClient;
	@Value("${elasticsearch.index.zbName}")
	protected String zbIndex;
	@Value("${elasticsearch.index.zgyqName}")
	protected String zgyqIndex;
	@Value("${elasticsearch.index.zjName}")
	
	@ApiOperation("创建索引-招标文件")
	@PostMapping("/zb")
	public ResponseData<String> createZBIndex() throws IOException {
		return indexService.createZBIndex();
	}

	@ApiOperation("批量添加招标文件")
	@PostMapping("/zb/bulk")
	public ResponseData<String> bulkZB() throws IOException {
		
		return indexService.bulkZBDoc();
	}
	
	@ApiOperation("删除索引-招标文件")
	@DeleteMapping("/zb")
	public ResponseData<String> deleteZBIndex() throws IOException {
		return indexService.deleteIndex(zbIndex);
	}
//	@ApiOperation("创建索引-资格要求")
//	@PostMapping("/zgyq")
//	public ResponseData<String> createZGYQIndex() throws IOException {
//		
//		return createIndex(zgyqIndex, mapping);
//	}
//	
//	@ApiOperation("创建索引-造价成果库")
//	@PostMapping("/zjcg")
//	public ResponseData<String> createZjcgIndex() throws IOException {
//		
//		return createIndex(zjcgIndex, mapping);
//	}
//	

	
	
//	@ApiOperation("批量添加资格要求")
//	@PostMapping("/zgyq/bulk")
//	public ResponseData<String> bulkZGYQ() throws IOException {
//		
//		// 1、创建批量操作请求
//        BulkRequest request = new BulkRequest(); 
//        
//        List<Map<String, Object>> sourceList = ImportFile.getZGYQMap();
//        int i = 1;
//        for (Map<String, Object> map : sourceList) {
//        	request.add(new IndexRequest(zgyqIndex, "_doc")  
//                    .source(map, XContentType.JSON));
//        	if (i%5 == 0) {
//        		bulkIndex(request);
//        		request = new BulkRequest(); 
//        		i++;
//        		continue;
//        	}
//        	if (i == map.size() && i%5 != 0) {
//        		bulkIndex(request);
//        	}
//        	i++;
//        }
//		
//		return ResponseData.success();
//	}
//
//	@ApiOperation("批量添加造价成果")
//	@PostMapping("/zj/bulk")
//	public ResponseData<String> bulkZJ() throws IOException {
//		
//		// 1、创建批量操作请求
//        
//        
//		BulkRequest request = new BulkRequest(); 
//        List<Map<String, Object>> sourceList = ImportFile.getZJMap();
//        int i = 1;
//        for (Map<String, Object> map : sourceList) {
//        	request.add(new IndexRequest(zjcgIndex, "_doc")  
//        			.source(map, XContentType.JSON));
//        	if (i%2 == 0) {
//        		bulkIndex(request);
//        		request = new BulkRequest(); 
//        		//i++;
//        		continue;
//        	}
//        	if (i == map.size() && i%2 != 0) {
//        		bulkIndex(request);
//        	}
//        	i++;
//        }
//        request.timeout("0");
//		return ResponseData.success();
//	}


	

}
