package io.gomk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.gomk.framework.controller.SuperController;
import io.swagger.annotations.Api;

/**
 * <p>
 * 词库管理
 * </p>
 *
 * @author Robinxiao
 * @since 2019-09-27
 */
@RestController
@RequestMapping("/dict")
@Api(description = "词库操作")
public class DictController extends SuperController {
	
	private Logger logger = LoggerFactory.getLogger(DictController.class);
//	
//	@Autowired
//	IDictService dictService;
//	
//	@GetMapping("/add")
//	public ResponseData<?> add(DictRequest request) throws Exception {
//		dictService.add(request);
//		return ResponseData.success();
//	}
//	@GetMapping("/delete")
//	public ResponseData<?> delete(Integer id) throws Exception {
//		dictService.delete(id);
//		return ResponseData.success();
//	}
//	
//	@GetMapping("/update")
//	public ResponseData<?> update(DictRequest request) throws Exception {
//		dictService.update(request);
//		return ResponseData.success();
//	}
//	
//	@GetMapping("/search")
//	public ResponseData<?> search(DictRequest request) throws Exception {
//		dictService.search(request);
//		return ResponseData.success();
//	}

}
