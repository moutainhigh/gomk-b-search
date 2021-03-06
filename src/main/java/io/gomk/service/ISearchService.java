package io.gomk.service;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.get.GetResponse;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.gomk.common.utils.PageResult;
import io.gomk.controller.response.NumberVO;
import io.gomk.controller.response.SearchResultVO;
import io.gomk.controller.response.ZgyqDetailVO;
import io.gomk.framework.utils.tree.TreeDto;


public interface ISearchService {

	PageResult<Page<List<SearchResultVO>>> commonSearch(int page, int pageSize, String keyWord, String tag, String indexName) throws Exception;

	
	PageResult<Page<List<SearchResultVO>>> searchCommonRecommend(int size, String tag, String indexName) throws IOException;


	List<String> getConmpletion(int size, String keyWord) throws IOException;


	GetResponse getEsDoc(String indexName, String id) throws IOException;


	PageResult<Page<List<NumberVO>>> getBdw(int page, int pageSize, String keyWord) throws IOException;


	List<TreeDto> selectTagByKeyword(String keyWord, String indexName) throws IOException;


	List<String> searchWeightRecommend(int size, String keyword, String zgyqIndex);





}
