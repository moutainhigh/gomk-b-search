package io.gomk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.gomk.mapper.GTagClassifyMapper;
import io.gomk.mapper.GTagClassifyScopeMapper;
import io.gomk.model.GTagClassify;
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

}
