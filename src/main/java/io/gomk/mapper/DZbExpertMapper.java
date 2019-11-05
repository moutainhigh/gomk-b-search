package io.gomk.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gomk.model.entity.DZbExpert;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * <p>
 * 专家维度表 Mapper 接口
 * </p>
 *
 * @author guanhua
 * @since 2019-11-02
 */
@Repository
public interface DZbExpertMapper extends BaseMapper<DZbExpert> {


    IPage<DZbExpert> selectExpert(Page<Map<String,String>> param, @Param("expertName")String expertName);


}
