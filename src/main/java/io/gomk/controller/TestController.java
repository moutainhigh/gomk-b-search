package io.gomk.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.gomk.common.rs.response.ResponseData;
import io.gomk.es6.EsUtil;
import io.gomk.framework.hbase.HBaseClient;
import io.gomk.framework.hbase.HBaseService;
import io.gomk.framework.hdfs.HdfsOperator;
import io.gomk.framework.utils.RandomUtil;
import io.gomk.framework.utils.parsefile.ParseFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;

@RestController
@RequestMapping("/test")
@Api(description = "hdfs hbase 上传下载测试类")
@Slf4j
public class TestController {

	final Base64.Decoder decoder = Base64.getDecoder();
	final Base64.Encoder encoder = Base64.getEncoder();

	@Autowired
	HBaseClient hbaseClient;
	@Autowired
	EsUtil esUtil;
	@Autowired
	ParseFile parseFile;
	private final static String STORE_HDFS = "HDFS";

	@Value("${spring.data.hdfs.server}")
	protected String hdfsServer;
	@Value("${spring.data.hdfs.zbfbDst}")
	protected String zbfbDst;
	// @Autowired
	// private MyRedisUtil myRedisUtil;

	// @Autowired
	// HBaseService hbaseService;

	@ApiOperation("hbase--rowke查询")
	@PostMapping("/hbase")
	public ResponseData<?> hbase(String rowKey) throws Exception {
		HBaseService hbaseService = hbaseClient.getService();
		log.info("tableNames:" + hbaseService.getAllTableNames().toString());
		//String rowKey = "00024cf4b7084a9ebda0e4b8610045d4";
		String tableName = "FileStore";
		hbaseService.GetData(tableName, rowKey);
		return ResponseData.success();
	}

	@ApiOperation("hdfs操作")
	@PostMapping("/hdfs")
	public ResponseData<?> hdfs() throws Exception {
		System.setProperty("HADOOP_USER_NAME", "hdfs");
		Configuration configuration = new Configuration();
		configuration.set("fs.default.name", "hdfs://10.212.169.158:8020");
		String pathName = "/ibs/AttachStorage/201712/J134/5693490d-ad46-4679-812f-42b0067a6ed3/a51358bc-8d21-466e-98b1-776a1ed0cf87.doc";
		HdfsOperator.getFromHDFS(pathName, "/temp/d0cf87.doc", configuration);
		log.info("===hdfs InputStream====");

		return ResponseData.success();
	}

	public void down(String text) throws IOException {
		BASE64Decoder be = new BASE64Decoder();
		byte[] c = be.decodeBuffer(text);
		FileOutputStream out = new FileOutputStream(new File("/temp/t2.pdf"));
		out.write(c);
		out.close();
	}

	@ApiOperation("入库测试")
	@PostMapping("/1")
	public ResponseData<?> test1() throws Exception {
		esUtil.parseAndSaveEs();
		return ResponseData.success();
	}

	@ApiOperation("压缩文件处理")
	@PostMapping("/2")
	public ResponseData<?> test2() throws Exception {
		esUtil.parseRarAndZip();
		return ResponseData.success();
	}

	@ApiOperation("分项报价抽取")
	@PostMapping("/3")
	public ResponseData<?> test3(String filePath) throws Exception {
		File file = new File(filePath);
		return ResponseData.success(parseFile.parsePdfTable(new FileInputStream(file)));
	}

	@ApiOperation("文件上传")
	@PostMapping("/4")
	public ResponseData<?> test4(String filePath) throws Exception {
		File file = new File(filePath);
		System.setProperty("HADOOP_USER_NAME", "hdfs");
		Configuration configuration = new Configuration();
		configuration.set("fs.defaultFS", hdfsServer);
		String dst = zbfbDst + file.getName();
		HdfsOperator.putToHDFS(file.getAbsolutePath(), dst, configuration);
		file.delete();
		return ResponseData.success();
	}
	@ApiOperation("hbase测试")
	@PostMapping("/5")
	public ResponseData<?> test5(String filePath) throws Exception {
		HBaseService hbaseService = hbaseClient.getService();
		log.info(hbaseService.getAllTableNames().toString());
		return ResponseData.success();
	}
	@ApiOperation("test 投标文件 ")
	@PostMapping("/6")
	public ResponseData<?> test6() throws Exception {
		esUtil.testTbFIle();
		return ResponseData.success();
	}
//    @ApiOperation("内置标签入库")
//    @PostMapping("/3")
//    public ResponseData<?> test3() throws Exception {
//    	esUtil.in
//    	return ResponseData.success();
//    }

//    @ApiOperation("存redis 用户权限信息")
//    @PostMapping("/redis/user/role")
//    public ResponseData<?> saveUser() throws Exception {
//    	List<ScopeEnum> scopes = EnumUtils.getEnumList(ScopeEnum.class);
//    	List<Map<String, String>> userRoles = new ArrayList<>();
//    	for (ScopeEnum se : scopes) {
//    		String pinyin = HanyuPinyinUtil.toHanyuPinyin(se.getDesc());
//    		Map<String, String> map = new HashMap<>();
//    		map.put("authority", pinyin);
//    		userRoles.add(map);
//    	}
//    	String json = JSON.toJSONString(userRoles);
//    	String result = RedisUtil.putString("shgcadmin", json);
//    	log.info("add redis result:" + result);
//    	return ResponseData.success();
//    }
//    @ApiOperation("取token信息，base64解码，得到user-key,到redis中取数据")
//    @GetMapping("/redis/user/role/{userKeyBase64}")
//    public ResponseData<?> getUser(@PathVariable("userKeyBase64") String userKeyBase64) {
//    	
//    	String restore = "";
//		try {
//			restore = new String(decoder.decode(userKeyBase64), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			return ResponseData.error("token is error");
//		}
//    	String salt = "shgc";
//    	String userKey = restore.substring(salt.length());
//    	
//    	//Object obj = myRedisUtil.get(userKey);
//    	//log.info("my redis:" + obj);
//    	String obj = RedisUtil.getString(userKey);
//    	log.info("master redis:" + obj);
//    	
//    	if (obj == null) {
//    		return ResponseData.error("not in redis");
//    	}
//    	List<Map<String, String>> userRoles = JSON.parseObject(obj.toString(), new TypeReference<List<Map<String, String>>>(){});
//    	
//    	log.info("userRoles:" + userRoles);
//    	return ResponseData.success();
//    }
	public static void main(String[] args) throws UnsupportedEncodingException {
		final Base64.Decoder decoder = Base64.getDecoder();
		final Base64.Encoder encoder = Base64.getEncoder();
		String userKey = "shgcadmin";
		String salt = "shgc";
		String userKeyBase64 = encoder.encodeToString((salt + userKey).getBytes("UTF-8"));
		log.info("userKey:" + userKey);
		// c2hnY3NoZ2NhZG1pbg==
		log.info("base64:" + userKeyBase64);
		String restore = new String(decoder.decode(userKeyBase64), "UTF-8");
		log.info("restore:" + restore);
		log.info("userKey:" + restore.substring(salt.length()));

	}

}
