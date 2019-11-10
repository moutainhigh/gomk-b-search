package io.gomk.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gomk.model.entity.DZbExpert;
import io.gomk.model.entity.ShzjProductpriceNew;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author nick
 * @since 2019-11-04
 */
@Repository
public interface ShzjProductpriceNewMapper extends BaseMapper<ShzjProductpriceNew> {

    IPage<ShzjProductpriceNew> selectProductPrice(Page<Map<String,String>> param, @Param("productName")String productName);

    List<Map<String,Object>> selectPriceCharts(@Param("productName")String productName);

}
