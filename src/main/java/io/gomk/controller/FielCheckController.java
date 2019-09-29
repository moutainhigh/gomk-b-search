package io.gomk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.gomk.common.rs.response.ResponseData;
import io.gomk.framework.controller.SuperController;
import io.gomk.service.IFileCheckService;
import io.swagger.annotations.Api;

/**
 * @author Robinxiao
 * @since 2019-09-27
 */
@RestController
@RequestMapping("/filecheck")
@Api(description = "文件自检")
public class FielCheckController extends SuperController {
	
	private Logger logger = LoggerFactory.getLogger(FielCheckController.class);
	
	@Autowired
	IFileCheckService fileCheckService;
	
	@GetMapping("/comparison")
	public ResponseData<?> comparison(String content) throws Exception {
		fileCheckService.fileCheck(content);
		return ResponseData.success();
	}
	
}
