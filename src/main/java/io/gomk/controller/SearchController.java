package io.gomk.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.gomk.common.rs.response.ResponseData;
import io.gomk.common.utils.PageResult;
import io.gomk.controller.response.SearchResultVO;
import io.gomk.es6.ESRestClient;
import io.gomk.framework.controller.SuperController;
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
public class SearchController extends SuperController {
	
	private Logger logger = LoggerFactory.getLogger(SearchController.class);
	
	@Autowired
	ESRestClient esClient;
	@Value("${elasticsearch.index.zbName}")
    private String zbIndex;	
	@Value("${elasticsearch.index.zjName}")
    private String zjcgIndex;
	@Value("${elasticsearch.index.zgyqName}")
    private String zgyqIndex;
	@Value("${elasticsearch.analyzer}")
    private String analyzer;
	
	@ApiOperation("招标文件")
	@ApiImplicitParams({
		@ApiImplicitParam(name="page", value="第几页", required=true, paramType="query", dataType="int", defaultValue="1"),
		@ApiImplicitParam(name="pageSize", value="每页条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="keyWord", value="关键字", required=false, paramType="query", dataType="String", defaultValue="设备")
	})
	@GetMapping("/zb")
	public ResponseData<PageResult<Page<List<SearchResultVO>>>> searchZB(int page, int pageSize, String keyWord) throws IOException {
		return execSearch(zbIndex, page, pageSize, keyWord, false);
	}

	@ApiOperation("资格要求库")
	@ApiImplicitParams({
		@ApiImplicitParam(name="page", value="第几页", required=true, paramType="query", dataType="int", defaultValue="1"),
		@ApiImplicitParam(name="pageSize", value="每页条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="keyWord", value="关键字", required=false, paramType="query", dataType="String", defaultValue="设备")
	})
	@GetMapping("/zgyq")
	public ResponseData<PageResult<Page<List<SearchResultVO>>>> searchZGYQ(int page, int pageSize, String keyWord) throws IOException {
		return execSearch(zgyqIndex, page, pageSize, keyWord, true);
	}
	@ApiOperation("造价成果库")
	@ApiImplicitParams({
		@ApiImplicitParam(name="page", value="第几页", required=true, paramType="query", dataType="int", defaultValue="1"),
		@ApiImplicitParam(name="pageSize", value="每页条数", required=true, paramType="query", dataType="int", defaultValue="10"),
		@ApiImplicitParam(name="keyWord", value="关键字", required=false, paramType="query", dataType="String", defaultValue="设备")
	})
	@GetMapping("/zjcg")
	public ResponseData<PageResult<Page<List<SearchResultVO>>>> searchZJCG(int page, int pageSize, String keyWord) throws IOException {
		//return execSearch(zjcgIndex, page, pageSize, keyWord);
		return execSearch(zjcgIndex, page, pageSize, keyWord, false);
	}

	private ResponseData<PageResult<Page<List<SearchResultVO>>>> execSearch(String indexName, int page, int pageSize, String keyWord, Boolean bl) throws IOException{
		RestHighLevelClient client = esClient.getClient();
		List<SearchResultVO> result = new ArrayList<>();
		 // 1、创建search请求
        //SearchRequest searchRequest = new SearchRequest();
        SearchRequest searchRequest = new SearchRequest(indexName); 
        searchRequest.types("_doc");
      
        
        // 2、用SearchSourceBuilder来构造查询请求体 ,请仔细查看它的方法，构造各种查询的方法都在这。
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder(); 
        
        //构造QueryBuilder
        /*QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("user", "kimchy")
                .fuzziness(Fuzziness.AUTO)
                .prefixLength(3)
                .maxExpansions(10);
        sourceBuilder.query(matchQueryBuilder);*/
        
        //sourceBuilder.query(QueryBuilders.matchAllQuery());
        		//.matchQuery("华安工业", "title")); 
        //sourceBuilder.query(QueryBuilders.matchQuery("content", keyWord).operator(Operator.AND));
        //sourceBuilder.query(QueryBuilders.termQuery("title", "华安工业"));
        sourceBuilder.query(QueryBuilders.multiMatchQuery(keyWord, "title", "content").operator(Operator.AND));
        sourceBuilder.from((page-1)*pageSize); 
        sourceBuilder.size(pageSize); 
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS)); 
        //是否返回_source字段
        //sourceBuilder.fetchSource(false);
        
        //设置返回哪些字段
        /*String[] includeFields = new String[] {"title", "user", "innerObject.*"};
        String[] excludeFields = new String[] {"_type"};
        sourceBuilder.fetchSource(includeFields, excludeFields);*/
        
        //指定排序
        //sourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC)); 
        //sourceBuilder.sort(new FieldSortBuilder("_uid").order(SortOrder.ASC));
        
        // 设置返回 profile 
        //sourceBuilder.profile(true);
        
        //将请求体加入到请求中
        searchRequest.source(sourceBuilder);
        
        // 可选的设置
        //searchRequest.routing("routing");
        // 高亮设置
       
        HighlightBuilder highlightBuilder = new HighlightBuilder(); 
        HighlightBuilder.Field highlightTitle =
                new HighlightBuilder.Field("title"); 
        highlightTitle.highlighterType("unified");  
        highlightBuilder.field(highlightTitle);  
        HighlightBuilder.Field highlightContent = new HighlightBuilder.Field("content");
        highlightBuilder.field(highlightContent);
        sourceBuilder.highlighter(highlightBuilder);
        
       /* HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);
        highlightBuilder.preTags("<span style=\"color:red\">");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);*/
     
        
        
        //加入聚合
        /*TermsAggregationBuilder aggregation = AggregationBuilders.terms("by_company")
                .field("company.keyword");
        aggregation.subAggregation(AggregationBuilders.avg("average_age")
                .field("age"));
        sourceBuilder.aggregation(aggregation);*/
        
        //做查询建议
        /*SuggestionBuilder termSuggestionBuilder =
                SuggestBuilders.termSuggestion("user").text("kmichy"); 
            SuggestBuilder suggestBuilder = new SuggestBuilder();
            suggestBuilder.addSuggestion("suggest_user", termSuggestionBuilder); 
        sourceBuilder.suggest(suggestBuilder);*/
        
        //3、发送请求        
        SearchResponse searchResponse = client.search(searchRequest);
        
        
        //4、处理响应
        //搜索结果状态信息
        RestStatus status = searchResponse.status();
        TimeValue took = searchResponse.getTook();
        Boolean terminatedEarly = searchResponse.isTerminatedEarly();
        boolean timedOut = searchResponse.isTimedOut();
        
        //分片搜索情况
        int totalShards = searchResponse.getTotalShards();
        int successfulShards = searchResponse.getSuccessfulShards();
        int failedShards = searchResponse.getFailedShards();
        for (ShardSearchFailure failure : searchResponse.getShardFailures()) {
            // failures should be handled here
        }
        
        String fragmentString = "";
        //处理搜索命中文档结果
        SearchHits hits = searchResponse.getHits();
        
        long totalHits = hits.getTotalHits();
        float maxScore = hits.getMaxScore();
        
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            // do something with the SearchHit
        	SearchResultVO vo = new SearchResultVO();
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            
            //取_source字段值
            //String sourceAsString = hit.getSourceAsString(); //取成json串
            Map<String, Object> sourceAsMap = hit.getSourceAsMap(); // 取成map对象
            //从map中取字段值
            /*
            String documentTitle = (String) sourceAsMap.get("title"); 
            List<Object> users = (List<Object>) sourceAsMap.get("user");
            Map<String, Object> innerObject = (Map<String, Object>) sourceAsMap.get("innerObject");
            */
            logger.info("index:" + index + "  type:" + type + "  id:" + id);
         //   logger.info(sourceAsString);
            
            vo.setTitle(sourceAsMap.get("title").toString());
            vo.setAddDate(sourceAsMap.get("add_date").toString());
            vo.setTag(sourceAsMap.get("tag").toString());
            Object fileUrl = sourceAsMap.get("file_url");
            vo.setFileUrl(fileUrl == null ? "" : fileUrl.toString());
            //取高亮结果
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlight1 = highlightFields.get("title");
            if (highlight1 != null) {
            	Text[] fragments1 = highlight1.fragments();  
            	fragmentString = fragments1[0].string();
            	logger.info("fragments1 size:" + fragments1.length);
            	logger.info("fragmentString1:" + fragmentString);
            	vo.setTitle(fragmentString);
            }
 
            if (indexName.equals(zgyqIndex)) {
            	String text = sourceAsMap.get("content").toString();
            	if (text.indexOf("资格要求") != -1) {
            		vo.setZgyqInfo(text.substring(text.indexOf("资格要求")));
            		vo.setZbfwInfo(text.substring(0, text.indexOf("资格要求")));
            	}
        	} 
            
    		HighlightField highlight2 = highlightFields.get("content"); 
            if (highlight2 != null) {
            	Text[] fragments2 = highlight2.fragments();  
            	fragmentString = fragments2[0].string();
            	logger.info("fragments1 size:" + fragments2.length);
            	logger.info("fragmentString1:" + fragmentString);
            	vo.setContent(fragmentString);
            }
            
            result.add(vo);
        }
        
