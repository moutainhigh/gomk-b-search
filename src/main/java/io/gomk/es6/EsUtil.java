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

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
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
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.hankcs.hanlp.HanLP;
import com.itextpdf.text.DocumentException;

import io.gomk.enums.ScopeEnum;
import io.gomk.framework.hbase.HBaseClient;
import io.gomk.framework.hbase.HBaseService;
import io.gomk.framework.hdfs.HdfsOperator;
import io.gomk.framework.utils.FileListUtil;
import io.gomk.framework.utils.RandomUtil;
import io.gomk.framework.utils.parse.PDF;
import io.gomk.framework.utils.parse.Word2003;
import io.gomk.framework.utils.parse.Word2007;
import io.gomk.framework.utils.parse.ZipAndRarTools;
import io.gomk.framework.utils.parsefile.ParseFile;
import io.gomk.mapper.DZbPrjMapper;
import io.gomk.mapper.MasterDBMapper;
import io.gomk.mapper.OneselfMapper;
import io.gomk.model.GTbIdcardExtract;
import io.gomk.model.GTbQuoteExtract;
import io.gomk.model.GZgyq;
import io.gomk.model.entity.DZbPrj;
import io.gomk.service.IGTbQuoteExtractService;
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
	HBaseClient hbaseClient;
	@Autowired
	OneselfMapper db2esMapper;
	@Autowired
	MasterDBMapper masterDBMapper;
	@Autowired
	IGZgyqService zgyqService;
	@Autowired
	IGWordsService wordsService;
	@Autowired
	IGTbQuoteExtractService tbQuoteExtractService;
	@Autowired
	DZbPrjMapper prjMapper;

	@Autowired
	private IdcardOcrUtil idcardOcr;

	@Value("${elasticsearch.index.zbName}")
	protected String zbIndex;
	@Value("${elasticsearch.index.tbName}")
	protected String tbIndex;
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
	private final static String STORE_HDFS = "HDFS";
	private final static String ZJCG_CODE = "成果文件"; // 造价

	@Value("${spring.data.hdfs.server}")
	protected String hdfsServer;
	@Value("${spring.data.hbase.tableName}")
	protected String hbaseTableName;
	@Value("${spring.data.hbase.columnName}")
	protected String hbasecolumnName;

	/*
	 * 条件更新
	 * 
	 * @return async 是否异步 addOrDelete 添加or删除 false删除
	 */
	public void updateTagByIds(String indexName, Set<String> tagSet, List<String> ids, boolean async,
			boolean addOrDelete) throws IOException {
		RestHighLevelClient client = esClient.getClient();
		Set<String> idSet = new HashSet<String>(ids);
		for (String id : idSet) {
			Map<String, Object> jsonMap = new HashMap<>();
			GetRequest getRequest = new GetRequest(indexName, "_doc", id);
			GetResponse getResponse = client.get(getRequest);
			Object obj = getResponse.getSourceAsMap().get("tag");

			Set<String> result = null;
			if (addOrDelete) {
				if (obj == null) {
					result = tagSet;
				} else {
					result = new HashSet<String>((List<String>) obj);
					result.addAll(tagSet);
				}
			} else {
				if (obj != null) {
					result = new HashSet<String>((List<String>) obj);
					result.removeAll(tagSet);
				} else {
					break;
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

	public List<String> getIDsByKeyword(String indexName, QueryBuilder query) throws IOException {
		List<String> result = new ArrayList<>();
		RestHighLevelClient client = esClient.getClient();

		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(query);
		sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
		sourceBuilder.fetchSource("_id", null);

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
		}
		return result;
	}

	public String getIndexname(int scope) throws Exception {
		ScopeEnum scopes = ScopeEnum.fromValue(scope);
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
		case TBWJ:
			indexName = tbIndex;
			break;
		default:
			break;
		}
		return indexName;
	}

	/**
	 * 测试投标文件
	 */
	public void testTbFIle() {
		List<DBInfoBean> list = masterDBMapper.getToubiaoDBInfo();
		list.forEach(bean -> {
			InputStream in = getInputStream(bean);
			if (in != null) {
//				try {
//					saveTB(bean, in);
//				} catch (IOException e1) {
//					log.error("error:"+e1.getMessage());
//				}
				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int len;
					while ((len = in.read(buffer)) > -1) {
						baos.write(buffer, 0, len);
					}
					baos.flush();

					if (bean.getExt().contains("pdf")) {

						GTbQuoteExtract qe = new GTbQuoteExtract();
						qe.setPrjCode(bean.getPrjCode());
						qe.setPrjName(bean.getPrjName());
						qe.setUuid(bean.getUuid());
						qe.setTitle(bean.getTitle());
						// 存分项报价
						tbQuoteExtractService.insertPrice(new ByteArrayInputStream(baos.toByteArray()), qe);

						GTbIdcardExtract entity = new GTbIdcardExtract();
						entity.setPrjCode(bean.getPrjCode());
						entity.setPrjName(bean.getPrjName());
						entity.setUuid(bean.getUuid());
						entity.setTitle(bean.getTitle());
						// 存储身份证信息
						idcardOcr.insertIdcardInfo(new ByteArrayInputStream(baos.toByteArray()),
								new ByteArrayInputStream(baos.toByteArray()), entity);
					}
				} catch (IOException e) {
					log.error("io error" + e.getMessage());
				}
			}
		});
	}

	/**
	 * 处理造价压缩文件
	 */
	public void parseRarAndZip() {
		List<DBInfoBean> list = masterDBMapper.getRarAndZipDBInfo();
		log.info("===size====" + list.size());
		list.forEach(bean -> {
			InputStream in = getInputStream(bean);
			try {
				if (in != null) {
					disposeZJRARandZIP(bean, in);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	/**
	 * 下截文件--抽取内容--存储es索引
	 * 
	 * @param i 1招标 2投标 3造价
	 */
	public void parseAndSaveEs(int type) {

		String timeSign = "";
		while (true) {
//			//查询已索引时间戳
//			timeSign = db2esMapper.selectStoredDateTImeSTOREDATETIME();
//			if (StringUtils.isBlank(timeSign)) {
//				timeSign = "1970-01-01 19:19:14.0";
//			} 
			// List<DBInfoBean> list = masterDBMapper.getDBInfo(timeSign);

			// List<String> ids = db2esMapper.selectIds();
			// List<DBInfoBean> list = masterDBMapper.getTestALLDBInfo(ids);

			List<DBInfoBean> list = new ArrayList<>();
			String numberSign = db2esMapper.selectSign(type);
			switch (type) {
			case 1:
				list = masterDBMapper.getDBInfoByNumber1(Integer.parseInt(numberSign));
				break;
			case 2:
				list = masterDBMapper.getDBInfoByNumber2(Integer.parseInt(numberSign));
				break;
			case 3:
				list = masterDBMapper.getDBInfoByNumber3(Integer.parseInt(numberSign));
				break;

			default:
				break;
			}
			

			// 暂时切到自己库
			log.info("===size====" + list.size());
			if (list.size() == 0)
				break;
			
			// 1. 下载文件 分页查询未处理的纪录
			for (DBInfoBean bean : list) {
				// db2esMapper.insertFileSign(bean.getUuid());
				Integer k = Integer.parseInt(numberSign) + 1;
				db2esMapper.updateSign(numberSign, k);
				
				// timeSign = bean.getSTOREDATETIME();
				log.info(bean.getUuid() + "==========wjtm=======" + bean.getWjtm());
				// log.info( "==========detail======" + bean.toString());
				switch (bean.getFileType()) {
				case "ztb":
					if (bean.getWjtm().startsWith("招标文件及审批表")) {
						log.info("====招标文件 ====");
						InputStream in = getInputStream(bean);
						if (in != null) {
							try {
								saveZB(bean, in);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								log.error("error" + e.getMessage());
								break;
							}
						}
					} else if ("投标文件".equals(bean.getWjtm())) {
						log.info("====投标文件 ====");
						InputStream in = getInputStream(bean);

						if (in != null) {
							try {
								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								byte[] buffer = new byte[1024];
								int len;
								while ((len = in.read(buffer)) > -1) {
									baos.write(buffer, 0, len);
								}
								baos.flush();

								saveTB(bean, new ByteArrayInputStream(baos.toByteArray()));
								if (bean.getExt().contains("pdf")) {

									GTbQuoteExtract qe = new GTbQuoteExtract();
									qe.setPrjCode(bean.getPrjCode());
									qe.setPrjName(bean.getPrjName());
									qe.setUuid(bean.getUuid());
									qe.setTitle(bean.getTitle());
									// 存分项报价
									tbQuoteExtractService.insertPrice(new ByteArrayInputStream(baos.toByteArray()), qe);

									GTbIdcardExtract entity = new GTbIdcardExtract();
									entity.setPrjCode(bean.getPrjCode());
									entity.setPrjName(bean.getPrjName());
									entity.setUuid(bean.getUuid());
									entity.setTitle(bean.getTitle());
									// 存储身份证信息
									idcardOcr.insertIdcardInfo(new ByteArrayInputStream(baos.toByteArray()),
											new ByteArrayInputStream(baos.toByteArray()), entity);
								}
							} catch (IOException e) {
								log.error("io error" + e.getMessage());
								break;
							}
						}
					}
					break;
				case "gczj":
					log.info("====zaojia文件 ====");
					try {
						if (bean.getBz() != null && bean.getBz().equals(ZJCG_CODE)) {
							if (!bean.getExt().equals("rar") && !bean.getExt().equals("zip")) {
								InputStream in = getInputStream(bean);
								if (in != null) {
									saveZaojia(bean, in);
								}
							} else if (bean.getExt().equals("zip") || bean.getExt().equals("rar")) {
								// 处理压缩文件
								InputStream in = getInputStream(bean);
								disposeZJRARandZIP(bean, in);
							}
						} else {
							break;
						}

					} catch (IOException e) {
						log.error("error" + e.getMessage());
						break;
					}
					break;
				default:
					break;
				}
			}
		}

	}

	private InputStream getInputStream(DBInfoBean bean) {
		String storeType = bean.getStoreType();
		String storeUrl = bean.getStoreUrl();
		String ext = bean.getExt();
		log.info("===get inputStream====" + storeType);
		return getInputStreams(storeType, storeUrl, ext);
	}

	public InputStream getInputStreams(String storeType, String storeUrl, String ext) {
		if (storeType.equals(STORE_HBASE)) {
			log.info("===save type:hbase====");
			HBaseService hbaseService = hbaseClient.getService();
			InputStream in = hbaseService.getInputStream(hbaseTableName, storeUrl);
			// log.info("=====hbase inputstream:" + in.toString());
			return in;

		} else if (storeType.equals(STORE_HDFS)) {
			log.info("===save type:hdfs====");
			System.setProperty("HADOOP_USER_NAME", "hdfs");
			Configuration configuration = new Configuration();
			configuration.set("fs.defaultFS", hdfsServer);
			String target = "/temp/" + RandomUtil.getGlobalUniqueId() + "." + ext;
			// HdfsOperator.getFromHDFS(storeUrl, target, configuration);
			return HdfsOperator.getInputStreamFromHDFS(storeUrl, target, configuration);

		}
		return null;
	}

	private void saveZB(DBInfoBean bean, InputStream in) throws IOException {
		String extensionName = bean.getExt();

		ESInfoBean esBean = new ESInfoBean();
		BeanUtils.copyProperties(bean, esBean);
		esBean.setAddDate(new Date());

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) > -1) {
			baos.write(buffer, 0, len);
		}
		baos.flush();
		// 全文
		String content = getContent(new ByteArrayInputStream(baos.toByteArray()), extensionName);

		// 招标范围
		String zbfw = parseFileUtil.parseTenderScope(new ByteArrayInputStream(baos.toByteArray()), extensionName);
		// 资格要求
		List<String> zgyqList = parseFileUtil.parseTenderQualification(new ByteArrayInputStream(baos.toByteArray()),
				extensionName);
		// 评标办法
		String pbbf = parseFileUtil.parseTenderMethod(new ByteArrayInputStream(baos.toByteArray()), extensionName);
		// 技术要求
		String jsyq = parseFileUtil.parseTechnicalRequirement(new ByteArrayInputStream(baos.toByteArray()),
				extensionName);

//					log.info("zbfw====" + zbfw);
//					log.info("zgyqList=======" + zgyqList.toString());
//					log.info("pbbf=====" + pbbf);
		log.info("jsyq=====" + jsyq);

		esBean.setZbfw(zbfw);
		// ========存招标文件库=======
		if (StringUtils.isNotBlank(content)) {
			esBean.setContent(content);
			List<String> phraseList = HanLP.extractPhrase(content, 3);
			log.info("========words size=======" + phraseList.size());
			// 保存新词
			wordsService.saveByList(phraseList);
			saveES(zbIndex, esBean);
		}

		// ==========存索引-资格要求及按条存数据库=======
		if (zgyqList != null && zgyqList.size() > 0) {
			esBean.setContent(zgyqList.toString());
			saveES(zgyqIndex, esBean);
			zgyqList.forEach(str -> {
				if (StringUtils.isNotBlank(str)) {
					QueryWrapper<GZgyq> query = new QueryWrapper<>();
					query.lambda().eq(GZgyq::getContent, str);
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
		// =====存索引-评标办法===========
		if (StringUtils.isNotBlank(pbbf)) {
			esBean.setContent(pbbf);
			saveES(pbbfIndex, esBean);
		}
		// =======存索引-技术要求=========
		if (StringUtils.isNotBlank(jsyq)) {
			esBean.setContent(jsyq);
			saveES(jsyqIndex, esBean);
		}
	}

	/**
	 * 处理投标文件
	 * 
	 * @param bean
	 * @param in
	 * @throws IOException
	 */
	private void saveTB(DBInfoBean bean, InputStream in) throws IOException {
		// 全文
		String content = getContent(in, bean.getExt());
		if (StringUtils.isNotBlank(content)) {
			ESInfoBean esBean = new ESInfoBean();
			BeanUtils.copyProperties(bean, esBean);
			esBean.setAddDate(new Date());
			esBean.setContent(content);
			saveES(tbIndex, esBean);
		}
	}

	/**
	 * 处理非压缩造价文件
	 * 
	 * @param bean
	 * @param in
	 * @throws IOException
	 */
	private void saveZaojia(DBInfoBean bean, InputStream in) throws IOException {
		// 全文
		String content = getContent(in, bean.getExt());
		if (StringUtils.isNotBlank(content)) {
			ESInfoBean esBean = new ESInfoBean();
			BeanUtils.copyProperties(bean, esBean);
			esBean.setAddDate(new Date());
			esBean.setContent(content);
			saveES(zjcgIndex, esBean);
		}
	}

	/**
	 * 处理造价文件
	 * 
	 * @param bean
	 * @throws IOException
	 */
	private void disposeZJRARandZIP(DBInfoBean bean, InputStream initialStream) throws IOException {
		String ext = bean.getExt();
		String zipPath = "/temp/targetFile_zip.zip";
		String rarPath = "/temp/targetFile_rar.rar";
		String targetDir = "/temp/zip_rar";
		if (initialStream != null) {
			if ("zip".equals(ext)) {
				File targetFile = new File(zipPath);
				FileUtils.copyInputStreamToFile(initialStream, targetFile);
				ZipAndRarTools.unZipFiles(zipPath, targetDir);
			} else if ("rar".equals(ext)) {
				// File targetFile = new File(rarPath);
				// FileUtils.copyInputStreamToFile(initialStream, targetFile);
				try {
					ZipAndRarTools.unRar(initialStream, targetDir);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
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
					ZipAndRarTools.delDir(targetDir);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private String getContent(InputStream in, String extensionName) throws InvalidPasswordException, IOException {
		String content = "";
		if (extensionName.endsWith("doc")) {
			content = Word2003.read(in);
		} else if (extensionName.endsWith("docx")) {
			content = Word2007.read(in);
		} else if (extensionName.endsWith("pdf")) {
			content = PDF.read(in);
		}
		log.info("content=length==========" + content.length());
		return content;
	}

	public void saveES(String index, ESInfoBean esBean) throws IOException {
		RestHighLevelClient client = ESClientFactory.getClient();
		esBean.setWeight(0);
		if (StringUtils.isNotBlank(esBean.getPrjCode())) {
			Set<String> tagSet = getTagByPrjCode(esBean.getPrjCode());
			esBean.setTags(tagSet);
			esBean.setWeight(1);
		}
		IndexRequest request = new IndexRequest(index, "_doc");
		request.source(JSON.toJSONString(esBean), XContentType.JSON);

		log.info("=============insert ES...=======");
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
		log.info("===" + fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()));
	}

	public void updateWeightById(String indexName, String id, int num) throws IOException {
		RestHighLevelClient client = esClient.getClient();
		GetRequest getRequest = new GetRequest(indexName, "_doc", id);
		GetResponse getResponse = client.get(getRequest);
		if (getResponse.isExists()) {
			Object obj = getResponse.getSourceAsMap().get("weight");
			int weight = 1;
			if (obj != null) {
				weight = Integer.parseInt(obj.toString()) + num;
			}

			Map<String, Object> jsonMap = new HashMap<>();
			jsonMap.put("weight", weight);

			UpdateRequest request = new UpdateRequest(indexName, "_doc", id).doc(jsonMap);
			request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
			client.update(request);
		}
	}

	/**
	 * 更新权重
	 * 
	 * @param indexname
	 * @param ids
	 * @param num
	 * @throws IOException
	 */
	public void updateWeightByIds(String indexname, List<String> ids, int num) throws IOException {
		for (String id : ids) {
			updateWeightById(indexname, id, num);
		}
	}

	/**
	 * 给索引打内置标签
	 */

	private Set<String> getTagByPrjCode(String prjCode) {
		DZbPrj prj = masterDBMapper.getTagInfo(prjCode);
		if (prj != null) {
			Set<String> tagSet = new HashSet<>();
			tagSet.add(prj.getPrjType()); // 项目类型
			tagSet.add(prj.getIndustryName()); // 专业版块
			tagSet.add(prj.getPrjNature()); // 项目阶段
			tagSet.add(prj.getIfCentPurchas().equals("1") ? "是" : "否"); // 是否集采
			tagSet.add(prj.getCapitalSource()); // 资金来源

			return tagSet;
		}
		return null;
	}

	public void parseLocalZBFileSaveEs(String directoryPath) throws IOException {
		List<File> files = new ArrayList<>();
		FileListUtil.getFiles(directoryPath, 3, files);
		for (File f : files) {
			if (f.isFile()) {
				InputStream in = new FileInputStream(f);
				DBInfoBean bean = new DBInfoBean();
				bean.setTitle(f.getName());
				bean.setPrjCode(IdWorker.get32UUID());
				bean.setPrjName("项目名称-" + bean.getPrjCode());
				bean.setUuid("uuid");
				saveZB(bean, in);
			}
		}
	}

	public void parseLocalTBFileSaveEs(String directoryPath) throws IOException, DocumentException {
		List<File> files = new ArrayList<>();
		FileListUtil.getFiles(directoryPath, 3, files);
		for (File f : files) {
			if (f.isFile() && f.getName().endsWith("pdf")) {
				InputStream in = new FileInputStream(f);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len;
				while ((len = in.read(buffer)) > -1) {
					baos.write(buffer, 0, len);
				}
				baos.flush();
				DBInfoBean bean = new DBInfoBean();
				bean.setTitle(f.getName());
				bean.setPrjCode(IdWorker.get32UUID());
				bean.setPrjName("项目名称-" + bean.getPrjCode());
				bean.setUuid("uuid");

				GTbIdcardExtract entity = new GTbIdcardExtract();
				entity.setPrjCode(bean.getPrjCode());
				entity.setPrjName(bean.getPrjName());
				entity.setUuid(bean.getUuid());
				entity.setTitle(bean.getTitle());
				// 存储身份证信息
				idcardOcr.insertIdcardInfo(new ByteArrayInputStream(baos.toByteArray()),
						new ByteArrayInputStream(baos.toByteArray()), entity);
				// saveTB(bean, in);
			}
		}
	}

	public void parseZhaobiaoEs() {
		parseAndSaveEs(1);
	}

	public void parseTaobiaoEs() {
		parseAndSaveEs(2);
	}

	public void parseZaojiaEs() {
		parseAndSaveEs(3);
	}

}