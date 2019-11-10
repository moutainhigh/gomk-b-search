package io.gomk.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gomk.model.entity.DZbExpert;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 专家维度表 服务类
 * </p>
 *
 * @author nick
 * @since 2019-11-02
 */
public interface DZbExpertService extends IService<DZbExpert> {

    IPage<DZbExpert> selectExpert(Page<Map<String,String>> param, @Param("expertName")String expertName);

}
