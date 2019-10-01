package io.gomk.mapper;

import io.gomk.model.GTag;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Robinxiao
 * @since 2019-10-01
 */
public interface GTagMapper extends BaseMapper<GTag> {

	@Select("select count(id) from t_g_tag where tag_name = #{name}")
	int getCountByTagName(String name);

}
