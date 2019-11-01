package io.gomk.service;

import io.gomk.common.utils.PageResult;
import io.gomk.model.GWords;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 词库 服务类
 * </p>
 *
 * @author Robinxiao
 * @since 2019-11-01
 */
public interface IGWordsService extends IService<GWords> {

	void saveByList(List<String> phraseList);

	PageResult<Page<List<GWords>>> selectPageList(int page, int pageSize, boolean b);

}
