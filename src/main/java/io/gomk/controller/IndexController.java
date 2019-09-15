package io.gomk.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.gomk.common.rs.response.ResponseData;
import io.gomk.es6.ESRestClient;
import io.gomk.framework.controller.SuperController;
import io.gomk.framework.utils.parse.ImportFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 索引操作
 * </p>
 *
 * @author Robinxiao
 * @since 2019-09-08
 */
@RestController
@RequestMapping("/es/index")
@Api(description = "索引操作")
public class IndexController extends SuperController {

	@Autowired
	ESRestClient esClient;
	@Value("${elasticsearch.index.zbName}")
    private String zbIndex;
	@Value("${elasticsearch.shards}")
    private Integer shards;
	@Value("${elasticsearch.replicas}")
    private Integer replicas;
	@Value("${elasticsearch.analyzer}")
    private String analyzer;
	
	
	@ApiOperation("create index")
	@PostMapping("/")
	public ResponseData<String> create() throws IOException {
		// 1、创建 创建索引request 参数：索引名mess
		CreateIndexRequest request = new CreateIndexRequest(zbIndex);

		// 2、设置索引的settings
		request.settings(Settings.builder().put("index.number_of_shards", shards) // 分片数
				.put("index.number_of_replicas", replicas) // 副本数
				.put("analysis.analyzer.default.tokenizer", analyzer) // 默认分词器
		);

		// 3、设置索引的mappings
        request.mapping("_doc",
                "  {\n" +
                "    \"_doc\": {\n" +
                "      \"properties\": {\n" +
                "        \"title\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"analyzer\": \"hanlp\"\n" +
                "          \"term_vector\": \"with_positions_offsets\"\n" +
                "        },\n" +
                "        \"content\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"analyzer\": \"hanlp\",\n" +
                "          \"term_vector\": \"with_positions_offsets\"\n" +
                "        },\n" +
                "        \"keyword_suggest\": {\n" +
                "          \"type\": \"completion\",\n" +
                "          \"analyzer\": \"hanlp\",\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }",
                
                XContentType.JSON);
        
		// 4、 设置索引的别名
		//request.alias(new Alias("test_1"));

		// 5、 发送请求
		// 5.1 同步方式发送请求
		RestHighLevelClient client = esClient.getClient();
		
		GetIndexRequest request1 = new GetIndexRequest();
		request1.indices(zbIndex);
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
	

	@ApiOperation("bulk index")
	@PostMapping("/doc/bulk")
	public ResponseData<String> bulk() throws IOException {
		
		// 1、创建批量操作请求
        BulkRequest request = new BulkRequest(); 
        request.add(new IndexRequest(zbIndex, "_doc", "1")  
                .source(XContentType.JSON,"title", "foo"));
        request.add(new IndexRequest(zbIndex, "_doc", "2")  
                .source(XContentType.JSON,"title", "商品和服务"));
        request.add(new IndexRequest(zbIndex, "_doc", "3")  
                .source(XContentType.JSON,"title", "上海华安工业（集团）公司董事长谭旭光和秘书胡花蕊来到美国纽约现代艺术博物馆参观"));
        
        bulkIndex(request);
		
		return ResponseData.success();
	}
	
	@ApiOperation("索引测试招标文件")
	@PostMapping("/bulk/zb")
	public ResponseData<String> bulkZB() throws IOException {
		
		// 1、创建批量操作请求
        BulkRequest request = new BulkRequest(); 
        
        List<Map<String, Object>> sourceList = ImportFile.getSourceMap();
        for (Map<String, Object> map : sourceList) {
        	request.add(new IndexRequest(zbIndex, "_doc")  
                    .source(map, XContentType.JSON));
        }
        /*request.add(new IndexRequest(zbIndex, "_doc", "1")  
                .source(XContentType.JSON,"title", "foo"));
        request.add(new IndexRequest(zbIndex, "_doc", "2")  
                .source(XContentType.JSON,"title", "商品和服务"));
        request.add(new IndexRequest(zbIndex, "_doc", "3")  
                .source(XContentType.JSON,"title", "上海华安工业（集团）公司董事长谭旭光和秘书胡花蕊来到美国纽约现代艺术博物馆参观"));
        
        */
        
        bulkIndex(request);
		
		return ResponseData.success();
	}


	private void bulkIndex(BulkRequest request) throws IOException {
		RestHighLevelClient client = esClient.getClient();
        /*
        request.add(new DeleteRequest("mess", "_doc", "3")); 
        request.add(new UpdateRequest("mess", "_doc", "2") 
                .doc(XContentType.JSON,"other", "test"));
        request.add(new IndexRequest("mess", "_doc", "4")  
                .source(XContentType.JSON,"field", "baz"));
        */
        
        // 2、可选的设置
        /*
        request.timeout("2m");
        request.setRefreshPolicy("wait_for");  
        request.waitForActiveShards(2);
        */
        
        
        //3、发送请求        
    
        // 同步请求
        BulkResponse bulkResponse = client.bulk(request);
        
        
        //4、处理响应
        if(bulkResponse != null) {
            for (BulkItemResponse bulkItemResponse : bulkResponse) { 
                DocWriteResponse itemResponse = bulkItemResponse.getResponse(); 

                if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.INDEX
                        || bulkItemResponse.getOpType() == DocWriteRequest.OpType.CREATE) { 
                    IndexResponse indexResponse = (IndexResponse) itemResponse;
                    //TODO 新增成功的处理

                } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.UPDATE) { 
                    UpdateResponse updateResponse = (UpdateResponse) itemResponse;
                   //TODO 修改成功的处理

                } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.DELETE) { 
                    DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
                    //TODO 删除成功的处理
                }
            }
        }
        
        
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

}
