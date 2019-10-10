package io.gomk.service.impl;

import io.gomk.model.GCompletion;
import io.gomk.common.utils.PageResult;
import io.gomk.es6.ESRestClient;
import io.gomk.mapper.GCompletionMapper;
import io.gomk.service.IGCompletionService;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Robinxiao
 * @since 2019-10-01
 */
@Service
public class GCompletionServiceImpl extends ServiceImpl<GCompletionMapper, GCompletion> implements IGCompletionService {

	@Autowired
	GCompletionMapper completionMapper;
	@Autowired
	protected ESRestClient esClient;
	
	@Value("${elasticsearch.index.completionName}")
	protected String completionIndex;
	
	@Override
	public List<String> getConmpletion(int size, String keyWord) {
		return completionMapper.getConmpletion(size, keyWord);
	}

	@Override
	public PageResult<Page<List<String>>> getBdw(int page, int pageSize, String keyWord) {
		int totalHits = completionMapper.countBdw(keyWord);
		List<String> result = completionMapper.getBdw((page-1)*pageSize, pageSize, keyWord);
		return new PageResult(page, pageSize, totalHits, result);
	}

}
