package io.gomk.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.gomk.common.rs.response.ResponseData;
import io.gomk.framework.utils.parse.ImportFile;
import io.gomk.service.IIndexService;

@Service
public class IndexService extends EsBaseService implements IIndexService {
	
	@Override
	public ResponseData<String> createZBIndex() throws IOException {
		String mapping = "  {\n" +
	            "    \"_doc\": {\n" +
	            "      \"properties\": {\n" +
	            "        \"title\": {\n" +
	            "          \"type\": \"text\",\n" +
	            "          \"analyzer\": \"hanlp\",\n" +
	            "          \"term_vector\": \"with_positions_offsets\"\n" +
	            "        },\n" +
	            "        \"content\": {\n" +
	            "          \"type\": \"text\",\n" +
	            "          \"analyzer\": \"hanlp\",\n" +
	            "          \"term_vector\": \"with_positions_offsets\"\n" +
	            "        },\n" +
	            "        \"tag\": {\n" +
	            "          \"type\": \"text\",\n" +
	            "          \"analyzer\": \"hanlp\"\n" +
	            "        },\n" +
	            "        \"abstract\": {\n" +
	            "          \"type\": \"keyword\"\n" +
	            "        },\n" +
	            "        \"add_date\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "        }\n" +
	            "      }\n" +
	            "    }\n" +
	            "  }";
		return createIndex(zbIndex, mapping);
	}
	
	@Override
	public ResponseData<String> bulkZBDoc() throws IOException {
		List<Map<String, Object>> sourceList = ImportFile.getZBMap();
		bulkDoc(zbIndex, sourceList);
		return ResponseData.success();
	}

	@Override
	public ResponseData<String> deleteIndex(String zbIndex) {
//		RestHighLevelClient client = esClient.getClient();
//		DeleteRequest request = new DeleteRequest(zbIndex, "_doc", "1");
//		
//		client.delete(deleteRequest, headers)
		return null;
	}
	
	@Override
	public ResponseData<String> createZGYQIndex() throws IOException {
		String mapping = "  {\n" +
                "    \"_doc\": {\n" +
                "      \"properties\": {\n" +
                "        \"title\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"analyzer\": \"hanlp\",\n" +
                "          \"term_vector\": \"with_positions_offsets\"\n" +
                "        },\n" +
                "        \"zbfw\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"analyzer\": \"hanlp\",\n" +
                "          \"term_vector\": \"with_positions_offsets\"\n" +
                "        },\n" +
                "        \"zgyq\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"analyzer\": \"hanlp\",\n" +
                "          \"term_vector\": \"with_positions_offsets\"\n" +
                "        },\n" +
                "        \"content\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"analyzer\": \"hanlp\",\n" +
                "          \"term_vector\": \"with_positions_offsets\"\n" +
                "        },\n" +
                "        \"tag\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "        },\n" +
                "        \"add_date\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }";
		return createIndex(zgyqIndex, mapping);
	}

	@Override
	public ResponseData<String> bulkZGYQDoc() throws IOException {
		List<Map<String, Object>> sourceList = ImportFile.getZGYQMap();
		bulkDoc(zgyqIndex, sourceList);
		return ResponseData.success();
	}

	@Override
	public ResponseData<String> createPBBFIndex() throws IOException {
		String mapping = "  {\n" +
                "    \"_doc\": {\n" +
                "      \"properties\": {\n" +
                "        \"title\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"analyzer\": \"hanlp\",\n" +
                "          \"term_vector\": \"with_positions_offsets\"\n" +
                "        },\n" +
                "        \"content\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"analyzer\": \"hanlp\",\n" +
                "          \"term_vector\": \"with_positions_offsets\"\n" +
                "        },\n" +
                "        \"tag\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "        },\n" +
                "        \"add_date\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }";
		return createIndex(pbbfIndex, mapping);
	}

	@Override
	public ResponseData<String> createJSYQIndex() throws IOException {
		String mapping = "  {\n" +
                "    \"_doc\": {\n" +
                "      \"properties\": {\n" +
                "        \"title\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"analyzer\": \"hanlp\",\n" +
                "          \"term_vector\": \"with_positions_offsets\"\n" +
                "        },\n" +
                "        \"content\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"analyzer\": \"hanlp\",\n" +
                "          \"term_vector\": \"with_positions_offsets\"\n" +
                "        },\n" +
                "        \"tag\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "        },\n" +
                "        \"add_date\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }";
		return createIndex(jsyqIndex, mapping);
	}

	@Override
	public ResponseData<String> createZJIndex() throws IOException {
		String mapping = "  {\n" +
                "    \"_doc\": {\n" +
                "      \"properties\": {\n" +
                "        \"title\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"analyzer\": \"hanlp\",\n" +
                "          \"search_analyzer\": \"hanlp\",\n" +
                "          \"term_vector\": \"with_positions_offsets\"\n" +
                "        },\n" +
                "        \"content\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"analyzer\": \"hanlp\",\n" +
                "          \"search_analyzer\": \"hanlp\",\n" +
                "          \"term_vector\": \"with_positions_offsets\"\n" +
                "        },\n" +
                "        \"tag\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "        },\n" +
                "        \"file_url\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "        },\n" +
                "        \"add_date\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }";
		return createIndex(zjIndex, mapping);
	}

	@Override
	public ResponseData<String> bulkZJCGDoc() throws IOException {
		List<Map<String, Object>> sourceList = ImportFile.getZJCGMap();
		bulkDoc(zjIndex, sourceList);
		return ResponseData.success();
	}


	@Override
	public ResponseData<String> bulkJSYQDoc() throws IOException {
		List<Map<String, Object>> sourceList = ImportFile.getJSYQMap();
		bulkDoc(jsyqIndex, sourceList);
		return ResponseData.success();
	}

	@Override
	public ResponseData<String> bulkPBBFDoc() throws IOException {
		List<Map<String, Object>> sourceList = ImportFile.getPBBFMap();
		bulkDoc(pbbfIndex, sourceList);
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
		RestHighLevelClient client = esClient.getClient();
		
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
        System.out.println("acknowledged = " + acknowledged);
        System.out.println("shardsAcknowledged = " + shardsAcknowledged);

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
        client.close();
		return ResponseData.success();
	}

	private void bulkDoc(String index,  List<Map<String, Object>> sourceList) throws IOException {
	  RestHighLevelClient client = esClient.getClient();

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
        client.close();
	}

	@Override
	public ResponseData<String> createCompletionIndex() throws IOException {
		String mapping = "  {\n" +
	            "    \"_doc\": {\n" +
	            "      \"properties\": {\n" +
	            "        \"words\": {\n" +
	            "          \"type\": \"text\",\n" +
	            "          \"analyzer\": \"hanlp\"" +
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
	public ResponseData<String> createZCFGIndex() throws IOException {
		String mapping = "  {\n" +
                "    \"_doc\": {\n" +
                "      \"properties\": {\n" +
                "        \"title\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"analyzer\": \"hanlp\",\n" +
                "          \"term_vector\": \"with_positions_offsets\"\n" +
                "        },\n" +
                "        \"content\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"analyzer\": \"hanlp\",\n" +
                "          \"term_vector\": \"with_positions_offsets\"\n" +
                "        },\n" +
                "        \"tag\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "        },\n" +
                "        \"add_date\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }";
		return createIndex(zcfgIndex, mapping);
	}

	@Override
	public ResponseData<String> bulkZCFGDoc() throws IOException {
		List<Map<String, Object>> sourceList = ImportFile.getZCFGMap();
		bulkDoc(zcfgIndex, sourceList);
		return ResponseData.success();
	}

	@Override
	public ResponseData<String> createZBFBIndex() throws IOException {
		String mapping = "  {\n" +
                "    \"_doc\": {\n" +
                "      \"properties\": {\n" +
                "        \"title\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"analyzer\": \"hanlp\",\n" +
                "          \"term_vector\": \"with_positions_offsets\"\n" +
                "        },\n" +
                "        \"content\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"analyzer\": \"hanlp\",\n" +
                "          \"term_vector\": \"with_positions_offsets\"\n" +
                "        },\n" +
                "        \"tag\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "        },\n" +
                "        \"add_date\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }";
		return createIndex(zbfbIndex, mapping);
	}

	@Override
	public ResponseData<String> bulkZBFBDoc() throws IOException {
		List<Map<String, Object>> sourceList = ImportFile.getZBFBMap();
		bulkDoc(zbfbIndex, sourceList);
		return ResponseData.success();
	}


}