        // 获取聚合结果
        /*
        Aggregations aggregations = searchResponse.getAggregations();
        Terms byCompanyAggregation = aggregations.get("by_company"); 
        Bucket elasticBucket = byCompanyAggregation.getBucketByKey("Elastic"); 
        Avg averageAge = elasticBucket.getAggregations().get("average_age"); 
        double avg = averageAge.getValue();
        */
        
        // 获取建议结果
        /*Suggest suggest = searchResponse.getSuggest(); 
        TermSuggestion termSuggestion = suggest.getSuggestion("suggest_user"); 
        for (TermSuggestion.Entry entry : termSuggestion.getEntries()) { 
            for (TermSuggestion.Entry.Option option : entry) { 
                String suggestText = option.getText().string();
            }
        }
        */
        
        //异步方式发送获查询请求
        /*
        ActionListener<SearchResponse> listener = new ActionListener<SearchResponse>() {
            @Override
            public void onResponse(SearchResponse getResponse) {
                //结果获取
            }
        
            @Override
            public void onFailure(Exception e) {
                //失败处理
            }
        };
        client.searchAsync(searchRequest, listener); 
        */
  
        client.close();
        PageResult pageResult = new PageResult(page, pageSize, totalHits, result);
		return ResponseData.success(pageResult);
	}
	

	

}
