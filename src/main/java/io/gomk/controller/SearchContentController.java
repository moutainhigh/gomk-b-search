package io.gomk.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.get.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.gomk.common.rs.response.ResponseData;
import io.gomk.common.utils.PageResult;
import io.gomk.controller.response.ContrastVO;
import io.gomk.controller.response.NumberVO;
import io.gomk.controller.response.SearchResultVO;
import io.gomk.controller.response.ZgyqDetailVO;
import io.gomk.es6.EsUtil;
import io.gomk.framework.controller.SuperController;
import io.gomk.framework.utils.jython.RuntimeUtils;
import io.gomk.framework.utils.tree.TreeDto;
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
	@Value("${elasticsearch.index.zcfgName}")
	protected String zcfgIndex;
	@Value("${elasticsearch.index.zbfbName}")
	protected String zbfbIndex;
	
	@Autowired
	EsUtil esUtil;
	@Autowired
	ISearchService searchService;
	@Autowired
	IGCompletionService completionService;
	
	@ApiOperation("根据关键字得到查询结果中的所有top10标签")
	@ApiImplicitParams({
		@ApiImplicitParam(name="scope", value="1(招标文件库)2(资格要求库)3(评标办法库)4(技术要求库)5(造价成果库)6(政策法规库)7(招标范本库)", required=true, paramType="query", dataType="Integer", defaultValue="2"),
		@ApiImplicitParam(name="keyWord", value="关键字", required=true, paramType="query", dataType="String", defaultValue="设备")
	})
	@GetMapping("/result/tag")
	public ResponseData<List<TreeDto>> searchTagByKeyword(int scope, String keyWord) throws Exception {
		if (StringUtils.isBlank(keyWord)) {
			return ResponseData.success();
		}
		String indexName = esUtil.getIndexname(scope);
		return ResponseData.success(searchService.selectTagByKeyword(keyWord, indexName));
	}
	@ApiOperation("查询搜索列表详细信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name="scope", value="1(招标文件库)2(资格要求库)3(评标办法库)4(技术要求库)5(造价成果库)6(政策法规库)7(招标范本库)", required=true, paramType="query", dataType="Integer", defaultValue="2"),
		@ApiImplicitParam(name="id", value="列表id", required=true, paramType="query", dataType="String", defaultValue="xxxx")
	})
	@GetMapping("/doc/detail")
	public ResponseData<String> getDocDetail(Integer scope, String id) throws Exception {
		if (StringUtils.isBlank(id) || scope == null) {
			return ResponseData.success();
		}
		String indexName = esUtil.getIndexname(scope);
		GetResponse esResponse1 = searchService.getEsDoc(indexName, id);
		if (!esResponse1.isExists()) {
			return ResponseData.error("id is not found!");
		}
		Object obj1 = esResponse1.getSourceAsMap().get("content");
		if (obj1 == null) {
			return ResponseData.error("id or scope is error.");
		}
		return ResponseData.success(obj1.toString());
	}
	
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
	public ResponseData<PageResult<Page<List<NumberVO>>>> searchCompletion(int page, int pageSize, String keyWord) throws Exception {
		if (StringUtils.isBlank(keyWord)) {
			return ResponseData.success();
		}
		return ResponseData.success(searchService.getBdw(page, pageSize, keyWord));
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
	
	@ApiOperation("资格要求库推荐(按标签)")
	@ApiImplicitParams({
		@ApiImplicitParam(name="size", value="条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="tag", value="标签", required=false, paramType="query", dataType="String", defaultValue="中型项目")
	})
	@GetMapping("/zgyq/recommend/tag")
	public ResponseData<PageResult<Page<List<SearchResultVO>>>> zgyqRecommend(int size, String tag) throws Exception {
		return ResponseData.success(searchService.searchCommonRecommend(size, tag, zgyqIndex));
	}
	
	@ApiOperation("资格要求库推荐(按权重)")
	@ApiImplicitParams({
		@ApiImplicitParam(name="size", value="条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="keyword", value="关键字", required=false, paramType="query", dataType="String", defaultValue="中型项目")
	})
	@GetMapping("/zgyq/recommend/weight")
	public ResponseData<List<String>> zgyqRecommend2(int size, String keyword) throws Exception {
		size = size > 20 ? 20 : size;
		return ResponseData.success(searchService.searchWeightRecommend(size, keyword, zgyqIndex));
	}
	
	@ApiOperation("资格要求-列表详情")
	@ApiImplicitParams({
		@ApiImplicitParam(name="docId", value="列表ID", required=false, paramType="query", dataType="String", defaultValue="xxx")
	})
	@GetMapping("/zgyq/item/detail/{docId}")
	public ResponseData<ZgyqDetailVO> zgyqItemDetail(@PathVariable("docId") String docId) throws Exception {
		ZgyqDetailVO vo  = new ZgyqDetailVO();
		GetResponse esResponse1 = searchService.getEsDoc(zgyqIndex, docId);
		if (!esResponse1.isExists()) {
			return ResponseData.error("id is not found!");
		}
		Object zbfw = esResponse1.getSourceAsMap().get("zbfw");
		if (zbfw != null) {
			vo.setZbfw(zbfw.toString());
		}
		Object zgyq = esResponse1.getSourceAsMap().get("content");
		if (zgyq != null) {
			String str[] = zgyq.toString().split(";");
			vo.setZgyqs(Arrays.asList(str));
		}
		return ResponseData.success(vo);
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
	
	
	@ApiOperation("政策法规库")
	@ApiImplicitParams({
		@ApiImplicitParam(name="page", value="第几页", required=true, paramType="query", dataType="int", defaultValue="1"),
		@ApiImplicitParam(name="pageSize", value="每页条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="keyWord", value="关键字", required=true, paramType="query", dataType="String", defaultValue="露天煤矿"),
		@ApiImplicitParam(name="tag", value="标签", required=false, paramType="query", dataType="String", defaultValue="")
	})
	@GetMapping("/zcfg")
	public ResponseData<PageResult<Page<List<SearchResultVO>>>> searchZcfg(int page, int pageSize, String keyWord, String tag) throws Exception {
		if (StringUtils.isBlank(keyWord)) {
			return ResponseData.success();
		}
		return ResponseData.success(searchService.commonSearch(page, pageSize, keyWord, tag, zcfgIndex));
	}
	
	@ApiOperation("政策法规库推荐(右边)")
	@ApiImplicitParams({
		@ApiImplicitParam(name="size", value="条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="tag", value="标签", required=false, paramType="query", dataType="String", defaultValue="中型项目")
	})
	@GetMapping("/zcfg/recommend")
	public ResponseData<PageResult<Page<List<SearchResultVO>>>> zcfgRecommend(int size, String tag) throws Exception {
		return ResponseData.success(searchService.searchCommonRecommend(size, tag, zcfgIndex));
	}

	@ApiOperation("招标范本库")
	@ApiImplicitParams({
		@ApiImplicitParam(name="page", value="第几页", required=true, paramType="query", dataType="int", defaultValue="1"),
		@ApiImplicitParam(name="pageSize", value="每页条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="keyWord", value="关键字", required=true, paramType="query", dataType="String", defaultValue="露天煤矿"),
		@ApiImplicitParam(name="tag", value="标签", required=false, paramType="query", dataType="String", defaultValue="")
	})
	@GetMapping("/zbfb")
	public ResponseData<PageResult<Page<List<SearchResultVO>>>> searchZbfb(int page, int pageSize, String keyWord, String tag) throws Exception {
		if (StringUtils.isBlank(keyWord)) {
			return ResponseData.success();
		}
		return ResponseData.success(searchService.commonSearch(page, pageSize, keyWord, tag, zbfbIndex));
	}
	
	@ApiOperation("招标范本库推荐(右边)")
	@ApiImplicitParams({
		@ApiImplicitParam(name="size", value="条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="tag", value="标签", required=false, paramType="query", dataType="String", defaultValue="中型项目")
	})
	@GetMapping("/zbfb/recommend")
	public ResponseData<PageResult<Page<List<SearchResultVO>>>> zbfbRecommend(int size, String tag) throws Exception {
		return ResponseData.success(searchService.searchCommonRecommend(size, tag, zbfbIndex));
	}
	@ApiOperation("文件对比")
	@ApiImplicitParams({
		@ApiImplicitParam(name="scope", value="2(资格要求库)3(评标办法库)4(技术要求库)5(造价成果库)6(政策法规库)7(招标范本库)", required=true, paramType="query", dataType="Integer", defaultValue="2"),
		@ApiImplicitParam(name="id1", value="id1", required=true, paramType="query", dataType="String", defaultValue="K0y8j20BsIxgh5km1CLR"),
		@ApiImplicitParam(name="id2", value="id2", required=true, paramType="query", dataType="String", defaultValue="Kky8j20BsIxgh5km1CLR")
	})
	@GetMapping("/contrast")
	public ResponseData<ContrastVO> contrast(int scope, String id1, String id2) throws Exception {
		if (StringUtils.isBlank(id1) || StringUtils.isBlank(id2)) {
			return ResponseData.error("请选择条目!");
		}
		String indexName = esUtil.getIndexname(scope);
		GetResponse esResponse1 = searchService.getEsDoc(indexName, id1);
		GetResponse esResponse2 = searchService.getEsDoc(indexName, id2);
		if (!esResponse1.isExists() || !esResponse2.isExists()) {
			return ResponseData.error("id is not found!");
		}
		Object obj1 = esResponse1.getSourceAsMap().get("content");
		Object obj2 = esResponse2.getSourceAsMap().get("content");
		if (obj1 == null || obj2 == null) {
			return ResponseData.error("id or scope is error.");
		}
		return ResponseData.success(RuntimeUtils.getContrastResult(obj1.toString(), obj2.toString()));
	}
	
//	
//	@ApiOperation("文件对比")
//	@PostMapping("/contrast")
//	public ResponseData<String> contrast(@RequestBody ContrastRequest request) throws Exception {
//		String content1 = request.getContent1();
//		String content2 = request.getContent2();
//		if (StringUtils.isBlank(content1) || StringUtils.isBlank(content2)) {
//			return ResponseData.success();
//		}
//		
//		return ResponseData.success(RuntimeUtils.getContrastResult(content1, content2));
//	}
	
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
