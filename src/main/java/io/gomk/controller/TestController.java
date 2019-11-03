package io.gomk.controller;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import io.gomk.common.rs.response.ResponseData;
import io.gomk.enums.ScopeEnum;
import io.gomk.es6.EsUtil;
import io.gomk.framework.hbase.HBaseClient;
import io.gomk.framework.hbase.HBaseService;
import io.gomk.framework.redis.RedisUtil;
import io.gomk.framework.utils.HanyuPinyinUtil;
import io.gomk.task.DBInfoBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


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
    private RedisUtil redisUtil;
   // @Autowired
	//HBaseService hbaseService; 
   
    @ApiOperation("hbase操作")
    @PostMapping("/hbase")
    public ResponseData<?> hbase() throws Exception {
    	HBaseService hbaseService = hbaseClient.getService();
    	log.info("tableNames:" + hbaseService.getAllTableNames().toString());
    	String rowKey = "0014d5a310d048a48732e5b209df9e09";
    	String tableName = "FileStore";
    	Map<String, String> map = hbaseService.getRowData(tableName, rowKey);
    	
    	return ResponseData.success(map);
    }
    
    @ApiOperation("入库测试")
    @PostMapping("/1")
    public ResponseData<?> test1() throws Exception {
    	esUtil.parseAndSaveEs();
    	return ResponseData.success();
    }

    @ApiOperation("存redis 用户权限信息")
    @PostMapping("/redis/user/role")
    public ResponseData<?> saveUser() throws Exception {
    	List<ScopeEnum> scopes = EnumUtils.getEnumList(ScopeEnum.class);
    	List<Map<String, String>> userRoles = new ArrayList<>();
    	for (ScopeEnum se : scopes) {
    		String pinyin = HanyuPinyinUtil.toHanyuPinyin(se.getDesc());
    		Map<String, String> map = new HashMap<>();
    		map.put("authority", pinyin);
    		userRoles.add(map);
    	}
    	String json = JSON.toJSONString(userRoles);
    	boolean bl = redisUtil.set("shgcadmin", json);
    	log.info("add redis result:" + bl);
    	return ResponseData.success();
    }
    @ApiOperation("取token信息，base64解码，得到user-key,到redis中取数据")
    @GetMapping("/redis/user/role/{userKeyBase64}")
    public ResponseData<?> getUser(@PathVariable("userKeyBase64") String userKeyBase64) {
    	
    	String restore = "";
		try {
			restore = new String(decoder.decode(userKeyBase64), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return ResponseData.error("token is error");
		}
    	String salt = "shgc";
    	String userKey = restore.substring(salt.length());
    	Object obj = redisUtil.get(userKey);
    	if (obj == null) {
    		return ResponseData.error("not in redis");
    	}
    	List<Map<String, String>> userRoles = JSON.parseObject(obj.toString(), new TypeReference<List<Map<String, String>>>(){});
    	
    	log.info("userRoles:" + userRoles);
    	return ResponseData.success();
    }
    public static void main(String[] args) throws UnsupportedEncodingException {
    	final Base64.Decoder decoder = Base64.getDecoder();
    	final Base64.Encoder encoder = Base64.getEncoder();
    	String userKey = "shgcadmin";
    	String salt = "shgc";
    	String userKeyBase64 = encoder.encodeToString((salt + userKey).getBytes("UTF-8"));
    	log.info("userKey:" + userKey);
    	//c2hnY3NoZ2NhZG1pbg==
    	log.info("base64:" + userKeyBase64);
    	String restore = new String(decoder.decode(userKeyBase64), "UTF-8");
    	log.info("restore:" + restore);
    	log.info("userKey:" + restore.substring(salt.length()));
    	
	}
   

}
