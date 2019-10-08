package io.gomk.mapper;

import io.gomk.framework.utils.tree.TreeDto;
import io.gomk.model.GTagClassify;

import java.util.List;

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
public interface GTagClassifyMapper extends BaseMapper<GTagClassify> {

	@Select("select tgtc.id as id, tgtc.classify_name as name, tgtc.parent_id as parentId from t_g_tag_classify tgtc "
			+ "inner join t_g_tag_classify_scope tgtcs  on tgtcs.classify_id=tgtc.id " + 
			" where tgtcs.scopes=#{scope}")
	List<TreeDto> selectByScope(Integer scope);
	@Select("select tgtc.id as id, tgtc.classify_name as name, tgtc.parent_id as parentId from t_g_tag_classify tgtc ")
	List<TreeDto> selectAllClassify();

}
