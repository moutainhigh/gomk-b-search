package io.gomk.mapper;

import io.gomk.model.GCompletion;

import java.util.List;

import org.apache.ibatis.annotations.Param;
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
public interface GCompletionMapper extends BaseMapper<GCompletion> {

	@Select("select words from t_g_completion where words like '%${keyWord}%' limit #{size}")
	List<String> getConmpletion(@Param("size")int size, @Param("keyWord")String keyWord);
	@Select("select words from t_g_completion where words like '%${keyWord}%' limit #{from}, #{to}")
	List<String> getBdw(@Param("from")int from, @Param("to")int to, @Param("keyWord")String keyWord);
	@Select("select count(*) from t_g_completion where words like '%${keyWord}%'")
	int countBdw(@Param("keyWord")String keyWord);

}
