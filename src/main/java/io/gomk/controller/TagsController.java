package io.gomk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.gomk.common.rs.response.ResponseData;
import io.gomk.controller.request.DictRequest;
import io.gomk.controller.request.TagRequest;
import io.gomk.framework.controller.SuperController;
import io.gomk.service.ITagService;
import io.swagger.annotations.Api;

/**
 * <p>
 * 标签管理
 * </p>
 *
 * @author Robinxiao
 * @since 2019-09-27
 */
@RestController
@RequestMapping("/tag")
@Api(description = "标签操作")
public class TagsController extends SuperController {
	
	private Logger logger = LoggerFactory.getLogger(TagsController.class);
	
	@Autowired
	ITagService tagService;
	
	@GetMapping("/add")
	public ResponseData<?> add(TagRequest request) throws Exception {
		tagService.add(request);
		return ResponseData.success();
	}
	@GetMapping("/delete")
	public ResponseData<?> delete(Integer id) throws Exception {
		tagService.delete(id);
		return ResponseData.success();
	}
	
	@GetMapping("/update")
	public ResponseData<?> update(TagRequest request) throws Exception {
		tagService.update(request);
		return ResponseData.success();
	}
	
	@GetMapping("/search")
	public ResponseData<?> search(TagRequest request) throws Exception {
		tagService.search(request);
		return ResponseData.success();
	}

}
