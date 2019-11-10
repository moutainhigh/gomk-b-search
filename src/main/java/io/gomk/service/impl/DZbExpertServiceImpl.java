package io.gomk.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gomk.model.entity.DZbExpert;
import io.gomk.mapper.DZbExpertMapper;
import io.gomk.service.DZbExpertService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 专家维度表 服务实现类
 * </p>
 *
 * @author nick
 * @since 2019-11-02
 */
@Service
public class DZbExpertServiceImpl extends ServiceImpl<DZbExpertMapper, DZbExpert> implements DZbExpertService {

    @Autowired
    private DZbExpertMapper expertMapper;


    @Override
    public IPage<DZbExpert> selectExpert(Page<Map<String, String>> param, String expertName) {
        return expertMapper.selectExpert(param,expertName);
    }
}
