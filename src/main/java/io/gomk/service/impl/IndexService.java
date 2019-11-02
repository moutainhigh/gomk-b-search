package io.gomk.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.gomk.common.rs.response.ResponseData;
import io.gomk.es6.ESClientFactory;
import io.gomk.es6.EsUtil;
import io.gomk.framework.utils.parse.ImportFile;
import io.gomk.service.IIndexService;
import io.gomk.task.ESInfoBean;

@Service
public class IndexService extends EsBaseService implements IIndexService {
	@Autowired
	EsUtil esUtil;
	
	public String mapping = "  {\n" +
            "    \"_doc\": {\n" +
            "      \"properties\": {\n" +
            "        \"uuid\": {\n" +
            "          \"type\": \"keyword\"\n" +
            "        },\n" +
            "        \"title\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"analyzer\": \"ik_max_word\",\n" +
            "          \"term_vector\": \"with_positions_offsets\"\n" +
            "        },\n" +
            "        \"content\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"analyzer\": \"ik_max_word\",\n" +
            "          \"term_vector\": \"with_positions_offsets\"\n" +
            "        },\n" +
            "        \"zbfw\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"analyzer\": \"ik_max_word\",\n" +
            "          \"term_vector\": \"with_positions_offsets\"\n" +
            "        },\n" +
            "        \"tag\": {\n" +
            "          \"type\": \"text\",\n" +
            "          \"analyzer\": \"ik_max_word\"\n" +
            "        },\n" +
            "        \"pkgCode\": {\n" +
            "          \"type\": \"keyword\"\n" +
            "        },\n" +
            "        \"pkgName\": {\n" +
            "          \"type\": \"keyword\"\n" +
            "        },\n" +
            "        \"prjCode\": {\n" +
            "          \"type\": \"keyword\"\n" +
            "        },\n" +
            "        \"prjName\": {\n" +
            "          \"type\": \"keyword\"\n" +
            "        },\n" +
            "        \"prjType\": {\n" +
            "          \"type\": \"keyword\"\n" +
            "        },\n" +
            "        \"prjNature\": {\n" +
            "          \"type\": \"keyword\"\n" +
            "        },\n" +
            "        \"prjIndustry\": {\n" +
            "          \"type\": \"keyword\"\n" +
            "        },\n" +
            "        \"prjCust\": {\n" +
            "          \"type\": \"keyword\"\n" +
            "        },\n" +
            "        \"winAmount\": {\n" +
            "          \"type\": \"double\"\n" +
            "        },\n" +
            "        \"entrustAmt\": {\n" +
            "          \"type\": \"double\"\n" +
            "        },\n" +
            "        \"noticeDate\": {\n" +
            "          \"type\": \"date\",\n" +
            "          \"ignore_malformed\": true,\n" +
            "          \"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\"\n" +
            "        },\n" +
            "        \"zbType\": {\n" +
            "          \"type\": \"keyword\"\n" +
            "        },\n" +
            "        \"weight\": {\n" +
            "          \"type\": \"keyword\"\n" +
            "        },\n" +
            "        \"directoryTree\": {\n" +
            "          \"type\": \"keyword\"\n" +
            "        },\n" +
            "        \"currentPath\": {\n" +
            "          \"type\": \"keyword\"\n" +
            "        },\n" +
            "        \"addDate\": {\n" +
            "          \"type\": \"date\",\n" +
            "          \"ignore_malformed\": true,\n" +
            "          \"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\"\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }";
	
	
	@Override
	public ResponseData<String> createZBIndex() throws IOException {
		createIndex(zbIndex, mapping);
		createIndex(tbIndex, mapping);
		return ResponseData.success();
	}
	
	@Override
	public ResponseData<String> bulkZBDoc() throws IOException {
		List<Map<String, Object>> sourceList = ImportFile.getZBMap();
		bulkDoc(zbIndex, sourceList);
		return ResponseData.success();
	}

