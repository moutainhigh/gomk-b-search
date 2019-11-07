package io.gomk.mapper;

import io.gomk.model.GTagClassifyScope;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Robinxiao
 * @since 2019-10-23
 */
@DS("oneself")
public interface GTagClassifyScopeMapper extends BaseMapper<GTagClassifyScope> {

	@Select("select scopes from t_g_tag_classify_scope where classify_id=#{id}")
	List<Integer> selectScopeByClassifyId(Integer id);

}
