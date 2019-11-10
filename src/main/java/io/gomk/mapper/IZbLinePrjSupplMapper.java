package io.gomk.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gomk.model.entity.IZbLinePrjSuppl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 行项目_供应商报价数据 Mapper 接口
 * </p>
 *
 * @author nick
 * @since 2019-11-02
 */
@Repository
public interface IZbLinePrjSupplMapper extends BaseMapper<IZbLinePrjSuppl> {

    IPage<Map<String,String>> queryQuote(Page<Map<String,String>> param, @Param("mateName")String mateName);

    List<Map<String,Object>> selectPriceCharts(@Param("mateName")String mateName);

}
