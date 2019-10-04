package io.gomk.service;

import java.io.IOException;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.gomk.common.utils.PageResult;
import io.gomk.controller.response.SearchResultVO;


public interface ISearchService {

	PageResult<Page<List<SearchResultVO>>> commonSearch(int page, int pageSize, String keyWord, String tag, String indexName) throws Exception;

	
	PageResult<Page<List<SearchResultVO>>> searchCommonRecommend(int size, String tag, String indexName) throws IOException;

}