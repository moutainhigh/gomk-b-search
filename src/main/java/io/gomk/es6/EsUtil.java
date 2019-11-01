package io.gomk.es6;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hankcs.hanlp.HanLP;

import io.gomk.enums.TagClassifyScopeEnum;
import io.gomk.framework.utils.FileListUtil;
import io.gomk.framework.utils.parse.PDF;
import io.gomk.framework.utils.parse.Word2003;
import io.gomk.framework.utils.parse.Word2007;
import io.gomk.framework.utils.parse.ZipUtil;
import io.gomk.framework.utils.parsefile.ParseFile;
import io.gomk.mapper.DB2ESMapper;
import io.gomk.mapper.MasterDBMapper;
import io.gomk.model.GZgyq;
import io.gomk.service.IGWordsService;
import io.gomk.service.IGZgyqService;
import io.gomk.task.DBInfoBean;
import io.gomk.task.ESInfoBean;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EsUtil {
	@Autowired
	protected ESRestClient esClient;
	@Autowired
	private ParseFile parseFileUtil;
	@Autowired
	DB2ESMapper db2esMapper;
	@Autowired
	MasterDBMapper masterDBMapper;
	@Autowired
	IGZgyqService zgyqService;
	@Autowired
	IGWordsService wordsService;
	

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

	private final static String STORE_HBASE = "HBASE";
	private final static String STORE_HDFS = "HFS";

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
			// log.info(hit.getSourceAsMap().get("title").toString());
		}

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

	/**
	 * 下截文件--抽取内容--存储es索引
	 */
	public void parseAndSaveEs() {
		int size = 10;
		while(true) {
			//查询结构化数据
			List<String> ids = db2esMapper.selectIDS(size);
			List<DBInfoBean> list = masterDBMapper.getDBInfo(ids);
			log.info("===size====" + list.size());
			if (list.size() >= 0) break;
			// 1. 下载文件 分页查询未处理的纪录
			list.forEach(bean -> {
				switch (bean.getFileType()) {
				case "zb":
					disposeZB(bean);
					break;
				case "tb":
					disposeTB(bean);
					break;
				case "zj":
					disposeZJ(bean);
					break;
				default:
					break;
				}
				db2esMapper.insertFileSign(bean.getUuid());
			});
		}
		
	}

	/**
	 * 处理招标文件
	 * 
	 * @param bean
	 */
	private void disposeZB(DBInfoBean bean) {
		String storeUrl = bean.getStoreUrl();
		String storeType = bean.getStoreType();
		String extensionName = bean.getExt();
		if (storeType.equals(STORE_HBASE)) {

		} else if (storeType.equals(STORE_HDFS)) {

		}
		File tempFile = new File("/Users/vko/Documents/my-code/DOC/zb/神华准能物资供应中心2018年第二批设备采购招标文件.doc");
		InputStream in;
		try {
			in = new FileInputStream(tempFile);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			byte[] buffer = new byte[1024];  
			int len;  
			while ((len = in.read(buffer)) > -1 ) {  
			    baos.write(buffer, 0, len);  
			}  
			baos.flush(); 
		
			ESInfoBean esBean = new ESInfoBean();
			BeanUtils.copyProperties(bean, esBean);
			esBean.setAddDate(new Date());
			//全文
			String content = getContent(new ByteArrayInputStream(baos.toByteArray()), extensionName);
			
			//招标范围
			String zbfw = parseFileUtil.parseTenderScope(new ByteArrayInputStream(baos.toByteArray()), extensionName);
			//资格要求
			List<String> zgyqList = parseFileUtil.parseTenderQualification(new ByteArrayInputStream(baos.toByteArray()), extensionName);
			//评标办法
			String pbbf = parseFileUtil.parseTenderMethod(new ByteArrayInputStream(baos.toByteArray()), extensionName);
			//技术要求
			String jsyq = parseFileUtil.parseTechnicalRequirement(new ByteArrayInputStream(baos.toByteArray()), extensionName);
			
//			log.info("zbfw====" + zbfw);
//			log.info("zgyqList=======" + zgyqList.toString());
//			log.info("pbbf=====" + pbbf);
//			log.info("jsyq=====" + jsyq);
			
			//esBean.setZbfw(zbfw);
			//========存招标文件库=======
			if (StringUtils.isNotBlank(content)) {
				esBean.setContent(content);
				List<String> phraseList = HanLP.extractPhrase(content, 2);
				//保存新词
				wordsService.saveByList(phraseList);
				saveES(zbIndex, esBean);
			}
		
		
			//==========存索引-资格要求及按条存数据库=======
			if (zgyqList != null && zgyqList.size() > 0) {
				esBean.setContent(zgyqList.toString());
				saveES(zgyqIndex, esBean);
				zgyqList.forEach(str ->{
					QueryWrapper<GZgyq> query = new QueryWrapper<>();
					query.lambda()
			    		.eq(GZgyq::getContent, str);
					GZgyq zgyqItem = zgyqService.getOne(query);
					if (zgyqItem != null) {
						zgyqItem.setAmount(zgyqItem.getAmount() + 1);
						zgyqService.updateById(zgyqItem);
					} else {
						zgyqItem = new GZgyq();
						zgyqItem.setAmount(0);
						zgyqItem.setContent(str);
						zgyqService.save(zgyqItem);
					}
				});
				
//========	??????????存数据库
			}
			//=====存索引-评标办法===========
			if (StringUtils.isNotBlank(pbbf)) {
				esBean.setContent(pbbf);
				saveES(pbbfIndex, esBean);
			}
			//=======存索引-技术要求=========
			if (StringUtils.isNotBlank(jsyq)) {
				esBean.setContent(jsyq);
				saveES(jsyqIndex, esBean);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 处理投标文件
	 * 
	 * @param bean
	 */
	private void disposeTB(DBInfoBean bean) {

	}

	/**
	 * 处理造价文件
	 * 
	 * @param bean
	 */
	private void disposeZJ(DBInfoBean bean) {
		if (bean.getExt().equals("zip")) {
			String path = "/Users/vko/Documents/my-code/temp/1.zip";
			String targetDir = "/Users/vko/Documents/my-code/temp/2";
			try {
				ZipUtil.unZipFiles(path, targetDir);
			
				List<File> files = new ArrayList<>();
				StringBuffer sb = new StringBuffer();
				FileListUtil.findDir(targetDir, 3, files, sb);
				
				for (File f : files) {
					if (f.isFile()) {
						String fileName = f.getName();
						String content = "";
						if (fileName.endsWith(".doc")) {
							content = Word2003.read(f.getAbsolutePath());
						} else if (fileName.endsWith(".docx")) {
							content = Word2007.read(f.getAbsolutePath());
						} else if (fileName.endsWith(".pdf")) {
							content = PDF.read(f.getAbsolutePath());
						}
						if (!"".equals(content)) {
							ESInfoBean esBean = new ESInfoBean();
							BeanUtils.copyProperties(bean, esBean);
							esBean.setAddDate(new Date());
							fileName = fileName.substring(0, fileName.lastIndexOf("."));
							esBean.setTitle(fileName);
							esBean.setContent(content);
							esBean.setDirectoryTree(sb.toString());
							saveES(zjcgIndex, esBean);
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					ZipUtil.delDir(targetDir);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	

	private String getContent(InputStream in, String extensionName) throws IOException {
		String content = "";
		if (extensionName.endsWith("doc")) {
			content = Word2003.read(in);
		} else if (extensionName.endsWith("docx")) {
			content = Word2007.read(in);
		} else if (extensionName.endsWith("pdf")) {
			content = PDF.read(in);
		}
		return content;
	}

	private void saveES(String index, ESInfoBean esBean) throws IOException {
		RestHighLevelClient client = ESClientFactory.getClient();

		IndexRequest request = new IndexRequest(index, "_doc");
		request.source(JSON.toJSONString(esBean), XContentType.JSON);
		// 同步请求
		IndexResponse response = client.index(request);
		log.info(response.getResult().toString());
	}

	public static void main(String[] args) {
//		File tempFile = new File("/Users/vko/Documents/my-code/DOC/zb/神华准能物资供应中心2018年第二批设备采购招标文件.doc");
//		InputStream in;
//		try {
//			in = new FileInputStream(tempFile);
//			String extensionName = "doc";
//			Map map = new ParseFile().parseText(in, extensionName);
//			log.info("========" + map);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String fileName = "心2018年第二批设备采购招标文件.doc";
		log.info("==="+fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()));
	}

	public void parseLocalFileSaveEs(String directoryPath) throws IOException {
		List<File> files = new ArrayList<>();
		FileListUtil.getFiles(directoryPath, 3, files);
		for (File f : files) {
			if (f.isFile()) {
				InputStream in = new FileInputStream(f);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();  
				byte[] buffer = new byte[1024];  
				int len;  
				while ((len = in.read(buffer)) > -1 ) {  
				    baos.write(buffer, 0, len);  
				}  
				baos.flush(); 
				
				String fileName = f.getName();
				System.out.println(fileName);
				String content = "";
				if (fileName.endsWith(".doc")) {
					content = Word2003.read(f.getAbsolutePath());
				} else if (fileName.endsWith(".docx")) {
					content = Word2007.read(f.getAbsolutePath());
				} else if (fileName.endsWith(".pdf")) {
					content = PDF.read(f.getAbsolutePath());
				}
				ESInfoBean esBean = new ESInfoBean();
				if (StringUtils.isNotBlank(content)) {
					String extensionName = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
					fileName = fileName.substring(0, fileName.lastIndexOf("."));
					esBean.setTitle(fileName);
					esBean.setContent(content);
					esBean.setAddDate(new Date());
					
					saveES(zbIndex, esBean);
				
					//招标范围
					String zbfw = parseFileUtil.parseTenderScope(new ByteArrayInputStream(baos.toByteArray()), extensionName);
					//资格要求
					List<String> zgyqList = parseFileUtil.parseTenderQualification(new ByteArrayInputStream(baos.toByteArray()), extensionName);
					//评标办法
					String pbbf = parseFileUtil.parseTenderMethod(new ByteArrayInputStream(baos.toByteArray()), extensionName);
					//技术要求
					String jsyq = parseFileUtil.parseTechnicalRequirement(new ByteArrayInputStream(baos.toByteArray()), extensionName);
					
					//log.info("zbfw====" + zbfw);
//					log.info("zgyqList=======" + zgyqList.toString());
//					log.info("pbbf=====" + pbbf);
//					log.info("jsyq=====" + jsyq);
					
//					
					esBean.setZbfw(zbfw);
					//==========存索引-资格要求及按条存数据库=======
					if (zgyqList != null && zgyqList.size() > 0) {
						
						
						esBean.setContent(zgyqList.toString());
						saveES(zgyqIndex, esBean);
						zgyqList.forEach(str ->{
							if (StringUtils.isNotBlank(str)) {
								QueryWrapper<GZgyq> query = new QueryWrapper<>();
								query.lambda()
								.eq(GZgyq::getContent, str);
								GZgyq zgyqItem = zgyqService.getOne(query);
								if (zgyqItem != null) {
									zgyqItem.setAmount(zgyqItem.getAmount() + 1);
									zgyqService.updateById(zgyqItem);
								} else {
									zgyqItem = new GZgyq();
									zgyqItem.setAmount(0);
									zgyqItem.setContent(str);
									zgyqService.save(zgyqItem);
								}
							}
						});
						
					}
					//=====存索引-评标办法===========
					if (StringUtils.isNotBlank(pbbf)) {
						esBean.setContent(pbbf);
						saveES(pbbfIndex, esBean);
					}
					//=======存索引-技术要求=========
					if (StringUtils.isNotBlank(jsyq)) {
						esBean.setContent(jsyq);
						saveES(jsyqIndex, esBean);
					}
				}
				
			}
		}
		
	}

	public void updateWeightById(String indexName, String id) throws IOException {
		RestHighLevelClient client = esClient.getClient();
		GetRequest getRequest = new GetRequest(indexName, "_doc", id);
		GetResponse getResponse = client.get(getRequest);
		if (getResponse.isExists()) {
			Object obj = getResponse.getSourceAsMap().get("weight");
			int weight = 1;
			if (obj != null) {
				weight = Integer.parseInt(obj.toString()) + 1;
			}

			Map<String, Object> jsonMap = new HashMap<>();
			jsonMap.put("weight", weight);

			UpdateRequest request = new UpdateRequest(indexName, "_doc", id).doc(jsonMap);
			request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
			client.update(request);
		}
	}

	public void updateWeightByIds(String indexname, List<String> ids) throws IOException {
		for (String id : ids) {
			updateWeightById(indexname, id);
		}
		
	}
}