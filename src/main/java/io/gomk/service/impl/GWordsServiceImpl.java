package io.gomk.service.impl;

import io.gomk.model.GTagClassify;
import io.gomk.model.GWords;
import io.gomk.common.utils.PageResult;
import io.gomk.mapper.GWordsMapper;
import io.gomk.service.IGWordsService;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 词库 服务实现类
 * </p>
 *
 * @author Robinxiao
 * @since 2019-11-01
 */
@Service
@DS("oneself")
public class GWordsServiceImpl extends ServiceImpl<GWordsMapper, GWords> implements IGWordsService {

	@Override
	public void saveByList(List<String> phraseList) {
		phraseList.forEach( words -> {
			GWords entity = new GWords();
			entity.setAddDict(false);
			entity.setConfirm(false);
			super.baseMapper.insert(entity);
		});
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PageResult<Page<List<GWords>>> selectPageList(int pageNo, int pageSize, boolean confirm) {
		Page<GWords> page = new Page<>(pageNo, pageSize);
		QueryWrapper<GWords> query = new QueryWrapper<>();
		query.lambda()
    		.eq(GWords::getConfirm, confirm);
		IPage<GWords> pages = baseMapper.selectPage(page, query);
		
		
		return new PageResult(pageNo, pageSize, pages.getTotal(), pages.getRecords());
	}
	
}
