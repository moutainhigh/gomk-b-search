package io.gomk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.gomk.common.rs.response.ResponseData;
import io.gomk.common.utils.PageResult;
import io.gomk.framework.controller.SuperController;
import io.gomk.model.GWords;
import io.gomk.service.IGWordsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 词库管理
 * </p>
 *
 * @author Robinxiao
 * @since 2019-09-27
 */
@RestController
@RequestMapping("/words")
@Api(description = "词库操作")
@Slf4j
public class WordsController extends SuperController {
	
	@Autowired
	IGWordsService wordsService;
	
	@ApiOperation("待确认列表")
	@GetMapping("/unconfirm")
	@ApiImplicitParams({
		@ApiImplicitParam(name="page", value="第几页", required=true, paramType="query", dataType="int", defaultValue="1"),
		@ApiImplicitParam(name="pageSize", value="每页条数", required=true, paramType="query", dataType="int", defaultValue="10")
	})
	public ResponseData<PageResult<Page<List<GWords>>>> unconfirm(int page, int pageSize) throws Exception {
		PageResult<Page<List<GWords>>> result = wordsService.selectPageList(page, pageSize, false);
		return ResponseData.success(result);
	}

	@ApiOperation("已确认列表")
	@GetMapping("/confirmed")
	@ApiImplicitParams({
		@ApiImplicitParam(name="page", value="第几页", required=true, paramType="query", dataType="int", defaultValue="1"),
		@ApiImplicitParam(name="pageSize", value="每页条数", required=true, paramType="query", dataType="int", defaultValue="10")
	})
	public ResponseData<PageResult<Page<List<GWords>>>> confirmed(int page, int pageSize) throws Exception {
		PageResult<Page<List<GWords>>> result = wordsService.selectPageList(page, pageSize, true);
		return ResponseData.success(result);
	}
	
	@ApiOperation("确认")
	@PostMapping("/confirm")
	public ResponseData<?> confirm(@RequestBody List<Integer> ids) throws Exception {
		ids.forEach(id -> {
			GWords word = new GWords();
			word.setId(id);
			word.setConfirm(true);
			wordsService.updateById(word);
		});
		return ResponseData.success();
	}
	
}
