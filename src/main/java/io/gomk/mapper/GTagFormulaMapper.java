package io.gomk.mapper;

import io.gomk.model.GTagFormula;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Robinxiao
 * @since 2019-10-20
 */
public interface GTagFormulaMapper extends BaseMapper<GTagFormula> {
	
	@Select("select * from t_g_tag_formula where tag_id=#{tagId}")
	List<GTagFormula> selectByTagId(Integer tagId);

}
