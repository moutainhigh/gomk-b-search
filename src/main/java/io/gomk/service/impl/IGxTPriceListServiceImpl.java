package io.gomk.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gomk.model.entity.IGxTPriceList;
import io.gomk.mapper.IGxTPriceListMapper;
import io.gomk.service.IGxTPriceListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nick
 * @since 2019-11-13
 */
@Service
public class IGxTPriceListServiceImpl extends ServiceImpl<IGxTPriceListMapper, IGxTPriceList> implements IGxTPriceListService {

    @Autowired
    private IGxTPriceListMapper iGxTPriceListMapper;
    @Override
    public IPage<Map<String, String>> queryQuote(Page<Map<String, String>> param, String mateName) {
        return iGxTPriceListMapper.queryQuote(param,mateName);
    }

    @Override
    public List<Map<String, Object>> selectPriceCharts(String mateName) {
        return iGxTPriceListMapper.selectPriceCharts(mateName);
    }
}
