package io.gomk.service;

import java.io.IOException;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.gomk.common.utils.PageResult;
import io.gomk.controller.response.SearchResultVO;


public interface ISearchService {

	PageResult<Page<List<SearchResultVO>>> searchZB(int page, int pageSize, String keyWord, String tag) throws Exception;

	PageResult<Page<List<SearchResultVO>>> searchZGYQ(int page, int pageSize, String keyWord);

	PageResult<Page<List<SearchResultVO>>> searchZHUANJIA(int page, int pageSize, String keyWord);

	PageResult<Page<List<SearchResultVO>>> searchZBR(int page, int pageSize, String keyWord);

	PageResult<Page<List<SearchResultVO>>> searchTBWJ(int page, int pageSize, String keyWord);

	PageResult<Page<List<SearchResultVO>>> searchPBBF(int page, int pageSize, String keyWord);

	PageResult<Page<List<SearchResultVO>>> searchJSYQ(int page, int pageSize, String keyWord);

	PageResult<Page<List<SearchResultVO>>> searchZCFG(int page, int pageSize, String keyWord);

	PageResult<Page<List<SearchResultVO>>> searchZJCG(int page, int pageSize, String keyWord);

	PageResult<Page<List<SearchResultVO>>> searchPrice(int page, int pageSize, String keyWord);

	PageResult<Page<List<SearchResultVO>>> searchZBRecommend(int size, String tag) throws IOException;

}
