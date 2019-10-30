package io.gomk.service.impl;

import io.gomk.model.GZgyq;
import io.gomk.controller.response.SearchResultVO;
import io.gomk.mapper.GZgyqMapper;
import io.gomk.service.IGZgyqService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 资格要求条目表 服务实现类
 * </p>
 *
 * @author Robinxiao
 * @since 2019-10-28
 */
@Service
public class GZgyqServiceImpl extends ServiceImpl<GZgyqMapper, GZgyq> implements IGZgyqService {
	@Autowired
	GZgyqMapper zgyqMapper;
	@Override
	public List<String> selectTopInfo(String keyword, int size) {
		return zgyqMapper.selectTopInfo(keyword, size);
	}

}
