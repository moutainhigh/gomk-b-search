package io.gomk.controller;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.gomk.common.rs.response.ResponseData;
import io.gomk.es6.EsUtil;
import io.gomk.framework.hbase.HBaseClient;
import io.gomk.framework.hbase.HBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/test")
@Api(description = "hdfs hbase 上传下载测试类")
@Slf4j
public class FileUploadTestController {

    @Autowired
    HBaseClient hbaseClient;
    @Autowired
    EsUtil esUtil;
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


   

}
