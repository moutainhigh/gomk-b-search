package io.gomk.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gomk.model.entity.ShzjProductpriceNew;
import io.gomk.mapper.ShzjProductpriceNewMapper;
import io.gomk.service.ShzjProductpriceNewService;
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
 * @since 2019-11-04
 */
@Service
public class ShzjProductpriceNewServiceImpl extends ServiceImpl<ShzjProductpriceNewMapper, ShzjProductpriceNew> implements ShzjProductpriceNewService {

    @Autowired
    private ShzjProductpriceNewMapper productpriceNewMapper;

    @Override
    public List<Map<String, Object>> selectPriceCharts(String productName) {
        return productpriceNewMapper.selectPriceCharts(productName);
    }

    @Override
    public IPage<ShzjProductpriceNew> selectProductPrice(Page<Map<String, String>> param, String productName) {
        return productpriceNewMapper.selectProductPrice(param,productName);
    }
}
