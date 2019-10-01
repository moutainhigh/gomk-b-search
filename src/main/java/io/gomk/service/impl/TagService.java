package io.gomk.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.gomk.controller.request.TagRequest;
import io.gomk.controller.response.ResultVO;
import io.gomk.mapper.TagMapper;
import io.gomk.service.ITagService;

@Service
public class TagService extends EsBaseService implements ITagService {

	Logger log = LoggerFactory.getLogger(TagService.class);
	@Autowired
	TagMapper mapper;
	@Override
	public void update(TagRequest request) {
		mapper.update(request);
		
	}

	@Override
	public void delete(Integer id) {
		mapper.delete(id);
	}

	@Override
	public void add(TagRequest request) {
		mapper.add(request);
	}

	@Override
	public ResultVO search(TagRequest request) {
		return mapper.search(request);
	}

	@Override
	public void addDocTag(String tagName, List<String> ids) throws IOException {
		RestHighLevelClient client = esClient.getClient();
		for (String id : ids) {
			
			Map<String, Object> jsonMap = new HashMap<>();
			
			GetRequest getRequest = new GetRequest(zbIndex, "_doc", id);
			GetResponse getResponse = client.get(getRequest);
			Object obj = getResponse.getSourceAsMap().get("tag");
			if (obj != null) {
				HashSet<String> result = new HashSet<>();
			    if (obj instanceof ArrayList<?>) {
			        for (Object o : (List<?>) obj) {
			            result.add(String.class.cast(o));
			        }
			    }
				result.add(tagName);
				jsonMap.put("tag", result);
				
			} else {
				HashSet<String> list = new HashSet<>();
				list.add(tagName);
				jsonMap.put("tag", list);
			}
			
	        UpdateRequest request = new UpdateRequest(zbIndex, "_doc", id).doc(jsonMap);
			UpdateResponse updateResponse = client.update(request);
	        log.info(updateResponse.getId() + ":" +updateResponse.getResult().toString());
	      //异步执行
	        //DeleteResponse  的典型监听器如下所示：
	        //异步方法不会阻塞并立即返回。
//	        ActionListener<UpdateResponse > listener = new ActionListener<UpdateResponse >() {
//	            @Override
//	            public void onResponse(UpdateResponse  updateResponse) {
//	                //执行成功时调用。 Response以参数方式提供
//	            	log.info(updateResponse.getId() + ":" +updateResponse.getResult().toString());
//	            }
//
//	            @Override
//	            public void onFailure(Exception e) {
//	                //在失败的情况下调用。 引发的异常以参数方式提供
//	            	log.info("error:" + e.getMessage());
//	            }
//	        };
//	        //异步执行获取索引请求需要将UpdateRequest  实例和ActionListener实例传递给异步方法：
//	        client.updateAsync(request, listener);
//	        
		}
		client.close();
	}


}
