package io.gomk.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.gomk.enums.TagClassifyScopeEnum;
import io.gomk.enums.UnknownEnumException;
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
 * @since 2019-10-23
 */
@Service
public class GTagClassifyServiceImpl extends ServiceImpl<GTagClassifyMapper, GTagClassify> implements IGTagClassifyService {

	@Autowired
	GTagClassifyScopeMapper tagClassifyScopeMapper;
	
	@Override
	@Transactional
	public void saveSecondClassify(Set<Integer> scopes, GTagClassify entity) throws UnknownEnumException {
		super.baseMapper.insert(entity);
		for (Integer scope : scopes) {
			TagClassifyScopeEnum.fromValue(scope);
			GTagClassifyScope classScope = new GTagClassifyScope();
			classScope.setClassifyId(entity.getId());
			classScope.setScopes(scope);
			tagClassifyScopeMapper.insert(classScope);
		}
		
	}

}
