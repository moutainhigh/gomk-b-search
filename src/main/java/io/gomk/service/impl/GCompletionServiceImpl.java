package io.gomk.service.impl;

import io.gomk.model.GCompletion;
import io.gomk.mapper.GCompletionMapper;
import io.gomk.service.IGCompletionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Robinxiao
 * @since 2019-10-01
 */
@Service
public class GCompletionServiceImpl extends ServiceImpl<GCompletionMapper, GCompletion> implements IGCompletionService {

	@Autowired
	GCompletionMapper completionMapper;
	
	@Override
	public List<String> getConmpletion(int size, String keyWord) {
		return completionMapper.getConmpletion(size, keyWord);
	}

}
