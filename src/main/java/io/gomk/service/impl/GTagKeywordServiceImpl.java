package io.gomk.service.impl;

import io.gomk.model.GTagKeyword;
import io.gomk.mapper.GTagKeywordMapper;
import io.gomk.service.IGTagKeywordService;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
public class GTagKeywordServiceImpl extends ServiceImpl<GTagKeywordMapper, GTagKeyword> implements IGTagKeywordService {

}
