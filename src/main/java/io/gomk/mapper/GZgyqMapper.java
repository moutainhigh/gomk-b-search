package io.gomk.mapper;

import io.gomk.controller.response.SearchResultVO;
import io.gomk.model.GZgyq;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 资格要求条目表 Mapper 接口
 * </p>
 *
 * @author Robinxiao
 * @since 2019-10-28
 */
public interface GZgyqMapper extends BaseMapper<GZgyq> {
	@Select("select content from t_g_zgyq where content like '%${keyword}%' order by amount desc limit #{size}")
	List<String> selectTopInfo(@Param("keyword")String keyword, @Param("size")int size);

}
