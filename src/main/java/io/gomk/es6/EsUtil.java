package io.gomk.es6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EsUtil {
	Logger log = LoggerFactory.getLogger(EsUtil.class);
	@Autowired
	protected ESRestClient esClient;

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
	        } else {
	        	//异步执行
		       // DeleteResponse  的典型监听器如下所示：
		        //异步方法不会阻塞并立即返回。
		        ActionListener<UpdateResponse > listener = new ActionListener<UpdateResponse >() {
		            @Override
		            public void onResponse(UpdateResponse  updateResponse) {
		                //执行成功时调用。 Response以参数方式提供
		            	log.info(updateResponse.getId() + ":" +updateResponse.getResult().toString());
		            }

		            @Override
		            public void onFailure(Exception e) {
		                //在失败的情况下调用。 引发的异常以参数方式提供
		            	log.info("error:" + e.getMessage());
		            }
		        };
		        //异步执行获取索引请求需要将UpdateRequest  实例和ActionListener实例传递给异步方法：
		        client.updateAsync(request, listener);
	        }
		}
		client.close();
	}
}