	@Override
	public ResponseData<String> deleteIndex(String zbIndex) {
//		RestHighLevelClient client = ESClientFactory.getClient();
//		DeleteRequest request = new DeleteRequest(zbIndex, "_doc", "1");
//		
//		client.delete(deleteRequest, headers)
		return null;
	}
	
	@Override
	public ResponseData<String> bulkZGYQDoc() throws IOException {
		List<Map<String, Object>> sourceList = ImportFile.getZGYQMap();
		bulkDoc(zgyqIndex, sourceList);
		return ResponseData.success();
	}

	@Override
	public ResponseData<String> bulkZJCGDoc() throws IOException {
		List<Map<String, Object>> sourceList = ImportFile.getZJCGMap();
		bulkDoc(zjcgIndex, sourceList);
		return ResponseData.success();
	}


	@Override
	public ResponseData<String> bulkJSYQDoc() throws IOException {
		List<ESInfoBean> sourceList = ImportFile.getJSYQMap();
		saveES(jsyqIndex, sourceList);
		return ResponseData.success();
	}

	private void saveES(String index, List<ESInfoBean> sourceList) {
		sourceList.forEach(bean -> {
			try {
				esUtil.saveES(index, bean);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	@Override
	public ResponseData<String> bulkPBBFDoc() throws IOException {
		List<ESInfoBean> sourceList = ImportFile.getPBBFMap();
		saveES(pbbfIndex, sourceList);
		return ResponseData.success();
	}

	private ResponseData<String> createIndex(String indexName, String mapping) throws IOException {
		// 1、创建 创建索引request 参数：索引名mess
		CreateIndexRequest request = new CreateIndexRequest(indexName);

		// 2、设置索引的settings
		request.settings(Settings.builder().put("index.number_of_shards", shards) // 分片数
				.put("index.number_of_replicas", replicas) // 副本数
				.put("index.refresh_interval", "30s")
				.put("analysis.analyzer.default.tokenizer", analyzer) // 默认分词器
		);

		// 3、设置索引的mappings
        request.mapping("_doc",
                mapping, 
                XContentType.JSON);
        
		// 4、 设置索引的别名
		//request.alias(new Alias("test_1"));

		// 5、 发送请求
		// 5.1 同步方式发送请求
		RestHighLevelClient client = ESClientFactory.getClient();
		
		GetIndexRequest request1 = new GetIndexRequest();
		request1.indices(indexName);
		if (client.indices().exists(request1)) {
			return ResponseData.error("index is exist.");
		}
		
		CreateIndexResponse createIndexResponse = client.indices().create(request);
		// 6、处理响应
        boolean acknowledged = createIndexResponse.isAcknowledged();
        boolean shardsAcknowledged = createIndexResponse
                .isShardsAcknowledged();
        System.out.println(indexName+ "==acknowledged = " + acknowledged);
        System.out.println(indexName+"==shardsAcknowledged = " + shardsAcknowledged);

        // 5.1 异步方式发送请求
        /*ActionListener<CreateIndexResponse> listener = new ActionListener<CreateIndexResponse>() {
            @Override
            public void onResponse(
                    CreateIndexResponse createIndexResponse) {
                // 6、处理响应
                boolean acknowledged = createIndexResponse.isAcknowledged();
                boolean shardsAcknowledged = createIndexResponse
                        .isShardsAcknowledged();
                System.out.println("acknowledged = " + acknowledged);
                System.out.println(
                        "shardsAcknowledged = " + shardsAcknowledged);
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("创建索引异常：" + e.getMessage());
            }
        };

        client.indices().createAsync(request, listener);
        */
        
		return ResponseData.success();
	}

	private void bulkDoc(String index,  List<Map<String, Object>> sourceList) throws IOException {
	  RestHighLevelClient client = ESClientFactory.getClient();

	  BulkRequest request = new BulkRequest(); 
	  request.setRefreshPolicy("wait_for");  
	  request.waitForActiveShards(2);
	  //request.timeout("2m");
	  int i = 1;
      for (Map<String, Object> map : sourceList) {
          request.add(new IndexRequest(index, "_doc")  
      			.source(map, XContentType.JSON));
          if (i%2 == 0) {
	          // 同步请求
	        client.bulk(request);
	        request = new BulkRequest(); 
	  		i++;
	  		continue;
	  	}
	  	if (i == sourceList.size() && i%2 != 0) {
	  		client.bulk(request);
	  	}
	  	i++;
      	
      }
        
//        //3、发送请求        
//    
//        // 同步请求
//        BulkResponse bulkResponse = client.bulk(request);
//        
//        
//        //4、处理响应
//        if(bulkResponse != null) {
//            for (BulkItemResponse bulkItemResponse : bulkResponse) { 
//                DocWriteResponse itemResponse = bulkItemResponse.getResponse(); 
//
//                if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.INDEX
//                        || bulkItemResponse.getOpType() == DocWriteRequest.OpType.CREATE) { 
//                    IndexResponse indexResponse = (IndexResponse) itemResponse;
//                    //TODO 新增成功的处理
//
//                } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.UPDATE) { 
//                    UpdateResponse updateResponse = (UpdateResponse) itemResponse;
//                   //TODO 修改成功的处理
//
//                } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.DELETE) { 
//                    DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
//                    //TODO 删除成功的处理
//                }
//            }
//        }
//        
        //异步方式发送批量操作请求
        /*
        ActionListener<BulkResponse> listener = new ActionListener<BulkResponse>() {
            @Override
            public void onResponse(BulkResponse bulkResponse) {
                
            }
        
            @Override
            public void onFailure(Exception e) {
                
            }
        };
        client.bulkAsync(request, listener);
        */
        
	}

	@Override
	public ResponseData<String> createCompletionIndex() throws IOException {
		String mapping = "  {\n" +
	            "    \"_doc\": {\n" +
	            "      \"properties\": {\n" +
	            "        \"words\": {\n" +
	            "          \"type\": \"text\",\n" +
	            "          \"analyzer\": \"ik_max_word\"" +
	            "        },\n" +
	            "        \"pkg_code\": {\n" +
	            "          \"type\": \"keyword\"\n" +
	            "        },\n" +
	            "        \"suppl_document_code\": {\n" +
	            "          \"type\": \"keyword\"\n" +
	            "        },\n" +
	            "        \"add_date\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "        }\n" +
	            "      }\n" +
	            "    }\n" +
	            "  }";
		return createIndex(completionIndex, mapping);
	}

	@Override
	public ResponseData<String> BulkCompletionDoc() throws IOException {
		List<Map<String, Object>> sourceList = ImportFile.getCompletionMap();
		bulkDoc(completionIndex, sourceList);
		return ResponseData.success();
	}

	@Override
	public ResponseData<String> bulkZCFGDoc() throws IOException {
		List<ESInfoBean> sourceList = ImportFile.getZCFGMap();
		saveES(zcfgIndex, sourceList);
		return ResponseData.success();
	}

	
	@Override
	public ResponseData<String> bulkZBFBDoc() throws IOException {
		List<ESInfoBean> sourceList = ImportFile.getZBFBMap();
		saveES(zbfbIndex, sourceList);
		return ResponseData.success();
	}

	@Override
	public ResponseData<String> createZGYQIndex() throws IOException {
		createIndex(zgyqIndex, mapping);
		return null;
	}

	@Override
	public ResponseData<String> createPBBFIndex() throws IOException {
		createIndex(pbbfIndex, mapping);
		return null;
	}

	@Override
	public ResponseData<String> createJSYQIndex() throws IOException {
		createIndex(jsyqIndex, mapping);
		return null;
	}

	@Override
	public ResponseData<String> createZJIndex() throws IOException {

		createIndex(zjcgIndex, mapping);
		return null;
	}

	@Override
	public ResponseData<String> createZCFGIndex() throws IOException {
		createIndex(zcfgIndex, mapping);
		return null;
	}

	@Override
	public ResponseData<String> createZBFBIndex() throws IOException {
		createIndex(zbfbIndex, mapping);
		return null;
	}


}
