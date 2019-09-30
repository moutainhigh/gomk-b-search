package io.gomk.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.gomk.common.rs.response.ResponseData;
import io.gomk.common.utils.PageResult;
import io.gomk.controller.response.SearchResultVO;
import io.gomk.framework.controller.SuperController;
import io.gomk.service.ISearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 搜索
 * </p>
 *
 * @author Robinxiao
 * @since 2019-09-08
 */
@RestController
@RequestMapping("/es/search")
@Api(description = "搜索操作")
public class SearchContentController extends SuperController {
	
	private Logger logger = LoggerFactory.getLogger(SearchContentController.class);
	
	@Autowired
	ISearchService searchService;
	
	@ApiOperation("招标文件")
	@ApiImplicitParams({
		@ApiImplicitParam(name="page", value="第几页", required=true, paramType="query", dataType="int", defaultValue="1"),
		@ApiImplicitParam(name="pageSize", value="每页条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="keyWord", value="关键字", required=false, paramType="query", dataType="String", defaultValue="设备")
	})
	@GetMapping("/zbwj")
	public ResponseData<PageResult<Page<List<SearchResultVO>>>> searchZB(int page, int pageSize, String keyWord) throws Exception {
		return ResponseData.success(searchService.searchZB(page, pageSize, keyWord));
	}
//
//	@ApiOperation("资格要求库")
//	@ApiImplicitParams({
//		@ApiImplicitParam(name="page", value="第几页", required=true, paramType="query", dataType="int", defaultValue="1"),
//		@ApiImplicitParam(name="pageSize", value="每页条数", required=true, paramType="query", dataType="int", defaultValue="10"),
//		@ApiImplicitParam(name="keyWord", value="关键字", required=false, paramType="query", dataType="String", defaultValue="设备")
//	})
//	@GetMapping("/zgyq") //资格要求
//	public ResponseData<PageResult<Page<List<SearchResultVO>>>> searchZGYQ(int page, int pageSize, String keyWord) throws Exception {
//		return ResponseData.success(searchService.searchZGYQ(page, pageSize, keyWord));
//	}
//	
//	@GetMapping("/zhuanjia") //专家库
//	public ResponseData<PageResult<Page<List<SearchResultVO>>>> searchZHUANJIA(int page, int pageSize, String keyWord) throws Exception {
//		return ResponseData.success(searchService.searchZHUANJIA(page, pageSize, keyWord));
//	}
//	
//	@GetMapping("/zbr")//招标人
//	public ResponseData<PageResult<Page<List<SearchResultVO>>>> searchZBR(int page, int pageSize, String keyWord) throws Exception {
//		return ResponseData.success(searchService.searchZBR(page, pageSize, keyWord));
//	}
//	
//	@GetMapping("/tbwj")//投标文件
//	public ResponseData<PageResult<Page<List<SearchResultVO>>>> searchTBWJ(int page, int pageSize, String keyWord) throws Exception {
//		return ResponseData.success(searchService.searchTBWJ(page, pageSize, keyWord));
//	}
//	
//	@GetMapping("/pbbf")//评标办法
//	public ResponseData<PageResult<Page<List<SearchResultVO>>>> searchPBBF(int page, int pageSize, String keyWord) throws Exception {
//		return ResponseData.success(searchService.searchPBBF(page, pageSize, keyWord));
//	}
//	
//	@GetMapping("/jsyq")//技术要求
//	public ResponseData<PageResult<Page<List<SearchResultVO>>>> searchJSYQ(int page, int pageSize, String keyWord) throws Exception {
//		return ResponseData.success(searchService.searchJSYQ(page, pageSize, keyWord));
//	}
//	
//	@GetMapping("/price")//价格
//	public ResponseData<PageResult<Page<List<SearchResultVO>>>> searchPrice(int page, int pageSize, String keyWord) throws Exception {
//		return ResponseData.success(searchService.searchPrice(page, pageSize, keyWord));
//	}
//	
//	@GetMapping("/zcfg") //政策法规
//	public ResponseData<PageResult<Page<List<SearchResultVO>>>> searchZCFG(int page, int pageSize, String keyWord) throws Exception {
//		return ResponseData.success(searchService.searchZCFG(page, pageSize, keyWord));
//	}
//	@GetMapping("/zjcg") //造价成果
//	public ResponseData<PageResult<Page<List<SearchResultVO>>>> searchZJCG(int page, int pageSize, String keyWord) throws Exception {
//		return ResponseData.success(searchService.searchZJCG(page, pageSize, keyWord));
//	}
}
