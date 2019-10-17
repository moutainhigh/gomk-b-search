package io.gomk.mapper;

import io.gomk.framework.utils.tree.TreeDto;
import io.gomk.model.GTag;

import java.util.List;
import java.util.Set;

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
	@Select(" select concat('T',tgt.id) as id, tgt.tag_name as name, tgt.classify_id as parentId from t_g_tag tgt " + 
			" inner join t_g_tag_classify tgtc on tgt.classify_id = tgtc.id" + 
			" inner join t_g_tag_classify_scope tgtcs on tgtc.id = tgtcs.classify_id" + 
			" where tgtcs.scopes =#{scope}")
	List<TreeDto> selectByScope(Integer scope);
	@Select("select * from t_g_tag where classify_id = #{id}")
	List<GTag> getTagBySecondId(Integer id);
	
	List<GTag> selectTagByNames(Set<String> tagSet);
	
	@Select("select concat('T',tgt.id) as id, tgt.tag_name as name, tgt.classify_id as parentId  from t_g_tag tgt")
	List<TreeDto> selectAllTag();

}
