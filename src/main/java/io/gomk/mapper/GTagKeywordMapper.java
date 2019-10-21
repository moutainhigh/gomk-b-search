package io.gomk.mapper;

import io.gomk.model.GTagKeyword;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Robinxiao
 * @since 2019-10-19
 */
public interface GTagKeywordMapper extends BaseMapper<GTagKeyword> {

	@Select("select keyword from t_g_tag_keyword where tag_id=#{id}")
	List<String> selectKeywords(Integer id);

}
