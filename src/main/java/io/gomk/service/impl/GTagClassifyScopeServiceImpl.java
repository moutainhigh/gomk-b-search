package io.gomk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.gomk.mapper.GTagClassifyScopeMapper;
import io.gomk.model.GTagClassifyScope;
import io.gomk.service.IGTagClassifyScopeService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Robinxiao
 * @since 2019-10-23
 */
@Service
@DS("oneself")
public class GTagClassifyScopeServiceImpl extends ServiceImpl<GTagClassifyScopeMapper, GTagClassifyScope> implements IGTagClassifyScopeService {

	@Autowired
	GTagClassifyScopeMapper scopeMaper;
	
	@Override
	public List<Integer> selectScopeByClassifyId(Integer id) {
		return scopeMaper.selectScopeByClassifyId(id);
	}

}
