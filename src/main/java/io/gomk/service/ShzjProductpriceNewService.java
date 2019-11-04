package io.gomk.service;

import io.gomk.model.entity.ShzjProductpriceNew;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author guanhua
 * @since 2019-11-04
 */
public interface ShzjProductpriceNewService extends IService<ShzjProductpriceNew> {

    List<Map<String,Object>> selectPriceCharts(String productName);
}
