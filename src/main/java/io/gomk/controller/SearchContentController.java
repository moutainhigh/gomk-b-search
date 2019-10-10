package io.gomk.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.gomk.common.rs.response.ResponseData;
import io.gomk.common.utils.PageResult;
import io.gomk.controller.request.ContrastRequest;
import io.gomk.controller.response.SearchResultVO;
import io.gomk.framework.controller.SuperController;
import io.gomk.framework.utils.jython.JythonUtils;
import io.gomk.service.IGCompletionService;
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
	
	@Value("${elasticsearch.index.zbName}")
	protected String zbIndex;
	@Value("${elasticsearch.index.zgyqName}")
	protected String zgyqIndex;
	@Value("${elasticsearch.index.jsyqName}")
	protected String jsyqIndex;
	@Value("${elasticsearch.index.pbbfName}")
	protected String pbbfIndex;
	@Value("${elasticsearch.index.zjName}")
	protected String zjcgIndex;
	
	@Autowired
	ISearchService searchService;
	@Autowired
	IGCompletionService completionService;
	
	@ApiOperation("搜索补全")
	@ApiImplicitParams({
		@ApiImplicitParam(name="size", value="条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="keyWord", value="关键字", required=false, paramType="query", dataType="String", defaultValue="设备")
	})
	@GetMapping("/completion")
	public ResponseData<List<String>> searchCompletion(int size, String keyWord) throws Exception {
		size = size > 10 ? 10 : size;
		//return ResponseData.success(completionService.getConmpletion(size, keyWord));
		return ResponseData.success(searchService.getConmpletion(size, keyWord));
	}
	
	@ApiOperation("标的物查询")
	@ApiImplicitParams({
		@ApiImplicitParam(name="page", value="第几页", required=true, paramType="query", dataType="int", defaultValue="1"),
		@ApiImplicitParam(name="pageSize", value="每页条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="keyWord", value="关键字", required=true, paramType="query", dataType="String", defaultValue="设备")
	})
	@GetMapping("/bdw")
	public ResponseData<PageResult<Page<List<String>>>> searchCompletion(int page, int pageSize, String keyWord) throws Exception {
		if (StringUtils.isBlank(keyWord)) {
			return ResponseData.success();
		}
		return ResponseData.success(completionService.getBdw(page, pageSize, keyWord));
	}
	
	@ApiOperation("招标文件")
	@ApiImplicitParams({
		@ApiImplicitParam(name="page", value="第几页", required=true, paramType="query", dataType="int", defaultValue="1"),
		@ApiImplicitParam(name="pageSize", value="每页条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="keyWord", value="关键字", required=true, paramType="query", dataType="String", defaultValue="设备"),
		@ApiImplicitParam(name="tag", value="标签", required=false, paramType="query", dataType="String", defaultValue="")
	})
	@GetMapping("/zbwj")
	public ResponseData<PageResult<Page<List<SearchResultVO>>>> searchZB(int page, int pageSize, String keyWord, String tag) throws Exception {
		if (StringUtils.isBlank(keyWord)) {
			return ResponseData.success();
		}
		return ResponseData.success(searchService.commonSearch(page, pageSize, keyWord, tag, zbIndex));
	}
	
	@ApiOperation("招标文件推荐(右边)")
	@ApiImplicitParams({
		@ApiImplicitParam(name="size", value="条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="tag", value="标签", required=false, paramType="query", dataType="String", defaultValue="中型项目")
	})
	@GetMapping("/zbwj/recommend")
	public ResponseData<PageResult<Page<List<SearchResultVO>>>> recommend(int size, String tag) throws Exception {
		return ResponseData.success(searchService.searchCommonRecommend(size, tag, zbIndex));
	}
	

	@ApiOperation("资格要求库")
	@ApiImplicitParams({
		@ApiImplicitParam(name="page", value="第几页", required=true, paramType="query", dataType="int", defaultValue="1"),
		@ApiImplicitParam(name="pageSize", value="每页条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="keyWord", value="关键字", required=true, paramType="query", dataType="String", defaultValue="王家岭煤业"),
		@ApiImplicitParam(name="tag", value="标签", required=false, paramType="query", dataType="String", defaultValue="")
	})
	@GetMapping("/zgyq")
	public ResponseData<PageResult<Page<List<SearchResultVO>>>> searchZGYQ(int page, int pageSize, String keyWord, String tag) throws Exception {
		if (StringUtils.isBlank(keyWord)) {
			return ResponseData.success();
		}
		return ResponseData.success(searchService.commonSearch(page, pageSize, keyWord, tag, zgyqIndex));
	}
	
	@ApiOperation("资格要求库推荐(右边)")
	@ApiImplicitParams({
		@ApiImplicitParam(name="size", value="条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="tag", value="标签", required=false, paramType="query", dataType="String", defaultValue="中型项目")
	})
	@GetMapping("/zgyq/recommend")
	public ResponseData<PageResult<Page<List<SearchResultVO>>>> zgyqRecommend(int size, String tag) throws Exception {
		return ResponseData.success(searchService.searchCommonRecommend(size, tag, zgyqIndex));
	}
	
	@ApiOperation("技术要求库")
	@ApiImplicitParams({
		@ApiImplicitParam(name="page", value="第几页", required=true, paramType="query", dataType="int", defaultValue="1"),
		@ApiImplicitParam(name="pageSize", value="每页条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="keyWord", value="关键字", required=true, paramType="query", dataType="String", defaultValue="王家岭煤业"),
		@ApiImplicitParam(name="tag", value="标签", required=false, paramType="query", dataType="String", defaultValue="")
	})
	@GetMapping("/jsyq")
	public ResponseData<PageResult<Page<List<SearchResultVO>>>> searchJSYQ(int page, int pageSize, String keyWord, String tag) throws Exception {
		if (StringUtils.isBlank(keyWord)) {
			return ResponseData.success();
		}
		return ResponseData.success(searchService.commonSearch(page, pageSize, keyWord, tag, jsyqIndex));
	}
	
	@ApiOperation("技术要求库推荐(右边)")
	@ApiImplicitParams({
		@ApiImplicitParam(name="size", value="条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="tag", value="标签", required=false, paramType="query", dataType="String", defaultValue="中型项目")
	})
	@GetMapping("/jsyq/recommend")
	public ResponseData<PageResult<Page<List<SearchResultVO>>>> JSYQRecommend(int size, String tag) throws Exception {
		return ResponseData.success(searchService.searchCommonRecommend(size, tag, jsyqIndex));
	}
	
	@ApiOperation("评标办法库")
	@ApiImplicitParams({
		@ApiImplicitParam(name="page", value="第几页", required=true, paramType="query", dataType="int", defaultValue="1"),
		@ApiImplicitParam(name="pageSize", value="每页条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="keyWord", value="关键字", required=true, paramType="query", dataType="String", defaultValue="王家岭煤业"),
		@ApiImplicitParam(name="tag", value="标签", required=false, paramType="query", dataType="String", defaultValue="")
	})
	@GetMapping("/pbbf")
	public ResponseData<PageResult<Page<List<SearchResultVO>>>> searchPBBF(int page, int pageSize, String keyWord, String tag) throws Exception {
		if (StringUtils.isBlank(keyWord)) {
			return ResponseData.success();
		}
		return ResponseData.success(searchService.commonSearch(page, pageSize, keyWord, tag, pbbfIndex));
	}
	
	@ApiOperation("评标办法库推荐(右边)")
	@ApiImplicitParams({
		@ApiImplicitParam(name="size", value="条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="tag", value="标签", required=false, paramType="query", dataType="String", defaultValue="中型项目")
	})
	@GetMapping("/pbbf/recommend")
	public ResponseData<PageResult<Page<List<SearchResultVO>>>> pbbfRecommend(int size, String tag) throws Exception {
		return ResponseData.success(searchService.searchCommonRecommend(size, tag, pbbfIndex));
	}
	
	@ApiOperation("造价成果办法库")
	@ApiImplicitParams({
		@ApiImplicitParam(name="page", value="第几页", required=true, paramType="query", dataType="int", defaultValue="1"),
		@ApiImplicitParam(name="pageSize", value="每页条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="keyWord", value="关键字", required=true, paramType="query", dataType="String", defaultValue="露天煤矿"),
		@ApiImplicitParam(name="tag", value="标签", required=false, paramType="query", dataType="String", defaultValue="")
	})
	@GetMapping("/zjcg")
	public ResponseData<PageResult<Page<List<SearchResultVO>>>> searchZjcg(int page, int pageSize, String keyWord, String tag) throws Exception {
		if (StringUtils.isBlank(keyWord)) {
			return ResponseData.success();
		}
		return ResponseData.success(searchService.commonSearch(page, pageSize, keyWord, tag, zjcgIndex));
	}
	
	@ApiOperation("造价成果库推荐(右边)")
	@ApiImplicitParams({
		@ApiImplicitParam(name="size", value="条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="tag", value="标签", required=false, paramType="query", dataType="String", defaultValue="中型项目")
	})
	@GetMapping("/zjcg/recommend")
	public ResponseData<PageResult<Page<List<SearchResultVO>>>> zjcgRecommend(int size, String tag) throws Exception {
		return ResponseData.success(searchService.searchCommonRecommend(size, tag, zjcgIndex));
	}
	
	@ApiOperation("文件对比")
	@PostMapping("/contrast")
	public ResponseData<String> contrast(@RequestBody ContrastRequest request) throws Exception {
		String content1 = request.getContent1();
		String content2 = request.getContent2();
		if (StringUtils.isBlank(content1) || StringUtils.isBlank(content2)) {
			return ResponseData.success();
		}
		
		return ResponseData.success(JythonUtils.getContrastResult(content1, content2));
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
