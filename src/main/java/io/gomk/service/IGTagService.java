package io.gomk.service;

import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.extension.service.IService;

import io.gomk.controller.request.FormulaVO;
import io.gomk.controller.response.TagDetailVO;
import io.gomk.framework.utils.tree.TreeDto;
import io.gomk.model.GTag;
import io.gomk.model.GTagKeyword;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Robinxiao
 * @since 2019-10-01
 */
public interface IGTagService extends IService<GTag> {

	void addDocTag(int scope, String tagName, List<String> ids) throws Exception;

	int getCountByTagName(String name);

	List<TreeDto> getTreeByScope(Integer scope);

	List<GTag> getTagBySecondId(Integer id);

	List<TreeDto> getAllTree();

	void saveTagForKeyword(GTag entity, GTagKeyword tagKeyword) throws Exception;

	void saveTagForFormula(GTag entity, Set<FormulaVO> formulaSet) throws Exception;

	TagDetailVO getTagDetail(GTag tag);

	List<String> getCompletion(int size, String keyWord);

	void deleteDocTag(int scope, String tagName, String id) throws Exception;

}
