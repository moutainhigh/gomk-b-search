package io.gomk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.gomk.enums.TagClassifyScopeEnum;
import io.gomk.mapper.GTagClassifyMapper;
import io.gomk.mapper.GTagClassifyScopeMapper;
import io.gomk.model.GTagClassify;
import io.gomk.model.GTagClassifyScope;
import io.gomk.service.IGTagClassifyService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Robinxiao
 * @since 2019-10-01
 */
@Service
public class GTagClassifyServiceImpl extends ServiceImpl<GTagClassifyMapper, GTagClassify> implements IGTagClassifyService {

	@Autowired
	GTagClassifyScopeMapper tagClassifyScopeMapper;
	
	@Transactional
	@Override
	public void saveByScope(GTagClassify entity, List<Integer> scopes) throws Exception {
		super.getBaseMapper().insert(entity);
		for (Integer scope : scopes) {
			GTagClassifyScope scopeEntity = new GTagClassifyScope();
			scopeEntity.setClassifyId(entity.getId());
			scopeEntity.setScopes(TagClassifyScopeEnum.fromValue(scope));
			tagClassifyScopeMapper.insert(scopeEntity);
		}
	}

}
