package io.gomk.es6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.gomk.enums.TagClassifyScopeEnum;

@Component
public class EsUtil {
	Logger log = LoggerFactory.getLogger(EsUtil.class);
	@Autowired
	protected ESRestClient esClient;
	
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
	@Value("${elasticsearch.index.completionName}")
	protected String completionIndex;

	/*
	 * 条件更新
	 * 
	 * @return
	 */
	public void updateTagByIds(String indexName, String tag, List<String> ids, boolean async) throws IOException {
		RestHighLevelClient client = esClient.getClient();
		Set<String> idSet = new HashSet<String>(ids);
		for (String id : idSet) {
			Map<String, Object> jsonMap = new HashMap<>();
			GetRequest getRequest = new GetRequest(indexName, "_doc", id);
			GetResponse getResponse = client.get(getRequest);
			Object obj = getResponse.getSourceAsMap().get("tag");
			HashSet<String> result = new HashSet<>();
			result.add(tag);
			if (obj != null) {
				if (obj instanceof ArrayList<?>) {
					for (Object o : (List<?>) obj) {
						result.add(String.class.cast(o));
					}
				}
			}
			jsonMap.put("tag", result);

			UpdateRequest request = new UpdateRequest(indexName, "_doc", id).doc(jsonMap);
			request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
			if (!async) {
				UpdateResponse updateResponse = client.update(request);
				client.close();
			} else {
				// 异步执行
				// DeleteResponse 的典型监听器如下所示：
				// 异步方法不会阻塞并立即返回。
				ActionListener<UpdateResponse> listener = new ActionListener<UpdateResponse>() {
					@Override
					public void onResponse(UpdateResponse updateResponse) {
						// 执行成功时调用。 Response以参数方式提供
						log.info(updateResponse.getId() + ":" + updateResponse.getResult().toString());
					}

					@Override
					public void onFailure(Exception e) {
						// 在失败的情况下调用。 引发的异常以参数方式提供
						log.info("error:" + e.getMessage());
					}
				};
				// 异步执行获取索引请求需要将UpdateRequest 实例和ActionListener实例传递给异步方法：
				client.updateAsync(request, listener);
				
			}
		}
		
	}

	public List<String> getIDsByKeyword(String indexName, BoolQueryBuilder query) throws IOException {
		List<String> result = new ArrayList<>();
		RestHighLevelClient client = esClient.getClient();
		
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(query);
		sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
		sourceBuilder.fetchSource("_id", null);

//		HighlightBuilder highlightBuilder = new HighlightBuilder();
//		HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field("content");
//		highlightTitle.highlighterType("unified");
//		highlightBuilder.field(highlightTitle);
//		sourceBuilder.highlighter(highlightBuilder);

		SearchRequest searchRequest = new SearchRequest(indexName);
		searchRequest.types("_doc");
		// 将请求体加入到请求中
		searchRequest.source(sourceBuilder);

		// 3、发送请求
		SearchResponse searchResponse = client.search(searchRequest);
		// 处理搜索命中文档结果
		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHits = hits.getHits();
		for (SearchHit hit : searchHits) {
			result.add(hit.getId());

//			// 取高亮结果
//			String fragmentString = "";
//			Map<String, HighlightField> highlightFields = hit.getHighlightFields();
//			HighlightField highlight1 = highlightFields.get("content");
//			if (highlight1 != null) {
//				Text[] fragments1 = highlight1.fragments();
//				fragmentString = fragments1[0].string();
//				log.info("fragments1 size:" + fragments1.length);
//				for (Text t : fragments1) {
//					log.info("fragmentString1:" + t.toString());
//				}
//			}
			//log.info(hit.getSourceAsMap().get("title").toString());
		}
		client.close();

		return result;
	}
	

	public String getIndexname(int scope) throws Exception {
		TagClassifyScopeEnum scopes = TagClassifyScopeEnum.fromValue(scope);
		String indexName = "";
		switch (scopes) {
			case ZBWJ:
				indexName = zbIndex;
				break;
			case ZGYQ:
				indexName = zgyqIndex;
				break;
			case ZJCG:
				indexName = zjcgIndex;
				break;
			case JSYQ:
				indexName = jsyqIndex;
				break;
			case PBBF:
				indexName = pbbfIndex;
				break;
			case ZCFG:
				indexName = zcfgIndex;
				break;
			case ZBFB:
				indexName = zbfbIndex;
				break;
			default:
				break;
		}
		return indexName;
	}

}
