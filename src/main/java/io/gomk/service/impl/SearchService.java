package io.gomk.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;

import io.gomk.common.utils.PageResult;
import io.gomk.controller.response.SearchResultVO;
import io.gomk.service.ISearchService;

@Service
public class SearchService extends EsBaseService implements ISearchService {

	private Logger logger = LoggerFactory.getLogger(SearchService.class);

	
	@Override
	public PageResult<Page<List<SearchResultVO>>> commonSearch(int page, int pageSize, String keyWord, String tag, String indexName) throws Exception {

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
        
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(keyWord)) {
        	List<Term> termList = IndexTokenizer.segment(keyWord);
        	String words = "";
        	for (Term t : termList) {
        		words += t.word + " ";
        	}
        	MultiMatchQueryBuilder matchQueryBuilder = QueryBuilders.multiMatchQuery(words, "title", "content")
        			.operator(Operator.AND)
        			.analyzer(analyzer);
        	query.must(matchQueryBuilder);
        }
        if (StringUtils.isNotBlank(tag)) {
        	query.must(QueryBuilders.matchQuery("tag", tag).analyzer(analyzer));
        }
        sourceBuilder.query(query);
        
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
		return execSearch(indexName, sourceBuilder);
	}
	
	@Override
	public PageResult<Page<List<SearchResultVO>>> searchCommonRecommend(int size, String tag, String indexName) throws IOException {
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder(); 
		QueryBuilder queryBuilder = QueryBuilders.matchQuery("tag", tag);
		sourceBuilder.query(queryBuilder);
		sourceBuilder.from(0); 
        sourceBuilder.size(size > 10 ? 10 : size); 
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS)); 
		return execSearch(indexName, sourceBuilder);
	}

	
	private PageResult<Page<List<SearchResultVO>>> execSearch(String indexName, SearchSourceBuilder sourceBuilder) throws IOException{
		RestHighLevelClient client = esClient.getClient();
		List<SearchResultVO> result = new ArrayList<>();
		 // 1、创建search请求
        //SearchRequest searchRequest = new SearchRequest();
        SearchRequest searchRequest = new SearchRequest(indexName); 
        searchRequest.types("_doc");
      
        
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
        highlightTitle.highlighterType("fvh");  
        highlightBuilder.field(highlightContent);
        highlightBuilder.requireFieldMatch(false);
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
            vo.setId(id);
            
            //取_source字段值
            //String sourceAsString = hit.getSourceAsString(); //取成json串
            Map<String, Object> sourceAsMap = hit.getSourceAsMap(); // 取成map对象
            Object abstracts = sourceAsMap.get("abstract");
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
            if (indexName.equals(zbIndex)) {
                vo.setDetail(abstracts == null ? "" : abstracts.toString());
        	} else {
        		vo.setDetail(sourceAsMap.get("content").toString());
        	}
            
            Object obj = sourceAsMap.get("tag");
            HashSet<String> tags = new HashSet<>();
		    if (obj instanceof ArrayList<?>) {
		        for (Object o : (List<?>) obj) {
		            tags.add(String.class.cast(o));
		        }
		    }
            vo.setTags(tags);
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
            
    		HighlightField highlight2 = highlightFields.get("content"); 
            if (highlight2 != null) {
            	Text[] fragments2 = highlight2.fragments();  
            	fragmentString = fragments2[0].string();
            	logger.info("fragments1 size:" + fragments2.length);
            	logger.info("fragmentString1:" + fragmentString);
            	vo.setContent(fragmentString);
            } else {
            	if (indexName.equals(zbIndex)) {
                    vo.setContent(abstracts == null ? "" : abstracts.toString());
            	} else {
            		Object zbfw = sourceAsMap.get("zbfw");
            		vo.setContent(zbfw == null ? "" : zbfw.toString());
            	}
            }
            
            if (indexName.equals(zgyqIndex)) {
            	Object zgyq = sourceAsMap.get("zgyq");
            	Object zbfw = sourceAsMap.get("zbfw");
            	vo.setZgyqInfo(zgyq == null ? "" : zgyq.toString());
            	vo.setZbfwInfo(zbfw == null ? "" : zbfw.toString());
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
        PageResult pageResult = new PageResult(sourceBuilder.from(), sourceBuilder.size(), totalHits, result);
		return pageResult;
	}

	

	
	


}