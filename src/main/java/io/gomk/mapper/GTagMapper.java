package io.gomk.mapper;

import io.gomk.framework.utils.tree.TreeDto;
import io.gomk.model.GTag;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Robinxiao
 * @since 2019-10-01
 */
@DS("oneself")
public interface GTagMapper extends BaseMapper<GTag> {

	@Select("select count(id) from t_g_tag where tag_name = #{name} and classify_id = #{classifyId}")
	int getCountByTagName(@Param("name") String name, @Param("classifyId")Integer tagCustomClassifySecondId);
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
	
	@Select("select tag_name from t_g_tag where classify_id =21 and tag_name like '%${name}%' limit #{size}")
	List<String> getCompletion(@Param("size")int size, @Param("name")String name);
	
	@Select("select * from t_g_tag where tag_rule = #{tagRole}")
	List<GTag> getAllFixedTag(Integer tagRole);

}
