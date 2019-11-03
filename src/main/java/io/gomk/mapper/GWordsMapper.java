package io.gomk.mapper;

import io.gomk.model.GWords;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 词库 Mapper 接口
 * </p>
 *
 * @author Robinxiao
 * @since 2019-11-01
 */
public interface GWordsMapper extends BaseMapper<GWords> {

	@Select("select * from t_g_words where words =#{words}")
	GWords getByWords(String words);

}
