package io.gomk.mapper;

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
 * @author guanhua
 * @since 2019-11-04
 */
@Repository
public interface ShzjProductpriceNewMapper extends BaseMapper<ShzjProductpriceNew> {

    List<Map<String,Object>> selectPriceCharts(@Param("productName")String productName);
}
