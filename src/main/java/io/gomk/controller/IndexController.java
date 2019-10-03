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
	protected String zjIndex;
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
	
//	@ApiOperation("删除索引-招标文件")
//	@DeleteMapping("/zb")
//	public ResponseData<String> deleteZBIndex() throws IOException {
//		return indexService.deleteIndex(zbIndex);
//	}
	
	@ApiOperation("创建索引-资格要求")
	@PostMapping("/zgyq")
	public ResponseData<String> createZGYQIndex() throws IOException {
		
		return indexService.createZGYQIndex();
	}
	
	@ApiOperation("批量添加资格要求文件")
	@PostMapping("/zgyq/bulk")
	public ResponseData<String> bulkZGYQ() throws IOException {
		
		return indexService.bulkZGYQDoc();
	}
	
	@ApiOperation("创建索引-技术要求")
	@PostMapping("/jsyq")
	public ResponseData<String> createJSYQIndex() throws IOException {
		
		return indexService.createJSYQIndex();
	}
	
	@ApiOperation("批量添加技术要求文件")
	@PostMapping("/jsyq/bulk")
	public ResponseData<String> bulkJSYQ() throws IOException {
		
		return indexService.bulkJSYQDoc();
	}
	
	@ApiOperation("创建索引-评标办法")
	@PostMapping("/pbbf")
	public ResponseData<String> createPBBFIndex() throws IOException {
		
		return indexService.createPBBFIndex();
	}
	
	@ApiOperation("批量添加评标办法文件")
	@PostMapping("/pbbf/bulk")
	public ResponseData<String> bulkPBBF() throws IOException {
		
		return indexService.bulkPBBFDoc();
	}
	
	@ApiOperation("创建索引-造价成果库")
	@PostMapping("/zjcg")
	public ResponseData<String> createZjcgIndex() throws IOException {
		return indexService.createZJIndex();
	}
	
	@ApiOperation("批量添加造价成果")
	@PostMapping("/zjcg/bulk")
	public ResponseData<String> bulkZJCG() throws IOException {
		return indexService.bulkZJCGDoc();
	}
	

}
