package io.gomk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.gomk.common.rs.response.ResponseData;
import io.gomk.controller.request.AddDocTagRequest;
import io.gomk.framework.controller.SuperController;
import io.gomk.service.IGTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
	
	//private Logger logger = LoggerFactory.getLogger(TagsController.class);
	
	@Autowired
	IGTagService tagService;
	
	@ApiOperation("批量添加标签")
	@PostMapping("/doc")
	public ResponseData<?> add(AddDocTagRequest request) throws Exception {
		String tagName = request.getTag();
		// ? 检查数据库中是否存在 tagName
		int count = tagService.getCountByTagName(tagName);
		if (count == 0) {
			return ResponseData.error("tag is not exist.");
		}
		tagService.addDocTag(tagName, request.getIds());
		return ResponseData.success();
	}
	
	@ApiOperation("标签树")
	@PostMapping("/tree")
	public ResponseData<?> tree() throws Exception {
		
	//	tagService.addDocTag(tagName, request.getIds());
		return ResponseData.success();
	}
//	
//	@GetMapping("/add")
//	public ResponseData<?> add(TagRequest request) throws Exception {
//		tagService.add(request);
//		return ResponseData.success();
//	}
//	@GetMapping("/delete")
//	public ResponseData<?> delete(Integer id) throws Exception {
//		tagService.delete(id);
//		return ResponseData.success();
//	}
//	
//	@GetMapping("/update")
//	public ResponseData<?> update(TagRequest request) throws Exception {
//		tagService.update(request);
//		return ResponseData.success();
//	}
//	
//	@GetMapping("/search")
//	public ResponseData<?> search(TagRequest request) throws Exception {
//		tagService.search(request);
//		return ResponseData.success();
//	}

}
