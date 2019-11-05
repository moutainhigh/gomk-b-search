package io.gomk.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gomk.model.entity.ShzjProductpriceNew;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

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

    IPage<ShzjProductpriceNew> selectProductPrice(Page<Map<String,String>> param, @Param("productName")String productName);

}
